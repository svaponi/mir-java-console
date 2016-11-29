package it.miriade.runtime.core;

/**
 * Questa Factory fornisce istanze della stessa classe compilata a runtime. Il
 * metodo {@link RuntimeObjectFactory#newInstance(Object...)} permette di
 * costruire differenti istanze della classe passando diversi parametri al
 * costruttore.
 * 
 * @author svaponi
 *
 */
public class RuntimeObjectFactory<T> {

	private RuntimeClassDefinition classDef;

	/**
	 * Crea una nuova factory che produrrà oggetti della classe compilata.
	 * 
	 * @param def
	 */
	public RuntimeObjectFactory(RuntimeClassDefinition def) {
		super();
		this.classDef = def;
	}

	/**
	 * Crea una nuova factory che produrrà oggetti della classe compilata.
	 * 
	 * @param qualifiedClassName
	 * @param sourceCode
	 */
	public RuntimeObjectFactory(String qualifiedClassName, String sourceCode) {
		super();
		this.classDef = RuntimeClassDefinition.getInstance(qualifiedClassName, sourceCode);
	}

	/**
	 * Genera una nuova istanza della classe compilata, usanto gli args come
	 * parametri del costruttore.
	 * 
	 * @param ctorArgs
	 * @return
	 */
	public T newInstance(Object... ctorArgs) {
		return RuntimeClassCompiler.build(classDef, ctorArgs);
	}
}