package it.miriade.runtime.core.runnables;

import java.util.Arrays;
import java.util.List;

public interface MyRunnable extends Runnable {

	String first = "first";
	String second = "second";
	String third = "third";
	List<String> constant = Arrays.asList(first, second, third);
}
