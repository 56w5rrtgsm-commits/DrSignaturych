import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.format.DateTimeFormatter;

public class ScanResult {
    private final File file;
    private final Signature signature;
    private final long size;
    private final String created;
    private final String modified;

    public ScanResult(File file, Signature signature) {
        this.file = file;
        this.signature = signature;

        this.size = file.length();

        String createdDate = "";
        String modifiedDate = "";
        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            createdDate = fmt.format(attrs.creationTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            modifiedDate = fmt.format(attrs.lastModifiedTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        } catch (Exception ignored) {}
        this.created = createdDate;
        this.modified = modifiedDate;
    }

    public File getFile() { return file; }
    public Signature getSignature() { return signature; }
    public long getSize() { return size; }
    public String getCreated() { return created; }
    public String getModified() { return modified; }
}
