public class IDGenerator {
    private static IDGenerator instance = new IDGenerator();

    private volatile int ID = 0;

    private IDGenerator() {}

    public static IDGenerator getInstance() {
        return instance;
    }

    public synchronized int assignID() { return ID++; }

    public synchronized int getLastID() {
        return ID;
    }
}
