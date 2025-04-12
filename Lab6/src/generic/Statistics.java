package generic;

import java.io.PrintWriter;

public class Statistics {
	public static int numberOfInstructions;
	static int numberOfCycles;
	public static int numberOfDataStalls;
	public static int numberOfWrongPaths;
	public static int dataHits, dataMisses, instHits, instMisses;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Throughput (IPC) = " + (float)numberOfInstructions/numberOfCycles);
			writer.println("Data Hits = " + dataHits);
			writer.println("Data Misses = " + dataMisses);
			writer.println("Instruction Hits = " + instHits);
			writer.println("Instruction Misses = " + instMisses);
			
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
