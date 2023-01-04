package at.szyon.jfilehandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtility {
    public static final String PATH_SEPARATOR = File.separator;
    public static final String NEW_LINE = System.lineSeparator();
    public static final boolean create(File file) throws IOException {
        File dirs = new File(file.getParentFile().getAbsolutePath());
        dirs.mkdirs();
        return file.createNewFile();
    }

    public static final File file(String directory, String name, String suffix) {
        return new File(directory+File.separator+name+"."+suffix);
    }

    public static final File file(String name, String suffix) {
        return new File(File.separator+ name+"."+suffix);
    }

    public static final boolean delete(File file) {
        return file.delete();
    }

    public static final long byteSize(File file) {
        return file.length();
    }

    public static final File[] roots() {
        return File.listRoots();
    }

    public static final boolean copy(File from, File to) throws IOException {
        if(Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING) == null) {
            return false;
        }
        return true;
    }

    public static final boolean rename(File file, String newName) {
        return file.renameTo(new File(file.getParent() + PATH_SEPARATOR + newName));
    }
}
