import java.util.ArrayList;

public class Trie {
	private TrieNode root;

	public Trie() {
		root = new TrieNode(0);
	}

	public void insert(ArrayList<Integer> list, int vid) {
		TrieNode current = root;
		for(int i=0;i<list.size();i++) {
			int label = list.get(i);
			TrieNode child = current.subNode(label);
			if(child == null) {
				current.children.add(new TrieNode(label));
				child = current.subNode(label);
				child.parent = current;
				child.depth = i + 1;
			}
			current = child;
			current.content.add(vid);
		}
	}

	public void findPatterns(ArrayList<Pattern> P) {
		// newcomers are the TrieNode added during tree traversal
		ArrayList<TrieNode>	newcomers = new ArrayList<TrieNode>();
		ArrayList<TrieNode> leaves = new ArrayList<TrieNode>();
		findLeaves(leaves, root);
		
		for(TrieNode leaf : leaves) {
			TrieNode dummy = leaf;
			Pattern vn = new Pattern(dummy.content, dummy.content.size());
			// traversal to the root (actually to the node with depth one)
			while(dummy.depth > 0) {
				vn.outlinkList.add(dummy.label);
				dummy.visited = true;
				// add parent to P if it has a longer vertex list
				// only add parent with depth > 1 (see ex on paper)
				if(dummy.parent.visited == false &&
						dummy.parent.depth > 1 &&
						dummy.parent.content.size() > dummy.content.size())
					newcomers.add(dummy);
				dummy = dummy.parent;
			}
			P.add(vn);
		}
		
		// filling out outlinkList for newcomers
		for(TrieNode nc : newcomers) {
			TrieNode dummy = nc;
			Pattern vn = new Pattern(dummy.content, dummy.content.size());
			while(dummy.depth > 0) {
				vn.outlinkList.add(dummy.label);
				dummy = dummy.parent;
			}
			P.add(vn);
		}
	}

	// recursive method that "returns" a list of leaves with a vertex list
	// length greater than one
	private void findLeaves(ArrayList<TrieNode> leaves, TrieNode node) {
		if (node.children.size() == 0) return;
		for(TrieNode child : node.children) {
			if(child.children.size() == 0 && child.content.size() > 1)
				leaves.add(child);
			else
				findLeaves(leaves, child);
		}
	}
}
