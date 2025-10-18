package GUI_Package;
import database_objects.*;
import javax.swing.*;

// Setting an abstract classas in many GUIs the sizes and colours will be same
public abstract class BaseFrame extends JFrame{

    // Store user information
    protected User user;
    public BaseFrame(String Title, User user){
        this.user =  user;
        initialize(Title);
    }

    public BaseFrame(String title){
        initialize(title);
    }
    private void initialize(String title){
        setTitle(title);
        setSize(420, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  //terminate program when x is clicked
        setLayout(null); // Null allows to manually specifythe size and position of each gui component
        setResizable(false); // Prevent resizing
        setLocationRelativeTo(null); // Launch the window at the screen center
        addGUIComponents();
    }

    protected abstract void addGUIComponents(); //Each subclass will have to define it accordingly.

}