# Lab 0: Understanding Java (National Border Alert Simulation)
To run the simulator:
`java softwareSim.java`

# Lab 1: Comparing ISAs & Writing assembly in ToyRISC
To write a program and run it:
* Write the program and save it with a `.asm` extension
* Run the emulator as: `java -Xmx1g -jar <path to emulator> <path to .asm file> <starting address> <ending address>`

Address range = [0, 65535]

# Lab 2: Converting assembly to binaries
To run the assembler:
* Get the program file with `.asm` extension
* Run the assembler as: `java -Xmx1g -jar <path to assembler> <path to .asm file> <path to .out file>`

# Lab 3: Single Cycle Processor
To run the processor simulator:
* Get the binary with `.out` extension
* Run the simulator as `java -Xmx1g -jar <path to simulator> <path to config.xml> <path to stats file> <path to .out file>`

# Lab 4: Pipelining
To run the processor simulator:
* Get the binary with `.out` extension
* Run the simulator as `java -Xmx1g -jar <path to simulator> <path to config.xml> <path to stats file> <path to .out file>`

# Lab 5: Simulating Latencies
To change latencies, update the ```config.xml```

To run the processor simulator:
* Get the binary with `.out` extension
* Run the simulator as `java -Xmx1g -jar <path to simulator> <path to config.xml> <path to stats file> <path to .out file>`

# Lab 6: Simulating Caches
To change cache configurations, update the `config.xml`

To run the processor simulator:
* Get the binary with `.out` extension
* Run the simulator as `java -Xmx1g -jar <path to simulator> <path to config.xml> <path to stats file> <path to .out file>`
