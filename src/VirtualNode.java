import java.util.ArrayList;


public class VirtualNode extends Node {
	ArrayList<Integer> pointedByNodes;
	
	public VirtualNode(int id) {
		super(id);
		isVirtual = true;
		pointedByNodes = new ArrayList<Integer>();
	}

}
