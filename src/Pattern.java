import java.util.AbstractSet;
import java.util.ArrayList;


public class Pattern {
	int length;
	ArrayList<Integer> vertexList;
	ArrayList<Integer> outlinkList; // target nodes ID

	Pattern() {
		outlinkList = new ArrayList<Integer>(); // maintain inverse order
		vertexList = new ArrayList<Integer>();
	}
	
	Pattern(AbstractSet<Integer> ls, int leng) {
		outlinkList = new ArrayList<Integer>();
		vertexList = new ArrayList<Integer>();
		for(int x: ls) {
			vertexList.add(x);
		}
		length = leng;
	}
	
	int getFrequency() {
		return vertexList.size();
	}
	
	int getSavings() {
		return (length-1) * (getFrequency()-1) - 1;
	}
	
	// assuming patterns are sorted according to savings
	static void recompute(ArrayList<Pattern> patterns) {
		ArrayList<Integer> vertexList = patterns.get(0).vertexList;
		for (int i = 1; i < patterns.size(); i++) {
			Pattern p = patterns.get(i);
			for (int j = 0; j < vertexList.size() && p.vertexList.size() > 0; j++) { // Integer v : vertexList
				p.vertexList.remove(vertexList.get(j));
			}
			if (p.vertexList.size() == 0 || p.getSavings() <= 0) {
				patterns.remove(i);
				i--;
			}
		}
		patterns.remove(0); // removing the pattern(processed) with most savings
	}
}
