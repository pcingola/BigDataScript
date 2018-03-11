package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.value.Value;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

	protected String className;
	protected ClassDeclaration classDecl;

	public TypeClass(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		primitiveType = PrimitiveType.CLASS;
	}

	public TypeClass(ClassDeclaration classDeclaration) {
		super(PrimitiveType.CLASS);
		classDecl = classDeclaration;
		className = classDecl.getClassName();
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		String tcn = ((TypeClass) type).getClassName();

		// Compare if null
		if ((className == null) && (tcn == null)) return 0;
		if ((className != null) && (tcn == null)) return 1;
		if ((className == null) && (tcn != null)) return -1;

		// Compare names
		return className.compareTo(tcn);
	}

	public ClassDeclaration getClassDeclaration() {
		return classDecl;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public boolean isClass() {
		return true;
	}

	@Override
	public Value newValue() {
		return Value.NULL; // Default value for an object is null.
	}

	@Override
	protected void parse(ParseTree tree) {
		className = tree.getChild(0).getText();
	}

	@Override
	public String toString() {
		return className;
	}

}
