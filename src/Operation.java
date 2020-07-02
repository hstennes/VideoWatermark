import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Operation {

    private boolean isFolder;
    private String path;
    private String userText;
    private String[] names;

    private String workingFile;
    private int nameIndex;

    /**
     * Creates a new operation for a VideoEditor to complete
     * @param isFolder True if a folder of videos will be watermarked, false if it is a single file
     * @param path The directory or specific file to be watermarked
     * @param userText The text entered by the user, including keywords
     * @param names The list of names entered by the user, which may be empty
     */
    public Operation(boolean isFolder, String path, String userText, String[] names){
        this.isFolder = isFolder;
        this.path = path;
        this.userText = userText;
        this.names = names;
    }

    /**
     * Lists all files in the operation's folder, or returns the single file that the operation refers to
     * @return A list of files
     * @throws IOException If the operation was constructed for a folder, but the path was not a valid directory
     */
    public ArrayList<String> listFiles() throws IOException {
        if(!isFolder) {
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            return list;
        }
        else {
            File[] files = new File(path).listFiles();
            if(files == null) throw new IOException();
            ArrayList<String> mp4s = new ArrayList<>();
            for(File f : files) if(f.getAbsolutePath().endsWith("mp4")) mp4s.add(f.getAbsolutePath());
            return mp4s;
        }
    }

    /**
     * Sets the file this operation is "working on".  The getNextWatermark method can then be called for the given file until the operation
     * has run out of file versions (names to place in the watermark)
     * @param file The file to set as the "working file"
     */
    public void supplyFile(String file){
        this.workingFile = file;
        nameIndex = 0;
    }

    /**
     * Gets the next watermark for the working file.  This method can be called once for each version (customer name) to be placed on the
     * watermark.
     * @return A string array containing the watermark text and path for the watermarked file, in that order
     */
    public String[] getNextWatermark(){
        if(nameIndex >= names.length && nameIndex != 0) return null;
        String watermark = userText.replace("[filename]", new File(workingFile).getName());
        if(names.length == 0) {
            nameIndex++;
            return new String[] {watermark, addToFilename(workingFile, "_new")};
        }
        watermark = watermark.replace("[name]", names[nameIndex]);
        nameIndex++;
        return new String[] {watermark, addToFilename(workingFile, "_" + names[nameIndex])};
    }

    private String addToFilename(String name, String addition){
        String[] split = name.split("\\.");
        return split[0] + addition + "." + split[1];
    }

    /**
     * Returns the number of versions for the given file. The number of versions defaults to 1 if there are no names to insert
     * @return The number of versions
     */
    public int versionsPerFile(){
        return Math.max(names.length, 1);
    }

    /**
     * Tells weather getNextWatermark can be called for the current working file.  If this method returns false, then supplyFile must be called
     * again before calling getNextWatermark
     * @return The boolean
     */
    public boolean hasNext(){
        return nameIndex < names.length | nameIndex == 0;
    }

    /**
     * Tells weather this operation was set to folder (or single file) mode
     * @return The boolean
     */
    public boolean isFolder(){
        return isFolder;
    }
}
