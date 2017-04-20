package egwh.scienceintranetscraper.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import egwh.scienceintranetscraper.R;

/**
 * Created by eghar on 22/03/2017.
 */
public class Login extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText usernameField = (EditText)findViewById(R.id.username_input_field);
        final EditText passwordField = (EditText)findViewById(R.id.password_input_field);

        Button loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Check fields have something in them
                if(username.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your username and password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Perform login with entered username and password
                    PerformLogin pl = new PerformLogin(username, password, new PerformLogin.PerformLoginResponse() {
                        @Override
                        // Check the result
                        public void loginFinished(String result) {
                            if (result.equals("LOGIN_FAIL")) {
                                Toast.makeText(getApplicationContext(), "Incorrect username/password. Please try again.", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("CONNECTION_FAIL")){
                                Toast.makeText(getApplicationContext(), "Currently unable to connect to server. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // If succesful login continue to home screen
                                Intent menuIntent = new Intent("com.egwh.scienceintranetscraper.HomeScreen");
                                startActivity(menuIntent);
                            }
                        }});
                    pl.execute();
                }
            }
        });
    }

}

