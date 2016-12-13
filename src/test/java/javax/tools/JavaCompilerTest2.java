package javax.tools;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.miriade.runtime.DefaultSpec;

/**
 * @author svaponi
 */
public class JavaCompilerTest2 {

	static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	static File root;
	static File sourceFile;

	@BeforeClass
	public static void setup() throws Exception {
		root = new File("target/generated-sources");
		sourceFile = new File(root, "tmp/Test2.java");
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
		sourceCode.append("import " + List.class.getCanonicalName() + "; \n");
		sourceCode.append("import " + Arrays.class.getCanonicalName() + "; \n");
		sourceCode.append("import " + Comparator.class.getCanonicalName() + "; \n");
		sourceCode.append("import " + Collections.class.getCanonicalName() + "; \n");
		sourceCode.append("public class Test2 { \n");
		sourceCode.append("    StringBuffer buf = new StringBuffer(); \n");
		sourceCode.append("    public void println(Object s) { \n");
		sourceCode.append("        buf.append(s).append(\"\\n\"); \n");
		sourceCode.append("    } \n");
		sourceCode.append("    public StringBuffer buf() { \n");
		sourceCode.append("        return buf; \n");
		sourceCode.append("    } \n");
		sourceCode.append("    public Test2() { \n");
		sourceCode.append("        List<Package> list = Arrays.asList(Package.getPackages()); \n");
		sourceCode.append("        Collections.sort(list, new Comparator<Package>() { \n");
		sourceCode.append("            @Override \n");
		sourceCode.append("            public int compare(Package o1, Package o2) { \n");
		sourceCode.append("                return o1.getName().compareTo(o2.getName()); \n");
		sourceCode.append("            } \n");
		sourceCode.append("        }); \n");
		sourceCode.append("        list.forEach(x -> println(x)); \n");
		sourceCode.append("    } \n");
		sourceCode.append("} \n");

		// Save source in .java file.
		print(sourceCode.toString());
		Files.write(sourceFile.toPath(), sourceCode.toString().getBytes(StandardCharsets.UTF_8));

		// Compile source file.
		javac.run(null, null, null, sourceFile.getPath());

		// Load and instantiate compiled class.
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
		Class<?> cls = Class.forName("tmp.Test2", true, classLoader);
		Object testObj = cls.newInstance(); // Should print packages
		Assert.assertNotNull(testObj);
		Method bufMethod = testObj.getClass().getMethod("buf");
		Assert.assertNotNull(bufMethod);
		Object result = bufMethod.invoke(testObj);
		Assert.assertNotNull(result);

		System.out.println("Print all packages in classpath:");
		System.out.println(result.toString());
	}

	public static void print(String text) {
		if (Boolean.getBoolean(DefaultSpec.DEBUG_MODE_SYSPROP))
			System.out.println(text);
	}

}
