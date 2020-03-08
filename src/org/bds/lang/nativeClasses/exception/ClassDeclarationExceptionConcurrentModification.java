package org.bds.lang.nativeClasses.exception;

import java.util.LinkedList;
import java.util.List;

import org.bds.lang.statement.MethodDeclaration;

public class ClassDeclarationExceptionConcurrentModification extends ClassDeclarationException {

	private static final long serialVersionUID = -2260672994052335846L;

	public static final String CLASS_NAME_EXCEPTION_CONCURRENT_MODIFICATION = "ConcurrentModificationException";

	public ClassDeclarationExceptionConcurrentModification() {
		super();
	}

	@Override
	protected MethodDeclaration[] createMethods() {
		List<MethodDeclaration> methods = new LinkedList<>();
		methods.add(new MethodExceptionConstructor(getType()));
		return methods.toArray(new MethodDeclaration[0]);
	}

	@Override
	protected void initNativeClass() {
		if (className == null) className = CLASS_NAME_EXCEPTION_CONCURRENT_MODIFICATION;
		super.initNativeClass();
	}

}
