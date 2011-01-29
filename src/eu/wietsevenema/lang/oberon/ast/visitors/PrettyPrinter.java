package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Printer;
import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.BinaryExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalNegationExpression;

public class PrettyPrinter extends Visitor {

	Printer printer; 

	public PrettyPrinter(Printer ptr) {
		printer = ptr;
	}

	public void visit(BinaryExpression binexp) {
		printer.p("( ").pln().incr().indent();
		
		dispatch(binexp.getLeft());
		printer.p(binexp.getToken());
		dispatch(binexp.getRight());
		
		printer.pln().decr().indent().p(" )").pln().indent();
		
	}
	
	public void visit(LogicalNegationExpression lne){
		printer.p("~(").pln().incr().indent();
		dispatch(lne.getChild());
		printer.pln().decr().indent().p(" )").pln().indent();
	}

	public void visit(IntegerConstant c) {
		printer.p( c.getInteger());
	}

}
