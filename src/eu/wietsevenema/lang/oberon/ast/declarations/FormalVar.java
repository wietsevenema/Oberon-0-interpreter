package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;

public class FormalVar extends Declaration {

	private Identifier identifier;
	private VarType type;
		
	public FormalVar(Identifier identifier, VarType type){
		this.identifier = identifier;
		this.type = type;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public VarType getType() {
		return type;
	}
	
}
