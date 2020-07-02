import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Watermarker {

    public static final String PNG_FILE_NAME = "watermark.png";

    /**
     * Watermarks the video using the given text (newline characters create new lines in the watermark)
     * @param text The watermark text
     * @param videoPath The path of the video to watermark
     * @param newPath The location to which the watermarked video will be saved
     * @throws IOException If "watermark.png" is not found or a given file path is invalid
     * @throws InterruptedException Thrown by Process.waitFor
     */
    public static void watermarkVideoWin(String text, String videoPath, String newPath) throws IOException, InterruptedException {
        createAndSaveImage(text);
        ProcessBuilder builder = new ProcessBuilder("ffmpeg\\ffmpeg-20200623-ce297b4-win64-static\\bin\\ffmpeg",
                "-y", "-i", videoPath, "-i", PNG_FILE_NAME, "-filter_complex",
                "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2", newPath);
        Process p = builder.start();
        PipeStream out = new PipeStream(p.getInputStream(), System.out);
        PipeStream err = new PipeStream(p.getErrorStream(), System.err);
        out.start();
        err.start();
        p.waitFor();
    }

    private static void createAndSaveImage(String text) throws IOException{
        BufferedImage img = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        g2d.setFont(new Font("Arial", Font.BOLD, 55));
        g2d.setColor(new Color(150, 150, 150, 127));

        String[] lines = text.split("\n");
        int lineHeight = g2d.getFontMetrics().getHeight();
        int startingY = (img.getHeight() - lineHeight * lines.length) / 2;
        for(int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], centerText(g2d, lines[i], img.getWidth()), startingY + lineHeight * (i + 1));
        }

        g2d.dispose();
        File f = new File(PNG_FILE_NAME);
        ImageIO.write(img, "PNG", f);
    }

    private static int centerText(Graphics2D g2d, String text, int width) {
        FontMetrics metrics = g2d.getFontMetrics();
        return (width - metrics.stringWidth(text)) / 2;
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
