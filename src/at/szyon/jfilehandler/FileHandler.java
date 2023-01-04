package at.szyon.jfilehandler;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {
    private final File file;
    private byte[] cache;

    public FileHandler(File file) {
        this.file = file;
    }
    public final File getFile() {
        return this.file;
    }

    public final boolean fileExists() {
        return (this.file != null && this.file.exists());
    }

    public final boolean createFile() throws IOException {
        return FileUtility.create(this.file);
    }
    public final boolean deleteFile() {
        return FileUtility.delete(this.file);
    }

    public final boolean renameFile(String newName) {
        if(!this.fileExists()) return false;
        return FileUtility.rename(this.file, newName);
    }

    public final void write(final byte[] data) {
        if(this.cache == null) {
            this.cache = data;
            return;
        }

        this.cache = Arrays.copyOf(this.cache, this.cache.length + data.length);
        System.arraycopy(data, 0, this.cache, this.cache.length - data.length, data.length);
    }
    public final void writeString(final String string, final Charset charset) {
        if(this.cache == null) {
            this.cache = string.getBytes(charset);
            return;
        }

        byte[] stringData = string.getBytes();
        this.cache = Arrays.copyOf(this.cache, this.cache.length + stringData.length);
        System.arraycopy(stringData, 0, this.cache, this.cache.length - stringData.length, stringData.length);
    }
    public final void writeString(final String string) {
        if (this.cache == null) {
            this.cache = string.getBytes();
            return;
        }

        byte[] stringData = string.getBytes();
        this.cache = Arrays.copyOf(this.cache, this.cache.length + stringData.length);
        System.arraycopy(stringData, 0, this.cache, this.cache.length - stringData.length, stringData.length);
    }
    public final void writeLines(final List<String> lines) {
        if(lines == null) return;

        String string = lines.stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator();

        if (this.cache == null) {
            this.cache = string.getBytes();
            return;
        }

        byte[] stringData = string.getBytes();
        this.cache = Arrays.copyOf(this.cache, this.cache.length + stringData.length);
        System.arraycopy(stringData, 0, this.cache, this.cache.length - stringData.length, stringData.length);
    }
    public final void writeLines(final String... lines) {
        if(lines == null) return;
        this.writeLines(List.of(lines));
    }

    public final void update(boolean write, boolean read) throws IOException {
        if(write) this.writeFile();
        if(read) this.cache = this.readBytesFromFile();
    }

    private void writeFile() throws IOException {
        if(!this.fileExists()) return;

        FileOutputStream fo = new FileOutputStream(this.file);
        fo.write(this.cache);

        fo.flush();
        fo.close();
    }

    private final byte[] readBytesFromFile() throws IOException {
        if(!this.fileExists()) return null;

        FileInputStream fi = new FileInputStream(this.file);
        byte[] result = fi.readAllBytes();
        fi.close();

        return result;
    }
    public final String getAsString() {
        return (this.cache == null ? null : new String(this.cache));
    }
    public final String getAsString(final Charset charset) {
        return (this.cache == null ? null : new String(this.cache, charset));
    }
    public final List<String> getAsStringLines() {
        return (this.cache == null ? null : List.of(this.getAsString().split(System.lineSeparator())));
    }
    public final List<String> getAsStringLines(final Charset charset) {
        return (this.cache == null ? null : List.of(this.getAsString(charset).split(System.lineSeparator())));
    }
    public final List<String> getAsStringLines(final String split) {
        return (this.cache == null ? null : List.of(this.getAsString().split(split)));
    }
    public final List<String> getAsStringLines(final String split, final Charset charset) {
        return (this.cache == null ? null : List.of(this.getAsString(charset).split(split)));
    }

    public final byte[] getData() {
        return this.cache;
    }

    public final long getFileByteSize() {
        if(!this.fileExists()) return -1;
        return FileUtility.byteSize(this.file);
    }
}
