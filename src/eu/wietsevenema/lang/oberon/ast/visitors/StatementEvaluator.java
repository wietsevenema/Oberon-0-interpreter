package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVar;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVarRef;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureCallStatement;
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
import eu.wietsevenema.lang.oberon.parser.ProcedureDecl;

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

	public void visit(ProcedureCallStatement pcs)
			throws WrongNumberOfArgsException, IdentifierExpectedInParamList,
			VariableAlreadyDeclaredException, VariableNotDeclaredException,
			TypeMismatchException {

		// Find procedure node.
		ProcedureDecl procDecl = (ProcedureDecl) symbolTable.lookupProc(pcs
				.getIdentifier().getName());

		// Enter scope.
		symbolTable.enter();

		// Assign actual parameters in new scope. Discriminate between var and
		// ref.
		List<Expression> parameters = pcs.getParameters();
		List<FormalVar> formals = procDecl.getFormals();

		if (formals.size() != parameters.size()) {
			throw new WrongNumberOfArgsException();
		}
		for (int i = 0; i < formals.size(); i++) {
			FormalVar formal = formals.get(i);
			if (formal instanceof FormalVarRef) {
				// This is een variable parameter.
				// 1. Parameter should be variable,
				Expression param = parameters.get(i);
				if (!(param instanceof Identifier)) {
					throw new IdentifierExpectedInParamList("Param " + (i + 1)
							+ " of " + procDecl.getIdentifier().getName()
							+ " should be variable, not expression");
				}
				// 2. get valueReference
				ValueReference reference = symbolTable
						.lookupValueReference(((Identifier) param).getName());

				// 3. And assign in local scope with symbol defined in formal.
				// FIXME zou eerst type moeten declareren in local scope!!??
				symbolTable.declareValueReference(formal.getIdentifier()
						.getName(), reference);

			} else {
				// This is a value parameter.
				// 1. Parameter is expression, evaluate
				ExpressionEvaluator expressionEval = new ExpressionEvaluator(
						symbolTable);
				Expression param = parameters.get(i);
				Value<?> result = (Value<?>) expressionEval.dispatch(param);

				// 2. Declare type and assign value in local scope
				// with symbol defined in formal.
				String symbol = formal.getIdentifier().getName();
				symbolTable.declareValue(symbol, result);

			}

		}

		// Process declarations.
		Declarations decls = procDecl.getDeclarations();
		DeclarationEvaluator declEval = new DeclarationEvaluator(symbolTable);
		declEval.dispatch(decls);

		// Execute statements.
		List<Statement> statements = procDecl.getStatements();
		StatementEvaluator statEval = new StatementEvaluator(symbolTable);
		for (Statement stat : statements) {
			statEval.dispatch(stat);
		}

		// Exit scope.
		symbolTable.exit();

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
