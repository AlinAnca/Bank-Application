import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataFile {
    private String fileName;
    private String message = "";
    private boolean completed;

    public DataFile(){}

    public DataFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCompleted() {
        return completed;
    }


    public Set<User> readFile()
    {
        Set<User> users = new HashSet<>();

        if(validateFile())
        {
            File file = new File(fileName);
            try(Scanner scan = new Scanner(file))
            {
                scan.next();
                scan.next();
                while(scan.hasNext()){
                    users.add(new User(scan.next(),scan.next()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            completed = true;
            message = "Successfully read file.";
        } else{
            completed = false;
        }
        return users;
    }

    private boolean validateFile()
    {
        File file = new File(fileName);

        if(file.isDirectory() )
        {
            message = "Directory founded. ";
            return false;
        }

        if(!file.getName().endsWith(".txt"))
        {
            message = "Wrong type file. Not a'.txt' file. ";
            return false;
        }

        if(!file.exists())
        {
            message = "File '" + fileName + "' does not exists. ";
            return false;
        }

        return true;
    }

}