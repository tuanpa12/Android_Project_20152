package example.phanmemquanlythuchi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import Adapter.DanhSachChi;
import Adapter.DanhSachThu;
import Adapter.DoiNgay;
import Database.dbChi;
import Database.dbThu;
import Object.TienThuChi;

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
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class DanhSachThuChi extends Activity {
    public ArrayList<TienThuChi> sapxepthu = null;
    public ArrayList<TienThuChi> sapxepchi = null;
    public ArrayAdapter<String> adapterthu = null;
    public ArrayAdapter<String> adapterchi = null;
    public Context context = this;
    // income
    dbThu dbthu;
    SQLiteDatabase mDbthu;
    Cursor mCursorthu;
    private ListView listthu;
    // expense
    dbChi dbchi;
    SQLiteDatabase mDbchi;
    Cursor mCursorchi;
    private ListView listchi;

    private TienThuChi objectthu, objectchi;
    private ArrayList<TienThuChi> arrthu = null;
    private ArrayList<TienThuChi> arrchi = null;
    private DanhSachThu myadapterthu = null;
    private DanhSachChi myadapterchi = null;
    private DoiNgay doingaythu = null;
    private DoiNgay doingaychi = null;
    //sua thu
    private EditText suatenthu;
    private EditText suatienthu;
    private Spinner suanhomthu;
    private TextView suangaythu;
    private EditText suaghichuthu;
    private ImageButton btnsavethu, btnsuangaythu;
    private String datetimesuathu = "";
    private String[] arrspinnerthu = {"Tiền Lương", "Đòi Nợ", "Bán Đồ", "Đi Vay", "Khác"};
    //sua chi
    private EditText suatenchi;
    private EditText suatienchi;
    private Spinner suanhomchi;
    private TextView suangaychi;
    private EditText suaghichuchi;
    private ImageButton btnsavechi, btnsuangaychi;
    private String datetimesuachi = "";
    private String[] arrspinnerchi = {"Ăn Uống", "Quần Áo", "Cho vay", "Sinh Hoạt", "Đi Lại", "Trả Nợ", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baocaothuchi);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listthu = (ListView) findViewById(R.id.listView_danhsachkhoanthu);
        listchi = (ListView) findViewById(R.id.listView_danhsachkhoanchi);
        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
        LoadTab();

        //su kien khi click vào listview thu
        listthu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(DanhSachThuChi.this);
                b.setTitle("Lựa Chọn Của Bạn");
                b.setMessage("Bạn Muốn Gì?");
                b.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialogthu = new Dialog(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.suakhoanthu, null, false);
                        dialogthu.setContentView(v);
                        dialogthu.setTitle("Sửa Khoản Thu");
                        dialogthu.show();
                        // link to layout
                        suatenthu = (EditText) dialogthu.findViewById(R.id.editTextsuathu_tenkhoanthu);
                        suatienthu = (EditText) dialogthu.findViewById(R.id.editTextsuathu_tienkhoanthu);
                        suanhomthu = (Spinner) dialogthu.findViewById(R.id.spinnersuathu_nhomkhoanthu);
                        suangaythu = (TextView) dialogthu.findViewById(R.id.textViewsuathu_ngaykhoanthu);
                        suaghichuthu = (EditText) dialogthu.findViewById(R.id.editTextsuathu_ghichukhoanthu);
                        dexuat();
                        // pick time
                        btnsuangaythu = (ImageButton) dialogthu.findViewById(R.id.imageButtonsuathu_chonngaykhoanthu);
                        btnsuangaythu.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDatePickerDialog(null);
                            }
                        });
                        // set value to dialog
                        mDbthu = dbthu.getWritableDatabase();
                        suatenthu.setText(sapxepthu.get(position).getTen());
                        suatienthu.setText(sapxepthu.get(position).getTien());
                        suangaythu.setText(sapxepthu.get(position).getNgaythang());
                        suaghichuthu.setText(sapxepthu.get(position).getGhichu());
                        adapterthu = new ArrayAdapter<String>(DanhSachThuChi.this, android.R.layout.simple_spinner_dropdown_item, arrspinnerthu);
                        suanhomthu.setAdapter(adapterthu);
                        suanhomthu.setSelection(checkPosition(sapxepthu.get(position).getNhom(), arrspinnerthu));
                        // save button
                        btnsavethu = (ImageButton) dialogthu.findViewById(R.id.imageButtonsuathu_luukhoanthu);
                        btnsavethu.setOnClickListener(new OnClickListener() {

                            @SuppressWarnings("static-access")
                            @Override
                            public void onClick(View v) {
                                ContentValues cv = new ContentValues();
                                String name = suatenthu.getText().toString();
                                String cost = suatienthu.getText().toString();
                                String date = suangaythu.getText().toString();
                                String type = suanhomthu.getSelectedItem().toString();
                                String note = suaghichuthu.getText().toString();
                                cv.put(dbThu.COL_NAME, name);
                                cv.put(dbThu.COL_TIEN, cost);
                                cv.put(dbThu.COL_DATE, date);
                                cv.put(dbThu.COL_NHOM, type);
                                cv.put(dbThu.COL_GHICHU, note);
                                mDbthu.update(dbThu.TABLE_NAME, cv, "_id " + "=" + sapxepthu.get(position).getId(), null);
                                danhSachThu();
                                mDbthu.close();
                                dialogthu.cancel();
                            }
                        });

                    }
                });
                b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = sapxepthu.get(position).getId();
                        mDbthu.delete(dbThu.TABLE_NAME, "_id=?", new String[]{id});
                        danhSachThu();
                    }
                });
                b.create().show();
            }
        });
        //su kien khi click vào listview chi
        listchi.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(DanhSachThuChi.this);
                b.setTitle("Lựa Chọn Của Bạn");
                b.setMessage("Bạn Muốn Gì?");
                b.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialogchi = new Dialog(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.suakhoanchi, null, false);
                        dialogchi.setContentView(v);
                        dialogchi.setTitle("Sửa Khoản Chi");
                        dialogchi.show();
                        // link to layout
                        suatenchi = (EditText) dialogchi.findViewById(R.id.editTextsuachi_tenkhoanthu);
                        suatienchi = (EditText) dialogchi.findViewById(R.id.editTextsuachi_tienkhoanthu);
                        suanhomchi = (Spinner) dialogchi.findViewById(R.id.spinnersuachi_nhomkhoanthu);
                        suangaychi = (TextView) dialogchi.findViewById(R.id.textViewsuachi_ngaykhoanthu);
                        suaghichuchi = (EditText) dialogchi.findViewById(R.id.editTextsuachi_ghichukhoanthu);
                        dexuatchi();
                        // pick time
                        btnsuangaychi = (ImageButton) dialogchi.findViewById(R.id.imageButtonsuachi_chonngaykhoanthu);
                        btnsuangaychi.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDatePickerDialogchi(null);
                            }
                        });
                        // set value to dialog
                        mDbchi = dbchi.getWritableDatabase();
                        suatenchi.setText(sapxepchi.get(position).getTen());
                        suatienchi.setText(sapxepchi.get(position).getTien());
                        suangaychi.setText(sapxepchi.get(position).getNgaythang());
                        suaghichuchi.setText(sapxepchi.get(position).getGhichu());
                        adapterchi = new ArrayAdapter<String>(DanhSachThuChi.this, android.R.layout.simple_spinner_dropdown_item, arrspinnerchi);
                        suanhomchi.setAdapter(adapterchi);
                        suanhomchi.setSelection(checkPosition(sapxepchi.get(position).getNhom(), arrspinnerchi));
                        // save button
                        btnsavechi = (ImageButton) dialogchi.findViewById(R.id.imageButtonsuachi_luukhoanthu);
                        btnsavechi.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ContentValues cv = new ContentValues();
                                cv.put(dbChi.COL_NAME, suatenchi.getText().toString());
                                cv.put(dbChi.COL_TIEN, suatienchi.getText().toString());
                                cv.put(dbChi.COL_DATE, suangaychi.getText().toString());
                                cv.put(dbChi.COL_NHOM, suanhomchi.getSelectedItem().toString());
                                cv.put(dbChi.COL_GHICHU, suaghichuchi.getText().toString());
                                mDbchi.update(dbChi.TABLE_NAME, cv, "_id " + "=" + sapxepchi.get(position).getId(),
                                        null);
                                danhSachThu();
                                mDbchi.close();
                                dialogchi.cancel();
                                danhSachChi();
                            }
                        });
                    }
                });
                b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = sapxepchi.get(position).getId();
                        mDbchi.delete(dbChi.TABLE_NAME, "_id=?", new String[]{id});
                        danhSachChi();
                    }
                });
                b.create().show();
            }
        });
    }

    // find the position
    public int checkPosition(String name, String[] array) {
        int i = 0;
        for (; i < array.length; i++)
            if (array[i].equals(name))
                break;
        return i;
    }

    public void dexuat() {
        Calendar ngaythang = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        suangaythu.setText(dinhdang.format(ngaythang.getTime()));
    }

    public void dexuatchi() {
        Calendar ngaythang = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        suangaychi.setText(dinhdang.format(ngaythang.getTime()));
    }

    //datepicker ngay chi
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialogchi(View v) {
        DialogFragment newFragment = new DatePickerFragmentchi();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //datepicker thu
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        danhSachThu();
        danhSachChi();
    }

    public void LoadTab() {
        final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
        //goi cau lenh setup
        tab.setup();
        TabHost.TabSpec spec;
        //tao tab 1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("DANH SÁCH THU");
        tab.addTab(spec);
        //tao tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("DANH SÁCH CHI");
        tab.addTab(spec);
        tab.setCurrentTab(0);
    }

    public void danhSachThu() {
        mDbthu = dbthu.getWritableDatabase();
        String query = "select * from thu";
        mCursorthu = mDbthu.rawQuery(query, null);
        arrthu = new ArrayList<TienThuChi>();
        doingaythu = new DoiNgay();
        if (mCursorthu.moveToFirst()) {
            do {
                objectthu = new TienThuChi();
                objectthu.setId(mCursorthu.getString(0));
                objectthu.setTen(mCursorthu.getString(1));
                objectthu.setTien(mCursorthu.getString(2));
                objectthu.setNhom(mCursorthu.getString(3));
                objectthu.setNgaythang(doingaythu.doiDate(mCursorthu.getString(4)));
                objectthu.setGhichu(mCursorthu.getString(5));
                arrthu.add(objectthu);
            } while (mCursorthu.moveToNext());
        }
        sapxepthu = new ArrayList<TienThuChi>();
        sapxepthu = doingaythu.sapXep(arrthu);
        for (int i = 0; i < sapxepthu.size(); i++) {
            String strsapxep = doingaythu.ngay(sapxepthu.get(i).getNgaythang());
            sapxepthu.get(i).setNgaythang(strsapxep);
        }
        myadapterthu = new DanhSachThu(this, R.layout.t_customlayout_thu, sapxepthu);
        listthu.setAdapter(myadapterthu);
    }

    public void danhSachChi() {
        mDbchi = dbchi.getWritableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        arrchi = new ArrayList<TienThuChi>();
        doingaychi = new DoiNgay();
        if (mCursorchi.moveToFirst()) {
            do {
                objectchi = new TienThuChi();
                objectchi.setId(mCursorchi.getString(0));
                objectchi.setTen(mCursorchi.getString(1));
                objectchi.setTien(mCursorchi.getString(2));
                objectchi.setNhom(mCursorchi.getString(3));
                objectchi.setNgaythang(doingaychi.doiDate(mCursorchi.getString(4)));
                objectchi.setGhichu(mCursorchi.getString(5));
                arrchi.add(objectchi);
            } while (mCursorchi.moveToNext());
        }
        sapxepchi = new ArrayList<TienThuChi>();
        sapxepchi = doingaychi.sapXep(arrchi);
        for (int i = 0; i < sapxepchi.size(); i++) {
            String strsapxep = doingaychi.ngay(sapxepchi.get(i).getNgaythang());
            sapxepchi.get(i).setNgaythang(strsapxep);
        }
        myadapterchi = new DanhSachChi(this, R.layout.t_customlayout_chi, sapxepchi);
        listchi.setAdapter(myadapterchi);
    }

    @SuppressLint({"ValidFragment", "NewApi"})
    public class DatePickerFragmentchi extends DialogFragment implements
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
                    datetimesuachi = "0" + String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesuachi = "0" + String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimesuachi = String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesuachi = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            }
            suangaychi.setText(datetimesuachi);
        }
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
                    datetimesuathu = "0" + String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesuathu = "0" + String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimesuathu = String.valueOf(day) + "/" + "0" + String.valueOf(month) + "/" + String.valueOf(year);
                } else {
                    datetimesuathu = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                }
            }
            suangaythu.setText(datetimesuathu);
        }
    }
}
