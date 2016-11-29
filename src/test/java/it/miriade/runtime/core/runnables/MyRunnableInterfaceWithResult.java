package it.miriade.runtime.core.runnables;

import java.util.Arrays;
import java.util.List;

import it.miriade.runtime.core.runnables.RunnableWithResult;

public interface MyRunnableInterfaceWithResult extends RunnableWithResult {

	String first = "first";
	String second = "second";
	String third = "third";
	List<String> constant = Arrays.asList(first, second, third);
}
