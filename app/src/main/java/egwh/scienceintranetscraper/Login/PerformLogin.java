package egwh.scienceintranetscraper.Login;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;


/**
 * @file PerformLogin.java
 * @author Ed Harper
 * @date 09/03/2017
 *
 * Performs login and gets cookies for cookie store
 */

public class PerformLogin extends AsyncTask<Void, Void, String> {

    /**
     * Perform login interface to receive result of async task
     */
    public interface PerformLoginResponse {
        void loginFinished(String result);
    }

    public PerformLoginResponse delegate = null;

    private final String BASE_URL = "https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/";
    private final String LOGIN_URL = "https://science.swansea.ac.uk/intranet/accounts/login/";
    private final String CHECK_URL = "https://science.swansea.ac.uk/intranet/";
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    private final String NEXT = "/intranet/";

    private final String CONNECTION_FAIL = "CONNECTION_FAIL";
    private final String LOGIN_FAIL = "LOGIN_FAIL";
    private final String SUCCESS = "SUCCESS";

    private final String CSRF_TOKEN = "csrftoken";

    private String csrftoken;
    private Map<String, String> cookies;

    private CookieStorage cookieStorage = new CookieStorage();

    private String username;
    private String password;

    private Exception exception;
    private int statusCode;
    private String loginCheck;

    public PerformLogin(String username, String password, PerformLoginResponse delegate){
        this.username = username;
        this.password = password;
        this.delegate = delegate;
    }

    /**
     * Perform login and store cookies
     * @param params
     * @return
     */
    public String doInBackground(Void...params){

        try {
            // HTTP Get request
            Connection.Response getReq = Jsoup
                    .connect(BASE_URL)
                    .method(Connection.Method.GET)
                    .execute();

            // Store cookies
            cookies = getReq.cookies();

            // Strip crsftoken
            csrftoken = cookies.get(CSRF_TOKEN);
            // Login Request
            Connection.Response loginReq = Jsoup
                    .connect(LOGIN_URL)
                    .data("csrfmiddlewaretoken", csrftoken)
                    .data("username", username)
                    .data("password", password)
                    .data("/next/", NEXT)
                    .userAgent(USER_AGENT)
                    .referrer(LOGIN_URL)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();

            // Get Status code
            statusCode = loginReq.statusCode();

            //Store cookies
            cookies = loginReq.cookies();
            cookieStorage.storeCookies(cookies, LOGIN_URL);
            // DEBUG CODE
            System.out.println(cookies);

            Document checkSuccess = Jsoup
                    .connect(CHECK_URL)
                    .userAgent(USER_AGENT)
                    .referrer(LOGIN_URL)
                    .cookies(cookies)
                    .get();

            // Check that user is now logged in
            Elements el = checkSuccess.select("#logout");
            loginCheck = el.toString();

        }
        catch (Exception e){
            this.exception = e;
        }
        return null;
    }

    /**
     * Check result of doInBackground and pass result to Login.java
     * @param result
     */
    protected void onPostExecute(String result){
        if(exception != null){
            cookieStorage.removeCookies();
            result = CONNECTION_FAIL;
        }
        else if(statusCode != 200){
            cookieStorage.removeCookies();
            result = CONNECTION_FAIL;
        }
        else if(!loginCheck.contains("Logged in as")){
            cookieStorage.removeCookies();
            result = LOGIN_FAIL;
        }else{
            result = SUCCESS;
        }
        // Set result of async task
        delegate.loginFinished(result);
    }
}