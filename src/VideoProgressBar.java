import javax.swing.*;

public class VideoProgressBar extends JProgressBar {

    private int totalVideos, videosComplete;

    public VideoProgressBar(){
        super(0, 0);
        setStringPainted(true);
        setVisible(false);
    }

    public void processStarted(int totalVideos){
        this.totalVideos = totalVideos;
        setMaximum(totalVideos);
        setString("0 of " + totalVideos + " complete...");
        setVisible(true);
    }

    public void videoCompleted(){
        videosComplete++;
        if(videosComplete == totalVideos) {
            reset();
            return;
        }
        setValue(videosComplete);
        setString(videosComplete + " of " + totalVideos + " complete...");
    }

    private void reset(){
        setVisible(false);
        setMaximum(0);
        totalVideos = 0;
        videosComplete = 0;
    }
}
