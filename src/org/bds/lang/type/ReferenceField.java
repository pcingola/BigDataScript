package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A reference to a field in a class
 *
 * @author pcingola
 */
public class ReferenceField extends ReferenceVar {

	public ReferenceField(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		classField = true;
	}

	void f() {

		Z z = new Z();

	}

}

class A {
	public Z z;
}

class Z {
	public A a;
}
