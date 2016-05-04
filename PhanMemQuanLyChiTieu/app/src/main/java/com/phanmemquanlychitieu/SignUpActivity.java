package com.phanmemquanlychitieu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

/**
 * Created by tuand on 02/05/2016.
 */
public class SignUpActivity extends Activity {
    EditText name, email, password, retype_password;
    Button btnSignUp;
    ScrollView signUp_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void loadControl() {
        name = (EditText) findViewById(R.id.user_name);
        email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);
        retype_password = (EditText) findViewById(R.id.user_retype_password);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        signUp_form = (ScrollView) findViewById(R.id.signUp_form);
    }
}
