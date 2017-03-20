package egwh.scienceintranetscraper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by eghar on 17/03/2017.
 *
 * Connects to Science intranet coursework page and scrapes the data from the tables, stores them
 * and displays them to the UI.
 */

public class CourseworkScraper extends Activity {
    final String url = "https://science.swansea.ac.uk/intranet/submission/coursework";
    final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private Map<String, String> cookies;

    private Element table;

    private ArrayList<String> headings = new ArrayList<>();

    // Arrays containing Different types of courseworks //
    private ArrayList<CurrentCoursework> cCourseworks = new ArrayList<>();
    private ArrayList<ReceivedCoursework> rCourseworks = new ArrayList<>();
    private ArrayList<FutureCoursework> fCourseworks = new ArrayList<>();

    private TableLayout tl;

    private Context context = CourseworkScraper.this;
    private PerformLogin pl;
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
        type = intent.getStringExtra("type");

    }

    /**
     * Gets 'current' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    private void currentCoursework(Element page){
        table = page.select("table").get(0);
        Log.d("table: ", table.text());

        // Get headings
        Elements heading = table.select("th");
        for(Element e : heading){
            headings.add(e.text());
        }

        // Select rows
        Elements rows = table.select("tr");

        // Loop through rows
        for(int i=1; i<rows.size(); i++){
            Log.d("ROWS: ", rows.get(i).text());
            // Select cells
            Elements cells = rows.get(i).select("td");

            //Loop through each cell on each row
            for(int j=0; j<cells.size(); j++){
                if(j==0){
                    moduleCode = cells.get(j).text();
                }else if(j==1){
                    lecturer = cells.get(j).text();
                }else if(j==2){
                    title = cells.get(j).text();
                }else if(j==3){
                    // Get deadline Date
                    try {
                        String ddate = cells.get(j).text();
                        dd = formatDateTime(ddate);
                    }catch(java.text.ParseException e){
                        e.printStackTrace();
                    }
                }else if(j==4){
                    try {
                        // Get feedback date
                        String fdate = cells.get(j).text();
                        fd = formatDate(fdate);
                        Log.d("FEEDBACK", fdate);
                    } catch(java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Create new coursework object with scraped data
            CurrentCoursework cw = new CurrentCoursework(lecturer, fd, moduleCode, title, dd);
            // Add to array
            cCourseworks.add(cw);
        }
    }

    /**
     * Gets 'received' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    protected void receivedCoursework(Element page){
        table = page.select("table").get(1);

        // Get headings
        Elements heading = table.select("th");
        for(Element e : heading){
            headings.add(e.text());
        }

        // Select rows
        Elements rows = table.select("tr");

        // Loop through rows
        for(int i=1; i<rows.size(); i++){
            Log.d("ROWS: ", rows.get(i).text());

            // Select cells
            Elements cells = rows.get(i).select("td");

            //Loop through each cell on each row
            for(int j=0; j<cells.size(); j++){
                if(j==0){
                    moduleCode = cells.get(j).text();
                }else if(j==1){
                    title = cells.get(j).text();
                }else if(j==2){
                    // Get deadline Date
                    try {
                        String ddate = cells.get(j).text();
                        dd = formatDate(ddate);
                    }catch(java.text.ParseException e){
                        e.printStackTrace();
                    }
                }else if(j==3){
                    // Get received on time identifier and trim
                    String identifier = cells.get(j).text().trim();
                    if(identifier.equals("On time")){
                        received = ReceivedCoursework.Received.ON_TIME;
                    }else if(identifier.equals("Late")){
                        received = ReceivedCoursework.Received.LATE;
                    }else{
                        received = ReceivedCoursework.Received.NO;
                    }
                }else if(j==4){
                    // Get feedback Date
                    try {
                        String fdate = cells.get(j).text();
                        fd = formatDate(fdate);
                        Log.d("FEEDBACK", fdate);
                    } catch(java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Create new coursework object with scraped data
            ReceivedCoursework cw = new ReceivedCoursework(received, fd, moduleCode, title, dd);
            // Add to array
            rCourseworks.add(cw);
        }
    }

    /**
     * Gets 'future' courseworks from scraped HTML page.
     * @param page the scraped html page.
     * @return an array of courseworks
     */
    private void futureCoursework(Element page){
        table = page.select("table").get(2);

        // Get headings
        Elements heading = table.select("th");
        for(Element e : heading){
            headings.add(e.text());
        }

        // Select rows
        Elements rows = table.select("tr");

        // Loop through rows
        for(int i=1; i<rows.size(); i++){
            Log.d("ROWS: ", rows.get(i).text());
            // Select cells
            Elements cells = rows.get(i).select("td");

            // Loop through each cell on each row
            for(int j=0; j<cells.size(); j++){
                if(j==0){
                    moduleCode = cells.get(j).text();
                }else if(j==1){
                    lecturer = cells.get(j).text();
                }else if(j==2){
                    title = cells.get(j).text();
                }else if(j==3){
                    // Get setdate
                    try {
                        String sDate = cells.get(j).text();;
                        sd = formatDate(sDate);
                    }catch(java.text.ParseException e){
                        e.printStackTrace();
                    }
                }else if(j==4){
                    // Get deadline Date
                    try {
                        String ddate = cells.get(j).text();
                        dd = formatDateTime(ddate);
                    }catch(java.text.ParseException e){
                        e.printStackTrace();
                    }
                }
            }
            // Create new coursework object with scraped data
            FutureCoursework cw = new FutureCoursework(lecturer, sd, moduleCode, title, dd);
            // Add to array
            fCourseworks.add(cw);
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

        protected void onPreExecute(){
            pd = new ProgressDialog(context);
            pd.setMessage("Fetching Courseworks...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        protected Void doInBackground(Void... params) {
            try {
                // Init login.
                pl = new PerformLogin();
                // Perform login and store cookies
                cookies = pl.performLogin();

                // Scrape the html page.
                Document htmlDoc = Jsoup
                        .connect(url)
                        .userAgent(userAgent)
                        .cookies(cookies)
                        .referrer("https://science.swansea.ac.uk/intranet/")
                        .get();

                Log.d("RESPONSE", htmlDoc.text());

                // Trim the content
                Element page = htmlDoc.getElementById("pagecontent");

                if(page != null){
                    Log.d("Page:", page.text());
                }

                // Coursework we are looking for
                if(type.equals("c")){
                    currentCoursework(page);
                }else if(type.equals("r")){
                    receivedCoursework(page);
                }else if(type.equals("f")){
                    futureCoursework(page);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Update UI Table.
         * command is the type of coursework.
         * @param result
         */
        protected void onPostExecute(Void result){
            if(type.equals("c")){
                CurrentCWTableGenerator ctg = new CurrentCWTableGenerator(context, tl, cCourseworks, headings);
                ctg.generateCWTable();
            }else if(type.equals("r")){
                ReceivedCWTableGenerator rtg = new ReceivedCWTableGenerator(context, tl, rCourseworks, headings);
                rtg.generateCWTable();
            }else if(type.equals("f")){
                FutureCWTableGenerator ftg = new FutureCWTableGenerator(context, tl, fCourseworks, headings);
                ftg.generateCWTable();
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
