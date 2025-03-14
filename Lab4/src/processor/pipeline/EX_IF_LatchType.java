package processor.pipeline;

public class EX_IF_LatchType {
	boolean IF_enable;
	int PC;

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setBranch(int b){
		PC = b;
	}
	
	public EX_IF_LatchType()
	{
		IF_enable = false;
	}

	public int getPC(){
		return PC;
	}

}
