package test.utils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loop in ascolto sullo STDIN per testare le Regex-Java
 * 
 * @author svaponi
 */
public class RegexTest {

	private static final String PREFIX = "r:";

	/*
	 * ^(?:for|if|while|else)$ = matcha SOLO le parole: for, if, while, else
	 * ^(?!for|if|while|else)$ = matcha TUTTO ESCLUSE le parole: for, if, while, else
	 */
	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {
			String pattern = readLine(in);
			Pattern regex = Pattern.compile(pattern);
			String input = readLine(in);
			while (!input.equalsIgnoreCase("exit")) {
				if (input.startsWith(PREFIX)) {
					pattern = input.substring(PREFIX.length());
					regex = Pattern.compile(pattern);
					input = readLine(in);
					continue;
				}
				Matcher match = regex.matcher(input);
				boolean result = match.matches();
				System.out.printf("Pattern.matches(\"%s\", \"%s\"): %s \n", pattern, input, result);
				if (result) {
					for (int i = 0; i < match.groupCount(); i++)
						System.out.printf("\t%d) %s \n", i, match.group(i));
				}
				input = readLine(in);
			}
		}
	}

	private static String readLine(Scanner in) {
		System.out.print("RegExp > ");
		return in.nextLine().trim();
	}
}
