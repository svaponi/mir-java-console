package it.miriade.runtime.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

/**
 * @author svaponi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaConsoleWithFileInputStreamTest extends BaseTest {

	JavaConsole console = new JavaConsole();

	// file scritto dai test
	static final String pathname = "target/runtime.txt";

	@Test
	public void write_on_file_from_is() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source1").getPath());
		InputStream is = new FileInputStream(sourceFile);
		console.changeInput(is);
		console.start(Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
		Assert.assertTrue(file.exists());
		List<String> lines = Files.readAllLines(file.toPath());

		Assert.assertEquals("The first line", lines.get(0));
		Assert.assertEquals("The second line", lines.get(1));
		Assert.assertEquals("The third line", lines.get(2));
		file.delete();
	}

	@Test
	public void write_on_file_2_from_is() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source2").getPath());
		InputStream is = new FileInputStream(sourceFile);
		console.changeInput(is);
		console.start(Arrays.asList(File.class, PrintWriter.class));

		File file = (File) new File(pathname);
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
