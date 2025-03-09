package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static RegisterFile registerFile = new RegisterFile();  //create registerFile
	static MainMemory mem = new MainMemory();  //create memory space
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		byte[] buffer = new byte[4];  //create buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);  //create ByteBuffer
        try (FileInputStream fis = new FileInputStream(assemblyProgramFile)) {  //open file
        fis.read(buffer);  //read value
        int value = byteBuffer.getInt();  //get value
		
		registerFile.setProgramCounter(value);  //set PC to starting address
		System.out.println("Read integer and set PC to: " + value);
		registerFile.setValue(1, 65535);
		registerFile.setValue(2, 65535);
		
		int i = 0;
		
		//put data into main memory
		for(i = 0; i < value; i++){
			byteBuffer.clear(); 
			fis.read(buffer);
			value = byteBuffer.getInt();
			mem.setWord(i, value);
		}
		
		while(fis.read(buffer) != -1){
			byteBuffer.clear(); 
			value = byteBuffer.getInt();
			mem.setWord(i, value);	//put instructions into main memory
			i++;
		}
        } catch (IOException e){
            e.printStackTrace();
        }
	}
	public static void simulate()
	{
		System.out.println("\n\nSimulating now");
		Statistics.setNumberOfCycles(0);
		Statistics.setNumberOfInstructions(0);
		processor.setMainMemory(mem);
		processor.setRegisterFile(registerFile);
		int x = 1;
		while(simulationComplete == false && x <= 50)
		{
			System.out.println("v CYCLE "+x);
			processor.getRWUnit().performRW();
			x++;
			if(simulationComplete) break;
			
			System.out.println("v CYCLE "+x);
			processor.getMAUnit().performMA();
			x++;
			
			System.out.println("v CYCLE "+x);
			processor.getEXUnit().performEX();
			x++;
			
			System.out.println("v CYCLE "+x);
			processor.getOFUnit().performOF();
			x++;
			
			processor.getDLUnit().insertBubbles();

			System.out.println("v CYCLE "+x);
			processor.getIFUnit().performIF();
			x++;
			
			Statistics.setNumberOfInstructions(Statistics.numberOfInstructions+1);
			Statistics.setNumberOfCycles(Statistics.numberOfCycles+1);
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
