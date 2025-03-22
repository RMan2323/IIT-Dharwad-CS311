package processor.pipeline;
import generic.Statistics;
import processor.Processor;
public class DataLockUnit {
    Processor containingProcessor;
    IF_EnableLatchType IF_en;
    InstructionFetch IF_unit;
    Execute EX_unit;
    OF_EX_LatchType EX;
    EX_MA_LatchType MA;
    MA_RW_LatchType RW;
    int stalls, pass;
    boolean wasStalled;

    public DataLockUnit(Processor containingProcessor, IF_EnableLatchType iF_en, InstructionFetch iF_unit, OF_EX_LatchType eX_unit, EX_MA_LatchType mA_unit, MA_RW_LatchType rW_unit){
        this.containingProcessor = containingProcessor;
        this.IF_en = iF_en;
		this.IF_unit = iF_unit;
		this.EX = eX_unit;
		this.MA = mA_unit;
        this.RW = rW_unit;
        this.stalls = 0;
        this.pass = 0;
        wasStalled = false;
    }

    public void checkConflicts(int rsA1, int rsA2){
        int rBd;
        
        //OF-EX Conflict
        rBd = EX.rd;
        System.out.println("EX: "+rsA1 + " " + rsA2 + " " + rBd + " " + EX.writeTo31);
        if(((rBd == rsA1 || rBd == rsA2) && (rBd != -1)) || ((rsA1 == 31 || rsA2 == 31) && (EX.writeTo31))){
            //stall IF and OF
            //in EX stage, put three bubbles
            System.out.println("OF-EX CONFLICT!");
            System.out.println(rsA1 + " " + rsA2 + " " + rBd + " " + EX.writeTo31);
            stalls = 3;
            Statistics.numberOfDataStalls+=3;
            pass++;
            wasStalled = true;
            return;
        }

        //OF-MA Conflict
        rBd = MA.rd;
        System.out.println("MA: "+rsA1 + " " + rsA2 + " " + rBd + " " + MA.writeTo31);
        if(((rBd == rsA1 || rBd == rsA2) && (rBd != -1)) || ((rsA1 == 31 || rsA2 == 31) && (MA.writeTo31))){
            //stall IF and OF
            //in EX stage, put two bubbles
            System.out.println("OF-MA CONFLICT!");
            System.out.println(rsA1 + " " + rsA2 + " " + rBd + " " + EX.writeTo31);
            stalls = 2;
            Statistics.numberOfDataStalls+=2;
            pass++;
            wasStalled = true;
            return;
        }

        //OF-RW Conflict
        rBd = RW.rd;

        if(((rBd == rsA1 || rBd == rsA2) && (rBd != -1)) || ((rsA1 == 31 || rsA2 == 31) && (RW.writeTo31))){
            //stall IF and OF
            //in EX stage, put one bubble  
            System.out.println("OF-RW CONFLICT!");
            stalls = 1;
            Statistics.numberOfDataStalls+=1;
            pass++;
            wasStalled = true;
            return;
        }
    }

    public void insertBubbles(){
        if(stalls > 0){
            IF_en.setIF_enable(false);
            System.out.println("Stalled for "+stalls);
            stalls--;
            EX.isBubble = true;
        }
        else{
            IF_en.setIF_enable(true);
            EX.isBubble = false;
        }
    }

    public boolean shouldResume(){
        if(wasStalled && (stalls == 0)){
            wasStalled = false;
            return true;
        }
        else return false;
    }
}
