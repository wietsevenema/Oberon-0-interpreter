package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.Formal;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class FormalVar extends Declaration implements Formal {

	private Identifier identifier;
	private VarType type;
		
	public FormalVar(Identifier identifier, VarType type){
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	public VarType getType() {
		return type;
	}

	@Override
	public void assignParameter(SymbolTable symbolTable, Expression param) throws TypeMismatchException, IdentifierExpectedInParamList, VariableAlreadyDeclaredException {
		// This is a value parameter.
		// 1. Parameter is expression, evaluate
		ExpressionEvaluator expressionEval = new ExpressionEvaluator(
				symbolTable);
		Value<?> result = (Value<?>) expressionEval.dispatch(param);
		
		// 2. Check type.
		String type = this.getType().toString();
		if(!result.getType().equals(type)){
			throw new TypeMismatchException();
		}
		
		// 3. Declare variable and assign in local scope.
		//    With symbol defined in formal. 
		String symbol = this.getIdentifier().getName();
		symbolTable.declareValue(symbol, result);
		
	}
	
	
}
