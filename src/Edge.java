

public class Edge {
	int nodeId1 = -1;
	int nodeId2 = -1;
	int id = -1;
	
	public Edge(int id1, int id2, int id) {
		this.nodeId1 = id1;
		this.nodeId2 = id2;
		this.id = id;
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
