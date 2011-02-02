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
import eu.wietsevenema.lang.oberon.ast.expressions.UnaryMinExpression;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableUndefinedException;
import eu.wietsevenema.lang.oberon.interpreter.SymbolTable;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class ExpressionEvaluator extends Visitor {

	SymbolTable symboltable;

	public ExpressionEvaluator(SymbolTable symbolTable) {
		this.symboltable = symbolTable;
	}

	
	public Value<Integer> visit(AdditiveExpression ae) throws ClassCastException, ValueUndefinedException {
		Integer left 	= ((Value<Integer>) dispatch(ae.getLeft()	)).getValue();
		Integer right 	= ((Value<Integer>) dispatch(ae.getRight()	)).getValue();
				
		if (ae.getToken() == "+") {
			return new Value<Integer>(left + right);
		} else if (ae.getToken() == "-") {
			return new Value<Integer>(left - right);
		} else {
			throw new IllegalStateException("Token not recognized");
		}
	}

	public Value<Boolean> visit(BooleanConstant bc) {
		return new Value<Boolean>(bc.getValue());
	}

	public Value<Boolean> visit(EqualityExpression ee) throws ValueUndefinedException {
		Integer left 	= ((Value<Integer>) dispatch(ee.getLeft()	)).getValue();
		Integer right 	= ((Value<Integer>) dispatch(ee.getRight())).getValue();
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
		return new Value<Boolean>(result);
	}

	//FIXME lazy eval
	public Value<Boolean> visit(LogicalConjunctiveExpression lce) throws ValueUndefinedException {
		Boolean left 	= ((Value<Boolean>) dispatch(lce.getLeft()	)).getValue();
		Boolean right 	= ((Value<Boolean>) dispatch(lce.getRight()	)).getValue();
		return new Value<Boolean>(left && right);
	}

	//FIXME lazy eval
	public Value<Boolean> visit(LogicalDisjunctiveExpression lde) throws ValueUndefinedException {
		Boolean left 	= ((Value<Boolean>) dispatch(lde.getLeft()	)).getValue();
		Boolean right 	= ((Value<Boolean>) dispatch(lde.getRight()	)).getValue();
		return new Value<Boolean>(left || right);
	}

	public Value<Boolean> visit(LogicalNegationExpression lne) throws ValueUndefinedException {
		Expression exp = lne.getChild();
		Boolean value = ((Value<Boolean>) dispatch(exp)).getValue();
		return new Value<Boolean>(!value);
	}

	public Value<Integer> visit(MultiplicativeExpression me) throws ValueUndefinedException {
		Integer left 	= ((Value<Integer>) dispatch(me.getLeft()	)).getValue();
		Integer right 	= ((Value<Integer>) dispatch(me.getRight())).getValue();

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
		return new Value<Integer>(result);
	}

	public Value<Integer> visit(UnaryMinExpression um) {
		Value<Integer> result = (Value<Integer>) dispatch(um.getChild());
		return result;
	}

	public Value<Integer> visit(IntegerConstant ic) {
		return new Value<Integer>(ic.getValue());
	}

	public Value<?> visit(Identifier id) throws VariableUndefinedException, VariableNotDeclaredException {
		Value<?> result = (Value<?>) this.symboltable.lookupValue(id.getName());
		
		if (result == null) {
			throw new VariableUndefinedException(id.getName() + "undefined.");
		}
		return result;
	}
	
//	private Boolean getBoolean( Value<?> value){
//		
//	}

}
