package eu.wietsevenema.lang.oberon.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ExpressionPrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;
import eu.wietsevenema.lang.oberon.parser.Oberon;

public class ParseExpressionTest extends TestCase {
	
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

	public void testAllOperatorsGetParsed() throws IOException, InvalidInputException, ParseException{
		parseFile(getAbsFilename("oberon/expr/allops.expr"));
	}
	
	public void testAdditionOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/additionopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2+3)-1)+41)", actual);
	}
	
	public void testEqualityOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/equalityopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((((true=false)#true)<2)<=3)>23)>=12)", actual);
	}
	
	public void testMultiplicationOpsLeftAssoc() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/multiplicationopsleftassoc.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("(((2*3)DIV666)MOD12)", actual);
	}
	
	public void testParenthesisedOpsBind() throws IOException, InvalidInputException, ParseException{
		Node result = parseFile(getAbsFilename("oberon/expr/parenthesisedopsbind.expr"));
		ExpressionPrinter printer = new ExpressionPrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("((((2+1)-4)<false)>=3)", actual);
	}
	
	public void testNegationStrongerThanUnaryMin(){
		fail("Not implemented");
	}
	
	public void testUnaryMinStrongerThanDisjunction(){
		fail("Not implemented");
	}

	public void testDisjunctionStrongerThanConjunction(){
		fail("Not implemented");
	}

	public void testConjunctionStrongerThanAddition(){
		fail("Not implemented");
	}

	public void testAdditionStrongerThanEquality(){
		fail("Not implemented");
	}
	

	public void testUnaryMinOnlyFirst(){
		// -2 * 2 mag, maar 2 * -2 moet een parse-error geven...
		fail("Not implemented");
	}
	
	
	@After
	public void tearDown() throws Exception {
	}

}
