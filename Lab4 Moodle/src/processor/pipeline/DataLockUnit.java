package processor.pipeline;
import java.util.HashMap;

import processor.Processor;
public class DataLockUnit {
    Processor containingProcessor;
    IF_EnableLatchType IF_en;
    InstructionFetch IF_unit;
    Execute EX_unit;
    OF_EX_LatchType EX;
    EX_MA_LatchType MA;
    MA_RW_LatchType RW;
    int stalls;
    HashMap<String, String> opcodes = new HashMap<String, String>();
	HashMap<String, Integer> registers = new HashMap<String, Integer>();

    public DataLockUnit(Processor containingProcessor, IF_EnableLatchType iF_en, InstructionFetch iF_unit, OF_EX_LatchType eX_unit, EX_MA_LatchType mA_unit, MA_RW_LatchType rW_unit){
        this.containingProcessor = containingProcessor;
        this.IF_en = iF_en;
		this.IF_unit = iF_unit;
		this.EX = eX_unit;
		this.MA = mA_unit;
        this.RW = rW_unit;
        this.stalls = 0;
    }

    public void checkConflicts(int rsA1, int rsA2){
        int rBd;
        
        //OF-EX Conflict
        rBd = EX.rd;
        
        if((rBd == rsA1 || rBd == rsA2) && (rBd != -1)){
            //stall IF and OF
            //in EX stage, put three bubbles
            System.out.println("OF-EX CONFLICT!");
            stalls = 3;
            return;
        }

        //OF-MA Conflict
        rBd = MA.rd;

        if((rBd == rsA1 || rBd == rsA2) && (rBd != -1)){
            //stall IF and OF
            //in EX stage, put two bubbles
            System.out.println("OF-MA CONFLICT!");
            stalls = 2;
            return;
        }

        //OF-RW Conflict
        rBd = RW.rd;

        if((rBd == rsA1 || rBd == rsA2) && (rBd != -1)){
            //stall IF and OF
            //in EX stage, put one bubble  
            System.out.println("OF-RW CONFLICT!");
            stalls = 1;
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
}
