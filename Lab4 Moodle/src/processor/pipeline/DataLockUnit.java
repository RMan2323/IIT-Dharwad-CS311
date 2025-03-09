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
        stalls = 0;

        opcodes.put("00000", "add");
		opcodes.put("00001", "addi");
		opcodes.put("00010", "sub");
		opcodes.put("00011", "subi");
		opcodes.put("00100", "mul");
		opcodes.put("00101", "muli");
		opcodes.put("00110", "div");
		opcodes.put("00111", "divi");
		opcodes.put("01000", "and");
		opcodes.put("01001", "andi");
		opcodes.put("01010", "or");
		opcodes.put("01011", "ori");
		opcodes.put("01100", "xor");
		opcodes.put("01101", "xori");
		opcodes.put("01110", "slt");
		opcodes.put("01111", "slti");
		opcodes.put("10000", "sll");
		opcodes.put("10001", "slli");
		opcodes.put("10010", "srl");
		opcodes.put("10011", "srli");
		opcodes.put("10100", "sra");
		opcodes.put("10101", "srai");
		opcodes.put("10110", "load");
		opcodes.put("10111", "store");
		opcodes.put("11000", "jmp");
		opcodes.put("11001", "beq");
		opcodes.put("11010", "bne");
		opcodes.put("11011", "blt");
		opcodes.put("11100", "bgt");
		opcodes.put("11101", "end");
    }

    public void checkConflicts(int rsA1, int rsA2){
        int rBd;
        
        //OF-EX Conflict
        rBd = EX.rd;
        
        if(rBd == rsA1 || rBd == rsA2){
            //stall IF and OF
            //in EX stage, put three bubbles
            System.out.println("OF-EX CONFLICT!");
            stalls = 3;
        }

        //OF-MA Conflict
        rBd = MA.rd;

        if(rBd == rsA1 || rBd == rsA2){
            //stall IF and OF
            //in EX stage, put two bubbles
            System.out.println("OF-MA CONFLICT!");
            stalls = 2;
        }

        //OF-RW Conflict
        rBd = RW.rd;

        if(rBd == rsA1 || rBd == rsA2){
            //stall IF and OF
            //in EX stage, put one bubble
            System.out.println("OF-RW CONFLICT!");
            stalls = 1;
        }
    }

    public void insertBubbles(){
        if(stalls > 0){
            IF_en.setIF_enable(false);
            System.out.println("Stalled");
            stalls--;
            EX.isBubble = true;
        }
        else{
            IF_en.setIF_enable(true);
        }
    }
}
