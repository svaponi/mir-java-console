package it.miriade.runtime.core.runnables;

import java.util.Arrays;
import java.util.List;

import it.miriade.runtime.core.runnables.RunnableWithResult;

public abstract class MyAbstractRunnableWithResult implements RunnableWithResult {

	protected static final String first;
	protected static final String second;
	protected static final String third;

	static {
		first = "first";
		second = "second";
		third = "third";
		// do whatever...
	}

	protected final List<String> constant;

	public MyAbstractRunnableWithResult() {
		super();
		constant = Arrays.asList(first, second, third);
		// do whatever...
	}

	protected Object customMethod() {
		return constant;
	}
}
