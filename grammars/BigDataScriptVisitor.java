// Generated from BigDataScript.g4 by ANTLR 4.0
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface BigDataScriptVisitor<T> extends ParseTreeVisitor<T> {
	T visitStatementVarDeclaration(BigDataScriptParser.StatementVarDeclarationContext ctx);

	T visitMethodCall(BigDataScriptParser.MethodCallContext ctx);

	T visitExpressionList(BigDataScriptParser.ExpressionListContext ctx);

	T visitExpressionMinus(BigDataScriptParser.ExpressionMinusContext ctx);

	T visitLiteralReal(BigDataScriptParser.LiteralRealContext ctx);

	T visitKill(BigDataScriptParser.KillContext ctx);

	T visitWait(BigDataScriptParser.WaitContext ctx);

	T visitLiteralList(BigDataScriptParser.LiteralListContext ctx);

	T visitLiteralMap(BigDataScriptParser.LiteralMapContext ctx);

	T visitExpressionLe(BigDataScriptParser.ExpressionLeContext ctx);

	T visitExpressionLogicAnd(BigDataScriptParser.ExpressionLogicAndContext ctx);

	T visitExpressionDivide(BigDataScriptParser.ExpressionDivideContext ctx);

	T visitBreak(BigDataScriptParser.BreakContext ctx);

	T visitExpressionAssignment(BigDataScriptParser.ExpressionAssignmentContext ctx);

	T visitFunctionCall(BigDataScriptParser.FunctionCallContext ctx);

	T visitProgramUnit(BigDataScriptParser.ProgramUnitContext ctx);

	T visitLiteralBool(BigDataScriptParser.LiteralBoolContext ctx);

	T visitExpressionGe(BigDataScriptParser.ExpressionGeContext ctx);

	T visitExpressionBitNegation(BigDataScriptParser.ExpressionBitNegationContext ctx);

	T visitEol(BigDataScriptParser.EolContext ctx);

	T visitForLoopList(BigDataScriptParser.ForLoopListContext ctx);

	T visitStatmentExpr(BigDataScriptParser.StatmentExprContext ctx);

	T visitTypePrimitiveString(BigDataScriptParser.TypePrimitiveStringContext ctx);

	T visitForLoop(BigDataScriptParser.ForLoopContext ctx);

	T visitVarReferenceMap(BigDataScriptParser.VarReferenceMapContext ctx);

	T visitExpressionLogicOr(BigDataScriptParser.ExpressionLogicOrContext ctx);

	T visitTypeMap(BigDataScriptParser.TypeMapContext ctx);

	T visitLiteralInt(BigDataScriptParser.LiteralIntContext ctx);

	T visitExpressionGt(BigDataScriptParser.ExpressionGtContext ctx);

	T visitExpressionTask(BigDataScriptParser.ExpressionTaskContext ctx);

	T visitTypePrimitiveInt(BigDataScriptParser.TypePrimitiveIntContext ctx);

	T visitExit(BigDataScriptParser.ExitContext ctx);

	T visitTypePrimitiveReal(BigDataScriptParser.TypePrimitiveRealContext ctx);

	T visitExpressionUnaryPlus(BigDataScriptParser.ExpressionUnaryPlusContext ctx);

	T visitExpressionBitXor(BigDataScriptParser.ExpressionBitXorContext ctx);

	T visitTypeList(BigDataScriptParser.TypeListContext ctx);

	T visitExpressionDep(BigDataScriptParser.ExpressionDepContext ctx);

	T visitVarReferenceList(BigDataScriptParser.VarReferenceListContext ctx);

	T visitExpressionSys(BigDataScriptParser.ExpressionSysContext ctx);

	T visitExpressionBitAnd(BigDataScriptParser.ExpressionBitAndContext ctx);

	T visitBlock(BigDataScriptParser.BlockContext ctx);

	T visitExpressionUnaryMinus(BigDataScriptParser.ExpressionUnaryMinusContext ctx);

	T visitWhile(BigDataScriptParser.WhileContext ctx);

	T visitTypePrimitiveVoid(BigDataScriptParser.TypePrimitiveVoidContext ctx);

	T visitExpressionPlus(BigDataScriptParser.ExpressionPlusContext ctx);

	T visitIf(BigDataScriptParser.IfContext ctx);

	T visitExpressionNe(BigDataScriptParser.ExpressionNeContext ctx);

	T visitStatmentEol(BigDataScriptParser.StatmentEolContext ctx);

	T visitVarDeclaration(BigDataScriptParser.VarDeclarationContext ctx);

	T visitVariableInit(BigDataScriptParser.VariableInitContext ctx);

	T visitExpressionLogicNot(BigDataScriptParser.ExpressionLogicNotContext ctx);

	T visitPost(BigDataScriptParser.PostContext ctx);

	T visitExpressionCond(BigDataScriptParser.ExpressionCondContext ctx);

	T visitExpressionBitOr(BigDataScriptParser.ExpressionBitOrContext ctx);

	T visitForEnd(BigDataScriptParser.ForEndContext ctx);

	T visitVarReference(BigDataScriptParser.VarReferenceContext ctx);

	T visitExpressionTaskLiteral(BigDataScriptParser.ExpressionTaskLiteralContext ctx);

	T visitReturn(BigDataScriptParser.ReturnContext ctx);

	T visitCheckpoint(BigDataScriptParser.CheckpointContext ctx);

	T visitExpressionEq(BigDataScriptParser.ExpressionEqContext ctx);

	T visitForCondition(BigDataScriptParser.ForConditionContext ctx);

	T visitExpressionModulo(BigDataScriptParser.ExpressionModuloContext ctx);

	T visitTypePrimitiveBool(BigDataScriptParser.TypePrimitiveBoolContext ctx);

	T visitExpressionParen(BigDataScriptParser.ExpressionParenContext ctx);

	T visitLiteralString(BigDataScriptParser.LiteralStringContext ctx);

	T visitPre(BigDataScriptParser.PreContext ctx);

	T visitContinue(BigDataScriptParser.ContinueContext ctx);

	T visitExpressionLt(BigDataScriptParser.ExpressionLtContext ctx);

	T visitTypeArray(BigDataScriptParser.TypeArrayContext ctx);

	T visitFunctionDeclaration(BigDataScriptParser.FunctionDeclarationContext ctx);

	T visitForInit(BigDataScriptParser.ForInitContext ctx);

	T visitExpressionTimes(BigDataScriptParser.ExpressionTimesContext ctx);
}