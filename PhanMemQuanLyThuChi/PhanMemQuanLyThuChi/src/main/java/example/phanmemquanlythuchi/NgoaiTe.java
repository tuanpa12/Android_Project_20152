package example.phanmemquanlythuchi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import Adapter.DanhSachTienTe;
import Adapter.DanhSachTienTeChi;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressLint("ShowToast")
public class NgoaiTe extends Activity {

    final static String[] NAME_MONEY = {"US Dollar (USD)", "Euro (EUR)",
            "British Pound (GBD)", "HongKong Dollar (HKD)", "Japan Yen (JPY)",
            "South Korean Won (KRW)", "Indian RUPEE (INR)",
            "Kuwaiti Dinar (KWD)", "Swiss France (CHF)",
            "Austraylia Dollar (AUD)", "Malaysian Ringgit (MYR)",
            "Russian Ruble (RUB)", "Singapore Dollar (SGD)", "Thai Baht (THB)",
            "Canadian Dollar (CAD)", "Saudi Rial (SAR)"};
    final static String[] KH_MONEY = {"USD", "EUR", "GBP", "HKD", "JPY",
            "KRW", "INR", "KWD", "CHF", "AUD", "MYR", "RUB", "SGD", "THB",
            "CAD", "SAR"};
    public ArrayList<TienThuChi> sapxepthu = null;
    public ArrayList<TienThuChi> sapxepchi = null;
    public Context context = this;
    dbThu dbthu;
    SQLiteDatabase mDbthu;
    Cursor mCursorthu;
    SimpleCursorAdapter mAdapterthu;
    dbChi dbchi;
    SQLiteDatabase mDbchi;
    Cursor mCursorchi;
    ArrayAdapter<String> adapterthu = null;
    ArrayAdapter<String> adapterchi = null;
    DanhSachTienTe adapterdstien;
    ArrayAdapter<String> adaptertien = null;
    Spinner spinnertien;
    TextView txttygia;
    // danh sach thu
    private ListView listthu;
    // danh sach chi
    private ListView listchi;
    private SimpleCursorAdapter mAdapterchi;
    private TienThuChi objectthu, objectchi;
    private ArrayList<TienThuChi> arrthu = null;
    private ArrayList<TienThuChi> arrchi = null;
    private DanhSachTienTe myadapterthu = null;
    private DanhSachTienTeChi myadapterchi = null;
    private DoiNgay doingaythu = null;
    private DoiNgay doingaychi = null;
    private EditText suatenthu;
    private EditText suatienthu;
    private Spinner suanhomthu;
    private TextView suangaythu;
    private EditText suaghichuthu;
    private ImageButton btnsavethu, btnsuangaythu;
    private String datetimesuathu = "";
    private String[] arrspinnerthu = {"Tiền Lương", "Đòi Nợ", "Bán Đồ",
            "Đi Vay", "Khác"};
    // sua chi
    @SuppressWarnings("unused")
    private EditText suatenchi;
    private EditText suatienchi;
    private Spinner suanhomchi;
    private TextView suangaychi;
    private EditText suaghichuchi;
    private ImageButton btnsavechi, btnsuangaychi;
    private String datetimesuachi = "";
    private String[] arrspinnerchi = {"Ăn Uống", "Quần Áo",
            "Cho vay", "Sinh Hoạt", "Đi Lại", "Trả Nợ", "Khác"};

    // sua thu
    public String gomtheothang(String nhom, String thang, ArrayList<TienThuChi> a) {
        String tong = "";
        return tong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doitien);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listthu = (ListView) findViewById(R.id.listView_danhsachkhoanthu);
        listchi = (ListView) findViewById(R.id.listView_danhsachkhoanchi);
        spinnertien = (Spinner) findViewById(R.id.spinner_danhsachloaitien);
        txttygia = (TextView) findViewById(R.id.textView_tygia);
        adaptertien = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NAME_MONEY);
        spinnertien.setAdapter(adaptertien);
        if (checkConn()) {
            spinnertien.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new LoadDataTask(
                            example.phanmemquanlythuchi.NgoaiTe.this, position).execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            AlertDialog.Builder a = new AlertDialog.Builder(example.phanmemquanlythuchi.NgoaiTe.this);
            a.setTitle("Kiểm tra kết nối");
            a.setMessage("Không có kết nối Internet");
            a.setNegativeButton("OK", null);
            a.show();
        }

        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
        LoadTab();

        danhSachThu();
        danhSachChi();

    }

    public void init() {

    }

    public void loadSpin(int position) {
        if (checkConn()) {
            TienThuChi khoantien = new TienThuChi();
            txttygia.setText(khoantien.getTyGia(KH_MONEY[position]));
            myadapterthu.vietBua(KH_MONEY[position]);
            myadapterthu.notifyDataSetChanged();
            myadapterchi.vietBua(KH_MONEY[position]);
            myadapterchi.notifyDataSetChanged();
        } else {
            TienThuChi khoantien = new TienThuChi();
            txttygia.setText("");
            myadapterthu.vietBua("(VND)");
            myadapterthu.notifyDataSetChanged();
            myadapterchi.vietBua("(VND)");
            myadapterchi.notifyDataSetChanged();
        }
    }

    public boolean checkConn() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ConnectivityManager managerConn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo inforWIFI = managerConn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo inforMOBILE = managerConn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (inforWIFI != null && inforMOBILE != null) {
            if (!inforWIFI.isConnected() && !inforMOBILE.isConnected()) {
                return false;
            }
        }
        return true;
    }

    public void dexuat() {
        Calendar ngaythang = Calendar.getInstance();
        // dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        suangaythu.setText(dinhdang.format(ngaythang.getTime()));
    }

    public void dexuatchi() {
        Calendar ngaythang = Calendar.getInstance();
        // dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        suangaychi.setText(dinhdang.format(ngaythang.getTime()));
    }

    // datepicker ngay chi
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialogchi(View v) {
        DialogFragment newFragment = new DatePickerFragmentchi();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // datepicker thu
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void LoadTab() {
        final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
        // goi cau lenh setup
        tab.setup();
        TabHost.TabSpec spec;
        // tao tab 1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab11);
        spec.setIndicator("DANH SÁCH THU");
        tab.addTab(spec);
        // tao tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab21);
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
        myadapterthu = new DanhSachTienTe(this,
                R.layout.t_customlayout_thu, sapxepthu);
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
        myadapterchi = new DanhSachTienTeChi(this,
                R.layout.t_customlayout_chi, sapxepchi);
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
                    datetimesuachi = "0" + String.valueOf(day) + "/" + "0"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                } else {
                    datetimesuachi = "0" + String.valueOf(day) + "/"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimesuachi = String.valueOf(day) + "/" + "0"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                } else {
                    datetimesuachi = String.valueOf(day) + "/"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
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
                    datetimesuathu = "0" + String.valueOf(day) + "/" + "0"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                } else {
                    datetimesuathu = "0" + String.valueOf(day) + "/"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                }
            } else {
                if (month < 10) {
                    datetimesuathu = String.valueOf(day) + "/" + "0"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                } else {
                    datetimesuathu = String.valueOf(day) + "/"
                            + String.valueOf(month) + "/"
                            + String.valueOf(year);
                }
            }
            suangaythu.setText(datetimesuathu);
        }
    }

    public class LoadDataTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog progressDialog;
        Activity daddy;
        int postion;

        public LoadDataTask(Activity context, int postion) {
            daddy = context;
            this.postion = postion;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    int counter = 0;
                    while (counter <= 5) {
                        this.wait(100);
                        counter++;
                        publishProgress(counter * 20);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            loadSpin(postion);
        }
    }
}
