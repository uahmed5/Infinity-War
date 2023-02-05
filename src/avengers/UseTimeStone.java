package avengers;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Given a starting event and an Adjacency Matrix representing a graph of all possible 
 * events once Thanos arrives on Titan, determine the total possible number of timelines 
 * that could occur AND the number of timelines with a total Expected Utility (EU) at 
 * least the threshold value.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * UseTimeStoneInputFile name is passed through the command line as args[0]
 * Read from UseTimeStoneInputFile with the format:
 *    1. t (int): expected utility (EU) threshold
 *    2. v (int): number of events (vertices in the graph)
 *    3. v lines, each with 2 values: (int) event number and (int) EU value
 *    4. v lines, each with v (int) edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note 1: the last v lines of the UseTimeStoneInputFile is an ajacency matrix for a directed
 * graph. 
 * The rows represent the "from" vertex and the columns represent the "to" vertex.
 * 
 * The matrix below has only two edges: (1) from vertex 1 to vertex 3 and, (2) from vertex 2 to vertex 0
 * 0 0 0 0
 * 0 0 0 1
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * UseTimeStoneOutputFile name is passed through the command line as args[1]
 * Assume the starting event is vertex 0 (zero)
 * Compute all the possible timelines, output this number to the output file.
 * Compute all the posssible timelines with Expected Utility higher than the EU threshold,
 * output this number to the output file.
 * 
 * Note 2: output these number the in above order, one per line.
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for total number of timelines
 *     //Call StdOut.print() for number of timelines with EU >= threshold EU 
 * 
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/UseTimeStone usetimestone.in usetimestone.out
 * 
 * @author Yashas Ravi
 * 
 */

public class UseTimeStone {

    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java UseTimeStone <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        int threshold = StdIn.readInt();
        int numNodes = StdIn.readInt();
        int EU[] = new int[numNodes];
        int event[] = new int[numNodes];
        for(int i = 0; i < numNodes; i++)
        {
            event[i] = StdIn.readInt();
            EU[i] = StdIn.readInt();
        }
        
        LinkedList<Integer> adjMat [] = new LinkedList[numNodes];
        for(int i = 0; i < numNodes; i++)
        {
            adjMat[i] = new LinkedList<>();
            for(int j = 0; j < numNodes; j++)
            {
                int value = StdIn.readInt();
                if(value == 1)
                {
                    adjMat[i].add(j);
                }
            }
        }

        int total = 0;
        int count = 0;
        for(int i = 0; i < numNodes; i++)
        {
            LinkedList<List<Integer>> paths = findPaths(adjMat, 0, i);
            for(List<Integer> list : paths)
            {
                int ev = 0;
                for(int j = 0; j < list.size(); j++)
                {
                    int e = list.get(j);
                    ev += EU[e];
                }
                if(ev >= threshold)
                {
                    count++;
                }
            }
            
            total += paths.size();
        }
        StdOut.setFile(args[1]);
        StdOut.println(total);
        StdOut.println(count);
    }

        private static LinkedList<List<Integer>> findPaths(LinkedList<Integer> [] adjMat, int source, int destination)
        {
            boolean [] isVisited = new boolean[adjMat.length];
            Stack<Integer> path = new Stack<>();
            LinkedList<List<Integer>> allPaths = new LinkedList<>();
            DFS(adjMat, allPaths, path, isVisited, source, destination);
            return allPaths;
        }

        private static void DFS(LinkedList<Integer>[] adjMat, LinkedList<List<Integer>> allPaths, Stack<Integer> path, 
        boolean[] isVisited, int source, int destination)
        {
            path.push(source);
            isVisited[source] = true;
            if(source == destination)
            {
                LinkedList<Integer> list = new LinkedList<>();
                allPaths.addLast(list);
                Stack<Integer> reverse = new Stack<>();
                for(int v : path)
                {
                    reverse.push(v);
                }
                if(reverse.size() >= 1)
                {
                    list.add(reverse.pop());
                }
                while(!reverse.isEmpty())
                {
                    list.add(reverse.pop());
                }
            }
            else
            {
                for(int w : adjMat[source])
                {
                    if(!isVisited[w])
                    {
                        DFS(adjMat, allPaths, path, isVisited, w, destination);
                    }
                }
            }
            path.pop();
            isVisited[source] = false;
        
    }
}
