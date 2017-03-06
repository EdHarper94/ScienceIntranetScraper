package egwh.scienceintranetscraper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Time;
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

    private Intent intent;
    private String username;
    private String password;
    private String crsftoken;
    private Map<String, String> cookies;

    private Elements els;
    private Element table;

    private ArrayList<Lecture> lectures = new ArrayList<Lecture>();
    private ArrayList<String> days = new ArrayList();

    TableLayout tl;

    public TimetableScraper(){
    }

    /*
    public void performLoginProcess(){
        new performLogin().execute();
    }*/

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture_timetable);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        //tl = (TableLayout)findViewById(R.id.lecture_timetable);
        new performLogin().execute();
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

                table = htmlDoc.getElementById("timetable");

                Elements els = table.getElementsByClass("day");
                for(Element e: els){
                    days.add(e.text());
                }

                // Loop through table to gather lecture info
                for (Element e : table.select("div.slot")){

                   // if(!"".equals(e.select("strong").text())){
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


                        Lecture l = new Lecture(module, lecturer, room, day, hour, duration);
                        lectures.add(l);
                    //}
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }


        // Outputting Scrapped data to UI
        protected void onPostExecute(Void result){
            // ** DEBUG CODE ** //
            for(Lecture l : lectures){
                Log.d("Lectures: ", l.toString());
            }

            //Initialise table
            tl = (TableLayout)findViewById(R.id.lecture_timetable);
            // Set default row count (random number)
            int rowCount = -1;

            LayoutInflater inflater = getLayoutInflater();

            TableRow row = new TableRow(TimetableScraper.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT);
            //row.setWeightSum(1);

            //Add Headers to table
            TextView header = new TextView(TimetableScraper.this);
            header.setText(" ");
            row.addView(header);
            tl.addView(row);

            for(int j=0; j<days.size(); j++){
                tl.removeView(row);
                header = new TextView(TimetableScraper.this);
                header.setText(days.get(j));
                row.addView(header);
            }
            tl.addView(row);

            // Add Lectures to table
            for(int i=0; i<lectures.size(); i++){

                    if((lectures.get(i).getHour()== rowCount)) {

                        tl.removeView(row);
                        View lv = (View) inflater.inflate(R.layout.lecture_view, row, false);
                        TextView moduleCode = (TextView) lv.findViewById(R.id.moduleCode);
                        moduleCode.setText(lectures.get(i).getModuleCode());
                        row.addView(lv);
                        tl.addView(row);

                    }else {

                        rowCount = lectures.get(i).getHour();
                        row = new TableRow(TimetableScraper.this);
                        row.setLayoutParams(lp);

                        TextView hour = new TextView(TimetableScraper.this);
                        hour.setText(Integer.toString(lectures.get(i).getHour()));
                        hour.setHeight(200);

                        View lv = (View) inflater.inflate(R.layout.lecture_view, row, false);
                        TextView moduleCode = (TextView) lv.findViewById(R.id.moduleCode);
                        moduleCode.setText(lectures.get(i).getModuleCode());
                        row.addView(hour);
                        row.addView(lv);
                        tl.addView(row);
                    }

            }

        }
    }

}
