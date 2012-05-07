import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Node {
	ArrayList<Integer> edgeIds;
	int clusterIndex = -1;
	int id = -1;
	int degree = 0;
	Map<Integer, Integer> otherNodeIds;
	boolean isVirtual = false;
	
	public Node(int id) {
		this.id = id;
		edgeIds = new ArrayList<Integer>();
		otherNodeIds = new HashMap<Integer, Integer>();
	}
	
	public void addEdge(int edgeId, int otherNodeId) {
		edgeIds.add(edgeId);
		otherNodeIds.put(otherNodeId, 0);
	}
	
	public boolean containsEdge(int edgeId) {
		return edgeIds.contains(edgeId);
	}
	
	public boolean connected(int nodeId) {
		return otherNodeIds.containsKey(nodeId);
	}
}

