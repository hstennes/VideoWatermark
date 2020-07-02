import javax.swing.*;

public class VideoWatermark {

    private VideoWatermark(){
        Window window = new Window();

        String folder = "C:\\Users\\Hank\\Desktop\\Videos";
        new VideoEditor(window, new Operation(true, folder, "hello [filename] \n [filename] is cool", new String[0])).execute();
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(VideoWatermark::new);
    }
}


