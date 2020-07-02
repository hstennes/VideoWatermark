import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoEditor extends SwingWorker<Void, Integer> {

    private Window window;
    private Operation operation;
    private Watermarker watermarker;

    public VideoEditor(Window window, Operation operation){
        this.window = window;
        this.operation = operation;
        this.watermarker = new Watermarker();
    }

    @Override
    protected Void doInBackground() {
        try {
            ArrayList<String> files = operation.listFiles();
            publish(files.size() * operation.versionsPerFile());
            for(String file : files){
                operation.supplyFile(file);
                while(operation.hasNext()){
                    String[] next = operation.getNextWatermark();
                    watermarker.watermarkVideoWin(next[0], file, next[1]);
                    publish();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        if(chunks.size() > 0) {
            if(chunks.get(0) == 0) window.noVideosInFolder();
            else window.getProgessBar().processStarted(chunks.get(0));
        }
        else window.getProgessBar().videoCompleted();
    }

    @Override
    protected void done() {
        window.watermarkingDone();
    }
}


