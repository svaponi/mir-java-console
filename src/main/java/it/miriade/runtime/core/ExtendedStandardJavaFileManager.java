package it.miriade.runtime.core;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	private JavaBytecode bytecode;
	private DynamicClassLoader cl;

	/**
	 * Creates a new instance of ForwardingJavaFileManager.
	 *
	 * @param fileManager
	 *            delegate to this file manager
	 * @param cl
	 */
	protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, JavaBytecode bytecode, DynamicClassLoader cl) {
		super(fileManager);
		this.bytecode = bytecode;
		this.cl = cl;
		this.cl.setCode(bytecode);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
		return bytecode;
	}

	@Override
	public ClassLoader getClassLoader(JavaFileManager.Location location) {
		return cl;
	}
}