package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import xtc.tree.Node;
import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.Declarations;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVar;
import eu.wietsevenema.lang.oberon.ast.declarations.FormalVarRef;
import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.declarations.ProcedureDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.TypeDecl;
import eu.wietsevenema.lang.oberon.ast.declarations.VarDecl;
import eu.wietsevenema.lang.oberon.ast.expressions.ArraySelector;
import eu.wietsevenema.lang.oberon.ast.expressions.BinaryExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalNegationExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.TestExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.UnaryMinExpression;
import eu.wietsevenema.lang.oberon.ast.statements.AssignmentStatement;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureCallStatement;
import eu.wietsevenema.lang.oberon.ast.statements.WhileStatement;

/**
 * Deze moduleprinter is (nog) niet voor productiedoeleinden, alleen voor
 * unittests. Het zou mooi zijn als hier parsebare Oberon-0 uit kwam, want het
 * is mooi als print(parse(x))==x.
 * 
 * @author Wietse Venema
 * 
 */
public class ModulePrinter extends Visitor {

	public String visit(TestExpression exp) {
		return (String) dispatch(exp.getChild());
	}

	public String visit(Expression exp) {
		return exp.toString();
	}

	public String visit(BinaryExpression exp) {
		HashMap<String, String> operators = new HashMap<String, String>();

		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.AdditiveExpression", "+");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.SubtractiveExpression", "-");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.DivisiveExpression", "DIV");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.ModulusExpression", "MOD");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.EqualityExpression", "=");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.GreaterExpression", ">");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.GreaterOrEqualExpression", ">=");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.LessExpression", "<");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.NotExpression", "#");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.LessOrEqualExpression", "<=");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.LogicalConjunctiveExpression", "&");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.LogicalDisjunctiveExpression", "OR");
		operators.put("eu.wietsevenema.lang.oberon.ast.expressions.MultiplicativeExpression", "*");

		String className = exp.getClass().getCanonicalName();
		if (operators.containsKey(className)) {
			return "(" + this.dispatch(exp.getLeft()) + operators.get(className) + this.dispatch(exp.getRight()) + ")";
		} else {
			throw new IllegalStateException("Operator for binary expression " + className + " unknown.");
		}

	}

	public String visit(ArraySelector as) {
		return dispatch(as.getLeft()) + "[" + dispatch(as.getIndex()) + "]";
	}

	public String visit(LogicalNegationExpression lne) {
		return "(~" + dispatch(lne.getChild()) + ")";
	}

	public String visit(UnaryMinExpression un) {
		return "(-" + dispatch(un.getChild()) + ")";
	}

	public String visit(Declarations decls) {
		String result = "";
		
		
		result += joinNodes(decls.getConstants(), ";");
		result += joinNodes(decls.getTypes(), ";");
		result += joinNodes(decls.getVars(), ";");

				
		for (ProcedureDecl pd : decls.getProcedures()) {
			result += dispatch(pd);
		}
		return result;
	}

	public String visit(VarDecl vd) {
		String result = "VAR ";
		result += joinNodes(vd.getIdentifiers(), ",");
		result += ": " + vd.getType();
		return result;
	}
	
	public String visit(TypeDecl td) {
		String result = "TYPE " + td.getIdentifier() + "=" + td.getType() ;
		return result;
	}
	

	public String visit(ProcedureDecl pd) {
		String result = "PROCEDURE " + pd.getIdentifier().getName() + "(";
		result += joinNodes(pd.getFormalVars(), ";");
		result += ");";

		result += dispatch(pd.getDeclarations());

		result += joinNodes(pd.getStatements(), ";");

		return result;

	}

	public String visit(AssignmentStatement as) {
		return as.getIdentifier().getName() + ":=" + dispatch(as.getExpression());
	}

	public String visit(FormalVar fv) {
		return " " + fv.getIdentifier().getName() + " : " + fv.getType();
	}

	public String visit(FormalVarRef fv) {
		return "VAR " + fv.getIdentifier().getName() + " : " + fv.getType();
	}

	public String visit(Module m) {
		String result = "MODULE " + m.getIdentifier().getName() + ";";
		result += dispatch(m.getDeclarations());
		result += "BEGIN";
		result += joinNodes(m.getStats(), ";");
		result += "END " + m.getIdentifier().getName() + ".";
		return result;

	}

	public String visit(ProcedureCallStatement pcs) {
		String result = pcs.getIdentifier().getName();
		result += "(" + joinNodes(pcs.getParameters(), ",") + ")";
		return result;
	}

	private String joinNodes(List<? extends Node> ns, String delimiter) {
		if (ns.isEmpty()) {
			return "";
		}
		ArrayList<String> strings = new ArrayList<String>();
		for (Object n : ns) {
			strings.add((String) dispatch((Node) n));
		}
		return join(strings, delimiter);
	}

	public String visit(WhileStatement whileStat) {
		String result = "WHILE";
		result += dispatch(whileStat.getCondition());
		result += "DO";
		result += joinNodes(whileStat.getStatements(), ";");
		result += "END";
		return result;
	}

	private static String join(List<String> s, String delimiter) {
		if (s.isEmpty()) {
			return "";
		}
		Iterator<String> iter = s.iterator();
		StringBuffer buffer = new StringBuffer(iter.next());
		while (iter.hasNext())
			buffer.append(delimiter).append(iter.next());
		return buffer.toString();
	}

}
