package eu.wietsevenema.lang.oberon.ast.visitors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.declarations.Module;
import eu.wietsevenema.lang.oberon.ast.expressions.BinaryExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalNegationExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.TestExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.UnaryMinExpression;
import eu.wietsevenema.lang.oberon.ast.statements.ProcedureCallStatement;
import eu.wietsevenema.lang.oberon.ast.statements.Statement;

/**
 * Deze moduleprinter is (nog) niet voor productiedoeleinden, alleen 
 * voor unittests. Het zou mooi zijn als hier parsebare Oberon-0 uit
 * kwam, want het is mooi als print(parse(x))==x.  
 * @author Wietse Venema
 *
 */
public class ModulePrinter extends Visitor {
	
	public String visit(BinaryExpression binexp) {
		return "("
				+dispatch(binexp.getLeft())
				+binexp.getToken()
				+dispatch(binexp.getRight())
				+")"
				;
	}
	
	public String visit(TestExpression exp){
		return (String) dispatch(exp.getChild());
	}
	
	public String visit(Expression exp){
		return exp.toString();
	}
	
	public String visit(LogicalNegationExpression lne){
		return "(~"+dispatch(lne.getChild())+")";
	}
	
	public String visit(UnaryMinExpression un){
		return "(-"+dispatch(un.getChild())+")";
	}
	
	public String visit(Module m){
		String result = "MODULE "+m.getIdentifier().getName()+";";
		//TODO Declarations
		result += "BEGIN";
		ArrayList<String> stats = new ArrayList<String>();
		for(Statement s : m.getStats()){
			stats.add((String)dispatch(s));
		}
		result += join(stats, ";");
		result += "END "+m.getIdentifier().getName()+".";
		return result;
		
	}
	
	public String visit( ProcedureCallStatement pcs){
		String result = pcs.getIdentifier().getName();
		ArrayList<String> exps = new ArrayList<String>(); 
		for(Expression e: pcs.getParameters())
		{		
			exps.add((String)dispatch(e));
		}
		result += "(" + join(exps, ",") + ")";
		return result;
	}
	
	
	private static String join(List<String> s, String delimiter) {
	    if (s.isEmpty()) {
	    	return "";
	    }
	    Iterator<String> iter = s.iterator();
	    StringBuffer buffer = new StringBuffer(iter.next());
	    while (iter.hasNext()) buffer.append(delimiter).append(iter.next());
	    return buffer.toString();
	}
	
}
