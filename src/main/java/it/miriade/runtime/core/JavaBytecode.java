package it.miriade.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaBytecode extends SimpleJavaFileObject {
	private ByteArrayOutputStream os = new ByteArrayOutputStream();

	public JavaBytecode(String className) throws Exception {
		super(new URI(className), Kind.CLASS);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return os;
	}

	public byte[] getByteArray() {
		return os.toByteArray();
	}
}
