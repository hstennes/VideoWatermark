
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;

public class Window extends JFrame implements DocumentListener {

    public static final int WIDTH = 450, HEIGHT = 500;

    private Font stepFont;
    private JTextPane watermarkBox;
    private NamesField namesField;
    private VideoProgressBar progressBar;

    private File videoPath = null;
    private boolean inProgress = false;

    public Window(){
        super("Video Watermarking Tool");
        stepFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        setSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        createGUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createGUI(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createStepLabel("Step 1: Select file or folder", 30));
        JLabel fileLabel = new JLabel("(No folder selected)");
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton chooseVideo = new JButton("Choose folder");
        chooseVideo.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseVideo.addActionListener(actionEvent -> chooseVideoButtonPressed(fileLabel));
        panel.add(chooseVideo);
        panel.add(fileLabel);

        panel.add(createStepLabel("Step 2: List customer names", 35));
        namesField = new NamesField();
        namesField.setMaximumSize(new Dimension(300, 25));
        namesField.getDocument().addDocumentListener(this);
        panel.add(namesField);

        panel.add(createStepLabel("Step 3: Enter watermark text", 40));
        panel.add(createWatermarkBox());

        panel.add(createStepLabel("Step 4: Watermark videos", 40));
        JButton doWatermark = new JButton("Watermark Videos");
        doWatermark.setAlignmentX(Component.CENTER_ALIGNMENT);
        doWatermark.addActionListener(actionEvent -> watermarkButtonPressed());

        progressBar = new VideoProgressBar();
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(230, 20));
        progressBar.setBorderPainted(true);
        JPanel spacingPanel = new JPanel();
        spacingPanel.setMaximumSize(new Dimension(1, 40));
        spacingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doWatermark);
        panel.add(spacingPanel);
        panel.add(progressBar);
        add(panel);
    }

    public void noVideosFound(){
        JOptionPane.showMessageDialog(this,
                "The selected file was not an mp4, or the selected folder did not contain any mp4s",
                "mp4 file(s) not found",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void watermarkingDone(){
        inProgress = false;
    }

    private JLabel createStepLabel(String text, int topPadding){
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(topPadding, 0, 0, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(stepFont);
        return label;
    }

    private JScrollPane createWatermarkBox(){
        watermarkBox = new JTextPane(new WatermarkDocument());
        JPanel watermarkPanel = new JPanel(new BorderLayout());
        watermarkPanel.add(watermarkBox);
        JScrollPane scrollPane = new JScrollPane(watermarkPanel);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(300, 70));
        StyledDocument doc = watermarkBox.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc.addDocumentListener(this);
        return scrollPane;
    }

    private void chooseVideoButtonPressed(JLabel fileLabel){
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(fc.showOpenDialog(Window.this) == JFileChooser.APPROVE_OPTION) {
            videoPath = fc.getSelectedFile();
            if(!inProgress) progressBar.setVisible(false);
            fileLabel.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    private void watermarkButtonPressed(){
        if(videoPath == null) JOptionPane.showMessageDialog(Window.this,
                "Please select the folder containing the videos to be watermarked",
                "Add Missing Info",
                JOptionPane.INFORMATION_MESSAGE);
        else if(watermarkBox.getText().equals("")) JOptionPane.showMessageDialog(Window.this,
                "Please enter watermark text",
                "Add Missing Info",
                JOptionPane.INFORMATION_MESSAGE);
        else if(inProgress) JOptionPane.showMessageDialog(Window.this,
                "Please wait for watermarking to complete",
                "Watermarking in progress",
                JOptionPane.INFORMATION_MESSAGE);
        else doWatermark();
    }

    private void doWatermark(){
        if(namesField.getNames().length > 0 && !watermarkBox.getText().contains("[name]")){
            String[] options = new String[] {"Continue", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                    "You specified a list of names, but you did not include [name] in your watermark. \nDo you want to continue " +
                            "with watermarking?",
                    "Unused names",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if(choice != 0) return;
        }
        inProgress = true;
        new VideoEditor(this, new Operation(
                videoPath.getAbsolutePath(),
                watermarkBox.getText(),
                namesField.getNames())).execute();
    }

    public VideoProgressBar getProgessBar(){
        return progressBar;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if(!inProgress) progressBar.setVisible(false);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if(!inProgress) progressBar.setVisible(false);
    }

    @Override
    public void changedUpdate(DocumentEvent e) { }
}
