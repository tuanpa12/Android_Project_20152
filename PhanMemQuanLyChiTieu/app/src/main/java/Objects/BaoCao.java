package Objects;

import java.util.ArrayList;

import Adapter.DoiNgay;

public class BaoCao {
    private String title;
    private String ngay;
    private String tienthu;
    private String tienchi;
    private String nhom;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getTienthu() {
        return tienthu;
    }

    public void setTienthu(String tienthu) {
        this.tienthu = tienthu;
    }

    public String getTienchi() {
        return tienchi;
    }

    public void setTienchi(String tienchi) {
        this.tienchi = tienchi;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public int thuThangThu(String nam, ArrayList<BaoCao> a, String thang) {
        DoiNgay doi = new DoiNgay();
        int sum = 0;
        String thangnam = nam + thang;
        for (int i = 0; i < a.size(); i++) {
            if (doi.doiThang1(a.get(i).getNgay()).equals(thangnam)) {
                sum += Integer.parseInt(a.get(i).getTienthu());
            }
        }
        return sum;
    }

    public int thuThangChi(String nam, ArrayList<BaoCao> a, String thang) {
        DoiNgay doi = new DoiNgay();
        int sum = 0;
        String thangnam = nam + thang;
        for (int i = 0; i < a.size(); i++) {
            if (doi.doiThang1(a.get(i).getNgay()).equals(thangnam)) {
                sum += Integer.parseInt(a.get(i).getTienchi());
            }
        }
        return sum;
    }

    public int thuQuy(String thangdau, String thangcuoi, String nam, ArrayList<BaoCao> a) {
        int thu = 0;
        DoiNgay doi = new DoiNgay();
        String dau = nam + thangdau;
        String cuoi = nam + thangcuoi;
        for (int i = 0; i < a.size(); i++) {
            if (Integer.parseInt(doi.doiThang1(a.get(i).getNgay())) >= Integer.parseInt(dau) && Integer.parseInt(doi.doiThang1(a.get(i).getNgay())) <= Integer.parseInt(cuoi)) {
                thu += Integer.parseInt(a.get(i).getTienthu());
            }
        }
        return thu;
    }

    public int chiQuy(String thangdau, String thangcuoi, String nam, ArrayList<BaoCao> a) {
        int thu = 0;
        DoiNgay doi = new DoiNgay();
        String dau = nam + thangdau;
        String cuoi = nam + thangcuoi;
        for (int i = 0; i < a.size(); i++) {
            if (Integer.parseInt(doi.doiThang1(a.get(i).getNgay())) >= Integer.parseInt(dau) && Integer.parseInt(doi.doiThang1(a.get(i).getNgay())) <= Integer.parseInt(cuoi)) {
                thu += Integer.parseInt(a.get(i).getTienchi());
            }
        }
        return thu;
    }
}
