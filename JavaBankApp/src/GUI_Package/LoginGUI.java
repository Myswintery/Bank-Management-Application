package GUI_Package;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import database_objects.*;

public class LoginGUI extends BaseFrame{
    public LoginGUI(){
        super("Banking App Login");
    }

    protected void addGUIComponents(){
        JLabel AppTitle = new JLabel("Banking Application");
        // getWidth() will return frame's width which set to 420 in BaseFrame
        AppTitle.setBounds(0, 20, super.getWidth(), 40);
        // Setting font style [Dialog is a Logical Font (built-in)]
        // The 'new Font(...)' constructs the font, and 'setFont(...)' applies it
        AppTitle.setFont(new Font("Dialog", Font.BOLD, 32));  
        AppTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(AppTitle);

        JLabel lbluser = new JLabel("Enter Username: ");
        lbluser.setBounds(20, 120, getWidth()-30, 24);
        lbluser.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lbluser);

        JTextField txtuser = new JTextField();
        txtuser.setBounds(20, 160, getWidth()-50, 40);
        txtuser.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(txtuser);

        JLabel lblpwd = new JLabel("Enter Password: ");
        lblpwd.setBounds(20, 280, getWidth()-50, 24);
        lblpwd.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lblpwd);

        JPasswordField txtpwd = new JPasswordField();
        txtpwd.setBounds(20, 320, getWidth()-50, 40);
        txtpwd.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(txtpwd);

        JButton btnlogin = new JButton("Login");
        btnlogin.setBounds(20, 460, getWidth()-50, 40);
        btnlogin.setFont(new Font("Dialog", Font.BOLD, 20));
        btnlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                // get username
                String username = txtuser.getText();

                // get password
                String password = String.valueOf(txtpwd.getPassword());

                // validate login
                User user = MyJDBC.validateLogin(username, password);

                // if user in null it means invalid otherwise it is a valid account
                if (user != null){
                    // dispose this gui
                    LoginGUI.this.dispose();
                    
                    // launch bank app gui
                    BankAppGUI bankapp = new BankAppGUI(user);
                    bankapp.setVisible(true);

                    // show success dialog
                    JOptionPane.showMessageDialog(bankapp, "Login Successful!");
                }
                else{
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login Failed!");
                }
            }
        });
        add(btnlogin);

        // We can actually use HTML inside Swing!
        JLabel lblregister = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a>");
        lblregister.setBounds(0, 510, getWidth()-10, 30);
        lblregister.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblregister.setHorizontalAlignment(SwingConstants.CENTER);

        // adds an event listener so when the mouse is clicked it will launch the register gui 
        lblregister.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                // dispose of LoginGUI
                LoginGUI.this.dispose();

                // launch the register gui
                new RegisterGUI().setVisible(true);
            }
        });

        add(lblregister);
    }
}