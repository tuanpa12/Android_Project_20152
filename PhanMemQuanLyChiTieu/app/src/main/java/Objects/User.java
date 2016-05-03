package Objects;

/**
 * Created by Legendary on 27/04/2016.
 */
public class User {
    private String name;
    private String email;
    private String key;
    private String maxExpense;
    private String maxIncome;

    public User() {
    }

    public User(String name, String email, String key, String maxExpense, String maxIncome) {
        this.name = name;
        this.email = email;
        this.key = key;
        this.maxExpense = maxExpense;
        this.maxIncome = maxIncome;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public String getMaxExpense() {
        return maxExpense;
    }

    public void setMaxExpense(String maxExpense) {
        this.maxExpense = maxExpense;
    }

    public String getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(String maxIncome) {
        this.maxIncome = maxIncome;
    }
}
