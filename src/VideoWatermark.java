import javax.swing.*;

public class VideoWatermark {

    private VideoWatermark(){
        Window window = new Window();
        //window.watermarkingStarted();

        /*
        String folder = "C:\\Users\\Hank\\Desktop\\Videos1";
        new VideoEditor(window, new Operation(folder, "hello [filename] \n [name] is cool")).execute();
        */

        /*
        String folder = "C:\\Users\\Hank\\Desktop\\Videos2";
        new VideoEditor(window, new Operation(folder, "hello [name] \n [name] is amazing \n [filename] is also amazing",
                new String[] {"Bob", "Joe", "Larry"})).execute();
        */

        /*
        String folder = "C:\\Users\\Hank\\Desktop\\Videos1";
        new VideoEditor(window, new Operation(folder, "This is [filename] file \n also [name] is cool",
                new String[] {"Gary"})).execute();
         */

        /*
        String file = "C:\\Users\\Hank\\Desktop\\Videos2\\ocean.mp4";
        window.watermarkingStarted();
        new VideoEditor(window, new Operation(file, "[name][name][name]",
                new String[] {"Bob"})).execute();
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


