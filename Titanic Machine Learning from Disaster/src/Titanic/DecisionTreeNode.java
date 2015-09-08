package Titanic;
import java.util.*;

public class DecisionTreeNode {
	String label;

	HashMap<String, DecisionTreeNode> sons = new HashMap<String, DecisionTreeNode>();

	DecisionTreeNode() {

	}

	DecisionTreeNode(String label) {
		this.label = label;
	}

	boolean isLeaf() {
		return sons.size() == 0;
	}

	void add(String featureVal, DecisionTreeNode node) {
		sons.put(featureVal, node);
	}
}
