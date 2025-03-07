package processor.pipeline;

import processor.Processor;
import processor.pipeline.EX_MA_LatchType.maType;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performEX() {
		OF_EX_Latch.setEX_enable(false);
		// int result = 0, rem = 0;
		int op1 = OF_EX_Latch.imm1, op2 = OF_EX_Latch.imm2;

		switch (OF_EX_Latch.operation) {
			case "add":
			case "addi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 + op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "mul":
			case "muli":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 * op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
				break;
			case "sub":
			case "subi":
				EX_MA_Latch.setMA_enable(true);
				EX_IF_Latch.setIF_enable(false);
				EX_MA_Latch.setAluRes(op1 - op2);
				EX_MA_Latch.setMaType(maType.rw);
				EX_MA_Latch.rd = OF_EX_Latch.rd;
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
				break;
			case "bgt":
				System.out.println((op1 > op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 > op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				break;
			case "beq":
				System.out.println((op1 == op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 == op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				break;
			case "bne":
				System.out.println((op1 != op2) ? "----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt : "Not branching");
				EX_IF_Latch.setBranch((op1 != op2) ? OF_EX_Latch.branchTgt : -1);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				break;
			case "jmp":
			System.out.println("----\nBRANCHING to PC = "+OF_EX_Latch.branchTgt);
				EX_IF_Latch.setIF_enable(true);
				EX_MA_Latch.setMA_enable(false);
				EX_IF_Latch.setBranch(OF_EX_Latch.branchTgt);
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

	}

}
