package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable, endProg, isBubble, writeTo31;
	int rd, res, rem, endPC;
	int rs1, rs2;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		rs1 = -1;
		rs2 = -1;
		rd = -1;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setRd(int dest){
		rd = dest;
	}

	public void setRes(int result){
		res = result;
	}

	public void setEndProg(boolean endProgram){
		endProg = endProgram;
	}

}
