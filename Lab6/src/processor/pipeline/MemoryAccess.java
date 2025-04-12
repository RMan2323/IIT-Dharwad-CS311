package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import generic.Event;

@SuppressWarnings("unused")
public class MemoryAccess implements Element {
	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA() {
		if(EX_MA_Latch.isMA_busy){
			System.out.println("MA is busy");
			return;
		}
		if(EX_MA_Latch.isBubble) {
			System.out.println("MA Bubble");
			MA_RW_Latch.isBubble = true;
			MA_RW_Latch.rd = -1;
			if(MA_RW_Latch.writeTo31) MA_RW_Latch.writeTo31 = false;
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.rd = -1;
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
						containingProcessor.getCache().cacheDataWrite(EX_MA_Latch.stAddr, EX_MA_Latch.data);
						Simulator.getEventQueue().addEvent(
							new MemoryWriteEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								EX_MA_Latch.stAddr,
								EX_MA_Latch.data,
								EX_MA_Latch.isMA_busy,
								EX_MA_Latch
							)
						);
						System.out.println("MA: Will store "+EX_MA_Latch.data+" into address "+EX_MA_Latch.stAddr+" at time "+Clock.getCurrentTime()+Configuration.mainMemoryLatency);
						EX_MA_Latch.isMA_busy = true;
						break;
					case load:
						MA_RW_Latch.setRd(EX_MA_Latch.rd);
						int value = containingProcessor.getCache().cacheDataRead(EX_MA_Latch.ldAddr);
						int cacheLatency = containingProcessor.getCache().latency;
						// Simulator.getEventQueue().addEvent(
						// 	new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), EX_MA_Latch.ldAddr)
						// );
						// System.out.println("MA: Getting data at "+EX_MA_Latch.ldAddr+" into register "+EX_MA_Latch.rd+" at time "+(Clock.getCurrentTime()+Configuration.mainMemoryLatency));

						if (containingProcessor.getCache().wasDataHit) {
							//cache hit: use cache latency and schedule immediate response
							System.out.println("MA: Cache HIT at " + EX_MA_Latch.ldAddr + ", value = " + value + ", latency = " + cacheLatency);
							Simulator.getEventQueue().addEvent(
								new MemoryResponseEvent(
									Clock.getCurrentTime() + cacheLatency,
									this,
									this,
									value,
									EX_MA_Latch.ldAddr
								)
							);
						} else {
							//cache miss: use memory latency
							System.out.println("MA: Cache MISS at " + EX_MA_Latch.ldAddr + ", scheduling memory read, latency = " + Configuration.mainMemoryLatency);
							Simulator.getEventQueue().addEvent(
								new MemoryReadEvent(
									Clock.getCurrentTime() + Configuration.mainMemoryLatency,
									this,
									containingProcessor.getMainMemory(),
									EX_MA_Latch.ldAddr
								)
							);
						}

						EX_MA_Latch.isMA_busy = true;
						break;
					default:
						MA_RW_Latch.setRd(EX_MA_Latch.rd);
						MA_RW_Latch.setRes(EX_MA_Latch.aluRes);
						MA_RW_Latch.rem = EX_MA_Latch.remainder;
						MA_RW_Latch.setRW_enable(true);
						break;
				}
			MA_RW_Latch.rs1 = EX_MA_Latch.rs1;
			MA_RW_Latch.rs2 = EX_MA_Latch.rs2;
			MA_RW_Latch.rd = EX_MA_Latch.rd;
			MA_RW_Latch.writeTo31 = EX_MA_Latch.writeTo31;
			EX_MA_Latch.writeTo31 = false;
		}
	}
	
	@Override
	public void handleEvent(Event e){
		MemoryResponseEvent event = (MemoryResponseEvent) e;
		System.out.println("MA: LOADING " + containingProcessor.getMainMemory().getWord(EX_MA_Latch.ldAddr) + " into register " + EX_MA_Latch.rd);
		System.out.println("(Value is "+event.getValue()+")");
		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.isMA_busy = false;
		MA_RW_Latch.setRes(event.getValue());
	}
}
