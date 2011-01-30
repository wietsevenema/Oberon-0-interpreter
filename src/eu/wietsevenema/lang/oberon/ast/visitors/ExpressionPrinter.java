package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Node;
import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.BinaryExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalNegationExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.TestExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.UnaryMinExpression;
import eu.wietsevenema.lang.oberon.exceptions.InvalidInputException;

public class ExpressionPrinter extends Visitor {
	
	public Node visit(Node n) throws InvalidInputException{
		if( n instanceof Expression ){
			return n;
		} else {
			throw new InvalidInputException("Expected type Expression, got " + n.getClass());
		}
	}
	
	public String visit(BinaryExpression binexp) {
		return "("
				+dispatch(binexp.getLeft())
				+binexp.getToken()
				+dispatch(binexp.getRight())
				+")"
				;
	}
	
	public String visit(TestExpression exp){
		return (String) dispatch(exp.getChild());
	}
	
	public String visit(Expression exp){
		return exp.toString();
	}
	
	public String visit(LogicalNegationExpression lne){
		return "(~"+dispatch(lne.getChild())+")";
	}
	
	public String visit(UnaryMinExpression un){
		return "(-"+dispatch(un.getChild())+")";
	}
	
}
