package egwh.scienceintranetscraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Start login screen
        Intent login = new Intent("com.egwh.scienceintranetscraper.Login");
        startActivity(login);

        /*
        // DEBUG CODE (development)
        PerformLogin pl = new PerformLogin("", "", new PerformLogin.PerformLoginResponse() {
            @Override
            // Check the result
            public void loginFinished(String result) {
                if (result.equals("LOGIN_FAIL")) {
                    Toast.makeText(getApplicationContext(), "Incorrect username/password. Please try again.", Toast.LENGTH_SHORT).show();
                } else if (result.equals("CONNECTION_FAIL")){
                    Toast.makeText(getApplicationContext(), "Currently unable to connect to server. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent("com.egwh.scienceintranetscraper.StaffHoursScraper");
                    startActivity(intent);
                }
            }});
        pl.execute();
        */
    }
}



