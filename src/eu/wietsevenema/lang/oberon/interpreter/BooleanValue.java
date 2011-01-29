package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.ast.types.BooleanType;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class BooleanValue extends Value {

	Boolean value;
	
	public BooleanValue( Boolean bool ){
		this.value = bool;
	}
	
	public Boolean getValue(){
		return value;
	}

	@Override
	public boolean matchesType(Type type) {
		return (type instanceof BooleanType); 
	}
	
}
