package eu.wietsevenema.lang.oberon.interpreter;

import java.io.InputStream;
import java.io.OutputStream;

import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.visitors.ModuleEvaluator;

public class Environment {

	private Scope scope;
	private OutputStream out;
	private InputStream in;

	public Environment(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		this.scope = new Scope();
		BuiltIns.inject(this);
	}

	public Scope getScope() {
		return scope;
	}

	public OutputStream getOut() {
		return out;
	}

	public InputStream getIn() {
		return in;
	}

	public void runModule(Module result) {
		ModuleEvaluator me = new ModuleEvaluator(scope);
		me.dispatch(result);
	}

}
