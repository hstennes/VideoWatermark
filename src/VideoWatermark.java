import javax.swing.*;

//RELEASE 1.4.1
public class VideoWatermark {

    public static String OS = System.getProperty("os.name").toLowerCase();
    public static String VERSION = "v1.4";

    private VideoWatermark(){
        new Window();
    }

    public static void main(String[] args){
        if(OS.contains("win")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(VideoWatermark::new);
    }
}


