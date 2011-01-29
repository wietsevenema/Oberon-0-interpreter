package eu.wietsevenema.lang.oberon.interpreter;

public class BooleanValue extends Value {

	Boolean value;
	
	public BooleanValue( Boolean bool ){
		this.value = bool;
	}
	
	public Boolean getValue(){
		return value;
	}
	
}
