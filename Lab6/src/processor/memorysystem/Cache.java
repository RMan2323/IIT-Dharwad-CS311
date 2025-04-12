package processor.memorysystem;

public class Cache {
    CacheLine cacheLinesData[], cacheLinesInst[];
    public int size, latency, tag_len, index_len;
    public MainMemory memory;
    public boolean wasDataHit, wasInstHit;

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
        this.cacheLinesData = new CacheLine[size/4];
        for(int i = 0; i < cacheLinesData.length; i++) cacheLinesData[i] = new CacheLine();
        this.cacheLinesInst = new CacheLine[size/4];
        for(int i = 0; i < cacheLinesInst.length; i++) cacheLinesInst[i] = new CacheLine();
        this.index_len = (int)(Math.log(size/8) / Math.log(2));
        this.tag_len = 16-index_len;
        this.wasDataHit = false;
        this.wasInstHit = false;
    }

    //Data cache functions
    public int cacheDataRead(int addr){
        //convert addr to binary
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        //search both lines in the set
        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesData[i].valid && cacheLinesData[i].tag == tag) {
                wasDataHit = true;
                System.out.println("Cache Read: Hit");
                return cacheLinesData[i].data;
            }
        }
        System.out.println("Cache Read: Miss");
        //call handleCacheMiss() if not present
        wasDataHit = false;
        return handleDataCacheMiss(addr, true);
    }

    public void cacheDataWrite(int addr, int value){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesData[i].valid && cacheLinesData[i].tag == tag) {
                cacheLinesData[i].data = value;
                memory.setWord(addr, value); //write-through
                System.out.println("Cache Write: Hit");
                return;
            }
        }
        System.out.println("Cache Write: Miss");
        //call handleCacheMiss() if not present
        handleDataCacheMiss(addr, false);
        cacheDataWrite(addr, value);
    }

    public int handleDataCacheMiss(int addr, boolean isRead){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        System.out.println("handleCacheMiss: Got value "+memory.getWord(addr)+" at address "+addr);
        int value = memory.getWord(addr);

        //replace first invalid or use index 0
        int replacementIndex = -1;
        for (int i = setStart; i < setStart + 2; i++) {
            if (!cacheLinesData[i].valid) {
                replacementIndex = i;
                break;
            }
        }
        if (replacementIndex == -1) replacementIndex = setStart; // fallback

        cacheLinesData[replacementIndex].valid = true;
        cacheLinesData[replacementIndex].tag = tag;
        cacheLinesData[replacementIndex].data = value;

        System.out.println("handleCacheMiss: Put data "+value+" at index "+replacementIndex+" with tag "+tag);

        return isRead ? value : 0;
    }

    //Inst cache functions
    public int cacheInstRead(int addr){
        //convert addr to binary
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        //search both lines in the set
        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesInst[i].valid && cacheLinesInst[i].tag == tag) {
                wasInstHit = true;
                System.out.println("Cache Read: Hit");
                return cacheLinesInst[i].data;
            }
        }
        System.out.println("Cache Read: Miss");
        //call handleCacheMiss() if not present
        wasInstHit = false;
        return handleInstCacheMiss(addr, true);
    }

    public void cacheInstWrite(int addr, int value){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesInst[i].valid && cacheLinesInst[i].tag == tag) {
                cacheLinesInst[i].data = value;
                memory.setWord(addr, value); //write-through
                System.out.println("Cache Write: Hit");
                return;
            }
        }
        System.out.println("Cache Write: Miss");
        //call handleCacheMiss() if not present
        handleInstCacheMiss(addr, false);
        cacheInstWrite(addr, value);
    }

    public int handleInstCacheMiss(int addr, boolean isRead){
        int index = (addr >> 0) & ((1 << index_len) - 1);
        int tag = addr >> index_len;
        int setStart = index * 2;

        System.out.println("handleCacheMiss: Got value "+memory.getWord(addr)+" at address "+addr);
        int value = memory.getWord(addr);

        //replace first invalid or use index 0
        int replacementIndex = -1;
        for (int i = setStart; i < setStart + 2; i++) {
            if (!cacheLinesInst[i].valid) {
                replacementIndex = i;
                break;
            }
        }
        if (replacementIndex == -1) replacementIndex = setStart; // fallback

        cacheLinesInst[replacementIndex].valid = true;
        cacheLinesInst[replacementIndex].tag = tag;
        cacheLinesInst[replacementIndex].data = value;

        System.out.println("handleCacheMiss: Put Inst "+value+" at index "+replacementIndex+" with tag "+tag);

        return isRead ? value : 0;
    }
}
