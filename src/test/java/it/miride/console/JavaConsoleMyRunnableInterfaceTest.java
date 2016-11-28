package it.miride.console;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.miriade.console.JavaConsole;
import it.miride.console.runnables.MyRunnableInterface;

/**
 * 
 * @author svaponi
 *
 */
public class JavaConsoleMyRunnableInterfaceTest {

	JavaConsole<MyRunnableInterface> consoleWithInterface = new JavaConsole<MyRunnableInterface>(MyRunnableInterface.class);

	@Test
	public void return_hello_string() {
		Object obj = consoleWithInterface.execute("return \"hello\";");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void return_inherited_static_fields() {
		Object obj = consoleWithInterface.execute("return first;");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("first", obj.toString());
	}

	@Test
	public void return_inherited_fields() {
		Object obj = consoleWithInterface.execute("return constant;");
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(Arrays.asList("").getClass(), obj.getClass()); // classe runtime java.util.Arrays$ArrayList
		Assert.assertEquals(Arrays.asList("first", "second", "third"), obj);
	}

}
