package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.ast.types.Type;

public class IntegerValue extends Value implements Comparable<IntegerValue>{

	Integer value;
	
	public IntegerValue(Integer value){
		this.value = value;
	}
	
	public Integer getValue(){
		return this.value;
	}

	@Override
	public int compareTo(IntegerValue that) {
		return this.getValue().compareTo(that.getValue());
	}

	@Override
	public boolean matchesType(Type type) {
		return (type instanceof IntegerType);
	}
	

}
