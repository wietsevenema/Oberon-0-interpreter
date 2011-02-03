package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.values.Value;

//FIXME ik ben een hack om procedure overloading mogelijk te maken bij builtins.
public class TypeLessFormal implements Formal {

	private Identifier identifier;

	public TypeLessFormal( Identifier identifier ){
		this.identifier = identifier;
	}
	
	public TypeLessFormal(String string) {
		this.identifier = new Identifier(string);
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	public void assignParameter(SymbolTable symbolTable, Expression param)
			throws TypeMismatchException, IdentifierExpectedInParamList,
			VariableAlreadyDeclaredException {
		
		ExpressionEvaluator expressionEval = new ExpressionEvaluator(symbolTable);
		Value result = (Value) expressionEval.dispatch(param);
		String symbol = this.getIdentifier().getName();
		symbolTable.declareValue(symbol, result);

	}

}
