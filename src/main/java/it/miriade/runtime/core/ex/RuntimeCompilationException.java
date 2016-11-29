package it.miriade.runtime.core.ex;

public class RuntimeCompilationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RuntimeCompilationException() {
		super();

	}

	public RuntimeCompilationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public RuntimeCompilationException(String message, Throwable cause) {
		super(message, cause);

	}

	public RuntimeCompilationException(String message) {
		super(message);

	}

	public RuntimeCompilationException(Throwable cause) {
		super(cause);

	}

}
