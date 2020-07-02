import javax.swing.*;
import java.util.ArrayList;

public class NamesField extends JTextField {

    public String[] getNames(){
        String[] split = getText().split(",");
        ArrayList<String> nameList = new ArrayList<>();
        for(String str : split){
            String str2 = str.trim();
            if(!str2.equals("")) nameList.add(str2);
        }
        String[] nameArray = new String[nameList.size()];
        for(int i = 0; i < nameList.size(); i++) nameArray[i] = nameList.get(i);
        return nameArray;
    }

}
