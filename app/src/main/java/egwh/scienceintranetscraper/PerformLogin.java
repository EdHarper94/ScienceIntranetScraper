package egwh.scienceintranetscraper;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;


/**
 * Created by eghar on 09/03/2017.
 */

public class PerformLogin extends AsyncTask<Void, Void, Void> {
    final String baseUrl = "https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/";
    final String loginUrl = "https://science.swansea.ac.uk/intranet/accounts/login/";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    final private String next = "/intranet/";

    private String crsftoken;
    private Map<String, String> cookies;

    private CookieStorage cookieStorage = new CookieStorage();

    private String username;
    private String password;

    private Exception exception;

    public PerformLogin(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Void doInBackground(Void...params){

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

            // Debug code //
            Log.d("RESPONSE CODE: ", Integer.toString(loginReq.statusCode()));
            //Document htmlDoc = loginReq.parse();


            cookies = loginReq.cookies();
            //Store cookies
            cookieStorage.storeCookies(cookies, loginUrl);


            System.out.println(cookies);

        } catch (Exception e){
            this.exception = e;
        }
        return null;
    }

    protected void onPostExecute(Void result){
        if(exception != null) {
            System.out.println("ERROR LOGGING IN " + exception);
            exception.printStackTrace();
        }
    }
}