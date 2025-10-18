package GUI_Package;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import database_objects.*;

public class RegisterGUI extends BaseFrame{
    public RegisterGUI(){
        super("Banking App Registration");
    }

    @Override
    protected void addGUIComponents(){
        JLabel AppTitle = new JLabel("Banking Application");
        // getWidth() will return frame's width which set to 420 in BaseFrame
        AppTitle.setBounds(0, 20, super.getWidth(), 40);
        // Setting font style [Dialog is a Logical Font (built-in)]
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
        lblpwd.setBounds(20, 220, getWidth()-50, 24);
        lblpwd.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lblpwd);

        JPasswordField txtpwd = new JPasswordField();
        txtpwd.setBounds(20, 260, getWidth()-50, 40);
        txtpwd.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(txtpwd);

        JLabel lblrepwd = new JLabel("Re-type Password: ");
        lblrepwd.setBounds(20, 320, getWidth()-50, 40);
        lblrepwd.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lblrepwd);

        JPasswordField txtrepwd = new JPasswordField();
        txtrepwd.setBounds(20, 360, getWidth()-50, 40);
        txtrepwd.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(txtrepwd);
        
        JButton btnregi = new JButton("Register");
        btnregi.setBounds(20, 460, getWidth()-50, 40);
        btnregi.setFont(new Font("Dialog", Font.BOLD, 20));

        btnregi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                // get username
                String username = txtuser.getText();

                // get password
                String password = String.valueOf(txtpwd.getPassword());

                // get re password
                String rePassword = String.valueOf(txtrepwd.getPassword());

                // validate user input
                if (validateRegister(username, password, rePassword)){
                    // register the user to the database
                    if (MyJDBC.register(username,password)){
                        // register success - dispose this gui
                        RegisterGUI.this.dispose();
                        //launchLogin GUI
                        LoginGUI loginui = new LoginGUI();
                        loginui.setVisible(true);

                        // create a result dialog
                        JOptionPane.showMessageDialog(loginui, "Account Registered Successfully!");
                    }
                    else{
                        JOptionPane.showMessageDialog(RegisterGUI.this, "Error: Username already taken ");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(RegisterGUI.this, "Password must match the re-entered password");
                }
            }
        });

        add(btnregi);

        // We can actually use HTML inside Swing!
        JLabel lbllogin = new JLabel("<html><a href=\"#\">Have an account? Sign-In here</a>");
        lbllogin.setBounds(0, 510, getWidth()-10, 30);
        lbllogin.setFont(new Font("Dialog", Font.PLAIN, 20));
        lbllogin.setHorizontalAlignment(SwingConstants.CENTER);
        lbllogin.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent me){
            // dispose this gui
            RegisterGUI.this.dispose();
            // launch login gui
            new LoginGUI().setVisible(true);
           } 
        });
        add(lbllogin);
    }

    private boolean validateRegister(String username, String password, String rePassword){
        // all fields must have a value
        if (username.length()==0 || password.length()==0 || rePassword.length()==0) return false;

        // password and repassword must be same
        if (!password.equals(rePassword)) return false;

        // passes validation
        return true;
    }
}
