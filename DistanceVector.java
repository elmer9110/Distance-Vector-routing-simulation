/*
    Elmer Rivera
    Id:1001529110
    Distance Vector algorithm simulation program
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DistanceVector
{
    //global variables declared to hold the routers and links between them
    static int graph[][];
    static int inter[][];
    static int route[][];
    
    //Main method where Distance vector simulation is executed
    //Takes commang line arguments as input paramaters
    public static void main(String[] args) throws Exception
    {
        //Declerations to read input file provided store data provided in file
        File file=new File(args[0]);
        BufferedReader br= new BufferedReader(new FileReader(file));
        Vector<Integer> nodes = new Vector<Integer>();
        Vector<String> linkconnections = new Vector<String>();
        String link;

        //Read data in file until end of file is reached
        while((link=br.readLine())!=null)
        {
            linkconnections.add(link);
            String[] connection= link.split(" ");

            if (!nodes.contains(Integer.parseInt(connection[0]))) 
            {
				nodes.add(Integer.parseInt(connection[0]));
			}
			
			if (!nodes.contains(Integer.parseInt(connection[1]))) 
            {
				nodes.add(Integer.parseInt(connection[1]));
			}
        }

        //Initialize size of routing and distance tables based on the
        //number of nodes provided in the file
        int nodes_count= nodes.size();
        graph= new int[nodes_count][nodes_count];
        inter= new int[nodes_count][nodes_count];
        route= new int[nodes_count][nodes_count];
        Boolean mode_select=null;

        //Initialize distance vectors to infinity unless the source node and
        //destination node are the same
        for(int i=0;i<nodes_count;i++)
        {
            for(int j=0;j<nodes_count;j++)
            {
                if(i==j)
                {
                    graph[i][j]=0;
                }
                else
                {
                    graph[i][j]=16;
                }
            }
        }
        //Update distance vector table based on inital link costs
        for(int i=0;i<linkconnections.size();i++)
        {
            String vector_index=linkconnections.get(i);
            String[] token= vector_index.split(" ");
            int r1= nodes.indexOf(Integer.parseInt(token[0]));
            int r2= nodes.indexOf(Integer.parseInt(token[1]));
            graph[r1][r2]= Integer.parseInt(token[2]);
            graph[r2][r1]= Integer.parseInt(token[2]);

        }

        //Call functions to execute simulation by prompting the user what simulation mode
        //they desire and displaying the routing table being updated to the shortest distance path 
        //for each node until the table is stable.
        mode_select= userModeSelect();
        Table_Init(nodes_count);
        calculate_DistVectors(nodes_count, nodes, mode_select);
        System.out.println("Stable routing table shown below:");
        print_RoutingTable(nodes_count, nodes);

        //Prompt the user if they would like to change any of the link cost between any particular nodes
        //The only input accepted is "yes" or "no", if invalid input is detected then it will prompt the user
        //to try again. *Note, if the user enters "no" the program will exit.
        Boolean linkchange=null;
        while(true)
        {
            Scanner scan= new Scanner(System.in);
            System.out.println("\n\nWould you like to continue and adjust a link cost in the network?");
            System.out.print("Enter yes or no (no caps): ");
            String in= scan.nextLine();
            
            if(in.equals("yes"))
            {
                linkchange=true;
                break;
            }
            else if(in.equals("no"))
            {
                System.out.println("Exiting Program..........");
                System.exit(0);
            }
            else
            {
                System.out.println("ERROR: invalid input detected please try again\n");
            }
        }

        //If user enters "yes" they will be asked to enter a source node and destination node
        //as well as a link cost. Once entered the program will start simulation again using the 
        //routing table already calculated to reach a stable routing table.
        if(linkchange==true)
        {
            while(true)
            {
                Scanner in= new Scanner(System.in);
                System.out.print("Enter the source node for the cost change: ");
			    String source = in.nextLine();
			    System.out.print("Enter the destination node for the cost change: ");
			    String dest = in.nextLine(); 
			    System.out.print("Enter the new cost (16 for link failure): ");
			    String cost = in.nextLine(); 
			
			    
			    int j = nodes.indexOf(Integer.parseInt(source));
			    int k = nodes.indexOf(Integer.parseInt(dest));
			    graph[j][k] = Integer.parseInt(cost);
			    graph[k][j] = Integer.parseInt(cost);
			
			    mode_select= userModeSelect();
                Table_Init(nodes_count);
                calculate_DistVectors(nodes_count, nodes, mode_select);
                System.out.println("Stable routing table shown below:");
                print_RoutingTable(nodes_count, nodes);
			
                //After new routing table is calculated the program will prompt the user if they
                //want to continue or exit the program. If the user chooses to continue then they 
                //will be prompted to make another link cost change.
                while(true)
                {
                    Scanner s =new Scanner(System.in);
                    System.out.print("Enter continue or quit (No Caps): ");
			        String control = in.nextLine();
                    if (control.equals("quit")) 
                    {
				        System.exit(0);
			        }
                    else if(control.equals("continue"))
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("ERROR: invalid input detected please try again\n");
                    }
                } 
			    
            }
        }

        //end of main
    }

    /*
        Function name: userModeSelect()
        Input parameters: none
        Return type: Boolean
        Function that prompts the user two simulation mode options. The user will be able
        to choose between the two modes by entering a 1 or 2, any other input will prompt an error message
        and allow the user to try picking again.
    */
    static Boolean userModeSelect()
    {
        while(true)
        {
            Scanner input= new Scanner(System.in);
            System.out.println("===================================================================\n");
            System.out.println("Simulation Options");
            System.out.println("1: Step by Step Display\n"+"2: Simulation runs without intervention");
            System.out.print("Enter 1 or 2 to select simulation mode: ");
            String user_picked= input.nextLine();

            if(user_picked.equals("1"))
            {
                return true;
            }
            else if(user_picked.equals("2"))
            {
                return false;
            }
            else
            {
                System.out.println("ERROR: invalid input detected please try again\n");
            }
            
        }
    }

    /*
        Function name: Table_Init()
        Input parameters: count_Nodes
        Return type: void
        Function that takes the number of nodes provided by the input file and initializes
        the routing table and the intermediate hops table 
        
    */
    static void Table_Init(int count_Nodes)
    {
        for(int i=0;i<count_Nodes;i++)
        {
            for(int j=0;j<count_Nodes;j++)
            {
                if(i==j)
                {
                    route[i][j]=0;
                    inter[i][j]=i;
                }
                else
                {
                    route[i][j]=16;
                    inter[i][j]=16;
                }
            }
        }
    }

    /*
        Function name: updateRoutingTable()
        Input parameters: count_nodes, source_node
        Return type: Boolean
        Function that updates the routing table based on bellman-ford equation and the 
        link costs currently set to of the source node passed in the function. Also, the
        function will check if the table has reached a stable state. Then it will return either
        true or false.
    */
    static Boolean updateRoutingTable(int count_Nodes, int source_node)
    {
        Boolean is_table_stable= true;
        int link_cost=0;
        int intermediate_dist=0;
        //Update values by getting link cost and intermediate distance from source node 
        //to each other node and then plug values into bellman-ford equation and get the min.
        for(int i=0;i<count_Nodes;i++)
        {
            if(graph[source_node][i]!=16)
            {
                link_cost=graph[source_node][i];
                for(int j=0;j<count_Nodes;j++)
                {
                    intermediate_dist=route[i][j];
                    if(inter[i][j]==source_node)
                    {
                        intermediate_dist=16;
                    }
                    if ((link_cost + intermediate_dist) < route[source_node][j]) 
                    {
						route[source_node][j] = link_cost + intermediate_dist; 
						inter[source_node][j] = i; 
						is_table_stable = false; 
					}

                }
            }
        }
        return is_table_stable;
    }

    /*
        Function name: print_RoutingTable()
        Input parameters: count_nodes, Vector<Integer> nodes
        Return type: void
        Function that formats and prints the routing table in a matrix form and information
        to help the user understand the routing table.
        
    */
    static void print_RoutingTable(int count_Nodes, Vector<Integer> nodes)
    {
        System.out.println("\n===================================================================");
        System.out.println("Routing table below represents routing table for all nodes. *SEE readme for more details");
        System.out.println("Entire row represents the entire distance vector for the corresponding node in row.");
        System.out.println("Row and column labels are the nodes given in input.");
        System.out.println("\t\t\tNodes\n");
        for(int i=0;i<count_Nodes;i++)
        {
            System.out.print("\t"+nodes.get(i));
        }
        System.out.println();
        System.out.println("--|-------------------------------------------");
        for(int i=0;i<count_Nodes;i++)
        {
            System.out.print(nodes.get(i)+" "+"|");
            for(int j=0;j<count_Nodes;j++)
            {
                System.out.print("\t"+route[i][j]);
            }
            System.out.println();
        }
    }

    /*
        Function name: calculate_DistVectors()
        Input parameters: count_Nodes, Vector<Integer> nodes, mode
        Return type: void
        Function will calculae the distance vectors of each router to each node and call updateRoutingTable()
        to update routing table and print it until the routing table reaches a stable state. The function will 
        print the routing table without stopping or prompt the user to press enter to continue updating the routing table
        based on the simulation mode the user picked. The function will print the number of cycles the simulation needed for the 
        routing table to be stable.
        
    */
    static void calculate_DistVectors(int count_Nodes, Vector<Integer> nodes, Boolean mode)
    {
        long time_start=0;
        long time_finish=0;
        int source_router=0;
        int number_cycles=0;
        int counter=0;
        Boolean stable_status=false;

        if(mode==false)
        {
            time_start=System.currentTimeMillis();
        }
        System.out.println("\nStarting Distance Vector Algorithm................................");
        
        //delay the start by 2 seconds to allow the user to follow along
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        //Prints the intial routing table with no calcuations just inital link costs
        print_RoutingTable(count_Nodes, nodes);

        while(true)
        {
            stable_status=updateRoutingTable(count_Nodes, source_router);
            if(stable_status==true)
            {
                //This part of code helps give the compiler time update all values in the routing 
                //table.
                counter++;
                if(counter==2)
                {
                    System.out.println("\n\n\nAlgorithm has reached a stable state");
                    break;
                }
                
            }
            //Print updated routing table and increment number of cycles and the source node
            //being used to calcualte distance vectors.
            System.out.println("Updating..................");
            print_RoutingTable(count_Nodes, nodes);
            number_cycles++;
            source_router++;

            if(source_router==count_Nodes)
            {
                source_router=0;
            }

            //If the user picked step by step mode then they will promtped to press enter to see
            //the next updated routing table
            if(mode==true)
            {
                System.out.println("Press ENTER to see next iteration of routing tables");
                Scanner scan = new Scanner(System.in);
				scan.nextLine();
            }
        }
        //The program will also print the time elapsed during the calculation if not in step by step mode.
        //The 2000 is subracted because of the 2 second delay at the beginning of the simulation. The value is 2000
        //because it is in milliseconds.
        if(mode==false)
        {
            time_finish=System.currentTimeMillis();
            time_finish= (time_finish-time_start)-2000;
            System.out.println("Time till stable state was reached: "+time_finish+ " milliseconds");
        }


        System.out.println("Number of cycles needed to reach stable state: " + number_cycles + "\n");
    }


}