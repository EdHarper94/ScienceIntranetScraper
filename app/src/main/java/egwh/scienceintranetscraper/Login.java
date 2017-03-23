package egwh.scienceintranetscraper;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                if(username != null && password != null){
                    Intent returnIntent = new Intent();
                    Bundle data = new Bundle();

                    data.putString("username", username);
                    data.putString("password", password);

                    returnIntent.putExtras(data);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

}

