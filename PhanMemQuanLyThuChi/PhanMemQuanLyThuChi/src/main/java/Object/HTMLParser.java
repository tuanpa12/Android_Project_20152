package Object;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.os.StrictMode;

public class HTMLParser {
    static public NgoaiTe chuyendoi(Element e1, Element e2, Element e3) {
        float b;
        try {
            String[] a = e3.text().split(",");
            b = Float.parseFloat(a[0] + a[1]);
        } catch (Exception e) {
            b = Float.parseFloat(e3.text());
        }
        NgoaiTe m = new NgoaiTe(e1.text(), e2.text(), b);
        return m;
    }

    @SuppressLint("NewApi")
    public ArrayList<NgoaiTe> parseHTML() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ArrayList<NgoaiTe> moneys = new ArrayList<NgoaiTe>();
        Document doc;
        try {
            doc = Jsoup.connect("https://www.vietcombank.com.vn/exchangerates/").get();
            Elements links = doc.select("td");
            for (int i = 0; i < 95; i = i + 5) {
                NgoaiTe m = chuyendoi(links.get(i), links.get(i + 1), links.get(i + 4));
                moneys.add(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            moneys.add(new NgoaiTe("VND", "VietNam Dong", 1));
        }
        return moneys;
    }
}
