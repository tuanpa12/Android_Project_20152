package com.phanmemquanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import Database.UserDatabase;

/**
 * Created by Legendary on 27/04/2016.
 */
public class SplashActivity extends Activity {
    UserDatabase userDb;
    SQLiteDatabase mSQLite;
    Cursor cursor;
    String name, email;
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
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
            email = cursor.getString(2);
            isLogin = Boolean.parseBoolean(cursor.getString(3));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.start();
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            if (!isLogin) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            view2.setVisibility(View.INVISIBLE);
                            Thread.sleep(1000);
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
                            Thread.sleep(1000);
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
    });
}
