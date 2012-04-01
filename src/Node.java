import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


public class Node {
	ArrayList<Integer> edgeIds;
	int clusterIndex = -1;
	int id = -1;
	int degree = 0;

	public Node(int id) {
		this.id = id;
		edgeIds = new ArrayList<Integer>();
	}
	
	public void addEdge(int edgeId) {
		edgeIds.add(edgeId);
	}
	
	public boolean containsEdge(int edgeId) {
		return edgeIds.contains(edgeId);
	}
}

