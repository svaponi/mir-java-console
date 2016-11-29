package test.utils;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Loop in ascolto sullo STDIN per testare le Regex-Java
 * 
 * @author svaponi
 */
public class RegexTest {

	/*
	 * ^(?:for|if|while|else)$ = matcha SOLO le parole: for, if, while, else
	 * ^(?!for|if|while|else)$ = matcha TUTTO ESCLUSE le parole: for, if, while, else
	 */
	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {
			System.out.print("RegExp > ");
			String regex = in.nextLine();
			System.out.print(" > ");
			String input = in.nextLine().trim();
			while (!input.equalsIgnoreCase("exit")) {
				if (input.startsWith("r=")) {
					regex = input.split("=")[1];
					System.out.print("CONSOLE > ");
					input = in.nextLine().trim();
					continue;
				}
				System.out.printf("Pattern.matches(\"%s\", \"%s\"): %s \n", regex, input, Pattern.matches(regex, input));
				System.out.print("CONSOLE > ");
				input = in.nextLine().trim();
			}
		}
	}

}
