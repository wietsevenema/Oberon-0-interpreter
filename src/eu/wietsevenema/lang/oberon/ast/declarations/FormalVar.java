package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class FormalVar extends Declaration {

	private Identifier identifier;
	private Type type;
		
	public FormalVar(Identifier identifier, Type type){
		this.identifier = identifier;
		this.type = type;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Type getType() {
		return type;
	}
	
}
