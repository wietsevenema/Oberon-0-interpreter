package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.ast.types.Type;

abstract public class Value {

	abstract public boolean matchesType( Type type );
		

}
