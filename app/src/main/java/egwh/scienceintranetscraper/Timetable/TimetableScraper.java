package egwh.scienceintranetscraper.Timetable;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import egwh.scienceintranetscraper.Login.CookieStorage;
import egwh.scienceintranetscraper.R;


/**
 * @file TimetableScraper.java
 * @auther Ed Harper
 * @date 08/02/2017
 *
 * Scrapes science intranet timetable and displays it to the UI
 */

public class TimetableScraper extends Activity {

    // Final urls
    final String TT_URL = "https://science.swansea.ac.uk/intranet/attendance/timetable";
    final String BASE_URL = "https://science.swansea.ac.uk/intranet/";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    // Final html parsing selectors
    final String TIMETABLE = "timetable";
    final String DAY = "day";
    final String DIV_SLOT = "div.slot";
    final String STRONG = "strong";
    final String SPAN = "span";
    final String DAY_ATTRIBUTE = "data-day";
    final String HOUR_ATTRIBUTE = "data-hour";
    final String DIV_LECTURE = "div.lectureinfo.duration";

    // Cookie map
    private Map<String, String> cookies;
    // Cookie storage
    private CookieStorage cookieStorage = new CookieStorage();
    private Element table;

    private ArrayList<Lecture> lectures = new ArrayList<Lecture>();
    private ArrayList<String> days = new ArrayList();

    private TableLayout tl;

    // Date variables
    private static int weeksFromToday = 0;
    private static String unformattedDate = "";

    private Context context = TimetableScraper.this;
    private ProgressDialog pd;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_scraper);

        // Initialise TableLayout for timetable
        tl = (TableLayout)findViewById(R.id.lecture_timetable);

        // Excutes table scraping
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
                Toast.makeText(context, unformattedDate, Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weeksFromToday = weeksFromToday -1;
                String date = getWeek(weeksFromToday);
                onResume(date);
                Toast.makeText(context, unformattedDate, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * On resume execute scraping and invalidate previous tablelayout
     * @param date
     * @see - https://developer.android.com/reference/android/app/Activity.html
     */
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

        // Get next week (date + weeks from today)
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR)+ weeksFromToday);
        Date timeNow = c.getTime();

        // Save date for ui
        saveDate(timeNow);

        // Format date
        SimpleDateFormat df = new SimpleDateFormat("/yyyy/MM/dd");
        String formattedDate = df.format(timeNow);
        return formattedDate;
    }

    /**
     * Saves date in format for UI
     * @param date
     */
    private static void saveDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        unformattedDate = df.format(date);
    }

    /**
     * Scrapes time table from science intranet and scrapes html for data formatting
     */
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
                String newUrl = TT_URL + date;

                // Get cookies from cookie store
                cookies = cookieStorage.getCookiesMap(TT_URL);

                // Grab timetable page
                Document htmlDoc = Jsoup
                        .connect(newUrl)
                        .userAgent(USER_AGENT)
                        .referrer(BASE_URL)
                        .cookies(cookies)
                        .get();

                // Parse html
                scrapeHtml(htmlDoc);

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

    /**
     * Parses html and formats into Lecture object
     * @param htmlDoc the html page to parse
     */
    public void scrapeHtml(Document htmlDoc){
        // Grab timetable from html document
        table = htmlDoc.getElementById(TIMETABLE);

        //Get day headings
        Elements els = table.getElementsByClass(DAY);
        //Add to days array
        for(Element e: els){
            days.add(e.text());
        }

        // Loop through table to gather lecture info
        for (Element e : table.select(DIV_SLOT)){

            String module = e.select(STRONG).text();
            String lecturer = e.select(SPAN).text();
            String room = e.select(DIV_SLOT).text();
            int day = Integer.parseInt(e.attr(DAY_ATTRIBUTE));
            int hour = Integer.parseInt(e.attr(HOUR_ATTRIBUTE));
            String d = e.select(DIV_LECTURE).text().replaceAll("\\D+", "");
            int duration = 1;
            if(!"".equals(d)) {
                duration = Integer.parseInt(d);
            }

            Lecture l = new Lecture(module, lecturer, room, day, hour, duration);
            lectures.add(l);
        }
    }

    /**
     * Ends activity lifecycle and resets static variable
     */
    private void goBack(){
        finish();
        weeksFromToday = 0;
    }


}
