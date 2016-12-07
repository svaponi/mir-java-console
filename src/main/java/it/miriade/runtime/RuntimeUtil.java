package it.miriade.runtime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.miriade.runtime.core.RuntimeClassCompiler;
import it.miriade.runtime.core.RuntimeClassDefinition;
import it.miriade.runtime.core.runnables.Producer;

/**
 * Questa classe produce degli oggetti {@link Producer} che contengono una porzione di codice sorgente compilata a
 * runtime.
 * Tale codice sorgente di codice può essere inniettata in una classe/interfaccia che ne estende/implementa un'altra già
 * esistente, le caratteristiche o proprietà di quest'ultima saranno dunque visibili nella porzione di codice.
 * 
 * @author svaponi
 */
public class RuntimeUtil {

	/**
	 * Inizializza l'oggetto {@link Producer}.
	 * 
	 * @param code
	 *            codice sorgente Java da compilare a runtime
	 * @return
	 */
	public static Producer compile(String code) {
		return buildProducer(null, code, Collections.emptyList());
	}

	/**
	 * Genera un oggetto {@link Producer}.
	 * 
	 * @param source
	 *            codice sorgente Java da compilare a runtime
	 * @param imports
	 *            dipendenze necessarie all'esecuzione del codice sorgente
	 * @return
	 */
	public static Producer compile(String source, List<?> imports) {
		return buildProducer(null, source, imports);
	}

	/**
	 * Genera un oggetto {@link Producer}.
	 * 
	 * @param source
	 *            codice sorgente Java da compilare a runtime
	 * @param imports
	 *            dipendenze necessarie all'esecuzione del codice sorgente
	 * @return
	 */
	public static Producer compile(String source, Object... imports) {
		return buildProducer(null, source, Arrays.asList(imports));
	}

	/**
	 * Genera un oggetto {@link Producer}.
	 * 
	 * @param source
	 *            codice sorgente Java da compilare a runtime
	 * @param parent
	 *            classe/interfaccia da estendere/implementare che conterrà il codice sorgente
	 * @param imports
	 *            dipendenze necessarie all'esecuzione del codice sorgente
	 * @return
	 */
	public static <T extends Producer> Producer compile(Class<T> parent, String source, List<?> imports) {
		return buildProducer(parent, source, imports);
	}

	/**
	 * Genera un oggetto {@link Producer}.
	 * 
	 * @param source
	 *            codice sorgente Java da compilare a runtime
	 * @param parent
	 *            classe/interfaccia da estendere/implementare che conterrà il codice sorgente
	 * @param imports
	 *            dipendenze necessarie all'esecuzione del codice sorgente
	 * @return
	 */
	public static <T extends Producer> Producer compile(Class<T> parent, String source, Object... imports) {
		return buildProducer(parent, source, Arrays.asList(imports));
	}

	/*
	 * Metodo privato invocato dai metodi esposti
	 */
	private static <T extends Producer> Producer buildProducer(Class<T> parent, String source, List<?> imports) {
		RuntimeClassDefinition def = parent == null ? new RuntimeClassDefinition(Producer.class) : new RuntimeClassDefinition(parent);
		def.startDefinition(imports);
		def.appendSourceCode(source);
		def.closeDefinition();
		return RuntimeClassCompiler.build(def);
	}
}
