import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame {

    public static final int WIDTH = 400, HEIGHT = 400;

    private Font stepFont;
    private JTextPane watermarkBox;
    private VideoProgressBar progressBar;

    private File videoFolder = null;
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
        panel.add(createStepLabel("Step 1: Select video folder", 30));

        JLabel fileLabel = new JLabel("(No folder selected)");
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton chooseVideo = new JButton("Choose folder");
        chooseVideo.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseVideo.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(fc.showOpenDialog(Window.this) == JFileChooser.APPROVE_OPTION) {
                videoFolder = fc.getSelectedFile();
                if(!inProgress) progressBar.setVisible(false);
                fileLabel.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(chooseVideo);
        panel.add(fileLabel);
        panel.add(createStepLabel("Step 2: Enter watermark text", 40));
        panel.add(createWatermarkBox());
        panel.add(createStepLabel("Step 3: Watermark videos", 40));

        JButton doWatermark = new JButton("Watermark Videos");
        doWatermark.setAlignmentX(Component.CENTER_ALIGNMENT);
        doWatermark.addActionListener(actionEvent -> {
            if(videoFolder == null) JOptionPane.showMessageDialog(Window.this,
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
            else startWatermarking();
        });

        progressBar = new VideoProgressBar();
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(230, 20));
        progressBar.setBorderPainted(true);
        JPanel spacingPanel = new JPanel();
        spacingPanel.setMaximumSize(new Dimension(1, 50));
        spacingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(doWatermark);
        panel.add(spacingPanel);
        panel.add(progressBar);
        add(panel);
    }

    public void noVideosInFolder(){
        JOptionPane.showMessageDialog(this,
                "There are no mp4 files in the selected folder",
                "Files not Found",
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
        watermarkBox = new JTextPane();
        JPanel watermarkPanel = new JPanel(new BorderLayout());
        watermarkPanel.add(watermarkBox);
        JScrollPane scrollPane = new JScrollPane(watermarkPanel);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(250, 50));
        StyledDocument doc = watermarkBox.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc.addDocumentListener(new DocumentListener() {
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
        });
        return scrollPane;
    }

    private void startWatermarking() {
        inProgress = true;
        new VideoEditor(this, watermarkBox.getText(), videoFolder).execute();
    }

    public VideoProgressBar getProgessBar(){
        return progressBar;
    }
}
