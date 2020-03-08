package org.bds.lang.type;

import org.bds.lang.nativeClasses.exception.ClassDeclarationException;
import org.bds.lang.statement.ClassDeclaration;

public class TypeClassException extends TypeClass {

	private static final long serialVersionUID = -5945171123065005349L;

	public TypeClassException() {
		super(new ClassDeclarationException());
	}

	public TypeClassException(ClassDeclaration classDeclaration) {
		super(classDeclaration);
	}

}
