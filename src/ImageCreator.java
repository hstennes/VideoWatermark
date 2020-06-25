import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCreator {

    private String text = "Hank";

    public void createAndSaveImage(String fileName) throws IOException{
        BufferedImage img = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(new Font("Arial", Font.PLAIN, 65));
        g2d.setColor(new Color(150, 150, 150, 127));

        String[] lines = text.split("\n");
        int lineHeight = g2d.getFontMetrics().getHeight();
        int startingY = (img.getHeight() - lineHeight * lines.length) / 2;
        for(int i = 0; i < lines.length; i++)
            g2d.drawString(lines[i], centerText(g2d, lines[i], img.getWidth()), startingY + lineHeight * i);

        g2d.dispose();
        File f = new File(fileName);
        ImageIO.write(img, "PNG", f);
    }

    public void setText(String text){
        this.text = text;
    }

    public static int centerText(Graphics2D g2d, String text, int width) {
        FontMetrics metrics = g2d.getFontMetrics();
        return (width - metrics.stringWidth(text)) / 2;
    }
}