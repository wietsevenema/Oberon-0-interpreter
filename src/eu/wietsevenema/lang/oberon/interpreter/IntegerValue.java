package eu.wietsevenema.lang.oberon.interpreter;

public class IntegerValue extends Value {

	Integer value;
	
	public IntegerValue(Integer value){
		this.value = value;
	}
	
	public Integer getValue(){
		return this.value;
	}
}
