// Generated from BigDataScript.g4 by ANTLR 4.2.2
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BigDataScriptParser}.
 */
public interface BigDataScriptListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#checkpoint}.
	 * @param ctx the parse tree
	 */
	void enterCheckpoint(@NotNull BigDataScriptParser.CheckpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#checkpoint}.
	 * @param ctx the parse tree
	 */
	void exitCheckpoint(@NotNull BigDataScriptParser.CheckpointContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionLogicAnd}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicAnd(@NotNull BigDataScriptParser.ExpressionLogicAndContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionLogicAnd}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicAnd(@NotNull BigDataScriptParser.ExpressionLogicAndContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typePrimitiveString}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveString(@NotNull BigDataScriptParser.TypePrimitiveStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typePrimitiveString}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveString(@NotNull BigDataScriptParser.TypePrimitiveStringContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 */
	void enterEol(@NotNull BigDataScriptParser.EolContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#eol}.
	 * @param ctx the parse tree
	 */
	void exitEol(@NotNull BigDataScriptParser.EolContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#statementInclude}.
	 * @param ctx the parse tree
	 */
	void enterStatementInclude(@NotNull BigDataScriptParser.StatementIncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#statementInclude}.
	 * @param ctx the parse tree
	 */
	void exitStatementInclude(@NotNull BigDataScriptParser.StatementIncludeContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#varReferenceMap}.
	 * @param ctx the parse tree
	 */
	void enterVarReferenceMap(@NotNull BigDataScriptParser.VarReferenceMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#varReferenceMap}.
	 * @param ctx the parse tree
	 */
	void exitVarReferenceMap(@NotNull BigDataScriptParser.VarReferenceMapContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionEq}.
	 * @param ctx the parse tree
	 */
	void enterExpressionEq(@NotNull BigDataScriptParser.ExpressionEqContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionEq}.
	 * @param ctx the parse tree
	 */
	void exitExpressionEq(@NotNull BigDataScriptParser.ExpressionEqContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionMinus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionMinus(@NotNull BigDataScriptParser.ExpressionMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionMinus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionMinus(@NotNull BigDataScriptParser.ExpressionMinusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#varReferenceList}.
	 * @param ctx the parse tree
	 */
	void enterVarReferenceList(@NotNull BigDataScriptParser.VarReferenceListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#varReferenceList}.
	 * @param ctx the parse tree
	 */
	void exitVarReferenceList(@NotNull BigDataScriptParser.VarReferenceListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#while}.
	 * @param ctx the parse tree
	 */
	void enterWhile(@NotNull BigDataScriptParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#while}.
	 * @param ctx the parse tree
	 */
	void exitWhile(@NotNull BigDataScriptParser.WhileContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 */
	void enterForEnd(@NotNull BigDataScriptParser.ForEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forEnd}.
	 * @param ctx the parse tree
	 */
	void exitForEnd(@NotNull BigDataScriptParser.ForEndContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionNe}.
	 * @param ctx the parse tree
	 */
	void enterExpressionNe(@NotNull BigDataScriptParser.ExpressionNeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionNe}.
	 * @param ctx the parse tree
	 */
	void exitExpressionNe(@NotNull BigDataScriptParser.ExpressionNeContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionBitXor}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitXor(@NotNull BigDataScriptParser.ExpressionBitXorContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionBitXor}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitXor(@NotNull BigDataScriptParser.ExpressionBitXorContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionBitNegation}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitNegation(@NotNull BigDataScriptParser.ExpressionBitNegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionBitNegation}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitNegation(@NotNull BigDataScriptParser.ExpressionBitNegationContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void enterForCondition(@NotNull BigDataScriptParser.ForConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void exitForCondition(@NotNull BigDataScriptParser.ForConditionContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 */
	void enterProgramUnit(@NotNull BigDataScriptParser.ProgramUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#programUnit}.
	 * @param ctx the parse tree
	 */
	void exitProgramUnit(@NotNull BigDataScriptParser.ProgramUnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionBitAnd}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitAnd(@NotNull BigDataScriptParser.ExpressionBitAndContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionBitAnd}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitAnd(@NotNull BigDataScriptParser.ExpressionBitAndContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#post}.
	 * @param ctx the parse tree
	 */
	void enterPost(@NotNull BigDataScriptParser.PostContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#post}.
	 * @param ctx the parse tree
	 */
	void exitPost(@NotNull BigDataScriptParser.PostContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#continue}.
	 * @param ctx the parse tree
	 */
	void enterContinue(@NotNull BigDataScriptParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#continue}.
	 * @param ctx the parse tree
	 */
	void exitContinue(@NotNull BigDataScriptParser.ContinueContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#warning}.
	 * @param ctx the parse tree
	 */
	void enterWarning(@NotNull BigDataScriptParser.WarningContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#warning}.
	 * @param ctx the parse tree
	 */
	void exitWarning(@NotNull BigDataScriptParser.WarningContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(@NotNull BigDataScriptParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(@NotNull BigDataScriptParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionLogicNot}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicNot(@NotNull BigDataScriptParser.ExpressionLogicNotContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionLogicNot}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicNot(@NotNull BigDataScriptParser.ExpressionLogicNotContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forLoop}.
	 * @param ctx the parse tree
	 */
	void enterForLoop(@NotNull BigDataScriptParser.ForLoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forLoop}.
	 * @param ctx the parse tree
	 */
	void exitForLoop(@NotNull BigDataScriptParser.ForLoopContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forLoopList}.
	 * @param ctx the parse tree
	 */
	void enterForLoopList(@NotNull BigDataScriptParser.ForLoopListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forLoopList}.
	 * @param ctx the parse tree
	 */
	void exitForLoopList(@NotNull BigDataScriptParser.ForLoopListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#if}.
	 * @param ctx the parse tree
	 */
	void enterIf(@NotNull BigDataScriptParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#if}.
	 * @param ctx the parse tree
	 */
	void exitIf(@NotNull BigDataScriptParser.IfContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionVariableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void enterExpressionVariableInitImplicit(@NotNull BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionVariableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void exitExpressionVariableInitImplicit(@NotNull BigDataScriptParser.ExpressionVariableInitImplicitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMult}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentMult(@NotNull BigDataScriptParser.ExpressionAssignmentMultContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMult}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentMult(@NotNull BigDataScriptParser.ExpressionAssignmentMultContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionDep}.
	 * @param ctx the parse tree
	 */
	void enterExpressionDep(@NotNull BigDataScriptParser.ExpressionDepContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionDep}.
	 * @param ctx the parse tree
	 */
	void exitExpressionDep(@NotNull BigDataScriptParser.ExpressionDepContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionLt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLt(@NotNull BigDataScriptParser.ExpressionLtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionLt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLt(@NotNull BigDataScriptParser.ExpressionLtContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentDiv}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentDiv(@NotNull BigDataScriptParser.ExpressionAssignmentDivContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentDiv}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentDiv(@NotNull BigDataScriptParser.ExpressionAssignmentDivContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#pre}.
	 * @param ctx the parse tree
	 */
	void enterPre(@NotNull BigDataScriptParser.PreContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#pre}.
	 * @param ctx the parse tree
	 */
	void exitPre(@NotNull BigDataScriptParser.PreContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionUnaryPlus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionUnaryPlus(@NotNull BigDataScriptParser.ExpressionUnaryPlusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionUnaryPlus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionUnaryPlus(@NotNull BigDataScriptParser.ExpressionUnaryPlusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typePrimitiveVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveVoid(@NotNull BigDataScriptParser.TypePrimitiveVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typePrimitiveVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveVoid(@NotNull BigDataScriptParser.TypePrimitiveVoidContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 */
	void enterIncludeFile(@NotNull BigDataScriptParser.IncludeFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#includeFile}.
	 * @param ctx the parse tree
	 */
	void exitIncludeFile(@NotNull BigDataScriptParser.IncludeFileContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionLogicOr}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLogicOr(@NotNull BigDataScriptParser.ExpressionLogicOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionLogicOr}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLogicOr(@NotNull BigDataScriptParser.ExpressionLogicOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typePrimitiveReal}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveReal(@NotNull BigDataScriptParser.TypePrimitiveRealContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typePrimitiveReal}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveReal(@NotNull BigDataScriptParser.TypePrimitiveRealContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalBool}.
	 * @param ctx the parse tree
	 */
	void enterLiteralBool(@NotNull BigDataScriptParser.LiteralBoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalBool}.
	 * @param ctx the parse tree
	 */
	void exitLiteralBool(@NotNull BigDataScriptParser.LiteralBoolContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(@NotNull BigDataScriptParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(@NotNull BigDataScriptParser.VarDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionTimes}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTimes(@NotNull BigDataScriptParser.ExpressionTimesContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionTimes}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTimes(@NotNull BigDataScriptParser.ExpressionTimesContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#exit}.
	 * @param ctx the parse tree
	 */
	void enterExit(@NotNull BigDataScriptParser.ExitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#exit}.
	 * @param ctx the parse tree
	 */
	void exitExit(@NotNull BigDataScriptParser.ExitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(@NotNull BigDataScriptParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(@NotNull BigDataScriptParser.ExpressionListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionPlus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionPlus(@NotNull BigDataScriptParser.ExpressionPlusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionPlus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionPlus(@NotNull BigDataScriptParser.ExpressionPlusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(@NotNull BigDataScriptParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(@NotNull BigDataScriptParser.FunctionCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionParen}.
	 * @param ctx the parse tree
	 */
	void enterExpressionParen(@NotNull BigDataScriptParser.ExpressionParenContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionParen}.
	 * @param ctx the parse tree
	 */
	void exitExpressionParen(@NotNull BigDataScriptParser.ExpressionParenContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionCond}.
	 * @param ctx the parse tree
	 */
	void enterExpressionCond(@NotNull BigDataScriptParser.ExpressionCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionCond}.
	 * @param ctx the parse tree
	 */
	void exitExpressionCond(@NotNull BigDataScriptParser.ExpressionCondContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionUnaryMinus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionUnaryMinus(@NotNull BigDataScriptParser.ExpressionUnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionUnaryMinus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionUnaryMinus(@NotNull BigDataScriptParser.ExpressionUnaryMinusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionBitOr}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBitOr(@NotNull BigDataScriptParser.ExpressionBitOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionBitOr}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBitOr(@NotNull BigDataScriptParser.ExpressionBitOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalInt}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInt(@NotNull BigDataScriptParser.LiteralIntContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalInt}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInt(@NotNull BigDataScriptParser.LiteralIntContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalMapEmpty}.
	 * @param ctx the parse tree
	 */
	void enterLiteralMapEmpty(@NotNull BigDataScriptParser.LiteralMapEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalMapEmpty}.
	 * @param ctx the parse tree
	 */
	void exitLiteralMapEmpty(@NotNull BigDataScriptParser.LiteralMapEmptyContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(@NotNull BigDataScriptParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(@NotNull BigDataScriptParser.MethodCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#statementVarDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStatementVarDeclaration(@NotNull BigDataScriptParser.StatementVarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#statementVarDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStatementVarDeclaration(@NotNull BigDataScriptParser.StatementVarDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#wait}.
	 * @param ctx the parse tree
	 */
	void enterWait(@NotNull BigDataScriptParser.WaitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#wait}.
	 * @param ctx the parse tree
	 */
	void exitWait(@NotNull BigDataScriptParser.WaitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalString}.
	 * @param ctx the parse tree
	 */
	void enterLiteralString(@NotNull BigDataScriptParser.LiteralStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalString}.
	 * @param ctx the parse tree
	 */
	void exitLiteralString(@NotNull BigDataScriptParser.LiteralStringContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionGt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionGt(@NotNull BigDataScriptParser.ExpressionGtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionGt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionGt(@NotNull BigDataScriptParser.ExpressionGtContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionModulo}.
	 * @param ctx the parse tree
	 */
	void enterExpressionModulo(@NotNull BigDataScriptParser.ExpressionModuloContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionModulo}.
	 * @param ctx the parse tree
	 */
	void exitExpressionModulo(@NotNull BigDataScriptParser.ExpressionModuloContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typePrimitiveBool}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveBool(@NotNull BigDataScriptParser.TypePrimitiveBoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typePrimitiveBool}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveBool(@NotNull BigDataScriptParser.TypePrimitiveBoolContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typePrimitiveInt}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimitiveInt(@NotNull BigDataScriptParser.TypePrimitiveIntContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typePrimitiveInt}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimitiveInt(@NotNull BigDataScriptParser.TypePrimitiveIntContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(@NotNull BigDataScriptParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(@NotNull BigDataScriptParser.ErrorContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitAnd}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentBitAnd(@NotNull BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitAnd}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentBitAnd(@NotNull BigDataScriptParser.ExpressionAssignmentBitAndContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionLe}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLe(@NotNull BigDataScriptParser.ExpressionLeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionLe}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLe(@NotNull BigDataScriptParser.ExpressionLeContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalMap}.
	 * @param ctx the parse tree
	 */
	void enterLiteralMap(@NotNull BigDataScriptParser.LiteralMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalMap}.
	 * @param ctx the parse tree
	 */
	void exitLiteralMap(@NotNull BigDataScriptParser.LiteralMapContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitOr}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentBitOr(@NotNull BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentBitOr}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentBitOr(@NotNull BigDataScriptParser.ExpressionAssignmentBitOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(@NotNull BigDataScriptParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(@NotNull BigDataScriptParser.TypeListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionTask}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTask(@NotNull BigDataScriptParser.ExpressionTaskContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionTask}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTask(@NotNull BigDataScriptParser.ExpressionTaskContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionSys}.
	 * @param ctx the parse tree
	 */
	void enterExpressionSys(@NotNull BigDataScriptParser.ExpressionSysContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionSys}.
	 * @param ctx the parse tree
	 */
	void exitExpressionSys(@NotNull BigDataScriptParser.ExpressionSysContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMinus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentMinus(@NotNull BigDataScriptParser.ExpressionAssignmentMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentMinus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentMinus(@NotNull BigDataScriptParser.ExpressionAssignmentMinusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalListEmpty}.
	 * @param ctx the parse tree
	 */
	void enterLiteralListEmpty(@NotNull BigDataScriptParser.LiteralListEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalListEmpty}.
	 * @param ctx the parse tree
	 */
	void exitLiteralListEmpty(@NotNull BigDataScriptParser.LiteralListEmptyContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typeMap}.
	 * @param ctx the parse tree
	 */
	void enterTypeMap(@NotNull BigDataScriptParser.TypeMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typeMap}.
	 * @param ctx the parse tree
	 */
	void exitTypeMap(@NotNull BigDataScriptParser.TypeMapContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#break}.
	 * @param ctx the parse tree
	 */
	void enterBreak(@NotNull BigDataScriptParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#break}.
	 * @param ctx the parse tree
	 */
	void exitBreak(@NotNull BigDataScriptParser.BreakContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#kill}.
	 * @param ctx the parse tree
	 */
	void enterKill(@NotNull BigDataScriptParser.KillContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#kill}.
	 * @param ctx the parse tree
	 */
	void exitKill(@NotNull BigDataScriptParser.KillContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 */
	void enterVariableInit(@NotNull BigDataScriptParser.VariableInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#variableInit}.
	 * @param ctx the parse tree
	 */
	void exitVariableInit(@NotNull BigDataScriptParser.VariableInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#statmentEol}.
	 * @param ctx the parse tree
	 */
	void enterStatmentEol(@NotNull BigDataScriptParser.StatmentEolContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#statmentEol}.
	 * @param ctx the parse tree
	 */
	void exitStatmentEol(@NotNull BigDataScriptParser.StatmentEolContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitImplicit(@NotNull BigDataScriptParser.VariableInitImplicitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#variableInitImplicit}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitImplicit(@NotNull BigDataScriptParser.VariableInitImplicitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#varReference}.
	 * @param ctx the parse tree
	 */
	void enterVarReference(@NotNull BigDataScriptParser.VarReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#varReference}.
	 * @param ctx the parse tree
	 */
	void exitVarReference(@NotNull BigDataScriptParser.VarReferenceContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionTaskLiteral}.
	 * @param ctx the parse tree
	 */
	void enterExpressionTaskLiteral(@NotNull BigDataScriptParser.ExpressionTaskLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionTaskLiteral}.
	 * @param ctx the parse tree
	 */
	void exitExpressionTaskLiteral(@NotNull BigDataScriptParser.ExpressionTaskLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionDivide}.
	 * @param ctx the parse tree
	 */
	void enterExpressionDivide(@NotNull BigDataScriptParser.ExpressionDivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionDivide}.
	 * @param ctx the parse tree
	 */
	void exitExpressionDivide(@NotNull BigDataScriptParser.ExpressionDivideContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignment}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignment(@NotNull BigDataScriptParser.ExpressionAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignment}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignment(@NotNull BigDataScriptParser.ExpressionAssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#typeArray}.
	 * @param ctx the parse tree
	 */
	void enterTypeArray(@NotNull BigDataScriptParser.TypeArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#typeArray}.
	 * @param ctx the parse tree
	 */
	void exitTypeArray(@NotNull BigDataScriptParser.TypeArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#statmentExpr}.
	 * @param ctx the parse tree
	 */
	void enterStatmentExpr(@NotNull BigDataScriptParser.StatmentExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#statmentExpr}.
	 * @param ctx the parse tree
	 */
	void exitStatmentExpr(@NotNull BigDataScriptParser.StatmentExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void enterLiteralReal(@NotNull BigDataScriptParser.LiteralRealContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void exitLiteralReal(@NotNull BigDataScriptParser.LiteralRealContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionAssignmentPlus}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentPlus(@NotNull BigDataScriptParser.ExpressionAssignmentPlusContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionAssignmentPlus}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentPlus(@NotNull BigDataScriptParser.ExpressionAssignmentPlusContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(@NotNull BigDataScriptParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(@NotNull BigDataScriptParser.ForInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#expressionGe}.
	 * @param ctx the parse tree
	 */
	void enterExpressionGe(@NotNull BigDataScriptParser.ExpressionGeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#expressionGe}.
	 * @param ctx the parse tree
	 */
	void exitExpressionGe(@NotNull BigDataScriptParser.ExpressionGeContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#literalList}.
	 * @param ctx the parse tree
	 */
	void enterLiteralList(@NotNull BigDataScriptParser.LiteralListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#literalList}.
	 * @param ctx the parse tree
	 */
	void exitLiteralList(@NotNull BigDataScriptParser.LiteralListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(@NotNull BigDataScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(@NotNull BigDataScriptParser.FunctionDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link BigDataScriptParser#return}.
	 * @param ctx the parse tree
	 */
	void enterReturn(@NotNull BigDataScriptParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by {@link BigDataScriptParser#return}.
	 * @param ctx the parse tree
	 */
	void exitReturn(@NotNull BigDataScriptParser.ReturnContext ctx);
}