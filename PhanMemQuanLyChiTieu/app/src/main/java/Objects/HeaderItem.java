package Objects;

/**
 * Created by Legendary on 05/05/2016.
 */
public class HeaderItem {
    private String date;
    private String total;

    public HeaderItem() {
        super();
    }

    public HeaderItem(String date, String total) {
        this.date = date;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
