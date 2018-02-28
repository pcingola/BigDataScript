// Generated from BigDataScript.g4 by ANTLR 4.7.1
package org.bds.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BigDataScriptParser}.
 */
public interface BigDataScriptListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 */
	void enterProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 */
	void exitProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 */
	void enterEol(BigDataScriptParser.EolContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 */
	void exitEol(BigDataScriptParser.EolContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(BigDataScriptParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(BigDataScriptParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typePrimitiveString}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveString(BigDataScriptParser.TypePrimitiveStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typePrimitiveString}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveString(BigDataScriptParser.TypePrimitiveStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeArray(BigDataScriptParser.TypeArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeArray(BigDataScriptParser.TypeArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typePrimitiveVoid}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveVoid(BigDataScriptParser.TypePrimitiveVoidContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typePrimitiveVoid}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveVoid(BigDataScriptParser.TypePrimitiveVoidContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeMap}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeMap(BigDataScriptParser.TypeMapContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeMap}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeMap(BigDataScriptParser.TypeMapContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typePrimitiveReal}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveReal(BigDataScriptParser.TypePrimitiveRealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typePrimitiveReal}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveReal(BigDataScriptParser.TypePrimitiveRealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeClass}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeClass(BigDataScriptParser.TypeClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeClass}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeClass(BigDataScriptParser.TypeClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typePrimitiveBool}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveBool(BigDataScriptParser.TypePrimitiveBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typePrimitiveBool}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveBool(BigDataScriptParser.TypePrimitiveBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typePrimitiveInt}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveInt(BigDataScriptParser.TypePrimitiveIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typePrimitiveInt}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveInt(BigDataScriptParser.TypePrimitiveIntContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(BigDataScriptParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(BigDataScriptParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 */
	void enterVariableInit(BigDataScriptParser.VariableInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 */
	void exitVariableInit(BigDataScriptParser.VariableInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitImplicit(BigDataScriptParser.VariableInitImplicitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitImplicit(BigDataScriptParser.VariableInitImplicitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 */
	void enterIncludeFile(BigDataScriptParser.IncludeFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 */
	void exitIncludeFile(BigDataScriptParser.IncludeFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock(BigDataScriptParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock(BigDataScriptParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak(BigDataScriptParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak(BigDataScriptParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakpoint(BigDataScriptParser.BreakpointContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakpoint(BigDataScriptParser.BreakpointContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCheckpoint(BigDataScriptParser.CheckpointContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCheckpoint(BigDataScriptParser.CheckpointContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue(BigDataScriptParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue(BigDataScriptParser.ContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code debug}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDebug(BigDataScriptParser.DebugContext ctx);
	/**
	 * Exit a parse tree produced by the {@code debug}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDebug(BigDataScriptParser.DebugContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exit}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExit(BigDataScriptParser.ExitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exit}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExit(BigDataScriptParser.ExitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code print}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrint(BigDataScriptParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code print}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrint(BigDataScriptParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code println}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrintln(BigDataScriptParser.PrintlnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code println}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrintln(BigDataScriptParser.PrintlnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code warning}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWarning(BigDataScriptParser.WarningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code warning}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWarning(BigDataScriptParser.WarningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code error}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterError(BigDataScriptParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code error}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitError(BigDataScriptParser.ErrorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forLoop}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForLoop(BigDataScriptParser.ForLoopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forLoop}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForLoop(BigDataScriptParser.ForLoopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forLoopList}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForLoopList(BigDataScriptParser.ForLoopListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forLoopList}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForLoopList(BigDataScriptParser.ForLoopListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIf(BigDataScriptParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIf(BigDataScriptParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code kill}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterKill(BigDataScriptParser.KillContext ctx);
	/**
	 * Exit a parse tree produced by the {@code kill}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitKill(BigDataScriptParser.KillContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn(BigDataScriptParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn(BigDataScriptParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wait}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWait(BigDataScriptParser.WaitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wait}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWait(BigDataScriptParser.WaitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code switch}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitch(BigDataScriptParser.SwitchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code switch}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitch(BigDataScriptParser.SwitchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile(BigDataScriptParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile(BigDataScriptParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementVarDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementVarDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementClassDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementClassDeclaration(BigDataScriptParser.StatementClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementClassDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementClassDeclaration(BigDataScriptParser.StatementClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementExpr}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpr(BigDataScriptParser.StatementExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementExpr}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpr(BigDataScriptParser.StatementExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementInclude}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementInclude(BigDataScriptParser.StatementIncludeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementInclude}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementInclude(BigDataScriptParser.StatementIncludeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code help}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterHelp(BigDataScriptParser.HelpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code help}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitHelp(BigDataScriptParser.HelpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statmentEol}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatmentEol(BigDataScriptParser.StatmentEolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statmentEol}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatmentEol(BigDataScriptParser.StatmentEolContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(BigDataScriptParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(BigDataScriptParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void enterForCondition(BigDataScriptParser.ForConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void exitForCondition(BigDataScriptParser.ForConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 */
	void enterForEnd(BigDataScriptParser.ForEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 */
	void exitForEnd(BigDataScriptParser.ForEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLogicAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLogicAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentList(BigDataScriptParser.ExpressionAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentList(BigDataScriptParser.ExpressionAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionEq}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionEq}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionDepOperator}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionDepOperator(BigDataScriptParser.ExpressionDepOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionDepOperator}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionDepOperator(BigDataScriptParser.ExpressionDepOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionNe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionNe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionBitXor}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionBitXor}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionBitNegation}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionBitNegation}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code post}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPost(BigDataScriptParser.PostContext ctx);
	/**
	 * Exit a parse tree produced by the {@code post}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPost(BigDataScriptParser.PostContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterReferenceMap(BigDataScriptParser.ReferenceMapContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitReferenceMap(BigDataScriptParser.ReferenceMapContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLogicNot}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLogicNot}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionVariableInitImplicit}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionVariableInitImplicit(BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionVariableInitImplicit}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionVariableInitImplicit(BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentMult}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentMult(BigDataScriptParser.ExpressionAssignmentMultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentMult}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentMult(BigDataScriptParser.ExpressionAssignmentMultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionDep}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionDep}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentDiv}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentDiv(BigDataScriptParser.ExpressionAssignmentDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentDiv}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentDiv(BigDataScriptParser.ExpressionAssignmentDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pre}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPre(BigDataScriptParser.PreContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pre}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPre(BigDataScriptParser.PreContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionUnaryPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionUnaryPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLogicOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLogicOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionParallel}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionParallel(BigDataScriptParser.ExpressionParallelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionParallel}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionParallel(BigDataScriptParser.ExpressionParallelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalBool}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalBool}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionGoal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionGoal(BigDataScriptParser.ExpressionGoalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionGoal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionGoal(BigDataScriptParser.ExpressionGoalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionTimes}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionTimes}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(BigDataScriptParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(BigDataScriptParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionCond}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionCond}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionUnaryMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionUnaryMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalInt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInt(BigDataScriptParser.LiteralIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalInt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInt(BigDataScriptParser.LiteralIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalMapEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralMapEmpty(BigDataScriptParser.LiteralMapEmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalMapEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralMapEmpty(BigDataScriptParser.LiteralMapEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(BigDataScriptParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(BigDataScriptParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalNull}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralNull(BigDataScriptParser.LiteralNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalNull}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralNull(BigDataScriptParser.LiteralNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalString}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralString(BigDataScriptParser.LiteralStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalString}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralString(BigDataScriptParser.LiteralStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionGt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionGt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionModulo}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionModulo}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentBitAnd(BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentBitAnd(BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralMap(BigDataScriptParser.LiteralMapContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralMap(BigDataScriptParser.LiteralMapContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentBitOr(BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentBitOr(BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionTask}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionTask}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceVar}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterReferenceVar(BigDataScriptParser.ReferenceVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceVar}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitReferenceVar(BigDataScriptParser.ReferenceVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionSys}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionSys}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentMinus(BigDataScriptParser.ExpressionAssignmentMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentMinus(BigDataScriptParser.ExpressionAssignmentMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterReferenceList(BigDataScriptParser.ReferenceListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitReferenceList(BigDataScriptParser.ReferenceListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalListEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralListEmpty(BigDataScriptParser.LiteralListEmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalListEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralListEmpty(BigDataScriptParser.LiteralListEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionTaskLiteral}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionTaskLiteral}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionDivide}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionDivide}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignment}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignment}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalReal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralReal(BigDataScriptParser.LiteralRealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalReal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralReal(BigDataScriptParser.LiteralRealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAssignmentPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentPlus(BigDataScriptParser.ExpressionAssignmentPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAssignmentPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentPlus(BigDataScriptParser.ExpressionAssignmentPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionGe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionGe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralList(BigDataScriptParser.LiteralListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralList(BigDataScriptParser.LiteralListContext ctx);
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(BigDataScriptParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(BigDataScriptParser.ExpressionListContext ctx);
}