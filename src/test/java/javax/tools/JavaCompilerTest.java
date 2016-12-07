package javax.tools;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.miriade.runtime.DefaultSpec;

/**
 * @author svaponi
 */
public class JavaCompilerTest {

	static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	static File root;
	static File sourceFile;

	@BeforeClass
	public static void setup() throws Exception {
		root = new File("target/generated-sources");
		sourceFile = new File(root, "tmp/Test.java");
		sourceFile.deleteOnExit();
		sourceFile.getParentFile().mkdirs();
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr--r--");
		Files.createFile(sourceFile.toPath(), PosixFilePermissions.asFileAttribute(perms));
	}

	@Test
	public void test() throws Exception {

		// Prepare source somehow.
		StringBuffer sourceCode = new StringBuffer();
		sourceCode.append("package tmp; \n");
		sourceCode.append("import " + DefaultSpec.class.getCanonicalName() + "; \n");
		sourceCode.append("public class Test { \n");
		sourceCode.append("    static int counter = 0; \n");
		sourceCode.append("    static String greeting = \"hello\"; \n");
		sourceCode.append("    static { \n");
		sourceCode.append("        print(++counter); \n");
		sourceCode.append("    } \n");
		sourceCode.append("    public Test() { \n");
		sourceCode.append("        print(++counter); \n");
		sourceCode.append("    } \n");
		sourceCode.append("    public String hello() { \n");
		sourceCode.append("        print(++counter); \n");
		sourceCode.append("        return greeting; \n");
		sourceCode.append("    } \n");
		sourceCode.append("    public static void print(Object content) { \n");
		sourceCode.append("        if (Boolean.getBoolean(DefaultSpec.DEBUG_MODE_SYSPROP)) \n");
		sourceCode.append("            System.out.println(content); \n");
		sourceCode.append("    } \n");
		sourceCode.append("} \n");

		// Save source in .java file.
		print(sourceCode.toString());
		Files.write(sourceFile.toPath(), sourceCode.toString().getBytes(StandardCharsets.UTF_8));

		// Compile source file.
		javac.run(null, null, null, sourceFile.getPath());

		// Load and instantiate compiled class.
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
		print("Should print \"1\"");
		Class<?> cls = Class.forName("tmp.Test", true, classLoader); // Should print "1".
		print("Should print \"2\"");
		Object testObj = cls.newInstance(); // Should print "2".
		Assert.assertNotNull(testObj);
		Method helloMethod = testObj.getClass().getMethod("hello");
		Assert.assertNotNull(helloMethod);
		print("Should print \"3\"");
		Object result = helloMethod.invoke(testObj); // Should print "3"
		Assert.assertNotNull(result);
		Assert.assertEquals("hello", result);
	}

	public static void print(String text) {
		if (Boolean.getBoolean(DefaultSpec.DEBUG_MODE_SYSPROP))
			System.out.println(text);
	}

}
