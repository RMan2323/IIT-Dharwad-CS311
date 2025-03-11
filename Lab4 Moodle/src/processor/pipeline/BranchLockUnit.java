package processor.pipeline;

import processor.Processor;
public class BranchLockUnit {
    Processor containingProcessor;
    IF_EnableLatchType IF_en;
    InstructionFetch IF_unit;
    IF_OF_LatchType OF;
    OF_EX_LatchType EX;
    Execute EX_unit;

    public BranchLockUnit(Processor containingProcessor, IF_EnableLatchType iF_en, InstructionFetch iF_unit, OF_EX_LatchType eX, IF_OF_LatchType oF){
        this.containingProcessor = containingProcessor;
        this.IF_en = iF_en;
        this.OF = oF;
        this.EX = eX;
		this.IF_unit = iF_unit;
    }

    public void handleBranchTaken(){
        //TODO insert bubbles in IF-OF and OF-EX
        //if fetched PC in IF != btPC, then insert bubbles (EX.isBubble = true) and also in IF and fetch from new PC
        // if(containingProcessor.getRegisterFile().getProgramCounter() == EX.branchTgt){
        //     return;
        // }else{
            // EX.isBubble = true;
            EX.isBranchBubble = true;
            OF.setBubble(true);
            System.out.println("BRANCHING AND INSERTING BUBBLES");
        // }
    }
}