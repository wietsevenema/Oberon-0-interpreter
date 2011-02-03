package eu.wietsevenema.lang.oberon.interpreter;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;
import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;

public class BuiltIns {

	public static void inject(Environment environment) {
		SymbolTable symbolTable = environment.getSymbolTable();
		if(environment!=null){
			symbolTable.declareProc("Write", new Write(environment));
			symbolTable.declareProc("WriteLn", new WriteLn(environment));
		}
		symbolTable.declareProc("AssertEquals", new AssertEquals());
	}
	
	
	public static class AssertEquals implements Procedure {
		
		@Override
		public Identifier getIdentifier() {
			return new Identifier("AssertEquals");
		}

		@Override
		public List<Formal> getFormals() {
			ArrayList<Formal> formals = new ArrayList<Formal>();
			formals.add(new TypeLessFormal("expected"	) );
			formals.add(new TypeLessFormal("actual"		) );
			return formals;
		}

		@Override
		public void execute(SymbolTable symbolTable) {
			try {
				Value<?> expected 	= symbolTable.lookupValue("expected");
				Value<?> actual 	= symbolTable.lookupValue("actual");
				if (expected == null && actual == null) {
                    return;
				}
				if (expected != null && expected.getValue().equals(actual.getValue())) {
                    return;
				}
				throw new AssertionError("Expected "+expected.getValue() + " but got " + actual.getValue());
				
			} catch (VariableNotDeclaredException e) {
				throw new IllegalStateException(e);
			} catch (ValueUndefinedException e) {
				throw new IllegalStateException(e);
			}
			
		}

	}
	
	public static class WriteLn implements Procedure {
		private PrintWriter out;

		public WriteLn(Environment environment){
			this.out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(environment.getOut())));
		}
		
		@Override
		public Identifier getIdentifier() {
			return new Identifier("WriteLn");
		}

		@Override
		public List<Formal> getFormals() {
			ArrayList<Formal> formals = new ArrayList<Formal>();
			return formals;
		}

		@Override
		public void execute(SymbolTable symbolTable) {
			this.out.println();
		}

	}

	
	
	public static class Write implements Procedure {
		private PrintWriter out;

		public Write(Environment environment){
			this.out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(environment.getOut())));
		}
		
		@Override
		public Identifier getIdentifier() {
			return new Identifier("Write");
		}

		@Override
		public List<Formal> getFormals() {
			ArrayList<Formal> formals = new ArrayList<Formal>();
			formals.add(new TypeLessFormal(new Identifier("out")));
			return formals;
		}

		@Override
		public void execute(SymbolTable symbolTable) {
			try {
				Value<?> result = symbolTable.lookupValue("out");
				this.out.print(result.toString());
			} catch (VariableNotDeclaredException e) {
				throw new IllegalStateException(e);
			}
		}
		
	}
	
	
}

