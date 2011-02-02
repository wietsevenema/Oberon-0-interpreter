package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.expressions.AdditiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.BooleanConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.EqualityExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.ast.statements.WhileStatement;
import eu.wietsevenema.lang.oberon.ast.visitors.StatementEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class StatementEvaluatorTest  {

	private SymbolTable symbols;

	@Before
	public void setUp() {
		this.symbols = new SymbolTable();
	}

	
	@Test
	public void testAssignment() throws VariableAlreadyDeclaredException, ValueUndefinedException, VariableNotDeclaredException  {
		//Declare vars
		symbols.declareValue("a", Value.fromTypeName("INTEGER"));
		symbols.declareValue("b", Value.fromTypeName("BOOLEAN"));
		
		//Construct assignment statements
		AssignmentStatement as1 = new AssignmentStatement(
				new Identifier("a"),
				new IntegerConstant(2));
		
		AssignmentStatement as2 = new AssignmentStatement(
				new Identifier("b"),
				new BooleanConstant(true));

		StatementEvaluator se = new StatementEvaluator(symbols);
		se.dispatch(as1);
		se.dispatch(as2);
		
		assertEquals((symbols.lookupValue("a")).getValue(), new Integer(2));
		assertEquals((symbols.lookupValue("b")).getValue(), new Boolean(true));
	}
	
	
	@Test
	public void testSecondAssignment() throws ValueUndefinedException, VariableNotDeclaredException, VariableAlreadyDeclaredException {
		symbols.declareValue("a", Value.fromTypeName("INTEGER"));
		AssignmentStatement first = new AssignmentStatement(
				new Identifier("a"),
				new IntegerConstant(2));

		AssignmentStatement second = new AssignmentStatement(
				new Identifier("a"),
				new IntegerConstant(3));

		StatementEvaluator se = new StatementEvaluator(symbols);
		se.dispatch(first);
		assertEquals((symbols.lookupValue("a")).getValue(), new Integer(2));
		se.dispatch(second);
		assertEquals((symbols.lookupValue("a")).getValue(), new Integer(3));
	}
	
	@Test
	public void testWhileStatement() throws VariableAlreadyDeclaredException, ValueUndefinedException, VariableNotDeclaredException{
		
		Expression condition = new EqualityExpression(
						new Identifier("count"),
						new IntegerConstant(5),
						"<"
					);
		
		Statement stat1 = new AssignmentStatement(
							new Identifier("count"), 
							new AdditiveExpression(
									new Identifier("count"),
									new IntegerConstant(1),
									"+"
							)
							);
		
		Statement stat2 = new AssignmentStatement(
							new Identifier("touch"),
							new BooleanConstant(true)
						);
		
		ArrayList<Statement> statements = new ArrayList<Statement>();
		statements.add(stat1);
		statements.add(stat2);
		
		WhileStatement whilestat = new WhileStatement(condition, statements);
		
		symbols.declareValue("count", new Value<Integer>(0));
		symbols.declareValue("touch", new Value<Boolean>(false));
		
		StatementEvaluator eval = new StatementEvaluator(symbols);
		
		eval.dispatch(whilestat);		
		
		assertEquals(new Integer(5), 		symbols.lookupValue("count").getValue());
		assertEquals(new Boolean(true), 	symbols.lookupValue("touch").getValue());
		
	}
	
	
	
}
