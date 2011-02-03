package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.Iterator;
import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVar;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVarRef;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ElseIfStatement;
import eu.wietsevenema.lang.oberon.ast.statements.IfStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureCallStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureDecl;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.ast.statements.WhileStatement;
import eu.wietsevenema.lang.oberon.exceptions.IdentifierExpectedInParamList;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.WrongNumberOfArgsException;
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
					+ "' not declared.");
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
			TypeMismatchException {

		// Find procedure node.
		ProcedureDecl procedure = (ProcedureDecl) symbolTable.lookupProc(pCall
				.getIdentifier().getName());
		
		// Enter scope.
		symbolTable.enter();

		
		List<Expression> parameters = pCall.getParameters();
		List<FormalVar> formals = procedure.getFormals();
		
		
		if (formals.size() != parameters.size()) {
			throw new WrongNumberOfArgsException();
		}
		for (int i = 0; i < formals.size(); i++) {
			FormalVar formal = formals.get(i);
			if (formal instanceof FormalVarRef) {
				// Dit is een variable parameter.
				// 1. Parameter should be variable,
				Expression param = parameters.get(i);
				if (!(param instanceof Identifier)) {
					throw new IdentifierExpectedInParamList("Param " + (i + 1)
							+ " should be variable, not expression");
				}
				
				// 2. Get reference to value (from parent scope)
				ValueReference reference = symbolTable.
						lookupValueReference(((Identifier) param).getName());
				
				// 3. Check type.
				String type = formal.getType().toString();
				if(!reference.getValue().getType().equals(type)){
					throw new TypeMismatchException();
				}
								
				// 4. And assign in local scope with symbol defined in formal.
				symbolTable.declareValueReference(formal.getIdentifier()
						.getName(), reference);

			} else {
				// This is a value parameter.
				// 1. Parameter is expression, evaluate
				ExpressionEvaluator expressionEval = new ExpressionEvaluator(
						symbolTable);
				Expression param = parameters.get(i);
				Value<?> result = (Value<?>) expressionEval.dispatch(param);
				
				// 2. Check type.
				String type = formal.getType().toString();
				if(!result.getType().equals(type)){
					throw new TypeMismatchException();
				}
				
				// 3. Declare variable and assign in local scope.
				//    With symbol defined in formal. 
				String symbol = formal.getIdentifier().getName();
				symbolTable.declareValue(symbol, result);
			
			}

		}
		

		// Process declarations.
		DeclarationEvaluator declEval = new DeclarationEvaluator(symbolTable);
		declEval.dispatch(procedure.getDeclarations());

		// Execute statements.
		StatementEvaluator statEval = new StatementEvaluator(symbolTable);
		for( Statement s : procedure.getStatements()){
			statEval.dispatch(s);
		}
		
		
		// Exit scope.
		symbolTable.exit();

	}

	
	public void visit(IfStatement ifStatement) throws TypeMismatchException, ValueUndefinedException{
		
		if(evalCondition(ifStatement.getCondition())){
			//1. Als de if matched, evalueren we die statement en stoppen de evaluatie. 
			evalStatements(ifStatement.getTrueStatements());
			return; 
		} else {
			for (Iterator<ElseIfStatement> iterator = ifStatement.getElseIfs().iterator(); 
				 iterator.hasNext();) {
				ElseIfStatement elseif = (ElseIfStatement) iterator.next();
				
				if(evalCondition(elseif.getCondition())){
					//2. Zodra er een elseif is gematched en uitgevoerd stopt de evaluatie.
					evalStatements(elseif.getTrueStatements());
					return;  
				}
			}
			
			//3. Finally, als zowel het if statement en de elseif's falen te matchen, voeren we de else uit. 
			evalStatements(ifStatement.getFalseStatements());
			return;
			
		}
		
		
	}
	
	private void evalStatements( List<Statement> statements){
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
			throw new TypeMismatchException("While statement expects boolean condition");
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
