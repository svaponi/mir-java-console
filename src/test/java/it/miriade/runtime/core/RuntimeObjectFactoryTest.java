package it.miriade.runtime.core;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RuntimeObjectFactoryTest extends BaseTest {

	private RuntimeObjectFactory<Object> factory;
	private StringBuffer sourceCode = new StringBuffer();

	@Test
	public void t_00_build_demo() {
		sourceCode.append("package test;").append('\n');
		sourceCode.append("public class Demo {").append('\n');
		sourceCode.append("	private String s;").append('\n');
		sourceCode.append("	private int i;").append('\n');
		sourceCode.append("	public String toString() {").append('\n');
		sourceCode.append("		return s + i;").append('\n');
		sourceCode.append("	}").append('\n');
		sourceCode.append("}").append('\n');

		factory = new RuntimeObjectFactory<Object>("test.Demo", sourceCode.toString());
		Assert.assertNotNull(factory);
		Object t0 = factory.newInstance();
		Assert.assertEquals("test.Demo", t0.getClass().getName());
		Assert.assertEquals("null0", t0.toString());
	}

	@Test
	public void t_01_build_demo_with_no_args() {
		sourceCode.append("package test;").append('\n');
		sourceCode.append("public class Demo1 {").append('\n');
		sourceCode.append("	private String s;").append('\n');
		sourceCode.append("	private int i;").append('\n');
		sourceCode.append("	public Demo1() {").append('\n');
		sourceCode.append("		this.s = \"unknow\";").append('\n');
		sourceCode.append("		this.i = 0;").append('\n');
		sourceCode.append("	}").append('\n');
		sourceCode.append("	public String toString() {").append('\n');
		sourceCode.append("		return s + i;").append('\n');
		sourceCode.append("	}").append('\n');
		sourceCode.append("}").append('\n');

		factory = new RuntimeObjectFactory<Object>("test.Demo1", sourceCode.toString());
		Assert.assertNotNull(factory);
		Object t0 = factory.newInstance();
		Assert.assertEquals("test.Demo1", t0.getClass().getName());
		Assert.assertEquals("unknow0", t0.toString());
	}

	@Test
	public void t_02_build_demo_with_args() {
		sourceCode.append("package test;").append('\n');
		sourceCode.append("public class Demo2 {").append('\n');
		sourceCode.append("	private String s;").append('\n');
		sourceCode.append("	private int i;").append('\n');
		sourceCode.append("	public Demo2(String s, int i) {").append('\n');
		sourceCode.append("		this.s = s;").append('\n');
		sourceCode.append("		this.i = i;").append('\n');
		sourceCode.append("	}").append('\n');
		sourceCode.append("	public String toString() {").append('\n');
		sourceCode.append("		return s + i;").append('\n');
		sourceCode.append("	}").append('\n');
		sourceCode.append("}").append('\n');

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
