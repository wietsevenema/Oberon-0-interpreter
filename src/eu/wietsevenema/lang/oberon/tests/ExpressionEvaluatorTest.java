package eu.wietsevenema.lang.oberon.tests;


import junit.framework.TestCase;

import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.expressions.AdditiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.MultiplicativeExpression;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
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
		Expression div = 
			new MultiplicativeExpression(
				new IntegerConstant(6),
				new IntegerConstant(4),
				"DIV"
				);
		
		Expression mod = 
			new MultiplicativeExpression(
				new IntegerConstant(6),
				new IntegerConstant(4),
				"MOD"
				);
		
		
		ExpressionEvaluator evaluator = new ExpressionEvaluator(new SymbolTable());
		IntegerValue divResult = (IntegerValue) evaluator.dispatch(div);
		IntegerValue modResult = (IntegerValue) evaluator.dispatch(mod);
		
		/*
		 * 6 DIV 4 == 1
		 * 6 MOD 4 == 2
		 */
		assertEquals(new Integer(1), divResult.getValue());
		assertEquals(new Integer(2), modResult.getValue());
	}

	@Test
	public void testSimpleIdentifier() throws VariableNotDeclaredException, TypeMismatchException, VariableAlreadyDeclaredException{
		SymbolTable st = new SymbolTable();
		st.getCurrent().defineType("a", new IntegerType());
		st.getCurrent().defineValue("a", new IntegerValue(40));
		
		Expression exp = new AdditiveExpression(new IntegerConstant(2), new Identifier("a"), "+");
		
		ExpressionEvaluator evaluator = new ExpressionEvaluator(st);
		IntegerValue result = (IntegerValue) evaluator.dispatch(exp);
		assertEquals(new Integer(42), result.getValue());
	}
	
	
	
}
