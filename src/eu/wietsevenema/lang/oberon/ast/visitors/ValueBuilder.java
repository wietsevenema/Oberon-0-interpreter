package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.types.BooleanType;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.interpreter.Value;

public class ValueBuilder extends Visitor {
	
	public Value<Integer> visit( IntegerType integerType){
		return new Value<Integer>( null );
	}
	
	public Value<Boolean> visit( BooleanType booleanType){
		return new Value<Boolean>( null );
	}
	
}
