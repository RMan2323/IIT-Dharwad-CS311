package processor.memorysystem;

import generic.Statistics;

public class Cache {
    CacheLine cacheLinesData[], cacheLinesInst[];
    public int dataSize, dataLatency, dataTagLen, dataIndexLen,instSize, instLatency, instTagLen, instIndexLen;
    public MainMemory memory;
    public boolean wasDataHit, wasInstHit;
    int[] dataLRU, instLRU;

    //there will be size/4 lines
    //each set has 2 lines
    //there will be size/8 sets
    //addr bit length is 16 bits
    //offset of zero bits
    //index of lg(size/8) bits
    //tag of 16-lg(size/8) bits
    //int logBase2 = (int) (Math.log(x) / Math.log(2));

    public Cache(int dataSize, int dataLatency, int instSize, int instLatency) {
        this.dataSize = dataSize;
        this.dataLatency = dataLatency;
        this.instSize = instSize;
        this.instLatency = instLatency;

        this.cacheLinesData = new CacheLine[dataSize / 4];
        for (int i = 0; i < cacheLinesData.length; i++) cacheLinesData[i] = new CacheLine();

        this.cacheLinesInst = new CacheLine[instSize / 4];
        for (int i = 0; i < cacheLinesInst.length; i++) cacheLinesInst[i] = new CacheLine();

        this.dataIndexLen = (int)(Math.log(dataSize / 8) / Math.log(2));
        this.dataTagLen = 16 - dataIndexLen;

        this.instIndexLen = (int)(Math.log(instSize / 8) / Math.log(2));
        this.instTagLen = 16 - instIndexLen;

        this.wasDataHit = false;
        this.wasInstHit = false;

        this.dataLRU = new int[dataSize / 8];
        this.instLRU = new int[instSize / 8];
    }

    //data cache
    public int cacheDataRead(int addr) {
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << dataIndexLen) - 1);
        int tag = addr >> dataIndexLen;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesData[i].valid && cacheLinesData[i].tag == tag) {
                wasDataHit = true;
                dataLRU[index] = (i == setStart) ? 0 : 1;
                System.out.println("Cache Read: Hit");
                return cacheLinesData[i].data;
            }
        }
        System.out.println("Cache Read: Miss");
        wasDataHit = false;
        return handleDataCacheMiss(addr, true);
    }

    public void cacheDataWrite(int addr, int value) {
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << dataIndexLen) - 1);
        int tag = addr >> dataIndexLen;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesData[i].valid && cacheLinesData[i].tag == tag) {
                cacheLinesData[i].data = value;
                dataLRU[index] = (i == setStart) ? 0 : 1;
                memory.setWord(addr, value);
                System.out.println("Cache Write: Hit");
                Statistics.dataHits++;
                return;
            }
        }
        System.out.println("Cache Write: Miss");
        Statistics.dataMisses++;
        handleDataCacheMiss(addr, false);
        cacheDataWrite(addr, value);
    }

    public int handleDataCacheMiss(int addr, boolean isRead) {
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << dataIndexLen) - 1);
        int tag = addr >> dataIndexLen;
        int setStart = index * 2;

        int value = memory.getWord(addr);

        int replacementIndex = -1;
        for (int i = setStart; i < setStart + 2; i++) {
            if (!cacheLinesData[i].valid) {
                replacementIndex = i;
                break;
            }
        }
        if (replacementIndex == -1) replacementIndex = (dataLRU[index] == 1) ? setStart : setStart + 1;

        cacheLinesData[replacementIndex].valid = true;
        cacheLinesData[replacementIndex].tag = tag;
        cacheLinesData[replacementIndex].data = value;

        dataLRU[index] = (replacementIndex == setStart) ? 0 : 1;

        System.out.println("handleCacheMiss: Put data " + value + " at index " + replacementIndex + " with tag " + tag);

        return isRead ? value : 0;
    }

    //instruction cache
    public int cacheInstRead(int addr) {
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << instIndexLen) - 1);
        int tag = addr >> instIndexLen;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesInst[i].valid && cacheLinesInst[i].tag == tag) {
                wasInstHit = true;
                instLRU[index] = (i == setStart) ? 0 : 1;
                System.out.println("Cache Read: Hit");
                return cacheLinesInst[i].data;
            }
        }
        System.out.println("Cache Read: Miss");
        wasInstHit = false;
        return handleInstCacheMiss(addr, true);
    }

    public void cacheInstWrite(int addr, int value) {
        //take lower index_len bits and go to index
        //take upper tag_len bits and compare with two lines
        int index = (addr >> 0) & ((1 << instIndexLen) - 1);
        int tag = addr >> instIndexLen;
        int setStart = index * 2;

        for (int i = setStart; i < setStart + 2; i++) {
            if (cacheLinesInst[i].valid && cacheLinesInst[i].tag == tag) {
                cacheLinesInst[i].data = value;
                instLRU[index] = (i == setStart) ? 0 : 1;
                memory.setWord(addr, value);
                System.out.println("Cache Write: Hit");
                return;
            }
        }
        System.out.println("Cache Write: Miss");
        handleInstCacheMiss(addr, false);
        cacheInstWrite(addr, value);
    }

    public int handleInstCacheMiss(int addr, boolean isRead) {
        int index = (addr >> 0) & ((1 << instIndexLen) - 1);
        int tag = addr >> instIndexLen;
        int setStart = index * 2;

        int value = memory.getWord(addr);

        int replacementIndex = -1;
        for (int i = setStart; i < setStart + 2; i++) {
            if (!cacheLinesInst[i].valid) {
                replacementIndex = i;
                break;
            }
        }
        if (replacementIndex == -1) replacementIndex = (instLRU[index] == 1) ? setStart : setStart + 1;

        cacheLinesInst[replacementIndex].valid = true;
        cacheLinesInst[replacementIndex].tag = tag;
        cacheLinesInst[replacementIndex].data = value;

        instLRU[index] = (replacementIndex == setStart) ? 0 : 1;

        System.out.println("handleCacheMiss: Put Inst " + value + " at index " + replacementIndex + " with tag " + tag);

        return isRead ? value : 0;
    }
}