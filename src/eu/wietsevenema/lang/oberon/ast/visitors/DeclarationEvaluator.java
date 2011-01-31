package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.ConstantDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.TypeDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.VarDecl;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.types.Type;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.parser.ProcedureDecl;

public class DeclarationEvaluator extends Visitor {

	SymbolTable symbolTable;
	
	public DeclarationEvaluator( SymbolTable symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public void visit( Declarations decls ){
		List<VarDecl> varDecls = decls.getVars();
		for( VarDecl v : varDecls){
			dispatch(v);
		}
		
		List<ConstantDecl> constantDecls = decls.getConstants();
		for( ConstantDecl c : constantDecls){
			dispatch(c);
		}
		
		List<TypeDecl> typeDecls = decls.getTypes();
		for(TypeDecl t: typeDecls){
			dispatch(t);
		}
		
		List<ProcedureDecl> procDecls = decls.getProcedures();
		for(ProcedureDecl p: procDecls){
			dispatch(p);
		}
		
	}
	
	public void visit( ProcedureDecl procDecl){
		symbolTable.declareProc(procDecl.getIdentifier().getName(), procDecl);
	}
	
	public void visit( ConstantDecl constantDecl){
		//FIXME
	}
	
	public void visit( TypeDecl typeDecl){
		//FIXME
	}
	
	public void visit( VarDecl varDecl ) throws VariableAlreadyDeclaredException{
		List<Identifier> identifiers = varDecl.getIdentifiers();
		Type type = varDecl.getType();
		for( Identifier id : identifiers){
			symbolTable.declareType(id.getName(), type);
		}
	}
	
	
}
