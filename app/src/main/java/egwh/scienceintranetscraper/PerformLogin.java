package egwh.scienceintranetscraper;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * Created by eghar on 09/03/2017.
 */

public class PerformLogin extends AsyncTask<Void, Void, String> {

    /**
     * Perform login interface to receive result of async task
     */
    public interface PerformLoginResponse {
        void loginFinished(String result);
    }

    public PerformLoginResponse delegate = null;

    final String baseUrl = "https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/";
    final String loginUrl = "https://science.swansea.ac.uk/intranet/accounts/login/";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    final private String next = "/intranet/";

    final private String CONNECTION_FAIL = "CONNECTION_FAIL";
    final private String LOGIN_FAIL = "LOGIN_FAIL";
    final private String SUCCESS = "SUCCESS";

    private String crsftoken;
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
                    .connect(baseUrl)
                    .method(Connection.Method.GET)
                    .execute();

            // Store cookies
            cookies = getReq.cookies();

            // Strip crsftoken
            crsftoken = cookies.get("csrftoken");
            // Login Request
            Connection.Response loginReq = Jsoup
                    .connect(loginUrl)
                    .data("csrfmiddlewaretoken", crsftoken)
                    .data("username", username)
                    .data("password", password)
                    .data("/next/", next)
                    .userAgent(userAgent)
                    .referrer("https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/")
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();

            // Get Status code
            statusCode = loginReq.statusCode();

            //Store cookies
            cookies = loginReq.cookies();
            cookieStorage.storeCookies(cookies, loginUrl);
            // DEBUG CODE
            System.out.println(cookies);

            Document checkSuccess = Jsoup
                    .connect("https://science.swansea.ac.uk/intranet/")
                    .userAgent(userAgent)
                    .referrer("https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/")
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
        if(statusCode != 200){
            System.out.println("Connection Issue" + statusCode + " Exception " + exception);
            cookieStorage.removeCookies();
            result = CONNECTION_FAIL;
        }
        else if(!loginCheck.contains("Logged in as")){
            System.out.println("Login failed");
            cookieStorage.removeCookies();
            result = LOGIN_FAIL;
        }else{
            result = SUCCESS;
        }
        // Set result of async task
        delegate.loginFinished(result);
    }
}