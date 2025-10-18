package database_objects;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.util.ArrayList;

// JDBC Class is used to interact with our MySQL Database to perform activities such as retrieving and updating our databse
public class MyJDBC {
    private static final String DB_URL =  "jdbc:mysql://localhost:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "myswintery";

    // if valid return an object with the user's information
    public static User validateLogin(String username, String password){
        try{
            //establish a connection to the database using configurations
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // SQL Query
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            // replace the ? with values
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // execute query and store into a result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // next() returns true/false
            // true - query returns data and result set now points to the first row
            // false - query returned no data and result set equals to null
            if(resultSet.next()){
                // Success
                // get id
                int userId = resultSet.getInt("id");

                // get current balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // return user object
                return  new User(userId, username, password, currentBalance);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }

        // not valid user
        return null;
    }

    // registers a new user to the database
    public static boolean register(String username, String password){
        try{
            // first we will check if the username has already been taken
            if(!checkUser(username)){
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password, current_balance)" + 
                    "VALUES(?, ?, ?)"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();
                return true;
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return false;
    }

    private static boolean checkUser(String username){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return true;
    }

    public static boolean addTransactionToDatabase(Transaction transactionObj){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement insertTransaction = connection.prepareStatement(
                "INSERT INTO transactions(user_Id, transaction_type, transaction_amount, transaction_date) " + 
                "VALUES(?, ?, ?, NOW())"
            );

            insertTransaction.setInt(1, transactionObj.getUserId());
            insertTransaction.setString(2, transactionObj.getTransactionType());
            insertTransaction.setBigDecimal(3, transactionObj.getTransactionAmount());
            insertTransaction.executeUpdate();

            return true;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return false;
    }

    public static boolean updateCurrentBalance(User user){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement
            updateBalance = connection.prepareStatement(
                "UPDATE users SET current_balance = ? WHERE id = ?"
            );
            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());
            updateBalance.executeUpdate();
            return true;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return false;
    }

    public static boolean transfer(User user, String transferredUsername, float transferAmount){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement queryUser = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ?"
            );
            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while (resultSet.next()){
                // perform transfer
                User transferredUser = new User(
                    resultSet.getInt("id"),
                    transferredUsername,
                    resultSet.getString("password"),
                    resultSet.getBigDecimal("current_balance")
                );

                // create transaction
                Transaction transferTransaction = new Transaction(
                    user.getId(),
                    "Transfer",
                    new BigDecimal(-transferAmount),
                    null
                );

                // this transaction belongs to the receiver
                Transaction receivedTransaction = new Transaction(transferredUser.getId(),
                "Transfer",
                new BigDecimal(transferAmount),
                null
                );

                // update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                // update user current balance
                 user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                // add these transactions to the database
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);

                return true;
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return false; 
    }

    // used for past transaction
    public static ArrayList<Transaction> getPastTransaction(User user){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement
            selectAllTransaction = connection.prepareStatement(
                "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getId());
            ResultSet resultSet = selectAllTransaction.executeQuery();
            
            // iterate through the results if many
            while(resultSet.next()){
                // create transaction object
                Transaction transactionObj = new Transaction(user.getId(),
                resultSet.getString("transaction_type"),
                resultSet.getBigDecimal("transaction_amount"),
                resultSet.getDate("transaction_date")
                );
                // store into arraylist
                pastTransactions.add(transactionObj);
            }

        }
        catch(SQLException se){se.printStackTrace();}
        return pastTransactions;
    } 
}