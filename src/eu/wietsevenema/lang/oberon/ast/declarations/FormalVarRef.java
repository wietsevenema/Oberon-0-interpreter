package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class FormalVarRef extends FormalVar {

	public FormalVarRef(Identifier identifier, Type type) {
		super(identifier, type);
	}

}
