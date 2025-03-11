package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable, isBubble, writeTo31, isBranchBubble;
	int imm1, imm2, branchTgt, rd, rs1, rs2, opcode;
	String operation;
	
	public OF_EX_LatchType()
	{
		isBubble = false;
		isBranchBubble = false;
		EX_enable = false;
		writeTo31 = false;
		rs1 = -1;
		rs2 = -1;
		rd = -1;
	}

	public void setBubble(boolean bub){
		isBubble = bub;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setImm(int op1, int op2){
		imm1 = op1;  //op1
		imm2 = op2;  //op2
	}

	public void setBt(int bt){
		branchTgt = bt;

	}

	public void setOperation(String op){
		operation = op;
	}

	public void setDest(int dest){
		rd = dest;
	}

}
