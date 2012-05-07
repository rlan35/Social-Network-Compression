import java.util.ArrayList;


public class Compression {


	public static void main(String[] args) {
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
		DataReader reader = new DataReader();
		Graph graph = new Graph();
		reader.fill(graph);
		System.out.println("Nodes:" + graph.nodes.size());
		System.out.println("Edges:" + graph.edges.size());

		ClusteringPhase cp = new ClusteringPhase();
		long startingTime = System.currentTimeMillis();
		ArrayList<ArrayList<Integer>> results = cp.doCluster(graph, 100);
		long endingTime = System.currentTimeMillis();

		System.out.println("Clustering Time: " + (endingTime - startingTime) + "ms");

		startingTime = System.currentTimeMillis();
		System.out.println("before mining: # of nodes = " + graph.nodes.size());
		System.out.println("before mining: # of edges = " + graph.edges.size());
		MiningPhase.doMining(graph, results);
		System.out.println("after mining: # of nodes = " + graph.nodes.size());
		System.out.println("after mining: # of edges = " + graph.edges.size());
		endingTime = System.currentTimeMillis();
		
		System.out.println("Mining Time: " + (endingTime - startingTime) / 1000 + "s");
		
		//add one synthetic node
		//		Node n = new Node(11111111);
		//		graph.nodes.put(n.id, n);
		//		Edge e1 = new Edge(11111111, 17325, graph.edges.size());
		//		graph.edges.put(e1.id, e1);
		//		Edge e2 = new Edge(11111111, 55937, graph.edges.size());
		//		graph.edges.put(e2.id, e2);
		//		Edge e3 = new Edge(11111111, 61955, graph.edges.size());
		//		graph.edges.put(e3.id, e3);
		//		Edge e4 = new Edge(11111111, 61953, graph.edges.size());
		//		graph.edges.put(e4.id, e4);
		//		Edge e5 = new Edge(11111111, 61952, graph.edges.size());
		//		graph.edges.put(e5.id, e5);
		//		Edge e6 = new Edge(11111111, 61959, graph.edges.size());
		//		graph.edges.put(e6.id, e6);
		//		Edge e7 = new Edge(11111111, 61958, graph.edges.size());
		//		graph.edges.put(e7.id, e7);
		//		Edge e8 = new Edge(11111111, 61957, graph.edges.size());
		//		graph.edges.put(e8.id, e8);
		//		Edge e9 = new Edge(11111111, 61939, graph.edges.size());
		//		graph.edges.put(e9.id, e9);
		//		Edge e10 = new Edge(11111111, 61937, graph.edges.size());
		//		graph.edges.put(e10.id, e10);
		//		Edge e11 = new Edge(11111111, 61941, graph.edges.size());
		//		graph.edges.put(e11.id, e11);
		//		Edge e12 = new Edge(11111111, 61940, graph.edges.size());
		//		graph.edges.put(e12.id, e12);
		//		Edge e13 = new Edge(11111111, 61942, graph.edges.size());
		//		graph.edges.put(e13.id, e13);
		//		n.addEdge(e1.id);
		//		n.addEdge(e2.id);
		//		n.addEdge(e3.id);
		//		n.addEdge(e4.id);
		//		n.addEdge(e5.id);
		//		n.addEdge(e6.id);
		//		n.addEdge(e7.id);
		//		n.addEdge(e8.id);
		//		n.addEdge(e9.id);
		//		n.addEdge(e10.id);
		//		n.addEdge(e11.id);
		//		n.addEdge(e12.id);
		//		n.addEdge(e13.id);

		//Add another synthetic node
		Node n = new Node(11111111);
		graph.nodes.put(n.id, n);
		Edge e1 = new Edge(11111111, 13430, graph.edges.size());
		graph.edges.put(e1.id, e1);
		Edge e2 = new Edge(11111111, 14331, graph.edges.size());
		graph.edges.put(e2.id, e2);
		Edge e3 = new Edge(11111111, 33223, graph.edges.size());
		graph.edges.put(e3.id, e3);
		Edge e4 = new Edge(11111111, 4444, graph.edges.size());
		graph.edges.put(e4.id, e4);
		Edge e5 = new Edge(11111111, 22235, graph.edges.size());
		graph.edges.put(e5.id, e5);
		Edge e6 = new Edge(11111111, 1116, graph.edges.size());
		graph.edges.put(e6.id, e6);
		Edge e7 = new Edge(11111111, 33127, graph.edges.size());
		graph.edges.put(e7.id, e7);
		Edge e8 = new Edge(11111111, 45338, graph.edges.size());
		graph.edges.put(e8.id, e8);
		Edge e9 = new Edge(11111111, 33329, graph.edges.size());
		graph.edges.put(e9.id, e9);
		Edge e10 = new Edge(11111111, 2330, graph.edges.size());
		graph.edges.put(e10.id, e10);
		Edge e11 = new Edge(11111111, 3311, graph.edges.size());
		graph.edges.put(e11.id, e11);
		Edge e12 = new Edge(11111111, 22312, graph.edges.size());
		graph.edges.put(e12.id, e12);
		Edge e13 = new Edge(11111111, 22213, graph.edges.size());
		graph.edges.put(e13.id, e13);
		n.addEdge(e1.id, e1.nodeId2);
		n.addEdge(e2.id, e2.nodeId2);
		n.addEdge(e3.id, e3.nodeId2);
		n.addEdge(e4.id, e4.nodeId2);
		n.addEdge(e5.id, e5.nodeId2);
		n.addEdge(e6.id, e6.nodeId2);
		n.addEdge(e7.id, e7.nodeId2);
		n.addEdge(e8.id, e8.nodeId2);
		n.addEdge(e9.id, e9.nodeId2);
		n.addEdge(e10.id, e10.nodeId2);
		n.addEdge(e11.id, e11.nodeId2);
		n.addEdge(e12.id, e12.nodeId2);
		n.addEdge(e13.id, e13.nodeId2);


		// Cluster the new node
		startingTime = System.currentTimeMillis();
		ArrayList<Integer> foo = cp.assignCluster(graph, 11111111);
		endingTime = System.currentTimeMillis();

		System.out.println("Assigning Time: " + (endingTime - startingTime)+ "ms");
		//		int count = 0;
		//		for (int i = 0; i < results.size(); i++) {
		//			count += results.get(i).size();
		//			System.out.println(results.get(i).size());
		//			System.out.println(results.get(i));
		//			//count += results.get(i).size();
		//		}
		//		System.out.println("C size:" + results.size());
		//
		//		System.out.println(count);
		//		

		startingTime = System.currentTimeMillis();
		System.out.println("before re-mining: # of nodes = " + graph.nodes.size());
		System.out.println("before re-mining: # of edges = " + graph.edges.size());
		MiningPhase.redo(graph, foo);
		System.out.println("after re-mining: # of nodes = " + graph.nodes.size());
		System.out.println("after re-mining: # of edges = " + graph.edges.size());
		endingTime = System.currentTimeMillis();
		System.out.println("Re-mining Time: " + (endingTime - startingTime) / 1000 + "s");
	}
}
