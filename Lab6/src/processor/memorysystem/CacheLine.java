package processor.memorysystem;

public class CacheLine {
    int data, tag;
    boolean valid;

    public CacheLine(){
        valid = false;
    }
}
