package com.phanmemquanlychitieu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import Adapter.Menu;
import Database.UserDatabase;
import Database.dbChi;
import Database.dbLaiXuat;
import Database.dbThu;
import Objects.BaoCao;
import Objects.Item;

public class MainActivity1 extends AppCompatActivity {
    final static String[] mItemTexts = new String[]{
            "Thu nhập", "Chi tiêu", "Danh Sách",
            "Biều đồ", "Đổi tiền tệ", "Gửi tiết kiệm", "Đồng bộ", "Đăng xuất"};
    final static int[] mItemImgs = new int[]{
            R.drawable.thu, R.drawable.chi, R.drawable.danh_sach,
            R.drawable.ic_diagram, R.drawable.ic_currency_exchange, R.drawable.ic_money_saving, R.drawable.ic_sync,
            R.drawable.ic_logout};
    // danh sach thu
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
    ArrayList<BaoCao> arrthu;
    ArrayList<BaoCao> arrchi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com/");
        usersRef = root.child("257a29b4");

        userDb = new UserDatabase(this);
        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
        laiXuatDb = new dbLaiXuat(this);

        danhSachThu();
        danhSachChi();
        GridView grid = (GridView) findViewById(R.id.gridView_menu);
        Menu adapter = new Menu(this, mItemTexts, mItemImgs);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (position == 0) {
                    Intent chuyen = new Intent(MainActivity1.this, TienThu.class);
                    startActivity(chuyen);
                } else if (position == 1) {
                    Intent chuyen = new Intent(MainActivity1.this, TienChi.class);
                    startActivity(chuyen);
                } else if (position == 2) {
                    Intent chuyen = new Intent(MainActivity1.this, DanhSachThuChi.class);
                    startActivity(chuyen);
                } else if (position == 3) {
                    if (arrthu.size() == 0 || arrchi.size() == 0) {
                        Toast toast = Toast.makeText(MainActivity1.this, "Danh sách rỗng", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Intent chuyen = new Intent(MainActivity1.this, BaoCaoThuChi.class);
                        startActivity(chuyen);
                    }
                } else if (position == 4) {
                    Intent chuyen = new Intent(MainActivity1.this, NgoaiTe.class);
                    startActivity(chuyen);
                } else if (position == 5) {
                    Intent chuyen = new Intent(MainActivity1.this, LaiXuat.class);
                    startActivity(chuyen);
                } else if (position == 6) {
                    syncData();
                    Toast.makeText(MainActivity1.this, "Sync success", Toast.LENGTH_SHORT).show();
                } else if (position == 7) {
                    // log out
                    AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity1.this);
                    exitDialog.setTitle("Quản lý chi tiêu");
                    exitDialog.setMessage("Bạn có thực sự muốn đăng xuất chương trình không?");
                    exitDialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity1.this, LoginActivity.class);
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
            }
        });
    }

    public void syncData() {
        int id;
        Firebase expenseRef = usersRef.child("Expense");
        Firebase incomeRef = usersRef.child("Income");
        expenseRef.setValue(null);
        incomeRef.setValue(null);
        // sync expense data
        mDbchi = dbchi.getReadableDatabase();
        String queryChi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(queryChi, null);
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
        mCursorchi.close();

        // sync income data
        mDbthu = dbthu.getReadableDatabase();
        String queryThu = "select * from thu";
        mCursorthu = mDbthu.rawQuery(queryThu, null);
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
        mCursorthu.close();
    }

    public void danhSachChi() {
        mDbchi = dbchi.getReadableDatabase();
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
        mCursorchi.close();
    }

    public void danhSachThu() {
        mDbthu = dbthu.getReadableDatabase();
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
        mCursorthu.close();
    }
}