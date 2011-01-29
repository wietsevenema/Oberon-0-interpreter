package eu.wietsevenema.lang.oberon.ast.declarations;

import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class VarDecl extends Declaration {

	List<Identifier> identifiers;
	Type type;

	public List<Identifier> getIdentifiers() {
		return identifiers;
	}

	public Type getType() {
		return type;
	}
	
	public VarDecl(List<Identifier> identifiers, Type type) {
		this.identifiers = identifiers;
		this.type = type;
	}

}
