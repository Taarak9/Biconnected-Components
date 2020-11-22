import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ForwardStar {
   public static void main(String[] args) throws FileNotFoundException {

      if (args.length != 1)
          System.out.println("Usage: java ForwardStar <filename>");

      File file = new File(args[0]);

      Scanner scan = new Scanner(file);

      ArrayList<ArrayList<Integer>> biList = new ArrayList<ArrayList<Integer>>();

      String[] firstLine = scan.nextLine().trim().split(" ");

      int nvertex, nedges;
      nvertex = Integer.parseInt(firstLine[0]);
      nedges = Integer.parseInt(firstLine[1]);

      System.out.println("No. of vertices: " + nvertex);
      System.out.println("No. of edges: " + nedges);

      while (scan.hasNextInt()) {
           String[] line = scan.nextLine().trim().split(" ");
           
           ArrayList<Integer> list = new ArrayList<Integer>();
           for (int i = 0; i < line.length; i++) {
                list.add(Integer.parseInt(line[i]));
           }
           biList.add(list);
      }
      
      // sort the graph input
      Collections.sort(biList, new ListComparator<>());

      //System.out.println(biList.get(0).get(1));
      //System.out.println(point(biList, 8));
      
      Integer[] pointArray = new Integer[nvertex+1];
      
      pointArray = makePointArray(biList, pointArray, nvertex, nedges);

      //System.out.println(pointArray[10]);

      displayGraph(biList);

      displayPointArray(pointArray, nvertex+1);
   }

   public static int point(ArrayList<ArrayList<Integer>> biList, Integer vertex) {
      int i = 0;
      
      for (i = 0; i < biList.size(); i++) {
           if (biList.get(i).get(0) == vertex) {
                return i+1;
           }
      }
      return -1; 
   }

   public static Integer[] makePointArray(ArrayList<ArrayList<Integer>> biList, Integer[] pointArray, Integer nvertex, Integer nedges) {
      int k = 0;

      pointArray[0] = 0;
      for (k = 1; k < nvertex; k++) {
           pointArray[k] = point(biList, k);
      }
      pointArray[k] = nedges + 1;

      return pointArray;
   }

   public static void displayGraph(ArrayList<ArrayList<Integer>> biList) {
      int i = 0;

      System.out.println("head tail");
      for (i = 0; i < biList.size(); i++)
           System.out.println(biList.get(i).get(0) + " " +  biList.get(i).get(1));
   }

   public static void displayPointArray(Integer[] array, Integer arraySize) {
      int i = 0;

      System.out.println("Point Array");
      for (i = 1; i < arraySize; i++)
           System.out.println("Vertex: "+i+" Point: "+array[i]);
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
