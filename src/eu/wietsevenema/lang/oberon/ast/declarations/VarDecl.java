package eu.wietsevenema.lang.oberon.ast.declarations;

import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class VarDecl extends Declaration {

	List<Identifier> ids;
	Type type;
	
	public VarDecl(List<Identifier> list, Type type) {
		this.ids = list;
		this.type = type;
	}

}
