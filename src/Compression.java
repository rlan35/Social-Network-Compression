import java.util.ArrayList;


public class Compression {

	
	public static void main(String[] args) {
		DataReader reader = new DataReader();
		Graph graph = new Graph();
		reader.fill(graph);
		System.out.println("Nodes:" + graph.nodes.size());
		System.out.println("Edges:" + graph.edges.size());
		
		ClusteringPhase cp = new ClusteringPhase();

		ArrayList<ArrayList<Integer>> results = cp.doCluster(graph, 10);
		
//		int count = 0;
		for (int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i).size());
			System.out.println(results.get(i));
			//count += results.get(i).size();
		}
		System.out.println("C size:" + results.size());

		System.out.println(results.size());
		
		System.out.println("before mining: # of nodes = " + graph.nodes.size());
		System.out.println("before mining: # of edges = " + graph.edges.size());
		MiningPhase.doMining(graph, results);
		System.out.println("after mining: # of nodes = " + graph.nodes.size());
		System.out.println("after mining: # of edges = " + graph.edges.size());

	}
}
