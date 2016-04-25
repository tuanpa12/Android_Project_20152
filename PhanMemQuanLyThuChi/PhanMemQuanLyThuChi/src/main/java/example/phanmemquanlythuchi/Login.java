package example.phanmemquanlythuchi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Legendary on 25/04/2016.
 */
public class Login extends Activity {
    EditText email, password;
    Button btnLogin, btnSignUp;
    Firebase root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com/");
        loadControl();

        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        email.setText("");
                        password.setText("");
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Successfully created user account", Toast.LENGTH_LONG).show();
                        password.setText("");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void loadControl() {
        email = (EditText) findViewById(R.id.et_login_1);
        password = (EditText) findViewById(R.id.et_login_2);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }
}
