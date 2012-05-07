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

	// re-mine one cluster to which a new node has just been added
	static void redo(Graph g, ArrayList<Integer> cluster) {
		for (int i=0; i < cluster.size(); i++) {
			Node n = g.nodes.get(cluster.get(i));
			if (n.isVirtual) {
				// disconnect source nodes and the virtual node
				for (Integer sourceID : ((VirtualNode) n).pointedByNodes) {
					for (int j=0; j < g.nodes.get(sourceID).edgeIds.size(); j++) {
						Integer outEdge = g.nodes.get(sourceID).edgeIds.get(j);
						if (g.edges.get(outEdge).getOtherNodeId(sourceID) == n.id) {
							g.edges.remove(outEdge);
							g.nodes.get(sourceID).edgeIds.remove(j);
							break;
						}
					}
				}

				// connect source nodes to target nodes
				for (Integer outEdge : n.edgeIds) {
					Integer targetID = g.edges.get(outEdge).getOtherNodeId(n.id);
					for (Integer sourceID : ((VirtualNode) n).pointedByNodes) {
						Edge e = null;
						for(int j=1; e==null; j++) {
							if(g.edges.get(g.edges.size() + j) == null) {
								e = new Edge(sourceID, targetID, g.edges.size() + j);
							}
						}
						g.edges.put(e.id, e);
						g.nodes.get(sourceID).edgeIds.add(e.id);
					}
				}

				// disconnect virtual node and target nodes
				for (Integer outEdge : n.edgeIds) {
					g.edges.remove(outEdge);
				}

				// remove the virtual node from the graph
				g.nodes.remove(cluster.get(i));
				cluster.remove(i--);
			}
		}
		
		// re-mine this cluster; following can commented out for debugging
		mineOneCluster(g, cluster);
	}

	static void doMining(Graph g, ArrayList<ArrayList<Integer>> sets) {
		for (ArrayList<Integer> W : sets) {
			mineOneCluster(g, W);
		}
	}

	static void mineOneCluster(Graph g, ArrayList<Integer> W) {
		counter.clear();

		// lines 1-5
		for (Integer v : W) {
			Node n = g.nodes.get(v);
			//System.out.println("now processing node id: " + v);
			//System.out.println("size is " + n.edgeIds.size());
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
			VirtualNode v = null;
			for(int i=1; v==null; i++) {
				if(g.nodes.get(g.nodes.size() + i) == null) {
					v = new VirtualNode(g.nodes.size() + i);
					W.add(v.id);
				}
			}
			// connect VN to targets
			for (Integer outlink : p.outlinkList) {
				Edge e = null;
				for(int i=1; e==null; i++) {
					if(g.edges.get(g.edges.size() + i) == null) {
						e = new Edge(v.id, outlink, g.edges.size() + i);
					}
				}
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
						if(targetID.equals(target)) {
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
				Edge e = null;
				for(int i=1; e==null; i++) {
					if(g.edges.get(g.edges.size() + i) == null) {
						e = new Edge(source, v.id, g.edges.size() + i);
					}
				}
				g.edges.put(e.id, e);				
				n.edgeIds.add(e.id);
				v.pointedByNodes.add(source);
			}
			
			// recompute the list of potential VNs
			Pattern.recompute(P);
			Collections.sort(P, new MiningPhase.MyPatternComparator()); // what if P is empty!?
		}
	}
}
