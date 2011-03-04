package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.visitors.ModuleEvaluator;
import eu.wietsevenema.lang.oberon.ast.visitors.ModulePrinter;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;
import eu.wietsevenema.lang.oberon.exceptions.ParseException;
import eu.wietsevenema.lang.oberon.exceptions.SymbolNotDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.interpreter.Scope;
import eu.wietsevenema.lang.oberon.interpreter.values.IntegerValue;

public class ParserTest {

	@Test
	public void testProcedureCall() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseModuleFile(Util.getAbsFilename("oberon/parser/procedurecall.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		assertEquals("Module[Procedure,Declarations[{},{},{},{}],{" + "ProcedureCallStatement[Procedure1,{}],"
				+ "ProcedureCallStatement[Procedure2,{}],"
				+ "ProcedureCallStatement[Procedure3,{a,1,AdditiveExpression[a,2]}]}]", actual);
	}

	@Test
	public void testProcedureDecl() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseModuleFile(Util.getAbsFilename("oberon/parser/proceduredef.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		String expected = "Module[Procedure,Declarations[{},{},{}," + "{ProcedureDecl[test,"
				+ "{FormalVarRef[i,INTEGER],FormalVarRef[k,INTEGER],FormalVar[x,BOOLEAN],FormalVar[y,BOOLEAN]},"
				+ "Declarations[{},{},{VarDecl[{t},INTEGER],VarDecl[{q,r},BOOLEAN]},{}],"
				+ "{AssignmentStatement[i,0]}],"
				+ "ProcedureDecl[test2,{},Declarations[{},{},{VarDecl[{x},INTEGER]},{}],{}]" + "}],{}]";

		assertEquals(expected, actual);
	}

	@Test
	public void testWhileStatement() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseModuleFile(Util.getAbsFilename("oberon/parser/whilestatement.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		String expected = "Module[While,Declarations[{},{},{VarDecl[{t1},INTEGER],VarDecl[{t2},BOOLEAN]},{}],{"
				+ "WhileStatement[LessOrEqualExpression[t1,5],{"
				+ "AssignmentStatement[t,AdditiveExpression[t,1]],AssignmentStatement[t2,true]" + "}]" + "}]";
		assertEquals(expected, actual);
	}

	@Test
	public void testSwapProcedure() throws IOException, InvalidInputException, ParseException, ValueUndefinedException,
			SymbolNotDeclaredException {
		Node result = Util.parseModuleFile(Util.getAbsFilename("oberon/swap.o0"));
		Scope st = new Scope();
		ModuleEvaluator me = new ModuleEvaluator(st);
		me.dispatch(result);

		/*
		 * x := 1; y := 2; Swap(x, y)
		 */

		assertEquals(new Integer(2), ((IntegerValue) st.lookupValue("x")).getValue());
		assertEquals(new Integer(1), ((IntegerValue) st.lookupValue("y")).getValue());

	}

	@Test
	public void testAllOperatorsGetParsed() throws IOException, InvalidInputException, ParseException {
		Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/allops.expr"));
	}

	@Test
	public void testAdditionOpsLeftAssoc() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/additionopsleftassoc.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		assertEquals("AdditiveExpression[SubtractiveExpression[AdditiveExpression[2,3],1],41]", actual);
	}

	@Test(expected = ParseException.class)
	public void testEqualityOpsNotChainable() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/equalityops.expr"));
		ModulePrinter printer = new ModulePrinter();

		printer.dispatch(result);

	}

	@Test
	public void testMultiplicationOpsLeftAssoc() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/multiplicationopsleftassoc.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		assertEquals("ModulusExpression[DivisiveExpression[MultiplicativeExpression[2,3],666],12]", actual);
	}

	@Test
	public void testParenthesisedOpsBind() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/parenthesisedopsbind.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		assertEquals(
				"GreaterOrEqualExpression[LessExpression[SubtractiveExpression[AdditiveExpression[2,1],4],false],3]",
				actual);
	}

	@Test
	public void testPrecedenceOrder() throws IOException, InvalidInputException, ParseException {
		// 1 # 1 + 1 DIV 1 & 1 OR 1 ~ 1
		Node result = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/precedence.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		String expected = "NotExpression[1,LogicalDisjunctiveExpression["
				+ "AdditiveExpression[1,LogicalConjunctiveExpression["
				+ "DivisiveExpression[1,1],1]],LogicalNegationExpression[1]]]";
		assertEquals(expected, actual);
	}

	@Test
	public void testUnaryMin() throws IOException, InvalidInputException, ParseException {
		// -3 * 2 bind als -(3*2)!!
		Node unaryMinLhs = Util.parseExpressionFile(Util.getAbsFilename("oberon/expr/unarymin.expr"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(unaryMinLhs);
		assertEquals("UnaryMinExpression[MultiplicativeExpression[3,2]]", actual);
	}

	@Test
	public void arraySelectorTest() throws InvalidInputException, ParseException, IOException {

		ModulePrinter printer = new ModulePrinter();

		Node result = Util.parseExpressionString("a[1][2+3]");
		String actual = (String) printer.dispatch(result);
		assertEquals("ArraySelector[ArraySelector[a,1],AdditiveExpression[2,3]]", actual);

		result = Util.parseExpressionString("a[b]");
		actual = (String) printer.dispatch(result);
		assertEquals("ArraySelector[a,b]", actual);

	}

	@Test(expected = ParseException.class)
	public void emptyArraySelectorTest() throws InvalidInputException, ParseException, IOException {
		Util.parseExpressionString("a[]");
	}

	@Test
	public void testTypeAlias() throws IOException, InvalidInputException, ParseException {
		Node result = Util.parseModuleFile(Util.getAbsFilename("oberon/parser/typealias.o0"));
		ModulePrinter printer = new ModulePrinter();
		String actual = (String) printer.dispatch(result);
		String expected = "Module[TypeAlias,Declarations[{},{TypeDecl[myType,INTEGER],TypeDecl[secondType,BOOLEAN]},"
				+ "{VarDecl[{a},myType]},{}],{AssignmentStatement[a,999]}]";
		assertEquals(expected, actual);
	}

	@Test
	public void testRecordType() throws IOException, InvalidInputException, ParseException {
		fail("Not implemented");
		// Node result =
		// Util.parseModuleFile(Util.getAbsFilename("oberon/parser/typealias.o0"));
		// JSONSerializer serializer = new JSONSerializer().transform(new
		// ObjectTransformer(), "*");
		// String actual = serializer.serialize( result );
		// assertEquals("MODULE TypeAlias;TYPE myType=INTEGER;TYPE secondType=BOOLEANVAR a: myTypeBEGINa:=999END TypeAlias.",
		// actual);
	}

}
