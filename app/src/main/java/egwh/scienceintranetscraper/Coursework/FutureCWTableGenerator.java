package egwh.scienceintranetscraper.Coursework;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;

/**
 * @file FutureCWTableGenerator.java
 * @author Ed Harper
 * @date 19/03/2017
 *
 * Generates Table Layout for passed Future Courseworks
 */

public class FutureCWTableGenerator {

    private Context context;
    private TableLayout tl;

    private ArrayList<FutureCoursework> courseworks = new ArrayList<>();
    private ArrayList<String> headings = new ArrayList<>();

    private final DateFormat DATE_TIME_FORMAT = CourseworkGlobals.DATE_TIME_FORMAT;
    private final DateFormat DATE_FORMAT = CourseworkGlobals.DATE_FORMAT;

    /**
     * Initialises future coursework table generator
     * @param context passed context
     * @param tl passed table layout
     * @param courseworks passed courseworks
     * @param headings passed headings
     */
    public FutureCWTableGenerator(Context context, TableLayout tl, ArrayList<FutureCoursework> courseworks, ArrayList<String> headings){
        this.context = context;
        this.tl = tl;
        this.courseworks = courseworks;
        this.headings = headings;
    }

    /**
     * Sorts FutureCoursework and generates their table
     * @return the generated TableLayout
     * @see FutureCoursework
     */
    public TableLayout generateCWTable(){

        TableRow row = new TableRow(context);

        // Init layout params (same for all Coursework table gens)
        TableRow.LayoutParams lp = new TableRow.LayoutParams(300, 200);
        lp.setMargins(5,10,5,10);

        // Add all headings to row
        for(int j=0; j<headings.size(); j++){
            tl.removeView(row);
            TextView header = new TextView(context);
            header.setText(headings.get(j));
            header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.setGravity(CENTER);
            row.addView(header);
        }
        // Add headings to table
        tl.addView(row);

        // Add a row for each coursework
        for(int i=0; i<courseworks.size(); i++){
            row = new TableRow(context);


            // Add module code
            TextView moduleCode = new TextView(context);
            moduleCode.setText(courseworks.get(i).getModuleCode());
            // Set text view layout params
            moduleCode.setLayoutParams(lp);
            moduleCode.setGravity(CENTER);

            // Add lecturer text
            TextView lecturer = new TextView(context);
            lecturer.setText(courseworks.get(i).getLecturer());
            lecturer.setLayoutParams(lp);
            lecturer.setGravity(CENTER);

            // Add title text
            TextView title = new TextView(context);
            title.setText(courseworks.get(i).getTitle());
            title.setLayoutParams(lp);
            title.setGravity(CENTER);

            // Add set date
            TextView sDate = new TextView(context);
            String date = DATE_FORMAT.format(courseworks.get(i).getSetDate());
            sDate.setText(date);
            sDate.setLayoutParams(lp);
            sDate.setGravity(CENTER);

            // Add deadline date
            TextView dDate = new TextView(context);
            date = DATE_TIME_FORMAT.format(courseworks.get(i).getDeadlineDate());
            dDate.setText(date);
            dDate.setLayoutParams(lp);
            dDate.setGravity(CENTER);

            // Add Views to row
            row.addView(moduleCode);
            row.addView(lecturer);
            row.addView(title);
            row.addView(sDate);
            row.addView(dDate);

            // Add row to table
            tl.addView(row);
        }
        // Return generated table
        return tl;
    }
}
