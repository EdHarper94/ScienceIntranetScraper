package egwh.scienceintranetscraper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;

/**
 * Created by eghar on 19/03/2017.
 */

public class CurrentCWTableGenerator {

    private Context context;
    private TableLayout tl;

    private ArrayList<CurrentCoursework> courseworks = new ArrayList<>();
    private ArrayList<String> headings = new ArrayList<>();

    public CurrentCWTableGenerator(Context context, TableLayout tl, ArrayList<CurrentCoursework> courseworks, ArrayList<String> headings){
        this.context = context;
        this.tl = tl;
        this.courseworks = courseworks;
        this.headings = headings;
    }

    /**
     * Sorts CurrentCourseworks and generates their table
     * @return the generated TableLayout
     * @see CurrentCoursework
     */
    public TableLayout generateCWTable(){

        TableRow row = new TableRow(context);

        // Init layout params (same for all Coursework table gens)
        TableRow.LayoutParams lp = new TableRow.LayoutParams(300, 200);
        lp.setMargins(5,10,5,10);

        // Add all headings to heading row
        for(int j=0; j<headings.size(); j++){

            Log.d("headings", headings.get(j));
            tl.removeView(row);
            TextView header = new TextView(context);
            header.setText(headings.get(j));
            header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header.setGravity(CENTER);
            row.addView(header);

        }
        // Add headings to table
        tl.addView(row);

        // Add a row for each coursework
        for(int i=0; i<courseworks.size(); i++){
            row = new TableRow(context);

            // Add module text
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

            // Add deadline date
            TextView dDate = new TextView(context);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = df.format(courseworks.get(i).getDeadlineDate());
            dDate.setText(date);
            dDate.setLayoutParams(lp);
            dDate.setGravity(CENTER);

            // Add feedback date
            TextView fDate = new TextView(context);
            df = new SimpleDateFormat("dd/MM/yyyy");
            date = df.format(courseworks.get(i).getFeedbackDate());
            fDate.setText(date);
            fDate.setLayoutParams(lp);
            fDate.setGravity(CENTER);

            // Add Views to row
            row.addView(moduleCode);
            row.addView(lecturer);
            row.addView(title);
            row.addView(dDate);
            row.addView(fDate);

            // Add rows to table
            tl.addView(row);

        }
        // Return the generated table
        return tl;
    }
}
