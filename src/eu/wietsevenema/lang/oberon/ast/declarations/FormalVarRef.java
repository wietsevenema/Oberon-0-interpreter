package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;

public class FormalVarRef extends FormalVar {

	public FormalVarRef(Identifier identifier, VarType type) {
		super(identifier, type);
	}

}
