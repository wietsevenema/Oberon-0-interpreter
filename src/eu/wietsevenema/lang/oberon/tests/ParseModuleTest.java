package eu.wietsevenema.lang.oberon.tests;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ModulePrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;

public class ParseModuleTest extends ParserTest {

	@Test
	public void testProcedureCall() throws IOException, InvalidInputException, ParseException{
		Node result = parseModuleFile(getAbsFilename("oberon/parser/procedurecall.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String)printer.dispatch(result);
		assertEquals("MODULE Procedure;BEGINProcedure1();Procedure2();Procedure3(a,1,(a+2))END Procedure.", actual);
	}
	
	@Test
	public void testProcedureDef() throws IOException, InvalidInputException, ParseException{
//		Node result = parseModuleFile(getAbsFilename("oberon/parser/proceduredef.o0"));
//		ModulePrinter printer = new ModulePrinter();
//		String actual = (String)printer.dispatch(result);
//		assertEquals("MODULE Procedure;BEGINProcedure1();Procedure2();Procedure3(a,1,(a+2))END Procedure.", actual);
	}
	

}
