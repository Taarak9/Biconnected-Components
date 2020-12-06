import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class BiConnectedComponents {

   private static ArrayList<ArrayList<Integer>> edgeList = new ArrayList<ArrayList<Integer>>();
   
   private static ArrayList<ArrayList<Integer>> stack = new ArrayList<ArrayList<Integer>>();

   static int time = 0;
   
   // number of biconnected components
   static int ncomp = 0;

   public static void main(String[] args) throws FileNotFoundException {

      if (args.length != 1) {
           System.out.println("Usage: java BiConnectedComponents <filename>");
           System.exit(1);
      }

      File file = new File(args[0]);

      Scanner scan = new Scanner(file);

      String[] firstLine = scan.nextLine().trim().split(" ");

      int nvertex, nedges;
      nvertex = Integer.parseInt(firstLine[0]);
      nedges = Integer.parseInt(firstLine[1]);

      //System.out.println("No. of vertices: " + nvertex);
      //System.out.println("No. of edges: " + nedges);

      // parse input file
      while (scan.hasNextInt()) {
           String[] line = scan.nextLine().trim().split(" ");
           
           ArrayList<Integer> list = new ArrayList<Integer>();
           for (int i = 0; i < line.length; i++) {
                list.add(Integer.parseInt(line[i]) - 1);
           }
           edgeList.add(list);
      }
     
      addMissingEdges();

      // sorts the edge list
      Collections.sort(edgeList, new ListComparator<>());
      
      Integer[] pointArray = new Integer[nvertex+1];
      
      makePointArray(pointArray, nvertex, nedges);

      //displayGraph();

      //displayPointArray(pointArray, nvertex+1);

      Integer[] start = new Integer[nvertex];
      Integer[] low = new Integer[nvertex];
      Integer[] parent = new Integer[nvertex];


      init(nvertex, start, low, parent);

      runBCC(nvertex, start, low, parent, pointArray);

      System.exit(0);
   }

   // finds if an particular edge is present in a graph
   public static int findEdge(Integer tail, Integer head) {
        int i = 0;

        for (i = 0; i < edgeList.size(); i++) {
             if ( edgeList.get(i).get(0) == head && edgeList.get(i).get(1) == tail)
                return 0;  
        }
        return -1;
   }


   // adds missing other directed edges of an undirected graph
   public static void addMissingEdges() {
      int e = 0;

      for (e = 0; e < edgeList.size(); e++) {
           if (findEdge(edgeList.get(e).get(0), edgeList.get(e).get(1)) == -1) {
               edgeList.add(new ArrayList<Integer>(Arrays.asList(edgeList.get(e).get(1),
					       edgeList.get(e).get(0))));
           }
      }
   }
   
   // returns pointer of a given vertex
   public static int point(Integer vertex) {
      int i = 0;
      
      for (i = 0; i < edgeList.size(); i++) {
           if (edgeList.get(i).get(0) == vertex) {
                return i;
           }
      }
      return -1; 
   }

   // creates pointer array 
   public static void makePointArray(Integer[] pointArray, Integer nvertex, Integer nedges) {
      int k = 0;

      //pointArray[0] = 0;
      for (k = 0; k < nvertex; k++) {
           pointArray[k] = point(k);
      }
      // undirected graph
      pointArray[k] = 2 * nedges;
   }

   // displays graph
   public static void displayGraph() {
      int i = 0;

      System.out.println("----------------------------------------------");
      System.out.println("Edge list");
      System.out.println("----------------------------------------------");
      for (i = 0; i < edgeList.size(); i++)
           System.out.println("tail: " + edgeList.get(i).get(0) + " -> "
			   + "head: " +  edgeList.get(i).get(1));
   }

   // displays pointer array
   public static void displayPointArray(Integer[] array, Integer arraySize) {
      int i = 0;

      System.out.println("----------------------------------------------");
      System.out.println("Point Array");
      System.out.println("----------------------------------------------");
      for (i = 0; i < arraySize; i++)
           System.out.println("Point: " + array[i] + " ->" + 
			   " Vertex: "+ i);
   }

   // biconnected components
   public static void getBCC(Integer u, Integer[] start, Integer[] low, 
		   Integer parent[], Integer[] pointArray) {
      start[u] = low[u] = time++;
      //System.out.println("time " + time);
      //System.out.println("----------------------------------------------");
      
      int nchild = 0;

      int n = 0;

      for (n = pointArray[u]; n < pointArray[u+1]; n++) {
           int v = edgeList.get(n).get(1);
           //System.out.println("Neighbour " + v);
           
           if ( v == parent[u] ) {
                continue;
           }
           // Forward edge
           else if ( ( low[v] != -1 ) && ( start[v] > start[u] ) ) {
                continue;
           }
	   else if ( start[v] == -1 ) {
                nchild++;
		
		parent[v] = u;
                
		stack.add(new ArrayList<Integer>(Arrays.asList(u, v)));
                //System.out.println("Push " + u + ", " + v );
                //System.out.println(" Stack " + stack);

                getBCC(v, start, low, parent, pointArray);

                if (low[u] > low[v]) {
                     low[u] = low[v];  
                }

                if ( low[v] >= start[u] )  {
                     //System.out.println("----------------------------------------------");
                     ncomp++;
                     System.out.printf("\nBiconnected Component %d:\n", ncomp);
		     //System.out.println("Articulation point " + u);
		     System.out.printf("{");
                     while ( ( stack.get(stack.size() - 1).get(0) != u ) || ( stack.get(stack.size() - 1).get(1) != v ) ) {
                         //System.out.println("pop "+stack.get(stack.size() - 1));
                         System.out.print( "(" + ( stack.get(stack.size() - 1).get(0) + 1) + "," +
					 ( stack.get(stack.size() - 1).get(1) + 1 ) + "), ");
                         stack.remove(stack.size() - 1);
                         //System.out.println(stack);
                     }	 
                     //System.out.println("pop "+stack.get(stack.size() - 1));
                     System.out.printf("(" + ( stack.get(stack.size() - 1).get(0) + 1) + "," + 
				     ( stack.get(stack.size() - 1).get(1) + 1 ) + ")");
		     System.out.printf("}\n");
                     stack.remove(stack.size() - 1);
                     //System.out.println(stack);
                     //System.out.println("----------------------------------------------");
                }
           }
	   // Back edge
	   else if ( ( v != parent[u] ) && ( start[v] < start[u] ) ) {
                if ( low[u] > start[v])
                     low[u] = start[v];
                
		stack.add(new ArrayList<Integer>(Arrays.asList(u, v)));
                //System.out.println("Push " + u + ", " + v );
                //System.out.println(" Stack " + stack);
           }
      }
   }

   // initilalization
   public static void init(Integer nvertex, Integer[] start, Integer[] low,
		   Integer parent[]) {
      int x = 0;

      for (x = 0; x < nvertex; x++) {
          start[x] = -1;
          low[x] = -1;
          parent[x] = -1;
      }
   }

   // computes BCC
   public static void runBCC(Integer nvertex, Integer[] start, Integer[] low,
		   Integer parent[], Integer[] pointArray) {
      int y = 0;
      for (y = 0; y < nvertex; y++) {
          //System.out.println("vertex " + y);
          if (start[y] == -1)
	      getBCC(y, start, low, parent, pointArray);

	  if (stack.size() != 0) {
               ncomp++;
               System.out.printf("\nBiconnected Component %d:\n", ncomp);
               System.out.printf("{");
          }
	  
	  /*
	  while (stack.size() != 0) {
               //System.out.println("pop "+stack.get(stack.size() - 1));
               System.out.print( "(" + ( stack.get(stack.size() - 1).get(0) + 1) + "," + 
			       ( stack.get(stack.size() - 1).get(1) + 1 ) + "), ");
               stack.remove(stack.size() - 1);
               //System.out.println(stack);
          }
	  */
      }
      System.out.printf("}\n");
   }
}

class ListComparator<T extends Comparable<T>> implements Comparator<ArrayList<T>> {
  @Override
  public int compare(ArrayList<T> o1, ArrayList<T> o2) {
    for (int i = 0; i < Math.min(o1.size(), o2.size()); i++) {
         int c = o1.get(i).compareTo(o2.get(i));

         if (c != 0)
             return c;
    }
    return Integer.compare(o1.size(), o2.size());
  }
}

