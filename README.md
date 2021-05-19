# Distance-Vector-routing-simulation

## Operation Theory
The program takes an input text file containing graph information that represents a network of routers. Then the program will ask the user if they want to conduct an uninterrupted or step by step simulation. Once the simulation is completed and all routers have found all best paths using the bellman-ford equation the program will prompt the user if they want to simulate a link failure or terminate the program. This process will keep repeating until the user decides to end the simulation.

## Instruction for compiling and executing DV program:
First, assure all provided files and images are in the same directory. The format of the input file should be [node, node, link cost]. 

Example file format:

1 2 1

2 3 2

Next, to compile and run program enter the following lines into the linux terminal. Filename can be whatever name you would like just make sure it is a .txt file and is passed as an argument like shown below:

Javac DistanceVector.java

Java DistanceVector filename.txt

## Notes on routing table display:
The routing table is displayed as a single table as opposed to making a routing table for each node. This was done for simplicity since I displayed it in the linux terminal. Additionally, this still meets the requirements of showing the routing table of each node being updated. 
I accomplished this by making the graph representation of the nodes bidirectional and using a graph matrix to represent each  distance vector. Thus, when the routing table shown is being updated in the program it is representing the routing table values of node x to y and y to x  i.e., both directions.
		
## Known bugs:

•	During the link cost adjustment portion of the program where the user can change the link cost after the initial stable routing table is calculated will lead to an nullpointer-exception if the source node or destination node entered is not one of the node values given in the input file.
