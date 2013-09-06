package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;

/**
 * Expression
 * 
 * @author pcingola
 */
public class ExpressionAssignment extends ExpressionBinary {

	public ExpressionAssignment(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {

		// Get value
		Object value = right.eval(csThread);

		if (left instanceof VarReference) {
			// Get symbol
			VarReference varName = (VarReference) left;
			ScopeSymbol ssym = varName.getScopeSymbol(csThread.getScope());
			// Cast to destination type
			value = right.getReturnType().cast(value);
			ssym.setValue(value);
		} else if (left instanceof VarReferenceList) {
			VarReferenceList listIndex = (VarReferenceList) left;
			listIndex.setValue(csThread, value);
		} else if (left instanceof VarReferenceMap) {
			VarReferenceMap listIndex = (VarReferenceMap) left;
			listIndex.setValue(csThread, value);
		} else throw new RuntimeException("Unimplemented assignment evaluation for type " + left.getReturnType());

		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
		returnType = left.getReturnType();

		return returnType;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		// Is 'left' a variable?
		if (!(left instanceof VarReference) //
				&& (!(left instanceof VarReferenceList)) //
				&& (!(left instanceof VarReferenceMap)) //
		) compilerMessages.add(this, "Assignment to non variable", MessageType.ERROR);
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (!right.getReturnType().canCast(left.getReturnType())) {
			// Can we cast 'right type' into 'left type'?
			compilerMessages.add(this, "Cannot cast " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		}
	}

}
