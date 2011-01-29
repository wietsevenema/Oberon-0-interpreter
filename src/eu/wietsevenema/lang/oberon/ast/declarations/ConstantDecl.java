package eu.wietsevenema.lang.oberon.ast.declarations;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.visitors.TransformGenerics;

public class ConstantDecl extends Declaration {

	Identifier id;
	Expression exp;
	
	public ConstantDecl(Identifier id, Node exp) {
		this.id = id;
		TransformGenerics tg = new TransformGenerics();
		this.exp = (Expression) tg.dispatch(exp);
	}

}
