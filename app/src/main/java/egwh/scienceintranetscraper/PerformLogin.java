package egwh.scienceintranetscraper;

import android.content.Context;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;


/**
 * Created by eghar on 09/03/2017.
 */

public class PerformLogin {
    final String baseUrl = "https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/";
    final String loginUrl = "https://science.swansea.ac.uk/intranet/accounts/login/";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    final private String next = "/intranet/";

    private String crsftoken;
    private Map<String, String> cookies;

    public Map<String, String> performLogin() {
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
                    .data("username", "")
                    .data("password", "")
                    .data("/next/", next)
                    .userAgent(userAgent)
                    .referrer("https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/")
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();

            // Debug code //
            Log.d("RESPONSE CODE: ", Integer.toString(loginReq.statusCode()));
            //Document htmlDoc = loginReq.parse();

            //Get new cookies after login
            cookies = loginReq.cookies();


            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d("COOOOKIIIESSSSS", key + value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookies;
    }
}