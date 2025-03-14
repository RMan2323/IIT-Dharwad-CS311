package processor.pipeline;

import configuration.Configuration;
import processor.Clock;
import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Processor;
import generic.Event;

public class InstructionFetch implements Element{

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	int dataStall;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		dataStall = 0;
	}

	public void performIF() {
		if(IF_EnableLatch.isIF_enable()){
			if(IF_EnableLatch.isIF_busy){
				return;
			}
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					this,
					containingProcessor.getMainMemory(),
					containingProcessor.getRegisterFile().getProgramCounter())
			);
		}
		if (IF_EnableLatch.isIF_enable() && !EX_IF_Latch.isIF_enable()) {
			System.out.println("Performing IF!!!!!!");
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);

			IF_OF_Latch.setInstruction(newInstruction);
			IF_OF_Latch.PC = currentPC;

			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			IF_OF_Latch.setOF_enable(true);
		} else if (IF_EnableLatch.isIF_enable() && EX_IF_Latch.isIF_enable()) {
			System.out.println("Performing IF!!!!!!");
			int currentPC = (EX_IF_Latch.getPC() == -1) ? containingProcessor.getRegisterFile().getProgramCounter() : EX_IF_Latch.PC;
			System.out.println((EX_IF_Latch.getPC() == -1) ? "Next PC" : "Branched to " + EX_IF_Latch.PC);
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);

			IF_OF_Latch.setInstruction(newInstruction);
			IF_OF_Latch.PC = currentPC;

			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			EX_IF_Latch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
		// IF_EnableLatch.setIF_enable(false);
	}

	@Override
	public void handleEvent(Event e){
		if(IF_OF_Latch.isOF_busy){
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		} else{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			IF_OF_Latch.setInstruction(event.getValue());
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.isIF_busy = false;
		}
	}
}