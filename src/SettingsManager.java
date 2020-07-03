import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SettingsManager {

    private final static int DEFAULT_TEXT_SIZE = 55;
    private final static int DEFAULT_ALPHA = 127;

    public static void showOptions(){
        int[] options = readOptions();
        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(options[0], 10, 150, 1));
        JSpinner alphaSpinner = new JSpinner(new SpinnerNumberModel(options[1], 0, 255, 1));

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Text size:"));
        myPanel.add(sizeSpinner);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Transparency: "));
        myPanel.add(alphaSpinner);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Options", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("Text size: " + sizeSpinner.getValue());
            System.out.println("Transparency: " + alphaSpinner.getValue());
            writeOptions(new int[] {(int) sizeSpinner.getValue(), (int) alphaSpinner.getValue()});
        }
    }

    public static int[] readOptions(){
        try {
            File myObj = new File("data/options.txt");
            if(!myObj.exists()) {
                writeOptions(new int[] {DEFAULT_TEXT_SIZE, DEFAULT_ALPHA});
                return new int[] {DEFAULT_TEXT_SIZE, DEFAULT_ALPHA};
            }

            Scanner myReader = new Scanner(myObj);
            String[] split = myReader.nextLine().split(",");

            int[] options = new int[split.length];
            for(int i = 0; i < split.length; i++) options[i] = Integer.parseInt(split[i].trim());
            myReader.close();
            return options;
        } catch (IOException e) {
            e.printStackTrace();
            return new int[] {DEFAULT_TEXT_SIZE, DEFAULT_ALPHA};
        }
    }

    private static void writeOptions(int[] options) {
        try {
            FileWriter myWriter = new FileWriter("data/options.txt");
            myWriter.write(options[0] + ", " + options[1]);
            myWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
