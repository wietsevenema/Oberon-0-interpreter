package eu.wietsevenema.lang.oberon.ast.expressions;

public class UnaryMin extends Expression {

	Expression child;
	
	public UnaryMin(Expression child){
		this.child = child;
	}
	
	public Expression getChild() {
		return child;
	}
	
}
