import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Watermarker {

    private String prevText;

    /**
     * Watermarks the video using the given text (newline characters create new lines in the watermark)
     * @param text The watermark text
     * @param videoPath The path of the video to watermark
     * @param newPath The location to which the watermarked video will be saved
     */
    public void watermarkVideo(@NotNull String text, String videoPath, String newPath) {
        try {
            if(!text.equals(prevText)) {
                ImageCreator.createAndSaveImage(text);
                prevText = text;
            }
            if(VideoWatermark.OS.contains("win")) runFfmpegWin(videoPath, newPath);
            else if(VideoWatermark.OS.contains("mac")) runFfmpegMac(videoPath, newPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runFfmpegWin(String videoPath, String newPath) throws IOException, InterruptedException{
        ProcessBuilder builder = new ProcessBuilder("ffmpeg\\ffmpeg-20200623-ce297b4-win64-static\\bin\\ffmpeg",
                "-y", "-i", videoPath, "-i", ImageCreator.PNG_FILE_NAME, "-filter_complex",
                "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2", newPath);
        Process p = builder.start();
        PipeStream out = new PipeStream(p.getInputStream(), System.out);
        PipeStream err = new PipeStream(p.getErrorStream(), System.err);
        out.start();
        err.start();
        p.waitFor();
    }

    private void runFfmpegMac(String videoPath, String newPath){

    }
}

class PipeStream extends Thread {
    private InputStream is;
    private OutputStream os;

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
