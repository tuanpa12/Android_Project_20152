package com.phanmemquanlychitieu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Database.dbThu;

public class TienThu extends Activity {
    private EditText sotienkhoanthu, ghichukhoanthu;
    private Spinner nhomkhoanthu;
    private TextView ngaykhoanthu;
    private ImageButton luukhoanthu;

    //khoi tao thuoc tinh cho db
    private dbThu dbthu;
    private SQLiteDatabase mDbthu;

    //du lieu cho spinner
    private String[] arrspinner = {"Tiền Lương", "Đòi Nợ", "Bán Đồ", "Đi Vay", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhapkhoanthu);

        sotienkhoanthu = (EditText) findViewById(R.id.editText_tienkhoanthu);
        ghichukhoanthu = (EditText) findViewById(R.id.editText_ghichukhoanthu);
        nhomkhoanthu = (Spinner) findViewById(R.id.spinner_nhomkhoanthu);
        ngaykhoanthu = (TextView) findViewById(R.id.textView_ngaykhoanthu);
        ImageButton chonngaykhoanthu = (ImageButton) findViewById(R.id.imageButton_chonngaykhoanthu);
        luukhoanthu = (ImageButton) findViewById(R.id.imageButton_luukhoanthu);
        ArrayAdapter<String> adapterthu = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrspinner);
        nhomkhoanthu.setAdapter(adapterthu);
        dbthu = new dbThu(this);
        dexuat();
        chonngaykhoanthu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(null);
            }
        });
        luu();
    }

    public void dexuat() {
        Calendar ngaythang = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        ngaykhoanthu.setText(dinhdang.format(ngaythang.getTime()));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //đổ dữ liệu lên bdthu
    @SuppressLint("NewApi")
    public void luu() {
        luukhoanthu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sotienkhoanthu.getText().toString().isEmpty() || checkZero(sotienkhoanthu.getText().toString())) {
                    Toast toast = Toast.makeText(TienThu.this, "Bạn Chưa Nhập Tiền", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    mDbthu = dbthu.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    String cost = sotienkhoanthu.getText().toString();
                    String type = nhomkhoanthu.getSelectedItem().toString();
                    String note = ghichukhoanthu.getText().toString();
                    String date = ngaykhoanthu.getText().toString();
                    cv.put(dbThu.COL_TIEN, cost);
                    cv.put(dbThu.COL_NHOM, type);
                    cv.put(dbThu.COL_GHICHU, note);
                    cv.put(dbThu.COL_DATE, date);
                    mDbthu.insert(dbThu.TABLE_NAME, null, cv);

                    // reset view
                    sotienkhoanthu.setText(null);
                    ghichukhoanthu.setText(null);
                    Toast toast = Toast.makeText(TienThu.this, "Nhập Thành Công", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            String datetimeloc;
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
            ngaykhoanthu.setText(datetimeloc);
        }
    }

}
