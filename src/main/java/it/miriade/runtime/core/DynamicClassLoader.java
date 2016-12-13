package it.miriade.runtime.core;

import java.util.HashMap;
import java.util.Map;

public class DynamicClassLoader extends ClassLoader {

	private Map<String, JavaBytecode> bytecodeRepo = new HashMap<>();

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}

	public void setCode(JavaBytecode cc) {
		bytecodeRepo.put(cc.getName(), cc);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		JavaBytecode bytecode = bytecodeRepo.get(name);
		if (bytecode == null) {
			return super.findClass(name);
		}
		byte[] byteArray = bytecode.getByteArray();
		return defineClass(name, byteArray, 0, byteArray.length);
	}
}
