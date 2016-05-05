package Objects;

/**
 * Created by Legendary on 05/05/2016.
 */
public class ChildItem {
    private String type;
    private String cost;

    public ChildItem() {
        super();
    }

    public ChildItem(String type, String cost) {
        this.type = type;
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
