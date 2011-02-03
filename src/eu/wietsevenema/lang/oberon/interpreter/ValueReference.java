package eu.wietsevenema.lang.oberon.interpreter;

import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.interpreter.values.Value;

public class ValueReference {

	private Value value;
	
	public ValueReference( Value value){
		this.value = value;
	}

	public Value getValue() {
		return value;
	}
	
	public void setValue(Value value) throws TypeMismatchException {
		if(this.value.getClass() == value.getClass()){
			this.value = value;
		} else {
			throw new TypeMismatchException();
		}
	}
	
	
}
