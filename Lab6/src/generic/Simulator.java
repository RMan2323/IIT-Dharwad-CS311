package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import generic.EventQueue;
@SuppressWarnings("unused")
public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static RegisterFile registerFile = new RegisterFile();  //create registerFile
	static MainMemory mem = new MainMemory();
	static EventQueue eventQueue = new EventQueue();
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}

	public static EventQueue getEventQueue(){
		return eventQueue;
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
		Statistics.numberOfDataStalls = 0;
		Statistics.numberOfWrongPaths = 0;
		Statistics.dataHits = 0;
		Statistics.dataMisses = 0;
		Statistics.instHits = 0;
		Statistics.instMisses = 0;
		processor.setMainMemory(mem);
		processor.getCache().memory = mem;
		processor.setRegisterFile(registerFile);
		int x = 0;

		while(simulationComplete == false)
		{
			System.out.println("CLOCK: "+Clock.getCurrentTime());
			System.out.println("v CYCLE "+x++ + " RW:");
			processor.getRWUnit().performRW();
			if(simulationComplete) break;

			System.out.println("\nv CYCLE "+x++ + " MA:");
			processor.getMAUnit().performMA();
			
			System.out.println("\nv CYCLE "+x++ + " EX:");
			processor.getEXUnit().performEX();
			
			System.out.println("\nProcess events:");
			eventQueue.processEvents();
			
			System.out.println("\nv CYCLE "+x++ + " OF:");
			processor.getOFUnit().performOF();
			
			System.out.println("\nv CYCLE "+x++ + " IF:");
			processor.getDLUnit().insertBubbles();
			processor.getIFUnit().performIF();

			Clock.incrementClock();
			
			System.out.println("-----------------------------------------------------------------------");
			Statistics.setNumberOfCycles(Statistics.numberOfCycles+1);
		}
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
