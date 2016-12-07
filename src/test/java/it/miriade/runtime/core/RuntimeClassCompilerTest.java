package it.miriade.runtime.core;

import org.junit.Assert;
import org.junit.Test;

public class RuntimeClassCompilerTest {

	@Test
	public void compile() throws Exception {

		StringBuffer sourceCode = new StringBuffer();
		sourceCode.append("package it.miriade.runtime;\n");
		sourceCode.append("public class MyClass {\n");
		sourceCode.append("   public void method() {}");
		sourceCode.append("}");

		Class<?> myClass = RuntimeClassCompiler.compile("it.miriade.runtime.MyClass", sourceCode.toString());
		Assert.assertNotNull(myClass);
		Assert.assertEquals("it.miriade.runtime.MyClass", myClass.getName());
		Assert.assertNotNull(myClass.getDeclaredMethod("method"));
	}
}
