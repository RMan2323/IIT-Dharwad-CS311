package processor.pipeline;

public class EX_MA_LatchType {
	
	public boolean MA_enable, endProg, isBubble, writeTo31, isMA_busy;
	int aluRes, ldAddr, stAddr, remainder;
	enum maType{load, store, rw};
	maType type;
	int data, rd;
	int rs1, rs2, endPC;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		isMA_busy = false;
		rs1 = -1;
		rs2 = -1;
		rd = -1;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setAluRes(int res){
		aluRes = res;
	}

	public void setRemainder(int rem){
		remainder = rem;
	}

	public void setLdAddr(int addr){
		ldAddr = addr;
	}

	public void setStAddr(int addr){
		stAddr = addr;
	}

	public void setMaType(maType type){
		this.type = type;
	}

	public void setEndProg(boolean endProgram){
		endProg = endProgram;
	}

}
