import javax.swing.*;

public class NamesField extends JTextField {

    public String[] getNames(){
        String[] names = getText().split(",");
        for(int i = 0; i < names.length; i++) names[i] = names[i].trim();
        return names;
    }

}
