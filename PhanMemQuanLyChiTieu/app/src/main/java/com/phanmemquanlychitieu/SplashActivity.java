package com.phanmemquanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import Database.UserDatabase;

/**
 * Created by Legendary on 27/04/2016.
 */
public class SplashActivity extends Activity implements Runnable {
    UserDatabase userDb;
    SQLiteDatabase mSQLite;
    Cursor cursor;
    ImageView view1, view2;
    boolean isLogin = false;
    String query = "select * from " + UserDatabase.TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view1 = (ImageView) findViewById(R.id.img1);
        view2 = (ImageView) findViewById(R.id.img2);

        getActionBar().hide();
        userDb = new UserDatabase(this);
        mSQLite = userDb.getReadableDatabase();
        cursor = mSQLite.rawQuery(query, null);
        String name, email;
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
            email = cursor.getString(2);
            isLogin = Boolean.parseBoolean(cursor.getString(3));
        }
        run();
    }

    @Override
    public void run() {
        if (!isLogin) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        view2.setVisibility(View.INVISIBLE);
                        Thread.sleep(200);
                        view1.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
