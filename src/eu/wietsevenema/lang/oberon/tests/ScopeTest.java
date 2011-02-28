package eu.wietsevenema.lang.oberon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import eu.wietsevenema.lang.oberon.exceptions.ValueUndefinedException;
import eu.wietsevenema.lang.oberon.exceptions.VariableAlreadyDeclaredException;
import eu.wietsevenema.lang.oberon.exceptions.VariableNotDeclaredException;
import eu.wietsevenema.lang.oberon.interpreter.Scope;
import eu.wietsevenema.lang.oberon.interpreter.ValueReference;
import eu.wietsevenema.lang.oberon.interpreter.values.BooleanValue;
import eu.wietsevenema.lang.oberon.interpreter.values.IntegerValue;

public class ScopeTest {

	Scope scope;

	@Before
	public void setUp() throws Exception {
		scope = new Scope();
		scope.declareValue("bool2", new BooleanValue(true));
		scope = new Scope(scope);
		scope.declareValue("int2", new IntegerValue(-1));

	}

	@Test
	public void testLookup() throws VariableNotDeclaredException {
		assertNotNull(scope.lookupValue("int2"));
		assertNotNull(scope.lookupValue("bool2"));
	}

	@Test
	public void testValueReference() throws VariableNotDeclaredException {
		ValueReference referenceBool2 = scope.lookupValueReference("bool2");
		assertNotNull(referenceBool2);
		scope.declareValueReference("reference-bool2", referenceBool2);
		assertEquals(scope.lookupValueReference("reference-bool2"), referenceBool2);
		assertEquals(scope.lookupValue("bool2"), scope.lookupValue("reference-bool2"));
	}

	@Test(expected = VariableAlreadyDeclaredException.class)
	public void testDoubleDeclareFails() throws VariableAlreadyDeclaredException {
		scope.declareValue("c", new BooleanValue(null));
		scope.declareValue("c", new BooleanValue(null));
	}

	@Test
	public void testDoubleDeclareScoped() throws VariableAlreadyDeclaredException, VariableNotDeclaredException {
		scope.declareValue("c", new BooleanValue(true));
		scope = new Scope(scope);
		scope.declareValue("c", new BooleanValue(false));
	}

	@Test(expected = ValueUndefinedException.class)
	public void testValueUndefined() throws VariableAlreadyDeclaredException, VariableNotDeclaredException,
			ValueUndefinedException {
		scope.declareValue("tve.a", new IntegerValue(null));
		((IntegerValue) scope.lookupValue("tve.a")).getValue();
	}

}