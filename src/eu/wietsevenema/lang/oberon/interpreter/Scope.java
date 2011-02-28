package eu.wietsevenema.lang.oberon.interpreter;

import java.util.HashMap;
import java.util.Map;

import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.values.Value;

public class Scope {

	Scope parent;

	private Map<String, ValueReference> symbols = new HashMap<String, ValueReference>();
	private Map<String, Procedure> procs = new HashMap<String, Procedure>();
	private Map<String, VarType> types = new HashMap<String, VarType>();

	public Scope() {
	}

	public Scope(Scope parent) {
		this.parent = parent;
	}

	public Scope getParent() {
		return parent;
	}

	public Procedure lookupProc(String symbol) {
		Procedure result = this.lookupProcLocal(symbol);
		if (result == null && this.parent != null) {
			result = parent.lookupProc(symbol);
		}
		return result;
	}

	private Procedure lookupProcLocal(String symbol) {
		return procs.get(symbol);
	}

	public VarType lookupType(String symbol) {
		VarType result = this.lookupTypeLocal(symbol);
		if (result == null && this.parent != null) {
			result = parent.lookupType(symbol);
		}
		return result;
	}

	private VarType lookupTypeLocal(String symbol) {
		return types.get(symbol);
	}

	public Value lookupValue(String symbol) throws VariableNotDeclaredException {
		ValueReference valueRef = this.lookupValueReference(symbol);
		if (valueRef == null) {
			throw new VariableNotDeclaredException();
		}
		return valueRef.getValue();
	}

	
	public ValueReference lookupValueReference(String symbol) {
		ValueReference result = this.lookupValueReferenceLocal(symbol);
		if (result == null && this.parent != null) {
			result = parent.lookupValueReference(symbol);
		}
		return result;
	}

	private ValueReference lookupValueReferenceLocal(String symbol) {
		return symbols.get(symbol);
	}

	public void declareValue(String symbol, Value value) throws VariableAlreadyDeclaredException {
		ValueReference valueRef = this.lookupValueReferenceLocal(symbol);
		if (valueRef != null) { // Variabele bestaat al in deze scope.
			throw new VariableAlreadyDeclaredException();
		}
		this.declareValueReference(symbol, new ValueReference(value));
	}

	public void declareValueReference(String symbol, ValueReference valueRef) {
		assert valueRef != null;
		symbols.put(symbol, valueRef);
	}

	public void declareProc(String symbol, Procedure proc) {
		assert proc != null;
		procs.put(symbol, proc);
	}

	public void declareType(String symbol, VarType type) {
		assert type != null;
		types.put(symbol, type);
	}

}
