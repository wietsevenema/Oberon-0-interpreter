package eu.wietsevenema.lang.oberon.ast.visitors;

import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.types.BooleanType;
import eu.wietsevenema.lang.oberon.ast.types.IntegerType;
import eu.wietsevenema.lang.oberon.interpreter.values.BooleanValue;
import eu.wietsevenema.lang.oberon.interpreter.values.IntegerValue;

public class ValueBuilder extends Visitor {
	
	public IntegerValue visit( IntegerType integerType){
		return new IntegerValue( null );
	}
	
	public BooleanValue visit( BooleanType booleanType){
		return new BooleanValue( null );
	}
	
}
