package processor.pipeline;

import processor.Processor;
public class BranchLockUnit {
    Processor containingProcessor;
    IF_EnableLatchType IF_en;
    InstructionFetch IF_unit;
    IF_OF_LatchType OF;
    OF_EX_LatchType EX;
    Execute EX_unit;

    public void handleBranchTaken(){
        //TODO insert bubbles in IF-OF and OF-EX
        //if fetched PC in IF != btPC, then insert bubbles (EX.isBubble = true) and also in IF and fetch from new PC
    }
}