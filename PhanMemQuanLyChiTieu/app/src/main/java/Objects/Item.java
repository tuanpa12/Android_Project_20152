package Objects;

/**
 * Created by Legendary on 27/04/2016.
 */
public class Item {
    private String name;
    private String cost;
    private String type;
    private String note;
    private String date;
    private int id;

    public Item() {
    }

    public Item(String name, String cost, String type, String note, String date, int id) {
        this.name = name;
        this.cost = cost;
        this.type = type;
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
