package example.phanmemquanlythuchi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import Adapter.Menu;
import Database.dbChi;
import Database.dbThu;
import Object.BaoCao;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.firebase.client.Firebase;

public class MainActivity extends Activity {
    private Firebase root;
    private Firebase usersRef;


    final static String[] mItemTexts = new String[]{
            "Thu nhập", "Chi tiêu", "Danh Sách",
            "Biều đồ", "Đổi tiền tệ", "Gửi tiết kiệm", "Đồng bộ", "Thoát"};
    final static int[] mItemImgs = new int[]{
            R.drawable.icon_thu, R.drawable.icon_chi, R.drawable.icon_danh_sach,
            R.drawable.icon_bieu_do, R.drawable.icon_tien_te, R.drawable.save_money, R.drawable.sync,
            R.drawable.icon_exit};
    // danh sach thu
    dbThu dbthu;
    SQLiteDatabase mDbthu;
    Cursor mCursorthu;
    // danh sach chi
    dbChi dbchi;
    SQLiteDatabase mDbchi;
    Cursor mCursorchi;
    // sync
    BaoCao objectchi2;
    private ArrayList<BaoCao> arrthu;
    private ArrayList<BaoCao> arrchi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuchi);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        root = new Firebase("https://expenseproject.firebaseio.com/");
        usersRef = root.child(Build.SERIAL);

        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
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
                    Intent chuyen = new Intent(MainActivity.this, TienThu.class);
                    startActivity(chuyen);
                } else if (position == 1) {
                    Intent chuyen = new Intent(MainActivity.this, TienChi.class);
                    startActivity(chuyen);
                } else if (position == 2) {
                    Intent chuyen = new Intent(MainActivity.this, DanhSachThuChi.class);
                    startActivity(chuyen);
                } else if (position == 3) {
                    if (arrthu.size() == 0 || arrchi.size() == 0) {
                        Toast toast = Toast.makeText(MainActivity.this, "Danh sách rỗng", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Intent chuyen = new Intent(MainActivity.this, BaoCaoThuChi.class);
                        startActivity(chuyen);
                    }
                } else if (position == 4) {
                    Intent chuyen = new Intent(MainActivity.this, NgoaiTe.class);
                    startActivity(chuyen);
                } else if (position == 5) {
                    Intent chuyen = new Intent(MainActivity.this, LaiXuat.class);
                    startActivity(chuyen);
                } else if (position == 6) {
                    syncData();
                    Toast.makeText(MainActivity.this, "Sync success", Toast.LENGTH_SHORT).show();
                } else if (position == 7) {
                    //thoát chương trình
                    AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
                    exitDialog.setTitle("Quản lý chi tiêu");
                    exitDialog.setMessage("Bạn có thực sự muốn thoát chương trình không?");
                    exitDialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    exitDialog.setPositiveButton("Để sau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    exitDialog.setCancelable(false);
                    exitDialog.show();
                    //
                }
            }
        });
    }

    public void syncData() {
        // sync expense data
        mDbchi = dbchi.getWritableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        Map<String, String> map;
        Firebase expenseRef = usersRef.child("Expense");
        if (mCursorchi.moveToFirst()) {
            do {
                map = new HashMap<String, String>();
                String node = mCursorchi.getString(0);
                String name = mCursorchi.getString(1);
                String cost = mCursorchi.getString(2);
                String type = mCursorchi.getString(3);
                String note = mCursorchi.getString(4);
                String date = mCursorchi.getString(5);
                map.put("name", name);
                map.put("cost", cost);
                map.put("type", type);
                map.put("note", note);
                map.put("date", date);
                expenseRef.child(node).setValue(map);
            } while (mCursorchi.moveToNext());
        }

        // sync income data
        mDbthu = dbthu.getWritableDatabase();
        String query = "select * from thu";
        mCursorthu = mDbthu.rawQuery(query, null);
        Firebase incomeRef = usersRef.child("Income");
        if (mCursorthu.moveToFirst()) {
            do {
                map = new HashMap<String, String>();
                String node = mCursorthu.getString(0);
                String name = mCursorthu.getString(1);
                String cost = mCursorthu.getString(2);
                String type = mCursorthu.getString(3);
                String note = mCursorthu.getString(4);
                String date = mCursorthu.getString(5);
                map.put("name", name);
                map.put("cost", cost);
                map.put("type", type);
                map.put("note", note);
                map.put("date", date);
                incomeRef.child(node).setValue(map);
            } while (mCursorthu.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_thu_chi, menu);
        return true;
    }

    public void danhSachChi() {
        mDbchi = dbchi.getWritableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        arrchi = new ArrayList<BaoCao>();
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
        arrthu = new ArrayList<BaoCao>();
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