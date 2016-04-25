package example.phanmemquanlythuchi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import Object.Laixuat;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

class dbLaiXuat extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "tblaixuat";
    public static final String COL_NAME = "tennganhang";
    public static final String COL_TIEN = "tienlaixuat";
    public static final String COL_LAIXUAT = "laixuat";
    public static final String COL_DATE = "ngaythanglaixuat";
    private static final String DB_NAME = "tienlaixuat";
    private static final int DB_VERSION = 1;
    private static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT," + COL_TIEN + " TEXT," + COL_LAIXUAT + " TEXT," + COL_DATE + " TEXT);";

    public dbLaiXuat(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STRING_CREATE);
        ContentValues cv = new ContentValues(3);
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}


public class LaiXuat extends Activity {
    EditText tiennganhang, laixuat;
    Spinner tennganhang, suatennganhang;
    ImageButton save;
    ListView listlaixuat;
    String datetimeloc = "";
    String datetimesualoc = "";
    Laixuat oblaixuat;

    dbLaiXuat dblaixuat;
    SQLiteDatabase mDblaixuat;
    Cursor mCursorlaixuat;
    SimpleCursorAdapter mAdapter;

    EditText suatiennganhang, sualaixuat;
    TextView suangaythang, ngaythang;
    ImageButton img_suangay;
    ImageButton img_savesua;

    //khai bao thuoc tinh xem
    private TextView xemnganhang;
    private TextView xemtien;
    private TextView xemlaixuat;
    private EditText edt_sothang;
    private ImageButton img_btn_kq;
    private TextView tv_kq;

