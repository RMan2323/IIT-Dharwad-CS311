package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable, isIF_busy;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
		isIF_busy = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

}
