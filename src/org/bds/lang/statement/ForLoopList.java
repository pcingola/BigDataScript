package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.nativeClasses.exception.ClassDeclarationExceptionConcurrentModification;
import org.bds.lang.nativeMethods.list.MethodNativeListHashCode;
import org.bds.lang.nativeMethods.list.MethodNativeListSize;
import org.bds.lang.nativeMethods.map.MethodNativeMapHashCode;
import org.bds.lang.nativeMethods.map.MethodNativeMapValues;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.ValueFunction;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForLoopList extends StatementWithScope {

	// Note:	It is important that 'begin' node is type-checked before the others in order to
	//			add variables to the scope before ForCondition, ForEnd or Statement uses them.
	//			So the field name should be alphabetically sorted before the other (that's why
	//			I call it 'begin' and not 'init').
	//			Yes, it's a horrible hack.
	VarDeclaration beginVarDecl;

	Expression expression;
	Statement statement;
	String iterableListName;
	String iterableCountName;
	private static final long serialVersionUID = 1093702814601505502L;

	public ForLoopList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;

		if (isTerminal(tree, idx, "for")) idx++; // 'for'
		if (isTerminal(tree, idx, "(")) idx++; // '('
		if (!isTerminal(tree, idx, ":")) beginVarDecl = (VarDeclaration) factory(tree, idx++); // Is this a 'for:beginVarDecl'?
		if (isTerminal(tree, idx, ":")) idx++; // ':'
		if (!isTerminal(tree, idx, ";")) expression = (Expression) factory(tree, idx++); // Is this a 'for:expression'?
		if (isTerminal(tree, idx, ")")) idx++; // ')'

		statement = (Statement) factory(tree, idx++);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		returnType = expression.returnType(symtab);
		return returnType;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		String labelBase = baseLabelName();
		String loopInitLabel = labelBase + "init";
		String loopStartLabel = labelBase + "start";
		String loopContinueLabel = labelBase + "continue";
		String loopEndLabel = labelBase + "end";

		// Loop variable
		VariableInit vinit = beginVarDecl.getVarInit()[0];
		String varName = vinit.getVarName();

		// Internal state variables
		String varExpr = baseVarName() + "expr";
		String varList = baseVarName() + "list";
		String varCounter = baseVarName() + "count";
		String varMaxCounter = baseVarName() + "max_count";
		String varHashCode = baseVarName() + "hash_code";

		// Find native methods
		SymbolTable symtab = returnType.getSymbolTable();
		ValueFunction methodHashCode = null;
		ValueFunction methodSize = null;
		ValueFunction methodValues = null;

		if (isList()) {
			methodSize = symtab.findFunction(MethodNativeListSize.class);
			methodHashCode = symtab.findFunction(MethodNativeListHashCode.class);
		} else if (isMap()) {
			// We iterate on the list of map's values
			TypeMap tmap = (TypeMap) returnType;
			methodValues = symtab.findFunction(MethodNativeMapValues.class);
			methodHashCode = symtab.findFunction(MethodNativeMapHashCode.class);
			TypeList tlist = TypeList.get(tmap.getValueType());
			methodSize = tlist.getSymbolTable().findFunction(MethodNativeListSize.class);
		} else throw new RuntimeException("Cannot iterate on type " + returnType);

		//
		// Sample code;
		//   for(var : expressionList) {
		//       statements
		//   }
		//
		// How the loop is executed:
		//   $expr = expression
		//   $list = expression.values()
		//   $maxCount = $list.size()
		//   $hash_code = $expr.hash()
		//   for(int $count=0 ; $count < $maxCount ; $count++ ) {
		//     var = list[$count]
		//     ...
		//     statements
		//     ...
		//     if( $expr.hash() != $hash_code) throw ConcurrentModification()
		//   }
		//

		if (isNeedsScope()) sb.append("scopepush\n");

		sb.append(toAsmForInitGetList(varExpr, varList, methodValues)); // Evaluate expression and extract list to iterate
		sb.append(toAsmForInitGetMax(varList, methodSize, varMaxCounter)); // Get max loop counter
		sb.append(toAsmForInitGetHashCode(varExpr, methodHashCode, varHashCode)); // Get hashcode
		sb.append(toAsmForStart(loopInitLabel, vinit, varCounter)); // For loop start
		sb.append(toAsmForCond(loopStartLabel, varCounter, varMaxCounter, loopEndLabel)); // For loop conditional
		sb.append(toAsmForVarAssign(varCounter, varList, varName)); // For loop variable assignment
		sb.append(statement.toAsm()); // Execute statements: 'statements' inside the loop
		sb.append(toAsmForEnd(loopContinueLabel, varExpr, varHashCode, methodHashCode, varCounter, loopStartLabel)); // For loop end		// Loop end part

		// Loop finished
		sb.append(loopEndLabel + ":\n");
		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
	}

	protected String toAsmConcurrentModificationCheck(String varExpr, ValueFunction methodHashCode, String varHashCode) {
		String labelBase = baseLabelName();
		String loopConcModOk = labelBase + "concurent_modification_ok";

		return "load " + varExpr + "\n" //
				+ "callnative " + methodHashCode + "\n" //
				+ "load " + varHashCode + "\n" //
				+ "eqi\n" // If equal, the hashcode did not change => OK
				+ "jmpt " + loopConcModOk + "\n" //
				// Concurrent modification check Fail, we need to throw an exception
				+ "new " + ClassDeclarationExceptionConcurrentModification.CLASS_NAME_EXCEPTION_CONCURRENT_MODIFICATION + "\n" // Create a new exception object
				+ "throw \n" // Thow the exception
				+ "" + loopConcModOk + ":\n" // Concurrent modification check OK
		;

	}

	/**
	 * For loop condition:
	 *     for(... ; $count < $maxCount ; ...)
	 */
	protected String toAsmForCond(String loopStartLabel, String varCounter, String varMaxCounter, String loopEndLabel) {
		return loopStartLabel + ":\n" //
				+ "load " + varCounter + "\n" //
				+ "load " + varMaxCounter + "\n" //
				+ "lti\n" //
				+ "jmpf " + loopEndLabel + "\n" //
		;
	}

	/**
	 * For loop end:
	 *     for( .... ; $count++) {
	 */
	protected String toAsmForEnd(String loopContinueLabel, String varExpr, String varHashCode, ValueFunction methodHashCode, String varCounter, String loopStartLabel) {
		return loopContinueLabel + ":\n" //
				+ "node " + id + "\n" //
				+ toAsmConcurrentModificationCheck(varExpr, methodHashCode, varHashCode) // Check concurrent modification
				+ "load " + varCounter + "\n" // Loop end part: $i++
				+ "inc\n" //
				+ "storepop " + varCounter + "\n" //
				+ "jmp " + loopStartLabel + "\n" // Jump to beginning of loop
		;
	}

	/**
	 * For loop initialization: Get list size:
	 *     $maxCount = $list.size()
	 */
	protected String toAsmForInitGetHashCode(String varExpr, ValueFunction methodHashCode, String varHashCode) {
		return "load " + varExpr + "\n" //
				+ "callnative " + methodHashCode + "\n" //
				+ "varpop " + varHashCode + "\n" //
		;
	}

	/**
	 * Evaluate expression and extract list to iterate:
	 *     $list = expressionList
	 */
	protected String toAsmForInitGetList(String varExpr, String varList, ValueFunction methodValues) {
		if (isList()) {
			// Evaluate expression: '$list = expressionList'
			return expression.toAsm() //
					+ "var " + varExpr + "\n" //
					+ "varpop " + varList + "\n" //
			;
		} else if (isMap()) {
			return expression.toAsm() //
					+ "var " + varExpr + "\n" //
					+ "callnative " + methodValues + "\n" //
					+ "varpop " + varList + "\n" //
			;
		} else {
			throw new RuntimeException("Cannot iterate on type " + expression.getReturnType());
		}
	}

	/**
	 * For loop initialization: Get list size:
	 *     $maxCount = $list.size()
	 */
	protected String toAsmForInitGetMax(String varList, ValueFunction methodSize, String varMaxCounter) {
		return "load " + varList + "\n" //
				+ "callnative " + methodSize + "\n" //
				+ "varpop " + varMaxCounter + "\n" //
		;
	}

	/**
	 * For loop start:
	 *     for(int $count=0 ; ...
	 */
	protected String toAsmForStart(String loopInitLabel, VariableInit vinit, String varCounter) {
		return loopInitLabel + ":\n" //
				+ vinit.toAsm() // Initialize variables: 'for(int $count = 0 ;'
				+ "pushi 0\n" //
				+ "varpop " + varCounter + "\n" //
		;
	}

	/**
	 * For loop variable assign (beginning of each iteration)
	 *     for( ... ) {
	 *         var = list[$count]
	 *     }
	 */
	protected String toAsmForVarAssign(String varCounter, String varList, String varName) {
		// Assign loop variable: 'var = list[$count]'
		return "load " + varCounter + "\n" //
				+ "load " + varList + "\n" //
				+ "reflist\n" //
				+ "storepop " + varName + "\n" //
		;

	}

	@Override
	public String toString() {
		return "for( " + beginVarDecl + " : " + expression + " ) {\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
				+ "}" //
		;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		Type exprType = getReturnType();

		if (statement == null) compilerMessages.add(this, "Empty for statement", MessageType.ERROR);

		if (exprType != null) {
			if (!exprType.isList() && !exprType.isMap()) {
				compilerMessages.add(this, "Expression should return a list or a map", MessageType.ERROR);
			} else if (beginVarDecl != null) {
				Type baseType;

				if (exprType.isList()) baseType = ((TypeList) exprType).getElementType();
				else if (exprType.isMap()) baseType = ((TypeMap) exprType).getValueType();
				else {
					compilerMessages.add(this, "Expression should return a list or a map", MessageType.ERROR);
					return;
				}

				Type varType = beginVarDecl.getType();
				if ((baseType != null) && !baseType.canCastTo(varType)) {
					compilerMessages.add(this, "Cannot cast " + baseType + " to " + varType, MessageType.ERROR);
				}
			}
		}
	}
}
