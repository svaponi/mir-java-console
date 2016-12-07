package it.miriade.runtime;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.core.runnables.MyAbstractProducer;
import it.miriade.runtime.core.runnables.MyProducer;
import it.miriade.runtime.core.runnables.Producer;

/**
 * @author svaponi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RuntimeUtilWithParentTest extends BaseTest {

	@Test
	public void t01_return_this() {
		Object obj = RuntimeUtil.compile(MyProducer.class, "return this;").run();
		Assert.assertNotNull(obj);
		Assert.assertTrue(Producer.class.isAssignableFrom(obj.getClass()));
		Assert.assertTrue(MyProducer.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(DefaultSpec.RUNTIME_CLASSNAME, obj.getClass().getSimpleName());
		/*
		 * ATTENZIONE: il package è valido solo se DefaultSpec.RUNTIME_PACKAGE
		 * esiste già nei sorgenti, altrimenti torna null
		 */
		Assert.assertEquals(DefaultSpec.RUNTIME_PACKAGE, obj.getClass().getPackage().getName());
	}

	@Test
	public void t02_return_hello_string() {
		Object obj = RuntimeUtil.compile(MyProducer.class, "return \"hello\";").run();
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void t03_return_inherited_static_fields() {
		Object obj = RuntimeUtil.compile(MyProducer.class, "return first;").run();
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("first", obj.toString());
	}

	@Test
	public void t04_return_inherited_fields() {
		Object obj = RuntimeUtil.compile(MyProducer.class, "return constant;").run();
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(Arrays.asList("").getClass(), obj.getClass()); // classe
																			 // runtime
																			 // java.util.Arrays$ArrayList
		Assert.assertEquals(Arrays.asList("first", "second", "third"), obj);
	}

	@Test
	public void t11_return_this() {
		Object obj = RuntimeUtil.compile(MyAbstractProducer.class, "return this;").run();
		Assert.assertNotNull(obj);
		Assert.assertTrue(Producer.class.isAssignableFrom(obj.getClass()));
		Assert.assertTrue(MyAbstractProducer.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(DefaultSpec.RUNTIME_CLASSNAME, obj.getClass().getSimpleName());
		/*
		 * ATTENZIONE: il package è valido solo se DefaultSpec.RUNTIME_PACKAGE
		 * esiste già nei sorgenti, altrimenti torna null
		 */
		Assert.assertEquals(DefaultSpec.RUNTIME_PACKAGE, obj.getClass().getPackage().getName());
	}

	@Test
	public void t12_return_hello_string() {
		Object obj = RuntimeUtil.compile(MyAbstractProducer.class, "return \"hello\";").run();
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void t13_return_inherited_static_fields() {
		Object obj = RuntimeUtil.compile(MyAbstractProducer.class, "return ordinal(1);").run();
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("1st", obj.toString());
	}

	@Test
	public void t14_return_inherited_fields() {
		Object obj = RuntimeUtil.compile(MyAbstractProducer.class, "return inheritedMethod();").run();
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(Arrays.asList("").getClass(), obj.getClass()); // classe
																			 // runtime
																			 // java.util.Arrays$ArrayList
		Assert.assertEquals(Arrays.asList("first", "second", "third"), obj);
	}

}
