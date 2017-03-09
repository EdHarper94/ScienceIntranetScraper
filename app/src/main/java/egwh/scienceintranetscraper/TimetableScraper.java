package egwh.scienceintranetscraper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private ArrayList<String> days = new ArrayList();

    private TableLayout tl;
    private Calendar c;
    private static int weeksFromToday = 0;

    public TimetableScraper(){
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_scraper);

        //Get data from parent intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // Initialise TableLayout for timetable
        tl = (TableLayout)findViewById(R.id.lecture_timetable);
        new performLogin().execute("");


        Button nextButton = (Button)findViewById(R.id.next_week_button);
        Button prevButton = (Button)findViewById(R.id.previous_week_button);


        nextButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                weeksFromToday = weeksFromToday +1;
                String date = getWeek(weeksFromToday);
                onResume(date);
                Toast.makeText(TimetableScraper.this, getWeek(weeksFromToday), Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weeksFromToday = weeksFromToday -1;
                String date = getWeek(weeksFromToday);
                onResume(date);
                Toast.makeText(TimetableScraper.this, getWeek(weeksFromToday), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onResume(String date){
        super.onResume();

        new performLogin().execute(date);
        // Clear table once loaded.
        tl.invalidate();
        tl.removeAllViews();
    }

    // Gets week
    // weeksFromToday - Static int keeping track of weeks from current day.
    private static String getWeek(int weeksFromToday){
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR)+ weeksFromToday);

        SimpleDateFormat df = new SimpleDateFormat("/yyyy/MM/dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    // Performs log in and grabs data.
    private class performLogin extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String...params){
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


                String date = params[0];
                String newUrl = ttUrl + date;
                Log.d("DATEEEEEEEEEEEEE", newUrl);

                // Grab timetable page
                Document htmlDoc = Jsoup
                        .connect(newUrl)
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

            // Set default row count (random number)
            int rowCount = -1;

            LayoutInflater inflater = getLayoutInflater();

            TableRow row = new TableRow(TimetableScraper.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //Add Headers to table
            TextView header = new TextView(TimetableScraper.this);
            header.setText(" ");
            row.addView(header);
            tl.addView(row);

            for(int j=0; j<days.size(); j++){
                tl.removeView(row);
                header = new TextView(TimetableScraper.this);
                header.setText(days.get(j));
                header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(header);
            }
            tl.addView(row);

            // Add Lectures to table
            for(int i=0; i<lectures.size(); i++){

                    if((lectures.get(i).getHour()== rowCount)) {

                        tl.removeView(row);

                        //Inflate view. See lecture_view.xml
                        View lv = (View) inflater.inflate(R.layout.lecture_view, row, false);

                        //Add Module code info
                        TextView moduleCode = (TextView) lv.findViewById(R.id.moduleCode);
                        moduleCode.setText(lectures.get(i).getModuleCode());

                        // Add lecturer info
                        TextView lecturer = (TextView) lv.findViewById(R.id.lecturer);
                        lecturer.setText(lectures.get(i).getLecturer());

                        //Add room info
                        TextView room = (TextView) lv.findViewById(R.id.room);
                        Log.d("ROOMS: ", lectures.get(i).getRoom());
                        room.setText(lectures.get(i).getRoom());

                        row.addView(lv);
                        tl.addView(row);

                    }else {

                        rowCount = lectures.get(i).getHour();
                        row = new TableRow(TimetableScraper.this);
                        row.setLayoutParams(lp);

                        // Add row header.
                        TextView hour = new TextView(TimetableScraper.this);
                        hour.setText(Integer.toString(lectures.get(i).getHour()));
                        hour.setHeight(200);

                        //Inflate view. See lecture_view.xml
                        View lv = (View) inflater.inflate(R.layout.lecture_view, row, false);

                        //Add Module code info
                        TextView moduleCode = (TextView) lv.findViewById(R.id.moduleCode);
                        moduleCode.setText(lectures.get(i).getModuleCode());

                        // Add lecturer info
                        TextView lecturer = (TextView) lv.findViewById(R.id.lecturer);
                        lecturer.setText(lectures.get(i).getLecturer());

                        //Add room info
                        TextView room = (TextView) lv.findViewById(R.id.room);
                        Log.d("ROOMS: ", lectures.get(i).getRoom());
                        room.setText(lectures.get(i).getRoom());

                        //Add row
                        row.addView(hour);
                        row.addView(lv);
                        tl.addView(row);
                    }

            }
            // Clear arrays once added to calendar.
            lectures.clear();
            days.clear();
        }
    }



}
