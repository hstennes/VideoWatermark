import javax.swing.text.*;
import java.awt.*;

public class WatermarkDocument extends DefaultStyledDocument {

    private AttributeSet attr, attrBlack;

    public WatermarkDocument(){
        StyleContext cont = StyleContext.getDefaultStyleContext();
        attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    }

    @Override
    public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        setColors(getText(0, getLength()));
    }

    @Override
    public void remove (int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        setColors(getText(0, getLength()));
    }

    private void setColors(String text){
        setCharacterAttributes(0, text.length(), attrBlack, false);
        addColor(text, "[name]");
        addColor(text, "[filename]");
    }

    private void addColor(String text, String str){
        int index = -1;
        while(true){
            index = text.indexOf(str, index + 1);
            if(index == -1) break;
            setCharacterAttributes(index, str.length(), attr, false);
        }
    }
}
