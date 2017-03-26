package egwh.scienceintranetscraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start login screen
        Intent login = new Intent("com.egwh.scienceintranetscraper.Login");
        startActivity(login);
    }
}



