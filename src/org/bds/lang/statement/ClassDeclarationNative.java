package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

public abstract class ClassDeclarationNative extends ClassDeclaration {

	private static final long serialVersionUID = -831104482356629903L;

	public ClassDeclarationNative() {
		this(null, null);
	}

	public ClassDeclarationNative(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		initNativeClass();
	}

	/**
	 * Create class fields
	 */
	protected abstract FieldDeclaration[] createFields();

	/**
	 * Create class methods
	 */
	protected abstract MethodDeclaration[] createMethods();

	protected void initNativeClass() {
		classNameParent = null;
		classParent = null;

		fieldDecl = createFields();
		methodDecl = createMethods();
	}

}
