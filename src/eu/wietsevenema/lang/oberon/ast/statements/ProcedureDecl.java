package eu.wietsevenema.lang.oberon.ast.statements;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.declarations.Declaration;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVar;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVarRef;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.VarType;

public class ProcedureDecl extends Declaration {
	
	private Identifier identifier;
	private List<FormalVar> formals;
	private Declarations declarations;
	private List<Statement> statements;
	
	public ProcedureDecl(Identifier identifier, List<Node> formals,
			Declarations declarations, List<Statement> statements) {
		this.identifier = identifier;
		this.formals = transformFormals(formals);
		this.declarations = (declarations!=null)
								? declarations
								: new Declarations(null, null, null, null);
		this.statements = (statements!=null)
								? statements
								: new ArrayList<Statement>();
	}

	
	public Identifier getIdentifier() {
		return identifier;
	}

	public List<FormalVar> getFormals() {
		return formals;
	}


	public Declarations getDeclarations() {
		return declarations;
	}


	public List<Statement> getStatements() {
		return statements;
	}


	private List<FormalVar> transformFormals(List<Node> formals){
		ArrayList<FormalVar> result = new ArrayList<FormalVar>();
		if(formals != null){
			for( Node n: formals){
				/* 
				 * Node n => ("VAR":Word)? IdentList void:":":Symbol Type
				 * get(0) = ("VAR":Word)?
				 * get(1) = IdentList
				 * get(2) = Type
				 */
				//TODO refactor
				if(n.getString(0) == null){ // Normal var.
					for(Object identifier : (List<?>)n.get(1) ){
						if(!(identifier instanceof Identifier) ){
							throw new IllegalStateException();
						} else {
							result.add(
								new FormalVar(
									(Identifier)identifier, 
									(VarType)n.get(2)
									)
								);
						}
					}
				} else { // Reference var.
					for(Object identifier : (List<?>)n.get(1) ){
						if(!(identifier instanceof Identifier) ){
							throw new IllegalStateException();
						} else {
							result.add(
							new FormalVarRef(
									(Identifier)identifier, 
									(VarType)n.get(2)
									)
							);
						}
					}
				}
			}
		}
		return result;
	}


	

	 
	
}
