package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.nativeMethods.list.MethodNativeListSize;
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

	private static final long serialVersionUID = 1093702814601505502L;

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
		String varList = baseVarName() + "list";
		String varCounter = baseVarName() + "count";
		String varMaxCounter = baseVarName() + "max_count";

		// Find native methods
		SymbolTable symtab = returnType.getSymbolTable();
		ValueFunction methodSize = null;
		ValueFunction methodValues = null;

		if (isList()) {
			methodSize = symtab.findFunction(MethodNativeListSize.class);
		} else if (isMap()) {
			// We iterate on the list of map's values
			TypeMap tmap = (TypeMap) returnType;
			TypeList tlist = TypeList.get(tmap.getValueType());
			methodValues = symtab.findFunction(MethodNativeMapValues.class);
			methodSize = tlist.getSymbolTable().findFunction(MethodNativeListSize.class);
		} else throw new RuntimeException("Cannot iterate on type " + returnType);

		//
		// Sample code;
		//   for(var : expressionList) {
		//       statements
		//   }
		//
		// How the loop is executed:
		//   $list = expressionList
		//   $maxCount = $list.size()
		//   for(int $count=0 ; $count < $maxCount ; $count++ ) {
		//     var = list[$count]
		//     statements
		//   }
		//

		if (isNeedsScope()) sb.append("scopepush\n");

		// Evaluate expression and extract list to iterate
		sb.append(expression.toAsm());
		if (expression.isList()) {
			// Evaluate expression: '$list = expressionList'
			sb.append("var " + varList + "\n");
			sb.append("pop\n");
		} else if (expression.isMap()) {
			sb.append("callmnative " + methodValues + "\n");
			sb.append("var " + varList + "\n");
			sb.append("pop\n");
		} else {
			throw new RuntimeException("Cannot iterate on type " + expression.getReturnType());
		}

		// Get list size: '$maxCount = $list.size()'
		sb.append("load " + varList + "\n");
		sb.append("callmnative " + methodSize + "\n");
		sb.append("var " + varMaxCounter + "\n");
		sb.append("pop\n");

		// Loop start
		sb.append(loopInitLabel + ":\n");

		// Initialize variables: 'for(int $count = 0 ;'
		sb.append(vinit.toAsm());
		sb.append("pushi 0\n");
		sb.append("var " + varCounter + "\n");
		sb.append("pop\n");

		// Loop condition: 'for(... ; $count < $maxCount ; ...)'
		sb.append(loopStartLabel + ":\n");
		sb.append("load " + varCounter + "\n");
		sb.append("load " + varMaxCounter + "\n");
		sb.append("lti\n");
		sb.append("jmpf " + loopEndLabel + "\n");

		// Assign loop variable: 'var = list[$count]'
		sb.append("load " + varCounter + "\n");
		sb.append("load " + varList + "\n");
		sb.append("reflist\n");
		sb.append("store " + varName + "\n");
		sb.append("pop\n");

		// Execute statements: 'statements'
		sb.append(statement.toAsm());

		// Loop end part: $i++
		sb.append(loopContinueLabel + ":\n");
		sb.append("load " + varCounter + "\n");
		sb.append("inc\n");
		sb.append("store " + varCounter + "\n");
		sb.append("pop\n");

		// Jump to beginning of loop
		sb.append("jmp " + loopStartLabel + "\n");

		// Loop finished
		sb.append(loopEndLabel + ":\n");
		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
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
