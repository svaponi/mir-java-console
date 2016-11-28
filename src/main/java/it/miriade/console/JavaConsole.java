package it.miriade.console;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

import it.miriade.console.runtime.RuntimeCompilationException;
import it.miriade.console.runtime.RuntimeObjectCompiler;

/**
 * 
 * @author svaponi
 *
 */
public class JavaConsole<T extends ConsoleRunnable> {

	static final String RUNTIME_PACKAGE = "it.miriade.console.runtime";
	static final String RUNTIME_CLASSNAME = "OnFlyProgram";
	static final String QUALIFIED_CLASSNAME = RUNTIME_PACKAGE + "." + RUNTIME_CLASSNAME;
	static final boolean ident = true;

	private StringBuffer sourceCode;
	public Class<T> clazz;

	public JavaConsole(Class<T> clazz) {
		super();
		this.clazz = clazz;
		sourceCode = new StringBuffer();
	}

	public Object execute(String code) {
		return execute(code, Collections.emptyList());
	}

	public Object execute(String code, List<Class<?>> imports) {
		try {
			init(imports);
			append(code);
			close();
			T cycle = RuntimeObjectCompiler.build(QUALIFIED_CLASSNAME, sourceCode.toString());
			return cycle.run();
		} catch (SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void interactive() {
		interactive(Collections.emptyList());
	}

	public void interactive(List<Class<?>> imports) {
		String qualifiedClassName = QUALIFIED_CLASSNAME;
		System.out.println();
		try (Scanner in = new Scanner(System.in)) {
			System.out.print("CONSOLE > ");
			String line = in.nextLine();
			init(imports);
			while (!line.equalsIgnoreCase("exit")) {
				if (line.equalsIgnoreCase("!!")) { // se leggo !! da stdin
													// chiudo il metodo e
													// compilo

					close();
					System.out.println();
					System.out.println(sourceCode.toString());

					try {
						Class<T> cycleClass = RuntimeObjectCompiler.build(qualifiedClassName, sourceCode.toString());
						Constructor<T> ctor = cycleClass.getConstructor();
						T cycle = (T) ctor.newInstance();
						Object result = cycle.run();
						System.out.println(result);

					} catch (Exception e) {
						e.printStackTrace();
					}

					init(imports); // comincio un nuovo ciclo

				} else {
					append(line);
				}

				line = in.nextLine();
			}
		}
	}

	private void init(List<Class<?>> imports) {
		try {
			sourceCode.delete(0, sourceCode.length());
			sourceCode.append("package ").append(RUNTIME_PACKAGE).append(";").append(nl());
			for (Class<?> clazz : imports)
				sourceCode.append("import ").append(clazz.getCanonicalName()).append(";").append(nl());
			String extendsOrImplements = clazz.isInterface() ? " implements " : " extends ";
			sourceCode.append("public class ").append(RUNTIME_CLASSNAME).append(extendsOrImplements)
					.append(clazz.getCanonicalName()).append(" {").append("\n");
			sourceCode.append(tab()).append("public Object run() {").append("\n");

			// System.out.println(sourceCode.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeCompilationException(e.getMessage());
		}
	}

	private String nl() {
		return ident ? "\n" : "";
	}

	public String tab() {
		return ident ? "\t" : "";
	}

	void append(String[] lines) {
		for (String line : lines)
			append(line);
	}

	void append(String line) {
		sourceCode.append(tab()).append(tab()).append(line).append(nl());
	}

	void close() {
		sourceCode.append(tab()).append("}").append(nl());
		sourceCode.append("}").append(nl());
	}

}
