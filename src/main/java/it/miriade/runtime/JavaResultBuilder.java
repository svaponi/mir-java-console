package it.miriade.runtime;

import java.util.Collections;
import java.util.List;

import it.miriade.console.JavaConsole;
import it.miriade.runtime.core.RuntimeClassCompiler;
import it.miriade.runtime.core.RuntimeClassDefinition;
import it.miriade.runtime.core.runnables.RunnableWithResult;

/**
 * Questa classe esegue una porzione di codice compilata runtime e ritorna
 * l'oggetto risultante. La porzione di codice può essere inniettata in una
 * classe che ne estende un'altra già esistente e che possiede determinate
 * caratteristiche o proprietà che possono essere utilizzate nella porzione di
 * codice.
 * 
 * @author svaponi
 */
public class JavaResultBuilder extends JavaConsole {

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	public JavaResultBuilder() {
		this(RunnableWithResult.class);
	}

	public JavaResultBuilder(Class<?> clazz) {
		super();
		def = new RuntimeClassDefinition(clazz);
	}

	public Object runAndGetResult(String code) {
		return runAndReturn(code, Collections.emptyList());
	}

	public Object runAndReturn(String code, List<?> imports) {
		RunnableWithResult cycle = build(code, imports);
		return cycle.run();
	}

	public RunnableWithResult build(String code, List<?> imports) {
		def.startDefinition(imports);
		def.appendCode(code);
		def.closeDefinition();
		return RuntimeClassCompiler.build(def);
	}
}
