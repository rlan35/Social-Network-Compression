import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class MiningPhase {

	static Map<Integer, Integer> counter = new HashMap<Integer, Integer>();

	static class MyCountComparator implements Comparator<Integer> {
		public int compare(Integer x, Integer y) {
			int c1 = counter.get(x);
			int c2 = counter.get(y);

			if (c1 == c2)
				return 0;
			else if (c1 > c2)
				return -1;
			else
				return 1;
		}
	}
	
	static class MyPatternComparator implements Comparator<Pattern> {
		public int compare(Pattern x, Pattern y) {
			int s1 = x.getSavings();
			int s2 = y.getSavings();

			if (s1 == s2)
				return 0;
			else if (s1 > s2)
				return -1;
			else
				return 1;
		}
	}

	private ArrayList<ArrayList<Integer>> doMining(Graph g, ArrayList<ArrayList<Integer>> sets) {
		for (ArrayList<Integer> W : sets) {
			counter.clear();
			
			// lines 1-5
			for (Integer v : W) {
				Node n = g.nodes.get(v);
				for (int i = 0; i < n.edgeIds.size(); i++) {
					Integer eid = n.edgeIds.get(i);
					Object obj = counter.get(eid);
					if (obj == null) {
						counter.put(eid, new Integer(1));
					} else {
						int tmp = ((Integer) obj).intValue() + 1;
						counter.put(eid, new Integer(tmp));
					}
				}
			}

			// line 6 not necessary if using MyComparator
			// lines 7-17
			Trie T = new Trie();
			for (Integer v : W) {
				ArrayList<Integer> L = new ArrayList<Integer>();
				Node n = g.nodes.get(v);
				for (int i = 0; i < n.edgeIds.size(); i++) {
					Integer eid = n.edgeIds.get(i);
					if(counter.get(eid) > 1) {
						L.add(eid);
					}
				}
				Collections.sort(L, new MiningPhase.MyCountComparator());
				// add L to trie
				T.insert(L, v);
			}
			
			// lines 18-20
			ArrayList<Pattern> P = new ArrayList<Pattern>();
			T.findPatterns(P);
			Collections.sort(P, new MiningPhase.MyPatternComparator());
			
			// lines 21-30
			for (Pattern p : P) {
				VirtualNode v = new VirtualNode(g.nodes.size()+1);
			}
		}
	}
}
