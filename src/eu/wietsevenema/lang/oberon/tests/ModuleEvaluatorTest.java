package eu.wietsevenema.lang.oberon.tests;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.declarations.ConstantDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.declarations.TypeDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.VarDecl;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.visitors.ModuleEvaluator;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;

public class ModuleEvaluatorTest  {
	
	SymbolTable symbolTable;
		
	@Before
	public void setUp() throws Exception {
		this.symbolTable = new SymbolTable();
	}

	@Test
	public void testModuleWithDeclarations(){
		List<Statement> stats = new ArrayList<Statement>();
		stats.add(
				new AssignmentStatement(new Identifier("a"), new IntegerConstant(1) )
				);
		
		List<Identifier> ids = new ArrayList<Identifier>();
		ids.add( new Identifier("a"));
		
		List<VarDecl> decls = new ArrayList<VarDecl>();
		decls.add(
				new VarDecl(ids, new IntegerType())
				);
		
		Module m = new Module(
						new Identifier("Testmodule"), 
						new Declarations(
								new ArrayList<ConstantDecl>(),
								new ArrayList<TypeDecl>(),
								decls
								),
						stats);
		
		ModuleEvaluator me = new ModuleEvaluator(symbolTable);
		me.dispatch(m);
		
		assertNotNull(symbolTable.lookupType("a"));
		assertNotNull(symbolTable.lookupValue("a"));
	}
	
	public void testModuleWithConstants(){
		fail("Not implemented");
	}
	
	public void testModuleWithTypes(){
		fail("Not implemented");
	}
	
	
}
