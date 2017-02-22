package egwh.scienceintranetscraper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by eghar on 08/02/2017.
 */

public class TimetableScraper extends Activity {

    final String baseUrl = "https://science.swansea.ac.uk/intranet/accounts/login/?next=/intranet/";
    final String loginUrl = "https://science.swansea.ac.uk/intranet/accounts/login/";
    final String ttUrl = "https://science.swansea.ac.uk/intranet/attendance/timetable";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    final private String next = "/intranet/";

    private String username;
    private String password;
    private String crsftoken;
    private Map<String, String> cookies;

    private Elements els;
    private Element table;

    private ArrayList<Lecture> lectures = new ArrayList<Lecture>();

    public TimetableScraper(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void performLoginProcess(){
        new performLogin().execute();
    }

    public void onCreate(Bundle savedInstenceState){

    }

    // Get the cookies required for login
    private class performLogin extends AsyncTask<Void, Void, Void> {

        public Void doInBackground(Void...params){
            try{
                // HTTP Get request
                Connection.Response getReq = Jsoup
                        .connect(baseUrl)
                        .method(Connection.Method.GET)
                        .execute();

                // Store cookies
                cookies = getReq.cookies();
                System.out.println("COOKIES SAVED" + cookies);

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
                System.out.println("RESPONSE CODE: " + loginReq.statusCode());
                //Document htmlDoc = loginReq.parse();


                //Get new cookies after login
                cookies = loginReq.cookies();

                // Grab timetable page
                Document htmlDoc = Jsoup
                        .connect(ttUrl)
                        .userAgent(userAgent)
                        .referrer("https://science.swansea.ac.uk/intranet/")
                        .cookies(cookies)
                        .get();

                // Debug Code //
                //System.out.println(htmlDoc);

                table = htmlDoc.getElementById("timetable");

                // Loop through table to gather lecture info
                for (Element e : table.select("div.slot")){
                    if(!"".equals(e.select("strong").text())){
                        String module = e.select("strong").text();
                        String lecturer = e.select("span").text();
                        String room = e.select("div.lectureinfo.room").text();
                        int day = Integer.parseInt(e.attr("data-day"));
                        int hour = Integer.parseInt(e.attr("data-hour"));
                        String d = e.select("div.lectureinfo.duration").text().replaceAll("\\D+", "");
                        int duration = 1;
                        if(!"".equals(d)) {
                            duration = Integer.parseInt(d);
                        }
                        lectures.add(new Lecture(module, lecturer, room, day, hour, duration));
                    }
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result){
            for(Lecture l : lectures){
                System.out.println(l.toString());
            }
            //System.out.println(els);
        }
    }

}
