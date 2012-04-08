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

	static void doMining(Graph g, ArrayList<ArrayList<Integer>> sets) {
		for (ArrayList<Integer> W : sets) {
			System.out.println("size of the current set is " + W.size());
			counter.clear();

			// lines 1-5
			for (Integer v : W) {
				Node n = g.nodes.get(v);
				for (int i = 0; i < n.edgeIds.size(); i++) {
					Integer eid = n.edgeIds.get(i);
					Integer targetID = g.edges.get(eid).getOtherNodeId(v);
					Object obj = counter.get(targetID);
					if (obj == null) {
						counter.put(targetID, new Integer(1));
					} else {
						int tmp = ((Integer) obj).intValue() + 1;
						counter.put(targetID, new Integer(tmp));
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
					Integer targetID = g.edges.get(eid).getOtherNodeId(v);
					if(counter.get(targetID) > 1) {
						L.add(targetID);
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
			while (P.size() > 0) {
				Pattern p = P.get(0); // the pattern with most savings
				VirtualNode v = new VirtualNode(g.nodes.size()+1);
				// connect VN to targets
				for (Integer outlink : p.outlinkList) {
					Edge e = new Edge(v.id, outlink, g.edges.size()+1);
					g.edges.put(e.id, e);
					v.edgeIds.add(e.id);
				}
				g.nodes.put(v.id, v);

				// disconnect sources and targets
				for (Integer outlink : v.edgeIds) {
					Integer target = g.edges.get(outlink).getOtherNodeId(v.id);
					for (Integer source : p.vertexList) {
						Node n = g.nodes.get(source);
						for (int i = 0; i < n.edgeIds.size(); i++) {
							Integer eid = n.edgeIds.get(i);
							Integer targetID = g.edges.get(eid).getOtherNodeId(source); //edgeId2!?
							// check if source and target are connected
							if(targetID == target) {
								n.edgeIds.remove(i);
								g.edges.remove(eid);
								break;
							}
						}
					}
				}

				// connect sources to VN
				for (Integer source : p.vertexList) {
					Node n = g.nodes.get(source);
					Edge e = new Edge(source, v.id, g.edges.size()+1);
					g.edges.put(e.id, e);				
					n.edgeIds.add(e.id);
				}
				
				// recompute the list of potential VNs
				Pattern.recompute(P);
				Collections.sort(P, new MiningPhase.MyPatternComparator()); // what if P is empty!?
			}
		}
	}
}
