package eu.wietsevenema.lang.oberon.ast;

import xtc.tree.Node;

public class IntegerConstant extends Node {

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
