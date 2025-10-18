package javabankapp;
import GUI_Package.*;
import javax.swing.*;

public class JavaBankApp {

    public static void main(String[] args) {
        // Using invokeLater to make updates to the GUI more thread safe
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new LoginGUI().setVisible(true);
            }
        });
    }
    
}
