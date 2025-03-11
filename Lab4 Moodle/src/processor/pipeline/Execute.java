package processor.pipeline;
import processor.Processor;
import processor.pipeline.EX_MA_LatchType.maType;
import processor.pipeline.BranchLockUnit;
import generic.Statistics;

@SuppressWarnings("unused")
public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	BranchLockUnit BLU;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, BranchLockUnit bLU) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.BLU = bLU;
	}

	public void performEX() {
//		OF_EX_Latch.setEX_enable(false);
		if(OF_EX_Latch.isBubble || OF_EX_Latch.isBranchBubble) {
			System.out.println("EX Bubble");
			EX_MA_Latch.isBubble = true;
			EX_MA_Latch.rd = -1;
			OF_EX_Latch.rd = -1;
			System.out.println("Set OF_EX_Latch.rd = "+OF_EX_Latch.rd);
			if(EX_MA_Latch.writeTo31) EX_MA_Latch.writeTo31 = false;
			OF_EX_Latch.writeTo31 = false;
			if(OF_EX_Latch.isBranchBubble) OF_EX_Latch.isBranchBubble = false;
			return;
		}
		if(OF_EX_Latch.isEX_enable()) {
			EX_MA_Latch.isBubble = false;
			System.out.println("Performing EX!!!!!!");
			Statistics.setNumberOfInstructions(Statistics.numberOfInstructions+1);
		// int result = 0, rem = 0;
		int op1 = OF_EX_Latch.imm1, op2 = OF_EX_Latch.imm2;
		// int instruction = OF_EX_Latch.instruction;

		switch (OF_EX_Latch.operation) {
			case "add":
			case "addi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 + op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				OF_EX_Latch.writeTo31 = false;
				System.out.println("EX: Adding");
				break;
			case "mul":
			case "muli":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 * op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				OF_EX_Latch.writeTo31 = false;
				break;
			case "sub":
			case "subi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 - op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				OF_EX_Latch.writeTo31 = false;
				break;
			case "div":
			case "divi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 / op2);
				EX_MA_Latch.setRemainder(op1 % op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				// rem = op1 % op2;
				break;
			case "and":
			case "andi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 & op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				OF_EX_Latch.writeTo31 = false;
				break;
			case "or":
			case "ori":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 | op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "xor":
			case "xori":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 ^ op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "slt":
			case "slti":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes((op1 < op2) ? 1 : 0);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "sll":
			case "slli":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 << op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "srl":
			case "srli":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 >>> op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "sra":
			case "srai":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 >> op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;

			// branches
			case "blt":
				System.out.println((op1 < op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 < op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				if(op1 < op2) BLU.handleBranchTaken();
				break;
			case "bgt":
				System.out.println((op1 > op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 > op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				if(op1 > op2) BLU.handleBranchTaken();
				break;
			case "beq":
				System.out.println((op1 == op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 == op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				if(op1 == op2) BLU.handleBranchTaken();
				break;
			case "bne":
				System.out.println((op1 != op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 != op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				if(op1 != op2) BLU.handleBranchTaken();
				break;
			case "jmp":
			System.out.println("----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				EX_IF_Latch.setBranch(OF_EX_Latch.branchTgt);
				BLU.handleBranchTaken();
				break;

			case "load":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setLdAddr(OF_EX_Latch.imm1);
				EX_MA_Latch.rd = OF_EX_Latch.imm2;
				EX_MA_Latch.setMaType(maType.load);
				break;
			case "store":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setStAddr(OF_EX_Latch.imm1);
				EX_MA_Latch.data = OF_EX_Latch.imm2;
				EX_MA_Latch.setMaType(maType.store);
				break;

			case "end":
				EX_MA_Latch.setMA_enable(true);
				EX_MA_Latch.setEndProg(true);
			default:
				break;
		}
		EX_MA_Latch.rs1 = OF_EX_Latch.rs1;
		EX_MA_Latch.rs2 = OF_EX_Latch.rs2;
		EX_MA_Latch.rd = OF_EX_Latch.rd;
		EX_MA_Latch.writeTo31 = OF_EX_Latch.writeTo31;
		if(EX_MA_Latch.writeTo31) EX_MA_Latch.writeTo31 = false;
	}

	}

}
