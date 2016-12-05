package it.miriade.runtime.core.runnables;

/**
 * Interfaccia usata dalla console per definire un oggetto con un metodo
 * conosciuto da poter eseguire. Permette di ritornare un {@link Object}.
 * 
 * @author svaponi
 */
public interface RunnableWithResult {
	public Object run();
}
