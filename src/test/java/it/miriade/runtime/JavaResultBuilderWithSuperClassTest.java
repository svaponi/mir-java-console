package it.miriade.runtime;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.core.runnables.MyAbstractRunnableWithResult;
import it.miriade.runtime.core.runnables.MyRunnableInterfaceWithResult;
import it.miriade.runtime.core.runnables.RunnableWithResult;

/**
 * @author svaponi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaResultBuilderWithSuperClassTest {

	JavaResultBuilder builderWithInterfaceAsParent = new JavaResultBuilder(MyRunnableInterfaceWithResult.class);

	@Test
	public void t01_return_this() {
		Object obj = builderWithInterfaceAsParent.runAndGetResult("return this;");
		Assert.assertNotNull(obj);
		Assert.assertTrue(RunnableWithResult.class.isAssignableFrom(obj.getClass()));
		Assert.assertTrue(MyRunnableInterfaceWithResult.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(DefaultSpec.RUNTIME_CLASSNAME, obj.getClass().getSimpleName());
		/*
		 * ATTENZIONE: il package è valido solo se DefaultSpec.RUNTIME_PACKAGE
		 * esiste già nei sorgenti, altrimenti torna null
		 */
		Assert.assertEquals(DefaultSpec.RUNTIME_PACKAGE, obj.getClass().getPackage().getName());
	}

	@Test
	public void t02_return_hello_string() {
		Object obj = builderWithInterfaceAsParent.runAndGetResult("return \"hello\";");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void t03_return_inherited_static_fields() {
		Object obj = builderWithInterfaceAsParent.runAndGetResult("return first;");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("first", obj.toString());
	}

	@Test
	public void t04_return_inherited_fields() {
		Object obj = builderWithInterfaceAsParent.runAndGetResult("return constant;");
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(Arrays.asList("").getClass(), obj.getClass()); // classe
																			 // runtime
																			 // java.util.Arrays$ArrayList
		Assert.assertEquals(Arrays.asList("first", "second", "third"), obj);
	}

	JavaResultBuilder builderWithAbstractAsParent = new JavaResultBuilder(MyAbstractRunnableWithResult.class);

	@Test
	public void t11_return_this() {
		Object obj = builderWithAbstractAsParent.runAndGetResult("return this;");
		Assert.assertNotNull(obj);
		Assert.assertTrue(RunnableWithResult.class.isAssignableFrom(obj.getClass()));
		Assert.assertTrue(MyAbstractRunnableWithResult.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(DefaultSpec.RUNTIME_CLASSNAME, obj.getClass().getSimpleName());
		/*
		 * ATTENZIONE: il package è valido solo se DefaultSpec.RUNTIME_PACKAGE
		 * esiste già nei sorgenti, altrimenti torna null
		 */
		Assert.assertEquals(DefaultSpec.RUNTIME_PACKAGE, obj.getClass().getPackage().getName());
	}

	@Test
	public void t12_return_hello_string() {
		Object obj = builderWithAbstractAsParent.runAndGetResult("return \"hello\";");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void t13_return_inherited_static_fields() {
		Object obj = builderWithAbstractAsParent.runAndGetResult("return first;");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("first", obj.toString());
	}

	@Test
	public void t14_return_inherited_fields() {
		Object obj = builderWithAbstractAsParent.runAndGetResult("return customMethod();");
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(Arrays.asList("").getClass(), obj.getClass()); // classe
																			 // runtime
																			 // java.util.Arrays$ArrayList
		Assert.assertEquals(Arrays.asList("first", "second", "third"), obj);
	}
}
