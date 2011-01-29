package eu.wietsevenema.lang.oberon.ast.types;

import xtc.tree.Node;

public class Identifier extends Node {
	
	String name;
	
	public Identifier(String name) {
		this.name = name;
	}

}
