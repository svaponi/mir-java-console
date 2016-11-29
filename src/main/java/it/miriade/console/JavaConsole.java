package it.miriade.console;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import it.miriade.runtime.core.RuntimeClassCompiler;
import it.miriade.runtime.core.RuntimeClassDefinition;

/**
 * Console Java: permette di eseguire del codice a runtime. Il codice in input
 * viene inserito nel corpo del metodo {@link Runnable#run()} di una classe che
 * implementa l'interfaccia {@link Runnable}. Dopo essere stata compilata ed
 * inizializzato un oggetto da questa classe, viene eseguito tale metodo.
 * 
 * @author svaponi
 */
public class JavaConsole {

	public static final Pattern returnStatement = Pattern.compile("^return(\\ \\w*)?[;]?$");
	public static final Pattern returnEmptyStatement = Pattern.compile("^return(\\ )*[;]?$");
	public static final Pattern missingEndColon = Pattern.compile("^(?!for|if|while|else).*[^;]$");
	public static final Pattern sysoShortcut = Pattern.compile("^syso\\ (.*)[;]?$");

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	protected InputStream is = System.in;
	protected Scanner in;
	protected RuntimeClassDefinition def;

	public JavaConsole() {
		this(Runnable.class);
	}

	public JavaConsole(Class<?> clazz) {
		super();
		def = new RuntimeClassDefinition(clazz);
	}

	public void changeInput(InputStream is) {
		this.is = is;
	}

	/**
	 * Esegue una porzione di codice
	 * 
	 * @param code
	 */
	public void run(String code) {
		run(code, Collections.emptyList());
	}

	/**
	 * Esegue una porzione di codice utilizzando con la possibilità di usare le
	 * classi importate.
	 * 
	 * @param code
	 * @param imports
	 */
	public void run(String code, List<?> imports) {
		def.startDefinition(imports);
		def.appendCode(code);
		def.closeDefinition();
		((Runnable) RuntimeClassCompiler.build(def)).run();
	}

	public void run(List<String> linesOfCode, List<?> imports) {
		def.startDefinition(imports);
		def.appendCode(linesOfCode);
		def.closeDefinition();
		((Runnable) RuntimeClassCompiler.build(def)).run();
	}

	public void start() {
		start(Collections.emptyList());
	}

	public void start(List<?> imports) {
		try {
			in = new Scanner(is);
			List<String> lines = new ArrayList<>();
			String line = validateLine(readLine());
			boolean eof = false; // end of file

			loop: while (!line.equalsIgnoreCase("exit")) {
				if (eof || returnStatement.matcher(line).find()) {
					// se leggo un return da stdin chiudo il metodo e procedo
					// con la compilazione

					if (!eof)
						lines.add("return;");

					try {
						run(lines, imports);
						lines.clear(); // svuoto il buffer
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (eof)
						break loop;

				} else if (line != null && !line.isEmpty()) {
					lines.add(validateLine(line));
				}

				try {
					line = readLine();
				} catch (NoSuchElementException e) {
					eof = true;
				}
			}
		} finally {
			in.close();
		}
	}

	/**
	 * Legge una linea di codice dall'input. Di default input è lo STDIN, ovvero
	 * la console, ma può essere anche un file o qualsiasi sorgente implementi
	 * {@link InputStream}.
	 * 
	 * @return
	 */
	protected String readLine() {
		if (System.in.equals(is))
			System.out.print("CONSOLE > ");
		return in.nextLine().trim();
	}

	/**
	 * Valida la linea di codice aggiungendo alcune banali sistemazioni (es.
	 * punto e virgola alla fine)
	 * 
	 * @param line
	 * @return
	 */
	protected String validateLine(String line) {
		if (line == null || line.isEmpty())
			return "";
		if (sysoShortcut.matcher(line).find())
			line = line.replaceFirst("syso\\ (.*)[;]?$", "System.out.println($1);");
		if (missingEndColon.matcher(line).find())
			line += ";";
		return line;
	}

}
