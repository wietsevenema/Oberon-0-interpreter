package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;

public class Value<T>  {

	private T value;
	
	public Value( T value ){
		this.value = value;
	}
	
	public final boolean matchesType(Value<?> that) throws ValueUndefinedException {
		return this.getValue().equals(that.getValue());
	}
	
	@SuppressWarnings("unchecked") // Runtime error is fine.  
	public void setValue(Object value){ 
		this.value = (T)value;
	}
		
	public T getValue() throws ValueUndefinedException {
		if (this.value == null) {
			throw new ValueUndefinedException();
		}
		return this.value;
	}
	
	

}
