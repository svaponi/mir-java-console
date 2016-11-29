package it.miriade.runtime.core;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.core.RuntimeObjectFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RuntimeObjectFactoryTest {

	private RuntimeObjectFactory<Object> factory;
	private StringBuffer sourceCode = new StringBuffer();

	@Test
	public void t_00_build_demo() {
		sourceCode.append("package test;");
		sourceCode.append("public class Demo {");
		sourceCode.append("	private String s;");
		sourceCode.append("	private int i;");
		sourceCode.append("	public String toString() {");
		sourceCode.append("		return s + i;");
		sourceCode.append("	}");
		sourceCode.append("}");

		factory = new RuntimeObjectFactory<Object>("test.Demo", sourceCode.toString());
		Assert.assertNotNull(factory);
		Object t0 = factory.newInstance();
		Assert.assertEquals("test.Demo", t0.getClass().getName());
		Assert.assertEquals("null0", t0.toString());
	}

	@Test
	public void t_01_build_demo_with_no_args() {
		sourceCode.append("package test;");
		sourceCode.append("public class Demo1 {");
		sourceCode.append("	private String s;");
		sourceCode.append("	private int i;");
		sourceCode.append("	public Demo1() {");
		sourceCode.append("		this.s = \"unknow\";");
		sourceCode.append("		this.i = 0;");
		sourceCode.append("	}");
		sourceCode.append("	public String toString() {");
		sourceCode.append("		return s + i;");
		sourceCode.append("	}");
		sourceCode.append("}");

		factory = new RuntimeObjectFactory<Object>("test.Demo1", sourceCode.toString());
		Assert.assertNotNull(factory);
		Object t0 = factory.newInstance();
		Assert.assertEquals("test.Demo1", t0.getClass().getName());
		Assert.assertEquals("unknow0", t0.toString());
	}
	

	@Test
	public void t_02_build_demo_with_args() {
		sourceCode.append("package test;");
		sourceCode.append("public class Demo2 {");
		sourceCode.append("	private String s;");
		sourceCode.append("	private int i;");
		sourceCode.append("	public Demo2(String s, int i) {");
		sourceCode.append("		this.s = s;");
		sourceCode.append("		this.i = i;");
		sourceCode.append("	}");
		sourceCode.append("	public String toString() {");
		sourceCode.append("		return s + i;");
		sourceCode.append("	}");
		sourceCode.append("}");

		factory = new RuntimeObjectFactory<Object>("test.Demo2", sourceCode.toString());
		Assert.assertNotNull(factory);
		Object t1 = factory.newInstance("test", 1);
		Assert.assertEquals("test.Demo2", t1.getClass().getName());
		Assert.assertEquals("test1", t1.toString());
		Object t2 = factory.newInstance("example", 2);
		Assert.assertEquals("test.Demo2", t2.getClass().getName());
		Assert.assertEquals("example2", t2.toString());
		Object t3 = factory.newInstance("foo", 3);
		Assert.assertEquals("test.Demo2", t3.getClass().getName());
		Assert.assertEquals("foo3", t3.toString());
	}
}
