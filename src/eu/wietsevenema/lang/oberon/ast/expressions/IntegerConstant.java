package eu.wietsevenema.lang.oberon.ast.expressions;



public class IntegerConstant extends Expression {

	private Integer integer;

	public IntegerConstant(Integer integer) {
		this.setInteger(integer); 
	}

	private void setInteger(Integer integer) {
		this.integer = integer;
	}

	public Integer getInteger() {
		return integer;
	}

}
