package it.miriade.runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.DefaultSpec;
import it.miriade.runtime.JavaResultBuilder;
import it.miriade.runtime.core.runnables.RunnableWithResult;

/**
 * 
 * @author svaponi
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaResultBuilderTest {

	JavaResultBuilder console = new JavaResultBuilder();

	@Test
	public void t_00_return_this() {
		Object obj = console.runAndGetResult("return this;");
		Assert.assertNotNull(obj);
		Assert.assertTrue(RunnableWithResult.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(DefaultSpec.RUNTIME_CLASSNAME, obj.getClass().getSimpleName());
		/*
		 * ATTENZIONE: il package è valido solo se DefaultSpec.RUNTIME_PACKAGE
		 * esiste già nei sorgenti, altrimenti torna null
		 */
		Assert.assertEquals(DefaultSpec.RUNTIME_PACKAGE, obj.getClass().getPackage().getName());
	}		
	
	@Test
	public void t_10_return_hello_string() {
		Object obj = console.runAndGetResult("return \"hello\";");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void t_11_return_Math_random() {
		Object obj = console.runAndGetResult("return Math.random();");
		Assert.assertNotNull(obj);
		Assert.assertEquals(Double.class, obj.getClass());
		Assert.assertTrue(((Double) obj) >= 0);
		Assert.assertTrue(((Double) obj) <= 1);
	}

	@Test
	public void t_12_return_list_of_int() {

		StringBuffer sourceCodeBuf = new StringBuffer();
		sourceCodeBuf.append("List<Object> list = new ArrayList<>();");
		sourceCodeBuf.append("for(int i = 0; i < 99999; i++) {");
		sourceCodeBuf.append("	list.add(i);");
		sourceCodeBuf.append("}");
		sourceCodeBuf.append("return list;");

		Object obj = console.runAndReturn(sourceCodeBuf.toString(), Arrays.asList(List.class, ArrayList.class));
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(ArrayList.class, obj.getClass());

		for (int i = 0; i < 99999; i++)
			Assert.assertEquals(i, ((List<?>) obj).get(i));

	}

	@Test
	public void t_20_write_on_file() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("return/source1").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		Object obj = console.runAndReturn(sourceCode, Arrays.asList(File.class, PrintWriter.class));
		Assert.assertNotNull(obj);
		Assert.assertEquals(File.class, obj.getClass());
		File file = (File) obj;
		Assert.assertTrue(file.exists());

		List<String> lines = Files.readAllLines(file.toPath());
		Assert.assertEquals("The first line", lines.get(0));
		Assert.assertEquals("The second line", lines.get(1));
		Assert.assertEquals("The third line", lines.get(2));
		file.delete();
	}

	@Test
	public void t_21_write_on_file() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("return/source2").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		Object obj = console.runAndReturn(sourceCode, Arrays.asList(File.class, PrintWriter.class));
		Assert.assertNotNull(obj);
		Assert.assertEquals(File.class, obj.getClass());
		File file = (File) obj;
		Assert.assertTrue(file.exists());

		List<String> lines = Files.readAllLines(file.toPath());
		Assert.assertEquals("The first line", lines.get(0));
		Assert.assertEquals("The second line", lines.get(1));
		Assert.assertEquals("The third line", lines.get(2));
		for (int i = 4; i < Math.pow(10, 6); i++) {
			if (i % 10 == 1)
				Assert.assertEquals("The " + i + "st line", lines.get(i - 1));
			else if (i % 10 == 2)
				Assert.assertEquals("The " + i + "nd line", lines.get(i - 1));
			else if (i % 10 == 3)
				Assert.assertEquals("The " + i + "rd line", lines.get(i - 1));
			else
				Assert.assertEquals("The " + i + "th line", lines.get(i - 1));
		}
		file.delete();
	}

}
