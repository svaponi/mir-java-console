package it.miride.console;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.miriade.console.JavaSimpleConsole;

/**
 * 
 * @author svaponi
 *
 */
public class JavaSimpleConsoleTest {

	JavaSimpleConsole console = new JavaSimpleConsole();

	@Test
	public void return_hello_string() {
		Object obj = console.execute("return \"hello\";");
		Assert.assertNotNull(obj);
		Assert.assertEquals(String.class, obj.getClass());
		Assert.assertEquals("hello", obj.toString());
	}

	@Test
	public void return_Math_random() {
		Object obj = console.execute("return Math.random();");
		Assert.assertNotNull(obj);
		Assert.assertEquals(Double.class, obj.getClass());
		Assert.assertTrue(((Double) obj) >= 0);
		Assert.assertTrue(((Double) obj) <= 1);
	}

	@Test
	public void return_list_of_int() {

		StringBuffer sourceCodeBuf = new StringBuffer();
		sourceCodeBuf.append("List<Object> list = new ArrayList<>();");
		sourceCodeBuf.append("for(int i = 0; i < 99999; i++) {");
		sourceCodeBuf.append("	list.add(i);");
		sourceCodeBuf.append("}");
		sourceCodeBuf.append("return list;");

		Object obj = console.execute(sourceCodeBuf.toString(), Arrays.asList(List.class, ArrayList.class));
		Assert.assertNotNull(obj);
		Assert.assertTrue(List.class.isAssignableFrom(obj.getClass()));
		Assert.assertEquals(ArrayList.class, obj.getClass());

		for (int i = 0; i < 99999; i++)
			Assert.assertEquals(i, ((List<?>) obj).get(i));

	}

	@Test
	public void write_on_file() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("sources/source1").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		Object obj = console.execute(sourceCode, Arrays.asList(File.class, PrintWriter.class));
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
	public void write_on_file_2() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("sources/source2").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		Object obj = console.execute(sourceCode, Arrays.asList(File.class, PrintWriter.class));
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
