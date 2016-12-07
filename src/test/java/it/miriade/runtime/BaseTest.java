package it.miriade.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author svaponi
 */
public abstract class BaseTest {

	/**
	 * Attivare la modalità debug durante i test
	 */
	static final boolean ENABLE_DEBUG_MODE = false;
	static String oldDebugMode;

	@BeforeClass
	public static void setup() {
		oldDebugMode = System.getProperty("debug");
		if (ENABLE_DEBUG_MODE)
			System.setProperty(DefaultSpec.DEBUG_MODE_SYSPROP, Boolean.toString(ENABLE_DEBUG_MODE));

	}

	@AfterClass
	public static void tearDown() {
		if (ENABLE_DEBUG_MODE)
			System.setProperty(DefaultSpec.DEBUG_MODE_SYSPROP, oldDebugMode);
	}

}
