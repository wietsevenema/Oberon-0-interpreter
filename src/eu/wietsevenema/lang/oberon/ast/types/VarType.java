package eu.wietsevenema.lang.oberon.ast.types;

import xtc.tree.Node;

public class VarType extends Node {

	private String type;

	public VarType(String type) {
		this.type = type;
	}

	public String toString(){
		return this.getTypename();
	}

	private String getTypename() {
		return this.type;
	}
	

}
