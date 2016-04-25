package Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Object.BaoCao;
import Object.TienThuChi;


public class DoiNgay {
    public String doiDate(String date) {
        String str = date.substring(6);
        String str1 = date.substring(3, 5);
        String str2 = date.substring(0, 2);
        String kq = str + str1 + str2;
        return kq;
    }

    public String doiThang(String date) {
        String str = date.substring(6);
        String str1 = date.substring(3, 5);
        String kq = str + str1;
        return kq;
    }

    public String doiThang1(String date) {
        String str = date.substring(0, 4);
        String str1 = date.substring(4, 6);
        String kq = str + str1;
        return kq;
    }

    public String doiNam(String date) {
        String str = date.substring(6);
        String kq = str;
        return kq;
    }

    public String doiNam1(String date) {
        String str = date.substring(0, 4);
        String kq = str;
        return kq;
    }

    public String ngay(String string) {
        String chuoi = string.substring(6) + "/" + string.substring(4, 6) + "/" + string.substring(0, 4);
        return chuoi;

    }

    public String thang(String string) {
        String chuoi = string.substring(4, 6) + "/" + string.substring(0, 4);
        return chuoi;

    }

    public String nam(String string) {
        String chuoi = string.substring(0, 4);
        return chuoi;

    }

    public ArrayList<TienThuChi> sapXep(ArrayList<TienThuChi> arrinfo) {
        Collections.sort(arrinfo, new Comparator<TienThuChi>() {
            @Override
            public int compare(TienThuChi lhs, TienThuChi rhs) {
                if (Float.parseFloat(lhs.getNgaythang()) < Float.parseFloat(rhs.getNgaythang())) {
                    return 1;
                } else {
                    if (Float.parseFloat(lhs.getNgaythang()) == Float.parseFloat(rhs.getNgaythang())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        return arrinfo;
    }

    public ArrayList<BaoCao> sapXep1(ArrayList<BaoCao> arrchi) {
        Collections.sort(arrchi, new Comparator<BaoCao>() {
            @Override
            public int compare(BaoCao lhs, BaoCao rhs) {
                if (Float.parseFloat(lhs.getNgay()) < Float.parseFloat(rhs.getNgay())) {
                    return 1;
                } else {
                    if (Float.parseFloat(lhs.getNgay()) == Float.parseFloat(rhs.getNgay())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        return arrchi;
    }

    public ArrayList<BaoCao> sapXepnam(ArrayList<BaoCao> arrhientai) {
        Collections.sort(arrhientai, new Comparator<BaoCao>() {
            @Override
            public int compare(BaoCao lhs, BaoCao rhs) {
                if (Float.parseFloat(lhs.getNgay()) < Float.parseFloat(rhs.getNgay())) {
                    return 1;
                } else {
                    if (Float.parseFloat(lhs.getNgay()) == Float.parseFloat(rhs.getNgay())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        return arrhientai;
    }
}