package egwh.scienceintranetscraper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

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

    final String ttUrl = "https://science.swansea.ac.uk/intranet/attendance/timetable";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private Map<String, String> cookies;

    private Element table;

    private ArrayList<Lecture> lectures = new ArrayList<Lecture>();
    private ArrayList<String> days = new ArrayList();

    private TableLayout tl;
    private static int weeksFromToday = 0;

    private Context context = TimetableScraper.this;
    private ProgressDialog pd;
    private CookieStorage cookieStorage = new CookieStorage();

    public TimetableScraper(){
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_scraper);

        // Initialise TableLayout for timetable
        tl = (TableLayout)findViewById(R.id.lecture_timetable);
        new scrapeTimetable().execute("");

        Button homeButton = (Button)findViewById(R.id.home_button);
        Button nextButton = (Button)findViewById(R.id.next_week_button);
        Button prevButton = (Button)findViewById(R.id.previous_week_button);

        homeButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                goBack();
            }
        });

        nextButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                weeksFromToday = weeksFromToday +1;
                String date = getWeek(weeksFromToday);
                onResume(date);
                Toast.makeText(context, getWeek(weeksFromToday), Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weeksFromToday = weeksFromToday -1;
                String date = getWeek(weeksFromToday);
                onResume(date);
                Toast.makeText(context, getWeek(weeksFromToday), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onResume(String date){
        super.onResume();

        new scrapeTimetable().execute(date);
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
    private class scrapeTimetable extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute(){
            pd = new ProgressDialog(context);
            pd.setMessage("Fetching Lectures...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected Void doInBackground(String...params){
            try{

                //Get passed date
                String date = params[0];
                //Update timetable URL.
                String newUrl = ttUrl + date;

                // Get cookies from cookie store
                cookies = cookieStorage.getCookiesMap(ttUrl);

                // Grab timetable page
                Document htmlDoc = Jsoup
                        .connect(newUrl)
                        .userAgent(userAgent)
                        .referrer("https://science.swansea.ac.uk/intranet/")
                        .cookies(cookies)
                        .get();

                // Grab timetable from html document
                table = htmlDoc.getElementById("timetable");

                //Get day headings
                Elements els = table.getElementsByClass("day");
                //Add to days array
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
            //Initliase timetable.
            TimetableGenerator ttg = new TimetableGenerator(context, tl, lectures,days);
            //Create table.
            ttg.generateTable();

            //If progress is still showing dismiss.
            if(pd.isShowing()){
                pd.dismiss();
            }

        }
    }

    private void goBack(){
        finish();
    }


}
