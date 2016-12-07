package it.miriade.runtime.core.runnables;

/**
 * Questa interfaccia definisce una gerarchia di oggetti che producono un risultato. La caratteristica principale è di
 * esporre il metodo {@link Producer#run()} che permette di generare un oggetto di ritorno di tipo {@link Object}.
 * 
 * @author svaponi
 */
public interface Producer {
	public Object run();
}
