package eu.wietsevenema.lang.oberon.ast.declarations;

import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;

public class Module extends Declaration {

	public Identifier getIdentifier() {
		return identifier;
	}

	public Declarations getDecls() {
		return decls;
	}

	public List<Statement> getStats() {
		return stats;
	}

	Identifier identifier; 
	Declarations decls; 
	List<Statement> stats;
	
	public Module(Identifier identifier, Declarations decls, List<Statement> stats) {
		this.identifier = identifier;
		this.decls = decls;
		this.stats = stats;
	}

}
