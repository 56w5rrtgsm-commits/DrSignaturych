public class Signature {
    private final String name;
    private final byte[] bytes;
    private final long offset; // -1 = любой offset

    public Signature(String name, byte[] bytes, long offset) {
        this.name = name;
        this.bytes = bytes;
        this.offset = offset;
    }

    public String getName() { return name; }
    public byte[] getBytes() { return bytes; }
    public long getOffset() { return offset; }

    @Override
    public String toString() {
        return name + " @ " + offset;
    }

    public static byte[] fromHex(String hex) {
        hex = hex.replaceAll("\\s+","");
        byte[] data = new byte[hex.length()/2];
        for(int i=0;i<data.length;i++){
            data[i] = (byte) Integer.parseInt(hex.substring(i*2,i*2+2),16);
        }
        return data;
    }
}
