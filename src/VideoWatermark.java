import javax.swing.*;

public class VideoWatermark {

    public static String OS = System.getProperty("os.name").toLowerCase();

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


