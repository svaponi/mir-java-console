package it.miriade.runtime.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import it.miriade.runtime.BaseTest;

/**
 * @author svaponi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaConsoleBugVer130Test extends BaseTest {

	// public static abstract class Test130 implements Runnable {
	// StringBuffer buf = new StringBuffer();
	//
	// public void println(Object s) {
	// buf.append(s).append("\n");
	// System.out.println(s);
	// }
	// }

	// JavaConsole console = new JavaConsole(Test130.class);
	JavaConsole console = new JavaConsole();

	// TODO: capire perchè non va!!
	// @Test
	public void bug_version_1_3_0() throws IOException {

		File sourceFile = new File(ClassLoader.getSystemResource("runnable/source5").getPath());
		InputStream is = new FileInputStream(sourceFile);
		console.changeInput(is);
		console.start(Arrays.asList(List.class.getPackage()));
	}
}
