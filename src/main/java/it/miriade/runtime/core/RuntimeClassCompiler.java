package it.miriade.runtime.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.mdkt.compiler.InMemoryJavaCompiler;

import it.miriade.runtime.DefaultSpec;
import it.miriade.runtime.core.ex.RuntimeCompilationException;

/**
 * Questa classe genera classi a runtime compilando il codice sorgente in
 * ingresso.
 * 
 * @author svaponi
 *
 */
public class RuntimeClassCompiler {

	public static <T> T build(RuntimeClassDefinition def, Object... ctorArgs) {
		return build(def.getQualifiedClassName(), def.getSourceCode(), ctorArgs);
	}

	@SuppressWarnings("unchecked")
	public static <T> T build(String qualifiedClassName, String sourceCode, Object... ctorArgs) {
		try {
			if (Boolean.parseBoolean(System.getProperty(DefaultSpec.DEBUG_MODE_SYSPROP, "false")))
				System.out.printf("\nSource code\n-----------\n%s\n\nConstructor\n-----------\n%s(%s)\n\n",
						sourceCode.toString().trim(), qualifiedClassName,
						Arrays.deepToString(ctorArgs).replaceAll("\\[(.*)\\]", "$1"));
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
			throw new RuntimeCompilationException(
					"Missing constructor with " + ctorArgs.length + " argument" + (ctorArgs.length == 1 ? "" : "s"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeCompilationException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> compile(String qualifiedClassName, String sourceCode) throws Exception {
		Class<T> programClass = (Class<T>) InMemoryJavaCompiler.compile(qualifiedClassName, sourceCode);
		return programClass;
	}

}