package egwh.scienceintranetscraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    static final int LOGIN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoginCredentials();
    }

    /**
     * Starts login activity
     */
    public void getLoginCredentials(){
        Intent getLoginCredentials = new Intent(this, Login.class);
        startActivityForResult(getLoginCredentials, LOGIN_REQUEST_CODE);
    }

    public void startMenu(){
        Intent menuIntent = new Intent("com.egwh.scienceintranetscraper.HomeScreen");
        startActivity(menuIntent);
    }

    /**
     * Gets data from login activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // Get data from activity
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");

                // Perform login with data
                new PerformLogin(username, password).execute();

                // Once logged in open menu
                startMenu();
            }
            if(resultCode == RESULT_CANCELED){
            }
        }
    }
}



