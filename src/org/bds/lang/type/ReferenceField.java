package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A reference to a field in a class
 *
 * @author pcingola
 */
public class ReferenceField extends ReferenceVar {

	//	/**
	//	 * Create a reference form a string
	//	 */
	//	public static Expression factory(BdsNode parent, String var) {
	//		if (var == null || var.isEmpty()) return null;
	//
	//		int idxCurly = var.indexOf('{');
	//		int idxBrace = var.indexOf('[');
	//
	//		Reference varRef = null;
	//		if (idxCurly < 0 && idxBrace < 0) varRef = new ReferenceField(parent, null);
	//		else if (idxCurly < 0 && idxBrace > 0) varRef = new ReferenceList(parent, null);
	//		else if (idxCurly > 0 && idxBrace < 0) varRef = new ReferenceMap(parent, null);
	//		else if (idxBrace < idxCurly) varRef = new ReferenceList(parent, null);
	//		else varRef = new ReferenceMap(parent, null);
	//
	//		// Parse string
	//		varRef.parse(var);
	//		return varRef;
	//	}

	public ReferenceField(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

}
