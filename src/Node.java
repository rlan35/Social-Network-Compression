import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


public class Node {
	ArrayList<Integer> edgeIds;
	int clusterIndex = -1;
	int id = -1;
	int degree = 0;

	public Node() {
		edgeIds = new ArrayList<Integer>();
	}
}
