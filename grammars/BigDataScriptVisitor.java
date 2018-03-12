// Generated from BigDataScript.g4 by ANTLR 4.7.1
package org.bds.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BigDataScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BigDataScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEol(BigDataScriptParser.EolContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(BigDataScriptParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArray(BigDataScriptParser.TypeArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeInt}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeInt(BigDataScriptParser.TypeIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeMap}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeMap(BigDataScriptParser.TypeMapContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeReal}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReal(BigDataScriptParser.TypeRealContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeString}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeString(BigDataScriptParser.TypeStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeClass}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeClass(BigDataScriptParser.TypeClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeBool}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBool(BigDataScriptParser.TypeBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeVoid}
	 * labeled alternative in {@link BigDataScriptParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeVoid(BigDataScriptParser.TypeVoidContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#classDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDef(BigDataScriptParser.ClassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInit(BigDataScriptParser.VariableInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitImplicit(BigDataScriptParser.VariableInitImplicitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeFile(BigDataScriptParser.IncludeFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code block}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(BigDataScriptParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(BigDataScriptParser.BreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakpoint(BigDataScriptParser.BreakpointContext ctx);
	/**
	 * Visit a parse tree produced by the {@code checkpoint}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckpoint(BigDataScriptParser.CheckpointContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(BigDataScriptParser.ContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code debug}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDebug(BigDataScriptParser.DebugContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exit}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit(BigDataScriptParser.ExitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code print}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(BigDataScriptParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code println}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintln(BigDataScriptParser.PrintlnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code warning}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWarning(BigDataScriptParser.WarningContext ctx);
	/**
	 * Visit a parse tree produced by the {@code error}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(BigDataScriptParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forLoop}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoop(BigDataScriptParser.ForLoopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forLoopList}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoopList(BigDataScriptParser.ForLoopListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(BigDataScriptParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code kill}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKill(BigDataScriptParser.KillContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(BigDataScriptParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wait}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait(BigDataScriptParser.WaitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code switch}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch(BigDataScriptParser.SwitchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(BigDataScriptParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementVarDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classDeclaration}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(BigDataScriptParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementExpr}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpr(BigDataScriptParser.StatementExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementInclude}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementInclude(BigDataScriptParser.StatementIncludeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code help}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHelp(BigDataScriptParser.HelpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statmentEol}
	 * labeled alternative in {@link BigDataScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatmentEol(BigDataScriptParser.StatmentEolContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(BigDataScriptParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCondition(BigDataScriptParser.ForConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEnd(BigDataScriptParser.ForEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLogicAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentList(BigDataScriptParser.ExpressionAssignmentListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionEq}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionDepOperator}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDepOperator(BigDataScriptParser.ExpressionDepOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionNe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionBitXor}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionBitNegation}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code post}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPost(BigDataScriptParser.PostContext ctx);
	/**
	 * Visit a parse tree produced by the {@code referenceMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceMap(BigDataScriptParser.ReferenceMapContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLogicNot}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionVariableInitImplicit}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionVariableInitImplicit(BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentMult}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentMult(BigDataScriptParser.ExpressionAssignmentMultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionDep}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentDiv}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentDiv(BigDataScriptParser.ExpressionAssignmentDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pre}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPre(BigDataScriptParser.PreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionUnaryPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLogicOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionParallel}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionParallel(BigDataScriptParser.ExpressionParallelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalBool}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionGoal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGoal(BigDataScriptParser.ExpressionGoalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionTimes}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(BigDataScriptParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionCond}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionUnaryMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalInt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInt(BigDataScriptParser.LiteralIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalMapEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralMapEmpty(BigDataScriptParser.LiteralMapEmptyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCall}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(BigDataScriptParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalNull}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralNull(BigDataScriptParser.LiteralNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalString}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralString(BigDataScriptParser.LiteralStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionGt}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionModulo}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentBitAnd}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentBitAnd(BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalMap}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralMap(BigDataScriptParser.LiteralMapContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentBitOr}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentBitOr(BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionTask}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);
	/**
	 * Visit a parse tree produced by the {@code referenceVar}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceVar(BigDataScriptParser.ReferenceVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionSys}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentMinus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentMinus(BigDataScriptParser.ExpressionAssignmentMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code referenceClass}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceClass(BigDataScriptParser.ReferenceClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code referenceList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceList(BigDataScriptParser.ReferenceListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalListEmpty}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralListEmpty(BigDataScriptParser.LiteralListEmptyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionTaskLiteral}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionDivide}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignment}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalReal}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralReal(BigDataScriptParser.LiteralRealContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAssignmentPlus}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentPlus(BigDataScriptParser.ExpressionAssignmentPlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionGe}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalList}
	 * labeled alternative in {@link BigDataScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralList(BigDataScriptParser.LiteralListContext ctx);
	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(BigDataScriptParser.ExpressionListContext ctx);
}