package eu.wietsevenema.lang.oberon.ast.statements;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.visitors.TransformGenerics;


public class AssignmentStatement extends Statement {

	Identifier id;
	Expression exp;

	public AssignmentStatement(Identifier id, Node exp) {
		this.id = id;
		TransformGenerics tg = new TransformGenerics();
		this.exp = (Expression) tg.dispatch(exp);
	}

}
