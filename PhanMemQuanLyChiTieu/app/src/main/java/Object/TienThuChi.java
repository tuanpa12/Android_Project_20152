package Object;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Adapter.DoiNgay;

public class TienThuChi {
    private String ten;
    private String tien;
    private String nhom;
    private String ghichu;
    private String ngaythang;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTien() {
        return this.tien;
    }

    public void setTien(String tien) {
        this.tien = tien;
    }

    public String getVND() {
        return this.tien + " (VND)";
    }

    public String getUSD() {
        return fromVND(this.tien);
    }

    public String fromVND(String str) {
        String cd;
        Float f = Float.parseFloat(str);
        float a = f / 22000;
        DecimalFormat first = new DecimalFormat("#.##");
        String format_string = first.format(a);
        cd = "" + format_string + " (USD)";
        return cd;
    }

    public String getNgoaiTe(String string) {
        String m = "";
        HTMLParser htmlParser = new HTMLParser();
        ArrayList<NgoaiTe> moneys = new ArrayList<NgoaiTe>();
        try {
            moneys = htmlParser.parseHTML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (NgoaiTe money : moneys) {
            if (money.kiHieu.equals(string)) {
                m = money.fromVND(getTien());
            }
        }
        return m;
    }

    public String getTyGia(String string) {
        String m = "";
        HTMLParser htmlParser = new HTMLParser();
        ArrayList<NgoaiTe> moneys = new ArrayList<NgoaiTe>();
        try {
            moneys = htmlParser.parseHTML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (NgoaiTe money : moneys) {
            if (money.kiHieu.equals(string)) {
                m = money.tyGia + " (VND)";
            }
        }
        return m;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getNgaythang() {
        return ngaythang;
    }

    public void setNgaythang(String ngaythang) {
        this.ngaythang = ngaythang;
    }

    public double kqngay(String nhom, String ngay, ArrayList<BaoCao> a) {
        double thuthang = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getNgay().equals(ngay) && a.get(i).getNhom().equals(nhom)) {
                thuthang += Double.parseDouble(a.get(i).getTienchi());
            }
        }
        return thuthang;
    }

    public double kqthang(String nhom, String thang, ArrayList<BaoCao> a) {
        double thuthang = 0;
        DoiNgay doingaychi = new DoiNgay();
        for (int i = 0; i < a.size(); i++) {
            if (doingaychi.doiThang1(a.get(i).getNgay()).equals(thang) && a.get(i).getNhom().equals(nhom)) {
                thuthang += Double.parseDouble(a.get(i).getTienchi());
            }
        }
        return thuthang;
    }

    public double kqquy(String nhom, String thangdau, String thangsau, String nam, ArrayList<BaoCao> a) {
        double thuthang = 0;
        DoiNgay doiquychi = new DoiNgay();
        String dau = nam + thangdau;
        double quydau = Double.parseDouble(dau);
        String cuoi = nam + thangsau;
        double quycuoi = Double.parseDouble(cuoi);
        DoiNgay doingaychi = new DoiNgay();
        for (int i = 0; i < a.size(); i++) {
            if (Double.parseDouble(doiquychi.doiThang1(a.get(i).getNgay())) >= quydau && Double.parseDouble(doiquychi.doiThang1(a.get(i).getNgay())) <= quycuoi && a.get(i).getNhom().equals(nhom)) {
                thuthang += Double.parseDouble(a.get(i).getTienchi());
            }
        }
        return thuthang;
    }

    public double kqdsthang(String nhom, String nam, String thang, ArrayList<BaoCao> a) {
        double thuthang = 0;
        String thangnam = nam + thang;
        DoiNgay doingaychi = new DoiNgay();
        for (int i = 0; i < a.size(); i++) {
            if (doingaychi.doiThang1(a.get(i).getNgay()).equals(thangnam) && a.get(i).getNhom().equals(nhom)) {
                thuthang += Double.parseDouble(a.get(i).getTienchi());
            }
        }
        return thuthang;

    }

    public double kqnam(String nhom, String nam, ArrayList<BaoCao> a) {
        double thuthang = 0;
        DoiNgay doingaychi = new DoiNgay();
        for (int i = 0; i < a.size(); i++) {
            if (doingaychi.doiNam1(a.get(i).getNgay()).equals(nam) && a.get(i).getNhom().equals(nhom)) {
                thuthang += Double.parseDouble(a.get(i).getTienchi());
            }
        }
        return thuthang;
    }
}





