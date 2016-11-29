package it.miriade.console;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Carico tutte le classi disponibili nel classpath TODO: verificare se sta in
 * piedi!!
 * 
 * @author svaponi
 *
 */
public class ImportDependencies {

	static List<String> classFiles = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		Enumeration<URL> roots = Thread.currentThread().getContextClassLoader().getResources("");
		for (URL url : Collections.list(roots)) {
			File root = new File(url.getPath());
			scan(root);
		}
		for (String clazz : classFiles)
			System.out.println(clazz);
		
		new JavaConsole().start();
	}

	private static void scan(File root) {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				scan(file);
			} else {
				String name = file.getAbsolutePath();
				if (name.endsWith(".class"))
					classFiles.add(format(name));
			}
		}
	}

	static String locator = "target/classes/";

	private static String format(String name) {
		System.out.println(name);
		int start = name.indexOf(locator) + locator.length();
		name = name.substring(start, name.length() - ".class".length());
		return name.replaceAll("/", "\\.");
	}
}
