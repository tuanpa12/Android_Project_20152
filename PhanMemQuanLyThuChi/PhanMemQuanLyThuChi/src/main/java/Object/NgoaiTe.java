package Object;

import java.text.DecimalFormat;

public class NgoaiTe {
    public String kiHieu;
    String name;
    float tyGia;

    public NgoaiTe(String kiHieu, String name, float tyGia) {
        this.name = name;
        this.kiHieu = kiHieu;
        this.tyGia = tyGia;
    }

    public String fromVND(String str) {
        String cd;
        Float f = Float.parseFloat(str);
        float a = f / this.tyGia;
        DecimalFormat first = new DecimalFormat("#.###");
        String format_string = first.format(a);
        cd = "" + format_string + "-" + this.kiHieu;
        return cd;
    }

    @Override
    public String toString() {
        String s = kiHieu + " - " + tyGia + " - " + name;
        return s;
    }

}
