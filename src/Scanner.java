import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;

public class Scanner {

    private final List<Signature> signatures;
    private final ExecutorService pool;

    public interface ResultHandler { void onFound(ScanResult result); }

    public Scanner(List<Signature> signatures) {
        this.signatures = signatures;
        this.pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }

    public void scanFolder(File folder, ResultHandler handler) {
        if(folder==null || !folder.exists()) return;
        scanDir(folder, handler);
        pool.shutdown();
    }

    private void scanDir(File dir, ResultHandler handler){
        if(!dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if(files==null) return;

        for(File f: files){
            if(f.isDirectory()) scanDir(f, handler);
            else pool.submit(() -> scanFile(f, handler));
        }
    }

    private void scanFile(File file, ResultHandler handler){
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
            if(raf.length()<2) return;
            byte[] mz = new byte[2];
            raf.readFully(mz);
            if(mz[0]!='M'||mz[1]!='Z') return;

            for(Signature sig: signatures){
                if(sig.getOffset()>=0){
                    if(raf.length() < sig.getOffset() + sig.getBytes().length) continue;
                    raf.seek(sig.getOffset());
                    byte[] buf = new byte[sig.getBytes().length];
                    raf.readFully(buf);
                    if(matches(buf,sig.getBytes()))
                        Platform.runLater(() -> handler.onFound(new ScanResult(file,sig)));
                } else {
                    byte[] fileBytes = new byte[(int) raf.length()];
                    raf.seek(0);
                    raf.readFully(fileBytes);
                    for(int i=0;i<=fileBytes.length-sig.getBytes().length;i++){
                        boolean match = true;
                        for(int j=0;j<sig.getBytes().length;j++)
                            if(fileBytes[i+j]!=sig.getBytes()[j]) {match=false; break;}
                        if(match) {Platform.runLater(() -> handler.onFound(new ScanResult(file,sig))); break;}
                    }
                }
            }

        } catch(Exception ignored){}
    }

    private boolean matches(byte[] a, byte[] b){
        if(a.length!=b.length) return false;
        for(int i=0;i<a.length;i++) if(a[i]!=b[i]) return false;
        return true;
    }
}
