package eu.wietsevenema.lang.oberon.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;
import eu.wietsevenema.lang.oberon.parser.Oberon;

abstract public class ParserTest {

	protected String getAbsFilename(String relName) {
		return getClass().getResource(relName).getPath();
	}

	protected Node parseExpressionFile(String absPath) throws IOException, InvalidInputException, ParseException {
		return this.parseFile(absPath, true);
	}
	
	protected Node parseModuleFile(String absPath) throws IOException, InvalidInputException, ParseException {
		return this.parseFile(absPath, false);
	}
	
	private Node parseFile(String absPath, boolean parseExpression) throws IOException,
			InvalidInputException, ParseException {
				Reader in = null;
				in = new BufferedReader(new FileReader(new File(absPath)));
				
				Oberon p = new Oberon(in, absPath, (int) new File(absPath).length());
				Result r;
				if(parseExpression){
					r = p.pExpression(0);
				} else {
					r = p.pProgram(0);
				}
				
				
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

}