// Generated from BigDataScript.g4 by ANTLR 4.2.2
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BigDataScriptLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__69=1, T__68=2, T__67=3, T__66=4, T__65=5, T__64=6, T__63=7, T__62=8, 
		T__61=9, T__60=10, T__59=11, T__58=12, T__57=13, T__56=14, T__55=15, T__54=16, 
		T__53=17, T__52=18, T__51=19, T__50=20, T__49=21, T__48=22, T__47=23, 
		T__46=24, T__45=25, T__44=26, T__43=27, T__42=28, T__41=29, T__40=30, 
		T__39=31, T__38=32, T__37=33, T__36=34, T__35=35, T__34=36, T__33=37, 
		T__32=38, T__31=39, T__30=40, T__29=41, T__28=42, T__27=43, T__26=44, 
		T__25=45, T__24=46, T__23=47, T__22=48, T__21=49, T__20=50, T__19=51, 
		T__18=52, T__17=53, T__16=54, T__15=55, T__14=56, T__13=57, T__12=58, 
		T__11=59, T__10=60, T__9=61, T__8=62, T__7=63, T__6=64, T__5=65, T__4=66, 
		T__3=67, T__2=68, T__1=69, T__0=70, BOOL_LITERAL=71, INT_LITERAL=72, REAL_LITERAL=73, 
		STRING_LITERAL=74, STRING_LITERAL_SINGLE=75, HELP_LITERAL=76, SYS_LITERAL=77, 
		TASK_LITERAL=78, COMMENT=79, COMMENT_LINE=80, COMMENT_LINE_HASH=81, ID=82, 
		WS=83;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'+='", "'!='", "'while'", "'{'", "'void'", "'&&'", "'='", "'^'", "'for'", 
		"'error'", "'debug'", "'|='", "'int'", "'include'", "'task'", "'('", "'-='", 
		"','", "'/='", "'kill'", "'<-'", "'\n'", "'println'", "'exit'", "'>='", 
		"'<'", "'++'", "']'", "'~'", "'wait'", "'dep'", "'+'", "'goal'", "'*='", 
		"'/'", "'continue'", "'&='", "'return'", "'||'", "';'", "'}'", "'if'", 
		"'?'", "'warning'", "':='", "'<='", "'break'", "'&'", "'print'", "'*'", 
		"'.'", "'parallel'", "'par'", "':'", "'['", "'=='", "'--'", "'|'", "'>'", 
		"'bool'", "'=>'", "'!'", "'string'", "'checkpoint'", "'%'", "'else'", 
		"'breakpoint'", "')'", "'-'", "'real'", "BOOL_LITERAL", "INT_LITERAL", 
		"REAL_LITERAL", "STRING_LITERAL", "STRING_LITERAL_SINGLE", "HELP_LITERAL", 
		"SYS_LITERAL", "TASK_LITERAL", "COMMENT", "COMMENT_LINE", "COMMENT_LINE_HASH", 
		"ID", "WS"
	};
	public static final String[] ruleNames = {
		"T__69", "T__68", "T__67", "T__66", "T__65", "T__64", "T__63", "T__62", 
		"T__61", "T__60", "T__59", "T__58", "T__57", "T__56", "T__55", "T__54", 
		"T__53", "T__52", "T__51", "T__50", "T__49", "T__48", "T__47", "T__46", 
		"T__45", "T__44", "T__43", "T__42", "T__41", "T__40", "T__39", "T__38", 
		"T__37", "T__36", "T__35", "T__34", "T__33", "T__32", "T__31", "T__30", 
		"T__29", "T__28", "T__27", "T__26", "T__25", "T__24", "T__23", "T__22", 
		"T__21", "T__20", "T__19", "T__18", "T__17", "T__16", "T__15", "T__14", 
		"T__13", "T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", 
		"T__4", "T__3", "T__2", "T__1", "T__0", "IntegerNumber", "EscapeSequence", 
		"EscapedNewLine", "Exponent", "HexPrefix", "HexDigit", "NonIntegerNumber", 
		"SysMultiLine", "BOOL_LITERAL", "INT_LITERAL", "REAL_LITERAL", "STRING_LITERAL", 
		"STRING_LITERAL_SINGLE", "HELP_LITERAL", "SYS_LITERAL", "TASK_LITERAL", 
		"COMMENT", "COMMENT_LINE", "COMMENT_LINE_HASH", "ID", "WS"
	};


	public BigDataScriptLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BigDataScript.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 86: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 87: COMMENT_LINE_action((RuleContext)_localctx, actionIndex); break;

		case 88: COMMENT_LINE_HASH_action((RuleContext)_localctx, actionIndex); break;

		case 90: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void COMMENT_LINE_HASH_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:  skip();  break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:  skip();  break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:  skip();  break;
		}
	}
	private void COMMENT_LINE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:  skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2U\u02a8\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23"+
		"\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32"+
		"\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3$\3$\3%\3%"+
		"\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3"+
		")\3)\3*\3*\3+\3+\3+\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3/\3/\3/\3"+
		"\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3"+
		"\63\3\63\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\66\3"+
		"\66\3\66\3\66\3\67\3\67\38\38\39\39\39\3:\3:\3:\3;\3;\3<\3<\3=\3=\3=\3"+
		"=\3=\3>\3>\3>\3?\3?\3@\3@\3@\3@\3@\3@\3@\3A\3A\3A\3A\3A\3A\3A\3A\3A\3"+
		"A\3A\3B\3B\3C\3C\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3E\3E\3F\3"+
		"F\3G\3G\3G\3G\3G\3H\3H\3H\7H\u01ce\nH\fH\16H\u01d1\13H\3H\3H\6H\u01d5"+
		"\nH\rH\16H\u01d6\3H\3H\6H\u01db\nH\rH\16H\u01dc\5H\u01df\nH\3I\3I\3I\3"+
		"I\3I\3I\3I\3I\5I\u01e9\nI\3J\3J\3J\3J\5J\u01ef\nJ\3K\3K\5K\u01f3\nK\3"+
		"K\6K\u01f6\nK\rK\16K\u01f7\3L\3L\3L\3L\5L\u01fe\nL\3M\3M\3N\6N\u0203\n"+
		"N\rN\16N\u0204\3N\3N\7N\u0209\nN\fN\16N\u020c\13N\3N\5N\u020f\nN\3N\3"+
		"N\6N\u0213\nN\rN\16N\u0214\3N\5N\u0218\nN\3N\6N\u021b\nN\rN\16N\u021c"+
		"\3N\3N\6N\u0221\nN\rN\16N\u0222\5N\u0225\nN\3O\3O\7O\u0229\nO\fO\16O\u022c"+
		"\13O\3P\3P\3P\3P\3P\3P\3P\3P\3P\5P\u0237\nP\3Q\3Q\3R\3R\3S\3S\3S\3S\7"+
		"S\u0241\nS\fS\16S\u0244\13S\3S\3S\3T\3T\7T\u024a\nT\fT\16T\u024d\13T\3"+
		"T\3T\3U\3U\3U\3U\3U\3U\6U\u0257\nU\rU\16U\u0258\3U\3U\3V\3V\3V\3V\3V\6"+
		"V\u0262\nV\rV\16V\u0263\3V\3V\3W\3W\3W\3W\3W\3W\6W\u026e\nW\rW\16W\u026f"+
		"\3W\3W\3W\3X\3X\3X\3X\7X\u0279\nX\fX\16X\u027c\13X\3X\3X\3X\3X\3X\3Y\3"+
		"Y\3Y\3Y\7Y\u0287\nY\fY\16Y\u028a\13Y\3Y\3Y\3Z\3Z\7Z\u0290\nZ\fZ\16Z\u0293"+
		"\13Z\3Z\3Z\3[\3[\7[\u0299\n[\f[\16[\u029c\13[\3\\\3\\\3\\\3\\\3\\\3\\"+
		"\3\\\5\\\u02a5\n\\\3\\\3\\\3\u027a\2]\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083"+
		"C\u0085D\u0087E\u0089F\u008bG\u008dH\u008f\2\u0091\2\u0093\2\u0095\2\u0097"+
		"\2\u0099\2\u009b\2\u009d\2\u009fI\u00a1J\u00a3K\u00a5L\u00a7M\u00a9N\u00ab"+
		"O\u00adP\u00afQ\u00b1R\u00b3S\u00b5T\u00b7U\3\2\16\n\2$$))^^ddhhppttv"+
		"v\4\2\f\f\17\17\4\2GGgg\4\2--//\5\2\62;CHch\4\2$$^^\3\2))\4\2\13\13\""+
		"\"\6\2\f\f\17\17**}}\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\13\17\17\"\"\u02c6"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3"+
		"\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2"+
		"\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2"+
		"{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7"+
		"\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2"+
		"\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\3\u00b9"+
		"\3\2\2\2\5\u00bc\3\2\2\2\7\u00bf\3\2\2\2\t\u00c5\3\2\2\2\13\u00c7\3\2"+
		"\2\2\r\u00cc\3\2\2\2\17\u00cf\3\2\2\2\21\u00d1\3\2\2\2\23\u00d3\3\2\2"+
		"\2\25\u00d7\3\2\2\2\27\u00dd\3\2\2\2\31\u00e3\3\2\2\2\33\u00e6\3\2\2\2"+
		"\35\u00ea\3\2\2\2\37\u00f2\3\2\2\2!\u00f7\3\2\2\2#\u00f9\3\2\2\2%\u00fc"+
		"\3\2\2\2\'\u00fe\3\2\2\2)\u0101\3\2\2\2+\u0106\3\2\2\2-\u0109\3\2\2\2"+
		"/\u010b\3\2\2\2\61\u0113\3\2\2\2\63\u0118\3\2\2\2\65\u011b\3\2\2\2\67"+
		"\u011d\3\2\2\29\u0120\3\2\2\2;\u0122\3\2\2\2=\u0124\3\2\2\2?\u0129\3\2"+
		"\2\2A\u012d\3\2\2\2C\u012f\3\2\2\2E\u0134\3\2\2\2G\u0137\3\2\2\2I\u0139"+
		"\3\2\2\2K\u0142\3\2\2\2M\u0145\3\2\2\2O\u014c\3\2\2\2Q\u014f\3\2\2\2S"+
		"\u0151\3\2\2\2U\u0153\3\2\2\2W\u0156\3\2\2\2Y\u0158\3\2\2\2[\u0160\3\2"+
		"\2\2]\u0163\3\2\2\2_\u0166\3\2\2\2a\u016c\3\2\2\2c\u016e\3\2\2\2e\u0174"+
		"\3\2\2\2g\u0176\3\2\2\2i\u0178\3\2\2\2k\u0181\3\2\2\2m\u0185\3\2\2\2o"+
		"\u0187\3\2\2\2q\u0189\3\2\2\2s\u018c\3\2\2\2u\u018f\3\2\2\2w\u0191\3\2"+
		"\2\2y\u0193\3\2\2\2{\u0198\3\2\2\2}\u019b\3\2\2\2\177\u019d\3\2\2\2\u0081"+
		"\u01a4\3\2\2\2\u0083\u01af\3\2\2\2\u0085\u01b1\3\2\2\2\u0087\u01b6\3\2"+
		"\2\2\u0089\u01c1\3\2\2\2\u008b\u01c3\3\2\2\2\u008d\u01c5\3\2\2\2\u008f"+
		"\u01de\3\2\2\2\u0091\u01e0\3\2\2\2\u0093\u01ea\3\2\2\2\u0095\u01f0\3\2"+
		"\2\2\u0097\u01fd\3\2\2\2\u0099\u01ff\3\2\2\2\u009b\u0224\3\2\2\2\u009d"+
		"\u022a\3\2\2\2\u009f\u0236\3\2\2\2\u00a1\u0238\3\2\2\2\u00a3\u023a\3\2"+
		"\2\2\u00a5\u023c\3\2\2\2\u00a7\u0247\3\2\2\2\u00a9\u0250\3\2\2\2\u00ab"+
		"\u025c\3\2\2\2\u00ad\u0267\3\2\2\2\u00af\u0274\3\2\2\2\u00b1\u0282\3\2"+
		"\2\2\u00b3\u028d\3\2\2\2\u00b5\u0296\3\2\2\2\u00b7\u02a4\3\2\2\2\u00b9"+
		"\u00ba\7-\2\2\u00ba\u00bb\7?\2\2\u00bb\4\3\2\2\2\u00bc\u00bd\7#\2\2\u00bd"+
		"\u00be\7?\2\2\u00be\6\3\2\2\2\u00bf\u00c0\7y\2\2\u00c0\u00c1\7j\2\2\u00c1"+
		"\u00c2\7k\2\2\u00c2\u00c3\7n\2\2\u00c3\u00c4\7g\2\2\u00c4\b\3\2\2\2\u00c5"+
		"\u00c6\7}\2\2\u00c6\n\3\2\2\2\u00c7\u00c8\7x\2\2\u00c8\u00c9\7q\2\2\u00c9"+
		"\u00ca\7k\2\2\u00ca\u00cb\7f\2\2\u00cb\f\3\2\2\2\u00cc\u00cd\7(\2\2\u00cd"+
		"\u00ce\7(\2\2\u00ce\16\3\2\2\2\u00cf\u00d0\7?\2\2\u00d0\20\3\2\2\2\u00d1"+
		"\u00d2\7`\2\2\u00d2\22\3\2\2\2\u00d3\u00d4\7h\2\2\u00d4\u00d5\7q\2\2\u00d5"+
		"\u00d6\7t\2\2\u00d6\24\3\2\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7t\2\2\u00d9"+
		"\u00da\7t\2\2\u00da\u00db\7q\2\2\u00db\u00dc\7t\2\2\u00dc\26\3\2\2\2\u00dd"+
		"\u00de\7f\2\2\u00de\u00df\7g\2\2\u00df\u00e0\7d\2\2\u00e0\u00e1\7w\2\2"+
		"\u00e1\u00e2\7i\2\2\u00e2\30\3\2\2\2\u00e3\u00e4\7~\2\2\u00e4\u00e5\7"+
		"?\2\2\u00e5\32\3\2\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8\7p\2\2\u00e8\u00e9"+
		"\7v\2\2\u00e9\34\3\2\2\2\u00ea\u00eb\7k\2\2\u00eb\u00ec\7p\2\2\u00ec\u00ed"+
		"\7e\2\2\u00ed\u00ee\7n\2\2\u00ee\u00ef\7w\2\2\u00ef\u00f0\7f\2\2\u00f0"+
		"\u00f1\7g\2\2\u00f1\36\3\2\2\2\u00f2\u00f3\7v\2\2\u00f3\u00f4\7c\2\2\u00f4"+
		"\u00f5\7u\2\2\u00f5\u00f6\7m\2\2\u00f6 \3\2\2\2\u00f7\u00f8\7*\2\2\u00f8"+
		"\"\3\2\2\2\u00f9\u00fa\7/\2\2\u00fa\u00fb\7?\2\2\u00fb$\3\2\2\2\u00fc"+
		"\u00fd\7.\2\2\u00fd&\3\2\2\2\u00fe\u00ff\7\61\2\2\u00ff\u0100\7?\2\2\u0100"+
		"(\3\2\2\2\u0101\u0102\7m\2\2\u0102\u0103\7k\2\2\u0103\u0104\7n\2\2\u0104"+
		"\u0105\7n\2\2\u0105*\3\2\2\2\u0106\u0107\7>\2\2\u0107\u0108\7/\2\2\u0108"+
		",\3\2\2\2\u0109\u010a\7\f\2\2\u010a.\3\2\2\2\u010b\u010c\7r\2\2\u010c"+
		"\u010d\7t\2\2\u010d\u010e\7k\2\2\u010e\u010f\7p\2\2\u010f\u0110\7v\2\2"+
		"\u0110\u0111\7n\2\2\u0111\u0112\7p\2\2\u0112\60\3\2\2\2\u0113\u0114\7"+
		"g\2\2\u0114\u0115\7z\2\2\u0115\u0116\7k\2\2\u0116\u0117\7v\2\2\u0117\62"+
		"\3\2\2\2\u0118\u0119\7@\2\2\u0119\u011a\7?\2\2\u011a\64\3\2\2\2\u011b"+
		"\u011c\7>\2\2\u011c\66\3\2\2\2\u011d\u011e\7-\2\2\u011e\u011f\7-\2\2\u011f"+
		"8\3\2\2\2\u0120\u0121\7_\2\2\u0121:\3\2\2\2\u0122\u0123\7\u0080\2\2\u0123"+
		"<\3\2\2\2\u0124\u0125\7y\2\2\u0125\u0126\7c\2\2\u0126\u0127\7k\2\2\u0127"+
		"\u0128\7v\2\2\u0128>\3\2\2\2\u0129\u012a\7f\2\2\u012a\u012b\7g\2\2\u012b"+
		"\u012c\7r\2\2\u012c@\3\2\2\2\u012d\u012e\7-\2\2\u012eB\3\2\2\2\u012f\u0130"+
		"\7i\2\2\u0130\u0131\7q\2\2\u0131\u0132\7c\2\2\u0132\u0133\7n\2\2\u0133"+
		"D\3\2\2\2\u0134\u0135\7,\2\2\u0135\u0136\7?\2\2\u0136F\3\2\2\2\u0137\u0138"+
		"\7\61\2\2\u0138H\3\2\2\2\u0139\u013a\7e\2\2\u013a\u013b\7q\2\2\u013b\u013c"+
		"\7p\2\2\u013c\u013d\7v\2\2\u013d\u013e\7k\2\2\u013e\u013f\7p\2\2\u013f"+
		"\u0140\7w\2\2\u0140\u0141\7g\2\2\u0141J\3\2\2\2\u0142\u0143\7(\2\2\u0143"+
		"\u0144\7?\2\2\u0144L\3\2\2\2\u0145\u0146\7t\2\2\u0146\u0147\7g\2\2\u0147"+
		"\u0148\7v\2\2\u0148\u0149\7w\2\2\u0149\u014a\7t\2\2\u014a\u014b\7p\2\2"+
		"\u014bN\3\2\2\2\u014c\u014d\7~\2\2\u014d\u014e\7~\2\2\u014eP\3\2\2\2\u014f"+
		"\u0150\7=\2\2\u0150R\3\2\2\2\u0151\u0152\7\177\2\2\u0152T\3\2\2\2\u0153"+
		"\u0154\7k\2\2\u0154\u0155\7h\2\2\u0155V\3\2\2\2\u0156\u0157\7A\2\2\u0157"+
		"X\3\2\2\2\u0158\u0159\7y\2\2\u0159\u015a\7c\2\2\u015a\u015b\7t\2\2\u015b"+
		"\u015c\7p\2\2\u015c\u015d\7k\2\2\u015d\u015e\7p\2\2\u015e\u015f\7i\2\2"+
		"\u015fZ\3\2\2\2\u0160\u0161\7<\2\2\u0161\u0162\7?\2\2\u0162\\\3\2\2\2"+
		"\u0163\u0164\7>\2\2\u0164\u0165\7?\2\2\u0165^\3\2\2\2\u0166\u0167\7d\2"+
		"\2\u0167\u0168\7t\2\2\u0168\u0169\7g\2\2\u0169\u016a\7c\2\2\u016a\u016b"+
		"\7m\2\2\u016b`\3\2\2\2\u016c\u016d\7(\2\2\u016db\3\2\2\2\u016e\u016f\7"+
		"r\2\2\u016f\u0170\7t\2\2\u0170\u0171\7k\2\2\u0171\u0172\7p\2\2\u0172\u0173"+
		"\7v\2\2\u0173d\3\2\2\2\u0174\u0175\7,\2\2\u0175f\3\2\2\2\u0176\u0177\7"+
		"\60\2\2\u0177h\3\2\2\2\u0178\u0179\7r\2\2\u0179\u017a\7c\2\2\u017a\u017b"+
		"\7t\2\2\u017b\u017c\7c\2\2\u017c\u017d\7n\2\2\u017d\u017e\7n\2\2\u017e"+
		"\u017f\7g\2\2\u017f\u0180\7n\2\2\u0180j\3\2\2\2\u0181\u0182\7r\2\2\u0182"+
		"\u0183\7c\2\2\u0183\u0184\7t\2\2\u0184l\3\2\2\2\u0185\u0186\7<\2\2\u0186"+
		"n\3\2\2\2\u0187\u0188\7]\2\2\u0188p\3\2\2\2\u0189\u018a\7?\2\2\u018a\u018b"+
		"\7?\2\2\u018br\3\2\2\2\u018c\u018d\7/\2\2\u018d\u018e\7/\2\2\u018et\3"+
		"\2\2\2\u018f\u0190\7~\2\2\u0190v\3\2\2\2\u0191\u0192\7@\2\2\u0192x\3\2"+
		"\2\2\u0193\u0194\7d\2\2\u0194\u0195\7q\2\2\u0195\u0196\7q\2\2\u0196\u0197"+
		"\7n\2\2\u0197z\3\2\2\2\u0198\u0199\7?\2\2\u0199\u019a\7@\2\2\u019a|\3"+
		"\2\2\2\u019b\u019c\7#\2\2\u019c~\3\2\2\2\u019d\u019e\7u\2\2\u019e\u019f"+
		"\7v\2\2\u019f\u01a0\7t\2\2\u01a0\u01a1\7k\2\2\u01a1\u01a2\7p\2\2\u01a2"+
		"\u01a3\7i\2\2\u01a3\u0080\3\2\2\2\u01a4\u01a5\7e\2\2\u01a5\u01a6\7j\2"+
		"\2\u01a6\u01a7\7g\2\2\u01a7\u01a8\7e\2\2\u01a8\u01a9\7m\2\2\u01a9\u01aa"+
		"\7r\2\2\u01aa\u01ab\7q\2\2\u01ab\u01ac\7k\2\2\u01ac\u01ad\7p\2\2\u01ad"+
		"\u01ae\7v\2\2\u01ae\u0082\3\2\2\2\u01af\u01b0\7\'\2\2\u01b0\u0084\3\2"+
		"\2\2\u01b1\u01b2\7g\2\2\u01b2\u01b3\7n\2\2\u01b3\u01b4\7u\2\2\u01b4\u01b5"+
		"\7g\2\2\u01b5\u0086\3\2\2\2\u01b6\u01b7\7d\2\2\u01b7\u01b8\7t\2\2\u01b8"+
		"\u01b9\7g\2\2\u01b9\u01ba\7c\2\2\u01ba\u01bb\7m\2\2\u01bb\u01bc\7r\2\2"+
		"\u01bc\u01bd\7q\2\2\u01bd\u01be\7k\2\2\u01be\u01bf\7p\2\2\u01bf\u01c0"+
		"\7v\2\2\u01c0\u0088\3\2\2\2\u01c1\u01c2\7+\2\2\u01c2\u008a\3\2\2\2\u01c3"+
		"\u01c4\7/\2\2\u01c4\u008c\3\2\2\2\u01c5\u01c6\7t\2\2\u01c6\u01c7\7g\2"+
		"\2\u01c7\u01c8\7c\2\2\u01c8\u01c9\7n\2\2\u01c9\u008e\3\2\2\2\u01ca\u01df"+
		"\7\62\2\2\u01cb\u01cf\4\63;\2\u01cc\u01ce\4\62;\2\u01cd\u01cc\3\2\2\2"+
		"\u01ce\u01d1\3\2\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01df"+
		"\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d2\u01d4\7\62\2\2\u01d3\u01d5\4\629\2"+
		"\u01d4\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d4\3\2\2\2\u01d6\u01d7"+
		"\3\2\2\2\u01d7\u01df\3\2\2\2\u01d8\u01da\5\u0097L\2\u01d9\u01db\5\u0099"+
		"M\2\u01da\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01da\3\2\2\2\u01dc"+
		"\u01dd\3\2\2\2\u01dd\u01df\3\2\2\2\u01de\u01ca\3\2\2\2\u01de\u01cb\3\2"+
		"\2\2\u01de\u01d2\3\2\2\2\u01de\u01d8\3\2\2\2\u01df\u0090\3\2\2\2\u01e0"+
		"\u01e8\7^\2\2\u01e1\u01e9\t\2\2\2\u01e2\u01e3\4\62\65\2\u01e3\u01e4\4"+
		"\629\2\u01e4\u01e9\4\629\2\u01e5\u01e6\4\629\2\u01e6\u01e9\4\629\2\u01e7"+
		"\u01e9\4\629\2\u01e8\u01e1\3\2\2\2\u01e8\u01e2\3\2\2\2\u01e8\u01e5\3\2"+
		"\2\2\u01e8\u01e7\3\2\2\2\u01e9\u0092\3\2\2\2\u01ea\u01ee\7^\2\2\u01eb"+
		"\u01ef\t\3\2\2\u01ec\u01ed\7\17\2\2\u01ed\u01ef\7\f\2\2\u01ee\u01eb\3"+
		"\2\2\2\u01ee\u01ec\3\2\2\2\u01ef\u0094\3\2\2\2\u01f0\u01f2\t\4\2\2\u01f1"+
		"\u01f3\t\5\2\2\u01f2\u01f1\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f5\3\2"+
		"\2\2\u01f4\u01f6\4\62;\2\u01f5\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7"+
		"\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u0096\3\2\2\2\u01f9\u01fa\7\62"+
		"\2\2\u01fa\u01fe\7z\2\2\u01fb\u01fc\7\62\2\2\u01fc\u01fe\7Z\2\2\u01fd"+
		"\u01f9\3\2\2\2\u01fd\u01fb\3\2\2\2\u01fe\u0098\3\2\2\2\u01ff\u0200\t\6"+
		"\2\2\u0200\u009a\3\2\2\2\u0201\u0203\4\62;\2\u0202\u0201\3\2\2\2\u0203"+
		"\u0204\3\2\2\2\u0204\u0202\3\2\2\2\u0204\u0205\3\2\2\2\u0205\u0206\3\2"+
		"\2\2\u0206\u020a\7\60\2\2\u0207\u0209\4\62;\2\u0208\u0207\3\2\2\2\u0209"+
		"\u020c\3\2\2\2\u020a\u0208\3\2\2\2\u020a\u020b\3\2\2\2\u020b\u020e\3\2"+
		"\2\2\u020c\u020a\3\2\2\2\u020d\u020f\5\u0095K\2\u020e\u020d\3\2\2\2\u020e"+
		"\u020f\3\2\2\2\u020f\u0225\3\2\2\2\u0210\u0212\7\60\2\2\u0211\u0213\4"+
		"\62;\2\u0212\u0211\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0212\3\2\2\2\u0214"+
		"\u0215\3\2\2\2\u0215\u0217\3\2\2\2\u0216\u0218\5\u0095K\2\u0217\u0216"+
		"\3\2\2\2\u0217\u0218\3\2\2\2\u0218\u0225\3\2\2\2\u0219\u021b\4\62;\2\u021a"+
		"\u0219\3\2\2\2\u021b\u021c\3\2\2\2\u021c\u021a\3\2\2\2\u021c\u021d\3\2"+
		"\2\2\u021d\u021e\3\2\2\2\u021e\u0225\5\u0095K\2\u021f\u0221\4\62;\2\u0220"+
		"\u021f\3\2\2\2\u0221\u0222\3\2\2\2\u0222\u0220\3\2\2\2\u0222\u0223\3\2"+
		"\2\2\u0223\u0225\3\2\2\2\u0224\u0202\3\2\2\2\u0224\u0210\3\2\2\2\u0224"+
		"\u021a\3\2\2\2\u0224\u0220\3\2\2\2\u0225\u009c\3\2\2\2\u0226\u0229\5\u0093"+
		"J\2\u0227\u0229\n\3\2\2\u0228\u0226\3\2\2\2\u0228\u0227\3\2\2\2\u0229"+
		"\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u009e\3\2"+
		"\2\2\u022c\u022a\3\2\2\2\u022d\u022e\7v\2\2\u022e\u022f\7t\2\2\u022f\u0230"+
		"\7w\2\2\u0230\u0237\7g\2\2\u0231\u0232\7h\2\2\u0232\u0233\7c\2\2\u0233"+
		"\u0234\7n\2\2\u0234\u0235\7u\2\2\u0235\u0237\7g\2\2\u0236\u022d\3\2\2"+
		"\2\u0236\u0231\3\2\2\2\u0237\u00a0\3\2\2\2\u0238\u0239\5\u008fH\2\u0239"+
		"\u00a2\3\2\2\2\u023a\u023b\5\u009bN\2\u023b\u00a4\3\2\2\2\u023c\u0242"+
		"\7$\2\2\u023d\u0241\n\7\2\2\u023e\u023f\7^\2\2\u023f\u0241\13\2\2\2\u0240"+
		"\u023d\3\2\2\2\u0240\u023e\3\2\2\2\u0241\u0244\3\2\2\2\u0242\u0240\3\2"+
		"\2\2\u0242\u0243\3\2\2\2\u0243\u0245\3\2\2\2\u0244\u0242\3\2\2\2\u0245"+
		"\u0246\7$\2\2\u0246\u00a6\3\2\2\2\u0247\u024b\7)\2\2\u0248\u024a\n\b\2"+
		"\2\u0249\u0248\3\2\2\2\u024a\u024d\3\2\2\2\u024b\u0249\3\2\2\2\u024b\u024c"+
		"\3\2\2\2\u024c\u024e\3\2\2\2\u024d\u024b\3\2\2\2\u024e\u024f\7)\2\2\u024f"+
		"\u00a8\3\2\2\2\u0250\u0251\7j\2\2\u0251\u0252\7g\2\2\u0252\u0253\7n\2"+
		"\2\u0253\u0254\7r\2\2\u0254\u0256\3\2\2\2\u0255\u0257\t\t\2\2\u0256\u0255"+
		"\3\2\2\2\u0257\u0258\3\2\2\2\u0258\u0256\3\2\2\2\u0258\u0259\3\2\2\2\u0259"+
		"\u025a\3\2\2\2\u025a\u025b\5\u009dO\2\u025b\u00aa\3\2\2\2\u025c\u025d"+
		"\7u\2\2\u025d\u025e\7{\2\2\u025e\u025f\7u\2\2\u025f\u0261\3\2\2\2\u0260"+
		"\u0262\t\t\2\2\u0261\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263\u0261\3\2"+
		"\2\2\u0263\u0264\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u0266\5\u009dO\2\u0266"+
		"\u00ac\3\2\2\2\u0267\u0268\7v\2\2\u0268\u0269\7c\2\2\u0269\u026a\7u\2"+
		"\2\u026a\u026b\7m\2\2\u026b\u026d\3\2\2\2\u026c\u026e\t\t\2\2\u026d\u026c"+
		"\3\2\2\2\u026e\u026f\3\2\2\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270"+
		"\u0271\3\2\2\2\u0271\u0272\n\n\2\2\u0272\u0273\5\u009dO\2\u0273\u00ae"+
		"\3\2\2\2\u0274\u0275\7\61\2\2\u0275\u0276\7,\2\2\u0276\u027a\3\2\2\2\u0277"+
		"\u0279\13\2\2\2\u0278\u0277\3\2\2\2\u0279\u027c\3\2\2\2\u027a\u027b\3"+
		"\2\2\2\u027a\u0278\3\2\2\2\u027b\u027d\3\2\2\2\u027c\u027a\3\2\2\2\u027d"+
		"\u027e\7,\2\2\u027e\u027f\7\61\2\2\u027f\u0280\3\2\2\2\u0280\u0281\bX"+
		"\2\2\u0281\u00b0\3\2\2\2\u0282\u0283\7\61\2\2\u0283\u0284\7\61\2\2\u0284"+
		"\u0288\3\2\2\2\u0285\u0287\n\3\2\2\u0286\u0285\3\2\2\2\u0287\u028a\3\2"+
		"\2\2\u0288\u0286\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u028b\3\2\2\2\u028a"+
		"\u0288\3\2\2\2\u028b\u028c\bY\3\2\u028c\u00b2\3\2\2\2\u028d\u0291\7%\2"+
		"\2\u028e\u0290\n\3\2\2\u028f\u028e\3\2\2\2\u0290\u0293\3\2\2\2\u0291\u028f"+
		"\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0294\3\2\2\2\u0293\u0291\3\2\2\2\u0294"+
		"\u0295\bZ\4\2\u0295\u00b4\3\2\2\2\u0296\u029a\t\13\2\2\u0297\u0299\t\f"+
		"\2\2\u0298\u0297\3\2\2\2\u0299\u029c\3\2\2\2\u029a\u0298\3\2\2\2\u029a"+
		"\u029b\3\2\2\2\u029b\u00b6\3\2\2\2\u029c\u029a\3\2\2\2\u029d\u02a5\t\r"+
		"\2\2\u029e\u029f\7^\2\2\u029f\u02a5\7\f\2\2\u02a0\u02a1\7^\2\2\u02a1\u02a2"+
		"\7\17\2\2\u02a2\u02a5\7\f\2\2\u02a3\u02a5\7\16\2\2\u02a4\u029d\3\2\2\2"+
		"\u02a4\u029e\3\2\2\2\u02a4\u02a0\3\2\2\2\u02a4\u02a3\3\2\2\2\u02a5\u02a6"+
		"\3\2\2\2\u02a6\u02a7\b\\\5\2\u02a7\u00b8\3\2\2\2\"\2\u01cf\u01d6\u01dc"+
		"\u01de\u01e8\u01ee\u01f2\u01f7\u01fd\u0204\u020a\u020e\u0214\u0217\u021c"+
		"\u0222\u0224\u0228\u022a\u0236\u0240\u0242\u024b\u0258\u0263\u026f\u027a"+
		"\u0288\u0291\u029a\u02a4\6\3X\2\3Y\3\3Z\4\3\\\5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}