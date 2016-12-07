package it.miriade.runtime.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import it.miriade.runtime.DefaultSpec;
import it.miriade.runtime.core.ex.ClassDefinitionException;

/**
 * Oggetto che incapsula il codice che verrà eseguito a runtime.
 * 
 * @author svaponi
 */
public class RuntimeClassDefinition {

	private StringBuffer sourceCode;
	private String className;
	private String packageName;
	private String methodName;
	private Class<?> returnType;
	private Class<?> parent;
	private List<?> imports;
	private boolean initialized = false;
	private boolean needsReturnStatement = false;
	private boolean ident = true; // identa il codice per facilitare il debug

	public RuntimeClassDefinition() {
		this(DefaultSpec.RUNTIME_CLASSNAME, DefaultSpec.RUNTIME_PACKAGE, DefaultSpec.SUPER_CLASS, Collections.emptyList());
	}

	public RuntimeClassDefinition(Class<?> parent) {
		this(DefaultSpec.RUNTIME_CLASSNAME, DefaultSpec.RUNTIME_PACKAGE, parent, Collections.emptyList());
	}

	public RuntimeClassDefinition(Class<?> parent, List<?> imports) {
		this(DefaultSpec.RUNTIME_CLASSNAME, DefaultSpec.RUNTIME_PACKAGE, parent, imports);
	}

	public RuntimeClassDefinition(String className, String packageName, Class<?> parent, List<?> imports) {
		super();
		this.className = className;
		this.packageName = packageName;
		this.methodName = DefaultSpec.RUNTIME_METHOD;
		this.returnType = DefaultSpec.RUNTIME_METHOD_RETURN_TYPE;
		this.parent = parent;
		this.imports = (imports == null) ? new ArrayList<>() : imports;
		this.sourceCode = new StringBuffer();
		try {
			returnType = parent.getMethod(methodName).getReturnType();
			needsReturnStatement = returnType.equals(Void.TYPE);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ClassDefinitionException("Invalid method: " + methodName, e);
		}
	}

	/**
	 * Inizializza una istanza di {@link RuntimeClassDefinition} già completa di
	 * sorgente.
	 * 
	 * @param qualifiedClassName
	 * @param sourceCode
	 */
	static RuntimeClassDefinition getInstance(String qualifiedClassName, String sourceCode) {
		RuntimeClassDefinition def = new RuntimeClassDefinition();
		int index = qualifiedClassName.lastIndexOf('.');
		def.className = qualifiedClassName.substring(index + 1);
		def.packageName = qualifiedClassName.substring(0, index);
		def.sourceCode.append(sourceCode);
		def.initialized = true;
		return def;
	}

	/**
	 * Indica se la classe ha o meno un return statement.
	 * 
	 * @return
	 */
	public boolean hasReturnStatement() {
		return needsReturnStatement;
	}

	/**
	 * Indica il tipo ritornato dal codice eseguito a runtime. Se
	 * {@link RuntimeClassDefinition#hasReturnStatement()} torna FALSE allora
	 * questo metodo torna {@link Void#TYPE}.
	 * 
	 * @return
	 */
	public Class<?> returnType() {
		return returnType;
	}

	/**
	 * Ritorna il codice completo della classe.
	 * 
	 * @return
	 */
	public String getSourceCode() {
		return sourceCode.toString();
	}

	/**
	 * Ritorna il nome della classe che contien il codice da eseguire
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Ritorna il nome della classe completo di package
	 * 
	 * @return
	 */
	public String getQualifiedClassName() {
		return packageName + "." + className;
	}

	/**
	 * Imposta il flag per la formattazione del codice.
	 * 
	 * @param ident
	 */
	public void setIdentation(boolean ident) {
		this.ident = ident;
	}

	/**
	 * Comincia la definizione della classe che conterrà il codice eseguito a
	 * runtime.
	 */
	public void startDefinition() {
		startDefinition(Collections.emptyList());
	}

	/**
	 * Comincia la definizione della classe che conterrà il codice eseguito a
	 * runtime. Aggiunge nuovi imports.
	 * 
	 * @param additionalImports
	 *            nuovi imports
	 */
	public void startDefinition(List<?> additionalImports) {
		sourceCode.delete(0, sourceCode.length());
		sourceCode.append("package ").append(packageName).append(";").append(nl());
		Stream.concat(imports.stream(), additionalImports == null ? Stream.empty() : additionalImports.stream()).forEach(new Consumer<Object>() {

			@Override
			public void accept(Object dependency) {
				if (dependency instanceof Class<?>)
					sourceCode.append("import ").append(((Class<?>) dependency).getCanonicalName()).append(";").append(nl());
				else if (dependency instanceof Package)
					sourceCode.append("import ").append(((Package) dependency).getName()).append(".*").append(";").append(nl());
				else
					sourceCode.append("import ").append(dependency.toString()).append(";").append(nl());
			}
		});
		String extendsOrImplements = parent.isInterface() ? " implements " : " extends ";
		sourceCode.append("public class ").append(className).append(extendsOrImplements).append(parent.getCanonicalName()).append(" {").append("\n");
		sourceCode.append(tab()).append("public ").append(returnType.getSimpleName()).append(" ").append(methodName).append("() {").append("\n");
		initialized = true;
	}

	/**
	 * Appende una serie di linee di codice alla definizione della classe.
	 * 
	 * @param lines
	 */
	public void appendSourceCode(List<String> linesOfCode) throws ClassDefinitionException {
		appendSourceCode(linesOfCode.toArray(new String[] {}));
	}

	/**
	 * Appende una serie di linee di codice alla definizione della classe.
	 * 
	 * @param lines
	 */
	public void appendSourceCode(String[] lines) throws ClassDefinitionException {
		for (String line : lines)
			appendSourceCode(line);
	}

	/**
	 * Appende una linea di codice alla definizione della classe.
	 * 
	 * @param line
	 */
	public void appendSourceCode(String line) throws ClassDefinitionException {
		verifyInitialized();
		sourceCode.append(tab()).append(tab()).append(line).append(nl());
	}

	/**
	 * Chiude la definizione della classe.
	 */
	public void closeDefinition() throws ClassDefinitionException {
		verifyInitialized();
		sourceCode.append(tab()).append("}").append(nl());
		sourceCode.append("}").append(nl());
	}

	/**
	 * Vwerifica che la definizione sia stata inizializzata
	 * 
	 * @throws ClassDefinitionException
	 */
	private void verifyInitialized() throws ClassDefinitionException {
		if (!initialized)
			throw new ClassDefinitionException("Uninitialized class definition");
	}

	// metodo ausiliare per identare il codice
	private String nl() {
		return ident ? "\n" : "";
	}

	// metodo ausiliare per identare il codice
	private String tab() {
		return ident ? "\t" : "";
	}
}
