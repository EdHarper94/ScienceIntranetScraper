package egwh.scienceintranetscraper.Coursework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import egwh.scienceintranetscraper.Login.CookieStorage;
import egwh.scienceintranetscraper.R;

/**
 * Created by eghar on 17/03/2017.
 *
 * Connects to Science intranet coursework page and scrapes the data from the tables, stores them
 * and displays them to the UI.
 */

public class CourseworkScraper extends Activity {
    final String URL = "https://science.swansea.ac.uk/intranet/submission/coursework";
    final String BASE_URL = "https://science.swansea.ac.uk/intranet/";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private final String NO_COURSEWORKS = "No Courseworks available.";
    private final String CONNECTION_ERROR = "Could not connect, Please try again later";

    // HTML scraping selectors
    private final String PAGE_CONTENTS = "pagecontent";
    private final String TABLE = "table";
    private final String TABLE_HEADING = "th";
    private final String TABLE_ROW = "tr";
    private final String TABLE_CELL = "td";
    private final String ON_TIME = "On time";
    private final String LATE = "Late";

    // Coursework type identifiers
    private final String TYPE = CourseworkGlobals.TYPE;
    private final String CURRENT_CW = CourseworkGlobals.CURRENT_CW;
    private final String RECEIVED_CW = CourseworkGlobals.RECEIVED_CW;
    private final String FUTURE_CW = CourseworkGlobals.FUTURE_CW;

    private Map<String, String> cookies;

    private Element table;

    private ArrayList<String> headings = new ArrayList<>();

    // Arrays containing Different types of courseworks //
    private ArrayList<CurrentCoursework> cCourseworks = new ArrayList<>();
    private ArrayList<ReceivedCoursework> rCourseworks = new ArrayList<>();
    private ArrayList<FutureCoursework> fCourseworks = new ArrayList<>();

    private TableLayout tl;

    private Context context = CourseworkScraper.this;
    private CookieStorage cookieStorage = new CookieStorage();
    private ProgressDialog pd;

    // Variables for courseworks //
    private String moduleCode;
    private String lecturer;
    private String title;
    // Deadline Date
    private Date dd;
    // Feedback Date
    private Date fd;
    // Set Date
    private Date sd;
    private ReceivedCoursework.Received received;

    private String type;

    private Exception exception;

    public CourseworkScraper(){
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursework_scraper);

