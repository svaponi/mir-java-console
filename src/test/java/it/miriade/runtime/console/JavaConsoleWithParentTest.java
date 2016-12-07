package it.miriade.runtime.console;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.BaseTest;
import it.miriade.runtime.console.JavaConsole;
import it.miriade.runtime.core.runnables.MyAbstractRunnable;
import it.miriade.runtime.core.runnables.MyRunnable;

/**
 * @author svaponi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaConsoleWithParentTest extends BaseTest {

	// file scritto dai test
	static final String pathname = "target/runtime.txt";

	JavaConsole console = new JavaConsole(MyRunnable.class);

	@Test
	public void write_on_file_1() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source1").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		console.run(sourceCode, Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyRunnable.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnable.constant.indexOf(nth)));
		file.delete();
	}

	@Test
	public void write_on_file_2() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source2").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		console.run(sourceCode, File.class, PrintWriter.class);

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyRunnable.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnable.constant.indexOf(nth)));
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

	JavaConsole consoleAbstract = new JavaConsole(MyAbstractRunnable.class);

	@Test
	public void write_on_file_3() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source3").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		consoleAbstract.run(sourceCode, Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyAbstractRunnable.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnable.constant.indexOf(nth)));
		file.delete();
	}

	@Test
	public void write_on_file_4() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source4").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		consoleAbstract.run(sourceCode, File.class, PrintWriter.class);

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyAbstractRunnable.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnable.constant.indexOf(nth)));
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
