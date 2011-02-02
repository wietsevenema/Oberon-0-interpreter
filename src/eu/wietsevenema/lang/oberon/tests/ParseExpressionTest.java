package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ModulePrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;

public class ParseExpressionTest {
	
	public String getAbsFilename(String relName) {
		return getClass().getResource(relName).getPath();
	}
	
	@Test
	public void testAllOperatorsGetParsed() throws IOException, InvalidInputException, ParseException{
		Util.parseExpressionFile(getAbsFilename("oberon/expr/allops.expr"));
	}

	@Test
	public void testAdditionOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseExpressionFile(getAbsFilename("oberon/expr/additionopsleftassoc.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2+3)-1)+41)", actual);
	}
	
	@Test
	public void testEqualityOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseExpressionFile(getAbsFilename("oberon/expr/equalityopsleftassoc.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((((true=false)#true)<2)<=3)>23)>=12)", actual);
	}
	
	@Test
	public void testMultiplicationOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseExpressionFile(getAbsFilename("oberon/expr/multiplicationopsleftassoc.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2*3)DIV666)MOD12)", actual);
	}
	
	@Test
	public void testParenthesisedOpsBind() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseExpressionFile(getAbsFilename("oberon/expr/parenthesisedopsbind.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((2+1)-4)<false)>=3)", actual);
	}
	
	@Test
	public void testPrecedenceOrder() throws IOException, InvalidInputException, ParseException{
		//1 # 1 + 1 DIV 1 & 1 OR 1 ~ 1
		Node result = Util.parseExpressionFile(getAbsFilename("oberon/expr/precedence.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(1#(1+(1DIV(1&(1OR(~1))))))", actual);
	}
	
	@Test
	public void testUnaryMin() throws IOException, InvalidInputException, ParseException{
		// -3 * 2 bind als -(3*2)!! 
		Node unaryMinLhs = Util.parseExpressionFile(getAbsFilename("oberon/expr/unarymin.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(unaryMinLhs);
		assertEquals("-(3*2)", actual);
	}
	
	
	@Test
	public void logicalConnectivesEvaluateLazy(){
		
	}

}
