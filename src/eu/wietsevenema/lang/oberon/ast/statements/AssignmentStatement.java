package eu.wietsevenema.lang.oberon.ast.statements;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.visitors.TransformGenerics;


public class AssignmentStatement extends Statement {

	Identifier identifier;
	Expression expression;

	public Identifier getIdentifier() {
		return identifier;
	}

	public Expression getExpression() {
		return expression;
	}

	public AssignmentStatement(Identifier id, Expression expression){
		this.identifier = id;
		this.expression = expression;
	}

	public AssignmentStatement(Identifier id, Node exp) {
		TransformGenerics tg = new TransformGenerics();
		Expression expression = (Expression) tg.dispatch(exp);
		
		this.identifier = id;
		this.expression = expression;
	}

}
