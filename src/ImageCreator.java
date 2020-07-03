import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCreator {

    public static final String PNG_FILE_NAME = "data/watermark.png";

    public static void createAndSaveImage(String text, int[] options) throws IOException {
        BufferedImage img = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        g2d.setFont(new Font("Arial", Font.BOLD, options[0]));
        g2d.setColor(new Color(150, 150, 150, options[1]));

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
