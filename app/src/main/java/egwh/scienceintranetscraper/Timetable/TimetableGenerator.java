package egwh.scienceintranetscraper.Timetable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import egwh.scienceintranetscraper.R;

/**
 * Created by eghar on 09/03/2017.
 */

public class TimetableGenerator {

    private Context context;
    private TableLayout tl;
    private ArrayList<Lecture> lectures = new ArrayList<Lecture>();
    private ArrayList<String> days = new ArrayList();

    public TimetableGenerator(Context context, TableLayout tl, ArrayList<Lecture> lectures, ArrayList<String> days){
        this.context = context;
        this.tl = tl;
        this.lectures = lectures;
        this.days = days;
    }


    public TableLayout generateTable(){

        // Set default row count (random number)
        int rowCount = -1;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        TableRow row = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //Add Headers to table
        TextView header = new TextView(context);
        header.setText(" ");
        row.addView(header);
        tl.addView(row);

        for(int j=0; j<days.size(); j++){
            tl.removeView(row);
            header = new TextView(context);
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
                row = new TableRow(context);
                row.setLayoutParams(lp);

                // Add row header.
                TextView hour = new TextView(context);
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
        return tl;
    }
}
