package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

public class SymbolTableTest  {
	
	SymbolTable symbolTable;
	
	@Before
	public void setUp() throws Exception {
		symbolTable = new SymbolTable();
		symbolTable.declareType("int1", new IntegerType());
		symbolTable.declareType("bool2", new BooleanType());
		symbolTable.declareValue("bool2", new BooleanValue(true));
		symbolTable.enter();
		symbolTable.declareType("int2", new IntegerType());
		symbolTable.declareValue("int2", new IntegerValue(-1));
		
	}

	@Test
	public void testLookup(){
		assertNotNull(symbolTable.lookupValue("int2"));
		assertNotNull(symbolTable.lookupValue("bool2"));
	}
	
	@Test
	public void testLookupLocal(){
		assertNotNull(symbolTable.lookupValue("bool2"));
		assertNull(symbolTable.lookupValueLocal("bool2"));
	}
	
	@Test
	public void testValueReference(){
		symbolTable.declareValueReference(
				"reference-bool2", 
				symbolTable.lookupValueReference("bool2")
				);
		assertEquals(symbolTable.lookupValue("bool2"), symbolTable.lookupValue("reference-bool2") );
	}
	
	@Test(expected=VariableNotDeclaredException.class)
    public void testDeclareVariableBeforeTypeFails() throws VariableNotDeclaredException, TypeMismatchException{
		symbolTable.declareValue("a", new IntegerValue(1));	
	}
	 
	@Test(expected=TypeMismatchException.class)
	public void testDeclareVariableOfDifferentTypeFails() throws VariableNotDeclaredException, TypeMismatchException, VariableAlreadyDeclaredException{
		symbolTable.declareType("b", new BooleanType());
		symbolTable.declareValue("b", new IntegerValue(1));
	}
	
	@Test(expected=VariableAlreadyDeclaredException.class)
	public void testDoubleDeclareFails() throws VariableAlreadyDeclaredException  {
		symbolTable.declareType("c", new BooleanType());
		symbolTable.declareType("c", new BooleanType());
	}

	@Test
	public void testDoubleDeclareScoped() throws VariableAlreadyDeclaredException, VariableNotDeclaredException, TypeMismatchException{
		symbolTable.declareType("c", new BooleanType());
		symbolTable.declareValue("c", new BooleanValue(true));
		symbolTable.enter();
		symbolTable.declareType("c", new BooleanType());
		symbolTable.declareValue("c", new BooleanValue(false));
	}
	
	
	

}
