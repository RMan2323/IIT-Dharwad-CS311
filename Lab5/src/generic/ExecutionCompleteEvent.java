package generic;

public class ExecutionCompleteEvent extends Event {
	public int rd, value, rem;
	public boolean writeTo31;
	
	public ExecutionCompleteEvent(long eventTime, Element processingElement, int rd, int value, boolean writeTo31, int rem)
	{
		super(eventTime, EventType.ExecutionComplete, processingElement, processingElement);
		this.rd = rd;
		this.value = value;
		this.writeTo31 = writeTo31;
		this.rem = rem;
	}

}
