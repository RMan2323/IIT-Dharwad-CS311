package processor.pipeline;

import generic.Simulator;
import processor.Processor;

@SuppressWarnings("unused")
public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performRW() {
		if (MA_RW_Latch.isBubble) {
			System.out.println("RW Bubble");
			MA_RW_Latch.writeTo31 = false;
			return;
		}
		if (MA_RW_Latch.isRW_enable()) {
			// MA_RW_Latch.setRW_enable(false);
			System.out.println("Performing RW!!!!!!");
			// System.out.println("RW "+MA_RW_Latch.endProg);

			if (MA_RW_Latch.endProg) {
				Simulator.setSimulationComplete(true);
				System.out.println("----\nENDING");
			} else {
				if (MA_RW_Latch.rd != -1) {
					containingProcessor.getRegisterFile().setValue(MA_RW_Latch.rd, MA_RW_Latch.res);
					System.out.println("----\nPUTTING " + MA_RW_Latch.res + " in register " + MA_RW_Latch.rd);
					containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.rem);
					System.out.println("----\nPUTTING " + MA_RW_Latch.rem + " in register 31");
					if(MA_RW_Latch.writeTo31){
						System.out.println("REMAINDER WRITTEN TO 31!!!!!!!!!!!!!!!!!!!!");
						MA_RW_Latch.writeTo31 = false;
					}
				}
			}
			// IF_EnableLatch.setIF_enable(true);
		}

	}
}