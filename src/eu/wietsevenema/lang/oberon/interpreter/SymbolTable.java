package eu.wietsevenema.lang.oberon.interpreter;

import java.util.HashMap;
import java.util.Map;

import eu.wietsevenema.lang.oberon.ast.types.Type;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;

public class SymbolTable {

	public static class Scope {

		String name;
		Scope parent;

		Map<String, Value> symbols = new HashMap<String, Value>();
		Map<String, Type> types = new HashMap<String, Type>();

		Scope() {
		}
		
		Scope(Scope parent) {
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public Scope getParent() {
			return parent;
		}

		
		public Value lookupValue(String symbol) {
			Value result = lookupValueLocal(symbol);
			if(result == null && this.parent != null){
				result = parent.lookupValue(symbol);
			} 
			return result;
		}
		
		public Type lookupType(String symbol) {
			Type result = lookupTypeLocal(symbol);
			if(result == null && this.parent != null){
				result = parent.lookupType(symbol);
			} 
			return result;
		}

		public Value lookupValueLocal(String symbol) {
			return symbols.get(symbol);
		}
		
		public Type lookupTypeLocal(String symbol) {
			return types.get(symbol);
		}


		public void defineValue(String symbol, Value value) throws VariableNotDeclaredException, TypeMismatchException {
			Type type = this.lookupType(symbol);
			if(type==null){
				throw new VariableNotDeclaredException("Variable " + symbol + " has not yet been declared.");
			}
			if(!value.matchesType( type )){
				throw new TypeMismatchException("Variable " + symbol + " has type " + type.getName());
			}
			symbols.put(symbol, value);
		}
		
		public void defineType(String symbol, Type type) throws VariableAlreadyDeclaredException {
			if(this.lookupTypeLocal(symbol) != null){
				throw new VariableAlreadyDeclaredException("Variable "+ symbol + " already declared in this scope.");
			}
			types.put(symbol, type);
		}

	}

	private Scope current;

	public SymbolTable() {
		current = new Scope();
	}

	public Scope getCurrent() {
		return current;
	}
	
	public Value lookupValue(String symbol){
		return this.getCurrent().lookupValue(symbol);
	}

	public Type lookupType(String symbol){
		return this.getCurrent().lookupType(symbol);
	}
	
	public void enter() {
		Scope parent = current;
		Scope child = new Scope(parent);
		current = child;
	}

	public void exit() {
		current = current.parent;
	}

	public void defineValue(String symbol, Value value ) throws VariableNotDeclaredException, TypeMismatchException {
		this.getCurrent().defineValue(symbol, value);
	}
	
	public void defineType(String symbol, Type type ) throws VariableAlreadyDeclaredException {
		this.getCurrent().defineType(symbol, type);
	}

}
