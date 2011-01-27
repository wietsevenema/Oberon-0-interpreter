package eu.wietsevenema.lang.oberon;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.AdditiveExpression;

public class RemoveGenerics extends Visitor {

	/** Print the specified generic node. */
	public GNode visit(GNode n) {
		for (int i = 0; i < n.size(); i++) {
			Object o = n.get(i);
			if (o instanceof Node) {
				n.set(i, dispatch((Node) o));
			}
		}
		return n;
	}

	public AdditiveExpression visitAdditiveExpression(GNode n) {
		AdditiveExpression ae = new AdditiveExpression();
		ae.add(n.getNode(0)); // lhs
		ae.add(n.getNode(2)); // rhs
		return ae;
	}

	public Node visit(Node n) {
		return n;
	}
}