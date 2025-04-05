package processor.pipeline;

import processor.Processor;
import processor.pipeline.RegisterFile;
import java.util.HashMap;
import processor.pipeline.DataLockUnit;

@SuppressWarnings("unused")
public class OperandFetch {
	Processor containingProcessor;
	DataLockUnit DLU;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	HashMap<String, String> opcodes = new HashMap<String, String>();
	HashMap<String, Integer> registers = new HashMap<String, Integer>();

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, DataLockUnit dLU) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.DLU = dLU;

		opcodes.put("00000", "add");
		opcodes.put("00001", "addi");
		opcodes.put("00010", "sub");
		opcodes.put("00011", "subi");
		opcodes.put("00100", "mul");
		opcodes.put("00101", "muli");
		opcodes.put("00110", "div");
		opcodes.put("00111", "divi");
		opcodes.put("01000", "and");
		opcodes.put("01001", "andi");
		opcodes.put("01010", "or");
		opcodes.put("01011", "ori");
		opcodes.put("01100", "xor");
		opcodes.put("01101", "xori");
		opcodes.put("01110", "slt");
		opcodes.put("01111", "slti");
		opcodes.put("10000", "sll");
		opcodes.put("10001", "slli");
		opcodes.put("10010", "srl");
		opcodes.put("10011", "srli");
		opcodes.put("10100", "sra");
		opcodes.put("10101", "srai");
		opcodes.put("10110", "load");
		opcodes.put("10111", "store");
		opcodes.put("11000", "jmp");
		opcodes.put("11001", "beq");
		opcodes.put("11010", "bne");
		opcodes.put("11011", "blt");
		opcodes.put("11100", "bgt");
		opcodes.put("11101", "end");

		for (int i = 0; i < 32; i++) {
			registers.put(String.format("%05d", Integer.parseInt(Integer.toBinaryString(i))), i);
		}
	}

	public void performOF() {
		int instruction = IF_OF_Latch.getInstruction();
		String BinInstruction = String.format("%32s", Integer.toBinaryString(instruction)).replace(" ", "0");
		String opcode = BinInstruction.substring(0, 5);
		String operation = opcodes.get(opcode);
		System.out.println(IF_OF_Latch.PC + " " + operation);

		if(OF_EX_Latch.isEX_busy){
			IF_OF_Latch.isOF_busy = true;
			System.out.println("OF Busy because EX Busy");
			return;
		}

		if(!IF_OF_Latch.isCorrect){
			System.out.println("NOT CORRECT");
			OF_EX_Latch.isBubble = true;
			// IF_OF_Latch.isCorrect = true;
			return;
		}

		if(IF_OF_Latch.branchBubble > 0){
			System.out.println("OF Bubble 1");
			// IF_OF_Latch.isOF_busy = true;
			IF_OF_Latch.branchBubble--;
			IF_OF_Latch.setOF_enable(false);
			IF_OF_Latch.isCorrect = false;
			OF_EX_Latch.isBubble = true;
			return;
		}

		IF_OF_Latch.isOF_busy = false;

		if(IF_OF_Latch.isBubble && IF_OF_Latch.isOF_enable() && DLU.stalls == 0){
			System.out.println("OF Bubble 2");
			// IF_OF_Latch.isOF_busy = true;
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.isBubble = true;
			IF_OF_Latch.isBubble = false;
			IF_OF_Latch.isCorrect = false;
			return;
		}

		if(IF_OF_Latch.isOF_enable() || DLU.shouldResume()){
			OF_EX_Latch.isBubble = false;
			System.out.println("Performing OF!!!!!!");
			
			// int instruction = IF_OF_Latch.getInstruction();
			// String BinInstruction = String.format("%32s", Integer.toBinaryString(instruction)).replace(" ", "0");
			// String opcode = BinInstruction.substring(0, 5);
			// String operation = opcodes.get(opcode);
			String op1str = BinInstruction.substring(5, 10);
			int op1 = registers.get(op1str);

			String op2str = "";
			int op2 = 0;

			String op3str = "";
			int op3 = 0;

			int imm1 = 0, imm2 = 0, imm3 = 0;
			// System.out.println("OF: Operation: "+operation);
			OF_EX_Latch.rs1 = op1;

			int currentPC = IF_OF_Latch.PC;

			switch (operation) {
				// R3
				case "add":
				case "sub":
				case "mul":
				case "div":
				case "and":
				case "or":
				case "xor":
				case "slt":
				case "sll":
				case "srl":
				case "sra":
					op2str = BinInstruction.substring(10, 15);
					op2 = registers.get(op2str); // register
					op3str = BinInstruction.substring(15, 20);
					op3 = registers.get(op3str); // register
					OF_EX_Latch.setDest(op3);

					imm1 = (containingProcessor.getRegisterFile()).getValue(op1);
					imm2 = (containingProcessor.getRegisterFile()).getValue(op2);

					OF_EX_Latch.setImm(imm1, imm2);
					OF_EX_Latch.rs2 = op2;

					if(operation == "div") {OF_EX_Latch.writeTo31 = true; System.out.println(operation + " Set true");}
					else OF_EX_Latch.writeTo31 = false;
					
					break;

				// R2I
				case "addi":
				case "subi":
				case "muli":
				case "divi":
				case "andi":
				case "ori":
				case "xori":
				case "slti":
				case "slli":
				case "srli":
				case "srai":
					op2str = BinInstruction.substring(10, 15);
					op2 = registers.get(op2str); // register
					OF_EX_Latch.setDest(op2);
					op3str = BinInstruction.substring(15, 32);
					op3 = Integer.parseInt(op3str, 2); // imm

					imm1 = (containingProcessor.getRegisterFile()).getValue(op1);

					OF_EX_Latch.setImm(imm1, op3);
					OF_EX_Latch.rs2 = -1;

					if(operation == "divi") {OF_EX_Latch.writeTo31 = true; System.out.println(operation + " Set true");}

					break;

				// RI
				case "jmp":
					OF_EX_Latch.rs1 = -1;
					OF_EX_Latch.rs2 = -1;
					OF_EX_Latch.rd = -1;
					op2str = BinInstruction.substring(10, 32);
					if (BinInstruction.charAt(10) == '1') {
						op2str = "";
						for (int i = 0; i < 22; i++) {
							op2str = op2str + ((BinInstruction.substring(10, 32).charAt(i) == '1') ? '0' : '1');
						}
						op2 = Integer.parseInt(op2str, 2) + Integer.parseInt("1", 2);
						op2 = -1 * op2;
					} else {
						op2 = Integer.parseInt(op2str, 2);
					}

					OF_EX_Latch.setBt(currentPC + op2);

					break;

				case "beq":
				case "bne":
				case "blt":
				case "bgt":
					op2str = BinInstruction.substring(10, 15);
					op2 = registers.get(op2str); // register

					OF_EX_Latch.rs2 = op2;
					OF_EX_Latch.rd = -1;

					op3str = BinInstruction.substring(15, 32);

					if (BinInstruction.charAt(15) == '1') {
						op3str = "";
						for (int i = 0; i < 17; i++) {
							op3str = op3str + ((BinInstruction.substring(15, 32).charAt(i) == '1') ? '0' : '1');
						}
						op3 = Integer.parseInt(op3str, 2) + Integer.parseInt("1", 2);
						op3 = -1 * op3;
					} else {
						op3 = Integer.parseInt(op3str, 2);
					}

					imm1 = (containingProcessor.getRegisterFile()).getValue(op1);
					imm2 = (containingProcessor.getRegisterFile()).getValue(op2);

					OF_EX_Latch.setImm(imm1, imm2);
					// System.out.println(currentPC);
					OF_EX_Latch.setBt(currentPC + op3);

					break;

				case "end":
					OF_EX_Latch.rs1 = -1;
					OF_EX_Latch.rs2 = -1;
					OF_EX_Latch.rd = -1;
					OF_EX_Latch.setImm(0, 0);
					System.out.println("OF: Got end");
					OF_EX_Latch.endPC = IF_OF_Latch.PC;

					break;

				case "load":
					imm1 = (containingProcessor.getRegisterFile()).getValue(op1);
					op2str = BinInstruction.substring(10, 15);
					op2 = registers.get(op2str); // register

					OF_EX_Latch.rs2 = -1;
					OF_EX_Latch.rd = op2;

					op3str = BinInstruction.substring(15, 32);
					op3 = Integer.parseInt(op3str, 2);

					OF_EX_Latch.setImm(imm1 + op3, op2);

					break;

				case "store":
					System.out.println("OF: Got STORE instruction");
					imm1 = (containingProcessor.getRegisterFile()).getValue(op1);
					op2str = BinInstruction.substring(10, 15);
					op2 = registers.get(op2str); // register
					imm2 = (containingProcessor.getRegisterFile()).getValue(op2);

					op3str = BinInstruction.substring(15, 32);
					op3 = Integer.parseInt(op3str, 2);

					OF_EX_Latch.rd = -1;
					OF_EX_Latch.rs2 = op2;

					OF_EX_Latch.setImm(imm2 + op3, imm1);

					break;
				default:
					break;
			}
			if(DLU.pass == 1 && (OF_EX_Latch.rs1 == OF_EX_Latch.rd || OF_EX_Latch.rs2 == OF_EX_Latch.rd)){
				DLU.pass = 0;
				OF_EX_Latch.isInstructionBubble = false;
			}
			else{
				DLU.pass = 0;
				if(DLU.checkConflicts(OF_EX_Latch.rs1, OF_EX_Latch.rs2)){
					OF_EX_Latch.isInstructionBubble = true;
				} else OF_EX_Latch.isInstructionBubble = false;
			}
			OF_EX_Latch.setOperation(operation);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
			System.out.println("OF: SET EX TO ENABLE");
		}
	}
}