package eu.wietsevenema.lang.oberon.ast.declarations;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class TypeDecl extends Declaration {

	private Identifier identifier;
	private Type type;

	//FIXME i'm wrong. 
	public TypeDecl(Identifier identifier, Type type) {
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
