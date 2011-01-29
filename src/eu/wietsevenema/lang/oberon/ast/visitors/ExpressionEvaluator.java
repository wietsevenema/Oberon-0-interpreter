package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.AdditiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.BooleanConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.EqualityExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.ast.expressions.IntegerConstant;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalConjunctiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalDisjunctiveExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.LogicalNegationExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.MultiplicativeExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.UnaryMin;
import eu.wietsevenema.lang.oberon.exceptions.VariableUndefinedException;
import eu.wietsevenema.lang.oberon.interpreter.BooleanValue;
import eu.wietsevenema.lang.oberon.interpreter.IntegerValue;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class ExpressionEvaluator extends Visitor {

	SymbolTable symboltable;

	public ExpressionEvaluator(SymbolTable symboltable) {
		this.symboltable = symboltable;
	}

	public IntegerValue visit(AdditiveExpression ae) throws ClassCastException {
		Integer left 	= ((IntegerValue) dispatch(ae.getLeft()	)).getValue();
		Integer right 	= ((IntegerValue) dispatch(ae.getRight())).getValue();

		if (ae.getToken() == "+") {
			return new IntegerValue(left + right);
		} else if (ae.getToken() == "-") {
			return new IntegerValue(left - right);
		} else {
			throw new IllegalStateException("Token not recognized");
		}
	}

	public BooleanValue visit(BooleanConstant bc) {
		return new BooleanValue(bc.getValue());
	}

	public BooleanValue visit(EqualityExpression ee) {
		Integer left 	= ((IntegerValue) dispatch(ee.getLeft()	)).getValue();
		Integer right 	= ((IntegerValue) dispatch(ee.getRight())).getValue();
		boolean result;
		if(ee.getToken() == "="){
			result = left == right;
		} else if (ee.getToken() == "#"){
			result = left != right;
		} else if (ee.getToken() == "<"){
			result = left < right;
		} else if (ee.getToken() == "<="){
			result = left <= right;
		} else if (ee.getToken() == ">"){
			result = left > right;
		} else if (ee.getToken() == ">="){
			result = left >= right;
		} else {
			throw new IllegalStateException("Token not recognized");
		}
		return new BooleanValue(result);
	}

	public BooleanValue visit(LogicalConjunctiveExpression lce) {
		Boolean left 	= ((BooleanValue) dispatch(lce.getLeft()	)).getValue();
		Boolean right 	= ((BooleanValue) dispatch(lce.getRight()	)).getValue();
		return new BooleanValue(left && right);
	}

	public BooleanValue visit(LogicalDisjunctiveExpression lde) {
		Boolean left 	= ((BooleanValue) dispatch(lde.getLeft()	)).getValue();
		Boolean right 	= ((BooleanValue) dispatch(lde.getRight()	)).getValue();
		return new BooleanValue(left || right);
	}

	public BooleanValue visit(LogicalNegationExpression lne) {
		Expression exp = lne.getChild();
		Boolean value = ((BooleanValue) dispatch(exp)).getValue();
		return new BooleanValue(!value);
	}

	public IntegerValue visit(MultiplicativeExpression me) {
		Integer left 	= ((IntegerValue) dispatch(me.getLeft()	)).getValue();
		Integer right 	= ((IntegerValue) dispatch(me.getRight())).getValue();

		Integer result; 
		if (me.getToken() == "*") {
			result = left * right; 
		} else if (me.getToken() == "DIV") {
			result = (int)( left / right );
		} else if (me.getToken() == "MOD") {
			result = (int)(left % right );
		} else {
			throw new IllegalStateException("Token not recognized");
		}
		return new IntegerValue(result);
	}

	public IntegerValue visit(UnaryMin um) {
		IntegerValue result = (IntegerValue) dispatch(um.getChild());
		return result;
	}

	public IntegerValue visit(IntegerConstant ic) {
		return new IntegerValue(ic.getValue());
	}

	public Value visit(Identifier id) throws VariableUndefinedException {
		IntegerValue result = (IntegerValue) this.symboltable.lookupValue(id
				.getName());
		if (result == null) {
			throw new VariableUndefinedException(id.getName() + "undefined.");
		}
		return result;
	}

}
