// Generated from BigDataScript.g4 by ANTLR 4.2.2
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BigDataScriptParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__67=1, T__66=2, T__65=3, T__64=4, T__63=5, T__62=6, T__61=7, T__60=8, 
		T__59=9, T__58=10, T__57=11, T__56=12, T__55=13, T__54=14, T__53=15, T__52=16, 
		T__51=17, T__50=18, T__49=19, T__48=20, T__47=21, T__46=22, T__45=23, 
		T__44=24, T__43=25, T__42=26, T__41=27, T__40=28, T__39=29, T__38=30, 
		T__37=31, T__36=32, T__35=33, T__34=34, T__33=35, T__32=36, T__31=37, 
		T__30=38, T__29=39, T__28=40, T__27=41, T__26=42, T__25=43, T__24=44, 
		T__23=45, T__22=46, T__21=47, T__20=48, T__19=49, T__18=50, T__17=51, 
		T__16=52, T__15=53, T__14=54, T__13=55, T__12=56, T__11=57, T__10=58, 
		T__9=59, T__8=60, T__7=61, T__6=62, T__5=63, T__4=64, T__3=65, T__2=66, 
		T__1=67, T__0=68, BOOL_LITERAL=69, INT_LITERAL=70, REAL_LITERAL=71, STRING_LITERAL=72, 
		STRING_LITERAL_SINGLE=73, SYS_LITERAL=74, TASK_LITERAL=75, COMMENT=76, 
		COMMENT_LINE=77, COMMENT_LINE_HASH=78, ID=79, WS=80;
	public static final String[] tokenNames = {
		"<INVALID>", "'+='", "'!='", "'while'", "'{'", "'void'", "'&&'", "'='", 
		"'^'", "'for'", "'error'", "'|='", "'int'", "'include'", "'task'", "'('", 
		"'-='", "','", "'/='", "'kill'", "'<-'", "'\n'", "'println'", "'exit'", 
		"'>='", "'<'", "'++'", "']'", "'~'", "'wait'", "'dep'", "'+'", "'goal'", 
		"'*='", "'/'", "'continue'", "'&='", "'return'", "'||'", "';'", "'}'", 
		"'if'", "'?'", "'warning'", "':='", "'<='", "'break'", "'&'", "'print'", 
		"'*'", "'.'", "'parallel'", "'par'", "':'", "'['", "'=='", "'--'", "'|'", 
		"'>'", "'bool'", "'=>'", "'!'", "'string'", "'checkpoint'", "'%'", "'else'", 
		"')'", "'-'", "'real'", "BOOL_LITERAL", "INT_LITERAL", "REAL_LITERAL", 
		"STRING_LITERAL", "STRING_LITERAL_SINGLE", "SYS_LITERAL", "TASK_LITERAL", 
		"COMMENT", "COMMENT_LINE", "COMMENT_LINE_HASH", "ID", "WS"
	};
	public static final int
		RULE_programUnit = 0, RULE_eol = 1, RULE_typeList = 2, RULE_type = 3, 
		RULE_varDeclaration = 4, RULE_variableInit = 5, RULE_variableInitImplicit = 6, 
		RULE_includeFile = 7, RULE_statement = 8, RULE_forInit = 9, RULE_forCondition = 10, 
		RULE_forEnd = 11, RULE_expression = 12, RULE_expressionList = 13;
	public static final String[] ruleNames = {
		"programUnit", "eol", "typeList", "type", "varDeclaration", "variableInit", 
		"variableInitImplicit", "includeFile", "statement", "forInit", "forCondition", 
		"forEnd", "expression", "expressionList"
	};

	@Override
	public String getGrammarFileName() { return "BigDataScript.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public BigDataScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramUnitContext extends ParserRuleContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ProgramUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterProgramUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitProgramUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitProgramUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramUnitContext programUnit() throws RecognitionException {
		ProgramUnitContext _localctx = new ProgramUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programUnit);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(28); eol();
					}
					} 
				}
				setState(33);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(35); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(34); statement();
				}
				}
				setState(37); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 3) | (1L << 4) | (1L << 5) | (1L << 9) | (1L << 10) | (1L << 12) | (1L << 13) | (1L << 14) | (1L << 15) | (1L << 19) | (1L << 21) | (1L << 22) | (1L << 23) | (1L << 26) | (1L << 28) | (1L << 29) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 35) | (1L << 37) | (1L << 39) | (1L << 41) | (1L << 43) | (1L << 46) | (1L << 48) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 59) | (1L << 61) | (1L << 62) | (1L << 63))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (68 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EolContext extends ParserRuleContext {
		public EolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterEol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitEol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitEol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EolContext eol() throws RecognitionException {
		EolContext _localctx = new EolContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_eol);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(40); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(39);
					_la = _input.LA(1);
					if ( !(_la==21 || _la==39) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(42); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); type(0);
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==17) {
				{
				{
				setState(45); match(17);
				setState(46); type(0);
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TypePrimitiveStringContext extends TypeContext {
		public TypePrimitiveStringContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypePrimitiveString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypePrimitiveString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypePrimitiveString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeArrayContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeArrayContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypeArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypeArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypeArray(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePrimitiveVoidContext extends TypeContext {
		public TypePrimitiveVoidContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypePrimitiveVoid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypePrimitiveVoid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypePrimitiveVoid(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeMapContext extends TypeContext {
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeMapContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypeMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypeMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypeMap(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePrimitiveRealContext extends TypeContext {
		public TypePrimitiveRealContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypePrimitiveReal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypePrimitiveReal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypePrimitiveReal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePrimitiveBoolContext extends TypeContext {
		public TypePrimitiveBoolContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypePrimitiveBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypePrimitiveBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypePrimitiveBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePrimitiveIntContext extends TypeContext {
		public TypePrimitiveIntContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterTypePrimitiveInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitTypePrimitiveInt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitTypePrimitiveInt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			switch (_input.LA(1)) {
			case 59:
				{
				_localctx = new TypePrimitiveBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(53); match(59);
				}
				break;
			case 12:
				{
				_localctx = new TypePrimitiveIntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(54); match(12);
				}
				break;
			case 68:
				{
				_localctx = new TypePrimitiveRealContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(55); match(68);
				}
				break;
			case 62:
				{
				_localctx = new TypePrimitiveStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(62);
				}
				break;
			case 5:
				{
				_localctx = new TypePrimitiveVoidContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57); match(5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(73);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(71);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new TypeArrayContext(new TypeContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(60);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(61); match(54);
						setState(62); match(27);
						}
						break;

					case 2:
						{
						_localctx = new TypeMapContext(new TypeContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(63);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(64); match(4);
						setState(65); match(40);
						}
						break;

					case 3:
						{
						_localctx = new TypeMapContext(new TypeContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(66);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(67); match(4);
						setState(68); type(0);
						setState(69); match(40);
						}
						break;
					}
					} 
				}
				setState(75);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class VarDeclarationContext extends ParserRuleContext {
		public VariableInitContext variableInit(int i) {
			return getRuleContext(VariableInitContext.class,i);
		}
		public List<VariableInitContext> variableInit() {
			return getRuleContexts(VariableInitContext.class);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableInitImplicitContext variableInitImplicit() {
			return getRuleContext(VariableInitImplicitContext.class,0);
		}
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_varDeclaration);
		try {
			int _alt;
			setState(86);
			switch (_input.LA(1)) {
			case 5:
			case 12:
			case 59:
			case 62:
			case 68:
				enterOuterAlt(_localctx, 1);
				{
				setState(76); type(0);
				setState(77); variableInit();
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(78); match(17);
						setState(79); variableInit();
						}
						} 
					}
					setState(84);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(85); variableInitImplicit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVariableInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVariableInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVariableInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableInitContext variableInit() throws RecognitionException {
		VariableInitContext _localctx = new VariableInitContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_variableInit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); match(ID);
			setState(91);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(89); match(7);
				setState(90); expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitImplicitContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableInitImplicitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInitImplicit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVariableInitImplicit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVariableInitImplicit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVariableInitImplicit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableInitImplicitContext variableInitImplicit() throws RecognitionException {
		VariableInitImplicitContext _localctx = new VariableInitImplicitContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_variableInitImplicit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93); match(ID);
			setState(94); match(44);
			setState(95); expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeFileContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL_SINGLE() { return getToken(BigDataScriptParser.STRING_LITERAL_SINGLE, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(BigDataScriptParser.STRING_LITERAL, 0); }
		public EolContext eol() {
			return getRuleContext(EolContext.class,0);
		}
		public IncludeFileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeFile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterIncludeFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitIncludeFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitIncludeFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncludeFileContext includeFile() throws RecognitionException {
		IncludeFileContext _localctx = new IncludeFileContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_includeFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97); match(13);
			setState(98);
			_la = _input.LA(1);
			if ( !(_la==STRING_LITERAL || _la==STRING_LITERAL_SINGLE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(99); eol();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CheckpointContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public CheckpointContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterCheckpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitCheckpoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitCheckpoint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementVarDeclarationContext extends StatementContext {
		public VarDeclarationContext varDeclaration() {
			return getRuleContext(VarDeclarationContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public StatementVarDeclarationContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterStatementVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitStatementVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitStatementVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WaitContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public WaitContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterWait(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitWait(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitWait(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementIncludeContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public IncludeFileContext includeFile() {
			return getRuleContext(IncludeFileContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public StatementIncludeContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterStatementInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitStatementInclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitStatementInclude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public BreakContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterBreak(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitBreak(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitBreak(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ErrorContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ErrorContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitError(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitError(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class KillContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public KillContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterKill(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitKill(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitKill(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public WhileContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitWhile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatmentEolContext extends StatementContext {
		public EolContext eol() {
			return getRuleContext(EolContext.class,0);
		}
		public StatmentEolContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterStatmentEol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitStatmentEol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitStatmentEol(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExitContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ExitContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExit(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrintContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public PrintContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterPrint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitPrint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitPrint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrintlnContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public PrintlnContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterPrintln(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitPrintln(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitPrintln(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ContinueContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitContinue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitContinue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WarningContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public WarningContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterWarning(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitWarning(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitWarning(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlockContext extends StatementContext {
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public BlockContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementExprContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public StatementExprContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterStatementExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitStatementExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitStatementExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForLoopContext extends StatementContext {
		public ForEndContext end;
		public ForConditionContext forCondition() {
			return getRuleContext(ForConditionContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ForEndContext forEnd() {
			return getRuleContext(ForEndContext.class,0);
		}
		public ForLoopContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterForLoop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitForLoop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitForLoop(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForLoopListContext extends StatementContext {
		public VarDeclarationContext varDeclaration() {
			return getRuleContext(VarDeclarationContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ForLoopListContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterForLoopList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitForLoopList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitForLoopList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public IfContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitIf(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionDeclarationContext extends StatementContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public FunctionDeclarationContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public ReturnContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterReturn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitReturn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitReturn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(329);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				_localctx = new BlockContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(101); match(4);
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 3) | (1L << 4) | (1L << 5) | (1L << 9) | (1L << 10) | (1L << 12) | (1L << 13) | (1L << 14) | (1L << 15) | (1L << 19) | (1L << 21) | (1L << 22) | (1L << 23) | (1L << 26) | (1L << 28) | (1L << 29) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 35) | (1L << 37) | (1L << 39) | (1L << 41) | (1L << 43) | (1L << 46) | (1L << 48) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 59) | (1L << 61) | (1L << 62) | (1L << 63))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (68 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					{
					setState(102); statement();
					}
					}
					setState(107);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(108); match(40);
				}
				break;

			case 2:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(109); match(46);
				setState(113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(110); eol();
						}
						} 
					}
					setState(115);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				}
				}
				break;

			case 3:
				_localctx = new CheckpointContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(116); match(63);
				setState(118);
				switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
				case 1:
					{
					setState(117); expression(0);
					}
					break;
				}
				setState(123);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(120); eol();
						}
						} 
					}
					setState(125);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;

			case 4:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(126); match(35);
				setState(130);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(127); eol();
						}
						} 
					}
					setState(132);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				}
				break;

			case 5:
				_localctx = new ExitContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(133); match(23);
				setState(135);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(134); expression(0);
					}
					break;
				}
				setState(140);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(137); eol();
						}
						} 
					}
					setState(142);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				}
				}
				break;

			case 6:
				_localctx = new PrintContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(143); match(48);
				setState(145);
				switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
				case 1:
					{
					setState(144); expression(0);
					}
					break;
				}
				setState(150);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(147); eol();
						}
						} 
					}
					setState(152);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				}
				break;

			case 7:
				_localctx = new PrintlnContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(153); match(22);
				setState(155);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(154); expression(0);
					}
					break;
				}
				setState(160);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(157); eol();
						}
						} 
					}
					setState(162);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				}
				break;

			case 8:
				_localctx = new WarningContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(163); match(43);
				setState(165);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(164); expression(0);
					}
					break;
				}
				setState(170);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(167); eol();
						}
						} 
					}
					setState(172);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				}
				}
				break;

			case 9:
				_localctx = new ErrorContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(173); match(10);
				setState(175);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(174); expression(0);
					}
					break;
				}
				setState(180);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(177); eol();
						}
						} 
					}
					setState(182);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				}
				break;

			case 10:
				_localctx = new ForLoopContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(183); match(9);
				setState(184); match(15);
				setState(186);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 5) | (1L << 12) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 59) | (1L << 61) | (1L << 62))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (68 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					setState(185); forInit();
					}
				}

				setState(188); match(39);
				setState(190);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 61))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					setState(189); forCondition();
					}
				}

				setState(192); match(39);
				setState(194);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 61))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					setState(193); ((ForLoopContext)_localctx).end = forEnd();
					}
				}

				setState(196); match(66);
				setState(197); statement();
				setState(201);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(198); eol();
						}
						} 
					}
					setState(203);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				}
				break;

			case 11:
				_localctx = new ForLoopListContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(204); match(9);
				setState(205); match(15);
				setState(206); varDeclaration();
				setState(207); match(53);
				setState(208); expression(0);
				setState(209); match(66);
				setState(210); statement();
				setState(214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(211); eol();
						}
						} 
					}
					setState(216);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				}
				}
				break;

			case 12:
				_localctx = new IfContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(217); match(41);
				setState(218); match(15);
				setState(219); expression(0);
				setState(220); match(66);
				setState(221); statement();
				setState(225);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(222); eol();
						}
						} 
					}
					setState(227);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				setState(236);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(228); match(65);
					setState(229); statement();
					setState(233);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
					while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(230); eol();
							}
							} 
						}
						setState(235);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
					}
					}
					break;
				}
				}
				break;

			case 13:
				_localctx = new KillContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(238); match(19);
				setState(239); expression(0);
				setState(243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(240); eol();
						}
						} 
					}
					setState(245);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				}
				}
				break;

			case 14:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(246); match(37);
				setState(248);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(247); expression(0);
					}
					break;
				}
				setState(253);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(250); eol();
						}
						} 
					}
					setState(255);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
				}
				}
				break;

			case 15:
				_localctx = new WaitContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(256); match(29);
				setState(265);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(257); expression(0);
					setState(262);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
					while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(258); match(17);
							setState(259); expression(0);
							}
							} 
						}
						setState(264);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
					}
					}
					break;
				}
				setState(270);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(267); eol();
						}
						} 
					}
					setState(272);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				}
				}
				break;

			case 16:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(273); match(3);
				setState(274); match(15);
				setState(276);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 61))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					setState(275); expression(0);
					}
				}

				setState(278); match(66);
				setState(279); statement();
				setState(283);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(280); eol();
						}
						} 
					}
					setState(285);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				}
				}
				break;

			case 17:
				_localctx = new FunctionDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(286); type(0);
				setState(287); match(ID);
				setState(288); match(15);
				setState(290);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 12) | (1L << 59) | (1L << 62))) != 0) || _la==68 || _la==ID) {
					{
					setState(289); varDeclaration();
					}
				}

				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==17) {
					{
					{
					setState(292); match(17);
					setState(293); varDeclaration();
					}
					}
					setState(298);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(299); match(66);
				setState(300); statement();
				setState(304);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(301); eol();
						}
						} 
					}
					setState(306);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
				}
				}
				break;

			case 18:
				_localctx = new StatementVarDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(307); varDeclaration();
				setState(311);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(308); eol();
						}
						} 
					}
					setState(313);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				}
				}
				break;

			case 19:
				_localctx = new StatementExprContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(314); expression(0);
				setState(318);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(315); eol();
						}
						} 
					}
					setState(320);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				}
				}
				break;

			case 20:
				_localctx = new StatementIncludeContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(321); includeFile();
				setState(325);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(322); eol();
						}
						} 
					}
					setState(327);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				}
				}
				break;

			case 21:
				_localctx = new StatmentEolContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(328); eol();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForInitContext extends ParserRuleContext {
		public VarDeclarationContext varDeclaration() {
			return getRuleContext(VarDeclarationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitForInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_forInit);
		try {
			setState(333);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(331); varDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(332); expressionList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForConditionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterForCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitForCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitForCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForConditionContext forCondition() throws RecognitionException {
		ForConditionContext _localctx = new ForConditionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_forCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335); expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForEndContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForEndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forEnd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterForEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitForEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitForEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForEndContext forEnd() throws RecognitionException {
		ForEndContext _localctx = new ForEndContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_forEnd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337); expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExpressionLogicAndContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionLogicAndContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionLogicAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionLogicAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionLogicAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentListContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentListContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarReferenceMapContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public VarReferenceMapContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVarReferenceMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVarReferenceMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVarReferenceMap(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionEqContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionEqContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionEq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionEq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionEq(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionMinusContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionDepOperatorContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionDepOperatorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionDepOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionDepOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionDepOperator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarReferenceListContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public VarReferenceListContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVarReferenceList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVarReferenceList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVarReferenceList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionNeContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionNeContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionNe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionNe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionNe(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionBitXorContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionBitXorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionBitXor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionBitXor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionBitXor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionBitNegationContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionBitNegationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionBitNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionBitNegation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionBitNegation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionBitAndContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionBitAndContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionBitAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionBitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionBitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PostContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PostContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterPost(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitPost(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitPost(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionLogicNotContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionLogicNotContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionLogicNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionLogicNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionLogicNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionVariableInitImplicitContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionVariableInitImplicitContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionVariableInitImplicit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionVariableInitImplicit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionVariableInitImplicit(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentMultContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentMultContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentMult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentMult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentMult(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionDepContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionDepContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionDep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionDep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionDep(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionLtContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionLtContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionLt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionLt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionLt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentDivContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentDivContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PreContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PreContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterPre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitPre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitPre(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionUnaryPlusContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionUnaryPlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionUnaryPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionUnaryPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionUnaryPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionLogicOrContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionLogicOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionLogicOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionLogicOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionLogicOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionParallelContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionParallelContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionParallel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionParallel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionParallel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralBoolContext extends ExpressionContext {
		public TerminalNode BOOL_LITERAL() { return getToken(BigDataScriptParser.BOOL_LITERAL, 0); }
		public LiteralBoolContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionGoalContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionGoalContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionGoal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionGoal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionGoal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionTimesContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionTimesContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionTimes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionTimes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionTimes(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionPlusContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionPlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public FunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionParenContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionParenContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionParen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionParen(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionParen(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionCondContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionCondContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionCond(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionUnaryMinusContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionUnaryMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionUnaryMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionUnaryMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionUnaryMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionBitOrContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionBitOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionBitOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionBitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionBitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralIntContext extends ExpressionContext {
		public TerminalNode INT_LITERAL() { return getToken(BigDataScriptParser.INT_LITERAL, 0); }
		public LiteralIntContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralInt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralMapEmptyContext extends ExpressionContext {
		public LiteralMapEmptyContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralMapEmpty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralMapEmpty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralMapEmpty(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodCallContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public MethodCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralStringContext extends ExpressionContext {
		public TerminalNode STRING_LITERAL_SINGLE() { return getToken(BigDataScriptParser.STRING_LITERAL_SINGLE, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(BigDataScriptParser.STRING_LITERAL, 0); }
		public LiteralStringContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionGtContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionGtContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionGt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionGt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionGt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionModuloContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionModuloContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionModulo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionModulo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionModulo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentBitAndContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentBitAndContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentBitAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentBitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentBitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionLeContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionLeContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionLe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionLe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionLe(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralMapContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public LiteralMapContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralMap(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentBitOrContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentBitOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentBitOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentBitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentBitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionTaskContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionTaskContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionTask(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionTask(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionTask(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionSysContext extends ExpressionContext {
		public TerminalNode SYS_LITERAL() { return getToken(BigDataScriptParser.SYS_LITERAL, 0); }
		public ExpressionSysContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionSys(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionSys(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionSys(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentMinusContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralListEmptyContext extends ExpressionContext {
		public LiteralListEmptyContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralListEmpty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralListEmpty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralListEmpty(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarReferenceContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public VarReferenceContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterVarReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitVarReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitVarReference(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionTaskLiteralContext extends ExpressionContext {
		public TerminalNode TASK_LITERAL() { return getToken(BigDataScriptParser.TASK_LITERAL, 0); }
		public ExpressionTaskLiteralContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionTaskLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionTaskLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionTaskLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionDivideContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionDivideContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionDivide(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionDivide(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionDivide(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralRealContext extends ExpressionContext {
		public TerminalNode REAL_LITERAL() { return getToken(BigDataScriptParser.REAL_LITERAL, 0); }
		public LiteralRealContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralReal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralReal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralReal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionAssignmentPlusContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionAssignmentPlusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionAssignmentPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionAssignmentPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionAssignmentPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionGeContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionGeContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionGe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionGe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionGe(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralListContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public LiteralListContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterLiteralList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitLiteralList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitLiteralList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				_localctx = new PreContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(340);
				_la = _input.LA(1);
				if ( !(_la==26 || _la==56) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(341); expression(39);
				}
				break;

			case 2:
				{
				_localctx = new ExpressionBitNegationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(342); match(28);
				setState(343); expression(26);
				}
				break;

			case 3:
				{
				_localctx = new ExpressionLogicNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(344); match(61);
				setState(345); expression(25);
				}
				break;

			case 4:
				{
				_localctx = new ExpressionUnaryMinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(346); match(67);
				setState(347); expression(24);
				}
				break;

			case 5:
				{
				_localctx = new ExpressionUnaryPlusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(348); match(31);
				setState(349); expression(23);
				}
				break;

			case 6:
				{
				_localctx = new ExpressionGoalContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(350); match(32);
				setState(351); expression(11);
				}
				break;

			case 7:
				{
				_localctx = new ExpressionVariableInitImplicitContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(352); match(ID);
				setState(353); match(44);
				setState(354); expression(3);
				}
				break;

			case 8:
				{
				_localctx = new LiteralBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(355); match(BOOL_LITERAL);
				}
				break;

			case 9:
				{
				_localctx = new LiteralIntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(356); match(INT_LITERAL);
				}
				break;

			case 10:
				{
				_localctx = new LiteralRealContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(357); match(REAL_LITERAL);
				}
				break;

			case 11:
				{
				_localctx = new LiteralStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(358); match(STRING_LITERAL);
				}
				break;

			case 12:
				{
				_localctx = new LiteralStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(359); match(STRING_LITERAL_SINGLE);
				}
				break;

			case 13:
				{
				_localctx = new FunctionCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(360); match(ID);
				setState(361); match(15);
				setState(370);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 61))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
					{
					setState(362); expression(0);
					setState(367);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==17) {
						{
						{
						setState(363); match(17);
						setState(364); expression(0);
						}
						}
						setState(369);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(372); match(66);
				}
				break;

			case 14:
				{
				_localctx = new VarReferenceContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(373); match(ID);
				}
				break;

			case 15:
				{
				_localctx = new ExpressionParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(374); match(15);
				setState(375); expression(0);
				setState(376); match(66);
				}
				break;

			case 16:
				{
				_localctx = new LiteralListEmptyContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(378); match(54);
				setState(379); match(27);
				}
				break;

			case 17:
				{
				_localctx = new LiteralListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(380); match(54);
				setState(381); expression(0);
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==17) {
					{
					{
					setState(382); match(17);
					setState(383); expression(0);
					}
					}
					setState(388);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(389); match(27);
				}
				break;

			case 18:
				{
				_localctx = new LiteralMapEmptyContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(391); match(4);
				setState(392); match(40);
				}
				break;

			case 19:
				{
				_localctx = new LiteralMapContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(393); match(4);
				setState(394); expression(0);
				setState(395); match(60);
				setState(396); expression(0);
				setState(404);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==17) {
					{
					{
					setState(397); match(17);
					setState(398); expression(0);
					setState(399); match(60);
					setState(400); expression(0);
					}
					}
					setState(406);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(407); match(40);
				}
				break;

			case 20:
				{
				_localctx = new ExpressionSysContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(409); match(SYS_LITERAL);
				}
				break;

			case 21:
				{
				_localctx = new ExpressionTaskLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(410); match(TASK_LITERAL);
				}
				break;

			case 22:
				{
				_localctx = new ExpressionTaskContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(411); match(14);
				setState(423);
				switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
				case 1:
					{
					setState(412); match(15);
					setState(413); expression(0);
					setState(418);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==17) {
						{
						{
						setState(414); match(17);
						setState(415); expression(0);
						}
						}
						setState(420);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(421); match(66);
					}
					break;
				}
				setState(425); statement();
				}
				break;

			case 23:
				{
				_localctx = new ExpressionDepContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(426); match(30);
				setState(427); match(15);
				setState(428); expression(0);
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==17) {
					{
					{
					setState(429); match(17);
					setState(430); expression(0);
					}
					}
					setState(435);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(436); match(66);
				setState(437); statement();
				}
				break;

			case 24:
				{
				_localctx = new ExpressionParallelContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(439);
				_la = _input.LA(1);
				if ( !(_la==51 || _la==52) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(451);
				switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
				case 1:
					{
					setState(440); match(15);
					setState(441); expression(0);
					setState(446);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==17) {
						{
						{
						setState(442); match(17);
						setState(443); expression(0);
						}
						}
						setState(448);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(449); match(66);
					}
					break;
				}
				setState(453); statement();
				}
				break;

			case 25:
				{
				_localctx = new ExpressionAssignmentListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(454); match(15);
				setState(455); expression(0);
				setState(458); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(456); match(17);
					setState(457); expression(0);
					}
					}
					setState(460); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==17 );
				setState(462); match(66);
				setState(463); match(7);
				setState(464); expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(575);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(573);
					switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionLogicAndContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(468);
						if (!(precpred(_ctx, 44))) throw new FailedPredicateException(this, "precpred(_ctx, 44)");
						setState(469); match(6);
						setState(470); expression(45);
						}
						break;

					case 2:
						{
						_localctx = new ExpressionLogicOrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(471);
						if (!(precpred(_ctx, 43))) throw new FailedPredicateException(this, "precpred(_ctx, 43)");
						setState(472); match(38);
						setState(473); expression(44);
						}
						break;

					case 3:
						{
						_localctx = new ExpressionBitAndContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(474);
						if (!(precpred(_ctx, 42))) throw new FailedPredicateException(this, "precpred(_ctx, 42)");
						setState(475); match(47);
						setState(476); expression(43);
						}
						break;

					case 4:
						{
						_localctx = new ExpressionBitXorContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(477);
						if (!(precpred(_ctx, 41))) throw new FailedPredicateException(this, "precpred(_ctx, 41)");
						setState(478); match(8);
						setState(479); expression(42);
						}
						break;

					case 5:
						{
						_localctx = new ExpressionBitOrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(480);
						if (!(precpred(_ctx, 40))) throw new FailedPredicateException(this, "precpred(_ctx, 40)");
						setState(481); match(57);
						setState(482); expression(41);
						}
						break;

					case 6:
						{
						_localctx = new ExpressionNeContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(483);
						if (!(precpred(_ctx, 37))) throw new FailedPredicateException(this, "precpred(_ctx, 37)");
						setState(484); match(2);
						setState(485); expression(38);
						}
						break;

					case 7:
						{
						_localctx = new ExpressionEqContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(486);
						if (!(precpred(_ctx, 36))) throw new FailedPredicateException(this, "precpred(_ctx, 36)");
						setState(487); match(55);
						setState(488); expression(37);
						}
						break;

					case 8:
						{
						_localctx = new ExpressionModuloContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(489);
						if (!(precpred(_ctx, 35))) throw new FailedPredicateException(this, "precpred(_ctx, 35)");
						setState(490); match(64);
						setState(491); expression(36);
						}
						break;

					case 9:
						{
						_localctx = new ExpressionDivideContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(492);
						if (!(precpred(_ctx, 34))) throw new FailedPredicateException(this, "precpred(_ctx, 34)");
						setState(493); match(34);
						setState(494); expression(35);
						}
						break;

					case 10:
						{
						_localctx = new ExpressionTimesContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(495);
						if (!(precpred(_ctx, 33))) throw new FailedPredicateException(this, "precpred(_ctx, 33)");
						setState(496); match(49);
						setState(497); expression(34);
						}
						break;

					case 11:
						{
						_localctx = new ExpressionMinusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(498);
						if (!(precpred(_ctx, 32))) throw new FailedPredicateException(this, "precpred(_ctx, 32)");
						setState(499); match(67);
						setState(500); expression(33);
						}
						break;

					case 12:
						{
						_localctx = new ExpressionPlusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(501);
						if (!(precpred(_ctx, 31))) throw new FailedPredicateException(this, "precpred(_ctx, 31)");
						setState(502); match(31);
						setState(503); expression(32);
						}
						break;

					case 13:
						{
						_localctx = new ExpressionLtContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(504);
						if (!(precpred(_ctx, 30))) throw new FailedPredicateException(this, "precpred(_ctx, 30)");
						setState(505); match(25);
						setState(506); expression(31);
						}
						break;

					case 14:
						{
						_localctx = new ExpressionGtContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(507);
						if (!(precpred(_ctx, 29))) throw new FailedPredicateException(this, "precpred(_ctx, 29)");
						setState(508); match(58);
						setState(509); expression(30);
						}
						break;

					case 15:
						{
						_localctx = new ExpressionLeContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(510);
						if (!(precpred(_ctx, 28))) throw new FailedPredicateException(this, "precpred(_ctx, 28)");
						setState(511); match(45);
						setState(512); expression(29);
						}
						break;

					case 16:
						{
						_localctx = new ExpressionGeContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(513);
						if (!(precpred(_ctx, 27))) throw new FailedPredicateException(this, "precpred(_ctx, 27)");
						setState(514); match(24);
						setState(515); expression(28);
						}
						break;

					case 17:
						{
						_localctx = new ExpressionCondContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(516);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(517); match(42);
						setState(518); expression(0);
						setState(519); match(53);
						setState(520); expression(22);
						}
						break;

					case 18:
						{
						_localctx = new ExpressionDepOperatorContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(522);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(523); match(20);
						setState(524); expression(21);
						}
						break;

					case 19:
						{
						_localctx = new ExpressionAssignmentBitOrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(525);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(526); match(11);
						setState(527); expression(10);
						}
						break;

					case 20:
						{
						_localctx = new ExpressionAssignmentBitAndContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(528);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(529); match(36);
						setState(530); expression(9);
						}
						break;

					case 21:
						{
						_localctx = new ExpressionAssignmentDivContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(531);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(532); match(18);
						setState(533); expression(8);
						}
						break;

					case 22:
						{
						_localctx = new ExpressionAssignmentMultContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(534);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(535); match(33);
						setState(536); expression(7);
						}
						break;

					case 23:
						{
						_localctx = new ExpressionAssignmentMinusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(537);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(538); match(16);
						setState(539); expression(6);
						}
						break;

					case 24:
						{
						_localctx = new ExpressionAssignmentPlusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(540);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(541); match(1);
						setState(542); expression(5);
						}
						break;

					case 25:
						{
						_localctx = new ExpressionAssignmentContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(543);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(544); match(7);
						setState(545); expression(2);
						}
						break;

					case 26:
						{
						_localctx = new MethodCallContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(546);
						if (!(precpred(_ctx, 48))) throw new FailedPredicateException(this, "precpred(_ctx, 48)");
						setState(547); match(50);
						setState(548); match(ID);
						setState(549); match(15);
						setState(558);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 15) | (1L << 26) | (1L << 28) | (1L << 30) | (1L << 31) | (1L << 32) | (1L << 51) | (1L << 52) | (1L << 54) | (1L << 56) | (1L << 61))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (67 - 67)) | (1L << (BOOL_LITERAL - 67)) | (1L << (INT_LITERAL - 67)) | (1L << (REAL_LITERAL - 67)) | (1L << (STRING_LITERAL - 67)) | (1L << (STRING_LITERAL_SINGLE - 67)) | (1L << (SYS_LITERAL - 67)) | (1L << (TASK_LITERAL - 67)) | (1L << (ID - 67)))) != 0)) {
							{
							setState(550); expression(0);
							setState(555);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==17) {
								{
								{
								setState(551); match(17);
								setState(552); expression(0);
								}
								}
								setState(557);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(560); match(66);
						}
						break;

					case 27:
						{
						_localctx = new VarReferenceListContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(561);
						if (!(precpred(_ctx, 46))) throw new FailedPredicateException(this, "precpred(_ctx, 46)");
						setState(562); match(54);
						setState(563); expression(0);
						setState(564); match(27);
						}
						break;

					case 28:
						{
						_localctx = new VarReferenceMapContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(566);
						if (!(precpred(_ctx, 45))) throw new FailedPredicateException(this, "precpred(_ctx, 45)");
						setState(567); match(4);
						setState(568); expression(0);
						setState(569); match(40);
						}
						break;

					case 29:
						{
						_localctx = new PostContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(571);
						if (!(precpred(_ctx, 38))) throw new FailedPredicateException(this, "precpred(_ctx, 38)");
						setState(572);
						_la = _input.LA(1);
						if ( !(_la==26 || _la==56) ) {
						_errHandler.recoverInline(this);
						}
						consume();
						}
						break;
					}
					} 
				}
				setState(577);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(578); expression(0);
			setState(583);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==17) {
				{
				{
				setState(579); match(17);
				setState(580); expression(0);
				}
				}
				setState(585);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 3: return type_sempred((TypeContext)_localctx, predIndex);

		case 12: return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3: return precpred(_ctx, 44);

		case 4: return precpred(_ctx, 43);

		case 5: return precpred(_ctx, 42);

		case 6: return precpred(_ctx, 41);

		case 7: return precpred(_ctx, 40);

		case 8: return precpred(_ctx, 37);

		case 9: return precpred(_ctx, 36);

		case 10: return precpred(_ctx, 35);

		case 11: return precpred(_ctx, 34);

		case 12: return precpred(_ctx, 33);

		case 13: return precpred(_ctx, 32);

		case 14: return precpred(_ctx, 31);

		case 15: return precpred(_ctx, 30);

		case 16: return precpred(_ctx, 29);

		case 17: return precpred(_ctx, 28);

		case 18: return precpred(_ctx, 27);

		case 19: return precpred(_ctx, 21);

		case 20: return precpred(_ctx, 20);

		case 21: return precpred(_ctx, 9);

		case 22: return precpred(_ctx, 8);

		case 23: return precpred(_ctx, 7);

		case 24: return precpred(_ctx, 6);

		case 25: return precpred(_ctx, 5);

		case 26: return precpred(_ctx, 4);

		case 27: return precpred(_ctx, 1);

		case 28: return precpred(_ctx, 48);

		case 29: return precpred(_ctx, 46);

		case 30: return precpred(_ctx, 45);

		case 31: return precpred(_ctx, 38);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 3);

		case 1: return precpred(_ctx, 2);

		case 2: return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3R\u024d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\7\2 \n\2\f\2\16\2#\13\2\3"+
		"\2\6\2&\n\2\r\2\16\2\'\3\3\6\3+\n\3\r\3\16\3,\3\4\3\4\3\4\7\4\62\n\4\f"+
		"\4\16\4\65\13\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5=\n\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\7\5J\n\5\f\5\16\5M\13\5\3\6\3\6\3\6\3\6\7\6S\n"+
		"\6\f\6\16\6V\13\6\3\6\5\6Y\n\6\3\7\3\7\3\7\5\7^\n\7\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\n\3\n\7\nj\n\n\f\n\16\nm\13\n\3\n\3\n\3\n\7\nr\n\n\f"+
		"\n\16\nu\13\n\3\n\3\n\5\ny\n\n\3\n\7\n|\n\n\f\n\16\n\177\13\n\3\n\3\n"+
		"\7\n\u0083\n\n\f\n\16\n\u0086\13\n\3\n\3\n\5\n\u008a\n\n\3\n\7\n\u008d"+
		"\n\n\f\n\16\n\u0090\13\n\3\n\3\n\5\n\u0094\n\n\3\n\7\n\u0097\n\n\f\n\16"+
		"\n\u009a\13\n\3\n\3\n\5\n\u009e\n\n\3\n\7\n\u00a1\n\n\f\n\16\n\u00a4\13"+
		"\n\3\n\3\n\5\n\u00a8\n\n\3\n\7\n\u00ab\n\n\f\n\16\n\u00ae\13\n\3\n\3\n"+
		"\5\n\u00b2\n\n\3\n\7\n\u00b5\n\n\f\n\16\n\u00b8\13\n\3\n\3\n\3\n\5\n\u00bd"+
		"\n\n\3\n\3\n\5\n\u00c1\n\n\3\n\3\n\5\n\u00c5\n\n\3\n\3\n\3\n\7\n\u00ca"+
		"\n\n\f\n\16\n\u00cd\13\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u00d7\n\n"+
		"\f\n\16\n\u00da\13\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u00e2\n\n\f\n\16\n\u00e5"+
		"\13\n\3\n\3\n\3\n\7\n\u00ea\n\n\f\n\16\n\u00ed\13\n\5\n\u00ef\n\n\3\n"+
		"\3\n\3\n\7\n\u00f4\n\n\f\n\16\n\u00f7\13\n\3\n\3\n\5\n\u00fb\n\n\3\n\7"+
		"\n\u00fe\n\n\f\n\16\n\u0101\13\n\3\n\3\n\3\n\3\n\7\n\u0107\n\n\f\n\16"+
		"\n\u010a\13\n\5\n\u010c\n\n\3\n\7\n\u010f\n\n\f\n\16\n\u0112\13\n\3\n"+
		"\3\n\3\n\5\n\u0117\n\n\3\n\3\n\3\n\7\n\u011c\n\n\f\n\16\n\u011f\13\n\3"+
		"\n\3\n\3\n\3\n\5\n\u0125\n\n\3\n\3\n\7\n\u0129\n\n\f\n\16\n\u012c\13\n"+
		"\3\n\3\n\3\n\7\n\u0131\n\n\f\n\16\n\u0134\13\n\3\n\3\n\7\n\u0138\n\n\f"+
		"\n\16\n\u013b\13\n\3\n\3\n\7\n\u013f\n\n\f\n\16\n\u0142\13\n\3\n\3\n\7"+
		"\n\u0146\n\n\f\n\16\n\u0149\13\n\3\n\5\n\u014c\n\n\3\13\3\13\5\13\u0150"+
		"\n\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\7\16\u0170\n\16\f\16\16\16\u0173\13\16\5\16\u0175\n\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u0183\n\16"+
		"\f\16\16\16\u0186\13\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\7\16\u0195\n\16\f\16\16\16\u0198\13\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u01a3\n\16\f\16\16\16\u01a6\13"+
		"\16\3\16\3\16\5\16\u01aa\n\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u01b2"+
		"\n\16\f\16\16\16\u01b5\13\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7"+
		"\16\u01bf\n\16\f\16\16\16\u01c2\13\16\3\16\3\16\5\16\u01c6\n\16\3\16\3"+
		"\16\3\16\3\16\3\16\6\16\u01cd\n\16\r\16\16\16\u01ce\3\16\3\16\3\16\3\16"+
		"\5\16\u01d5\n\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\7\16\u022c\n\16\f\16\16\16\u022f\13\16\5\16\u0231"+
		"\n\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\7\16\u0240\n\16\f\16\16\16\u0243\13\16\3\17\3\17\3\17\7\17\u0248\n\17"+
		"\f\17\16\17\u024b\13\17\3\17\2\4\b\32\20\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\2\6\4\2\27\27))\3\2JK\4\2\34\34::\3\2\65\66\u02c8\2!\3\2\2\2\4"+
		"*\3\2\2\2\6.\3\2\2\2\b<\3\2\2\2\nX\3\2\2\2\fZ\3\2\2\2\16_\3\2\2\2\20c"+
		"\3\2\2\2\22\u014b\3\2\2\2\24\u014f\3\2\2\2\26\u0151\3\2\2\2\30\u0153\3"+
		"\2\2\2\32\u01d4\3\2\2\2\34\u0244\3\2\2\2\36 \5\4\3\2\37\36\3\2\2\2 #\3"+
		"\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"%\3\2\2\2#!\3\2\2\2$&\5\22\n\2%$\3\2\2"+
		"\2&\'\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\3\3\2\2\2)+\t\2\2\2*)\3\2\2\2+,\3"+
		"\2\2\2,*\3\2\2\2,-\3\2\2\2-\5\3\2\2\2.\63\5\b\5\2/\60\7\23\2\2\60\62\5"+
		"\b\5\2\61/\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\7\3\2"+
		"\2\2\65\63\3\2\2\2\66\67\b\5\1\2\67=\7=\2\28=\7\16\2\29=\7F\2\2:=\7@\2"+
		"\2;=\7\7\2\2<\66\3\2\2\2<8\3\2\2\2<9\3\2\2\2<:\3\2\2\2<;\3\2\2\2=K\3\2"+
		"\2\2>?\f\5\2\2?@\78\2\2@J\7\35\2\2AB\f\4\2\2BC\7\6\2\2CJ\7*\2\2DE\f\3"+
		"\2\2EF\7\6\2\2FG\5\b\5\2GH\7*\2\2HJ\3\2\2\2I>\3\2\2\2IA\3\2\2\2ID\3\2"+
		"\2\2JM\3\2\2\2KI\3\2\2\2KL\3\2\2\2L\t\3\2\2\2MK\3\2\2\2NO\5\b\5\2OT\5"+
		"\f\7\2PQ\7\23\2\2QS\5\f\7\2RP\3\2\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2\2\2UY"+
		"\3\2\2\2VT\3\2\2\2WY\5\16\b\2XN\3\2\2\2XW\3\2\2\2Y\13\3\2\2\2Z]\7Q\2\2"+
		"[\\\7\t\2\2\\^\5\32\16\2][\3\2\2\2]^\3\2\2\2^\r\3\2\2\2_`\7Q\2\2`a\7."+
		"\2\2ab\5\32\16\2b\17\3\2\2\2cd\7\17\2\2de\t\3\2\2ef\5\4\3\2f\21\3\2\2"+
		"\2gk\7\6\2\2hj\5\22\n\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2ln\3\2"+
		"\2\2mk\3\2\2\2n\u014c\7*\2\2os\7\60\2\2pr\5\4\3\2qp\3\2\2\2ru\3\2\2\2"+
		"sq\3\2\2\2st\3\2\2\2t\u014c\3\2\2\2us\3\2\2\2vx\7A\2\2wy\5\32\16\2xw\3"+
		"\2\2\2xy\3\2\2\2y}\3\2\2\2z|\5\4\3\2{z\3\2\2\2|\177\3\2\2\2}{\3\2\2\2"+
		"}~\3\2\2\2~\u014c\3\2\2\2\177}\3\2\2\2\u0080\u0084\7%\2\2\u0081\u0083"+
		"\5\4\3\2\u0082\u0081\3\2\2\2\u0083\u0086\3\2\2\2\u0084\u0082\3\2\2\2\u0084"+
		"\u0085\3\2\2\2\u0085\u014c\3\2\2\2\u0086\u0084\3\2\2\2\u0087\u0089\7\31"+
		"\2\2\u0088\u008a\5\32\16\2\u0089\u0088\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u008e\3\2\2\2\u008b\u008d\5\4\3\2\u008c\u008b\3\2\2\2\u008d\u0090\3\2"+
		"\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u014c\3\2\2\2\u0090"+
		"\u008e\3\2\2\2\u0091\u0093\7\62\2\2\u0092\u0094\5\32\16\2\u0093\u0092"+
		"\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0098\3\2\2\2\u0095\u0097\5\4\3\2\u0096"+
		"\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2"+
		"\2\2\u0099\u014c\3\2\2\2\u009a\u0098\3\2\2\2\u009b\u009d\7\30\2\2\u009c"+
		"\u009e\5\32\16\2\u009d\u009c\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00a2\3"+
		"\2\2\2\u009f\u00a1\5\4\3\2\u00a0\u009f\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u014c\3\2\2\2\u00a4\u00a2\3\2"+
		"\2\2\u00a5\u00a7\7-\2\2\u00a6\u00a8\5\32\16\2\u00a7\u00a6\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\u00ac\3\2\2\2\u00a9\u00ab\5\4\3\2\u00aa\u00a9\3\2"+
		"\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\u014c\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00b1\7\f\2\2\u00b0\u00b2\5\32"+
		"\16\2\u00b1\u00b0\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b6\3\2\2\2\u00b3"+
		"\u00b5\5\4\3\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2"+
		"\2\2\u00b6\u00b7\3\2\2\2\u00b7\u014c\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9"+
		"\u00ba\7\13\2\2\u00ba\u00bc\7\21\2\2\u00bb\u00bd\5\24\13\2\u00bc\u00bb"+
		"\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c0\7)\2\2\u00bf"+
		"\u00c1\5\26\f\2\u00c0\u00bf\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\3"+
		"\2\2\2\u00c2\u00c4\7)\2\2\u00c3\u00c5\5\30\r\2\u00c4\u00c3\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c7\7D\2\2\u00c7\u00cb\5\22"+
		"\n\2\u00c8\u00ca\5\4\3\2\u00c9\u00c8\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb"+
		"\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u014c\3\2\2\2\u00cd\u00cb\3\2"+
		"\2\2\u00ce\u00cf\7\13\2\2\u00cf\u00d0\7\21\2\2\u00d0\u00d1\5\n\6\2\u00d1"+
		"\u00d2\7\67\2\2\u00d2\u00d3\5\32\16\2\u00d3\u00d4\7D\2\2\u00d4\u00d8\5"+
		"\22\n\2\u00d5\u00d7\5\4\3\2\u00d6\u00d5\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8"+
		"\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u014c\3\2\2\2\u00da\u00d8\3\2"+
		"\2\2\u00db\u00dc\7+\2\2\u00dc\u00dd\7\21\2\2\u00dd\u00de\5\32\16\2\u00de"+
		"\u00df\7D\2\2\u00df\u00e3\5\22\n\2\u00e0\u00e2\5\4\3\2\u00e1\u00e0\3\2"+
		"\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4"+
		"\u00ee\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e7\7C\2\2\u00e7\u00eb\5\22"+
		"\n\2\u00e8\u00ea\5\4\3\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb"+
		"\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2"+
		"\2\2\u00ee\u00e6\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u014c\3\2\2\2\u00f0"+
		"\u00f1\7\25\2\2\u00f1\u00f5\5\32\16\2\u00f2\u00f4\5\4\3\2\u00f3\u00f2"+
		"\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6"+
		"\u014c\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00fa\7\'\2\2\u00f9\u00fb\5\32"+
		"\16\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00ff\3\2\2\2\u00fc"+
		"\u00fe\5\4\3\2\u00fd\u00fc\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2"+
		"\2\2\u00ff\u0100\3\2\2\2\u0100\u014c\3\2\2\2\u0101\u00ff\3\2\2\2\u0102"+
		"\u010b\7\37\2\2\u0103\u0108\5\32\16\2\u0104\u0105\7\23\2\2\u0105\u0107"+
		"\5\32\16\2\u0106\u0104\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106\3\2\2\2"+
		"\u0108\u0109\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u0108\3\2\2\2\u010b\u0103"+
		"\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u0110\3\2\2\2\u010d\u010f\5\4\3\2\u010e"+
		"\u010d\3\2\2\2\u010f\u0112\3\2\2\2\u0110\u010e\3\2\2\2\u0110\u0111\3\2"+
		"\2\2\u0111\u014c\3\2\2\2\u0112\u0110\3\2\2\2\u0113\u0114\7\5\2\2\u0114"+
		"\u0116\7\21\2\2\u0115\u0117\5\32\16\2\u0116\u0115\3\2\2\2\u0116\u0117"+
		"\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119\7D\2\2\u0119\u011d\5\22\n\2\u011a"+
		"\u011c\5\4\3\2\u011b\u011a\3\2\2\2\u011c\u011f\3\2\2\2\u011d\u011b\3\2"+
		"\2\2\u011d\u011e\3\2\2\2\u011e\u014c\3\2\2\2\u011f\u011d\3\2\2\2\u0120"+
		"\u0121\5\b\5\2\u0121\u0122\7Q\2\2\u0122\u0124\7\21\2\2\u0123\u0125\5\n"+
		"\6\2\u0124\u0123\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u012a\3\2\2\2\u0126"+
		"\u0127\7\23\2\2\u0127\u0129\5\n\6\2\u0128\u0126\3\2\2\2\u0129\u012c\3"+
		"\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012d\3\2\2\2\u012c"+
		"\u012a\3\2\2\2\u012d\u012e\7D\2\2\u012e\u0132\5\22\n\2\u012f\u0131\5\4"+
		"\3\2\u0130\u012f\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2\2\u0132"+
		"\u0133\3\2\2\2\u0133\u014c\3\2\2\2\u0134\u0132\3\2\2\2\u0135\u0139\5\n"+
		"\6\2\u0136\u0138\5\4\3\2\u0137\u0136\3\2\2\2\u0138\u013b\3\2\2\2\u0139"+
		"\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u014c\3\2\2\2\u013b\u0139\3\2"+
		"\2\2\u013c\u0140\5\32\16\2\u013d\u013f\5\4\3\2\u013e\u013d\3\2\2\2\u013f"+
		"\u0142\3\2\2\2\u0140\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u014c\3\2"+
		"\2\2\u0142\u0140\3\2\2\2\u0143\u0147\5\20\t\2\u0144\u0146\5\4\3\2\u0145"+
		"\u0144\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2"+
		"\2\2\u0148\u014c\3\2\2\2\u0149\u0147\3\2\2\2\u014a\u014c\5\4\3\2\u014b"+
		"g\3\2\2\2\u014bo\3\2\2\2\u014bv\3\2\2\2\u014b\u0080\3\2\2\2\u014b\u0087"+
		"\3\2\2\2\u014b\u0091\3\2\2\2\u014b\u009b\3\2\2\2\u014b\u00a5\3\2\2\2\u014b"+
		"\u00af\3\2\2\2\u014b\u00b9\3\2\2\2\u014b\u00ce\3\2\2\2\u014b\u00db\3\2"+
		"\2\2\u014b\u00f0\3\2\2\2\u014b\u00f8\3\2\2\2\u014b\u0102\3\2\2\2\u014b"+
		"\u0113\3\2\2\2\u014b\u0120\3\2\2\2\u014b\u0135\3\2\2\2\u014b\u013c\3\2"+
		"\2\2\u014b\u0143\3\2\2\2\u014b\u014a\3\2\2\2\u014c\23\3\2\2\2\u014d\u0150"+
		"\5\n\6\2\u014e\u0150\5\34\17\2\u014f\u014d\3\2\2\2\u014f\u014e\3\2\2\2"+
		"\u0150\25\3\2\2\2\u0151\u0152\5\32\16\2\u0152\27\3\2\2\2\u0153\u0154\5"+
		"\34\17\2\u0154\31\3\2\2\2\u0155\u0156\b\16\1\2\u0156\u0157\t\4\2\2\u0157"+
		"\u01d5\5\32\16)\u0158\u0159\7\36\2\2\u0159\u01d5\5\32\16\34\u015a\u015b"+
		"\7?\2\2\u015b\u01d5\5\32\16\33\u015c\u015d\7E\2\2\u015d\u01d5\5\32\16"+
		"\32\u015e\u015f\7!\2\2\u015f\u01d5\5\32\16\31\u0160\u0161\7\"\2\2\u0161"+
		"\u01d5\5\32\16\r\u0162\u0163\7Q\2\2\u0163\u0164\7.\2\2\u0164\u01d5\5\32"+
		"\16\5\u0165\u01d5\7G\2\2\u0166\u01d5\7H\2\2\u0167\u01d5\7I\2\2\u0168\u01d5"+
		"\7J\2\2\u0169\u01d5\7K\2\2\u016a\u016b\7Q\2\2\u016b\u0174\7\21\2\2\u016c"+
		"\u0171\5\32\16\2\u016d\u016e\7\23\2\2\u016e\u0170\5\32\16\2\u016f\u016d"+
		"\3\2\2\2\u0170\u0173\3\2\2\2\u0171\u016f\3\2\2\2\u0171\u0172\3\2\2\2\u0172"+
		"\u0175\3\2\2\2\u0173\u0171\3\2\2\2\u0174\u016c\3\2\2\2\u0174\u0175\3\2"+
		"\2\2\u0175\u0176\3\2\2\2\u0176\u01d5\7D\2\2\u0177\u01d5\7Q\2\2\u0178\u0179"+
		"\7\21\2\2\u0179\u017a\5\32\16\2\u017a\u017b\7D\2\2\u017b\u01d5\3\2\2\2"+
		"\u017c\u017d\78\2\2\u017d\u01d5\7\35\2\2\u017e\u017f\78\2\2\u017f\u0184"+
		"\5\32\16\2\u0180\u0181\7\23\2\2\u0181\u0183\5\32\16\2\u0182\u0180\3\2"+
		"\2\2\u0183\u0186\3\2\2\2\u0184\u0182\3\2\2\2\u0184\u0185\3\2\2\2\u0185"+
		"\u0187\3\2\2\2\u0186\u0184\3\2\2\2\u0187\u0188\7\35\2\2\u0188\u01d5\3"+
		"\2\2\2\u0189\u018a\7\6\2\2\u018a\u01d5\7*\2\2\u018b\u018c\7\6\2\2\u018c"+
		"\u018d\5\32\16\2\u018d\u018e\7>\2\2\u018e\u0196\5\32\16\2\u018f\u0190"+
		"\7\23\2\2\u0190\u0191\5\32\16\2\u0191\u0192\7>\2\2\u0192\u0193\5\32\16"+
		"\2\u0193\u0195\3\2\2\2\u0194\u018f\3\2\2\2\u0195\u0198\3\2\2\2\u0196\u0194"+
		"\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0199\3\2\2\2\u0198\u0196\3\2\2\2\u0199"+
		"\u019a\7*\2\2\u019a\u01d5\3\2\2\2\u019b\u01d5\7L\2\2\u019c\u01d5\7M\2"+
		"\2\u019d\u01a9\7\20\2\2\u019e\u019f\7\21\2\2\u019f\u01a4\5\32\16\2\u01a0"+
		"\u01a1\7\23\2\2\u01a1\u01a3\5\32\16\2\u01a2\u01a0\3\2\2\2\u01a3\u01a6"+
		"\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a7\3\2\2\2\u01a6"+
		"\u01a4\3\2\2\2\u01a7\u01a8\7D\2\2\u01a8\u01aa\3\2\2\2\u01a9\u019e\3\2"+
		"\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01d5\5\22\n\2\u01ac"+
		"\u01ad\7 \2\2\u01ad\u01ae\7\21\2\2\u01ae\u01b3\5\32\16\2\u01af\u01b0\7"+
		"\23\2\2\u01b0\u01b2\5\32\16\2\u01b1\u01af\3\2\2\2\u01b2\u01b5\3\2\2\2"+
		"\u01b3\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b6\3\2\2\2\u01b5\u01b3"+
		"\3\2\2\2\u01b6\u01b7\7D\2\2\u01b7\u01b8\5\22\n\2\u01b8\u01d5\3\2\2\2\u01b9"+
		"\u01c5\t\5\2\2\u01ba\u01bb\7\21\2\2\u01bb\u01c0\5\32\16\2\u01bc\u01bd"+
		"\7\23\2\2\u01bd\u01bf\5\32\16\2\u01be\u01bc\3\2\2\2\u01bf\u01c2\3\2\2"+
		"\2\u01c0\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c3\3\2\2\2\u01c2\u01c0"+
		"\3\2\2\2\u01c3\u01c4\7D\2\2\u01c4\u01c6\3\2\2\2\u01c5\u01ba\3\2\2\2\u01c5"+
		"\u01c6\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01d5\5\22\n\2\u01c8\u01c9\7"+
		"\21\2\2\u01c9\u01cc\5\32\16\2\u01ca\u01cb\7\23\2\2\u01cb\u01cd\5\32\16"+
		"\2\u01cc\u01ca\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01cc\3\2\2\2\u01ce\u01cf"+
		"\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d1\7D\2\2\u01d1\u01d2\7\t\2\2\u01d2"+
		"\u01d3\5\32\16\2\u01d3\u01d5\3\2\2\2\u01d4\u0155\3\2\2\2\u01d4\u0158\3"+
		"\2\2\2\u01d4\u015a\3\2\2\2\u01d4\u015c\3\2\2\2\u01d4\u015e\3\2\2\2\u01d4"+
		"\u0160\3\2\2\2\u01d4\u0162\3\2\2\2\u01d4\u0165\3\2\2\2\u01d4\u0166\3\2"+
		"\2\2\u01d4\u0167\3\2\2\2\u01d4\u0168\3\2\2\2\u01d4\u0169\3\2\2\2\u01d4"+
		"\u016a\3\2\2\2\u01d4\u0177\3\2\2\2\u01d4\u0178\3\2\2\2\u01d4\u017c\3\2"+
		"\2\2\u01d4\u017e\3\2\2\2\u01d4\u0189\3\2\2\2\u01d4\u018b\3\2\2\2\u01d4"+
		"\u019b\3\2\2\2\u01d4\u019c\3\2\2\2\u01d4\u019d\3\2\2\2\u01d4\u01ac\3\2"+
		"\2\2\u01d4\u01b9\3\2\2\2\u01d4\u01c8\3\2\2\2\u01d5\u0241\3\2\2\2\u01d6"+
		"\u01d7\f.\2\2\u01d7\u01d8\7\b\2\2\u01d8\u0240\5\32\16/\u01d9\u01da\f-"+
		"\2\2\u01da\u01db\7(\2\2\u01db\u0240\5\32\16.\u01dc\u01dd\f,\2\2\u01dd"+
		"\u01de\7\61\2\2\u01de\u0240\5\32\16-\u01df\u01e0\f+\2\2\u01e0\u01e1\7"+
		"\n\2\2\u01e1\u0240\5\32\16,\u01e2\u01e3\f*\2\2\u01e3\u01e4\7;\2\2\u01e4"+
		"\u0240\5\32\16+\u01e5\u01e6\f\'\2\2\u01e6\u01e7\7\4\2\2\u01e7\u0240\5"+
		"\32\16(\u01e8\u01e9\f&\2\2\u01e9\u01ea\79\2\2\u01ea\u0240\5\32\16\'\u01eb"+
		"\u01ec\f%\2\2\u01ec\u01ed\7B\2\2\u01ed\u0240\5\32\16&\u01ee\u01ef\f$\2"+
		"\2\u01ef\u01f0\7$\2\2\u01f0\u0240\5\32\16%\u01f1\u01f2\f#\2\2\u01f2\u01f3"+
		"\7\63\2\2\u01f3\u0240\5\32\16$\u01f4\u01f5\f\"\2\2\u01f5\u01f6\7E\2\2"+
		"\u01f6\u0240\5\32\16#\u01f7\u01f8\f!\2\2\u01f8\u01f9\7!\2\2\u01f9\u0240"+
		"\5\32\16\"\u01fa\u01fb\f \2\2\u01fb\u01fc\7\33\2\2\u01fc\u0240\5\32\16"+
		"!\u01fd\u01fe\f\37\2\2\u01fe\u01ff\7<\2\2\u01ff\u0240\5\32\16 \u0200\u0201"+
		"\f\36\2\2\u0201\u0202\7/\2\2\u0202\u0240\5\32\16\37\u0203\u0204\f\35\2"+
		"\2\u0204\u0205\7\32\2\2\u0205\u0240\5\32\16\36\u0206\u0207\f\27\2\2\u0207"+
		"\u0208\7,\2\2\u0208\u0209\5\32\16\2\u0209\u020a\7\67\2\2\u020a\u020b\5"+
		"\32\16\30\u020b\u0240\3\2\2\2\u020c\u020d\f\26\2\2\u020d\u020e\7\26\2"+
		"\2\u020e\u0240\5\32\16\27\u020f\u0210\f\13\2\2\u0210\u0211\7\r\2\2\u0211"+
		"\u0240\5\32\16\f\u0212\u0213\f\n\2\2\u0213\u0214\7&\2\2\u0214\u0240\5"+
		"\32\16\13\u0215\u0216\f\t\2\2\u0216\u0217\7\24\2\2\u0217\u0240\5\32\16"+
		"\n\u0218\u0219\f\b\2\2\u0219\u021a\7#\2\2\u021a\u0240\5\32\16\t\u021b"+
		"\u021c\f\7\2\2\u021c\u021d\7\22\2\2\u021d\u0240\5\32\16\b\u021e\u021f"+
		"\f\6\2\2\u021f\u0220\7\3\2\2\u0220\u0240\5\32\16\7\u0221\u0222\f\3\2\2"+
		"\u0222\u0223\7\t\2\2\u0223\u0240\5\32\16\4\u0224\u0225\f\62\2\2\u0225"+
		"\u0226\7\64\2\2\u0226\u0227\7Q\2\2\u0227\u0230\7\21\2\2\u0228\u022d\5"+
		"\32\16\2\u0229\u022a\7\23\2\2\u022a\u022c\5\32\16\2\u022b\u0229\3\2\2"+
		"\2\u022c\u022f\3\2\2\2\u022d\u022b\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u0231"+
		"\3\2\2\2\u022f\u022d\3\2\2\2\u0230\u0228\3\2\2\2\u0230\u0231\3\2\2\2\u0231"+
		"\u0232\3\2\2\2\u0232\u0240\7D\2\2\u0233\u0234\f\60\2\2\u0234\u0235\78"+
		"\2\2\u0235\u0236\5\32\16\2\u0236\u0237\7\35\2\2\u0237\u0240\3\2\2\2\u0238"+
		"\u0239\f/\2\2\u0239\u023a\7\6\2\2\u023a\u023b\5\32\16\2\u023b\u023c\7"+
		"*\2\2\u023c\u0240\3\2\2\2\u023d\u023e\f(\2\2\u023e\u0240\t\4\2\2\u023f"+
		"\u01d6\3\2\2\2\u023f\u01d9\3\2\2\2\u023f\u01dc\3\2\2\2\u023f\u01df\3\2"+
		"\2\2\u023f\u01e2\3\2\2\2\u023f\u01e5\3\2\2\2\u023f\u01e8\3\2\2\2\u023f"+
		"\u01eb\3\2\2\2\u023f\u01ee\3\2\2\2\u023f\u01f1\3\2\2\2\u023f\u01f4\3\2"+
		"\2\2\u023f\u01f7\3\2\2\2\u023f\u01fa\3\2\2\2\u023f\u01fd\3\2\2\2\u023f"+
		"\u0200\3\2\2\2\u023f\u0203\3\2\2\2\u023f\u0206\3\2\2\2\u023f\u020c\3\2"+
		"\2\2\u023f\u020f\3\2\2\2\u023f\u0212\3\2\2\2\u023f\u0215\3\2\2\2\u023f"+
		"\u0218\3\2\2\2\u023f\u021b\3\2\2\2\u023f\u021e\3\2\2\2\u023f\u0221\3\2"+
		"\2\2\u023f\u0224\3\2\2\2\u023f\u0233\3\2\2\2\u023f\u0238\3\2\2\2\u023f"+
		"\u023d\3\2\2\2\u0240\u0243\3\2\2\2\u0241\u023f\3\2\2\2\u0241\u0242\3\2"+
		"\2\2\u0242\33\3\2\2\2\u0243\u0241\3\2\2\2\u0244\u0249\5\32\16\2\u0245"+
		"\u0246\7\23\2\2\u0246\u0248\5\32\16\2\u0247\u0245\3\2\2\2\u0248\u024b"+
		"\3\2\2\2\u0249\u0247\3\2\2\2\u0249\u024a\3\2\2\2\u024a\35\3\2\2\2\u024b"+
		"\u0249\3\2\2\2C!\',\63<IKTX]ksx}\u0084\u0089\u008e\u0093\u0098\u009d\u00a2"+
		"\u00a7\u00ac\u00b1\u00b6\u00bc\u00c0\u00c4\u00cb\u00d8\u00e3\u00eb\u00ee"+
		"\u00f5\u00fa\u00ff\u0108\u010b\u0110\u0116\u011d\u0124\u012a\u0132\u0139"+
		"\u0140\u0147\u014b\u014f\u0171\u0174\u0184\u0196\u01a4\u01a9\u01b3\u01c0"+
		"\u01c5\u01ce\u01d4\u022d\u0230\u023f\u0241\u0249";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}