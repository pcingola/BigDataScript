package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.symbol.SymbolTable;

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

	/**
	 * Find 'type' for 'name'
	 * Also mark this as a 'classField' if the it refers to 'this.name'
	 */
	@Override
	protected Type findType(SymbolTable symtab) {
		// Is 'this' defined (is it a class?)
		TypeClass typeThis = (TypeClass) symtab.getType(ClassDeclaration.THIS);
		if (typeThis == null) return null;

		// Look up 'name' as a field in the class
		Type t = typeThis.getSymbolTable().getType(name);
		classField = (t != null);

		return t;
	}

}
