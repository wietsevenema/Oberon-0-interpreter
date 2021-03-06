package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.ast.visitors.interpreter.ValueReferenceResolver;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.SymbolAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.InterpreterScope;
import eu.wietsevenema.lang.oberon.interpreter.ValueReference;

public class FormalVarRef extends FormalVar {

	public FormalVarRef(Identifier identifier, VarType type) {
		super(identifier, type);
	}

	public void assignParameter(InterpreterScope scope, Expression param) throws TypeMismatchException,
			IdentifierExpectedInParamList, SymbolAlreadyDeclaredException {
		// 1. Get reference from parameter symbol (from parent scope)
		// 2. Assign in local scope with symbol defined in formal.

		// Note; I do runtime type checking. That is, when I assign a
		// value, i check if the type of the new value matches that of the old
		// value.

		ValueReferenceResolver resolv = new ValueReferenceResolver(scope);
		ValueReference reference = (ValueReference) resolv.dispatch(param);

		String symbol = this.getIdentifier().getName();
		scope.declareValueReference(symbol, reference);
	}

}
