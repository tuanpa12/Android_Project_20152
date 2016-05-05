package com.phanmemquanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import Database.UserDatabase;

/**
 * Created by Legendary on 27/04/2016.
 */
public class SplashActivity extends Activity {
    UserDatabase userDb;
    SQLiteDatabase mSQLite;
    Cursor cursor;
    ImageView view1, view2;
    String query = "select * from " + UserDatabase.TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view1 = (ImageView) findViewById(R.id.img1);
        view2 = (ImageView) findViewById(R.id.img2);
        userDb = new UserDatabase(this);
        Intent intent;
        if (hasLogin()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean hasLogin() {
        boolean result = false;
        mSQLite = userDb.getReadableDatabase();
        cursor = mSQLite.rawQuery(query, null);
        if (cursor.moveToFirst())
            result = true;
        return result;
    }
}
