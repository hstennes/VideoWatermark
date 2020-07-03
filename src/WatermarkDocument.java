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
        setColors(getText(0, getLength()), offset);
    }

    @Override
    public void remove (int offset, int len) throws BadLocationException {
        super.remove(offset, len);
        setColors(getText(0, getLength()), offset);
    }

    private void setColors(String text, int offset){
        int start = text.substring(0, offset).indexOf("\n") + 1;
        int end = text.substring(offset).indexOf("\n");
        if(end == -1) end = text.length();
        setCharacterAttributes(start, end, attrBlack, false);
        addColor(text, "[name]", start, end);
        addColor(text, "[filename]", start, end);
    }

    private void addColor(String text, String str, int start, int end){
        int index = start;
        while(index < end){
            index = text.indexOf(str, index);
            if(index == -1) break;
            setCharacterAttributes(index, str.length(), attr, false);
            index++;
        }
    }
}