    private Context context = this;
    private ArrayAdapter<String> adapterBank;
    String[] bankName = {"VietinBank", "CBBANK", "OceanBank", "GPBank", "AgriBank", "ACBank", "TPBank", "DongABank",
            "SeABank", "ABBank", "Techcom bank", "VPBank", "SHBank", "VietABank", "PGBank", "VCBank", "BIDV", "HSBC", "CitiBank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laixuat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dblaixuat = new dbLaiXuat(this);
        mDblaixuat = dblaixuat.getWritableDatabase();
        ngaythang = (TextView) findViewById(R.id.editText_ngaylaixuat);
        tennganhang = (Spinner) findViewById(R.id.edName);
        tiennganhang = (EditText) findViewById(R.id.edNum);
        laixuat = (EditText) findViewById(R.id.editLaiXuat);
        listlaixuat = (ListView) findViewById(R.id.lvHienThi1);

        adapterBank = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bankName);
        tennganhang.setAdapter(adapterBank);
        ngaythang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(null);
            }
        });
        dexuat();
        save = (ImageButton) findViewById(R.id.imageButton_laixuat);
        save.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("static-access")
            @Override
            public void onClick(View v) {
                if (tiennganhang.getText().toString().isEmpty() || checkZero(tiennganhang.getText().toString())) {
                    Toast toast = Toast.makeText(LaiXuat.this, "Bạn Chưa Nhập Tiền", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (laixuat.getText().toString().isEmpty() || checkZero(laixuat.getText().toString())) {
                    Toast toast = Toast.makeText(LaiXuat.this, "Bạn Chưa Nhập Lãi Xuất", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(dbLaiXuat.COL_NAME, tennganhang.getSelectedItem().toString());
                    cv.put(dbLaiXuat.COL_LAIXUAT, laixuat.getText().toString());
                    cv.put(dbLaiXuat.COL_TIEN, tiennganhang.getText().toString());
                    cv.put(dbLaiXuat.COL_DATE, ngaythang.getText().toString());
                    mDblaixuat.insert(dbLaiXuat.TABLE_NAME, null, cv);
                    laixuat.setText(null);
                    tiennganhang.setText(null);
                    String[] columns = new String[]{"_id", dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
                    mCursorlaixuat = mDblaixuat.query(dbLaiXuat.TABLE_NAME, columns, null, null, null, null, null, null);
                    String[] headers = new String[]{dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
                    mAdapter = new SimpleCursorAdapter(LaiXuat.this, R.layout.t_custom_listlaixuat,
                            mCursorlaixuat, headers, new int[]{R.id.tv_custom_tenlaixuat, R.id.tv_custom_ngaylaixuat});
                    listlaixuat.setAdapter(mAdapter);
                    Toast toast = Toast.makeText(LaiXuat.this, "Nhập Thành Công", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        listlaixuat.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(LaiXuat.this);
                b.setTitle("Lựa Chọn Của Bạn");
                b.setMessage("Bạn Muốn Gì?");
                b.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialogthu = new Dialog(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.t_sualaixuat, null, false);
                        dialogthu.setContentView(v);
                        dialogthu.setTitle("Sửa Lãi Xuất");
                        suatennganhang = (Spinner) dialogthu.findViewById(R.id.editText_suatenlaixuat);
                        suatennganhang.setAdapter(adapterBank);
                        suatiennganhang = (EditText) dialogthu.findViewById(R.id.editText_suatienlaixuat);
                        sualaixuat = (EditText) dialogthu.findViewById(R.id.editText_sualaixuat);
                        suangaythang = (TextView) dialogthu.findViewById(R.id.textView_ngaythanglaixuat);
                        String query = "select * from " + dbLaiXuat.TABLE_NAME;
                        mCursorlaixuat = mDblaixuat.rawQuery(query, null);
                        final ArrayList<Laixuat> arrngay = new ArrayList<Laixuat>();
                        if (mCursorlaixuat.moveToFirst()) {
                            do {
                                oblaixuat = new Laixuat();
                                oblaixuat.setId(mCursorlaixuat.getString(0));
                                oblaixuat.setTen(mCursorlaixuat.getString(1));
                                oblaixuat.setTien(mCursorlaixuat.getString(2));
                                oblaixuat.setLaixuat(mCursorlaixuat.getString(3));
                                oblaixuat.setNgay(mCursorlaixuat.getString(4));
                                arrngay.add(oblaixuat);
                            } while (mCursorlaixuat.moveToNext());
                        }
                        suatennganhang.setSelection(checkPosition(arrngay.get(position).getTen()));
                        suatiennganhang.setText(arrngay.get(position).getTien());
                        sualaixuat.setText(arrngay.get(position).getLaixuat());
                        suangaythang.setText(arrngay.get(position).getNgay());
                        img_suangay = (ImageButton) dialogthu.findViewById(R.id.imageButton_suangaythang);
                        img_suangay.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDatePickerDialog1(null);
                            }
                        });
                        img_savesua = (ImageButton) dialogthu.findViewById(R.id.imageButton_dongysua);
                        img_savesua.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues cv = new ContentValues();
                                cv.put(dbLaiXuat.COL_NAME, suatennganhang.getSelectedItem().toString());
                                cv.put(dbLaiXuat.COL_TIEN, suatiennganhang.getText().toString());
                                cv.put(dbLaiXuat.COL_DATE, suangaythang.getText().toString());
                                cv.put(dbLaiXuat.COL_LAIXUAT, sualaixuat.getText().toString());
                                mDblaixuat.update(dbLaiXuat.TABLE_NAME, cv, "_id " + "=" + arrngay.get(position).getId(),
                                        null);
                                String[] columns = new String[]{"_id", dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
                                mCursorlaixuat = mDblaixuat.query(dbLaiXuat.TABLE_NAME, columns, null, null, null, null, null, null);
                                String[] headers = new String[]{dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
                                mAdapter = new SimpleCursorAdapter(LaiXuat.this, R.layout.t_custom_listlaixuat,
                                        mCursorlaixuat, headers, new int[]{R.id.tv_custom_tenlaixuat, R.id.tv_custom_ngaylaixuat});
                                listlaixuat.setAdapter(mAdapter);
                                dialogthu.cancel();
                            }
                        });
                        dialogthu.show();
                    }
                });
                b.setNeutralButton("Xem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialogthu = new Dialog(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.activity_xemlaixuat, null, false);
                        dialogthu.setContentView(v);
                        dialogthu.setTitle("Xem Lãi Xuất");
                        xemnganhang = (TextView) dialogthu.findViewById(R.id.TextView_xemtennganhang);
                        xemtien = (TextView) dialogthu.findViewById(R.id.TextView_xemtienlaixuat);
                        xemlaixuat = (TextView) dialogthu.findViewById(R.id.TextView_xemlaixuat);
                        edt_sothang = (EditText) dialogthu.findViewById(R.id.EditText_xemsoluongthang);
                        img_btn_kq = (ImageButton) dialogthu.findViewById(R.id.imageButton_dongyxem);
                        tv_kq = (TextView) dialogthu.findViewById(R.id.textView_xemtonglaixuat);
                        String query = "select * from " + dbLaiXuat.TABLE_NAME;
                        mCursorlaixuat = mDblaixuat.rawQuery(query, null);
                        final ArrayList<Laixuat> arrngay = new ArrayList<Laixuat>();
                        if (mCursorlaixuat.moveToFirst()) {
                            do {
                                oblaixuat = new Laixuat();
                                oblaixuat.setTen(mCursorlaixuat.getString(1));
                                oblaixuat.setTien(mCursorlaixuat.getString(2));
                                oblaixuat.setLaixuat(mCursorlaixuat.getString(3));
                                arrngay.add(oblaixuat);
                            } while (mCursorlaixuat.moveToNext());
                        }
                        xemnganhang.setText(arrngay.get(position).getTen());
                        xemtien.setText(arrngay.get(position).getTien() + " (VND)");
                        xemlaixuat.setText(arrngay.get(position).getLaixuat());
                        img_btn_kq.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (edt_sothang.getText().toString().isEmpty() || checkZero(edt_sothang.getText().toString())) {
                                    Toast toast = Toast.makeText(LaiXuat.this, "Bạn Chưa Nhập Số Tháng", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    double d = kqLaiXuat(arrngay.get(position).getTien(), arrngay.get(position).getLaixuat(), edt_sothang.getText().toString());
                                    tv_kq.setText(String.valueOf(d) + " (VND)");
                                }
                            }
                        });
                        dialogthu.show();
                    }
                });
                b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCursorlaixuat.moveToPosition(position);
                        String rowId = mCursorlaixuat.getString(0); //Column 0 of the cursor is the id
                        mDblaixuat.delete(dbLaiXuat.TABLE_NAME, "_id = ?", new String[]{rowId});
                        mCursorlaixuat.requery();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                b.create().show();
            }
        });
    }

    public boolean checkZero(String s) {
        try {
            Float f = Float.parseFloat(s);
            if (f == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int checkPosition(String name) {
        int i = 0;
        for (; i < bankName.length; i++)
            if (bankName[i].equals(name))
                break;
        return i;
    }

    public void dexuat() {
        Calendar ngay = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        ngaythang.setText(dinhdang.format(ngay.getTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] columns = new String[]{"_id", dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
        mCursorlaixuat = mDblaixuat.query(dbLaiXuat.TABLE_NAME, columns, null, null, null, null, null, null);
        String[] headers = new String[]{dbLaiXuat.COL_NAME, dbLaiXuat.COL_DATE};
        mAdapter = new SimpleCursorAdapter(LaiXuat.this, R.layout.t_custom_listlaixuat,
                mCursorlaixuat, headers, new int[]{R.id.tv_custom_tenlaixuat, R.id.tv_custom_ngaylaixuat});
        listlaixuat.setAdapter(mAdapter);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog1(View v) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lai_xuat, menu);
        return true;
    }

    public double kqLaiXuat(String tien, String laixuat, String sothang) {
        double a = Double.parseDouble(tien);
        double b = Double.parseDouble(laixuat);
        double c = Double.parseDouble(sothang);
        double sum = a + a * b * c / 1200;
        ;
        return sum;
    }

    @SuppressLint({"ValidFragment", "NewApi"})
    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DATE);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            if (day < 10) {
                if (month < 10) {
                    datetimeloc = "0" + String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimeloc = "0" + String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimeloc = String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimeloc = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            }
            ngaythang.setText(datetimeloc);
        }
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment1 extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DATE);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            if (day < 10) {
                if (month < 10) {
                    datetimesualoc = "0" + String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesualoc = "0" + String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimesualoc = String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesualoc = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            }
            suangaythang.setText(datetimesualoc);
        }
    }
}
