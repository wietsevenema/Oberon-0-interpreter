package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionPrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;
import eu.wietsevenema.lang.oberon.parser.Oberon;

public class ParseExpressionTest {
	
	private String getAbsFilename( String relName){
		return getClass().getResource(relName).getPath();
	}

	private Node parseFile( String absPath ) throws IOException, InvalidInputException, ParseException{
		Reader in = null;
		in = new BufferedReader(new FileReader(new File(absPath)));
		
		Oberon p = new Oberon(in, absPath, (int) new File(absPath).length());
		Result r = p.pExpression(0);
		
		
		Node result; 
		if (r.hasValue()) {
			SemanticValue v = (SemanticValue) r;
			if (v.value instanceof Node) {
				result = (Node) v.value;
			} else {
				throw new InvalidInputException();
			}

		} else {
			ParseError err = (ParseError) r;
			if (-1 == err.index) {
				throw new ParseException("  Parse error");
			} else {
				throw new ParseException("  " + p.location(err.index) + ": "
						+ err.msg);
			}
		}
		in.close();
		return result;
	}
	
	@Before 
	public void setUp() throws Exception {
				
	}

	@Test
	public void testAllOperatorsGetParsed() throws IOException, InvalidInputException, ParseException{
		parseFile(getAbsFilename("oberon/expr/allops.expr"));
	}

	@Test
	public void testAdditionOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/additionopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2+3)-1)+41)", actual);
	}
	
	@Test
	public void testEqualityOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/equalityopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((((true=false)#true)<2)<=3)>23)>=12)", actual);
	}
	
	@Test
	public void testMultiplicationOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/multiplicationopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2*3)DIV666)MOD12)", actual);
	}
	
	@Test
	public void testParenthesisedOpsBind() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/parenthesisedopsbind.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((2+1)-4)<false)>=3)", actual);
	}
	
	@Test
	public void testPrecedenceOrder() throws IOException, InvalidInputException, ParseException{
		//1 # 1 + 1 DIV 1 & 1 OR 1 ~ 1
		Node result = parseFile(getAbsFilename("oberon/expr/precedence.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(1#(1+(1DIV(1&(1OR(~1))))))", actual);
	}
	
	@Test
	public void testUnaryMinOnlyFirst(){
		// -2 * 2 mag, maar 2 * -2 moet een parse-error geven...
		fail("Not implemented");
	}
	
	

}