        //Init TableLayout for coursework table
        tl = (TableLayout)findViewById(R.id.coursework_table);
        new scrapeCourseworks().execute();

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);

    }

    /**
     * Gets 'current' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    private void currentCoursework(Element page){
        try {
            table = page.select(TABLE).get(0);

            // Get headings
            Elements heading = table.select(TABLE_HEADING);
            for (Element e : heading) {
                headings.add(e.text());
            }

            // Select rows
            Elements rows = table.select(TABLE_ROW);

            // Loop through rows
            for (int i = 1; i < rows.size(); i++) {
                // Select cells
                Elements cells = rows.get(i).select(TABLE_CELL);

                //Loop through each cell on each row
                for (int j = 0; j < cells.size(); j++) {
                    if (j == 0) {
                        moduleCode = cells.get(j).text();
                    } else if (j == 1) {
                        lecturer = cells.get(j).text();
                    } else if (j == 2) {
                        title = cells.get(j).text();
                    } else if (j == 3) {
                        // Get deadline Date
                        try {
                            String ddate = cells.get(j).text();
                            dd = formatDateTime(ddate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (j == 4) {
                        try {
                            // Get feedback date
                            String fdate = cells.get(j).text();
                            fd = formatDate(fdate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Create new coursework object with scraped data
                CurrentCoursework cw = new CurrentCoursework(lecturer, fd, moduleCode, title, dd);
                // Add to array
                cCourseworks.add(cw);
            }
        }catch(IndexOutOfBoundsException e){
            this.exception = e;
        }
    }

    /**
     * Gets 'received' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    protected void receivedCoursework(Element page){
        try{
            table = page.select(TABLE).get(1);

            // Get headings
            Elements heading = table.select(TABLE_HEADING);
            for(Element e : heading){
                headings.add(e.text());
            }

            // Select rows
            Elements rows = table.select(TABLE_ROW);

            // Loop through rows
            for(int i=1; i<rows.size(); i++) {

                // Select cells
                Elements cells = rows.get(i).select(TABLE_CELL);

                //Loop through each cell on each row
                for (int j = 0; j < cells.size(); j++) {
                    if (j == 0) {
                        moduleCode = cells.get(j).text();
                    } else if (j == 1) {
                        title = cells.get(j).text();
                    } else if (j == 2) {
                        // Get deadline Date
                        try {
                            String ddate = cells.get(j).text();
                            dd = formatDate(ddate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (j == 3) {
                        // Get received on time identifier and trim
                        String identifier = cells.get(j).text().trim();
                        if (identifier.equals(ON_TIME)) {
                            received = ReceivedCoursework.Received.ON_TIME;
                        } else if (identifier.equals(LATE)) {
                            received = ReceivedCoursework.Received.LATE;
                        } else {
                            received = ReceivedCoursework.Received.NO;
                        }
                    } else if (j == 4) {
                        // Get feedback Date
                        try {
                            String fdate = cells.get(j).text();
                            fd = formatDate(fdate);;
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Create new coursework object with scraped data
                ReceivedCoursework cw = new ReceivedCoursework(received, fd, moduleCode, title, dd);
                // Add to array
                rCourseworks.add(cw);
            }
        }catch(IndexOutOfBoundsException e){
            this.exception = e;
        }
    }

    /**
     * Gets 'future' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    private void futureCoursework(Element page){
        try{
            table = page.select(TABLE).get(2);

            // Get headings
            Elements heading = table.select(TABLE_HEADING);
            for(Element e : heading){
                headings.add(e.text());
            }

            // Select rows
            Elements rows = table.select(TABLE_ROW);

            // Loop through rows
            for(int i=1; i<rows.size(); i++) {
                // Select cells
                Elements cells = rows.get(i).select(TABLE_CELL);

                // Loop through each cell on each row
                for (int j = 0; j < cells.size(); j++) {
                    if (j == 0) {
                        moduleCode = cells.get(j).text();
                    } else if (j == 1) {
                        lecturer = cells.get(j).text();
                    } else if (j == 2) {
                        title = cells.get(j).text();
                    } else if (j == 3) {
                        // Get setdate
                        try {
                            String sDate = cells.get(j).text();
                            ;
                            sd = formatDate(sDate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (j == 4) {
                        // Get deadline Date
                        try {
                            String ddate = cells.get(j).text();
                            dd = formatDateTime(ddate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Create new coursework object with scraped data
                FutureCoursework cw = new FutureCoursework(lecturer, sd, moduleCode, title, dd);
                // Add to array
                fCourseworks.add(cw);

            }
        }catch(IndexOutOfBoundsException e){
            this.exception = e;
        }
    }


    /**
     * Formats string into date object
     * @param dateString the date in string
     * @return the formatted date object
     * @throws ParseException
     */
    public Date formatDate(String dateString)throws ParseException{
        DateFormat format2 = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Date date = format2.parse(dateString);
        return date;
    }

    /**
     * Formats string into date & time object
     * @param dateTimeString the date & time string
     * @return the formatted date & time object
     * @throws ParseException
     */
    public Date formatDateTime(String dateTimeString)throws ParseException{
        DateFormat format = new SimpleDateFormat("d MMM yyyy hh:mm ", Locale.ENGLISH);
        Date date = format.parse(dateTimeString);
        return date;
    }

    /**
     * Scrape Intranet coursework page.
     */
    private class scrapeCourseworks extends AsyncTask<Void, Void, Void> {
        private Exception scrapeException;

        protected void onPreExecute(){
            pd = new ProgressDialog(context);
            pd.setMessage("Fetching Courseworks...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        protected Void doInBackground(Void... params) {
            try {

                cookies = cookieStorage.getCookiesMap(BASE_URL);

                // Scrape the html page.
                Document htmlDoc = Jsoup
                        .connect(URL)
                        .userAgent(USER_AGENT)
                        .cookies(cookies)
                        .referrer(BASE_URL)
                        .get();

                // Trim the content
                Element page = htmlDoc.getElementById(PAGE_CONTENTS);

                // Coursework we are looking for
                if(type.equals(CURRENT_CW)){
                    currentCoursework(page);
                }else if(type.equals(RECEIVED_CW)){
                    receivedCoursework(page);
                }else if(type.equals(FUTURE_CW)){
                    futureCoursework(page);
                }

            } catch (Exception e) {
                this.scrapeException = e;
            }
            return null;
        }

        /**
         * Update UI Table.
         * command is the type of coursework.
         * @param result
         */
        protected void onPostExecute(Void result) {
            if (scrapeException != null) {
                Toast.makeText(context, CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                scrapeException.printStackTrace();
            }

            if (exception == null) {
                if (type.equals(CURRENT_CW) && cCourseworks != null) {
                    CurrentCWTableGenerator ctg = new CurrentCWTableGenerator(context, tl, cCourseworks, headings);
                    ctg.generateCWTable();
                } else if (type.equals(RECEIVED_CW) && rCourseworks != null) {
                    ReceivedCWTableGenerator rtg = new ReceivedCWTableGenerator(context, tl, rCourseworks, headings);
                    rtg.generateCWTable();
                } else if (type.equals(FUTURE_CW) && fCourseworks != null) {
                    FutureCWTableGenerator ftg = new FutureCWTableGenerator(context, tl, fCourseworks, headings);
                    ftg.generateCWTable();
                } else {
                    Toast.makeText(context, NO_COURSEWORKS, Toast.LENGTH_LONG).show();
                }
            } else {
                if (exception instanceof IndexOutOfBoundsException) {
                    Toast.makeText(context, NO_COURSEWORKS, Toast.LENGTH_LONG).show();
                } else
                    exception.printStackTrace();
            }

            if(pd.isShowing()){
                pd.dismiss();
            }

        }

    }

    /**
     * Go back to previous menu
     */
    public void goBack(){
        finish();
    }
}
