package eu.wietsevenema.lang.oberon.interpreter;

import java.util.HashMap;
import java.util.Map;

import eu.wietsevenema.lang.oberon.ast.types.Type;
import eu.wietsevenema.lang.oberon.exceptions.TypeMismatchException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.parser.ProcedureDecl;

public class SymbolTable {

	public static class Scope {

		String name;
		Scope parent;

		Map<String, ValueReference> symbols = new HashMap<String, ValueReference>();
		Map<String, Type> types = new HashMap<String, Type>();
		Map<String, ProcedureDecl> procs = new HashMap<String, ProcedureDecl>();

		public Scope() {
		}
		
		public Scope(Scope parent) {
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public Scope getParent() {
			return parent;
		}
		
		public ProcedureDecl lookupProc(String symbol) {
			ProcedureDecl result = this.lookupProcLocal(symbol);
			if(result == null && this.parent != null){
				result = parent.lookupProc(symbol);
			} 
			return result;
		}
		
		private ProcedureDecl lookupProcLocal(String symbol) {
			return procs.get(symbol);
		}

		public Value lookupValue(String symbol) {
			ValueReference valueRef = this.lookupValueReference(symbol);
			return (valueRef==null)?null:valueRef.getValue();
		}
		
		public Value lookupValueLocal(String symbol) {
			ValueReference valueRef = this.lookupValueReferenceLocal(symbol);
			return (valueRef==null)?null:valueRef.getValue();
		}
		
		public Type lookupType(String symbol) {
			Type result = lookupTypeLocal(symbol);
			if(result == null && this.parent != null){
				result = parent.lookupType(symbol);
			} 
			return result;
		}

		public Type lookupTypeLocal(String symbol) {
			return types.get(symbol);
		}
		
		public ValueReference lookupValueReference(String symbol) {
			ValueReference result = this.lookupValueReferenceLocal(symbol);
			if(result == null && this.parent != null){
				result = parent.lookupValueReference(symbol);
			}
			return result;
		}
		
		public ValueReference lookupValueReferenceLocal(String symbol) {
			return symbols.get(symbol);
		}
		

		public void declareValue(String symbol, Value value) throws VariableNotDeclaredException, TypeMismatchException {
			Type type = this.lookupType(symbol);
			if(type==null){
				throw new VariableNotDeclaredException("Variable " + symbol + " has not yet been declared.");
			}
			if(!value.matchesType( type )){
				throw new TypeMismatchException("Variable " + symbol + " has declared type " + type.getName());
			}
			
			/* Check for existing reference */
			ValueReference valueRef = this.lookupValueReference(symbol);
			if(valueRef==null){
				this.declareValueReference(symbol, new ValueReference(value));
			} else {
				valueRef.setValue(value); 
			}
		}
		
		public void declareType(String symbol, Type type) throws VariableAlreadyDeclaredException {
			if(this.lookupTypeLocal(symbol) != null){
				throw new VariableAlreadyDeclaredException("Variable "+ symbol + " already declared in this scope.");
			}
			types.put(symbol, type);
		}

		public void declareValueReference(String symbol, ValueReference valueRef) {
			assert valueRef!=null;
			symbols.put(symbol, valueRef);
		}

		public void declareProc(String symbol, ProcedureDecl proc) {
			assert proc!=null;
			procs.put(symbol, proc);
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

	public void declareValue(String symbol, Value value ) throws VariableNotDeclaredException, TypeMismatchException {
		this.getCurrent().declareValue(symbol, value);
	}
	
	public void declareType(String symbol, Type type ) throws VariableAlreadyDeclaredException {
		this.getCurrent().declareType(symbol, type);
	}
	
	public void declareProc(String symbol, ProcedureDecl proc ) {
		this.getCurrent().declareProc(symbol, proc);
	}

	public ValueReference lookupValueReference(String symbol) {
		return this.getCurrent().lookupValueReference(symbol);
	}

	public void declareValueReference(String symbol,ValueReference ref) {
		this.getCurrent().declareValueReference(symbol, ref);
	}

	public Value lookupValueLocal(String symbol) {
		return this.getCurrent().lookupValueLocal(symbol);
	}

	public ProcedureDecl lookupProc(String name) {
		return this.getCurrent().lookupProc(name);
	}

}
