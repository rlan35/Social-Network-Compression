import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class ClusteringPhase {
	private static final int MaxCoef = 1000;
	private static int k;
	private ArrayList<Integer> hashValues;
	private ArrayList<ArrayList<Integer>> clusters;
	Map<Integer, SparseVector> vectorsMap;
	private double hashValue(int id, int coef)	{
		double hashValue = (MaxCoef - coef) * id;
		return hashValue;
	}
	
	public ArrayList<Integer> assignCluster(Graph g, int nodeId) {
		Map<Integer, Edge> edges = g.edges;
		Map<Integer, Node> nodes = g.nodes;
		Node newNode = g.nodes.get(nodeId);
		float[] vector= new float[k];
		
		//Compute the min-hash vector for each node
		
		for (int i = 0; i < k; i++) {
			int randomValue = hashValues.get(i);
			double minHash = Double.MAX_VALUE;
			Node n = newNode;
			for (int j = 0; j < n.edgeIds.size(); j++) {
				double hashValue = hashValue(edges.get(n.edgeIds.get(j)).getOtherNodeId(n.id), randomValue);

				if (hashValue < minHash) {
					minHash = hashValue;
				}
			}
			vector[i] = (float) minHash;
		}
		SparseVector s = new SparseVector(vector, newNode.id);
				
		//for (Float f : s.getValues())
			//System.out.print(" " + f);
		ArrayList<Integer>	cluster;
		int maxClusterIndex = -1;
		float maxWeight = 0;
	
		for (int i = 0; i < clusters.size(); i++) {
			cluster = clusters.get(i);
			float weight = 0;
			for (int j = 0; j < cluster.size(); j++) {
				//SparseVector curr = vectorsMap.get(cluster.get(j));
				Node curr = g.nodes.get(cluster.get(j));
				int count = 0;
				for (Integer k : newNode.otherNodeIds.keySet()) {
					if (curr.otherNodeIds.containsKey(k))
						count++;
				}
				weight += count;
				
			}
			//System.out.println(weight);
			if (maxWeight < weight) {
				maxWeight = weight;
				maxClusterIndex = i;
			}
		}
		if (maxClusterIndex != -1) {
			clusters.get(maxClusterIndex).add(newNode.id);
			return clusters.get(maxClusterIndex);
		} else {
			ArrayList<Integer> newCluster = new ArrayList<Integer>();
			newCluster.add(newNode.id);
			clusters.add(newCluster);
			return newCluster;
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> doCluster(Graph g, int k) {
		this.k = k;
		Map<Integer, Edge> edges = g.edges;
		Map<Integer, Node> nodes = g.nodes;
		int threshold = (int) Math.sqrt(nodes.size())/2;
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
		hashValues = new ArrayList<Integer>();
		for (int i = 0; i < k; i++) {
			int randomValue = r.nextInt(MaxCoef);
			hashValues.add(randomValue);
			for (Entry<Integer, Node> e : nodes.entrySet()) {
				double minHash = Double.MAX_VALUE;
				Node n = e.getValue();
				for (int j = 0; j < n.edgeIds.size(); j++) {
					double hashValue = hashValue(edges.get(n.edgeIds.get(j)).getOtherNodeId(n.id), randomValue);
					if (hashValue < minHash) {
						minHash = hashValue;
					}
				}
				vectors.get(n.id)[i] = (float) minHash;
			}
		}
		
		//Generate the min-hash matrix
		vectorsMap = new HashMap<Integer, SparseVector>();
		for (Entry<Integer, float[]> e : vectors.entrySet()) {
			//System.out.println(e.getValue()[0]);
			SparseVector s = new SparseVector(e.getValue(), (int) e.getKey());
			matrix.addRow(s);
			vectorsMap.put(e.getKey(), s);
		}
		
		Collections.sort(matrix.rows);
		
//		for (SparseVector s : matrix.rows) {
//			System.out.println(s.getValue(0));
//		}
		ArrayList<SparseVector> v = matrix.rows;
		
		ArrayList<SparseVector> list = new ArrayList<SparseVector>();
		//
		ArrayList<ArrayList<Integer>> returnResults = new ArrayList<ArrayList<Integer>>();
		//
		float currentValue = -1;
		for (int i = 0; i < v.size(); i++) {
			
			if (currentValue != v.get(i).getValues()[0]) {
				if (!list.isEmpty()) {
					ArrayList<ArrayList<Integer>> results;
					if (list.size() > threshold) {
						results = clusterFinder(list, 1, threshold, k);
					} else {
						results = new ArrayList<ArrayList<Integer>>();
						ArrayList<Integer> tempList = new ArrayList<Integer>();
						for (SparseVector s : list) {
							tempList.add(s.getId());
						}
						results.add(tempList);
					}
					returnResults.addAll(results);
					// Mining Phase here; Input results - clusters of ids of nodes
				} 
				currentValue = v.get(i).getValues()[0];
				list.clear();
				list.add(v.get(i));
			} else {
				list.add(v.get(i));
			}
		}
		clusters = new ArrayList<ArrayList<Integer>>();
		clusters.addAll(returnResults);
		
		return returnResults;
	}
	/** Cluster the nodes to size of threshold
	 * 
	 * @param list input list of nodes represented by their min-hash vector
	 * @param col the compared column number
	 * @param threshold the max size of a cluster
	 * @return a list of clusters
	 */
	private ArrayList<ArrayList<Integer>> clusterFinder(ArrayList<SparseVector> list, int col, int threshold, int k) {
		Map<Float, ArrayList<Integer>> clusters = new HashMap<Float, ArrayList<Integer>>();
		// Map to a hash map all the sparse vectors with same value in column col
		for (SparseVector s : list) {
			Float value = s.getValues()[col];
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
					temp.add(vectorsMap.get(i));
				}
				if (col + 1 < k)
					results.addAll(clusterFinder(temp, col + 1, threshold, k));
				else 
					results.add(result);
			} else {
				results.add(result);
			}
		}
		return results;
	}
}
