import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoEditor extends SwingWorker<Void, Integer> {

    private Window window;
    private String text;
    private File videoFolder;

    public VideoEditor(Window window, String text, File videoFolder){
        this.window = window;
        this.text = text;
        this.videoFolder = new File(videoFolder.getAbsolutePath());
    }

    @Override
    protected Void doInBackground() {
        try {
            File[] files = videoFolder.listFiles();
            if (files == null) return null;
            ArrayList<File> mp4s = new ArrayList<>();
            for (File file : files) if (file.getAbsolutePath().endsWith(".mp4")) mp4s.add(file);
            publish(mp4s.size());
            for (File file : mp4s) {
                String path = file.getAbsolutePath();
                Watermarker.watermarkVideoWin(text, path, path.split("\\.")[0] + "_new.mp4");
                publish();
            }
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        if(chunks.size() > 0) {
            if(chunks.get(0) == 0) window.noVideosInFolder();
            else window.getProgessBar().processStarted(chunks.get(0));
        }
        else window.getProgessBar().videoCompleted();
    }

    @Override
    protected void done() {
        window.watermarkingDone();
    }
}


