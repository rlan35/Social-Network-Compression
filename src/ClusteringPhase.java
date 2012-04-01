import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class ClusteringPhase {
	private static final int MaxCoef = 10000;
	Map<Integer, SparseVector> data = new HashMap<Integer, SparseVector>();
	private double hashValue(Edge e, int coef)	{
		int id1 = e.nodeId1;
		int id2 = e.nodeId2;
		int id = e.id;
		double hashValue = (MaxCoef - coef) * id1 + coef * id2 + id;
		return hashValue;
	}
	
	@SuppressWarnings("unchecked")
	public Graph doCluster(Graph g, int k) {
		Map<Integer, Edge> edges = g.edges;
		Map<Integer, Node> nodes = g.nodes;
		int threshold = (int) Math.sqrt(nodes.size());
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
		data.clear();
		for (Entry<Integer, float[]> e : vectors.entrySet()) {
			SparseVector s = new SparseVector(e.getValue(), (int) e.getKey());
			matrix.addRow(s);
			data.put(e.getKey(), s);
		}
		
		Collections.sort(matrix.rows);
		
		ArrayList<SparseVector> v = matrix.rows;
		
		ArrayList<SparseVector> list = new ArrayList<SparseVector>();
		int currentId = -1;
		for (int i = 0; i < v.size(); i++) {
			if (currentId != v.get(i).getId()) {
				if (!list.isEmpty()) {
					if (list.size() > threshold) {
						ArrayList<ArrayList<Integer>> results = clusterFinder(list, 1, threshold);
					} else {
						ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
						ArrayList<Integer> tempList = new ArrayList<Integer>();
						for (SparseVector s : list) {
							tempList.add(s.getId());
						}
					}
					
					// Mining Phase here; Input results - clusters of ids of nodes
				} 
				currentId = v.get(i).getId();
				list.clear();
				list.add(v.get(i));
			} else {
				list.add(v.get(i));
			}
		}
		return null;
	}
	/** Cluster the nodes to size of threshold
	 * 
	 * @param list input list of nodes represented by their min-hash vector
	 * @param col the compared column number
	 * @param threshold the max size of a cluster
	 * @return a list of clusters
	 */
	private ArrayList<ArrayList<Integer>> clusterFinder(ArrayList<SparseVector> list, int col, int threshold) {
		Map<Float, ArrayList<Integer>> clusters = new HashMap<Float, ArrayList<Integer>>();
		// Map to a hash map all the sparse vectors with same value in column col
		for (SparseVector s : list) {
			Float value = s.getValue(col);
			int id = s.getId();
			if (clusters.containsKey(value)) {
				clusters.get(value).add(id);
			} else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(id);
				clusters.put(value, temp);
			}
		}
		
		// Output the result or cluster on the next column
		ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> result : clusters.values()) {
			if (result.size() > threshold) {
				ArrayList<SparseVector> temp = new ArrayList<SparseVector>();
				for (Integer i : result) {
					temp.add(data.get(i));
				}
				results.addAll(clusterFinder(temp, col + 1, threshold));
			} else {
				results.add(result);
			}
		}
		return results;
	}
}
