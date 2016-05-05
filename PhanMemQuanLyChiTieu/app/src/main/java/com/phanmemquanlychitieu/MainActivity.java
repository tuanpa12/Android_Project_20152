package com.phanmemquanlychitieu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import Database.UserDatabase;
import Database.dbChi;
import Database.dbLaiXuat;
import Database.dbThu;
import Objects.BaoCao;
import Objects.Item;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserDatabase userDb;
    SQLiteDatabase mSQLite;

    dbLaiXuat laiXuatDb;
    SQLiteDatabase mDbLaiXuat;

    dbThu dbthu;
    SQLiteDatabase mDbthu;
    Cursor mCursorthu;
    // danh sach chi
    dbChi dbchi;
    SQLiteDatabase mDbchi;
    Cursor mCursorchi;
    // sync
    BaoCao objectchi2;
    Firebase root;
    Firebase usersRef;
    Cursor cursor;
    ArrayList<BaoCao> arrthu;
    ArrayList<BaoCao> arrchi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com/");
        usersRef = root.child(Build.SERIAL);

        TextView userName = (TextView) findViewById(R.id.headerName);
        if (userName != null) {
            userName.setText(getName());
        }
        userDb = new UserDatabase(this);
        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
        laiXuatDb = new dbLaiXuat(this);
        if (checkExpenseDb() && checkIncomeDb()) {
            danhSachThu();
            danhSachChi();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder add = new AlertDialog.Builder(MainActivity.this);
                    add.setTitle("Thêm thu - chi");
                    add.setMessage("Bạn muốn thêm khoản chi hay khoản thu?");
                    add.setNegativeButton("Thu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, TienThu.class);
                            startActivity(intent);
                        }
                    });
                    add.setPositiveButton("Chi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, TienChi.class);
                            startActivity(intent);
                        }
                    });

                    add.show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diagram) {
            if (arrthu.size() == 0 || arrchi.size() == 0) {
                Toast toast = Toast.makeText(MainActivity.this, "Danh sách rỗng", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent chuyen = new Intent(MainActivity.this, BaoCaoThuChi.class);
                startActivity(chuyen);
            }
        } else if (id == R.id.nav_exchange) {
            Intent chuyen = new Intent(MainActivity.this, NgoaiTe.class);
            startActivity(chuyen);
        } else if (id == R.id.nav_deposit) {
            Intent chuyen = new Intent(MainActivity.this, LaiXuat.class);
            startActivity(chuyen);
        } else if (id == R.id.nav_sync) {
            if (checkExpenseDb() || checkIncomeDb()) {
                syncData();
                Toast.makeText(MainActivity.this, "Sync success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Data empty", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_user_info) {
            // Handle info here
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
            exitDialog.setTitle("Quản lý chi tiêu");
            exitDialog.setMessage("Bạn có thực sự muốn đăng xuất chương trình không?");
            exitDialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    mSQLite = userDb.getWritableDatabase();
                    mDbthu = dbthu.getWritableDatabase();
                    mDbchi = dbchi.getWritableDatabase();
                    mDbLaiXuat = laiXuatDb.getWritableDatabase();

                    mSQLite.execSQL("delete from " + UserDatabase.TABLE_NAME);
                    mDbthu.execSQL("delete from " + dbThu.TABLE_NAME);
                    mDbchi.execSQL("delete from " + dbChi.TABLE_NAME);
                    mDbLaiXuat.execSQL("delete from " + dbLaiXuat.TABLE_NAME);
                    startActivity(intent);
                    finish();
                }
            });
            exitDialog.setPositiveButton("Để sau", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            exitDialog.setCancelable(false);
            exitDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void syncData() {
        int id;
        Firebase expenseRef = usersRef.child("Expense");
        Firebase incomeRef = usersRef.child("Income");
        expenseRef.setValue(null);
        incomeRef.setValue(null);
        // sync expense data
        mDbchi = dbchi.getReadableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        Item item;
        if (mCursorchi.moveToFirst()) {
            do {
                id = Integer.parseInt(mCursorchi.getString(0));
                String name = mCursorchi.getString(1);
                String cost = mCursorchi.getString(2);
                String type = mCursorchi.getString(3);
                String date = mCursorchi.getString(4);
                String note = mCursorchi.getString(5);
                item = new Item(name, cost, type, note, date, id);
                expenseRef.child("" + id).setValue(item);
            } while (mCursorchi.moveToNext());
        }

        // sync income data
        mDbthu = dbthu.getReadableDatabase();
        String query = "select * from thu";
        mCursorthu = mDbthu.rawQuery(query, null);
        if (mCursorthu.moveToFirst()) {
            do {
                id = Integer.parseInt(mCursorthu.getString(0));
                String name = mCursorthu.getString(1);
                String cost = mCursorthu.getString(2);
                String type = mCursorthu.getString(3);
                String date = mCursorthu.getString(4);
                String note = mCursorthu.getString(5);
                item = new Item(name, cost, type, note, date, id);
                incomeRef.child("" + id).setValue(item);
            } while (mCursorthu.moveToNext());
        }
    }

    public String getName() {
        String name = "";
        mSQLite = userDb.getReadableDatabase();
        String query = "select * from " + UserDatabase.TABLE_NAME;
        cursor = mSQLite.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
        }
        return name;
    }

    public boolean checkExpenseDb() {
        String query = "select * from " + dbChi.TABLE_NAME;
        boolean result = false;
        mDbchi = dbchi.getReadableDatabase();
        cursor = mDbchi.rawQuery(query, null);
        if (cursor.moveToFirst())
            result = true;
        return result;
    }

    public boolean checkIncomeDb() {
        String query = "select * from " + dbThu.TABLE_NAME;
        boolean result = false;
        mDbthu = dbthu.getReadableDatabase();
        cursor = mDbthu.rawQuery(query, null);
        if (cursor.moveToFirst())
            result = true;
        return result;
    }


    public void danhSachChi() {
        mDbchi = dbchi.getWritableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        arrchi = new ArrayList<>();
        if (mCursorchi.moveToFirst()) {
            do {
                objectchi2 = new BaoCao();
                objectchi2.setTienchi(mCursorchi.getString(2));
                objectchi2.setNgay(mCursorchi.getString(4));
                objectchi2.setNhom(mCursorchi.getString(3));
                arrchi.add(objectchi2);
            } while (mCursorchi.moveToNext());
        }
    }

    public void danhSachThu() {
        mDbthu = dbthu.getWritableDatabase();
        String query = "select * from thu";
        mCursorthu = mDbthu.rawQuery(query, null);
        arrthu = new ArrayList<>();
        if (mCursorthu.moveToFirst()) {
            do {
                objectchi2 = new BaoCao();
                objectchi2.setTienthu(mCursorthu.getString(2));
                objectchi2.setNgay(mCursorthu.getString(4));
                objectchi2.setNhom(mCursorthu.getString(3));
                arrthu.add(objectchi2);
            } while (mCursorthu.moveToNext());
        }
    }
}
