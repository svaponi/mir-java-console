package it.miride.console.runnables;

import java.util.Arrays;
import java.util.List;

import it.miriade.console.ConsoleRunnable;

public abstract class MyAbstractRunnable implements ConsoleRunnable {

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

	public MyAbstractRunnable() {
		super();
		constant = Arrays.asList(first, second, third);
		// do whatever...
	}

	protected Object customMethod() {
		return constant;
	}
}
