import javax.swing.*;

public class VideoWatermark {

    private VideoWatermark(){
        Window window = new Window();

        String folder = "C:\\Users\\Hank\\Desktop\\Videos1";
        new VideoEditor(window, new Operation(folder, "hello [filename] \n [filename] is cool")).execute();


        /*
        String file = "C:\\Users\\Hank\\Desktop\\Videos2\\earth.mp4";
        new VideoEditor(window, new Operation(file, "hello [name] \n [name] is amazing",
                new String[] {"Bob", "Joe", "Larry"})).execute();
        */
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


