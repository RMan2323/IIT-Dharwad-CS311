package generic;

import java.io.PrintWriter;
import processor.Clock;

public class Statistics {
	public static int numberOfInstructions;
	static int numberOfCycles;
	public static int numberOfDataStalls;
	public static int numberOfWrongPaths;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of cycles for which OF was stalled = " + numberOfDataStalls);
			writer.println("Number of times instructions on wrong branch entered pipeline = " + numberOfWrongPaths);
			writer.println("Throughput (IPC) = " + (float)numberOfInstructions/numberOfCycles);
			writer.println("Time taken = " + Clock.getCurrentTime());
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}
}
