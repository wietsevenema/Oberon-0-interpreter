package eu.wietsevenema.lang.oberon.ast.types;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.exceptions.NotImplementedException;

public class ArrayType extends VarType {

	private Expression expression;
	private VarType type;

	public ArrayType(Expression exp, VarType type) {
		this.expression = exp;
		this.type = type;
	}

	public Expression getExpression() {
		return expression;
	}

	public VarType getType() {
		return type;
	}

	@Override
	public String toString() {
		throw new NotImplementedException();
	}

}
