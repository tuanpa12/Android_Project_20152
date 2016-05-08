package Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Objects.BaoCao;
import Objects.TienThuChi;


public class DoiNgay {
    public String doiDate(String date) {
        String str = date.substring(6);
        String str1 = date.substring(3, 5);
        String str2 = date.substring(0, 2);
        return str + str1 + str2;
    }

    public String doiThang(String date) {
        String str = date.substring(6);
        String str1 = date.substring(3, 5);
        return str + str1;
    }

    public String doiThang1(String date) {
        String str = date.substring(0, 4);
        String str1 = date.substring(4, 6);
        return str + str1;
    }

    public String doiNam(String date) {
        return date.substring(6);
    }

    public String doiNam1(String date) {
        return date.substring(0, 4);
    }

    public String ngay(String string) {
        return string.substring(6) + "/" + string.substring(4, 6) + "/" + string.substring(0, 4);

    }

    public String thang(String string) {
        return string.substring(4, 6) + "/" + string.substring(0, 4);

    }

    public String nam(String string) {
        return string.substring(0, 4);

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