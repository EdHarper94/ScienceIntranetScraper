package egwh.scienceintranetscraper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

/**
 * @file StaffHoursScraper.java
 * @author Ed Harper
 * @date 20/04/2017
 *
 * Scrapes staff hours html from science intranet and parses to get formatted hours data
 */

public class StaffHoursScraper extends Activity {

    // Final url variables
    private final String BASE_URL = "https://science.swansea.ac.uk/intranet/";
    private final String SCRAPE_URL = "https://science.swansea.ac.uk/intranet/staff_officehours/list";
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private final int TIMOUT_MILS = 10000;
    private final String CONNECTION_ERROR = "Cannot retrieve staff office hours, please try again later.";

    // Final html parsing selector variables
    private final String TABLE = "table";
    private final String TABLE_ROW = "tr";
    private final String TABLE_COLUMN = "td";

    // Final variables for missing office hours
    private final String MISSING_HOURS = "NOT SPECIFIED";
    private final String MISSING_UPDATED = "N/A";

    private Context context = StaffHoursScraper.this;

    private ListView listView;
    private Button backButton;
    private ProgressDialog pd;
    private StaffHoursAdapter staffHoursAdapter;

    // Cookie map
    private Map<String, String> cookies;
    // Init cookie store
    private CookieStorage cookieStorage = new CookieStorage();

    private StaffHours staffHours;
    private ArrayList<StaffHours> staffHoursArrayList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hours_scraper);

        listView = (ListView)findViewById(R.id.staff_hours_list);
        backButton = (Button)findViewById(R.id.back_button);

        // Execute
        new getStaffOfficeHours().execute();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    /**
     * Scrapes staff data and office hours
     */
    private class getStaffOfficeHours extends AsyncTask<Void, Void, Void>{
        private Exception exception;

        protected void onPreExecute(){
            pd = new ProgressDialog(context);
            pd.setMessage("Fetching Staff Office Hours...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                cookies = cookieStorage.getCookiesMap(BASE_URL);

                // Scrape the html page.
                Document htmlDoc = Jsoup
                        .connect(SCRAPE_URL)
                        .userAgent(USER_AGENT)
                        .cookies(cookies)
                        .referrer(BASE_URL)
                        .timeout(TIMOUT_MILS)
                        .get();

                parseHtml(htmlDoc);

                Log.d("RESPONSE", htmlDoc.text());

            }catch(Exception e){
                this.exception = e;
            }

            return null;
        }

        /**
         * Updates UI with scraped data
         * @param params
         */
        @Override
        protected void onPostExecute(Void params){
            // Check for an exception or no staff hours
            if(exception !=null || staffHoursArrayList == null){
                Toast toast = Toast.makeText(context, CONNECTION_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                exception.printStackTrace();
            }else{
                if (staffHoursAdapter == null) {
                    staffHoursAdapter = new StaffHoursAdapter(context, staffHoursArrayList);
                    listView.setAdapter(staffHoursAdapter);
                } else {
                    staffHoursAdapter.notifyDataSetChanged();
                }
            }
            pd.dismiss();
        }
    }

    /**
     * Parse HTML into StaffHours format and store
     * @param htmlDoc the html to parse
     */
    public void parseHtml(Document htmlDoc){
        Element table = htmlDoc.select(TABLE).first();
        Elements rows = table.select(TABLE_ROW);

        // Init empty variables
        String fullName = "";
        String hours = "";
        String update = "";

        // Loop through rows
        for(int i=1; i<rows.size(); i++){
            Element row = rows.get(i);
            Elements cols = row.select(TABLE_COLUMN);
            // Loop through columns
            for(int j=0; j<cols.size(); j++){
                fullName = cols.get(0).text().trim();
                hours = cols.get(1).text();
                update = cols.get(2).text();
            }

            // Split Name
            String[] name = formatName(fullName);

            // Check for missing data
            if(hours.isEmpty()){
                hours = MISSING_HOURS;
            }
            if(update.isEmpty()){
                update = MISSING_UPDATED;
            }
            // Init staff hours object
            staffHours = new StaffHours(name[1], name[2], name[3], hours, update);
            // Store
            staffHoursArrayList.add(staffHours);
        }
    }

    /**
     * Formats html scraped name
     * @param fullName the full passed name to split
     * @return
     */
    public String[] formatName(String fullName){
        String[] name = fullName.split("\\s+");
        return name;
    }

    /**
     * Calls finish
     */
    public void goBack(){
        finish();
    }
}
