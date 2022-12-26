import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoEditor extends SwingWorker<Boolean, Integer> {

    private Window window;
    private Operation operation;
    private Watermarker watermarker;

    public VideoEditor(Window window, Operation operation){
        this.window = window;
        this.operation = operation;
        this.watermarker = new Watermarker();
    }

    @Override
    protected Boolean doInBackground() {
        ArrayList<String> files;
        try {
            files = operation.listFiles();
        } catch (IOException e) {
            displayErrorEDT(e);
            return false;
        }
        publish(files.size() * operation.versionsPerFile());
        for(String file : files){
            operation.supplyFile(file);
            while(operation.hasNext()){
                String[] next = operation.getNextWatermark();
                try{
                    watermarker.watermarkVideo(next[0], file, next[1]);
                    publish();
                } catch(IOException | InterruptedException e){
                    displayErrorEDT(e);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void process(List<Integer> chunks) {
        if(chunks.size() > 0) {
            //an integer number of videos was published, which means that the watermarking has just started
            if(chunks.get(0) == 0) window.noVideosFound();
            else window.getProgessBar().processStarted(chunks.get(0));
        }
        else {
            //no value was published, so the publish() call was made to indicate that a video has been completed
            window.getProgessBar().videoCompleted();
        }
    }

    @Override
    protected void done() {
        try {
            window.watermarkingDone(get());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayErrorEDT(Exception e) {
        SwingUtilities.invokeLater(() -> {
            e.printStackTrace();
            String msg = String.format("Unexpected problem: %s", e.toString());
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
}


