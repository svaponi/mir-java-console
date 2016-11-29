package it.miriade.runtime.core.ex;

/**
 * Sollevata se tentiamo di chiudere o scrivere del codice dentro una classe non
 * inizializzata.
 * 
 * @author svaponi
 *
 */
public class ClassDefinitionException extends RuntimeException {

	private static final long serialVersionUID = 6269954167601912321L;

	public ClassDefinitionException() {
		super();
	}

	public ClassDefinitionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClassDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassDefinitionException(String message) {
		super(message);
	}

	public ClassDefinitionException(Throwable cause) {
		super(cause);
	}

}
