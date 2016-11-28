package it.miriade.console.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import org.mdkt.compiler.InMemoryJavaCompiler;

/**
 * Questa classe compila il codice in ingresso a runtime.
 * 
 * @author svaponi
 *
 */
public class RuntimeObjectCompiler {

	public static <T> T build(String qualifiedClassName, String sourceCode, Object... ctorArgs) {
		try {
			Class<T> programClass = compile(qualifiedClassName, sourceCode.toString());
			Constructor<T> ctor = programClass.getConstructor();
			Parameter[] params = ctor.getParameters();
			if (ctorArgs.length < params.length)
				throw new RuntimeCompilationException(
						"Insufficient constructor arguments: got " + params.length + " needed " + ctorArgs.length);
			T program = (T) ctor.newInstance(ctorArgs);
			return program;
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