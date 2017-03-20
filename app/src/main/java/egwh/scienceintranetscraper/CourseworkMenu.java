package egwh.scienceintranetscraper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by eghar on 20/03/2017.
 */

public class CourseworkMenu extends Activity implements View.OnClickListener {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursework_menu);

        // Init button
        Button homeButton = (Button)findViewById(R.id.coursework_menu_home_button);
        homeButton.setOnClickListener(this);
        Button cCWButton = (Button)findViewById(R.id.current_cw_button);
        cCWButton.setOnClickListener(this);
        Button rCWButton = (Button)findViewById(R.id.received_cw_button);
        rCWButton.setOnClickListener(this);
        Button fCWButton = (Button)findViewById(R.id.future_cw_button);
        fCWButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.coursework_menu_home_button:
                finish();
                break;
            case R.id.current_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra("type", "c");
                startActivity(intent);
                break;
            case R.id.received_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra("type", "r");
                startActivity(intent);
                break;
            case R.id.future_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra("type", "f");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
