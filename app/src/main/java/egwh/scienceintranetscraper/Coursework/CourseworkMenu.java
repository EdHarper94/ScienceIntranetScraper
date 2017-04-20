package egwh.scienceintranetscraper.Coursework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import egwh.scienceintranetscraper.R;

/**
 * @file CourseworkMenu.java
 * @author Ed Harper
 * @date 20/03/2017
 *
 * Coursework menu activity
 */

public class CourseworkMenu extends Activity implements View.OnClickListener {

    private Intent intent;

    // Coursework type Variables
    private final String TYPE = CourseworkGlobals.TYPE;
    private final String CURRENT_CW = CourseworkGlobals.CURRENT_CW;
    private final String RECEIVED_CW = CourseworkGlobals.RECEIVED_CW;
    private final String FUTURE_CW = CourseworkGlobals.FUTURE_CW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursework_menu);

        // Init buttons
        Button homeButton = (Button)findViewById(R.id.coursework_menu_home_button);
        homeButton.setOnClickListener(this);
        Button cCWButton = (Button)findViewById(R.id.current_cw_button);
        cCWButton.setOnClickListener(this);
        Button rCWButton = (Button)findViewById(R.id.received_cw_button);
        rCWButton.setOnClickListener(this);
        Button fCWButton = (Button)findViewById(R.id.future_cw_button);
        fCWButton.setOnClickListener(this);

    }

    /**
     * Onclick for all buttons
     * @param view
     */
    @Override
    public void onClick(View view){

        // Switch on clicked button
        switch (view.getId()){
            case R.id.coursework_menu_home_button:
                finish();
                break;
            case R.id.current_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra(TYPE, CURRENT_CW);
                startActivity(intent);
                break;
            case R.id.received_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra(TYPE, RECEIVED_CW);
                startActivity(intent);
                break;
            case R.id.future_cw_button:
                intent = new Intent("com.egwh.scienceintranetscraper.CourseworkScraper");
                intent.putExtra(TYPE, FUTURE_CW);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
