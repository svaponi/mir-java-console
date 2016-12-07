package it.miriade.runtime.core.runnables;

import java.util.List;

public abstract class MyAbstractProducer implements MyProducer {

	protected List<String> inheritedMethod() {
		return constant;
	}

	protected String ordinal(int n) {
		if (n % 10 == 1)
			return n + "st";
		else if (n % 10 == 2)
			return n + "nd";
		else if (n % 10 == 3)
			return n + "rd";
		else
			return n + "th";
	}
}
