package generic;

import processor.pipeline.EX_MA_LatchType;

public class MemoryWriteEvent extends Event {

	int addressToWriteTo;
	int value;
	public boolean isMA_busy;
	public EX_MA_LatchType EX_MA_Latch;
	
	public MemoryWriteEvent(long eventTime, Element requestingElement, Element processingElement, int address, int value, boolean isMA_busy, EX_MA_LatchType EX_MA_Latch) {
		super(eventTime, EventType.MemoryWrite, requestingElement, processingElement);
		this.addressToWriteTo = address;
		this.value = value;
		this.isMA_busy = isMA_busy;
		this.EX_MA_Latch = EX_MA_Latch;
	}

	public int getAddressToWriteTo() {
		return addressToWriteTo;
	}

	public void setAddressToWriteTo(int addressToWriteTo) {
		this.addressToWriteTo = addressToWriteTo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
