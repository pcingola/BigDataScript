// Generated from BigDataScript.g4 by ANTLR 4.1
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import org.antlr.v4.runtime.misc.NotNull;
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
	 * Visit a parse tree produced by {@link BigDataScriptParser#statementVarDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementVarDeclaration(@NotNull BigDataScriptParser.StatementVarDeclarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(@NotNull BigDataScriptParser.MethodCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(@NotNull BigDataScriptParser.ExpressionListContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionMinus(@NotNull BigDataScriptParser.ExpressionMinusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalReal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralReal(@NotNull BigDataScriptParser.LiteralRealContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#kill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKill(@NotNull BigDataScriptParser.KillContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralMap(@NotNull BigDataScriptParser.LiteralMapContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralList(@NotNull BigDataScriptParser.LiteralListContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#wait}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWait(@NotNull BigDataScriptParser.WaitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionLe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLe(@NotNull BigDataScriptParser.ExpressionLeContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentDiv(@NotNull BigDataScriptParser.ExpressionAssignmentDivContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionLogicAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicAnd(@NotNull BigDataScriptParser.ExpressionLogicAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentBitOr(@NotNull BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionDivide}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDivide(@NotNull BigDataScriptParser.ExpressionDivideContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#break}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(@NotNull BigDataScriptParser.BreakContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignment(@NotNull BigDataScriptParser.ExpressionAssignmentContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(@NotNull BigDataScriptParser.FunctionCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramUnit(@NotNull BigDataScriptParser.ProgramUnitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalBool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralBool(@NotNull BigDataScriptParser.LiteralBoolContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionGe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGe(@NotNull BigDataScriptParser.ExpressionGeContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionBitNegation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitNegation(@NotNull BigDataScriptParser.ExpressionBitNegationContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEol(@NotNull BigDataScriptParser.EolContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forLoopList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoopList(@NotNull BigDataScriptParser.ForLoopListContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#statmentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatmentExpr(@NotNull BigDataScriptParser.StatmentExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typePrimitiveString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimitiveString(@NotNull BigDataScriptParser.TypePrimitiveStringContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoop(@NotNull BigDataScriptParser.ForLoopContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#varReferenceMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReferenceMap(@NotNull BigDataScriptParser.VarReferenceMapContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionLogicOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicOr(@NotNull BigDataScriptParser.ExpressionLogicOrContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentPlus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentPlus(@NotNull BigDataScriptParser.ExpressionAssignmentPlusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typeMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeMap(@NotNull BigDataScriptParser.TypeMapContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#statementInclude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementInclude(@NotNull BigDataScriptParser.StatementIncludeContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInt(@NotNull BigDataScriptParser.LiteralIntContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionGt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGt(@NotNull BigDataScriptParser.ExpressionGtContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionTask}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTask(@NotNull BigDataScriptParser.ExpressionTaskContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typePrimitiveInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimitiveInt(@NotNull BigDataScriptParser.TypePrimitiveIntContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentMult(@NotNull BigDataScriptParser.ExpressionAssignmentMultContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#exit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit(@NotNull BigDataScriptParser.ExitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typePrimitiveReal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimitiveReal(@NotNull BigDataScriptParser.TypePrimitiveRealContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionUnaryPlus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionUnaryPlus(@NotNull BigDataScriptParser.ExpressionUnaryPlusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitImplicit(@NotNull BigDataScriptParser.VariableInitImplicitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionBitXor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitXor(@NotNull BigDataScriptParser.ExpressionBitXorContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalMapEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralMapEmpty(@NotNull BigDataScriptParser.LiteralMapEmptyContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(@NotNull BigDataScriptParser.TypeListContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionDep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDep(@NotNull BigDataScriptParser.ExpressionDepContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#varReferenceList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReferenceList(@NotNull BigDataScriptParser.VarReferenceListContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentBitAnd(@NotNull BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionSys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionSys(@NotNull BigDataScriptParser.ExpressionSysContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionBitAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitAnd(@NotNull BigDataScriptParser.ExpressionBitAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull BigDataScriptParser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionUnaryMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionUnaryMinus(@NotNull BigDataScriptParser.ExpressionUnaryMinusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeFile(@NotNull BigDataScriptParser.IncludeFileContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalListEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralListEmpty(@NotNull BigDataScriptParser.LiteralListEmptyContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#while}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(@NotNull BigDataScriptParser.WhileContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typePrimitiveVoid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimitiveVoid(@NotNull BigDataScriptParser.TypePrimitiveVoidContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionPlus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionPlus(@NotNull BigDataScriptParser.ExpressionPlusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(@NotNull BigDataScriptParser.IfContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionNe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionNe(@NotNull BigDataScriptParser.ExpressionNeContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#statmentEol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatmentEol(@NotNull BigDataScriptParser.StatmentEolContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(@NotNull BigDataScriptParser.VarDeclarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInit(@NotNull BigDataScriptParser.VariableInitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionLogicNot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLogicNot(@NotNull BigDataScriptParser.ExpressionLogicNotContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#post}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPost(@NotNull BigDataScriptParser.PostContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionCond(@NotNull BigDataScriptParser.ExpressionCondContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionBitOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBitOr(@NotNull BigDataScriptParser.ExpressionBitOrContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(@NotNull BigDataScriptParser.ErrorContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEnd(@NotNull BigDataScriptParser.ForEndContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#varReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReference(@NotNull BigDataScriptParser.VarReferenceContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionTaskLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTaskLiteral(@NotNull BigDataScriptParser.ExpressionTaskLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(@NotNull BigDataScriptParser.ReturnContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#checkpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckpoint(@NotNull BigDataScriptParser.CheckpointContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionEq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionEq(@NotNull BigDataScriptParser.ExpressionEqContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCondition(@NotNull BigDataScriptParser.ForConditionContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionModulo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionModulo(@NotNull BigDataScriptParser.ExpressionModuloContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typePrimitiveBool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimitiveBool(@NotNull BigDataScriptParser.TypePrimitiveBoolContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionVariableInitImplicit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionVariableInitImplicit(@NotNull BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionParen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionParen(@NotNull BigDataScriptParser.ExpressionParenContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#literalString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralString(@NotNull BigDataScriptParser.LiteralStringContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#pre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPre(@NotNull BigDataScriptParser.PreContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#continue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(@NotNull BigDataScriptParser.ContinueContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAssignmentMinus(@NotNull BigDataScriptParser.ExpressionAssignmentMinusContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionLt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLt(@NotNull BigDataScriptParser.ExpressionLtContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#typeArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArray(@NotNull BigDataScriptParser.TypeArrayContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(@NotNull BigDataScriptParser.FunctionDeclarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(@NotNull BigDataScriptParser.ForInitContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionTimes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionTimes(@NotNull BigDataScriptParser.ExpressionTimesContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#expressionExec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionExec(@NotNull BigDataScriptParser.ExpressionExecContext ctx);

	/**
	 * Visit a parse tree produced by {@link BigDataScriptParser#warning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWarning(@NotNull BigDataScriptParser.WarningContext ctx);
}