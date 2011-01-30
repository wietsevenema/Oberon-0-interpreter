package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class StatementEvaluator extends Visitor {

	SymbolTable symbolTable;
	
	public StatementEvaluator(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	
	public void visit(AssignmentStatement assign) throws VariableNotDeclaredException, TypeMismatchException{
		ExpressionEvaluator eval = new ExpressionEvaluator(symbolTable);
		Value value = (Value)eval.dispatch(assign.getExpression());
		symbolTable.declareValue(
						assign.getIdentifier().getName(),
						value
						);	
	}
	
}
