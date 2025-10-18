package database_objects;
import java.math.*;

public class User {
    //Making the table columns
    private final int id;
    private final String username, password;
    private BigDecimal currentBalance;// the cuurent Balance shouldnt be final as it keeps changing

    public User(int id, String username, String password, BigDecimal currrentBalance){
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentBalance = currrentBalance;
    }

    // Getter Methods - that return the value of a private field in a class
    public int getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public BigDecimal getCurrentBalance(){
        return currentBalance;
    }

    // Making Setter method to update the value of current balance continously in the app
    public void setCurrentBalance(BigDecimal newBalance){
        currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
    }
}
