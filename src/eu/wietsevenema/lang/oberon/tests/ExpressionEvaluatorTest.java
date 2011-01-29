package eu.wietsevenema.lang.oberon.tests;


import junit.framework.TestCase;

import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.expressions.AdditiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.IntegerValue;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;


public class ExpressionEvaluatorTest extends TestCase {

	
	@Test
	public void testSimpleAddition(){
		Expression exp = new AdditiveExpression(new IntegerConstant(1), new IntegerConstant(3), "+");
		ExpressionEvaluator evaluator = new ExpressionEvaluator(new SymbolTable());
		IntegerValue result = (IntegerValue) evaluator.dispatch(exp);
		assertEquals(new Integer(4), result.getValue());
	}
	
	@Test
	public void testNestedAddition(){
		Expression exp = 
			new AdditiveExpression(
				new IntegerConstant(1), 
				new AdditiveExpression(
					new IntegerConstant(10),
					new IntegerConstant(12),
					"-"
				),
				"+");
		ExpressionEvaluator evaluator = new ExpressionEvaluator(new SymbolTable());
		IntegerValue result = (IntegerValue) evaluator.dispatch(exp);
		assertEquals(new Integer(1+(10-12)), result.getValue());
	}
	
	
	@Test
	public void testDivisionModulus(){
		fail("Not implemented");
	}

	@Test
	public void testSimpleIdentifier() throws VariableNotDeclaredException, TypeMismatchException{
		SymbolTable st = new SymbolTable();
		st.getCurrent().defineType("a", new IntegerType());
		st.getCurrent().defineValue("a", new IntegerValue(40));
		
		Expression exp = new AdditiveExpression(new IntegerConstant(2), new Identifier("a"), "+");
		
		ExpressionEvaluator evaluator = new ExpressionEvaluator(st);
		IntegerValue result = (IntegerValue) evaluator.dispatch(exp);
		assertEquals(new Integer(42), result.getValue());
	}
	
}
