package it.miriade.console;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Classe principale per l'invocazione del {@link JavaConsole}.
 * 
 * @author svaponi
 * @email s.vaponi@miriade.it
 */
public class Main {

	// Argomento pre stampare le info di help
	public static final String HELP_ARG = "h";
	public static final String HELP_ARG_NAME = "help";

	// Argomento to che indica se il debug è attivato
	public static final String DEBUG_ARG = "d";
	public static final String DEBUG_ARG_NAME = "debug";
	public static final String DEBUG_ARG_DESCR = "If TRUE debug mode is enabled. Runtime code is displayed on STDOUT.";

	// Argomento abilita gli shortcut in console (es. aggiunge i punto-virgola alla fine degli statement)
	// public static final String SHORTCUT_ARG = "sc";
	// public static final String SHORTCUT_ARG_NAME = "shortcut";
	// public static final String SHORTCUT_ARG_DESCR = "If TRUE debug mode is enabled. Runtime code is displayed on
	// STDOUT.";

	// Argomento che indica la classe/interfaccia da estendere
	public static final String SUPER_ARG = "s";
	public static final String SUPER_ARG_NAME = "super";
	public static final String SUPER_ARG_DESCR = "Class/interface to be extended/implemented by the runtime class definition.";

	public static void main(String[] args) {

		int status = 1;
		boolean launched = false;
		try {

			CommandLineParser parser = new GnuParser();
			List<Option> optionList = new ArrayList<Option>();

			/*
			 * Definisco gli argomenti in input
			 */
			OptionBuilder.withArgName(HELP_ARG_NAME);
			OptionBuilder.withLongOpt(HELP_ARG_NAME);
			OptionBuilder.hasArgs(0);
			OptionBuilder.withDescription("Print usage");
			optionList.add(OptionBuilder.create(HELP_ARG));

			OptionBuilder.withArgName(DEBUG_ARG_NAME);
			OptionBuilder.withLongOpt(DEBUG_ARG_NAME);
			OptionBuilder.hasArgs(0);
			OptionBuilder.withDescription(DEBUG_ARG_DESCR);
			optionList.add(OptionBuilder.create(DEBUG_ARG));

			OptionBuilder.withArgName(SUPER_ARG_NAME);
			OptionBuilder.withLongOpt(SUPER_ARG_NAME);
			OptionBuilder.hasArgs(1);
			OptionBuilder.withDescription(SUPER_ARG_DESCR);
			optionList.add(OptionBuilder.create(SUPER_ARG));

			Options options = new Options();
			for (Option op : optionList)
				options.addOption(op);

			/*
			 * Leggo gli argomenti in input
			 */
			CommandLine line = parser.parse(options, args);

			if (line.hasOption(HELP_ARG)) {
				printUsage();
				System.exit(0);
			}

			if (line.hasOption(DEBUG_ARG)) {
				System.out.println(String.format("%-14s %s ", DEBUG_ARG_NAME, Boolean.TRUE.toString()));
				System.setProperty("debug", Boolean.TRUE.toString());
			}

			Class<?> clazz = null;
			if (line.hasOption(SUPER_ARG)) {
				String className = line.getOptionValue(SUPER_ARG);
				clazz = ClassLoader.getSystemClassLoader().loadClass(className);
				System.out.println(String.format("%-14s %s ", SUPER_ARG_NAME, clazz.getName()));
			}

			if (clazz == null)
				new JavaConsole().start();
			else
				new JavaConsole(clazz).start();
			launched = true;
			status = 0;

		} catch (Exception e) {
			System.err.println(e.getMessage());

			if (!launched)
				printUsage();
		}

		// by convention, a nonzero status code indicates abnormal termination.
		// http://docs.oracle.com/javase/6/docs/api/java/lang/System.html#exit(int)
		System.exit(status);
	}

	/**
	 * Stampa le info per il corretto utilizzo del tool in stdout
	 */
	private static void printUsage() {
		System.out.println("");
		double version = getVersion();
		if (version == 0.0)
			System.out.println("Java 1.8 required! Current version is UNKNOWN \n");
		else if (version < 1.6)
			System.out.println("Java 1.8 required! Current version is Java " + version + " \n");
		System.out.printf("java -jar {name}.jar [-%s] \n", DEBUG_ARG);
		System.out.println("");
		System.out.printf("  -%s %-24s %-12s %s \n", DEBUG_ARG, DEBUG_ARG_NAME, "OPTIONAL", DEBUG_ARG_DESCR);
		System.out.printf("  -%s %-24s %-12s %s \n", SUPER_ARG, SUPER_ARG_NAME, "OPTIONAL", SUPER_ARG_DESCR);
	}

	/**
	 * Ritorna la versione Java corrente in double, es. 1.6
	 * 
	 * @return
	 */
	private static double getVersion() {
		try {
			String version = System.getProperty("java.version");
			int pos = version.indexOf('.');
			pos = version.indexOf('.', pos + 1);
			return Double.parseDouble(version.substring(0, pos));
		} catch (Exception e) {
			return 0;
		}
	}
}
