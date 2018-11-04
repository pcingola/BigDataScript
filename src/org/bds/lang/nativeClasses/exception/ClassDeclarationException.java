package org.bds.lang.nativeClasses.exception;

import java.util.LinkedList;
import java.util.List;

import org.bds.lang.nativeClasses.ClassDeclarationNative;
import org.bds.lang.statement.FieldDeclaration;
import org.bds.lang.statement.MethodDeclaration;

public class ClassDeclarationException extends ClassDeclarationNative {

	private static final long serialVersionUID = -4115713969638658245L;

	public static final String CLASS_NAME_EXCEPTION = "Exception";

	public ClassDeclarationException() {
		super();
	}

	@Override
	protected FieldDeclaration[] createFields() {
		return new FieldDeclaration[0];
	}

	@Override
	protected MethodDeclaration[] createMethods() {
		List<MethodDeclaration> methods = new LinkedList<>();
		methods.add(defaultConstructor());
		return methods.toArray(new MethodDeclaration[0]);
	}

	@Override
	protected void initNativeClass() {
		className = CLASS_NAME_EXCEPTION;
		super.initNativeClass();
	}

}
