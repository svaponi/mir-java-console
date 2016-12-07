package it.miriade.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import it.miriade.runtime.DefaultSpec;
import it.miriade.runtime.core.ex.RuntimeCompilationException;

/**
 * Questa classe genera classi a runtime compilando il codice sorgente in
 * ingresso.
 * 
 * @author svaponi
 */
public class RuntimeClassCompiler {

	private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	public static <T> T build(RuntimeClassDefinition def, Object... ctorArgs) {
		return build(def.getQualifiedClassName(), def.getSourceCode(), ctorArgs);
	}

	@SuppressWarnings("unchecked")
	public static <T> T build(String qualifiedClassName, String sourceCode, Object... ctorArgs) {
		try {
			if (Boolean.parseBoolean(System.getProperty(DefaultSpec.DEBUG_MODE_SYSPROP, "false")))
				System.out.printf("\nSource code\n-----------\n%s\n\nConstructor\n-----------\n%s(%s)\n\n", sourceCode.toString().trim(), qualifiedClassName, Arrays.deepToString(ctorArgs).replaceAll("\\[(.*)\\]", "$1"));
			Class<T> programClass = compile(qualifiedClassName, sourceCode.toString());
			Constructor<?>[] ctors = programClass.getConstructors();
			for (Constructor<?> ctor : ctors) {
				Parameter[] params = ctor.getParameters();
				if (ctorArgs.length == params.length) {
					/*
					 * TODO: impossibile verificare se il tipo dei parametri
					 * matcha con ctorArgs (il problema si presenta quando ho un
					 * tipo primitivo nel costruttore)
					 */
					return (T) ctor.newInstance(ctorArgs);
				}
			}
			throw new RuntimeCompilationException("Missing constructor with " + ctorArgs.length + " argument" + (ctorArgs.length == 1 ? "" : "s"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeCompilationException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> compile(String qualifiedClassName, String sourceCode) throws Exception {
		JavaBytecode bytecode = new JavaBytecode(qualifiedClassName);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(new JavaSource(qualifiedClassName, sourceCode));
		DynamicClassLoader cl = new DynamicClassLoader(ClassLoader.getSystemClassLoader());
		ExtendedStandardJavaFileManager fileManager = new ExtendedStandardJavaFileManager(javac.getStandardFileManager(null, null, null), bytecode, cl);
		JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, null, null, null, compilationUnits);
		boolean result = task.call();
		if (!result)
			throw new RuntimeCompilationException("Failed task: " + qualifiedClassName);
		Class<T> programClass = (Class<T>) cl.loadClass(qualifiedClassName);
		return programClass;
	}

	private static class JavaSource extends SimpleJavaFileObject {
		private String contents = null;

		public JavaSource(String className, String contents) throws Exception {
			super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.contents = contents;
		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return contents;
		}
	}

	private static class JavaBytecode extends SimpleJavaFileObject {
		private ByteArrayOutputStream os = new ByteArrayOutputStream();

		public JavaBytecode(String className) throws Exception {
			super(new URI(className), Kind.CLASS);
		}

		@Override
		public OutputStream openOutputStream() throws IOException {
			return os;
		}

		public byte[] getByteArray() {
			return os.toByteArray();
		}
	}

	private static class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

		private JavaBytecode bytecode;
		private DynamicClassLoader cl;

		/**
		 * Creates a new instance of ForwardingJavaFileManager.
		 *
		 * @param fileManager
		 *            delegate to this file manager
		 * @param cl
		 */
		protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, JavaBytecode bytecode, DynamicClassLoader cl) {
			super(fileManager);
			this.bytecode = bytecode;
			this.cl = cl;
			this.cl.setCode(bytecode);
		}

		@Override
		public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
			return bytecode;
		}

		@Override
		public ClassLoader getClassLoader(JavaFileManager.Location location) {
			return cl;
		}
	}

	private static class DynamicClassLoader extends ClassLoader {

		private Map<String, JavaBytecode> bytecodeRepo = new HashMap<>();

		public DynamicClassLoader(ClassLoader parent) {
			super(parent);
		}

		public void setCode(JavaBytecode cc) {
			bytecodeRepo.put(cc.getName(), cc);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			JavaBytecode bytecode = bytecodeRepo.get(name);
			if (bytecode == null) {
				return super.findClass(name);
			}
			byte[] byteArray = bytecode.getByteArray();
			return defineClass(name, byteArray, 0, byteArray.length);
		}
	}

}