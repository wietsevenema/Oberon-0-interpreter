package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;
import eu.wietsevenema.lang.oberon.interpreter.Scope;

public class ModuleEvaluator extends Visitor {

	Scope scope;

	public ModuleEvaluator(Scope scope) {
		this.scope = scope;
	}

	public void visit(Module module) {
		Declarations decls = module.getDeclarations();
		DeclarationEvaluator declEval = new DeclarationEvaluator(scope);
		declEval.dispatch(decls);

		List<Statement> statements = module.getStats();
		StatementEvaluator statEval = new StatementEvaluator(scope);
		for (Statement stat : statements) {
			statEval.dispatch(stat);
		}

	}

}
