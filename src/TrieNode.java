import java.util.LinkedList;
import java.util.TreeSet;

public class TrieNode {
	int label;
	TreeSet<Integer> content; // may be slow
	TrieNode parent;
	LinkedList<TrieNode> children;
	boolean visited;
	int depth;

	public TrieNode(int l){
		label = l;
		content = new TreeSet<Integer>();
		parent = null;
		children = new LinkedList<TrieNode>();
		visited = false;
		depth = 0;
	}

	public TrieNode subNode(int l){
		if(children!=null){
			for(TrieNode child:children){
				if(child.label == l){
					return child;
				}
			}
		}
		return null;
	}
}
