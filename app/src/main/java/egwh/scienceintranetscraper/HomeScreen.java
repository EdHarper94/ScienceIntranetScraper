package egwh.scienceintranetscraper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by eghar on 17/03/2017.
 */

public class HomeScreen extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Button tTButton = (Button)findViewById(R.id.timetable_button);
        Button cWButton = (Button)findViewById(R.id.coursework_button);

        tTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.egwh.scienceintranetscraper.TimetableScraper");
                startActivity(intent);
            }
        });

        cWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.egwh.scienceintranetscraper.CourseworkMenu");
                startActivity(intent);
            }
        });

    }
}
