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
	public static final Pattern sysoShortcut = Pattern.compile("^syso\\ ([^;!]*)[;]?[!]?$");
	public static final Pattern specialCommand = Pattern.compile("^:([\\w]*)$");
	public static final Pattern setCommand = Pattern.compile("^:([\\w]*)([\\ ]([\\w]*)[=]([\\w]*))?$");
	public static final Pattern runSingleLine = Pattern.compile("^(.*)!$");

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	protected InputStream is = System.in;
	protected Scanner in;
	protected RuntimeClassDefinition def;
	protected List<String> lines;

	public JavaConsole() {
		this(Runnable.class);
	}

	public JavaConsole(Class<?> clazz) {
		super();
		def = new RuntimeClassDefinition(clazz);
		lines = new ArrayList<>();
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
			// List<String> lines = new ArrayList<>();
			lines.clear();
			String line = "";
			boolean eof = false; // end of file
			boolean compile = false; // force code submition

			loop: while (true) {
				if (eof || compile) {

					// if (!eof)
					// lines.add("return;");

					try {
						run(lines, imports);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						compile = false;
						lines.clear(); // svuoto il buffer
					}

					if (eof)
						break loop;

				} else if (specialCommand.matcher(line).find()) {
					/*
					 * qui posso aggiungere meta-istruzioni identificate dal prefisso ':'
					 */
					String cmd = line.replaceFirst(specialCommand.pattern(), "$1");
					switch (cmd) {
					case "clear":
					case "clean":
					case "purge":
						lines.clear(); // svuoto il buffer
						break;
					case "print":
					case "show":
						lines.forEach(l -> System.out.println(l));
						break;
					case "last":
						System.out.println(lines.get(lines.size() - 1));
						break;
					case "delete":
					case "back":
					case "x":
						System.out.println("Removed: " + lines.remove(lines.size() - 1));
						break;
					case "compile":
					case "run":
						compile = true;
						continue loop;

					case "quit":
					case "exit":
						break loop;

					default:
						break;
					}
				} else {
					addLine(line);
				}

				try {
					line = readLine();
					if (runSingleLine.matcher(line).find()) {
						/*
						 * esegue subito la riga di codice che termina con !
						 */
						addLine(line.substring(0, line.lastIndexOf('!'))); // tolgo il ! alla fine
						compile = true;
						continue loop;
					}
					compile = returnStatement.matcher(line).find();

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
	 * Aggiunge righe di codice
	 * 
	 * @param line
	 */
	protected void addLine(String line) {
		if (line == null || line.isEmpty())
			return;
		lines.add(validateLine(line));
	}

	/**
	 * Valida la linea di codice aggiungendo alcune banali sistemazioni (es.
	 * punto e virgola alla fine)
	 * 
	 * @param line
	 * @return
	 */
	protected String validateLine(String line) {
		if (sysoShortcut.matcher(line).find())
			line = line.replaceFirst(sysoShortcut.pattern(), "System.out.println($1);");
		if (missingEndColon.matcher(line).find())
			line += ";";
		return line;
	}
}
