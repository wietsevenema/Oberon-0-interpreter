package eu.wietsevenema.lang.oberon.tests;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ModulePrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;

public class ParseModuleTest {


	public String getAbsFilename(String relName) {
		return getClass().getResource(relName).getPath();
	}

	
	@Test
	public void testProcedureCall() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseModuleFile(getAbsFilename("oberon/parser/procedurecall.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("MODULE Procedure;BEGINProcedure1();Procedure2();Procedure3(a,1,(a+2))END Procedure.", actual);
	}
	
	@Test
	public void testProcedureDecl() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseModuleFile(getAbsFilename("oberon/parser/proceduredef.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		//FIXME nicer print. 
		assertEquals("MODULE Procedure;PROCEDURE test" +
					"(VAR i : INTEGER;VAR k : INTEGER; x : BOOLEAN; y : BOOLEAN);VAR t: INTEGER;VAR q,r: BOOLEANi:=0"+
					"PROCEDURE test2();VAR x: INTEGERBEGINEND Procedure."
					, actual);
	}
	
	
	@Test
	public void testWhileStatement() throws IOException, InvalidInputException, ParseException{
		Node result = Util.parseModuleFile(getAbsFilename("oberon/parser/whilestatement.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		
		assertEquals("MODULE While;VAR t1: INTEGER;" +
				     "VAR t2: BOOLEAN" +
				     "BEGIN" +
				     "WHILE(t1<=5)" +
				     "DO" +
				     "t:=(t+1);" +
				     "t2:=true" +
				     "END" +
				     "END While."
				, actual);		
	}

}
