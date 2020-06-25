import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoEditor extends SwingWorker<Void, Integer> {

    private File videoFolder;
    private Window window;

    public VideoEditor(Window window, File videoFolder){
        this.window = window;
        this.videoFolder = new File(videoFolder.getAbsolutePath());
    }

    @Override
    protected Void doInBackground() throws Exception {
        File[] files = videoFolder.listFiles();
        if(files == null) return null;
        ArrayList<File> mp4s = new ArrayList<>();
        for (File file : files) if (file.getAbsolutePath().endsWith(".mp4")) mp4s.add(file);
        publish(mp4s.size());
        for (File file : mp4s) watermarkVideo(file.getAbsolutePath());
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

    private void watermarkVideo(String videoPath) throws IOException, InterruptedException {
        String newPath = videoPath.split("\\.")[0] + "_new.mp4";
        Process p = Runtime.getRuntime().exec("ffmpeg\\ffmpeg-20200620-29ea4e1-win64-static\\bin\\ffmpeg -y -i \"" + videoPath +
                "\" -i watermark.png -filter_complex \"overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2\" \"" + newPath + "\"");
        PipeStream out = new PipeStream(p.getInputStream(), System.out);
        PipeStream err = new PipeStream(p.getErrorStream(), System.err);
        out.start();
        err.start();
        p.waitFor();
        publish();
    }
}

class PipeStream extends Thread {
    InputStream is;
    OutputStream os;

    public PipeStream(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int len;
        try {
            while((len = is.read(buffer)) >= 0){
                os.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
