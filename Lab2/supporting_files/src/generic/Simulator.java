package generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;
import javax.lang.model.type.NullType;

import generic.Operand.OperandType;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.io.IOException;

public class Simulator {

	static FileInputStream inputcodeStream = null;
	static String outputFile;
	static Path p;
	static ArrayList<Integer> instructionInts = new ArrayList<Integer>();

	// static FileOutputStream fos = null;
	// static FileChannel fileChannel = null;

	// public static void writeToObjectFile(int instructionInt) {
	// ByteBuffer buffer = ByteBuffer.allocate(4);
	// buffer.putInt(instructionInt);
	// buffer.flip();

	// try {
	// fileChannel.write(buffer);

	// fileChannel.write(ByteBuffer.wrap(new byte[] { 10 }));
	// } catch (IOException e) {
	// }
	// }

	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();

		outputFile = objectProgramFile;
		p = Paths.get(outputFile);

		// if (outputFile == null) {
		// throw new IllegalArgumentException("Output file path cannot be null or
		// empty");
		// }

		// try {
		// // Open file for writing in append mode
		// fos = new FileOutputStream(outputFile, true); // true for append mode
		// fileChannel = fos.getChannel();
		// } catch (FileNotFoundException e) {
		// System.err.println("Error: The file " + outputFile + " could not be found or
		// created.");
		// //throw e; // Rethrow the exception to notify the caller
		// }
	}

	// public static void closeFiles() throws IOException {
	// if (fileChannel != null) {
	// fileChannel.close();
	// }
	// if (fos != null) {
	// fos.close();
	// }
	// }

	public static void writeBin() {
		try {
			FileChannel fc = (FileChannel) Files.newByteChannel(p, CREATE, WRITE, TRUNCATE_EXISTING);
			for (int i = 0; i < instructionInts.size(); i++) {
				System.out.println(instructionInts.get(i));
				ByteBuffer bb = ByteBuffer.allocate(4);
				bb.putInt(instructionInts.get(i));
				bb.flip();

				fc.write(bb);
			}
			fc.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static String convertToNBitBinary(int off, int n) {
        if (off < 0) {
            off = (1 << n) + off;
        }

        String binaryString = String.format("%" + n + "s", Integer.toBinaryString(off)).replace(' ', '0');

		if (binaryString.length() > n) {
			binaryString = binaryString.substring(binaryString.length() - n);
		}

        return binaryString;
    }

	public static int getTwosComplement(String binaryInt) {
		if (binaryInt.charAt(0) == '1') {
			String invertedInt = invertDigits(binaryInt);
			return (Integer.parseInt(invertedInt, 2) + 1) * (-1);
		} else {
			return Integer.parseInt(binaryInt, 2);
		}
	}

	public static String invertDigits(String binaryInt) {
		String result = binaryInt;
		result = result.replace("0", " ");
		result = result.replace("1", "0");
		result = result.replace(" ", "1");
		return result;
	}

	public static void assemble() {
		HashMap<String, String> opcodes = new HashMap<String, String>();
		opcodes.put("add", "00000");
		opcodes.put("addi", "00001");
		opcodes.put("sub", "00010");
		opcodes.put("subi", "00011");
		opcodes.put("mul", "00100");
		opcodes.put("muli", "00101");
		opcodes.put("div", "00110");
		opcodes.put("divi", "00111");
		opcodes.put("and", "01000");
		opcodes.put("andi", "01001");
		opcodes.put("or", "01010");
		opcodes.put("ori", "01011");
		opcodes.put("xor", "01100");
		opcodes.put("xori", "01101");
		opcodes.put("slt", "01110");
		opcodes.put("slti", "01111");
		opcodes.put("sll", "10000");
		opcodes.put("slli", "10001");
		opcodes.put("srl", "10010");
		opcodes.put("srli", "10011");
		opcodes.put("sra", "10100");
		opcodes.put("srai", "10101");
		opcodes.put("load", "10110");
		opcodes.put("store", "10111");
		opcodes.put("jmp", "11000");
		opcodes.put("beq", "11001");
		opcodes.put("bne", "11010");
		opcodes.put("blt", "11011");
		opcodes.put("bgt", "11100");
		opcodes.put("end", "11101");

		HashMap<Integer, String> registers = new HashMap<Integer, String>();
		for (int i = 0; i < 32; i++) {
			registers.put(i, String.format("%05d", Integer.parseInt(Integer.toBinaryString(i))));
		}
		instructionInts.add(ParsedProgram.firstCodeAddress);

		for (int i = 0; i < ParsedProgram.data.size(); i++) {
			instructionInts.add(ParsedProgram.data.get(i));
		}

		Operand op1, op2, op3;
		String operation,
				operand1Val, operand2Val, operand3Val,
				operand1Type, operand2Type, operand3Type,
				op1Bin, op2Bin, op3Bin,
				instruction;
		int instructionInt = 0;
		System.out.println("Code:");
		for (int i = 0; i < ParsedProgram.code.size(); i++) {

			// getting operation
			operation = new String(ParsedProgram.code.get(i).operationType.toString());

			System.out.println(operation);

			// getting operands
			op1 = ParsedProgram.code.get(i).sourceOperand1;
			op2 = ParsedProgram.code.get(i).sourceOperand2;
			System.out.println(op2);
			op3 = ParsedProgram.code.get(i).destinationOperand;

			// getting operandTypes
			operand1Type = (op1 != null) ? op1.getOperandType().toString() : "none";
			operand2Type = (op2 != null) ? op2.getOperandType().toString() : "none";
			operand3Type = (op3 != null) ? op3.getOperandType().toString() : "none";

			// getting operandValues
			operand1Val = (op1 != null) ? Integer.toString(op1.getValue()) : "none";
			if (operand2Type.equals("none")) {
				operand2Val = "none";
			} else if (operand2Type.equals("Register") || operand2Type.equals("Immediate")) {
				operand2Val = Integer.toString(op2.getValue());
			} else {
				operand2Val = op2.getLabelValue();
			}
			System.out.println("OP2Val: "+operand2Val);
			operand3Val = (op3 != null) ? Integer.toString(op3.getValue()) : "none";

			// converting operands to binary
			op1Bin = (op1 != null) ? registers.get(op1.getValue()) : "00000";
			if (operand2Type.equals("none")) {
				op2Bin = "00000";
			} else if (operand2Type.equals("Register") || operand2Type.equals("Immediate")) {
				op2Bin = registers.get(op2.getValue());
			} else {
				op2Bin = Integer.toBinaryString(ParsedProgram.symtab.get(operand2Val));
			}
			op3Bin = (op3 != null) ? registers.get(op3.getValue()) : "00000";

			switch (operation) {
				// R3 type
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
				case "sra": {
					op1Bin = registers.get(op1.getValue()); // register
					op2Bin = registers.get(op2.getValue()); // register
					op3Bin = registers.get(op3.getValue()); // register

					instruction = opcodes.get(operation) + op1Bin + op2Bin + op3Bin + "000000000000";
					instructionInt = getTwosComplement(instruction);

					break;
				}

				// R2I type
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
				case "load":
				case "store": {
					op1Bin = registers.get(op1.getValue()); // register
					op2Bin = String.format("%17s", Integer.toBinaryString(ParsedProgram.symtab.get(op2.getLabelValue()))).replace(' ', '0'); // immediate
					op3Bin = registers.get(op3.getValue()); // register
					System.out.println("Label value: "+op2.getLabelValue());
					System.err.println("Final: "+ParsedProgram.symtab.get(op2.getLabelValue()));
					instruction = opcodes.get(operation) + op1Bin + op3Bin + op2Bin;
					instructionInt = getTwosComplement(instruction);

					break;
				}

				case "beq":
				case "bne":
				case "blt":
				case "bgt": {
					op1Bin = registers.get(op1.getValue()); // register
					op2Bin = registers.get(op2.getValue()); // register
					int goTo = ParsedProgram.symtab.get(op3.getLabelValue());
					int PC = i;
					int off = goTo - PC - 1;
					op3Bin = convertToNBitBinary(off, 17);

					instruction = opcodes.get(operation) + op1Bin + op2Bin + op3Bin;
					instructionInt = getTwosComplement(instruction);

					break;
				}

				// RI type :
				case "jmp": {
					// System.out.println(ParsedProgram.symtab.get(op3.getLabelValue()));
					// op3Bin = Integer.toBinaryString(ParsedProgram.symtab.get(op3.getLabelValue())); // label

					// instruction = opcodes.get(operation) + "0000000000000000000000" + op3Bin;
					// instructionInt = getTwosComplement(instruction);
					int goTo = ParsedProgram.symtab.get(op3.getLabelValue());
					int PC = i;
					int off = goTo - PC - 1;
					op3Bin = convertToNBitBinary(off, 22);
					System.out.println(off + "lol = " +op3Bin);
					instructionInt = getTwosComplement("1100000000" + op3Bin);

					break;
				}

				case "end":
					// System.out.println("11101000000000000000000000000000");

					instruction = "11101000000000000000000000000000";
					instructionInt = getTwosComplement(instruction);

					break;

				default:
					Misc.printErrorAndExit("unknown instruction!!");
			}
			// writeToObjectFile(instructionInt);
			instructionInts.add(instructionInt);
		}
		writeBin();
		// try {
		// closeFiles();
		// } catch (IOException e) {
		// }
	}
} // end of class
