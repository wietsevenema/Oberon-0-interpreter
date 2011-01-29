package eu.wietsevenema.lang.oberon.tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.expressions.BooleanConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.types.BooleanType;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.visitors.StatementEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.BooleanValue;
import eu.wietsevenema.lang.oberon.interpreter.IntegerValue;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;

public class StatementEvaluatorTest extends TestCase {

	private SymbolTable symbols;

	@Before
	public void setUp() {
		this.symbols = new SymbolTable();
	}

	@Test
	public void testAssignment() throws VariableAlreadyDeclaredException {
		symbols.defineType("a", new IntegerType());
		AssignmentStatement as1 = new AssignmentStatement(
				new Identifier("a"),
				new IntegerConstant(2));

		symbols.defineType("b", new BooleanType());
		AssignmentStatement as2 = new AssignmentStatement(
				new Identifier("b"),
				new BooleanConstant(true));

		StatementEvaluator se = new StatementEvaluator(symbols);
		se.dispatch(as1);
		se.dispatch(as2);
		
		assertEquals(((IntegerValue)symbols.lookupValue("a")).getValue(), new Integer(2));
		assertEquals(((BooleanValue)symbols.lookupValue("b")).getValue(), new Boolean(true));
	}

	

}
