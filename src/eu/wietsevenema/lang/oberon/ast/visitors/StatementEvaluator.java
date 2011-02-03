package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.Iterator;
import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.ProcedureUndefinedException;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ElseIfStatement;
import eu.wietsevenema.lang.oberon.ast.statements.IfStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureCallStatement;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.ast.statements.WhileStatement;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.WrongNumberOfArgsException;
import eu.wietsevenema.lang.oberon.interpreter.Formal;
import eu.wietsevenema.lang.oberon.interpreter.Procedure;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;
import eu.wietsevenema.lang.oberon.interpreter.ValueReference;

public class StatementEvaluator extends Visitor {

	SymbolTable symbolTable;

	public StatementEvaluator(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public void visit(AssignmentStatement assign)
			throws VariableNotDeclaredException, TypeMismatchException {
		// 1. Retrieve existing reference.
		String symbol = assign.getIdentifier().getName();
		ValueReference currentValRef = symbolTable.lookupValueReference(symbol);

		if (currentValRef == null) {
			throw new VariableNotDeclaredException("Variable '" + symbol
					+ "' not declared. " + assign.getLocation().toString());
		}

		Value<?> currentVal = currentValRef.getValue();

		// 2. Evaluate expression
		ExpressionEvaluator eval = new ExpressionEvaluator(symbolTable);
		Value<?> value = (Value<?>) eval.dispatch(assign.getExpression());

		// 3. Assign new value
		if (currentVal.matchesType(value)) {
			currentValRef.setValue(value);
		} else {
			throw new TypeMismatchException();
		}

	}

	public void visit(ProcedureCallStatement pCall)
			throws WrongNumberOfArgsException, IdentifierExpectedInParamList,
			VariableAlreadyDeclaredException, VariableNotDeclaredException,
			TypeMismatchException, ProcedureUndefinedException {

		// Find procedure node.
		Procedure procedure = (Procedure) symbolTable.lookupProc(pCall
				.getIdentifier().getName());
		
		if(procedure == null){
			throw new ProcedureUndefinedException("Procedure "+ pCall.getIdentifier().getName() + " undefined.");
		}
		
		
		// Enter scope.
		symbolTable.enter();
		
		List<Expression> parameters = pCall.getParameters();
		List<Formal> formals = procedure.getFormals();
		
		if (formals.size() != parameters.size()) {
			throw new WrongNumberOfArgsException();
		}
		for (int i = 0; i < formals.size(); i++) {
			Formal formal = formals.get(i);
			formal.assignParameter( symbolTable, parameters.get(i) );
		}

		procedure.execute(symbolTable);
		
		// Exit scope.
		symbolTable.exit();

	}

	
	public void visit(IfStatement ifStatement) throws TypeMismatchException, ValueUndefinedException{
		
		if(evalCondition(ifStatement.getCondition())){
			//1. Als de if matched, evalueren we die statement en stoppen de evaluatie. 
			visitStatements(ifStatement.getTrueStatements());
			return; 
		} else {
			for (Iterator<ElseIfStatement> iterator = ifStatement.getElseIfs().iterator(); 
				 iterator.hasNext();) {
				ElseIfStatement elseif = (ElseIfStatement) iterator.next();
				
				if(evalCondition(elseif.getCondition())){
					//2. Zodra er een elseif is gematched en uitgevoerd stopt de evaluatie.
					visitStatements(elseif.getTrueStatements());
					return;  
				}
			}
			
			//3. Finally, als zowel het if statement en de elseif's falen te matchen, voeren we de else uit. 
			visitStatements(ifStatement.getFalseStatements());
			return;
			
		}
		
		
	}
	
	private void visitStatements( List<Statement> statements){
		if(!statements.isEmpty()){
			StatementEvaluator statEval = new StatementEvaluator(symbolTable);
			for (Statement stat : statements) {
				statEval.dispatch(stat);
			}
		}
	}
	
	private boolean evalCondition(Expression exp) throws TypeMismatchException, ValueUndefinedException{
		ExpressionEvaluator expEval = new ExpressionEvaluator(symbolTable);
		Value<?> result = (Value<?>) expEval.dispatch(exp);
		if(result.getType() != "BOOLEAN"){
			throw new TypeMismatchException("Boolean condition expected");
		} else {
			return (Boolean) result.getValue();
		}
	}
	
	
	public void visit(WhileStatement whilestat) throws TypeMismatchException, ValueUndefinedException{
		StatementEvaluator statEval = new StatementEvaluator(symbolTable);
		
		while(evalCondition(whilestat.getCondition())){
			for( Statement s: whilestat.getStatements()){
				statEval.dispatch(s);
			}
		}
		
	}
	
}
