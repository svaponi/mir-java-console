package it.miriade.console;

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

import it.miriade.runtime.JavaResultBuilder;
import it.miriade.runtime.core.runnables.MyRunnableInterface;

/**
 * 
 * @author svaponi
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaConsoleWithSuperClassTest {

	JavaResultBuilder consoleWithInterfaceAsParent = new JavaResultBuilder(MyRunnableInterface.class);

	// file scritto dai test
	static final String pathname = "target/runtime.txt";

	@Test
	public void write_on_file() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("void/source1").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		consoleWithInterfaceAsParent.run(sourceCode, Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyRunnableInterface.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnableInterface.constant.indexOf(nth)));
		file.delete();
	}

	@Test
	public void write_on_file_2() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("void/source2").getPath());
		String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
		consoleWithInterfaceAsParent.run(sourceCode, Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());
		for (String nth : MyRunnableInterface.constant)
			Assert.assertEquals("The " + nth + " line", lines.get(MyRunnableInterface.constant.indexOf(nth)));
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
