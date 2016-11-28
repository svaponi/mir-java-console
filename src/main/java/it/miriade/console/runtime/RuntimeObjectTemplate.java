package it.miriade.console.runtime;

/**
 * Questa classe instanzia oggetti usando il codice in ingresso a runtime.
 * 
 * @author svaponi
 *
 */
public class RuntimeObjectTemplate<T> {

	private String qualifiedClassName;
	private String sourceCode;

	public RuntimeObjectTemplate(String qualifiedClassName, String sourceCode) {
		super();
		this.qualifiedClassName = qualifiedClassName;
		this.sourceCode = sourceCode;
	}
	
	public T newInstance(Object... ctorArgs) {
		return RuntimeObjectCompiler.build(qualifiedClassName, sourceCode, ctorArgs);
	}
}