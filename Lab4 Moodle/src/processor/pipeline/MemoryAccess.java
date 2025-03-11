package processor.pipeline;

import processor.Processor;
@SuppressWarnings("unused")
public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA() {
		// int instruction = EX_MA_Latch.instruction;
		if(EX_MA_Latch.isBubble) {
			System.out.println("MA Bubble");
			MA_RW_Latch.isBubble = true;
			MA_RW_Latch.rd = -1;
			if(MA_RW_Latch.writeTo31) MA_RW_Latch.writeTo31 = false;
			return;
		}
		if(!EX_MA_Latch.isMA_enable()){
			MA_RW_Latch.setRW_enable(false);
			return;
		}
		if (EX_MA_Latch.isMA_enable()) {
			MA_RW_Latch.isBubble = false;
			System.out.println("Performing MA!!!!!!");
			EX_MA_Latch.setMA_enable(false);
			if (EX_MA_Latch.endProg) {
				MA_RW_Latch.setEndProg(true);
				MA_RW_Latch.setRW_enable(true);
				MA_RW_Latch.endPC = EX_MA_Latch.endPC;
				System.out.println("MA " + MA_RW_Latch.endProg);
			} else
				switch (EX_MA_Latch.type) {
					case store:
						containingProcessor.getMainMemory().setWord(EX_MA_Latch.stAddr, EX_MA_Latch.data);
						System.out.println("----\nSTORED " + EX_MA_Latch.data + " into address " + EX_MA_Latch.stAddr);
						break;
					case load:
						// containingProcessor.getRegisterFile().setValue(EX_MA_Latch.rd, containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr));
						MA_RW_Latch.setRd(EX_MA_Latch.rd);
						MA_RW_Latch.setRes(containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr));
						// MA_RW_Latch.rem = EX_MA_Latch.remainder;
						System.out.println("----\nLOADING " + containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr) + " into register " + EX_MA_Latch.rd);
						break;
					default:
						MA_RW_Latch.setRd(EX_MA_Latch.rd);
						MA_RW_Latch.setRes(EX_MA_Latch.aluRes);
						MA_RW_Latch.rem = EX_MA_Latch.remainder;
						break;
				}
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.rs1 = EX_MA_Latch.rs1;
			MA_RW_Latch.rs2 = EX_MA_Latch.rs2;
			MA_RW_Latch.rd = EX_MA_Latch.rd;
			MA_RW_Latch.writeTo31 = EX_MA_Latch.writeTo31;
			if(MA_RW_Latch.writeTo31) MA_RW_Latch.writeTo31 = false;
		}
	}

}
