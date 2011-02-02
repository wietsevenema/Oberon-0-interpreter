package eu.wietsevenema.lang.oberon.ast.statements;

import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.visitors.TransformGenerics;

import xtc.tree.Node;

public class WhileStatement extends Statement {

	private Expression condition;
	private List<Statement> statements;

	public WhileStatement(Node condition, List<Statement> statements) {
		TransformGenerics tg = new TransformGenerics();
		this.condition = (Expression)tg.dispatch(condition);
		this.statements = statements;
	}

	public Expression getCondition() {
		return condition;
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
