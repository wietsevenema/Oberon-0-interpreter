package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;

public class Value<T>  {

	private T value;
	//FIXME use enum; 
	private String typeName;
	
	private Value( String typeName ){
		this.typeName = typeName;
	}
	
	public Value( T value){
		this.value = value;
		String typename = value.getClass().getName();
		if(typename == "java.lang.Integer"){
			this.typeName = "INTEGER";
		} else if (typename == "java.lang.Boolean"){
			this.typeName = "BOOLEAN";
		} else {
			throw new IllegalStateException("Type " + typename + " unrecognized.");
		}
	}
		
	public static Value<?> fromTypeName( String typename ){
		if(typename == "INTEGER"){
			return new Value<Integer>(typename);
		} else if (typename == "BOOLEAN"){
			return new Value<Boolean>(typename);
		} else {
			throw new IllegalStateException("Type " + typename + " unrecognized.");
		}
	}
	
	public final boolean matchesType(Value<?> that) {
		return (this.getType().equals(that.getType()));
	}
	
	public String getType(){
		return this.typeName;
	}
	
	
	
	public void setValue(T value){
		this.value = value;
	}
	
	public T getValue() throws ValueUndefinedException {
		if (this.value == null) {
			throw new ValueUndefinedException();
		}
		return this.value;
	}

}
