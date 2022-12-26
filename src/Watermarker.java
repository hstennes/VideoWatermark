import java.io.*;

public class Watermarker {

    private static final String LOG_FILE_NAME = "data/log.txt";

    private String prevText;
    private int[] options;

    public Watermarker(){
        options = SettingsManager.readOptions();
    }

    /**
     * Watermarks the video using the given text (newline characters create new lines in the watermark)
     * @param text The watermark text
     * @param videoPath The path of the video to watermark
     * @param newPath The location to which the watermarked video will be saved
     */
    public void watermarkVideo(String text, String videoPath, String newPath) throws IOException, InterruptedException{
        if(!text.equals(prevText)) {
            ImageCreator.createAndSaveImage(text, options);
            prevText = text;
        }
        if(VideoWatermark.OS.contains("win")) runFfmpegWin(videoPath, newPath);
        else if(VideoWatermark.OS.contains("mac")) runFfmpegMac(videoPath, newPath);
    }

    private void runFfmpegWin(String videoPath, String newPath) throws IOException, InterruptedException{
        ProcessBuilder builder = new ProcessBuilder("ffmpeg\\bin\\ffmpeg",
                "-y", "-i", videoPath, "-i", ImageCreator.PNG_FILE_NAME, "-filter_complex",
                "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2", "-acodec", "copy", newPath);
        Process p = builder.start();
        PipeStream out = new PipeStream(p.getInputStream(), System.out);

        File targetFile = new File(LOG_FILE_NAME);
        OutputStream fileStream = new FileOutputStream(targetFile, true);

        PipeStream err = new PipeStream(p.getErrorStream(), fileStream);
        out.start();
        err.start();
        p.waitFor();
    }

    private void runFfmpegMac(String videoPath, String newPath) throws IOException, InterruptedException{
        ProcessBuilder builder = new ProcessBuilder("./ffmpeg", "-y", "-i", videoPath, "-i", ImageCreator.PNG_FILE_NAME, "-filter_complex",
                "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2", "-acodec", "copy", newPath);
        Process p = builder.start();
        PipeStream out = new PipeStream(p.getInputStream(), System.out);

        File targetFile = new File(LOG_FILE_NAME);
        OutputStream fileStream = new FileOutputStream(targetFile, true);

        PipeStream err = new PipeStream(p.getErrorStream(), fileStream);
        out.start();
        err.start();
        p.waitFor();
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
