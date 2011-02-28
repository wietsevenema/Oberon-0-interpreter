package eu.wietsevenema.lang.oberon.interpreter;

import java.util.HashMap;
import java.util.Map;

import eu.wietsevenema.lang.oberon.ast.types.VarType;
import eu.wietsevenema.lang.oberon.exceptions.SymbolAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.SymbolNotDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.TypeNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.values.Value;

public class Scope {

	Scope parent;

	private Map<String, Bindable> symbols = new HashMap<String, Bindable>();
	private Map<String, VarType> types = new HashMap<String, VarType>();

	public Scope() {
	}

	public Scope(Scope parent) {
		this.parent = parent;
	}

	public Scope getParent() {
		return parent;
	}

	public VarType lookupType(String symbol) throws TypeNotDeclaredException {
		VarType result = types.get(symbol);
		if (result == null && this.parent != null) {
			result = parent.lookupType(symbol);
		}
		if (result == null) {
			throw new TypeNotDeclaredException();
		}
		return result;
	}
	
	public void declareType(String symbol, VarType type) {
		assert type != null;
		types.put(symbol, type);
	}

	private Bindable lookupSymbol(String symbol) throws SymbolNotDeclaredException {
		Bindable result = symbols.get(symbol);
		if (result == null && this.parent != null) {
			result = parent.lookupSymbol(symbol);
		}
		if (result == null) {
			throw new SymbolNotDeclaredException();
		}
		return result;
	}

	public Value lookupValue(String symbol) throws SymbolNotDeclaredException {
		ValueReference valueRef = (ValueReference) this.lookupSymbol(symbol);
		return valueRef.getValue();
	}

	public ValueReference lookupValueReference(String symbol) throws SymbolNotDeclaredException {
		return (ValueReference) lookupSymbol(symbol);
	}

	public Procedure lookupProc(String symbol) throws SymbolNotDeclaredException {
		return (Procedure) lookupSymbol(symbol);
	}

	private void declareSymbol(String symbol, Bindable bindable) throws SymbolAlreadyDeclaredException {
		assert bindable != null;
		if (!symbols.containsKey(symbol)) {
			symbols.put(symbol, bindable);
		} else {
			throw new SymbolAlreadyDeclaredException();
		}
	}

	public void declareValue(String symbol, Value value) throws SymbolAlreadyDeclaredException {
		this.declareSymbol(symbol, new ValueReference(value));
	}

	public void declareValueReference(String symbol, ValueReference valueRef) throws SymbolAlreadyDeclaredException {
		declareSymbol(symbol, valueRef);
	}

	public void declareProc(String symbol, Procedure proc) throws SymbolAlreadyDeclaredException {
		declareSymbol(symbol, proc);
	}

	

}
