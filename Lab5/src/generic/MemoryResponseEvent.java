package generic;

public class MemoryResponseEvent extends Event {

	int value, addr;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int addr) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.addr = addr;
	}

	public int getValue() {
		return value;
	}

	public int getAddr() {
		return addr;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
