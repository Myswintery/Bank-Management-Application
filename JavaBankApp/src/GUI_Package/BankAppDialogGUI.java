package GUI_Package;
import database_objects.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;

// displays a custom dialog for our BankApp

public class BankAppDialogGUI extends JDialog implements ActionListener{
    private User user;
    private BankAppGUI bankappObj;
    private JLabel lblbalance, lblenterAmount, lblenteruser;
    private JTextField txtenterAmount, txtenterUser;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankAppDialogGUI(BankAppGUI bankappObj, User user){
        setSize(400, 400);

        // adding focus to dialog (can't interact with anything until the dialog is closed)
        setModal(true);
        setLocationRelativeTo(bankappObj);

        // when user closes dialoh, it releases the resources that were under usage
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // we will need refernce to our GUI so that we can update the current balance
        this.bankappObj = bankappObj;

        // accessing database to get user info
        this.user = user;
    }

    public void addCurrentBalanceAndAmount(){
        // balance label
        lblbalance = new JLabel("Balance: $" + user.getCurrentBalance());
        lblbalance.setBounds(0, 10, getWidth()-20, 20);
        lblbalance.setFont(new Font("Dialog", Font.BOLD, 16));
        lblbalance.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblbalance);

        // enter amount label
        lblenterAmount = new JLabel("Enter Amount:");
        lblenterAmount.setBounds(0, 50, getWidth()-20, 20);
        lblenterAmount.setFont(new Font("Dialog", Font.BOLD, 16));
        lblenterAmount.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblenterAmount);

        // enter amount text field
        txtenterAmount = new JTextField();
        txtenterAmount.setBounds(15, 80, getWidth()-50, 40);
        txtenterAmount.setFont(new Font("Dialog", Font.BOLD, 20));
        txtenterAmount.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtenterAmount);
    }

    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15,300, getWidth()-50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 18));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addUserTransfer(){
        // enter user label
        lblenteruser = new JLabel("Enter User:");
        lblenteruser.setBounds(0, 160, getWidth()-20, 20);
        lblenteruser.setFont(new Font("Dialog", Font.BOLD, 16));
        lblenteruser.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblenteruser);

        // enter user field
        txtenterUser = new JTextField();
        txtenterUser.setBounds(15, 190, getWidth()-50, 40);
        txtenterUser.setFont(new Font("Dialog", Font.BOLD, 20));
        txtenterUser.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtenterUser);
    }

    public void addPastTransactionComponents() {
        // Panel to hold all past transactions vertically
        pastTransactionPanel = new JPanel();
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));
        pastTransactionPanel.setBackground(Color.WHITE);

        // Retrieve past transactions from database
        pastTransactions = MyJDBC.getPastTransaction(user);

        // Add each transaction to the panel
        for (Transaction pastTransaction : pastTransactions) {
            // Container for individual transaction
            JPanel transactionContainer = new JPanel();
            transactionContainer.setLayout(new BoxLayout(transactionContainer, BoxLayout.Y_AXIS));
            transactionContainer.setBackground(Color.WHITE);
            transactionContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            transactionContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            // Transaction type
            JLabel lblType = new JLabel("Type: " + pastTransaction.getTransactionType());
            lblType.setFont(new Font("Dialog", Font.BOLD, 16));

            // Transaction amount
            JLabel lblAmount = new JLabel("Amount: $ " + String.format("%.2f", pastTransaction.getTransactionAmount()));
            lblAmount.setFont(new Font("Dialog", Font.PLAIN, 18));

            // Transaction date
            JLabel lblDate = new JLabel("Date: " + pastTransaction.getTransactionDate().toString());
            lblDate.setFont(new Font("Dialog", Font.ITALIC, 16));

            // Add labels to container
            transactionContainer.add(lblType);
            transactionContainer.add(lblAmount);
            transactionContainer.add(lblDate);

            // Add spacing between transactions
            pastTransactionPanel.add(transactionContainer);
            pastTransactionPanel.add(Box.createVerticalStrut(10));
        }

        // Scroll pane to wrap the transaction panel
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling

        // Add scroll pane to main container
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Refresh UI
        revalidate();
        repaint();

    }
    
    private void handleTransaction(String transactionType, float amount){
        Transaction transactionObj;

        if (transactionType.equals("Deposit")){
            // add to current balance
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amount)));

            // create transaction
            // putting null as we are using NOW() in sql which will get the current date 
            transactionObj = new Transaction(user.getId(), transactionType, new BigDecimal(amount), null);
        }
        else{
            // subtract from current balance
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));

            // since we want to show negative sign for amount withdrawn
            transactionObj = new Transaction(user.getId(), transactionType, new BigDecimal(-amount), null);
        }

        // update database
        if (MyJDBC.addTransactionToDatabase(transactionObj) && MyJDBC.updateCurrentBalance(user)){
            // show success dialog
            JOptionPane.showMessageDialog(this, transactionType + " successful!");

            // reset the fields
            resetFieldsAndUpdateCurrentBalance();
        }
        else{
            // show failure dialog
            JOptionPane.showMessageDialog(this, transactionType + " failed..");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance(){
        txtenterAmount.setText("");

        // only appears when transfer is clicked
        if(txtenterUser != null){
            txtenterUser.setText("");
        }

        // update current balance on dialog
        lblbalance.setText("Balance: $"+user.getCurrentBalance());

        // update current balance on main GUI
        bankappObj.getCurrBalance().setText("$" + user.getCurrentBalance());
    }

    private void handleTransfer(User user, String transferredUser, float amount){
        if(MyJDBC.transfer(user, transferredUser, amount)){
            // show success dialog
            JOptionPane.showMessageDialog(this, "Transfer Successful!");
            resetFieldsAndUpdateCurrentBalance();
        }
        else{
            // show failure message
            JOptionPane.showMessageDialog(this, "Transfer Failed...");
        }
    }

    public void actionPerformed(ActionEvent ae){
        String btnPressed = ae.getActionCommand();
        
        // get raw input from text field
        String input = txtenterAmount.getText().trim();

        // validate input is a number
        float amount;
        try {
            amount = Float.parseFloat(input);

            // also check if negative or zero
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount.");
                return;
            }
        } 
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number.");
            return;
        }

        // pressed deposit
        if(btnPressed.equals("Deposit")){
            // to handle the deposit transaction we make another function
            handleTransaction(btnPressed, amount);
        }
        else{
            // pressed withdraw or transfer
            // validate input by making sure that withdraw or transfer amount is less than current balance

            /*
             If result = -1 entered amount is greater than current balance
             If result = 0 entered amount is equal to balance
             If result - 1 entered amount is less than balance
             */

            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if(result < 0){
                // display error message
                JOptionPane.showMessageDialog(this, "Erro: Input value is more than current balance");
                return;
            }

            // if withdraw or transfer button was pressed
            if(btnPressed.equals("Withdraw")){
                handleTransaction(btnPressed, amount);
            }
            else{
                // transfer      
                String transferredUser = txtenterUser.getText().trim();
                if(transferredUser.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Please enter a valid user for transfer.");
                    return;
                }
                handleTransfer(user, transferredUser, amount);
            }
        }
    }
}
