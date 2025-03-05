package processor.pipeline;

import processor.Processor;

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
		if (EX_MA_Latch.isMA_enable()) {
			System.out.println("Performing MA");
			EX_MA_Latch.setMA_enable(false);
			if (EX_MA_Latch.endProg) {
				MA_RW_Latch.setEndProg(true);
				MA_RW_Latch.setRW_enable(true);
				System.out.println("MA " + MA_RW_Latch.endProg);
			} else
				switch (EX_MA_Latch.type) {
					case store:
						containingProcessor.getMainMemory().setWord(EX_MA_Latch.stAddr, EX_MA_Latch.data);
						System.out.println("----\nSTORED " + EX_MA_Latch.data + " into address " + EX_MA_Latch.stAddr);
						break;
					case load:
						containingProcessor.getRegisterFile().setValue(EX_MA_Latch.rd,
								containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr));
						System.out.println(
								"----\nLOADED " + containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr)
										+ " into register " + EX_MA_Latch.rd);
						break;
					default:
						MA_RW_Latch.setRW_enable(true);
						MA_RW_Latch.setRd(EX_MA_Latch.rd);
						MA_RW_Latch.setRes(EX_MA_Latch.aluRes);
						MA_RW_Latch.rem = EX_MA_Latch.remainder;
						break;
				}
		}
	}

}
