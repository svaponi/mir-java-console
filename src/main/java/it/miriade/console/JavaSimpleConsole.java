package it.miriade.console;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import it.miriade.console.runtime.RuntimeObjectCompiler;

/**
 * 
 * @author svaponi
 *
 */
public class JavaSimpleConsole {

	static final String RUNTIME_PACKAGE = "it.miriade.console.runtime";
	static final String RUNTIME_CLASS_NAME = "OnFlyProgram";
	static final boolean ident = true;

	private StringBuffer sourceCode;
	private String qualifiedClassName;

	public JavaSimpleConsole() {
		super();
		sourceCode = new StringBuffer();
		qualifiedClassName = RUNTIME_PACKAGE + "." + RUNTIME_CLASS_NAME;
	}

	public Object execute(String code) {
		return execute(code, Collections.emptyList());
	}

	public Object execute(String code, List<Class<?>> imports) {
		try {
			init(imports);
			append(code);
			close();
			ConsoleRunnable cycle = RuntimeObjectCompiler.build(qualifiedClassName, sourceCode.toString());
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
		String qualifiedClassName = RUNTIME_PACKAGE + "." + RUNTIME_CLASS_NAME;
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
						Class<ConsoleRunnable> cycleClass = RuntimeObjectCompiler.build(qualifiedClassName,
								sourceCode.toString());
						Constructor<ConsoleRunnable> ctor = cycleClass.getConstructor();
						ConsoleRunnable cycle = (ConsoleRunnable) ctor.newInstance();
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
		sourceCode.delete(0, sourceCode.length());
		sourceCode.append("package ").append(RUNTIME_PACKAGE).append(";").append(nl());
		for (Class<?> clazz : imports)
			sourceCode.append("import ").append(clazz.getCanonicalName()).append(";").append(nl());
		sourceCode.append("public class ").append(RUNTIME_CLASS_NAME).append(" implements ")
				.append(ConsoleRunnable.class.getCanonicalName()).append(" {").append("\n");
		sourceCode.append(tab()).append("public Object run() {").append("\n");
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
