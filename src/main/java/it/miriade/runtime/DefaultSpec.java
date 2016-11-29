package it.miriade.runtime;

/**
 * Valori di default usati per la compilazione a runtime.
 * 
 * @author svaponi
 */
public class DefaultSpec {

	public static final String VERSION = "1.1.0";

	public static final String DEBUG_MODE_SYSPROP = "debug";
	public static boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty(DefaultSpec.DEBUG_MODE_SYSPROP, "false"));

	// aggiunge colon alla fine delle istruzioni
	// public static final String ADD_COLON_SYSPROP = "add.colon";
	// public static boolean ADD_COLON = Boolean.parseBoolean(System.getProperty(DefaultSpec.ADD_COLON_SYSPROP,
	// "false"));

	/**
	 * Package in cui creare la classe a runtime. ATTENZIONE: se utilizzaiamo un
	 * package non esistente a compile-time, la chiamata
	 * obj.getClass().getPackage() tornerà null (obj è l'oggetto che implementa
	 * {@link DefaultSpec#SUPER_CLASS} generato a runtime)
	 */
	public static String RUNTIME_PACKAGE = DefaultSpec.class.getPackage().getName();
	public static String RUNTIME_CLASSNAME = "RuntimeObject";
	public static String RUNTIME_METHOD = "run";
	public static Class<?> RUNTIME_METHOD_RETURN_TYPE = Void.class;
	public static Class<?> SUPER_CLASS = Runnable.class;

}
