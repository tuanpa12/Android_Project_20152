package example.phanmemquanlythuchi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


import Adapter.BaoCaoNam;
import Adapter.BaoCaoQuy;
import Adapter.BaoCaoThang;
import Adapter.BaoCaoHienTai;
import Adapter.DoiNgay;
import Database.dbChi;
import Database.dbThu;
import Object.BaoCao;
import Object.TienThuChi;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

public class BaoCaoThuChi extends Activity {

    dbThu dbthu;
    SQLiteDatabase mDbthu;
    Cursor mCursorthu;
    //danh sach chi
    dbChi dbchi;
    // SimpleCursorAdapter mAdapterthu;
    SQLiteDatabase mDbchi;
    Cursor mCursorchi;
    // private SimpleCursorAdapter mAdapterchi;
    ListView listhientai;
    DanhSachThuChi dsthuchi;
    Object object;
    String title[] = {"Hôm Nay", "Tháng nay", "Năm Nay"};
    BaoCao danhsach;
    BaoCao danhsachthang;
    BaoCao danhsachnam;
    BaoCaoHienTai adapterbaocaohientai;
    BaoCao objectchi2;
    TienThuChi objectthuchi;
    //Khai báo biến báo cáo tháng
    EditText thangnam;
    ImageButton img_timnam;
    ImageButton img_btn_kqbaocaothang;
    ListView danhsachbaocaothang;
    BaoCaoThang adapterbaocaothang;
    String datetimenam = "";
    ArrayList<Integer> taikhoan;
    ArrayList<BaoCao> arrbaocaothang;
    ArrayList<Integer> imageId = null;
    BaoCao baocaothang;
    ArrayList<String> arrcalendar;
    ArrayList<String> arrtitlethang;
    //khai báo biến báo cáo quý
    EditText edit_quy;
    ImageButton img_timquy;
    ImageButton img_btn_kqbaocaoquy;
    ListView danhsachbaocaoquy;
    BaoCaoQuy adapterbaocaoquy;
    String datetimequy = "";
    ArrayList<BaoCao> arrbaocaoquy;
    ArrayList<String> a;
    //Khai báo biến báo cáo năm
    BaoCaoNam adapterbaocaonam;
    ArrayList<BaoCao> arrnam;
    BaoCao objectnam;
    ListView lvnam;
    BaoCao baocaonam;
    private ListView listthu;
    private ArrayList<BaoCao> arrthu = null;
    private ArrayList<BaoCao> arrchi = null;
    private ArrayList<BaoCao> arrhientai;
    private DoiNgay doingaythu = null;
    private DoiNgay doingaychi = null;
    private String datehientai = "";
    private String thanghientai = "";
    private String namhientai = "";
    private double sumthu;
    private double sumchi;
    private double sumthuthang;
    private double sumchithang;
    private double sumthunam;
    private double sumchinam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baocaothuchi);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listhientai = (ListView) findViewById(R.id.listView_baocaohientai);
        arrhientai = new ArrayList<BaoCao>();
        dbthu = new dbThu(this);
        dbchi = new dbChi(this);
        LoadTab();
        danhSachThu();
        danhSachChi();
        if (arrchi.size() == 0 && arrthu.size() == 0) {
            BaoCaoThuChi.this.finish();
        }
        //System.out.println("gia tri ban dau:"+khac.TIENBANDAU);
        layNgayGiohientai();

        //tinh tong gia tri thu chi
        for (int i = 0; i < arrthu.size(); i++) {
            if (arrthu.get(i).getNgay().equals(datehientai)) {
                String a = arrthu.get(i).getTienthu().toString();
                sumthu += Double.parseDouble(a);
            }
        }
        //System.out.println("sum--- thhu-)"+sumthu);
        //tong chi
        for (int i = 0; i < arrchi.size(); i++) {
            if (arrchi.get(i).getNgay().equals(datehientai)) {
                String a = arrchi.get(i).getTienchi().toString();
                sumchi += Double.parseDouble(a);
            }
        }

        //System.out.println("sum--- thhu-)"+sumchi);
        baoCaoNgayHienTai();
        baoCaoThangHienTai();
        baoCaoNamHienTai();
        adapterbaocaohientai = new BaoCaoHienTai(this, R.layout.t_custom_danhsachhientai, arrhientai);
        listhientai.setAdapter(adapterbaocaohientai);
        //danh sach hien tai
        listhientai.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    objectthuchi = new TienThuChi();
                    double a = objectthuchi.kqngay("Ăn Uống", datehientai, arrchi);
                    double b = objectthuchi.kqngay("Quần Áo", datehientai, arrchi);
                    double c = objectthuchi.kqngay("Cho vay", datehientai, arrchi);
                    double d = objectthuchi.kqngay("Sinh Hoạt", datehientai, arrchi);
                    double e = objectthuchi.kqngay("Đi Lại", datehientai, arrchi);
                    double f = objectthuchi.kqngay("Trả Nợ", datehientai, arrchi);
                    double g = objectthuchi.kqngay("Khác", datehientai, arrchi);
                    openChart(a, b, c, d, e, f, g);
                } else if (position == 1) {
                    objectthuchi = new TienThuChi();
                    double a = objectthuchi.kqthang("Ăn Uống", thanghientai, arrchi);
                    double b = objectthuchi.kqthang("Quần Áo", thanghientai, arrchi);
                    double c = objectthuchi.kqthang("Cho vay", thanghientai, arrchi);
                    double d = objectthuchi.kqthang("Sinh Hoạt", thanghientai, arrchi);
                    double e = objectthuchi.kqthang("Đi Lại", thanghientai, arrchi);
                    double f = objectthuchi.kqthang("Trả Nợ", thanghientai, arrchi);
                    double g = objectthuchi.kqthang("Khác", thanghientai, arrchi);
                    openChart(a, b, c, d, e, f, g);
                } else if (position == 2) {
                    objectthuchi = new TienThuChi();
                    double a = objectthuchi.kqnam("Ăn Uống", namhientai, arrchi);
                    double b = objectthuchi.kqnam("Quần Áo", namhientai, arrchi);
                    double c = objectthuchi.kqnam("Cho vay", namhientai, arrchi);
                    double d = objectthuchi.kqnam("Sinh Hoạt", namhientai, arrchi);
                    double e = objectthuchi.kqnam("Đi Lại", namhientai, arrchi);
                    double f = objectthuchi.kqnam("Trả Nợ", namhientai, arrchi);
                    double g = objectthuchi.kqnam("Khác", namhientai, arrchi);
                    openChart(a, b, c, d, e, f, g);
                }
            }
        });

        //danh sach theo tháng
        thangnam = (EditText) findViewById(R.id.editText_nhapthang);
        img_timnam = (ImageButton) findViewById(R.id.imageButton_chonthang);
        img_btn_kqbaocaothang = (ImageButton) findViewById(R.id.imageButton_baocaothang);
        danhsachbaocaothang = (ListView) findViewById(R.id.listView_baocaothang);
        layNamHienTai();
        //lấy du lieu thang
        img_btn_kqbaocaothang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imageId = new ArrayList<Integer>();
                imageId.add(R.drawable.thang1);
                imageId.add(R.drawable.thang2);
                imageId.add(R.drawable.thang3);
                imageId.add(R.drawable.thang4);
                imageId.add(R.drawable.thang5);
                imageId.add(R.drawable.thang6);
                imageId.add(R.drawable.thang7);
                imageId.add(R.drawable.lichcongtac);
                imageId.add(R.drawable.thang9);
                imageId.add(R.drawable.thang10);
                imageId.add(R.drawable.danhmuc);
                imageId.add(R.drawable.thang12);

                //ds thang
                arrcalendar = new ArrayList<String>();
                arrcalendar.add("01");
                arrcalendar.add("02");
                arrcalendar.add("03");
                arrcalendar.add("04");
                arrcalendar.add("05");
                arrcalendar.add("06");
                arrcalendar.add("07");
                arrcalendar.add("08");
                arrcalendar.add("09");
                arrcalendar.add("10");
                arrcalendar.add("11");
                arrcalendar.add("12");

                //tieu de thag
                arrtitlethang = new ArrayList<String>();
                arrtitlethang.add("tháng 1");
                arrtitlethang.add("tháng 2");
                arrtitlethang.add("tháng 3");
                arrtitlethang.add("tháng 4");
                arrtitlethang.add("tháng 5");
                arrtitlethang.add("tháng 6");
                arrtitlethang.add("tháng 7");
                arrtitlethang.add("tháng 8");
                arrtitlethang.add("tháng 9");
                arrtitlethang.add("tháng 10");
                arrtitlethang.add("tháng 11");
                arrtitlethang.add("tháng 12");

                arrbaocaothang = new ArrayList<BaoCao>();
                baocaothang = new BaoCao();
                DoiNgay doi = new DoiNgay();
                double thangthu1 = baocaothang.thuThangThu(thangnam.getText().toString(), arrthu, "01");
                double thangchi1 = baocaothang.thuThangChi(thangnam.getText().toString(), arrchi, "01");
                baocaothang.setTienthu(String.valueOf(thangthu1));
                baocaothang.setTienchi(String.valueOf(thangchi1));
                arrbaocaothang.add(baocaothang);

                BaoCao baocaothang2 = new BaoCao();
                double thangthu2 = baocaothang2.thuThangThu(thangnam.getText().toString(), arrthu, "02");
                double thangchi2 = baocaothang2.thuThangChi(thangnam.getText().toString(), arrchi, "02");
                baocaothang2.setTienthu(String.valueOf(thangthu2));
                baocaothang2.setTienchi(String.valueOf(thangchi2));
                arrbaocaothang.add(baocaothang2);

                BaoCao baocaothang3 = new BaoCao();
                double thangthu3 = baocaothang3.thuThangThu(thangnam.getText().toString(), arrthu, "03");
                double thangchi3 = baocaothang3.thuThangChi(thangnam.getText().toString(), arrchi, "03");
                baocaothang3.setTienthu(String.valueOf(thangthu3));
                baocaothang3.setTienchi(String.valueOf(thangchi3));
                arrbaocaothang.add(baocaothang3);

                BaoCao baocaothang4 = new BaoCao();
                double thangthu4 = baocaothang4.thuThangThu(thangnam.getText().toString(), arrthu, "04");
                double thangchi4 = baocaothang4.thuThangChi(thangnam.getText().toString(), arrchi, "04");
                baocaothang4.setTienthu(String.valueOf(thangthu4));
                baocaothang4.setTienchi(String.valueOf(thangchi4));
                arrbaocaothang.add(baocaothang4);

                BaoCao baocaothang5 = new BaoCao();
                double thangthu5 = baocaothang5.thuThangThu(thangnam.getText().toString(), arrthu, "05");
                double thangchi5 = baocaothang5.thuThangChi(thangnam.getText().toString(), arrchi, "05");
                baocaothang5.setTienthu(String.valueOf(thangthu5));
                baocaothang5.setTienchi(String.valueOf(thangchi5));
                arrbaocaothang.add(baocaothang5);

                BaoCao baocaothang6 = new BaoCao();
                double thangthu6 = baocaothang6.thuThangThu(thangnam.getText().toString(), arrthu, "06");
                double thangchi6 = baocaothang6.thuThangChi(thangnam.getText().toString(), arrchi, "06");
                baocaothang6.setTienthu(String.valueOf(thangthu6));
                baocaothang6.setTienchi(String.valueOf(thangchi6));
                arrbaocaothang.add(baocaothang6);

                BaoCao baocaothang7 = new BaoCao();
                double thangthu7 = baocaothang7.thuThangThu(thangnam.getText().toString(), arrthu, "07");
                double thangchi7 = baocaothang7.thuThangChi(thangnam.getText().toString(), arrchi, "07");
                baocaothang7.setTienthu(String.valueOf(thangthu7));
                baocaothang7.setTienchi(String.valueOf(thangchi7));
                arrbaocaothang.add(baocaothang7);

                BaoCao baocaothang8 = new BaoCao();
                double thangthu8 = baocaothang8.thuThangThu(thangnam.getText().toString(), arrthu, "08");
                double thangchi8 = baocaothang8.thuThangChi(thangnam.getText().toString(), arrchi, "08");
                baocaothang8.setTienthu(String.valueOf(thangthu8));
                baocaothang8.setTienchi(String.valueOf(thangchi8));
                arrbaocaothang.add(baocaothang8);

                BaoCao baocaothang9 = new BaoCao();
                double thangthu9 = baocaothang9.thuThangThu(thangnam.getText().toString(), arrthu, "09");
                double thangchi9 = baocaothang9.thuThangChi(thangnam.getText().toString(), arrchi, "09");
                baocaothang9.setTienthu(String.valueOf(thangthu9));
                baocaothang9.setTienchi(String.valueOf(thangchi9));
                arrbaocaothang.add(baocaothang9);

                BaoCao baocaothang10 = new BaoCao();
                double thangthu10 = baocaothang10.thuThangThu(thangnam.getText().toString(), arrthu, "10");
                double thangchi10 = baocaothang10.thuThangChi(thangnam.getText().toString(), arrchi, "10");
                baocaothang10.setTienthu(String.valueOf(thangthu10));
                baocaothang10.setTienchi(String.valueOf(thangchi10));
                arrbaocaothang.add(baocaothang10);

                BaoCao baocaothang11 = new BaoCao();
                double thangthu11 = baocaothang11.thuThangThu(thangnam.getText().toString(), arrthu, "11");
                double thangchi11 = baocaothang11.thuThangChi(thangnam.getText().toString(), arrchi, "11");
                baocaothang11.setTienthu(String.valueOf(thangthu11));
                baocaothang11.setTienchi(String.valueOf(thangchi11));
                arrbaocaothang.add(baocaothang11);

                BaoCao baocaothang12 = new BaoCao();
                double thangthu12 = baocaothang12.thuThangThu(thangnam.getText().toString(), arrthu, "12");
                //System.out.println("tien:"+doi.doiThang1(arrthu.get(0).getTienthu()));
                //System.out.println("tien:"+doi.doiThang1(arrthu.get(0).getNgay()));
                double thangchi12 = baocaothang12.thuThangChi(thangnam.getText().toString(), arrchi, "12");
                baocaothang12.setTienthu(String.valueOf(thangthu12));
                baocaothang12.setTienchi(String.valueOf(thangchi12));
                arrbaocaothang.add(baocaothang12);
                dsThang();
                adapterbaocaothang = new BaoCaoThang(BaoCaoThuChi.this, R.layout.t_custom_danhsachthang, arrbaocaothang, imageId, arrtitlethang);
                danhsachbaocaothang.setAdapter(adapterbaocaothang);
            }
        });

        danhsachbaocaothang.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                objectthuchi = new TienThuChi();
                double a = objectthuchi.kqthang("Ăn Uống", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double b = objectthuchi.kqthang("Quần Áo", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double c = objectthuchi.kqthang("Cho vay", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double d = objectthuchi.kqthang("Sinh Hoạt", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double e = objectthuchi.kqthang("Đi Lại", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double f = objectthuchi.kqthang("Trả Nợ", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                double g = objectthuchi.kqthang("Khác", thangnam.getText().toString() + arrcalendar.get(position), arrchi);
                System.out.println("nhap-------" + thangnam.getText().toString() + arrcalendar.get(position));
                DoiNgay doi = new DoiNgay();
                System.out.println("ngay--------" + doi.doiThang1(arrchi.get(position).getNgay()));
                openChart(a, b, c, d, e, f, g);
            }
        });
        img_timnam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(null);
            }
        });


        //danh sách theo quý
        edit_quy = (EditText) findViewById(R.id.editText_nhapnamquy);
        img_timquy = (ImageButton) findViewById(R.id.imageButton_chonquy);
        img_btn_kqbaocaoquy = (ImageButton) findViewById(R.id.imageButton_baocaoquy);
        danhsachbaocaoquy = (ListView) findViewById(R.id.listView_baocaoquy);
        edit_quy.setText(datetimequy);
        img_timquy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDatePickerDialog1(null);
            }
        });
        img_btn_kqbaocaoquy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                arrbaocaoquy = new ArrayList<BaoCao>();
                baocaothang = new BaoCao();
                a = new ArrayList<String>();
                a.add("Quý I");
                a.add("Quý II");
                a.add("Quý III");
                a.add("Quý IV");
                double quy1 = baocaothang.thuQuy("01", "03", edit_quy.getText().toString(), arrthu);
                double quy1a = baocaothang.chiQuy("01", "03", edit_quy.getText().toString(), arrchi);
                baocaothang.setTienthu(String.valueOf(quy1));
                baocaothang.setTienchi(String.valueOf(quy1a));
                arrbaocaoquy.add(baocaothang);

                BaoCao baocaothang1 = new BaoCao();
                double quy2 = baocaothang1.thuQuy("04", "06", edit_quy.getText().toString(), arrthu);
                double quy2a = baocaothang1.chiQuy("04", "06", edit_quy.getText().toString(), arrchi);
                baocaothang1.setTienthu(String.valueOf(quy2));
                baocaothang1.setTienchi(String.valueOf(quy2a));
                arrbaocaoquy.add(baocaothang1);

                BaoCao baocaothang2 = new BaoCao();
                double quy3 = baocaothang2.thuQuy("07", "09", edit_quy.getText().toString(), arrthu);
                double quy3a = baocaothang2.chiQuy("07", "09", edit_quy.getText().toString(), arrchi);
                baocaothang2.setTienthu(String.valueOf(quy3));
                baocaothang2.setTienchi(String.valueOf(quy3a));
                arrbaocaoquy.add(baocaothang2);

                BaoCao baocaothang3 = new BaoCao();
                double quy4 = baocaothang3.thuQuy("10", "12", edit_quy.getText().toString(), arrthu);
                double quy4a = baocaothang3.chiQuy("10", "12", edit_quy.getText().toString(), arrchi);
                baocaothang3.setTienthu(String.valueOf(quy4));
                baocaothang3.setTienchi(String.valueOf(quy4a));
                arrbaocaoquy.add(baocaothang3);
                adapterbaocaoquy = new BaoCaoQuy(BaoCaoThuChi.this, R.layout.t_custom_danhsachquy, arrbaocaoquy, a);
                danhsachbaocaoquy.setAdapter(adapterbaocaoquy);
            }
        });

        danhsachbaocaoquy.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                objectthuchi = new TienThuChi();
                double a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0;
                if (arg2 == 0) {

                    a = objectthuchi.kqquy("Ăn Uống", "01", "03", edit_quy.getText().toString(), arrchi);
                    b = objectthuchi.kqquy("Quần Áo", "01", "03", edit_quy.getText().toString(), arrchi);
                    c = objectthuchi.kqquy("Cho vay", "01", "03", edit_quy.getText().toString(), arrchi);
                    d = objectthuchi.kqquy("Sinh Hoạt", "01", "03", edit_quy.getText().toString(), arrchi);
                    e = objectthuchi.kqquy("Đi Lại", "01", "03", edit_quy.getText().toString(), arrchi);
                    f = objectthuchi.kqquy("Trả Nợ", "01", "03", edit_quy.getText().toString(), arrchi);
                    g = objectthuchi.kqquy("Khác", "01", "03", edit_quy.getText().toString(), arrchi);
                } else if (arg2 == 1) {

                    a = objectthuchi.kqquy("Ăn Uống", "04", "06", edit_quy.getText().toString(), arrchi);
                    b = objectthuchi.kqquy("Quần Áo", "04", "06", edit_quy.getText().toString(), arrchi);
                    c = objectthuchi.kqquy("Cho vay", "04", "06", edit_quy.getText().toString(), arrchi);
                    d = objectthuchi.kqquy("Sinh Hoạt", "04", "06", edit_quy.getText().toString(), arrchi);
                    e = objectthuchi.kqquy("Đi Lại", "04", "06", edit_quy.getText().toString(), arrchi);
                    f = objectthuchi.kqquy("Trả Nợ", "04", "06", edit_quy.getText().toString(), arrchi);
                    g = objectthuchi.kqquy("Khác", "04", "06", edit_quy.getText().toString(), arrchi);
                } else if (arg2 == 2) {

                    a = objectthuchi.kqquy("Ăn Uống", "07", "09", edit_quy.getText().toString(), arrchi);
                    b = objectthuchi.kqquy("Quần Áo", "07", "09", edit_quy.getText().toString(), arrchi);
                    c = objectthuchi.kqquy("Cho vay", "07", "09", edit_quy.getText().toString(), arrchi);
                    d = objectthuchi.kqquy("Sinh Hoạt", "07", "09", edit_quy.getText().toString(), arrchi);
                    e = objectthuchi.kqquy("Đi Lại", "07", "09", edit_quy.getText().toString(), arrchi);
                    f = objectthuchi.kqquy("Trả Nợ", "07", "09", edit_quy.getText().toString(), arrchi);
                    g = objectthuchi.kqquy("Khác", "07", "09", edit_quy.getText().toString(), arrchi);
                } else if (arg2 == 3) {

                    a = objectthuchi.kqquy("Ăn Uống", "10", "12", edit_quy.getText().toString(), arrchi);
                    b = objectthuchi.kqquy("Quần Áo", "10", "12", edit_quy.getText().toString(), arrchi);
                    c = objectthuchi.kqquy("Cho vay", "10", "12", edit_quy.getText().toString(), arrchi);
                    d = objectthuchi.kqquy("Sinh Hoạt", "10", "12", edit_quy.getText().toString(), arrchi);
                    e = objectthuchi.kqquy("Đi Lại", "10", "12", edit_quy.getText().toString(), arrchi);
                    f = objectthuchi.kqquy("Trả Nợ", "10", "12", edit_quy.getText().toString(), arrchi);
                    g = objectthuchi.kqquy("Khác", "10", "12", edit_quy.getText().toString(), arrchi);
                }
                openChart(a, b, c, d, e, f, g);
            }
        });

        //danh sách theo năm


        DoiNgay doi = new DoiNgay();
        lvnam = (ListView) findViewById(R.id.listView_baocaonam);
        double thu = 0;
        double chi = 0;
        arrnam = new ArrayList<BaoCao>();

        //code cua loc
        ArrayList<String> namthu = new ArrayList<String>();
        ArrayList<String> namchi = new ArrayList<String>();
        ArrayList<String> locnam = new ArrayList<String>();
        arrchi = doi.sapXep1(arrchi);
        arrthu = doi.sapXep1(arrthu);
        namchi.add(doi.doiNam1(arrchi.get(0).getNgay()));
        for (int i = 0; i < arrchi.size(); i++) {
            if (namchi.get(namchi.size() - 1).equals(doi.doiNam1(arrchi.get(i).getNgay()))) {

            } else {
                namchi.add(doi.doiNam1(arrchi.get(i).getNgay()));
            }
        }
        namthu.add(doi.doiNam1(arrthu.get(0).getNgay()));
        for (int i = 0; i < arrthu.size(); i++) {
            if (namthu.get(namthu.size() - 1).equals(doi.doiNam1(arrthu.get(i).getNgay()))) {
            } else {
                namthu.add(doi.doiNam1(arrthu.get(i).getNgay()));
            }
        }
        for (int i = 0; i < namthu.size(); i++) {
            locnam.add(namthu.get(i));
        }
        for (int i = 0; i < namchi.size(); i++) {
            locnam.add(namchi.get(i));
        }

        for (int i = 0; i < locnam.size(); i++) {
            for (int j = i + 1; i < locnam.size(); i++) {
                if (locnam.get(i).equals(locnam.get(j))) {
                    locnam.remove(j);
                    j--;
                }
            }
        }
        for (int i = 0; i < locnam.size(); i++) {
            baocaonam = new BaoCao();
            for (int j = 0; j < arrthu.size(); j++) {
                if (locnam.get(i).equals(doi.doiNam1(arrthu.get(j).getNgay()))) {
                    thu += Double.parseDouble(arrthu.get(j).getTienthu());
                }
            }

            baocaonam.setTienthu(String.valueOf(thu));
            for (int k = 0; k < arrchi.size(); k++) {
                if (locnam.get(i).equals(doi.doiNam1(arrchi.get(k).getNgay()))) {
                    chi += Double.parseDouble(arrchi.get(k).getTienchi());
                }
            }

            baocaonam.setTienchi(String.valueOf(chi));
            baocaonam.setNgay(locnam.get(i));
            arrnam.add(baocaonam);
            thu = 0;
            chi = 0;
        }

        arrnam = doi.sapXepnam(arrnam);
        adapterbaocaonam = new BaoCaoNam(this, R.layout.t_custom_danhsachnam, arrnam);
        lvnam.setAdapter(adapterbaocaonam);
        lvnam.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                objectthuchi = new TienThuChi();
                //gia trị nam chon tren listview
                String nam = arrnam.get(arg2).getNgay();
                double a = objectthuchi.kqnam("Ăn Uống", nam, arrchi);
                double b = objectthuchi.kqnam("Quần Áo", nam, arrchi);
                double c = objectthuchi.kqnam("Cho vay", nam, arrchi);
                double d = objectthuchi.kqnam("Sinh Hoạt", nam, arrchi);
                double e = objectthuchi.kqnam("Đi Lại", nam, arrchi);
                double f = objectthuchi.kqnam("Trả Nợ", nam, arrchi);
                double g = objectthuchi.kqnam("Khác", nam, arrchi);
                openChart(a, b, c, d, e, f, g);
            }
        });
    }

    public void dsThang() {
        if (arrbaocaothang.size() != 0) {
            for (int i = 0; i < arrbaocaothang.size(); i++) {
                if (Double.parseDouble(arrbaocaothang.get(i).getTienchi()) == 0 && Double.parseDouble(arrbaocaothang.get(i).getTienthu()) == 0) {
                    arrbaocaothang.remove(i);
                    imageId.remove(i);
                    arrcalendar.remove(i);
                    arrtitlethang.remove(i);
                    i--;
                }
            }
        }
    }

    public void baoCaoNgayHienTai() {
        danhsach = new BaoCao();
        doingaychi = new DoiNgay();
        danhsach.setTitle(title[0].toString());
        danhsach.setNgay(doingaychi.ngay(datehientai));
        danhsach.setTienthu(String.valueOf(sumthu));
        danhsach.setTienchi(String.valueOf(sumchi));
        arrhientai.add(danhsach);
    }

    public void baoCaoThangHienTai() {
        doingaychi = new DoiNgay();
        //System.out.println("sum--- thhu-)"+doingaychi.doiThang1(arrthu.get(0).getDate()));
        for (int i = 0; i < arrthu.size(); i++) {
            if (doingaychi.doiThang1(arrthu.get(i).getNgay()).equals(thanghientai)) {
                String a = arrthu.get(i).getTienthu().toString();
                sumthuthang += Double.parseDouble(a);
            }
        }
        for (int i = 0; i < arrchi.size(); i++) {
            if (doingaychi.doiThang1(arrchi.get(i).getNgay()).equals(thanghientai)) {
                String a = arrchi.get(i).getTienchi().toString();
                sumchithang += Double.parseDouble(a);
            }
        }
        danhsachthang = new BaoCao();
        doingaychi = new DoiNgay();
        danhsachthang.setTitle(title[1].toString());
        danhsachthang.setNgay(doingaychi.thang(datehientai));
        danhsachthang.setTienthu(String.valueOf(sumthuthang));
        danhsachthang.setTienchi(String.valueOf(sumchithang));
        arrhientai.add(danhsachthang);
    }

    public void baoCaoNamHienTai() {
        doingaychi = new DoiNgay();
        //System.out.println("sum--- thhu-)"+doingaychi.doiNam1(arrthu.get(0).getDate()));
        for (int i = 0; i < arrthu.size(); i++) {
            if (doingaychi.doiNam1(arrthu.get(i).getNgay()).equals(namhientai)) {
                String a = arrthu.get(i).getTienthu().toString();
                sumthunam += Double.parseDouble(a);
            }
        }
        for (int i = 0; i < arrchi.size(); i++) {
            if (doingaychi.doiNam1(arrchi.get(i).getNgay()).equals(namhientai)) {
                String a = arrchi.get(i).getTienchi().toString();
                sumchinam += Double.parseDouble(a);
            }
        }
        danhsachnam = new BaoCao();
        doingaychi = new DoiNgay();
        danhsachnam.setTitle(title[2].toString());
        danhsachnam.setNgay(doingaychi.nam(datehientai));
        danhsachnam.setTienthu(String.valueOf(sumthunam));
        danhsachnam.setTienchi(String.valueOf(sumchinam));
        arrhientai.add(danhsachnam);
    }

    public void LoadTab() {
        final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
        //goi cau lenh setup
        tab.setup();
        TabHost.TabSpec spec;
        //tao tab 1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("hiện");
        tab.addTab(spec);
        //tao tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("tháng");
        tab.addTab(spec);
        //
        spec = tab.newTabSpec("t3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("quý");
        tab.addTab(spec);

        spec = tab.newTabSpec("t4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("năm");
        tab.addTab(spec);
        tab.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bao_cao_thu_chi, menu);
        return true;
    }

    public void layNgayGiohientai() {
        Calendar ngay = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "dd/MM/yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        doingaychi = new DoiNgay();
        datehientai = doingaychi.doiDate(dinhdang.format(ngay.getTime()));
        thanghientai = doingaychi.doiThang(dinhdang.format(ngay.getTime()));
        namhientai = doingaychi.doiNam(dinhdang.format(ngay.getTime()));
    }

    public void layNamHienTai() {
        Calendar nam = Calendar.getInstance();
        //dinh dang 12h
        String dinhdang24 = "yyyy";
        SimpleDateFormat dinhdang = null;
        dinhdang = new SimpleDateFormat(dinhdang24, Locale.getDefault());
        thangnam.setText(dinhdang.format(nam.getTime()));
        datetimequy = dinhdang.format(nam.getTime());
    }

    //danh sach cac nam
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //danh sach quy
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog1(View v) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void danhSachThu() {
        mDbthu = dbthu.getWritableDatabase();
        String query = "select * from thu";
        mCursorthu = mDbthu.rawQuery(query, null);
        arrthu = new ArrayList<BaoCao>();
        doingaythu = new DoiNgay();
        if (mCursorthu.moveToFirst()) {
            do {
                objectchi2 = new BaoCao();
                objectchi2.setTienthu(mCursorthu.getString(2));
                objectchi2.setNgay(doingaythu.doiDate(mCursorthu.getString(4)));
                objectchi2.setNhom(mCursorthu.getString(3));
                arrthu.add(objectchi2);
            } while (mCursorthu.moveToNext());
        }
    }

    public void danhSachChi() {
        mDbchi = dbchi.getWritableDatabase();
        String querychi = "select * from chi";
        mCursorchi = mDbchi.rawQuery(querychi, null);
        arrchi = new ArrayList<BaoCao>();
        doingaychi = new DoiNgay();
        if (mCursorchi.moveToFirst()) {
            do {
                objectchi2 = new BaoCao();
                objectchi2.setTienchi(mCursorchi.getString(2));
                objectchi2.setNgay(doingaychi.doiDate(mCursorchi.getString(4)));
                objectchi2.setNhom(mCursorchi.getString(3));
                arrchi.add(objectchi2);
            } while (mCursorchi.moveToNext());
        }
    }

    public void openChart(double anuong, double quanao, double chovay, double sinhhoat, double dilai, double trano, double khac) {

        // Pie Chart Section Value
        ArrayList<Double> distribution = new ArrayList<Double>();
        distribution.add(0, anuong);
        distribution.add(1, quanao);
        distribution.add(2, chovay);
        distribution.add(3, sinhhoat);
        distribution.add(4, dilai);
        distribution.add(5, trano);
        distribution.add(6, khac);

        // Color of each Pie Chart Sections
        ArrayList<String> code = new ArrayList<String>();
        code.add(0, "Ăn Uống" + ":" + anuong);
        code.add(1, "Quần Áo " + ":" + quanao);
        code.add(2, "Cho vay" + ":" + chovay);
        code.add(3, "Sinh Hoạt" + ":" + sinhhoat);
        code.add(4, "Đi Lại" + ":" + dilai);
        code.add(5, "Trả Nợ" + ":" + trano);
        code.add(6, "Khác" + ":" + khac);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0, Color.BLUE);
        colors.add(1, Color.MAGENTA);
        colors.add(2, Color.GREEN);
        colors.add(3, Color.CYAN);
        colors.add(4, Color.RED);
        colors.add(5, Color.YELLOW);
        colors.add(6, Color.LTGRAY);
        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries("Biểu Đồ Thu Chi");
        for (int i = 0; i < distribution.size(); i++) {
            // Adding a slice with its values and name to the Pie Chart
            if (distribution.get(i) == 0.0) {
                distribution.remove(i);
                code.remove(i);
                colors.remove(i);
                i--;
            } else {
                distributionSeries.add(code.get(i), distribution.get(i));
            }
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.size(); i++) {
            if (distribution.get(i) == 0.0) {
                distribution.remove(i);
                code.remove(i);
                colors.remove(i);
                i--;
            } else {
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors.get(i));
                seriesRenderer.setDisplayChartValues(true);
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }
        }
        defaultRenderer.setChartTitle(" Biểu Đồ ");
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);
        // Creating an intent to plot bar chart using dataset and multipleRenderer
        Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "Biểu Đồ Thu Chi");
        // Start Activity
        startActivity(intent);

    }

    @SuppressLint({"ValidFragment", "NewApi"})
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = 0;
            int day = 0;
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int year, int month, int day) {

            thangnam.setText(String.valueOf(year));
        }
    }

    @SuppressLint({"ValidFragment", "NewApi"})
    public class DatePickerFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = 0;
            int day = 0;
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            edit_quy.setText(String.valueOf(year));
        }
    }

}
