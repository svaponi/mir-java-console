package it.miride.console.runnables;

import java.util.Arrays;
import java.util.List;

import it.miriade.console.ConsoleRunnable;

public interface MyRunnableInterface extends ConsoleRunnable {

	String first = "first";
	String second = "second";
	String third = "third";
	List<String> constant = Arrays.asList(first, second, third);
}
