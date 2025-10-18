package GUI_Package;
import database_objects.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* Performs banking functions- depositing, withdrawimg, viewing transaction history and transferring */

public class BankAppGUI extends BaseFrame implements ActionListener{
    private JTextField txtCurrentBalance;
    public JTextField getCurrBalance(){ return txtCurrentBalance; }

    public BankAppGUI(User user){
        super("Banking App", user);
    }
    @Override
    protected void addGUIComponents(){
        //Welcome Message via HTML Tags
        String welcome = "<html>"+
        "<body style='text-align:center'>"+
        "<b>Hello " + user.getUsername() + "</b><br>" +
        "Have a safe transaction!</body></html>";

        JLabel lblwelcomeMsg =  new JLabel(welcome);
        lblwelcomeMsg.setBounds(0, 20, getWidth()-10, 40);
        lblwelcomeMsg.setFont(new Font("Dialog", Font.PLAIN, 16));
        lblwelcomeMsg.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblwelcomeMsg);

        JLabel lblCurrentBalance = new JLabel("Current Balance: ");
        lblCurrentBalance.setBounds(0, 80, getWidth()-10, 30);
        lblCurrentBalance.setFont(new Font("Dialog", Font.BOLD, 22));
        lblCurrentBalance.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCurrentBalance);

        txtCurrentBalance = new JTextField("$" + user.getCurrentBalance());
        txtCurrentBalance.setBounds(15, 120, getWidth()-50, 40);
        txtCurrentBalance.setFont(new Font("Dialog", Font.BOLD, 28));
        txtCurrentBalance.setHorizontalAlignment(SwingConstants.RIGHT);
        add(txtCurrentBalance);
        txtCurrentBalance.setEditable(false);  // Disable interaction

        JButton btnDeposit = new JButton("Deposit");
        btnDeposit.setBounds(15, 180, getWidth()-50, 50);
        btnDeposit.setFont(new Font("Dialog", Font.BOLD, 22));
        btnDeposit.addActionListener(this);
        add(btnDeposit);

        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.setBounds(15, 250, getWidth()-50, 50);
        btnWithdraw.setFont(new Font("Dialog", Font.BOLD, 22));
        btnWithdraw.addActionListener(this);
        add(btnWithdraw);

        JButton btnTransfer = new JButton("Transfer Amount");
        btnTransfer.setBounds(15, 320, getWidth()-50, 50);
        btnTransfer.setFont(new Font("Dialog", Font.BOLD, 22));
        btnTransfer.addActionListener(this);
        add(btnTransfer);

        JButton btnPastTransactions = new JButton("Past Transactions");
        btnPastTransactions.setBounds(15, 390, getWidth()-50, 50);
        btnPastTransactions.setFont(new Font("Dialog", Font.BOLD, 22));
        btnPastTransactions.addActionListener(this);
        add(btnPastTransactions);

        JButton btnLogout = new JButton("Log Out");
        btnLogout.setBounds(15, 500, getWidth()-50, 50);
        btnLogout.setFont(new Font("Dialog", Font.BOLD, 22));
        btnLogout.addActionListener(this);
        add(btnLogout);
    }

    public void actionPerformed(ActionEvent ae){
        String btnPressed = ae.getActionCommand();

        // user pressed logout
        if(btnPressed.equals("Log Out")){
            // return user to the login gui
            this.dispose();
            new LoginGUI().setVisible(true);
            return;  // stops processing anything further on
        }

        // other functions

        BankAppDialogGUI bankdialogObj = new BankAppDialogGUI(this, user);
        //set the title of dialog header to the action
        bankdialogObj.setTitle(btnPressed);

        if(btnPressed.equals("Deposit") || btnPressed.equals("Withdraw") || btnPressed.equals("Transfer Amount")){
            // add in current balance and amount components to the dialog
            bankdialogObj.addCurrentBalanceAndAmount();

            // add action button
            bankdialogObj.addActionButton(btnPressed);

            // for the transfer action, it will require more components
            if(btnPressed.equals("Transfer Amount")){
                bankdialogObj.addUserTransfer();
            }
        }
        else if (btnPressed.equals("Past Transactions")){
            bankdialogObj.addPastTransactionComponents();
        }
        bankdialogObj.setVisible(true);
    }
}