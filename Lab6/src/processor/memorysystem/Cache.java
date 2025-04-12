package processor.memorysystem;

public class Cache {
    CacheLine cacheLines[];
    public int size, latency, tag_len, index_len;
    public MainMemory memory;
    public boolean wasHit;

    //there will be size/4 lines
    //each set has 2 lines
    //there will be size/8 sets
    //addr bit length is 16 bits
    //offset of zero bits
    //index of lg(size/8) bits
    //tag of 16-lg(size/8) bits
    //int logBase2 = (int) (Math.log(x) / Math.log(2));

    public Cache(int size, int latency){
        this.size = size;
        this.latency = latency;
        // this.memory = memory;
        this.cacheLines = new CacheLine[size/4];
        for(int i = 0; i < cacheLines.length; i++) cacheLines[i] = new CacheLine();
        this.index_len = (int)(Math.log(size/8) / Math.log(2));
        this.tag_len = 16-index_len;
        this.wasHit = false;
    }

    public int cacheRead(int addr){
        //convert addr to binary
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        //search both lines in the set
        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLines[i].valid && cacheLines[i].tag == tag) {
                wasHit = true;
                System.out.println("Cache Read: Hit");
                return cacheLines[i].data;
            }
        }
        System.out.println("Cache Read: Miss");
        //call handleCacheMiss() if not present
        wasHit = false;
        return handleCacheMiss(addr, true);
    }

    public void cacheWrite(int addr, int value){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLines[i].valid && cacheLines[i].tag == tag) {
                cacheLines[i].data = value;
                memory.setWord(addr, value); //write-through
                System.out.println("Cache Write: Hit");
                return;
            }
        }
        System.out.println("Cache Write: Miss");
        //call handleCacheMiss() if not present
        handleCacheMiss(addr, false);
        cacheWrite(addr, value);
    }

    public int handleCacheMiss(int addr, boolean isRead){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        System.out.println("handleCacheMiss: Got value "+memory.getWord(addr)+" at address "+addr);
        int value = memory.getWord(addr);

        //replace first invalid or use index 0
        int replacementIndex = -1;
        for (int i = setStart; i < setStart + 2; i++) {
            if (!cacheLines[i].valid) {
                replacementIndex = i;
                break;
            }
        }
        if (replacementIndex == -1) replacementIndex = setStart; // fallback

        cacheLines[replacementIndex].valid = true;
        cacheLines[replacementIndex].tag = tag;
        cacheLines[replacementIndex].data = value;

        System.out.println("handleCacheMiss: Put data "+value+" at index "+replacementIndex+" with tag "+tag);

        return isRead ? value : 0;
    }
}
