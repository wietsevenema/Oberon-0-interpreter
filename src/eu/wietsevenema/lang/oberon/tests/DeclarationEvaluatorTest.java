package eu.wietsevenema.lang.oberon.tests;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.declarations.VarDecl;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.visitors.DeclarationEvaluator;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;

public class DeclarationEvaluatorTest extends TestCase {

	SymbolTable symbolTable;
	
	@Before
	public void setUp() throws Exception {
		symbolTable = new SymbolTable();
	}
	
	@Test
	public void testVarDeclaration(){
		List<Identifier> identifiers = new ArrayList<Identifier>();
		identifiers.add(new Identifier("a"));
		identifiers.add(new Identifier("b"));
		identifiers.add(new Identifier("c"));
		VarDecl varDecl = new VarDecl(identifiers, new IntegerType());
		
		DeclarationEvaluator eval = new DeclarationEvaluator(symbolTable);
		eval.dispatch(varDecl);
		
		assertNotNull(symbolTable.lookupType("a"));
		assertNotNull(symbolTable.lookupType("b"));
		assertNotNull(symbolTable.lookupType("c"));
		
	}
	

}
