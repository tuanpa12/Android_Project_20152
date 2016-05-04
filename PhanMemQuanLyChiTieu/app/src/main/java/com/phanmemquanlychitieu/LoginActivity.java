package com.phanmemquanlychitieu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import Database.UserDatabase;

/**
 * Created by Legendary on 25/04/2016.
 */
public class LoginActivity extends Activity {
    EditText email, password;
    Button btnLogin, btnSignUp;
    ProgressBar bar;
    ScrollView login_form;
    Firebase root;
    UserDatabase userDb;
    SQLiteDatabase mSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();
        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com/");
        userDb = new UserDatabase(this);
        mSQLite = userDb.getWritableDatabase();

        loadControl();

        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = email.getText().toString();
                final String emails = email.getText().toString();

                root.authWithPassword(emails, password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        login_form.setVisibility(View.GONE);
                        bar.setVisibility(View.VISIBLE);
                        ContentValues cv = new ContentValues();
                        cv.put(UserDatabase.COL_NAME, name);
                        cv.put(UserDatabase.COL_EMAIL, emails);
                        cv.put(UserDatabase.COL_KEY, "true");
                        mSQLite.insert(UserDatabase.TABLE_NAME, null, cv);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadControl() {
        email = (EditText) findViewById(R.id.et_login_1);
        password = (EditText) findViewById(R.id.et_login_2);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        bar = (ProgressBar) findViewById(R.id.login_progress);
        login_form = (ScrollView) findViewById(R.id.login_form);
    }
}
