package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable, isBubble, isOF_busy, isCorrect;
	int instruction, PC, branchBubble;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		isOF_busy = false;
		isCorrect = true;
		branchBubble = 0;
	}

	public void setBubble(boolean bub){
		isBubble = bub;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
