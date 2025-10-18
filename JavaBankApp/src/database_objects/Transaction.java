package database_objects;
import java.math.BigDecimal;
import java.sql.Date;
// It is used to store transaction data

public class Transaction {
    private final int userId;
    private final String transactionType;
    private final BigDecimal transactionAmount;
    private Date transactionDate;

    public Transaction(int userId, String transactionType,  BigDecimal transactionAmount, Date transactionDate){
        this.userId = userId;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public int getUserId(){
        return userId;
    }
    public String getTransactionType(){
        return transactionType;
    }
    public BigDecimal getTransactionAmount(){
        return transactionAmount;
    }
    public Date getTransactionDate(){
        return transactionDate;
    } 
}
