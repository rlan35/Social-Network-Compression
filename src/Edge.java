

public class Edge {
	int nodeId1 = -1;
	int nodeId2 = -1;
	int id = -1;
	
	public Edge() {
		
	}
	
	public int getOtherNodeId(int id) {
		assert (id == nodeId1 || id == nodeId2) : "Invalid Node Id";
		if (id == nodeId1) {
			return nodeId2;
		} else {
			return nodeId1;
		}
	}
}
