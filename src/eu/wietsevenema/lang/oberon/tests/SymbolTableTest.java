package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.ast.types.BooleanType;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.BooleanValue;
import eu.wietsevenema.lang.oberon.interpreter.IntegerValue;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;

public class SymbolTableTest {
	
	SymbolTable symbolTable;
	
	@Before
	public void setUp() throws Exception {
		symbolTable = new SymbolTable();
		symbolTable.defineType("int1", new IntegerType());
		symbolTable.defineType("bool2", new BooleanType());
		symbolTable.defineValue("bool2", new BooleanValue(true));
		symbolTable.enter();
		symbolTable.defineType("int2", new IntegerType());
		symbolTable.defineValue("int2", new IntegerValue(-1));
		
	}

	
	public void testLookup(){
		
	}
	
	public void testLookupLocal(){
		
	}
	
	@Test(expected=VariableNotDeclaredException.class)
    public void testDeclareVariableBeforeType() throws VariableNotDeclaredException{
		symbolTable.defineValue("a", new IntegerValue(1));	
	}
	 

	@Test(expected=TypeMismatchException.class)
	public void testDeclareVariableOfDifferentType() throws VariableNotDeclaredException{
		symbolTable.defineType("b", new BooleanType());
		symbolTable.defineValue("b", new IntegerValue(1));
	}
	
	@Test(expected=VariableAlreadyDeclaredException.class)
	public void testDoubleDeclare(){
		symbolTable.defineType("c", new BooleanType());
		symbolTable.defineType("c", new BooleanType());
	}
	
	@Test
	public void testVariableAlreadyDeclaredNotRaised(){
		symbolTable.defineType("c", new BooleanType());
		symbolTable.enter();
		symbolTable.defineType("c", new BooleanType());
	}
	

	@Test
	public void testEnter() {
		
	}

	@Test
	public void testExit() {
		
	}

}
