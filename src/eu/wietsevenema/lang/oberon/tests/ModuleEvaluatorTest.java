package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.declarations.ConstantDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.declarations.TypeDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.VarDecl;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.ast.visitors.ModuleEvaluator;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;
import eu.wietsevenema.lang.oberon.parser.ProcedureDecl;

//FIXME PHP interpreter in Java bekijken
public class ModuleEvaluatorTest  {
	
	SymbolTable symbolTable;
		
	@Before
	public void setUp() throws Exception {
		this.symbolTable = new SymbolTable();
	}
	
	public String getAbsFilename(String relName) {
		return getClass().getResource(relName).getPath();
	}
	
	@Test
	public void testModuleWithVarDeclarations() throws VariableNotDeclaredException{
		
		
		List<Identifier> ids = new ArrayList<Identifier>();
		ids.add( new Identifier("a"));
		
		List<VarDecl> decls = new ArrayList<VarDecl>();
		decls.add(
				new VarDecl(ids, new VarType("INTEGER"))
				);
		
		List<Statement> stats = new ArrayList<Statement>();
		stats.add(
				new AssignmentStatement(new Identifier("a"), new IntegerConstant(1) )
				);
		
		Module m = new Module(
						new Identifier("Testmodule"), 
						new Declarations(
								new ArrayList<ConstantDecl>(),
								new ArrayList<TypeDecl>(),
								decls,
								new ArrayList<ProcedureDecl>()
								),
						stats);
		
		ModuleEvaluator me = new ModuleEvaluator(symbolTable);
		me.dispatch(m);
		
		assertNotNull(symbolTable.lookupValue("a"));
	}
	
	@Test
	public void testModuleWithConstants(){
		fail("Not implemented");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSwap() throws IOException, InvalidInputException, ParseException, ValueUndefinedException, VariableNotDeclaredException{
		Node result = Util.parseModuleFile(getAbsFilename("oberon/swap.o0"));
		SymbolTable st = new SymbolTable();
		ModuleEvaluator me = new ModuleEvaluator(st);
		me.dispatch(result);
		
		/*
		 * 	x := 1;
		 * 	y := 2;
		 * 	Swap(x, y)
		 */
		
		assertEquals( new Integer(2), ((Value<Integer>)st.lookupValue("x")).getValue() );
		assertEquals( new Integer(1), ((Value<Integer>)st.lookupValue("y")).getValue() );
		
	}
	
	public void testModuleWithTypes(){
		fail("Not implemented");
	}
	
		
	
}
