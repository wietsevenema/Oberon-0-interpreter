package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.ArraySelector;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.interpreter.Scope;
import eu.wietsevenema.lang.oberon.interpreter.ValueReference;
import eu.wietsevenema.lang.oberon.interpreter.values.ArrayValue;
import eu.wietsevenema.lang.oberon.interpreter.values.IntegerValue;

public class ValueReferenceResolver extends Visitor {

	private Scope scope;

	public ValueReferenceResolver(Scope scope) {
		this.scope = scope;
	}

	public ValueReference visit(Identifier id) {
		return scope.lookupValueReference(id.getName());
	}

	public ValueReference visit(ArraySelector selector) throws ValueUndefinedException {
		// 1. Get reference to left ArrayValue.
		ExpressionEvaluator exprEval = new ExpressionEvaluator(scope);
		ArrayValue left = (ArrayValue) exprEval.dispatch(selector.getLeft());

		// 2. Evaluate right side to get index
		IntegerValue index = (IntegerValue) exprEval.dispatch(selector.getIndex());

		return left.getReference(index.getValue());
	}
}
