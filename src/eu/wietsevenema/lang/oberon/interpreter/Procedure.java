package eu.wietsevenema.lang.oberon.interpreter;

import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;

public interface Procedure {

	public abstract Identifier getIdentifier();

	public abstract List<Formal> getFormals();

	public abstract void execute(SymbolTable symbolTable);

}