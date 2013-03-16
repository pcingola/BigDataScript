// Generated from BigDataScript.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface BigDataScriptListener extends ParseTreeListener {
	void enterStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);
	void exitStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);

	void enterMethodCall(BigDataScriptParser.MethodCallContext ctx);
	void exitMethodCall(BigDataScriptParser.MethodCallContext ctx);

	void enterExpressionList(BigDataScriptParser.ExpressionListContext ctx);
	void exitExpressionList(BigDataScriptParser.ExpressionListContext ctx);

	void enterExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);
	void exitExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);

	void enterLiteralReal(BigDataScriptParser.LiteralRealContext ctx);
	void exitLiteralReal(BigDataScriptParser.LiteralRealContext ctx);

	void enterKill(BigDataScriptParser.KillContext ctx);
	void exitKill(BigDataScriptParser.KillContext ctx);

	void enterWait(BigDataScriptParser.WaitContext ctx);
	void exitWait(BigDataScriptParser.WaitContext ctx);

	void enterLiteralList(BigDataScriptParser.LiteralListContext ctx);
	void exitLiteralList(BigDataScriptParser.LiteralListContext ctx);

	void enterLiteralMap(BigDataScriptParser.LiteralMapContext ctx);
	void exitLiteralMap(BigDataScriptParser.LiteralMapContext ctx);

	void enterExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);
	void exitExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);

	void enterExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);
	void exitExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);

	void enterExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);
	void exitExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);

	void enterBreak(BigDataScriptParser.BreakContext ctx);
	void exitBreak(BigDataScriptParser.BreakContext ctx);

	void enterExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);
	void exitExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);

	void enterFunctionCall(BigDataScriptParser.FunctionCallContext ctx);
	void exitFunctionCall(BigDataScriptParser.FunctionCallContext ctx);

	void enterProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);
	void exitProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);

	void enterLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);
	void exitLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);

	void enterExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);
	void exitExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);

	void enterExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);
	void exitExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);

	void enterEol(BigDataScriptParser.EolContext ctx);
	void exitEol(BigDataScriptParser.EolContext ctx);

	void enterForLoopList(BigDataScriptParser.ForLoopListContext ctx);
	void exitForLoopList(BigDataScriptParser.ForLoopListContext ctx);

	void enterStatmentExpr(BigDataScriptParser.StatmentExprContext ctx);
	void exitStatmentExpr(BigDataScriptParser.StatmentExprContext ctx);

	void enterTypePrimitiveString(BigDataScriptParser.TypePrimitiveStringContext ctx);
	void exitTypePrimitiveString(BigDataScriptParser.TypePrimitiveStringContext ctx);

	void enterForLoop(BigDataScriptParser.ForLoopContext ctx);
	void exitForLoop(BigDataScriptParser.ForLoopContext ctx);

	void enterVarReferenceMap(BigDataScriptParser.VarReferenceMapContext ctx);
	void exitVarReferenceMap(BigDataScriptParser.VarReferenceMapContext ctx);

	void enterExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);
	void exitExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);

	void enterTypeMap(BigDataScriptParser.TypeMapContext ctx);
	void exitTypeMap(BigDataScriptParser.TypeMapContext ctx);

	void enterLiteralInt(BigDataScriptParser.LiteralIntContext ctx);
	void exitLiteralInt(BigDataScriptParser.LiteralIntContext ctx);

	void enterExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);
	void exitExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);

	void enterExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);
	void exitExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);

	void enterTypePrimitiveInt(BigDataScriptParser.TypePrimitiveIntContext ctx);
	void exitTypePrimitiveInt(BigDataScriptParser.TypePrimitiveIntContext ctx);

	void enterExit(BigDataScriptParser.ExitContext ctx);
	void exitExit(BigDataScriptParser.ExitContext ctx);

	void enterTypePrimitiveReal(BigDataScriptParser.TypePrimitiveRealContext ctx);
	void exitTypePrimitiveReal(BigDataScriptParser.TypePrimitiveRealContext ctx);

	void enterExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);
	void exitExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);

	void enterExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);
	void exitExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);

	void enterTypeList(BigDataScriptParser.TypeListContext ctx);
	void exitTypeList(BigDataScriptParser.TypeListContext ctx);

	void enterExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);
	void exitExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);

	void enterVarReferenceList(BigDataScriptParser.VarReferenceListContext ctx);
	void exitVarReferenceList(BigDataScriptParser.VarReferenceListContext ctx);

	void enterExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);
	void exitExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);

	void enterExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);
	void exitExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);

	void enterBlock(BigDataScriptParser.BlockContext ctx);
	void exitBlock(BigDataScriptParser.BlockContext ctx);

	void enterExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);
	void exitExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);

	void enterWhile(BigDataScriptParser.WhileContext ctx);
	void exitWhile(BigDataScriptParser.WhileContext ctx);

	void enterTypePrimitiveVoid(BigDataScriptParser.TypePrimitiveVoidContext ctx);
	void exitTypePrimitiveVoid(BigDataScriptParser.TypePrimitiveVoidContext ctx);

	void enterExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);
	void exitExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);

	void enterIf(BigDataScriptParser.IfContext ctx);
	void exitIf(BigDataScriptParser.IfContext ctx);

	void enterExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);
	void exitExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);

	void enterStatmentEol(BigDataScriptParser.StatmentEolContext ctx);
	void exitStatmentEol(BigDataScriptParser.StatmentEolContext ctx);

	void enterVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);
	void exitVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);

	void enterVariableInit(BigDataScriptParser.VariableInitContext ctx);
	void exitVariableInit(BigDataScriptParser.VariableInitContext ctx);

	void enterExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);
	void exitExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);

	void enterPost(BigDataScriptParser.PostContext ctx);
	void exitPost(BigDataScriptParser.PostContext ctx);

	void enterExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);
	void exitExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);

	void enterExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);
	void exitExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);

	void enterForEnd(BigDataScriptParser.ForEndContext ctx);
	void exitForEnd(BigDataScriptParser.ForEndContext ctx);

	void enterVarReference(BigDataScriptParser.VarReferenceContext ctx);
	void exitVarReference(BigDataScriptParser.VarReferenceContext ctx);

	void enterExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);
	void exitExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);

	void enterReturn(BigDataScriptParser.ReturnContext ctx);
	void exitReturn(BigDataScriptParser.ReturnContext ctx);

	void enterCheckpoint(BigDataScriptParser.CheckpointContext ctx);
	void exitCheckpoint(BigDataScriptParser.CheckpointContext ctx);

	void enterExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);
	void exitExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);

	void enterForCondition(BigDataScriptParser.ForConditionContext ctx);
	void exitForCondition(BigDataScriptParser.ForConditionContext ctx);

	void enterExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);
	void exitExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);

	void enterTypePrimitiveBool(BigDataScriptParser.TypePrimitiveBoolContext ctx);
	void exitTypePrimitiveBool(BigDataScriptParser.TypePrimitiveBoolContext ctx);

	void enterExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);
	void exitExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);

	void enterLiteralString(BigDataScriptParser.LiteralStringContext ctx);
	void exitLiteralString(BigDataScriptParser.LiteralStringContext ctx);

	void enterPre(BigDataScriptParser.PreContext ctx);
	void exitPre(BigDataScriptParser.PreContext ctx);

	void enterContinue(BigDataScriptParser.ContinueContext ctx);
	void exitContinue(BigDataScriptParser.ContinueContext ctx);

	void enterExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);
	void exitExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);

	void enterTypeArray(BigDataScriptParser.TypeArrayContext ctx);
	void exitTypeArray(BigDataScriptParser.TypeArrayContext ctx);

	void enterFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);
	void exitFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);

	void enterForInit(BigDataScriptParser.ForInitContext ctx);
	void exitForInit(BigDataScriptParser.ForInitContext ctx);

	void enterExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
	void exitExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
}