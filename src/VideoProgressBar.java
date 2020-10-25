import javax.swing.*;

public class VideoProgressBar extends JProgressBar {

    private int totalVideos, videosComplete;

    public VideoProgressBar(){
        super(0, 0);
        setStringPainted(true);
        setVisible(false);
    }

    public void processStarted(int totalVideos){
        reset();
        this.totalVideos = totalVideos;
        setMaximum(totalVideos);
        setString("0 of " + totalVideos + " complete...");
        setVisible(true);
    }

    public void videoCompleted(){
        videosComplete++;
        if(videosComplete >= totalVideos) {
            setValue(totalVideos);
            setString("Done!");
            return;
        }
        setValue(videosComplete);
        setString(videosComplete + " of " + totalVideos + " complete...");
    }

    public void error(){
        setString("An error occurred");
        setValue(totalVideos);
    }

    private void reset(){
        setMaximum(0);
        totalVideos = 0;
        videosComplete = 0;
    }
}
