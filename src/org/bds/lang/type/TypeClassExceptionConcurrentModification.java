package org.bds.lang.type;

import org.bds.lang.nativeClasses.exception.ClassDeclarationExceptionConcurrentModification;

public class TypeClassExceptionConcurrentModification extends TypeClassException {

	private static final long serialVersionUID = -5945171123065005349L;

	public TypeClassExceptionConcurrentModification() {
		super(new ClassDeclarationExceptionConcurrentModification());
	}

}
