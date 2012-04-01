import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class ClusteringPhase {
	private static final int MaxCoef = 10000;
	
	private double hashValue(Edge e, int coef)	{
		int id1 = e.nodeId1;
		int id2 = e.nodeId2;
		int id = e.id;
		double hashValue = (MaxCoef - coef) * id1 + coef * id2 + id;
		return hashValue;
	}
	
	public Graph doCluster(Graph g, int k) {
		Map<Integer, Edge> edges = g.edges;
		Map<Integer, Node> nodes = g.nodes;
		Random r = new Random();
		SparseMatrix matrix = new SparseMatrix();
		Map<Integer, float[]> vectors = new HashMap<Integer, float[]>();
		
		
		//Initialize the min-hash vector
		for (Entry<Integer, Node> e : nodes.entrySet()) {
			int nodeId = e.getValue().id;
			float[] minHashes = new float[k];
			vectors.put(nodeId, minHashes);
		}
		
		//Compute the min-hash vector for each node
		for (int i = 0; i < k; i++) {
			for (Entry<Integer, Node> e : nodes.entrySet()) {
				double minHash = Double.MAX_VALUE;
				Node n = e.getValue();
				for (int j = 0; j < n.edgeIds.size(); j++) {
					double hashValue = hashValue(edges.get(n.edgeIds.get(j)), r.nextInt(MaxCoef));
					if (hashValue < minHash) {
						minHash = hashValue;
					}
				}
				vectors.get(n.id)[k] = (float) minHash;
			}
		}
		
		//Generate the min-hash matrix
		for (Entry<Integer, float[]> e : vectors.entrySet()) {
			SparseVector s = new SparseVector(e.getValue(), (int) e.getKey());
			matrix.addRow(s);
		}
		
		
		return null;
	}
	
}
