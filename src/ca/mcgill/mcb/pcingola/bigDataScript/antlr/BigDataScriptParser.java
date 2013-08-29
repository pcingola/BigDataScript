// Generated from BigDataScript.g4 by ANTLR 4.1
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BigDataScriptParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__54=1, T__53=2, T__52=3, T__51=4, T__50=5, T__49=6, T__48=7, T__47=8, 
		T__46=9, T__45=10, T__44=11, T__43=12, T__42=13, T__41=14, T__40=15, T__39=16, 
		T__38=17, T__37=18, T__36=19, T__35=20, T__34=21, T__33=22, T__32=23, 
		T__31=24, T__30=25, T__29=26, T__28=27, T__27=28, T__26=29, T__25=30, 
		T__24=31, T__23=32, T__22=33, T__21=34, T__20=35, T__19=36, T__18=37, 
		T__17=38, T__16=39, T__15=40, T__14=41, T__13=42, T__12=43, T__11=44, 
		T__10=45, T__9=46, T__8=47, T__7=48, T__6=49, T__5=50, T__4=51, T__3=52, 
		T__2=53, T__1=54, T__0=55, BOOL_LITERAL=56, INT_LITERAL=57, REAL_LITERAL=58, 
		STRING_LITERAL=59, STRING_LITERAL_SINGLE=60, SYS_LITERAL=61, TASK_LITERAL=62, 
		COMMENT=63, COMMENT_LINE=64, COMMENT_LINE_HASH=65, ID=66, WS=67;
	public static final String[] tokenNames = {
		"<INVALID>", "'&'", "'*'", "'['", "'<'", "'--'", "'continue'", "'!='", 
		"'<='", "'kill'", "'}'", "'%'", "'bool'", "')'", "'='", "'|'", "'!'", 
		"'real'", "']'", "','", "'\n'", "'-'", "'while'", "'exit'", "'('", "':'", 
		"'if'", "'int'", "'?'", "'void'", "'{'", "'wait'", "'break'", "'else'", 
		"'<-'", "'++'", "'checkpoint'", "'error'", "'^'", "'.'", "'=>'", "'+'", 
		"'for'", "'return'", "'warning'", "';'", "'&&'", "'include'", "'||'", 
		"'>'", "'string'", "'/'", "'=='", "'~'", "'>='", "'task'", "BOOL_LITERAL", 
		"INT_LITERAL", "REAL_LITERAL", "STRING_LITERAL", "STRING_LITERAL_SINGLE", 
		"SYS_LITERAL", "TASK_LITERAL", "COMMENT", "COMMENT_LINE", "COMMENT_LINE_HASH", 
		"ID", "WS"
	};
	public static final int
		RULE_programUnit = 0, RULE_eol = 1, RULE_typeList = 2, RULE_type = 3, 
		RULE_varDeclaration = 4, RULE_variableInit = 5, RULE_includeF = 6, RULE_statement = 7, 
		RULE_forInit = 8, RULE_forCondition = 9, RULE_forEnd = 10, RULE_expression = 11, 
		RULE_expressionList = 12;
	public static final String[] ruleNames = {
		"programUnit", "eol", "typeList", "type", "varDeclaration", "variableInit", 
		"includeF", "statement", "forInit", "forCondition", "forEnd", "expression", 
		"expressionList"
	};

	@Override
	public String getGrammarFileName() { return "BigDataScript.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public BigDataScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramUnitContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
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
			setState(29);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(26); eol();
					}
					} 
				}
				setState(31);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(33); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(32); statement();
				}
				}
				setState(35); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (6 - 3)) | (1L << (9 - 3)) | (1L << (12 - 3)) | (1L << (16 - 3)) | (1L << (17 - 3)) | (1L << (20 - 3)) | (1L << (21 - 3)) | (1L << (22 - 3)) | (1L << (23 - 3)) | (1L << (24 - 3)) | (1L << (26 - 3)) | (1L << (27 - 3)) | (1L << (29 - 3)) | (1L << (30 - 3)) | (1L << (31 - 3)) | (1L << (32 - 3)) | (1L << (35 - 3)) | (1L << (36 - 3)) | (1L << (37 - 3)) | (1L << (41 - 3)) | (1L << (42 - 3)) | (1L << (43 - 3)) | (1L << (44 - 3)) | (1L << (45 - 3)) | (1L << (47 - 3)) | (1L << (50 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0) );
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
			setState(38); 
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(37);
					_la = _input.LA(1);
					if ( !(_la==20 || _la==45) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(40); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			} while ( _alt!=2 && _alt!=-1 );
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
			setState(42); type(0);
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==19) {
				{
				{
				setState(43); match(19);
				setState(44); type(0);
				}
				}
				setState(49);
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
		public int _p;
		public TypeContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public TypeContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
			this._p = ctx._p;
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

	public final TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState, _p);
		TypeContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, RULE_type);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			switch (_input.LA(1)) {
			case 12:
				{
				_localctx = new TypePrimitiveBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(51); match(12);
				}
				break;
			case 27:
				{
				_localctx = new TypePrimitiveIntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(27);
				}
				break;
			case 17:
				{
				_localctx = new TypePrimitiveRealContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(53); match(17);
				}
				break;
			case 50:
				{
				_localctx = new TypePrimitiveStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(54); match(50);
				}
				break;
			case 29:
				{
				_localctx = new TypePrimitiveVoidContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(55); match(29);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(71);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(69);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new TypeArrayContext(new TypeContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(58);
						if (!(3 >= _localctx._p)) throw new FailedPredicateException(this, "3 >= $_p");
						setState(59); match(3);
						setState(60); match(18);
						}
						break;

					case 2:
						{
						_localctx = new TypeMapContext(new TypeContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(61);
						if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
						setState(62); match(30);
						setState(63); match(10);
						}
						break;

					case 3:
						{
						_localctx = new TypeMapContext(new TypeContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(64);
						if (!(1 >= _localctx._p)) throw new FailedPredicateException(this, "1 >= $_p");
						setState(65); match(30);
						setState(66); type(0);
						setState(67); match(10);
						}
						break;
					}
					} 
				}
				setState(73);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(74); type(0);
			setState(75); variableInit();
			setState(80);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(76); match(19);
					setState(77); variableInit();
					}
					} 
				}
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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
			setState(83); match(ID);
			setState(86);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(84); match(14);
				setState(85); expression(0);
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

	public static class IncludeFContext extends ParserRuleContext {
		public EolContext eol() {
			return getRuleContext(EolContext.class,0);
		}
		public TerminalNode STRING_LITERAL_SINGLE() { return getToken(BigDataScriptParser.STRING_LITERAL_SINGLE, 0); }
		public IncludeFContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeF; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterIncludeF(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitIncludeF(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitIncludeF(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncludeFContext includeF() throws RecognitionException {
		IncludeFContext _localctx = new IncludeFContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_includeF);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); match(47);
			setState(89); match(STRING_LITERAL_SINGLE);
			setState(90); eol();
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
	public static class StatementIncludeContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public IncludeFContext includeF() {
			return getRuleContext(IncludeFContext.class,0);
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
	public static class BlockContext extends StatementContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
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
	public static class WaitContext extends StatementContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
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
	public static class WhileContext extends StatementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
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
	public static class IfContext extends StatementContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
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
	public static class ForLoopListContext extends StatementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public VarDeclarationContext varDeclaration() {
			return getRuleContext(VarDeclarationContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
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
	public static class FunctionDeclarationContext extends StatementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
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
	public static class StatmentExprContext extends StatementContext {
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
		}
		public StatmentExprContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).enterStatmentExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BigDataScriptListener ) ((BigDataScriptListener)listener).exitStatmentExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BigDataScriptVisitor ) return ((BigDataScriptVisitor<? extends T>)visitor).visitStatmentExpr(this);
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
	public static class ForLoopContext extends StatementContext {
		public ForEndContext end;
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ForEndContext forEnd() {
			return getRuleContext(ForEndContext.class,0);
		}
		public EolContext eol(int i) {
			return getRuleContext(EolContext.class,i);
		}
		public ForConditionContext forCondition() {
			return getRuleContext(ForConditionContext.class,0);
		}
		public List<EolContext> eol() {
			return getRuleContexts(EolContext.class);
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

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(298);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				_localctx = new BlockContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(92); match(30);
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (6 - 3)) | (1L << (9 - 3)) | (1L << (12 - 3)) | (1L << (16 - 3)) | (1L << (17 - 3)) | (1L << (20 - 3)) | (1L << (21 - 3)) | (1L << (22 - 3)) | (1L << (23 - 3)) | (1L << (24 - 3)) | (1L << (26 - 3)) | (1L << (27 - 3)) | (1L << (29 - 3)) | (1L << (30 - 3)) | (1L << (31 - 3)) | (1L << (32 - 3)) | (1L << (35 - 3)) | (1L << (36 - 3)) | (1L << (37 - 3)) | (1L << (41 - 3)) | (1L << (42 - 3)) | (1L << (43 - 3)) | (1L << (44 - 3)) | (1L << (45 - 3)) | (1L << (47 - 3)) | (1L << (50 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
					{
					{
					setState(93); statement();
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(99); match(10);
				}
				break;

			case 2:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(100); match(32);
				setState(104);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(101); eol();
						}
						} 
					}
					setState(106);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				}
				break;

			case 3:
				_localctx = new CheckpointContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(107); match(36);
				setState(109);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(108); expression(0);
					}
					break;
				}
				setState(114);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(111); eol();
						}
						} 
					}
					setState(116);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				}
				}
				break;

			case 4:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(117); match(6);
				setState(121);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(118); eol();
						}
						} 
					}
					setState(123);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;

			case 5:
				_localctx = new ExitContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(124); match(23);
				setState(126);
				switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
				case 1:
					{
					setState(125); expression(0);
					}
					break;
				}
				setState(131);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(128); eol();
						}
						} 
					}
					setState(133);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
				}
				}
				break;

			case 6:
				_localctx = new WarningContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(134); match(44);
				setState(136);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(135); expression(0);
					}
					break;
				}
				setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(138); eol();
						}
						} 
					}
					setState(143);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				}
				}
				break;

			case 7:
				_localctx = new ErrorContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(144); match(37);
				setState(146);
				switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
				case 1:
					{
					setState(145); expression(0);
					}
					break;
				}
				setState(151);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(148); eol();
						}
						} 
					}
					setState(153);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				}
				break;

			case 8:
				_localctx = new ForLoopContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(154); match(42);
				setState(155); match(24);
				setState(157);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (12 - 3)) | (1L << (16 - 3)) | (1L << (17 - 3)) | (1L << (21 - 3)) | (1L << (24 - 3)) | (1L << (27 - 3)) | (1L << (29 - 3)) | (1L << (30 - 3)) | (1L << (35 - 3)) | (1L << (41 - 3)) | (1L << (50 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
					{
					setState(156); forInit();
					}
				}

				setState(159); match(45);
				setState(161);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (16 - 3)) | (1L << (21 - 3)) | (1L << (24 - 3)) | (1L << (30 - 3)) | (1L << (35 - 3)) | (1L << (41 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
					{
					setState(160); forCondition();
					}
				}

				setState(163); match(45);
				setState(165);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (16 - 3)) | (1L << (21 - 3)) | (1L << (24 - 3)) | (1L << (30 - 3)) | (1L << (35 - 3)) | (1L << (41 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
					{
					setState(164); ((ForLoopContext)_localctx).end = forEnd();
					}
				}

				setState(167); match(13);
				setState(168); statement();
				setState(172);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(169); eol();
						}
						} 
					}
					setState(174);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				}
				}
				break;

			case 9:
				_localctx = new ForLoopListContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(175); match(42);
				setState(176); match(24);
				setState(177); varDeclaration();
				setState(178); match(25);
				setState(179); expression(0);
				setState(180); match(13);
				setState(181); statement();
				setState(185);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(182); eol();
						}
						} 
					}
					setState(187);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				}
				break;

			case 10:
				_localctx = new IfContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(188); match(26);
				setState(189); match(24);
				setState(190); expression(0);
				setState(191); match(13);
				setState(192); statement();
				setState(196);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(193); eol();
						}
						} 
					}
					setState(198);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				setState(207);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(199); match(33);
					setState(200); statement();
					setState(204);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
					while ( _alt!=2 && _alt!=-1 ) {
						if ( _alt==1 ) {
							{
							{
							setState(201); eol();
							}
							} 
						}
						setState(206);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
					}
					}
					break;
				}
				}
				break;

			case 11:
				_localctx = new KillContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(209); match(9);
				setState(210); expression(0);
				setState(214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(211); eol();
						}
						} 
					}
					setState(216);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				}
				break;

			case 12:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(217); match(43);
				setState(219);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(218); expression(0);
					}
					break;
				}
				setState(224);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(221); eol();
						}
						} 
					}
					setState(226);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				}
				break;

			case 13:
				_localctx = new WaitContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(227); match(31);
				setState(236);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(228); expression(0);
					setState(233);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
					while ( _alt!=2 && _alt!=-1 ) {
						if ( _alt==1 ) {
							{
							{
							setState(229); match(19);
							setState(230); expression(0);
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
				setState(241);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(238); eol();
						}
						} 
					}
					setState(243);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				}
				}
				break;

			case 14:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(244); match(22);
				setState(245); match(24);
				setState(246); expression(0);
				setState(247); match(13);
				setState(248); statement();
				setState(252);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(249); eol();
						}
						} 
					}
					setState(254);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				}
				}
				break;

			case 15:
				_localctx = new FunctionDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(255); type(0);
				setState(256); match(ID);
				setState(257); match(24);
				setState(259);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 12) | (1L << 17) | (1L << 27) | (1L << 29) | (1L << 50))) != 0)) {
					{
					setState(258); varDeclaration();
					}
				}

				setState(265);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==19) {
					{
					{
					setState(261); match(19);
					setState(262); varDeclaration();
					}
					}
					setState(267);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(268); match(13);
				setState(269); statement();
				setState(273);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(270); eol();
						}
						} 
					}
					setState(275);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				}
				}
				break;

			case 16:
				_localctx = new StatementVarDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(276); varDeclaration();
				setState(280);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(277); eol();
						}
						} 
					}
					setState(282);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				}
				}
				break;

			case 17:
				_localctx = new StatmentExprContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(283); expression(0);
				setState(287);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(284); eol();
						}
						} 
					}
					setState(289);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				}
				}
				break;

			case 18:
				_localctx = new StatementIncludeContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(290); includeF();
				setState(294);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(291); eol();
						}
						} 
					}
					setState(296);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				}
				}
				break;

			case 19:
				_localctx = new StatmentEolContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(297); eol();
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
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public VarDeclarationContext varDeclaration() {
			return getRuleContext(VarDeclarationContext.class,0);
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
		enterRule(_localctx, 16, RULE_forInit);
		try {
			setState(302);
			switch (_input.LA(1)) {
			case 12:
			case 17:
			case 27:
			case 29:
			case 50:
				enterOuterAlt(_localctx, 1);
				{
				setState(300); varDeclaration();
				}
				break;
			case 3:
			case 5:
			case 16:
			case 21:
			case 24:
			case 30:
			case 35:
			case 41:
			case 53:
			case 55:
			case BOOL_LITERAL:
			case INT_LITERAL:
			case REAL_LITERAL:
			case STRING_LITERAL:
			case STRING_LITERAL_SINGLE:
			case SYS_LITERAL:
			case TASK_LITERAL:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(301); expressionList();
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
		enterRule(_localctx, 18, RULE_forCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304); expression(0);
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
		enterRule(_localctx, 20, RULE_forEnd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306); expressionList();
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
		public int _p;
		public ExpressionContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ExpressionContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
			this._p = ctx._p;
		}
	}
	public static class ExpressionDepContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
	public static class MethodCallContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
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
	public static class FunctionCallContext extends ExpressionContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ID() { return getToken(BigDataScriptParser.ID, 0); }
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
	public static class ExpressionTaskContext extends ExpressionContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
	public static class LiteralStringContext extends ExpressionContext {
		public TerminalNode STRING_LITERAL_SINGLE() { return getToken(BigDataScriptParser.STRING_LITERAL_SINGLE, 0); }
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

	public final ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState, _p);
		ExpressionContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, RULE_expression);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				_localctx = new PreContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(309);
				_la = _input.LA(1);
				if ( !(_la==5 || _la==35) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(310); expression(29);
				}
				break;

			case 2:
				{
				_localctx = new ExpressionBitNegationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(311); match(53);
				setState(312); expression(13);
				}
				break;

			case 3:
				{
				_localctx = new ExpressionLogicNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(313); match(16);
				setState(314); expression(12);
				}
				break;

			case 4:
				{
				_localctx = new ExpressionUnaryMinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(315); match(21);
				setState(316); expression(11);
				}
				break;

			case 5:
				{
				_localctx = new ExpressionUnaryPlusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(317); match(41);
				setState(318); expression(10);
				}
				break;

			case 6:
				{
				_localctx = new LiteralBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(319); match(BOOL_LITERAL);
				}
				break;

			case 7:
				{
				_localctx = new LiteralIntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(320); match(INT_LITERAL);
				}
				break;

			case 8:
				{
				_localctx = new LiteralRealContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(321); match(REAL_LITERAL);
				}
				break;

			case 9:
				{
				_localctx = new LiteralStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(322); match(STRING_LITERAL);
				}
				break;

			case 10:
				{
				_localctx = new LiteralStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(323); match(STRING_LITERAL_SINGLE);
				}
				break;

			case 11:
				{
				_localctx = new FunctionCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(324); match(ID);
				setState(325); match(24);
				setState(334);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (16 - 3)) | (1L << (21 - 3)) | (1L << (24 - 3)) | (1L << (30 - 3)) | (1L << (35 - 3)) | (1L << (41 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
					{
					setState(326); expression(0);
					setState(331);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==19) {
						{
						{
						setState(327); match(19);
						setState(328); expression(0);
						}
						}
						setState(333);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(336); match(13);
				}
				break;

			case 12:
				{
				_localctx = new VarReferenceContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(337); match(ID);
				}
				break;

			case 13:
				{
				_localctx = new ExpressionParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(338); match(24);
				setState(339); expression(0);
				setState(340); match(13);
				}
				break;

			case 14:
				{
				_localctx = new LiteralListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(342); match(3);
				setState(343); expression(0);
				setState(348);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==19) {
					{
					{
					setState(344); match(19);
					setState(345); expression(0);
					}
					}
					setState(350);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(351); match(18);
				}
				break;

			case 15:
				{
				_localctx = new LiteralMapContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(353); match(30);
				setState(354); expression(0);
				setState(355); match(40);
				setState(356); expression(0);
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==19) {
					{
					{
					setState(357); match(19);
					setState(358); expression(0);
					setState(359); match(40);
					setState(360); expression(0);
					}
					}
					setState(366);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(367); match(10);
				}
				break;

			case 16:
				{
				_localctx = new ExpressionSysContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(369); match(SYS_LITERAL);
				}
				break;

			case 17:
				{
				_localctx = new ExpressionTaskLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(370); match(TASK_LITERAL);
				}
				break;

			case 18:
				{
				_localctx = new ExpressionTaskContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(371); match(55);
				setState(383);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(372); match(24);
					setState(373); expression(0);
					setState(378);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==19) {
						{
						{
						setState(374); match(19);
						setState(375); expression(0);
						}
						}
						setState(380);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(381); match(13);
					}
					break;
				}
				setState(385); statement();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(477);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(475);
					switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionLogicAndContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(388);
						if (!(34 >= _localctx._p)) throw new FailedPredicateException(this, "34 >= $_p");
						setState(389); match(46);
						setState(390); expression(35);
						}
						break;

					case 2:
						{
						_localctx = new ExpressionLogicOrContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(391);
						if (!(33 >= _localctx._p)) throw new FailedPredicateException(this, "33 >= $_p");
						setState(392); match(48);
						setState(393); expression(34);
						}
						break;

					case 3:
						{
						_localctx = new ExpressionBitAndContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(394);
						if (!(32 >= _localctx._p)) throw new FailedPredicateException(this, "32 >= $_p");
						setState(395); match(1);
						setState(396); expression(33);
						}
						break;

					case 4:
						{
						_localctx = new ExpressionBitXorContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(397);
						if (!(31 >= _localctx._p)) throw new FailedPredicateException(this, "31 >= $_p");
						setState(398); match(38);
						setState(399); expression(32);
						}
						break;

					case 5:
						{
						_localctx = new ExpressionBitOrContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(400);
						if (!(30 >= _localctx._p)) throw new FailedPredicateException(this, "30 >= $_p");
						setState(401); match(15);
						setState(402); expression(31);
						}
						break;

					case 6:
						{
						_localctx = new ExpressionNeContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(403);
						if (!(27 >= _localctx._p)) throw new FailedPredicateException(this, "27 >= $_p");
						setState(404); match(7);
						setState(405); expression(28);
						}
						break;

					case 7:
						{
						_localctx = new ExpressionEqContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(406);
						if (!(26 >= _localctx._p)) throw new FailedPredicateException(this, "26 >= $_p");
						setState(407); match(52);
						setState(408); expression(27);
						}
						break;

					case 8:
						{
						_localctx = new ExpressionModuloContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(409);
						if (!(22 >= _localctx._p)) throw new FailedPredicateException(this, "22 >= $_p");
						setState(410); match(11);
						setState(411); expression(23);
						}
						break;

					case 9:
						{
						_localctx = new ExpressionDivideContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(412);
						if (!(21 >= _localctx._p)) throw new FailedPredicateException(this, "21 >= $_p");
						setState(413); match(51);
						setState(414); expression(22);
						}
						break;

					case 10:
						{
						_localctx = new ExpressionTimesContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(415);
						if (!(20 >= _localctx._p)) throw new FailedPredicateException(this, "20 >= $_p");
						setState(416); match(2);
						setState(417); expression(21);
						}
						break;

					case 11:
						{
						_localctx = new ExpressionMinusContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(418);
						if (!(19 >= _localctx._p)) throw new FailedPredicateException(this, "19 >= $_p");
						setState(419); match(21);
						setState(420); expression(20);
						}
						break;

					case 12:
						{
						_localctx = new ExpressionPlusContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(421);
						if (!(18 >= _localctx._p)) throw new FailedPredicateException(this, "18 >= $_p");
						setState(422); match(41);
						setState(423); expression(19);
						}
						break;

					case 13:
						{
						_localctx = new ExpressionLtContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(424);
						if (!(17 >= _localctx._p)) throw new FailedPredicateException(this, "17 >= $_p");
						setState(425); match(4);
						setState(426); expression(18);
						}
						break;

					case 14:
						{
						_localctx = new ExpressionGtContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(427);
						if (!(16 >= _localctx._p)) throw new FailedPredicateException(this, "16 >= $_p");
						setState(428); match(49);
						setState(429); expression(17);
						}
						break;

					case 15:
						{
						_localctx = new ExpressionLeContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(430);
						if (!(15 >= _localctx._p)) throw new FailedPredicateException(this, "15 >= $_p");
						setState(431); match(8);
						setState(432); expression(16);
						}
						break;

					case 16:
						{
						_localctx = new ExpressionGeContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(433);
						if (!(14 >= _localctx._p)) throw new FailedPredicateException(this, "14 >= $_p");
						setState(434); match(54);
						setState(435); expression(15);
						}
						break;

					case 17:
						{
						_localctx = new ExpressionDepContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(436);
						if (!(7 >= _localctx._p)) throw new FailedPredicateException(this, "7 >= $_p");
						setState(437); match(34);
						setState(438); expression(8);
						}
						break;

					case 18:
						{
						_localctx = new ExpressionAssignmentContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(439);
						if (!(1 >= _localctx._p)) throw new FailedPredicateException(this, "1 >= $_p");
						setState(440); match(14);
						setState(441); expression(2);
						}
						break;

					case 19:
						{
						_localctx = new ExpressionCondContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(442);
						if (!(8 >= _localctx._p)) throw new FailedPredicateException(this, "8 >= $_p");
						setState(443); match(28);
						setState(444); expression(0);
						setState(445); match(25);
						setState(446); expression(9);
						}
						break;

					case 20:
						{
						_localctx = new MethodCallContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(448);
						if (!(35 >= _localctx._p)) throw new FailedPredicateException(this, "35 >= $_p");
						setState(449); match(39);
						setState(450); match(ID);
						setState(451); match(24);
						setState(460);
						_la = _input.LA(1);
						if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (3 - 3)) | (1L << (5 - 3)) | (1L << (16 - 3)) | (1L << (21 - 3)) | (1L << (24 - 3)) | (1L << (30 - 3)) | (1L << (35 - 3)) | (1L << (41 - 3)) | (1L << (53 - 3)) | (1L << (55 - 3)) | (1L << (BOOL_LITERAL - 3)) | (1L << (INT_LITERAL - 3)) | (1L << (REAL_LITERAL - 3)) | (1L << (STRING_LITERAL - 3)) | (1L << (STRING_LITERAL_SINGLE - 3)) | (1L << (SYS_LITERAL - 3)) | (1L << (TASK_LITERAL - 3)) | (1L << (ID - 3)))) != 0)) {
							{
							setState(452); expression(0);
							setState(457);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==19) {
								{
								{
								setState(453); match(19);
								setState(454); expression(0);
								}
								}
								setState(459);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(462); match(13);
						}
						break;

					case 21:
						{
						_localctx = new PostContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(463);
						if (!(28 >= _localctx._p)) throw new FailedPredicateException(this, "28 >= $_p");
						setState(464);
						_la = _input.LA(1);
						if ( !(_la==5 || _la==35) ) {
						_errHandler.recoverInline(this);
						}
						consume();
						}
						break;

					case 22:
						{
						_localctx = new VarReferenceListContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(465);
						if (!(24 >= _localctx._p)) throw new FailedPredicateException(this, "24 >= $_p");
						setState(466); match(3);
						setState(467); expression(0);
						setState(468); match(18);
						}
						break;

					case 23:
						{
						_localctx = new VarReferenceMapContext(new ExpressionContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(470);
						if (!(23 >= _localctx._p)) throw new FailedPredicateException(this, "23 >= $_p");
						setState(471); match(30);
						setState(472); expression(0);
						setState(473); match(10);
						}
						break;
					}
					} 
				}
				setState(479);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
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
		enterRule(_localctx, 24, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480); expression(0);
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==19) {
				{
				{
				setState(481); match(19);
				setState(482); expression(0);
				}
				}
				setState(487);
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

		case 11: return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3: return 34 >= _localctx._p;

		case 4: return 33 >= _localctx._p;

		case 5: return 32 >= _localctx._p;

		case 6: return 31 >= _localctx._p;

		case 7: return 30 >= _localctx._p;

		case 8: return 27 >= _localctx._p;

		case 9: return 26 >= _localctx._p;

		case 10: return 22 >= _localctx._p;

		case 11: return 21 >= _localctx._p;

		case 12: return 20 >= _localctx._p;

		case 13: return 19 >= _localctx._p;

		case 14: return 18 >= _localctx._p;

		case 15: return 17 >= _localctx._p;

		case 17: return 15 >= _localctx._p;

		case 16: return 16 >= _localctx._p;

		case 19: return 7 >= _localctx._p;

		case 18: return 14 >= _localctx._p;

		case 21: return 8 >= _localctx._p;

		case 20: return 1 >= _localctx._p;

		case 23: return 28 >= _localctx._p;

		case 22: return 35 >= _localctx._p;

		case 25: return 23 >= _localctx._p;

		case 24: return 24 >= _localctx._p;
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return 3 >= _localctx._p;

		case 1: return 2 >= _localctx._p;

		case 2: return 1 >= _localctx._p;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3E\u01eb\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\7\2\36\n\2\f\2\16\2!\13\2\3\2\6\2$\n"+
		"\2\r\2\16\2%\3\3\6\3)\n\3\r\3\16\3*\3\4\3\4\3\4\7\4\60\n\4\f\4\16\4\63"+
		"\13\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5;\n\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\7\5H\n\5\f\5\16\5K\13\5\3\6\3\6\3\6\3\6\7\6Q\n\6\f\6\16\6"+
		"T\13\6\3\7\3\7\3\7\5\7Y\n\7\3\b\3\b\3\b\3\b\3\t\3\t\7\ta\n\t\f\t\16\t"+
		"d\13\t\3\t\3\t\3\t\7\ti\n\t\f\t\16\tl\13\t\3\t\3\t\5\tp\n\t\3\t\7\ts\n"+
		"\t\f\t\16\tv\13\t\3\t\3\t\7\tz\n\t\f\t\16\t}\13\t\3\t\3\t\5\t\u0081\n"+
		"\t\3\t\7\t\u0084\n\t\f\t\16\t\u0087\13\t\3\t\3\t\5\t\u008b\n\t\3\t\7\t"+
		"\u008e\n\t\f\t\16\t\u0091\13\t\3\t\3\t\5\t\u0095\n\t\3\t\7\t\u0098\n\t"+
		"\f\t\16\t\u009b\13\t\3\t\3\t\3\t\5\t\u00a0\n\t\3\t\3\t\5\t\u00a4\n\t\3"+
		"\t\3\t\5\t\u00a8\n\t\3\t\3\t\3\t\7\t\u00ad\n\t\f\t\16\t\u00b0\13\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00ba\n\t\f\t\16\t\u00bd\13\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\7\t\u00c5\n\t\f\t\16\t\u00c8\13\t\3\t\3\t\3\t\7\t\u00cd"+
		"\n\t\f\t\16\t\u00d0\13\t\5\t\u00d2\n\t\3\t\3\t\3\t\7\t\u00d7\n\t\f\t\16"+
		"\t\u00da\13\t\3\t\3\t\5\t\u00de\n\t\3\t\7\t\u00e1\n\t\f\t\16\t\u00e4\13"+
		"\t\3\t\3\t\3\t\3\t\7\t\u00ea\n\t\f\t\16\t\u00ed\13\t\5\t\u00ef\n\t\3\t"+
		"\7\t\u00f2\n\t\f\t\16\t\u00f5\13\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00fd\n"+
		"\t\f\t\16\t\u0100\13\t\3\t\3\t\3\t\3\t\5\t\u0106\n\t\3\t\3\t\7\t\u010a"+
		"\n\t\f\t\16\t\u010d\13\t\3\t\3\t\3\t\7\t\u0112\n\t\f\t\16\t\u0115\13\t"+
		"\3\t\3\t\7\t\u0119\n\t\f\t\16\t\u011c\13\t\3\t\3\t\7\t\u0120\n\t\f\t\16"+
		"\t\u0123\13\t\3\t\3\t\7\t\u0127\n\t\f\t\16\t\u012a\13\t\3\t\5\t\u012d"+
		"\n\t\3\n\3\n\5\n\u0131\n\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u014c\n"+
		"\r\f\r\16\r\u014f\13\r\5\r\u0151\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\7\r\u015d\n\r\f\r\16\r\u0160\13\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\7\r\u016d\n\r\f\r\16\r\u0170\13\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\7\r\u017b\n\r\f\r\16\r\u017e\13\r\3\r\3\r\5\r\u0182\n\r"+
		"\3\r\5\r\u0185\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\7\r\u01ca\n\r\f\r\16\r\u01cd\13\r\5\r\u01cf\n\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u01de\n\r\f\r\16\r\u01e1\13\r"+
		"\3\16\3\16\3\16\7\16\u01e6\n\16\f\16\16\16\u01e9\13\16\3\16\2\17\2\4\6"+
		"\b\n\f\16\20\22\24\26\30\32\2\4\4\2\26\26//\4\2\7\7%%\u024e\2\37\3\2\2"+
		"\2\4(\3\2\2\2\6,\3\2\2\2\b:\3\2\2\2\nL\3\2\2\2\fU\3\2\2\2\16Z\3\2\2\2"+
		"\20\u012c\3\2\2\2\22\u0130\3\2\2\2\24\u0132\3\2\2\2\26\u0134\3\2\2\2\30"+
		"\u0184\3\2\2\2\32\u01e2\3\2\2\2\34\36\5\4\3\2\35\34\3\2\2\2\36!\3\2\2"+
		"\2\37\35\3\2\2\2\37 \3\2\2\2 #\3\2\2\2!\37\3\2\2\2\"$\5\20\t\2#\"\3\2"+
		"\2\2$%\3\2\2\2%#\3\2\2\2%&\3\2\2\2&\3\3\2\2\2\')\t\2\2\2(\'\3\2\2\2)*"+
		"\3\2\2\2*(\3\2\2\2*+\3\2\2\2+\5\3\2\2\2,\61\5\b\5\2-.\7\25\2\2.\60\5\b"+
		"\5\2/-\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\7\3\2\2\2\63"+
		"\61\3\2\2\2\64\65\b\5\1\2\65;\7\16\2\2\66;\7\35\2\2\67;\7\23\2\28;\7\64"+
		"\2\29;\7\37\2\2:\64\3\2\2\2:\66\3\2\2\2:\67\3\2\2\2:8\3\2\2\2:9\3\2\2"+
		"\2;I\3\2\2\2<=\6\5\2\3=>\7\5\2\2>H\7\24\2\2?@\6\5\3\3@A\7 \2\2AH\7\f\2"+
		"\2BC\6\5\4\3CD\7 \2\2DE\5\b\5\2EF\7\f\2\2FH\3\2\2\2G<\3\2\2\2G?\3\2\2"+
		"\2GB\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2J\t\3\2\2\2KI\3\2\2\2LM\5\b"+
		"\5\2MR\5\f\7\2NO\7\25\2\2OQ\5\f\7\2PN\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3"+
		"\2\2\2S\13\3\2\2\2TR\3\2\2\2UX\7D\2\2VW\7\20\2\2WY\5\30\r\2XV\3\2\2\2"+
		"XY\3\2\2\2Y\r\3\2\2\2Z[\7\61\2\2[\\\7>\2\2\\]\5\4\3\2]\17\3\2\2\2^b\7"+
		" \2\2_a\5\20\t\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3"+
		"\2\2\2e\u012d\7\f\2\2fj\7\"\2\2gi\5\4\3\2hg\3\2\2\2il\3\2\2\2jh\3\2\2"+
		"\2jk\3\2\2\2k\u012d\3\2\2\2lj\3\2\2\2mo\7&\2\2np\5\30\r\2on\3\2\2\2op"+
		"\3\2\2\2pt\3\2\2\2qs\5\4\3\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2u"+
		"\u012d\3\2\2\2vt\3\2\2\2w{\7\b\2\2xz\5\4\3\2yx\3\2\2\2z}\3\2\2\2{y\3\2"+
		"\2\2{|\3\2\2\2|\u012d\3\2\2\2}{\3\2\2\2~\u0080\7\31\2\2\177\u0081\5\30"+
		"\r\2\u0080\177\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0085\3\2\2\2\u0082\u0084"+
		"\5\4\3\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u012d\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u008a\7."+
		"\2\2\u0089\u008b\5\30\r\2\u008a\u0089\3\2\2\2\u008a\u008b\3\2\2\2\u008b"+
		"\u008f\3\2\2\2\u008c\u008e\5\4\3\2\u008d\u008c\3\2\2\2\u008e\u0091\3\2"+
		"\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u012d\3\2\2\2\u0091"+
		"\u008f\3\2\2\2\u0092\u0094\7\'\2\2\u0093\u0095\5\30\r\2\u0094\u0093\3"+
		"\2\2\2\u0094\u0095\3\2\2\2\u0095\u0099\3\2\2\2\u0096\u0098\5\4\3\2\u0097"+
		"\u0096\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2"+
		"\2\2\u009a\u012d\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u009d\7,\2\2\u009d"+
		"\u009f\7\32\2\2\u009e\u00a0\5\22\n\2\u009f\u009e\3\2\2\2\u009f\u00a0\3"+
		"\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3\7/\2\2\u00a2\u00a4\5\24\13\2\u00a3"+
		"\u00a2\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a7\7/"+
		"\2\2\u00a6\u00a8\5\26\f\2\u00a7\u00a6\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"+
		"\u00a9\3\2\2\2\u00a9\u00aa\7\17\2\2\u00aa\u00ae\5\20\t\2\u00ab\u00ad\5"+
		"\4\3\2\u00ac\u00ab\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae"+
		"\u00af\3\2\2\2\u00af\u012d\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b2\7,"+
		"\2\2\u00b2\u00b3\7\32\2\2\u00b3\u00b4\5\n\6\2\u00b4\u00b5\7\33\2\2\u00b5"+
		"\u00b6\5\30\r\2\u00b6\u00b7\7\17\2\2\u00b7\u00bb\5\20\t\2\u00b8\u00ba"+
		"\5\4\3\2\u00b9\u00b8\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb"+
		"\u00bc\3\2\2\2\u00bc\u012d\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00bf\7\34"+
		"\2\2\u00bf\u00c0\7\32\2\2\u00c0\u00c1\5\30\r\2\u00c1\u00c2\7\17\2\2\u00c2"+
		"\u00c6\5\20\t\2\u00c3\u00c5\5\4\3\2\u00c4\u00c3\3\2\2\2\u00c5\u00c8\3"+
		"\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00d1\3\2\2\2\u00c8"+
		"\u00c6\3\2\2\2\u00c9\u00ca\7#\2\2\u00ca\u00ce\5\20\t\2\u00cb\u00cd\5\4"+
		"\3\2\u00cc\u00cb\3\2\2\2\u00cd\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00c9\3\2"+
		"\2\2\u00d1\u00d2\3\2\2\2\u00d2\u012d\3\2\2\2\u00d3\u00d4\7\13\2\2\u00d4"+
		"\u00d8\5\30\r\2\u00d5\u00d7\5\4\3\2\u00d6\u00d5\3\2\2\2\u00d7\u00da\3"+
		"\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u012d\3\2\2\2\u00da"+
		"\u00d8\3\2\2\2\u00db\u00dd\7-\2\2\u00dc\u00de\5\30\r\2\u00dd\u00dc\3\2"+
		"\2\2\u00dd\u00de\3\2\2\2\u00de\u00e2\3\2\2\2\u00df\u00e1\5\4\3\2\u00e0"+
		"\u00df\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2"+
		"\2\2\u00e3\u012d\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5\u00ee\7!\2\2\u00e6"+
		"\u00eb\5\30\r\2\u00e7\u00e8\7\25\2\2\u00e8\u00ea\5\30\r\2\u00e9\u00e7"+
		"\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec"+
		"\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00e6\3\2\2\2\u00ee\u00ef\3\2"+
		"\2\2\u00ef\u00f3\3\2\2\2\u00f0\u00f2\5\4\3\2\u00f1\u00f0\3\2\2\2\u00f2"+
		"\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u012d\3\2"+
		"\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f7\7\30\2\2\u00f7\u00f8\7\32\2\2\u00f8"+
		"\u00f9\5\30\r\2\u00f9\u00fa\7\17\2\2\u00fa\u00fe\5\20\t\2\u00fb\u00fd"+
		"\5\4\3\2\u00fc\u00fb\3\2\2\2\u00fd\u0100\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00ff\u012d\3\2\2\2\u0100\u00fe\3\2\2\2\u0101\u0102\5\b"+
		"\5\2\u0102\u0103\7D\2\2\u0103\u0105\7\32\2\2\u0104\u0106\5\n\6\2\u0105"+
		"\u0104\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u010b\3\2\2\2\u0107\u0108\7\25"+
		"\2\2\u0108\u010a\5\n\6\2\u0109\u0107\3\2\2\2\u010a\u010d\3\2\2\2\u010b"+
		"\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010e\3\2\2\2\u010d\u010b\3\2"+
		"\2\2\u010e\u010f\7\17\2\2\u010f\u0113\5\20\t\2\u0110\u0112\5\4\3\2\u0111"+
		"\u0110\3\2\2\2\u0112\u0115\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2"+
		"\2\2\u0114\u012d\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u011a\5\n\6\2\u0117"+
		"\u0119\5\4\3\2\u0118\u0117\3\2\2\2\u0119\u011c\3\2\2\2\u011a\u0118\3\2"+
		"\2\2\u011a\u011b\3\2\2\2\u011b\u012d\3\2\2\2\u011c\u011a\3\2\2\2\u011d"+
		"\u0121\5\30\r\2\u011e\u0120\5\4\3\2\u011f\u011e\3\2\2\2\u0120\u0123\3"+
		"\2\2\2\u0121\u011f\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u012d\3\2\2\2\u0123"+
		"\u0121\3\2\2\2\u0124\u0128\5\16\b\2\u0125\u0127\5\4\3\2\u0126\u0125\3"+
		"\2\2\2\u0127\u012a\3\2\2\2\u0128\u0126\3\2\2\2\u0128\u0129\3\2\2\2\u0129"+
		"\u012d\3\2\2\2\u012a\u0128\3\2\2\2\u012b\u012d\5\4\3\2\u012c^\3\2\2\2"+
		"\u012cf\3\2\2\2\u012cm\3\2\2\2\u012cw\3\2\2\2\u012c~\3\2\2\2\u012c\u0088"+
		"\3\2\2\2\u012c\u0092\3\2\2\2\u012c\u009c\3\2\2\2\u012c\u00b1\3\2\2\2\u012c"+
		"\u00be\3\2\2\2\u012c\u00d3\3\2\2\2\u012c\u00db\3\2\2\2\u012c\u00e5\3\2"+
		"\2\2\u012c\u00f6\3\2\2\2\u012c\u0101\3\2\2\2\u012c\u0116\3\2\2\2\u012c"+
		"\u011d\3\2\2\2\u012c\u0124\3\2\2\2\u012c\u012b\3\2\2\2\u012d\21\3\2\2"+
		"\2\u012e\u0131\5\n\6\2\u012f\u0131\5\32\16\2\u0130\u012e\3\2\2\2\u0130"+
		"\u012f\3\2\2\2\u0131\23\3\2\2\2\u0132\u0133\5\30\r\2\u0133\25\3\2\2\2"+
		"\u0134\u0135\5\32\16\2\u0135\27\3\2\2\2\u0136\u0137\b\r\1\2\u0137\u0138"+
		"\t\3\2\2\u0138\u0185\5\30\r\2\u0139\u013a\7\67\2\2\u013a\u0185\5\30\r"+
		"\2\u013b\u013c\7\22\2\2\u013c\u0185\5\30\r\2\u013d\u013e\7\27\2\2\u013e"+
		"\u0185\5\30\r\2\u013f\u0140\7+\2\2\u0140\u0185\5\30\r\2\u0141\u0185\7"+
		":\2\2\u0142\u0185\7;\2\2\u0143\u0185\7<\2\2\u0144\u0185\7=\2\2\u0145\u0185"+
		"\7>\2\2\u0146\u0147\7D\2\2\u0147\u0150\7\32\2\2\u0148\u014d\5\30\r\2\u0149"+
		"\u014a\7\25\2\2\u014a\u014c\5\30\r\2\u014b\u0149\3\2\2\2\u014c\u014f\3"+
		"\2\2\2\u014d\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u0151\3\2\2\2\u014f"+
		"\u014d\3\2\2\2\u0150\u0148\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0152\3\2"+
		"\2\2\u0152\u0185\7\17\2\2\u0153\u0185\7D\2\2\u0154\u0155\7\32\2\2\u0155"+
		"\u0156\5\30\r\2\u0156\u0157\7\17\2\2\u0157\u0185\3\2\2\2\u0158\u0159\7"+
		"\5\2\2\u0159\u015e\5\30\r\2\u015a\u015b\7\25\2\2\u015b\u015d\5\30\r\2"+
		"\u015c\u015a\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f"+
		"\3\2\2\2\u015f\u0161\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0162\7\24\2\2"+
		"\u0162\u0185\3\2\2\2\u0163\u0164\7 \2\2\u0164\u0165\5\30\r\2\u0165\u0166"+
		"\7*\2\2\u0166\u016e\5\30\r\2\u0167\u0168\7\25\2\2\u0168\u0169\5\30\r\2"+
		"\u0169\u016a\7*\2\2\u016a\u016b\5\30\r\2\u016b\u016d\3\2\2\2\u016c\u0167"+
		"\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f"+
		"\u0171\3\2\2\2\u0170\u016e\3\2\2\2\u0171\u0172\7\f\2\2\u0172\u0185\3\2"+
		"\2\2\u0173\u0185\7?\2\2\u0174\u0185\7@\2\2\u0175\u0181\79\2\2\u0176\u0177"+
		"\7\32\2\2\u0177\u017c\5\30\r\2\u0178\u0179\7\25\2\2\u0179\u017b\5\30\r"+
		"\2\u017a\u0178\3\2\2\2\u017b\u017e\3\2\2\2\u017c\u017a\3\2\2\2\u017c\u017d"+
		"\3\2\2\2\u017d\u017f\3\2\2\2\u017e\u017c\3\2\2\2\u017f\u0180\7\17\2\2"+
		"\u0180\u0182\3\2\2\2\u0181\u0176\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0183"+
		"\3\2\2\2\u0183\u0185\5\20\t\2\u0184\u0136\3\2\2\2\u0184\u0139\3\2\2\2"+
		"\u0184\u013b\3\2\2\2\u0184\u013d\3\2\2\2\u0184\u013f\3\2\2\2\u0184\u0141"+
		"\3\2\2\2\u0184\u0142\3\2\2\2\u0184\u0143\3\2\2\2\u0184\u0144\3\2\2\2\u0184"+
		"\u0145\3\2\2\2\u0184\u0146\3\2\2\2\u0184\u0153\3\2\2\2\u0184\u0154\3\2"+
		"\2\2\u0184\u0158\3\2\2\2\u0184\u0163\3\2\2\2\u0184\u0173\3\2\2\2\u0184"+
		"\u0174\3\2\2\2\u0184\u0175\3\2\2\2\u0185\u01df\3\2\2\2\u0186\u0187\6\r"+
		"\5\3\u0187\u0188\7\60\2\2\u0188\u01de\5\30\r\2\u0189\u018a\6\r\6\3\u018a"+
		"\u018b\7\62\2\2\u018b\u01de\5\30\r\2\u018c\u018d\6\r\7\3\u018d\u018e\7"+
		"\3\2\2\u018e\u01de\5\30\r\2\u018f\u0190\6\r\b\3\u0190\u0191\7(\2\2\u0191"+
		"\u01de\5\30\r\2\u0192\u0193\6\r\t\3\u0193\u0194\7\21\2\2\u0194\u01de\5"+
		"\30\r\2\u0195\u0196\6\r\n\3\u0196\u0197\7\t\2\2\u0197\u01de\5\30\r\2\u0198"+
		"\u0199\6\r\13\3\u0199\u019a\7\66\2\2\u019a\u01de\5\30\r\2\u019b\u019c"+
		"\6\r\f\3\u019c\u019d\7\r\2\2\u019d\u01de\5\30\r\2\u019e\u019f\6\r\r\3"+
		"\u019f\u01a0\7\65\2\2\u01a0\u01de\5\30\r\2\u01a1\u01a2\6\r\16\3\u01a2"+
		"\u01a3\7\4\2\2\u01a3\u01de\5\30\r\2\u01a4\u01a5\6\r\17\3\u01a5\u01a6\7"+
		"\27\2\2\u01a6\u01de\5\30\r\2\u01a7\u01a8\6\r\20\3\u01a8\u01a9\7+\2\2\u01a9"+
		"\u01de\5\30\r\2\u01aa\u01ab\6\r\21\3\u01ab\u01ac\7\6\2\2\u01ac\u01de\5"+
		"\30\r\2\u01ad\u01ae\6\r\22\3\u01ae\u01af\7\63\2\2\u01af\u01de\5\30\r\2"+
		"\u01b0\u01b1\6\r\23\3\u01b1\u01b2\7\n\2\2\u01b2\u01de\5\30\r\2\u01b3\u01b4"+
		"\6\r\24\3\u01b4\u01b5\78\2\2\u01b5\u01de\5\30\r\2\u01b6\u01b7\6\r\25\3"+
		"\u01b7\u01b8\7$\2\2\u01b8\u01de\5\30\r\2\u01b9\u01ba\6\r\26\3\u01ba\u01bb"+
		"\7\20\2\2\u01bb\u01de\5\30\r\2\u01bc\u01bd\6\r\27\3\u01bd\u01be\7\36\2"+
		"\2\u01be\u01bf\5\30\r\2\u01bf\u01c0\7\33\2\2\u01c0\u01c1\5\30\r\2\u01c1"+
		"\u01de\3\2\2\2\u01c2\u01c3\6\r\30\3\u01c3\u01c4\7)\2\2\u01c4\u01c5\7D"+
		"\2\2\u01c5\u01ce\7\32\2\2\u01c6\u01cb\5\30\r\2\u01c7\u01c8\7\25\2\2\u01c8"+
		"\u01ca\5\30\r\2\u01c9\u01c7\3\2\2\2\u01ca\u01cd\3\2\2\2\u01cb\u01c9\3"+
		"\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01ce"+
		"\u01c6\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01de\7\17"+
		"\2\2\u01d1\u01d2\6\r\31\3\u01d2\u01de\t\3\2\2\u01d3\u01d4\6\r\32\3\u01d4"+
		"\u01d5\7\5\2\2\u01d5\u01d6\5\30\r\2\u01d6\u01d7\7\24\2\2\u01d7\u01de\3"+
		"\2\2\2\u01d8\u01d9\6\r\33\3\u01d9\u01da\7 \2\2\u01da\u01db\5\30\r\2\u01db"+
		"\u01dc\7\f\2\2\u01dc\u01de\3\2\2\2\u01dd\u0186\3\2\2\2\u01dd\u0189\3\2"+
		"\2\2\u01dd\u018c\3\2\2\2\u01dd\u018f\3\2\2\2\u01dd\u0192\3\2\2\2\u01dd"+
		"\u0195\3\2\2\2\u01dd\u0198\3\2\2\2\u01dd\u019b\3\2\2\2\u01dd\u019e\3\2"+
		"\2\2\u01dd\u01a1\3\2\2\2\u01dd\u01a4\3\2\2\2\u01dd\u01a7\3\2\2\2\u01dd"+
		"\u01aa\3\2\2\2\u01dd\u01ad\3\2\2\2\u01dd\u01b0\3\2\2\2\u01dd\u01b3\3\2"+
		"\2\2\u01dd\u01b6\3\2\2\2\u01dd\u01b9\3\2\2\2\u01dd\u01bc\3\2\2\2\u01dd"+
		"\u01c2\3\2\2\2\u01dd\u01d1\3\2\2\2\u01dd\u01d3\3\2\2\2\u01dd\u01d8\3\2"+
		"\2\2\u01de\u01e1\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0"+
		"\31\3\2\2\2\u01e1\u01df\3\2\2\2\u01e2\u01e7\5\30\r\2\u01e3\u01e4\7\25"+
		"\2\2\u01e4\u01e6\5\30\r\2\u01e5\u01e3\3\2\2\2\u01e6\u01e9\3\2\2\2\u01e7"+
		"\u01e5\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\33\3\2\2\2\u01e9\u01e7\3\2\2"+
		"\29\37%*\61:GIRXbjot{\u0080\u0085\u008a\u008f\u0094\u0099\u009f\u00a3"+
		"\u00a7\u00ae\u00bb\u00c6\u00ce\u00d1\u00d8\u00dd\u00e2\u00eb\u00ee\u00f3"+
		"\u00fe\u0105\u010b\u0113\u011a\u0121\u0128\u012c\u0130\u014d\u0150\u015e"+
		"\u016e\u017c\u0181\u0184\u01cb\u01ce\u01dd\u01df\u01e7";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}