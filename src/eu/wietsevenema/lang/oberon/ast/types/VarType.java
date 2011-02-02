package eu.wietsevenema.lang.oberon.ast.types;

import xtc.tree.Node;

public class VarType extends Node {

	private String identifier;

	public VarType(String identifier) {
		this.identifier = identifier;
	}

	public String toString(){
		return this.getIdentifier();
	}

	private String getIdentifier() {
		return this.identifier;
	}

}
