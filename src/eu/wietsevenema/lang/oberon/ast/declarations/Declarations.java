package eu.wietsevenema.lang.oberon.ast.declarations;

import java.util.List;

import xtc.tree.Node;

public class Declarations extends Node {

	List<ConstantDecl> constants;
	List<TypeDecl> types;
	List<VarDecl> vars;
		
	public Declarations(List<ConstantDecl> cd, List<TypeDecl> td,
			List<VarDecl> vd) {
		this.constants = cd;
		this.types = td;
		this.vars = vd;
	}

}
