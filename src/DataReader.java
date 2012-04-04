import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class DataReader {

	public void fill(Graph graph) {
		Map<Integer, Edge> edges = new HashMap<Integer, Edge>();
		Map<Integer, Node> nodes = new HashMap<Integer, Node>();
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("slashdot0902.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  br.readLine();
			  br.readLine();
			  br.readLine();
			  br.readLine();
			  while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				  int id1 = Integer.parseInt(strLine.split("	")[0]);
				  int id2 = Integer.parseInt(strLine.split("	")[1]);
				  int edgeId = edges.size();
				  Edge e = new Edge(id1, id2, edgeId);
				  edges.put(edgeId, e);
				  if (!nodes.containsKey(id1)) {
					  Node n = new Node(id1);
					  n.addEdge(edgeId);
					  nodes.put(id1, n);
				  } else {
					  if (!nodes.get(id1).containsEdge(edgeId))
						  nodes.get(id1).addEdge(edgeId);
				  }
				  //System.out.println (strLine + ": " + id1 +"  " + id2);
			  }
			 

			  //Close the input stream
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
		graph.edges = edges;
		graph.nodes = nodes;
	}
	
}
