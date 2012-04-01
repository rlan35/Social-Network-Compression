import java.util.ArrayList;


public class Compression {

	
	public static void main(String[] args) {
		DataReader reader = new DataReader();
		Graph graph = new Graph();
		reader.fill(graph);
		System.out.println("Nodes:" + graph.nodes.size());
		System.out.println("Edges:" + graph.edges.size());
		
		ClusteringPhase cp = new ClusteringPhase();
		ArrayList<ArrayList<Integer>> results = cp.doCluster(graph, 1);
		
		System.out.print(results.size());
		
	}
}
