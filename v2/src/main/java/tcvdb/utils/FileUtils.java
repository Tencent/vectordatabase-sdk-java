package tcvdb.utils;


import java.io.File;

public class FileUtils {

    private FileUtils() {
    }

    public static String getFileType(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        return fileType;
    }
}
