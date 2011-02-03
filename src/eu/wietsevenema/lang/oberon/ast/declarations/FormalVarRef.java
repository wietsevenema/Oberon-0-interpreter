package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.ValueReference;

public class FormalVarRef extends FormalVar {

	public FormalVarRef(Identifier identifier, VarType type) {
		super(identifier, type);
	}
	
	public void assignParameter(SymbolTable symbolTable, Expression param) throws TypeMismatchException, IdentifierExpectedInParamList {
		// 1. Parameter should be variable,
		if (!(param instanceof Identifier)) {
			throw new IdentifierExpectedInParamList("Param should be variable, not expression");
		}
		
		// 2. Get reference from parameter symbol (from parent scope)
		ValueReference reference = symbolTable.
				lookupValueReference(((Identifier) param).getName());
		
		// 3. Check type.
		String type = this.getType().toString();
		if(!reference.getValue().getType().equals(type)){
			throw new TypeMismatchException();
		}
		
		// 4. And assign in local scope with symbol defined in formal.
		symbolTable.declareValueReference(this.getIdentifier()
				.getName(), reference);
	}

}
