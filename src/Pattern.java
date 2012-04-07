import java.util.AbstractSet;
import java.util.ArrayList;


public class Pattern {
	int length;
	ArrayList<Integer> vertexList;
	ArrayList<Integer> outlinkList;

	public Pattern() {
		outlinkList = new ArrayList<Integer>(); // maintain inverse order
		vertexList = new ArrayList<Integer>();
	}
	
	public Pattern(AbstractSet<Integer> ls, int leng) {
		outlinkList = new ArrayList<Integer>();
		vertexList = new ArrayList<Integer>();
		for(int x: ls) {
			vertexList.add(x);
		}
		length = leng;
	}
	
	public int getFrequency() {
		return vertexList.size();
	}
	
	public int getSavings() {
		return (length-1) * (getFrequency()-1) - 1;
	}
}
