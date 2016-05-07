package com.phanmemquanlychitieu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import Database.UserDatabase;
import Database.dbChi;
import Database.dbThu;
import Objects.Item;

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

    dbThu thuDb;
    dbChi chiDb;

    SQLiteDatabase sqLiteExpense, sqLiteIncome;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com");
        userDb = new UserDatabase(this);
        thuDb = new dbThu(this);
        chiDb = new dbChi(this);
        mSQLite = userDb.getWritableDatabase();

        loadControl();

        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email.getText().toString();
                String pass = password.getText().toString();
                authLogin(mail, pass);
            }
        });

        // sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    authLogin(mail, pass);
                }
                return false;
            }
        });
    }

    public void authLogin(final String email, String pass) {
        root.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                login_form.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);
                ContentValues cv = new ContentValues();
                cv.put(UserDatabase.COL_NAME, authData.getProvider());
                cv.put(UserDatabase.COL_EMAIL, email);
                cv.put(UserDatabase.COL_KEY, "true");
                mSQLite.insert(UserDatabase.TABLE_NAME, null, cv);
                // syncData(email);
                Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void syncData(String email) {
        Firebase refExpense = root.child("257a29b4").child("Expense");
        Query expenseQuery = refExpense.orderByValue();
        expenseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sqLiteExpense = chiDb.getWritableDatabase();
                for (DataSnapshot listItem : dataSnapshot.getChildren()) {
                    item = listItem.getValue(Item.class);
                    ContentValues cv = new ContentValues();
                    cv.put(dbThu.COL_NAME, item.getName());
                    cv.put(dbThu.COL_TIEN, item.getCost());
                    cv.put(dbThu.COL_NHOM, item.getType());
                    cv.put(dbThu.COL_GHICHU, item.getNote());
                    cv.put(dbThu.COL_DATE, item.getDate());
                    sqLiteExpense.insert(dbThu.TABLE_NAME, null, cv);
                }
                sqLiteExpense.close();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Firebase refIncome = root.child("257a29b4").child("Income");
        Query incomeQuery = refIncome.orderByValue();
        incomeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sqLiteIncome = thuDb.getWritableDatabase();
                for (DataSnapshot listItem : dataSnapshot.getChildren()) {
                    item = listItem.getValue(Item.class);
                    ContentValues cv = new ContentValues();
                    cv.put(dbThu.COL_NAME, item.getName());
                    cv.put(dbThu.COL_TIEN, item.getCost());
                    cv.put(dbThu.COL_NHOM, item.getType());
                    cv.put(dbThu.COL_GHICHU, item.getNote());
                    cv.put(dbThu.COL_DATE, item.getDate());
                    sqLiteIncome.insert(dbThu.TABLE_NAME, null, cv);
                }
                sqLiteIncome.close();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
