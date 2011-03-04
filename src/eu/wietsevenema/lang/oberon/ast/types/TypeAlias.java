package eu.wietsevenema.lang.oberon.ast.types;


import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;

public class TypeAlias extends VarType {

	private Identifier identifier;

	public TypeAlias(Identifier identifier) {
		this.identifier = identifier;
	}

	public Identifier getIdentifier() {
		return identifier;
	}


}
