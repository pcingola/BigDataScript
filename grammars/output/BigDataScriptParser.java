// $ANTLR 3.x /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g 2013-01-22 18:54:35

package ca.mcgill.mcb.pcingola.bigDataScript.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class BigDataScriptParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMP", "AMPAMP", "AMPEQ", "ARGS", "ARROW", "BANG", "BANGEQ", "BAR", "BARBAR", "BAREQ", "BLOCK", "BOOL", "BREAK", "BY", "CARET", "CARETEQ", "CASE", "CHECKPOINT", "COLON", "COMMA", "COMMENT", "CONTINUE", "DEFAULT", "DO", "DOT", "ELLIPSIS", "ELSE", "ENUM", "EQ", "EQEQ", "EXIT", "EXTENDS", "EscapeSequence", "EscapedNewLine", "Exponent", "FALSE", "FOR", "FOR_CONDITION", "FOR_END", "FOR_INIT", "FOR_INIT_LIST", "FOR_LOOP", "FOR_LOOP_LIST", "FUNCTION_CALL", "FUNCTION_DECLARATION", "GE", "GT", "HexDigit", "HexPrefix", "IDENTIFIER", "IF", "IMPORT", "INT", "INT_LITERAL", "IntegerNumber", "LBRACE", "LBRACKET", "LE", "LINE_COMMENT", "LITERAL_BOOL", "LITERAL_INT", "LITERAL_LIST", "LITERAL_MAP", "LITERAL_REAL", "LITERAL_STRING", "LPAREN", "LT", "MAIN", "METHOD_CALL", "MONKEYS_AT", "NAME", "NEW", "NEWLINE", "NonIntegerNumber", "PACKAGE", "PARAMETERS", "PERCENT", "PERCENTEQ", "PLUS", "PLUSEQ", "PLUSPLUS", "POST", "PRE", "PROGRAM_UNIT", "QUES", "RBRACE", "RBRACKET", "REAL", "REAL_LITERAL", "RETURN", "RPAREN", "SEMI", "SLASH", "SLASHEQ", "STAR", "STAREQ", "STRING", "STRING_LITERAL", "SUB", "SUBEQ", "SUBSUB", "SUPER", "SWITCH", "SYS_EXPRESSION", "SYS_LITERAL", "SysMultiLine", "TASK", "TASK_EXPRESSION", "TASK_LITERAL", "TASK_OPTIONS", "THIS", "TILDE", "TRUE", "TYPE", "TYPE_LIST", "TYPE_MAP", "VAR_DECLARATION", "VAR_INIT", "VOID", "WAIT", "WHILE", "WS"
	};

	public static final int EOF=-1;
	public static final int AMP=4;
	public static final int AMPAMP=5;
	public static final int AMPEQ=6;
	public static final int ARGS=7;
	public static final int ARROW=8;
	public static final int BANG=9;
	public static final int BANGEQ=10;
	public static final int BAR=11;
	public static final int BARBAR=12;
	public static final int BAREQ=13;
	public static final int BLOCK=14;
	public static final int BOOL=15;
	public static final int BREAK=16;
	public static final int BY=17;
	public static final int CARET=18;
	public static final int CARETEQ=19;
	public static final int CASE=20;
	public static final int CHECKPOINT=21;
	public static final int COLON=22;
	public static final int COMMA=23;
	public static final int COMMENT=24;
	public static final int CONTINUE=25;
	public static final int DEFAULT=26;
	public static final int DO=27;
	public static final int DOT=28;
	public static final int ELLIPSIS=29;
	public static final int ELSE=30;
	public static final int ENUM=31;
	public static final int EQ=32;
	public static final int EQEQ=33;
	public static final int EXIT=34;
	public static final int EXTENDS=35;
	public static final int EscapeSequence=36;
	public static final int EscapedNewLine=37;
	public static final int Exponent=38;
	public static final int FALSE=39;
	public static final int FOR=40;
	public static final int FOR_CONDITION=41;
	public static final int FOR_END=42;
	public static final int FOR_INIT=43;
	public static final int FOR_INIT_LIST=44;
	public static final int FOR_LOOP=45;
	public static final int FOR_LOOP_LIST=46;
	public static final int FUNCTION_CALL=47;
	public static final int FUNCTION_DECLARATION=48;
	public static final int GE=49;
	public static final int GT=50;
	public static final int HexDigit=51;
	public static final int HexPrefix=52;
	public static final int IDENTIFIER=53;
	public static final int IF=54;
	public static final int IMPORT=55;
	public static final int INT=56;
	public static final int INT_LITERAL=57;
	public static final int IntegerNumber=58;
	public static final int LBRACE=59;
	public static final int LBRACKET=60;
	public static final int LE=61;
	public static final int LINE_COMMENT=62;
	public static final int LITERAL_BOOL=63;
	public static final int LITERAL_INT=64;
	public static final int LITERAL_LIST=65;
	public static final int LITERAL_MAP=66;
	public static final int LITERAL_REAL=67;
	public static final int LITERAL_STRING=68;
	public static final int LPAREN=69;
	public static final int LT=70;
	public static final int MAIN=71;
	public static final int METHOD_CALL=72;
	public static final int MONKEYS_AT=73;
	public static final int NAME=74;
	public static final int NEW=75;
	public static final int NEWLINE=76;
	public static final int NonIntegerNumber=77;
	public static final int PACKAGE=78;
	public static final int PARAMETERS=79;
	public static final int PERCENT=80;
	public static final int PERCENTEQ=81;
	public static final int PLUS=82;
	public static final int PLUSEQ=83;
	public static final int PLUSPLUS=84;
	public static final int POST=85;
	public static final int PRE=86;
	public static final int PROGRAM_UNIT=87;
	public static final int QUES=88;
	public static final int RBRACE=89;
	public static final int RBRACKET=90;
	public static final int REAL=91;
	public static final int REAL_LITERAL=92;
	public static final int RETURN=93;
	public static final int RPAREN=94;
	public static final int SEMI=95;
	public static final int SLASH=96;
	public static final int SLASHEQ=97;
	public static final int STAR=98;
	public static final int STAREQ=99;
	public static final int STRING=100;
	public static final int STRING_LITERAL=101;
	public static final int SUB=102;
	public static final int SUBEQ=103;
	public static final int SUBSUB=104;
	public static final int SUPER=105;
	public static final int SWITCH=106;
	public static final int SYS_EXPRESSION=107;
	public static final int SYS_LITERAL=108;
	public static final int SysMultiLine=109;
	public static final int TASK=110;
	public static final int TASK_EXPRESSION=111;
	public static final int TASK_LITERAL=112;
	public static final int TASK_OPTIONS=113;
	public static final int THIS=114;
	public static final int TILDE=115;
	public static final int TRUE=116;
	public static final int TYPE=117;
	public static final int TYPE_LIST=118;
	public static final int TYPE_MAP=119;
	public static final int VAR_DECLARATION=120;
	public static final int VAR_INIT=121;
	public static final int VOID=122;
	public static final int WAIT=123;
	public static final int WHILE=124;
	public static final int WS=125;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public BigDataScriptParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public BigDataScriptParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
	this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
	return adaptor;
}
	@Override public String[] getTokenNames() { return BigDataScriptParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g"; }


	public static class main_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "main"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:1: main : (m+= mainElement )* EOF -> ^( PROGRAM_UNIT ( $m)* ) ;
	public final BigDataScriptParser.main_return main() throws RecognitionException {
		BigDataScriptParser.main_return retval = new BigDataScriptParser.main_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF1=null;
		List<Object> list_m=null;
		RuleReturnScope m = null;
		Object EOF1_tree=null;
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_mainElement=new RewriteRuleSubtreeStream(adaptor,"rule mainElement");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:6: ( (m+= mainElement )* EOF -> ^( PROGRAM_UNIT ( $m)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:8: (m+= mainElement )* EOF
			{
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:8: (m+= mainElement )*
			loop1:
			do {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==BANG||(LA1_0 >= BOOL && LA1_0 <= BREAK)||LA1_0==CHECKPOINT||LA1_0==CONTINUE||LA1_0==EXIT||(LA1_0 >= FALSE && LA1_0 <= FOR)||(LA1_0 >= IDENTIFIER && LA1_0 <= IF)||(LA1_0 >= INT && LA1_0 <= INT_LITERAL)||(LA1_0 >= LBRACE && LA1_0 <= LBRACKET)||LA1_0==LPAREN||LA1_0==NEWLINE||LA1_0==PLUS||LA1_0==PLUSPLUS||(LA1_0 >= REAL && LA1_0 <= RETURN)||LA1_0==SEMI||(LA1_0 >= STRING && LA1_0 <= SUB)||LA1_0==SUBSUB||LA1_0==SYS_LITERAL||LA1_0==TASK||(LA1_0 >= TILDE && LA1_0 <= TRUE)||(LA1_0 >= VOID && LA1_0 <= WHILE)) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:9: m+= mainElement
					{
					pushFollow(FOLLOW_mainElement_in_main1403);
					m=mainElement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_mainElement.add(m.getTree());
					if (list_m==null) list_m=new ArrayList<Object>();
					list_m.add(m.getTree());
					}
					break;

				default :
					break loop1;
				}
			} while (true);

			EOF1=(Token)match(input,EOF,FOLLOW_EOF_in_main1407); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_EOF.add(EOF1);

			// AST REWRITE
			// elements: m
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: m
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_m=new RewriteRuleSubtreeStream(adaptor,"token m",list_m);
			root_0 = (Object)adaptor.nil();
			// 193:34: -> ^( PROGRAM_UNIT ( $m)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:37: ^( PROGRAM_UNIT ( $m)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(PROGRAM_UNIT, "PROGRAM_UNIT")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:193:53: ( $m)*
				while ( stream_m.hasNext() ) {
					adaptor.addChild(root_1, stream_m.nextTree());

				}
				stream_m.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "main"


	public static class mainElement_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mainElement"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:195:1: mainElement : ( varDeclaration | statement );
	public final BigDataScriptParser.mainElement_return mainElement() throws RecognitionException {
		BigDataScriptParser.mainElement_return retval = new BigDataScriptParser.mainElement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope varDeclaration2 =null;
		ParserRuleReturnScope statement3 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:195:13: ( varDeclaration | statement )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==BOOL||LA2_0==INT||LA2_0==REAL||LA2_0==STRING||LA2_0==VOID) ) {
				switch ( input.LA(2) ) {
				case IDENTIFIER:
					{
					int LA2_3 = input.LA(3);
					if ( (LA2_3==COMMA||LA2_3==EQ||LA2_3==NEWLINE||LA2_3==SEMI) ) {
						alt2=1;
					}
					else if ( (LA2_3==LPAREN) ) {
						alt2=2;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 2, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACKET:
					{
					int LA2_4 = input.LA(3);
					if ( (LA2_4==RBRACKET) ) {
						int LA2_7 = input.LA(4);
						if ( (LA2_7==IDENTIFIER) ) {
							int LA2_3 = input.LA(5);
							if ( (LA2_3==COMMA||LA2_3==EQ||LA2_3==NEWLINE||LA2_3==SEMI) ) {
								alt2=1;
							}
							else if ( (LA2_3==LPAREN) ) {
								alt2=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 2, 3, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 2, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 2, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACE:
					{
					int LA2_5 = input.LA(3);
					if ( (LA2_5==RBRACE) ) {
						int LA2_8 = input.LA(4);
						if ( (LA2_8==IDENTIFIER) ) {
							int LA2_3 = input.LA(5);
							if ( (LA2_3==COMMA||LA2_3==EQ||LA2_3==NEWLINE||LA2_3==SEMI) ) {
								alt2=1;
							}
							else if ( (LA2_3==LPAREN) ) {
								alt2=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 2, 3, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 2, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA2_5==BOOL||LA2_5==INT||LA2_5==REAL||LA2_5==STRING||LA2_5==VOID) ) {
						int LA2_9 = input.LA(4);
						if ( (LA2_9==RBRACE) ) {
							int LA2_10 = input.LA(5);
							if ( (LA2_10==IDENTIFIER) ) {
								int LA2_3 = input.LA(6);
								if ( (LA2_3==COMMA||LA2_3==EQ||LA2_3==NEWLINE||LA2_3==SEMI) ) {
									alt2=1;
								}
								else if ( (LA2_3==LPAREN) ) {
									alt2=2;
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 2, 3, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 2, 10, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 2, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 2, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 2, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA2_0==BANG||LA2_0==BREAK||LA2_0==CHECKPOINT||LA2_0==CONTINUE||LA2_0==EXIT||(LA2_0 >= FALSE && LA2_0 <= FOR)||(LA2_0 >= IDENTIFIER && LA2_0 <= IF)||LA2_0==INT_LITERAL||(LA2_0 >= LBRACE && LA2_0 <= LBRACKET)||LA2_0==LPAREN||LA2_0==NEWLINE||LA2_0==PLUS||LA2_0==PLUSPLUS||(LA2_0 >= REAL_LITERAL && LA2_0 <= RETURN)||LA2_0==SEMI||(LA2_0 >= STRING_LITERAL && LA2_0 <= SUB)||LA2_0==SUBSUB||LA2_0==SYS_LITERAL||LA2_0==TASK||(LA2_0 >= TILDE && LA2_0 <= TRUE)||(LA2_0 >= WAIT && LA2_0 <= WHILE)) ) {
				alt2=2;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}
			switch (alt2) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:195:15: varDeclaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_varDeclaration_in_mainElement1430);
					varDeclaration2=varDeclaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, varDeclaration2.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:195:32: statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_statement_in_mainElement1434);
					statement3=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement3.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "mainElement"


	public static class semi_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "semi"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:1: semi : ( ( SEMI )+ | ( NEWLINE )+ );
	public final BigDataScriptParser.semi_return semi() throws RecognitionException {
		BigDataScriptParser.semi_return retval = new BigDataScriptParser.semi_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SEMI4=null;
		Token NEWLINE5=null;

		Object SEMI4_tree=null;
		Object NEWLINE5_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:6: ( ( SEMI )+ | ( NEWLINE )+ )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==SEMI) ) {
				alt5=1;
			}
			else if ( (LA5_0==NEWLINE) ) {
				alt5=2;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}
			switch (alt5) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:8: ( SEMI )+
					{
					root_0 = (Object)adaptor.nil();


					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:8: ( SEMI )+
					int cnt3=0;
					loop3:
					do {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( (LA3_0==SEMI) ) {
							int LA3_2 = input.LA(2);
							if ( (synpred3_BigDataScript()) ) {
								alt3=1;
							}

						}

						switch (alt3) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:8: SEMI
							{
							SEMI4=(Token)match(input,SEMI,FOLLOW_SEMI_in_semi1444); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							SEMI4_tree = 
							(Object)adaptor.create(SEMI4)
							;
							adaptor.addChild(root_0, SEMI4_tree);
							}

							}
							break;

						default :
							if ( cnt3 >= 1 ) break loop3;
							if (state.backtracking>0) {state.failed=true; return retval;}
								EarlyExitException eee =
									new EarlyExitException(3, input);
								throw eee;
						}
						cnt3++;
					} while (true);

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:17: ( NEWLINE )+
					{
					root_0 = (Object)adaptor.nil();


					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:17: ( NEWLINE )+
					int cnt4=0;
					loop4:
					do {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==NEWLINE) ) {
							int LA4_2 = input.LA(2);
							if ( (synpred5_BigDataScript()) ) {
								alt4=1;
							}

						}

						switch (alt4) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:17: NEWLINE
							{
							NEWLINE5=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_semi1450); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							NEWLINE5_tree = 
							(Object)adaptor.create(NEWLINE5)
							;
							adaptor.addChild(root_0, NEWLINE5_tree);
							}

							}
							break;

						default :
							if ( cnt4 >= 1 ) break loop4;
							if (state.backtracking>0) {state.failed=true; return retval;}
								EarlyExitException eee =
									new EarlyExitException(4, input);
								throw eee;
						}
						cnt4++;
					} while (true);

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "semi"


	public static class typeList_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "typeList"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:201:1: typeList : type ( COMMA type )* ;
	public final BigDataScriptParser.typeList_return typeList() throws RecognitionException {
		BigDataScriptParser.typeList_return retval = new BigDataScriptParser.typeList_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA7=null;
		ParserRuleReturnScope type6 =null;
		ParserRuleReturnScope type8 =null;

		Object COMMA7_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:201:10: ( type ( COMMA type )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:201:12: type ( COMMA type )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_type_in_typeList1460);
			type6=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, type6.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:201:17: ( COMMA type )*
			loop6:
			do {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==COMMA) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:201:18: COMMA type
					{
					COMMA7=(Token)match(input,COMMA,FOLLOW_COMMA_in_typeList1463); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA7_tree = 
					(Object)adaptor.create(COMMA7)
					;
					adaptor.addChild(root_0, COMMA7_tree);
					}

					pushFollow(FOLLOW_type_in_typeList1465);
					type8=type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type8.getTree());

					}
					break;

				default :
					break loop6;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "typeList"


	public static class memberDecl_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "memberDecl"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:206:1: memberDecl : ( varDeclaration | methodDeclaration );
	public final BigDataScriptParser.memberDecl_return memberDecl() throws RecognitionException {
		BigDataScriptParser.memberDecl_return retval = new BigDataScriptParser.memberDecl_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope varDeclaration9 =null;
		ParserRuleReturnScope methodDeclaration10 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:206:12: ( varDeclaration | methodDeclaration )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==BOOL||LA7_0==INT||LA7_0==REAL||LA7_0==STRING||LA7_0==VOID) ) {
				switch ( input.LA(2) ) {
				case IDENTIFIER:
					{
					int LA7_2 = input.LA(3);
					if ( (LA7_2==COMMA||LA7_2==EQ||LA7_2==NEWLINE||LA7_2==SEMI) ) {
						alt7=1;
					}
					else if ( (LA7_2==LPAREN) ) {
						alt7=2;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 7, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACKET:
					{
					int LA7_3 = input.LA(3);
					if ( (LA7_3==RBRACKET) ) {
						int LA7_7 = input.LA(4);
						if ( (LA7_7==IDENTIFIER) ) {
							int LA7_2 = input.LA(5);
							if ( (LA7_2==COMMA||LA7_2==EQ||LA7_2==NEWLINE||LA7_2==SEMI) ) {
								alt7=1;
							}
							else if ( (LA7_2==LPAREN) ) {
								alt7=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 7, 2, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 7, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 7, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACE:
					{
					int LA7_4 = input.LA(3);
					if ( (LA7_4==RBRACE) ) {
						int LA7_8 = input.LA(4);
						if ( (LA7_8==IDENTIFIER) ) {
							int LA7_2 = input.LA(5);
							if ( (LA7_2==COMMA||LA7_2==EQ||LA7_2==NEWLINE||LA7_2==SEMI) ) {
								alt7=1;
							}
							else if ( (LA7_2==LPAREN) ) {
								alt7=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 7, 2, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 7, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA7_4==BOOL||LA7_4==INT||LA7_4==REAL||LA7_4==STRING||LA7_4==VOID) ) {
						int LA7_9 = input.LA(4);
						if ( (LA7_9==RBRACE) ) {
							int LA7_10 = input.LA(5);
							if ( (LA7_10==IDENTIFIER) ) {
								int LA7_2 = input.LA(6);
								if ( (LA7_2==COMMA||LA7_2==EQ||LA7_2==NEWLINE||LA7_2==SEMI) ) {
									alt7=1;
								}
								else if ( (LA7_2==LPAREN) ) {
									alt7=2;
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 7, 2, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 7, 10, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 7, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 7, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}
			switch (alt7) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:206:14: varDeclaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_varDeclaration_in_memberDecl1479);
					varDeclaration9=varDeclaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, varDeclaration9.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:206:31: methodDeclaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_methodDeclaration_in_memberDecl1483);
					methodDeclaration10=methodDeclaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, methodDeclaration10.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "memberDecl"


	public static class varDeclaration_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "varDeclaration"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:1: varDeclaration : t= type v+= variableInit ( COMMA v+= variableInit )* semi -> ^( VAR_DECLARATION $t ( $v)* ) ;
	public final BigDataScriptParser.varDeclaration_return varDeclaration() throws RecognitionException {
		BigDataScriptParser.varDeclaration_return retval = new BigDataScriptParser.varDeclaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA11=null;
		List<Object> list_v=null;
		ParserRuleReturnScope t =null;
		ParserRuleReturnScope semi12 =null;
		RuleReturnScope v = null;
		Object COMMA11_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleSubtreeStream stream_semi=new RewriteRuleSubtreeStream(adaptor,"rule semi");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
		RewriteRuleSubtreeStream stream_variableInit=new RewriteRuleSubtreeStream(adaptor,"rule variableInit");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:16: (t= type v+= variableInit ( COMMA v+= variableInit )* semi -> ^( VAR_DECLARATION $t ( $v)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:18: t= type v+= variableInit ( COMMA v+= variableInit )* semi
			{
			pushFollow(FOLLOW_type_in_varDeclaration1494);
			t=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(t.getTree());
			pushFollow(FOLLOW_variableInit_in_varDeclaration1498);
			v=variableInit();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_variableInit.add(v.getTree());
			if (list_v==null) list_v=new ArrayList<Object>();
			list_v.add(v.getTree());
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:41: ( COMMA v+= variableInit )*
			loop8:
			do {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==COMMA) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:42: COMMA v+= variableInit
					{
					COMMA11=(Token)match(input,COMMA,FOLLOW_COMMA_in_varDeclaration1501); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COMMA.add(COMMA11);

					pushFollow(FOLLOW_variableInit_in_varDeclaration1505);
					v=variableInit();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_variableInit.add(v.getTree());
					if (list_v==null) list_v=new ArrayList<Object>();
					list_v.add(v.getTree());
					}
					break;

				default :
					break loop8;
				}
			} while (true);

			pushFollow(FOLLOW_semi_in_varDeclaration1509);
			semi12=semi();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_semi.add(semi12.getTree());
			// AST REWRITE
			// elements: v, t
			// token labels: 
			// rule labels: retval, t
			// token list labels: 
			// rule list labels: v
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);
			RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"token v",list_v);
			root_0 = (Object)adaptor.nil();
			// 208:73: -> ^( VAR_DECLARATION $t ( $v)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:76: ^( VAR_DECLARATION $t ( $v)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_DECLARATION, "VAR_DECLARATION")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:208:98: ( $v)*
				while ( stream_v.hasNext() ) {
					adaptor.addChild(root_1, stream_v.nextTree());

				}
				stream_v.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "varDeclaration"


	public static class variableInit_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "variableInit"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:1: variableInit : id= IDENTIFIER ( EQ eq= variableInitializer )? -> ^( VAR_INIT $id ( $eq)? ) ;
	public final BigDataScriptParser.variableInit_return variableInit() throws RecognitionException {
		BigDataScriptParser.variableInit_return retval = new BigDataScriptParser.variableInit_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token id=null;
		Token EQ13=null;
		ParserRuleReturnScope eq =null;

		Object id_tree=null;
		Object EQ13_tree=null;
		RewriteRuleTokenStream stream_EQ=new RewriteRuleTokenStream(adaptor,"token EQ");
		RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
		RewriteRuleSubtreeStream stream_variableInitializer=new RewriteRuleSubtreeStream(adaptor,"rule variableInitializer");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:14: (id= IDENTIFIER ( EQ eq= variableInitializer )? -> ^( VAR_INIT $id ( $eq)? ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:16: id= IDENTIFIER ( EQ eq= variableInitializer )?
			{
			id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableInit1534); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IDENTIFIER.add(id);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:30: ( EQ eq= variableInitializer )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==EQ) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:31: EQ eq= variableInitializer
					{
					EQ13=(Token)match(input,EQ,FOLLOW_EQ_in_variableInit1537); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_EQ.add(EQ13);

					pushFollow(FOLLOW_variableInitializer_in_variableInit1541);
					eq=variableInitializer();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_variableInitializer.add(eq.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: eq, id
			// token labels: id
			// rule labels: retval, eq
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleTokenStream stream_id=new RewriteRuleTokenStream(adaptor,"token id",id);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_eq=new RewriteRuleSubtreeStream(adaptor,"rule eq",eq!=null?eq.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 210:61: -> ^( VAR_INIT $id ( $eq)? )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:64: ^( VAR_INIT $id ( $eq)? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_INIT, "VAR_INIT")
				, root_1);

				adaptor.addChild(root_1, stream_id.nextNode());

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:210:80: ( $eq)?
				if ( stream_eq.hasNext() ) {
					adaptor.addChild(root_1, stream_eq.nextTree());

				}
				stream_eq.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "variableInit"


	public static class variableInitializer_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "variableInitializer"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:1: variableInitializer : ( arrayInitializer | assignmentExpression );
	public final BigDataScriptParser.variableInitializer_return variableInitializer() throws RecognitionException {
		BigDataScriptParser.variableInitializer_return retval = new BigDataScriptParser.variableInitializer_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arrayInitializer14 =null;
		ParserRuleReturnScope assignmentExpression15 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:21: ( arrayInitializer | assignmentExpression )
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==LBRACE) ) {
				int LA10_1 = input.LA(2);
				if ( (synpred10_BigDataScript()) ) {
					alt10=1;
				}
				else if ( (true) ) {
					alt10=2;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 10, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA10_0==BANG||LA10_0==FALSE||LA10_0==IDENTIFIER||LA10_0==INT_LITERAL||LA10_0==LBRACKET||LA10_0==LPAREN||LA10_0==PLUS||LA10_0==PLUSPLUS||LA10_0==REAL_LITERAL||(LA10_0 >= STRING_LITERAL && LA10_0 <= SUB)||LA10_0==SUBSUB||LA10_0==SYS_LITERAL||LA10_0==TASK||(LA10_0 >= TILDE && LA10_0 <= TRUE)) ) {
				alt10=2;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:23: arrayInitializer
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arrayInitializer_in_variableInitializer1566);
					arrayInitializer14=arrayInitializer();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayInitializer14.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:42: assignmentExpression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_assignmentExpression_in_variableInitializer1570);
					assignmentExpression15=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression15.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "variableInitializer"


	public static class arrayInitializer_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arrayInitializer"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:1: arrayInitializer : LBRACE ( variableInitializer ( COMMA variableInitializer )* )? ( COMMA )? RBRACE ;
	public final BigDataScriptParser.arrayInitializer_return arrayInitializer() throws RecognitionException {
		BigDataScriptParser.arrayInitializer_return retval = new BigDataScriptParser.arrayInitializer_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACE16=null;
		Token COMMA18=null;
		Token COMMA20=null;
		Token RBRACE21=null;
		ParserRuleReturnScope variableInitializer17 =null;
		ParserRuleReturnScope variableInitializer19 =null;

		Object LBRACE16_tree=null;
		Object COMMA18_tree=null;
		Object COMMA20_tree=null;
		Object RBRACE21_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:18: ( LBRACE ( variableInitializer ( COMMA variableInitializer )* )? ( COMMA )? RBRACE )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:20: LBRACE ( variableInitializer ( COMMA variableInitializer )* )? ( COMMA )? RBRACE
			{
			root_0 = (Object)adaptor.nil();


			LBRACE16=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_arrayInitializer1579); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			LBRACE16_tree = 
			(Object)adaptor.create(LBRACE16)
			;
			adaptor.addChild(root_0, LBRACE16_tree);
			}

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:27: ( variableInitializer ( COMMA variableInitializer )* )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==BANG||LA12_0==FALSE||LA12_0==IDENTIFIER||LA12_0==INT_LITERAL||(LA12_0 >= LBRACE && LA12_0 <= LBRACKET)||LA12_0==LPAREN||LA12_0==PLUS||LA12_0==PLUSPLUS||LA12_0==REAL_LITERAL||(LA12_0 >= STRING_LITERAL && LA12_0 <= SUB)||LA12_0==SUBSUB||LA12_0==SYS_LITERAL||LA12_0==TASK||(LA12_0 >= TILDE && LA12_0 <= TRUE)) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:28: variableInitializer ( COMMA variableInitializer )*
					{
					pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1582);
					variableInitializer17=variableInitializer();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, variableInitializer17.getTree());

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:48: ( COMMA variableInitializer )*
					loop11:
					do {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( (LA11_0==COMMA) ) {
							int LA11_1 = input.LA(2);
							if ( (LA11_1==BANG||LA11_1==FALSE||LA11_1==IDENTIFIER||LA11_1==INT_LITERAL||(LA11_1 >= LBRACE && LA11_1 <= LBRACKET)||LA11_1==LPAREN||LA11_1==PLUS||LA11_1==PLUSPLUS||LA11_1==REAL_LITERAL||(LA11_1 >= STRING_LITERAL && LA11_1 <= SUB)||LA11_1==SUBSUB||LA11_1==SYS_LITERAL||LA11_1==TASK||(LA11_1 >= TILDE && LA11_1 <= TRUE)) ) {
								alt11=1;
							}

						}

						switch (alt11) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:49: COMMA variableInitializer
							{
							COMMA18=(Token)match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer1585); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA18_tree = 
							(Object)adaptor.create(COMMA18)
							;
							adaptor.addChild(root_0, COMMA18_tree);
							}

							pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1587);
							variableInitializer19=variableInitializer();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, variableInitializer19.getTree());

							}
							break;

						default :
							break loop11;
						}
					} while (true);

					}
					break;

			}

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:79: ( COMMA )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==COMMA) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:214:80: COMMA
					{
					COMMA20=(Token)match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer1594); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA20_tree = 
					(Object)adaptor.create(COMMA20)
					;
					adaptor.addChild(root_0, COMMA20_tree);
					}

					}
					break;

			}

			RBRACE21=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_arrayInitializer1598); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			RBRACE21_tree = 
			(Object)adaptor.create(RBRACE21)
			;
			adaptor.addChild(root_0, RBRACE21_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arrayInitializer"


	public static class methodDeclaration_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "methodDeclaration"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:216:1: methodDeclaration : t= type n= IDENTIFIER p= formalParameters b= statement -> ^( FUNCTION_DECLARATION $n $t $p $b) ;
	public final BigDataScriptParser.methodDeclaration_return methodDeclaration() throws RecognitionException {
		BigDataScriptParser.methodDeclaration_return retval = new BigDataScriptParser.methodDeclaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token n=null;
		ParserRuleReturnScope t =null;
		ParserRuleReturnScope p =null;
		ParserRuleReturnScope b =null;

		Object n_tree=null;
		RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
		RewriteRuleSubtreeStream stream_formalParameters=new RewriteRuleSubtreeStream(adaptor,"rule formalParameters");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:216:19: (t= type n= IDENTIFIER p= formalParameters b= statement -> ^( FUNCTION_DECLARATION $n $t $p $b) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:216:21: t= type n= IDENTIFIER p= formalParameters b= statement
			{
			pushFollow(FOLLOW_type_in_methodDeclaration1609);
			t=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(t.getTree());
			n=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration1613); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IDENTIFIER.add(n);

			pushFollow(FOLLOW_formalParameters_in_methodDeclaration1617);
			p=formalParameters();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_formalParameters.add(p.getTree());
			pushFollow(FOLLOW_statement_in_methodDeclaration1621);
			b=statement();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_statement.add(b.getTree());
			// AST REWRITE
			// elements: b, p, t, n
			// token labels: n
			// rule labels: retval, t, b, p
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleTokenStream stream_n=new RewriteRuleTokenStream(adaptor,"token n",n);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);
			RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.getTree():null);
			RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"rule p",p!=null?p.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 216:72: -> ^( FUNCTION_DECLARATION $n $t $p $b)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:216:75: ^( FUNCTION_DECLARATION $n $t $p $b)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(FUNCTION_DECLARATION, "FUNCTION_DECLARATION")
				, root_1);

				adaptor.addChild(root_1, stream_n.nextNode());

				adaptor.addChild(root_1, stream_t.nextTree());

				adaptor.addChild(root_1, stream_p.nextTree());

				adaptor.addChild(root_1, stream_b.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "methodDeclaration"


	public static class type_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "type"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:218:1: type : ( typePrimitive | typeArrayList | typeMap );
	public final BigDataScriptParser.type_return type() throws RecognitionException {
		BigDataScriptParser.type_return retval = new BigDataScriptParser.type_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope typePrimitive22 =null;
		ParserRuleReturnScope typeArrayList23 =null;
		ParserRuleReturnScope typeMap24 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:218:6: ( typePrimitive | typeArrayList | typeMap )
			int alt14=3;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==BOOL||LA14_0==INT||LA14_0==REAL||LA14_0==STRING||LA14_0==VOID) ) {
				switch ( input.LA(2) ) {
				case EOF:
				case COMMA:
				case ELLIPSIS:
				case GT:
				case IDENTIFIER:
					{
					alt14=1;
					}
					break;
				case LBRACKET:
					{
					alt14=2;
					}
					break;
				case LBRACE:
					{
					alt14=3;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 14, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}
			switch (alt14) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:218:8: typePrimitive
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_typePrimitive_in_type1647);
					typePrimitive22=typePrimitive();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, typePrimitive22.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:219:4: typeArrayList
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_typeArrayList_in_type1652);
					typeArrayList23=typeArrayList();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArrayList23.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:220:4: typeMap
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_typeMap_in_type1657);
					typeMap24=typeMap();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, typeMap24.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type"


	public static class typePrimitive_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "typePrimitive"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:223:1: typePrimitive : t= primitiveType -> ^( TYPE $t) ;
	public final BigDataScriptParser.typePrimitive_return typePrimitive() throws RecognitionException {
		BigDataScriptParser.typePrimitive_return retval = new BigDataScriptParser.typePrimitive_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope t =null;

		RewriteRuleSubtreeStream stream_primitiveType=new RewriteRuleSubtreeStream(adaptor,"rule primitiveType");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:223:15: (t= primitiveType -> ^( TYPE $t) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:223:17: t= primitiveType
			{
			pushFollow(FOLLOW_primitiveType_in_typePrimitive1669);
			t=primitiveType();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_primitiveType.add(t.getTree());
			// AST REWRITE
			// elements: t
			// token labels: 
			// rule labels: retval, t
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 223:37: -> ^( TYPE $t)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:223:40: ^( TYPE $t)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(TYPE, "TYPE")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "typePrimitive"


	public static class typeArrayList_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "typeArrayList"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:225:1: typeArrayList : t= typePrimitive LBRACKET RBRACKET -> ^( TYPE_LIST $t) ;
	public final BigDataScriptParser.typeArrayList_return typeArrayList() throws RecognitionException {
		BigDataScriptParser.typeArrayList_return retval = new BigDataScriptParser.typeArrayList_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACKET25=null;
		Token RBRACKET26=null;
		ParserRuleReturnScope t =null;

		Object LBRACKET25_tree=null;
		Object RBRACKET26_tree=null;
		RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
		RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
		RewriteRuleSubtreeStream stream_typePrimitive=new RewriteRuleSubtreeStream(adaptor,"rule typePrimitive");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:225:15: (t= typePrimitive LBRACKET RBRACKET -> ^( TYPE_LIST $t) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:225:17: t= typePrimitive LBRACKET RBRACKET
			{
			pushFollow(FOLLOW_typePrimitive_in_typeArrayList1692);
			t=typePrimitive();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_typePrimitive.add(t.getTree());
			LBRACKET25=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_typeArrayList1694); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET25);

			RBRACKET26=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_typeArrayList1696); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET26);

			// AST REWRITE
			// elements: t
			// token labels: 
			// rule labels: retval, t
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 225:53: -> ^( TYPE_LIST $t)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:225:56: ^( TYPE_LIST $t)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(TYPE_LIST, "TYPE_LIST")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "typeArrayList"


	public static class typeMap_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "typeMap"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:227:1: typeMap : (val= primitiveType LBRACE RBRACE -> ^( TYPE_MAP $val STRING ) |val= primitiveType LBRACE key= primitiveType RBRACE -> ^( TYPE_MAP $val $key) );
	public final BigDataScriptParser.typeMap_return typeMap() throws RecognitionException {
		BigDataScriptParser.typeMap_return retval = new BigDataScriptParser.typeMap_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACE27=null;
		Token RBRACE28=null;
		Token LBRACE29=null;
		Token RBRACE30=null;
		ParserRuleReturnScope val =null;
		ParserRuleReturnScope key =null;

		Object LBRACE27_tree=null;
		Object RBRACE28_tree=null;
		Object LBRACE29_tree=null;
		Object RBRACE30_tree=null;
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleSubtreeStream stream_primitiveType=new RewriteRuleSubtreeStream(adaptor,"rule primitiveType");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:227:9: (val= primitiveType LBRACE RBRACE -> ^( TYPE_MAP $val STRING ) |val= primitiveType LBRACE key= primitiveType RBRACE -> ^( TYPE_MAP $val $key) )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==BOOL||LA15_0==INT||LA15_0==REAL||LA15_0==STRING||LA15_0==VOID) ) {
				int LA15_1 = input.LA(2);
				if ( (LA15_1==LBRACE) ) {
					int LA15_2 = input.LA(3);
					if ( (LA15_2==RBRACE) ) {
						alt15=1;
					}
					else if ( (LA15_2==BOOL||LA15_2==INT||LA15_2==REAL||LA15_2==STRING||LA15_2==VOID) ) {
						alt15=2;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 15, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 15, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}
			switch (alt15) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:228:2: val= primitiveType LBRACE RBRACE
					{
					pushFollow(FOLLOW_primitiveType_in_typeMap1719);
					val=primitiveType();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_primitiveType.add(val.getTree());
					LBRACE27=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_typeMap1721); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LBRACE.add(LBRACE27);

					RBRACE28=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_typeMap1723); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE28);

					// AST REWRITE
					// elements: val
					// token labels: 
					// rule labels: val, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_val=new RewriteRuleSubtreeStream(adaptor,"rule val",val!=null?val.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 228:37: -> ^( TYPE_MAP $val STRING )
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:228:40: ^( TYPE_MAP $val STRING )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(TYPE_MAP, "TYPE_MAP")
						, root_1);

						adaptor.addChild(root_1, stream_val.nextTree());

						adaptor.addChild(root_1, 
						(Object)adaptor.create(STRING, "STRING")
						);

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:229:4: val= primitiveType LBRACE key= primitiveType RBRACE
					{
					pushFollow(FOLLOW_primitiveType_in_typeMap1744);
					val=primitiveType();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_primitiveType.add(val.getTree());
					LBRACE29=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_typeMap1746); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LBRACE.add(LBRACE29);

					pushFollow(FOLLOW_primitiveType_in_typeMap1750);
					key=primitiveType();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_primitiveType.add(key.getTree());
					RBRACE30=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_typeMap1752); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE30);

					// AST REWRITE
					// elements: key, val
					// token labels: 
					// rule labels: val, retval, key
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_val=new RewriteRuleSubtreeStream(adaptor,"rule val",val!=null?val.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_key=new RewriteRuleSubtreeStream(adaptor,"rule key",key!=null?key.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 229:55: -> ^( TYPE_MAP $val $key)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:229:58: ^( TYPE_MAP $val $key)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(TYPE_MAP, "TYPE_MAP")
						, root_1);

						adaptor.addChild(root_1, stream_val.nextTree());

						adaptor.addChild(root_1, stream_key.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "typeMap"


	public static class primitiveType_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "primitiveType"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:232:1: primitiveType : ( BOOL | INT | REAL | STRING | VOID );
	public final BigDataScriptParser.primitiveType_return primitiveType() throws RecognitionException {
		BigDataScriptParser.primitiveType_return retval = new BigDataScriptParser.primitiveType_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set31=null;

		Object set31_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:232:15: ( BOOL | INT | REAL | STRING | VOID )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
			{
			root_0 = (Object)adaptor.nil();


			set31=(Token)input.LT(1);
			if ( input.LA(1)==BOOL||input.LA(1)==INT||input.LA(1)==REAL||input.LA(1)==STRING||input.LA(1)==VOID ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, 
				(Object)adaptor.create(set31)
				);
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "primitiveType"


	public static class typeArguments_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "typeArguments"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:234:1: typeArguments : LT type ( COMMA type )* GT ;
	public final BigDataScriptParser.typeArguments_return typeArguments() throws RecognitionException {
		BigDataScriptParser.typeArguments_return retval = new BigDataScriptParser.typeArguments_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LT32=null;
		Token COMMA34=null;
		Token GT36=null;
		ParserRuleReturnScope type33 =null;
		ParserRuleReturnScope type35 =null;

		Object LT32_tree=null;
		Object COMMA34_tree=null;
		Object GT36_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:234:15: ( LT type ( COMMA type )* GT )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:234:17: LT type ( COMMA type )* GT
			{
			root_0 = (Object)adaptor.nil();


			LT32=(Token)match(input,LT,FOLLOW_LT_in_typeArguments1799); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			LT32_tree = 
			(Object)adaptor.create(LT32)
			;
			adaptor.addChild(root_0, LT32_tree);
			}

			pushFollow(FOLLOW_type_in_typeArguments1801);
			type33=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, type33.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:234:25: ( COMMA type )*
			loop16:
			do {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==COMMA) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:234:26: COMMA type
					{
					COMMA34=(Token)match(input,COMMA,FOLLOW_COMMA_in_typeArguments1804); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA34_tree = 
					(Object)adaptor.create(COMMA34)
					;
					adaptor.addChild(root_0, COMMA34_tree);
					}

					pushFollow(FOLLOW_type_in_typeArguments1806);
					type35=type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type35.getTree());

					}
					break;

				default :
					break loop16;
				}
			} while (true);

			GT36=(Token)match(input,GT,FOLLOW_GT_in_typeArguments1810); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			GT36_tree = 
			(Object)adaptor.create(GT36)
			;
			adaptor.addChild(root_0, GT36_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "typeArguments"


	public static class formalParameters_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "formalParameters"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:236:1: formalParameters : LPAREN ! ( formalParameterDecls )? RPAREN !;
	public final BigDataScriptParser.formalParameters_return formalParameters() throws RecognitionException {
		BigDataScriptParser.formalParameters_return retval = new BigDataScriptParser.formalParameters_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN37=null;
		Token RPAREN39=null;
		ParserRuleReturnScope formalParameterDecls38 =null;

		Object LPAREN37_tree=null;
		Object RPAREN39_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:236:18: ( LPAREN ! ( formalParameterDecls )? RPAREN !)
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:236:20: LPAREN ! ( formalParameterDecls )? RPAREN !
			{
			root_0 = (Object)adaptor.nil();


			LPAREN37=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters1819); if (state.failed) return retval;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:236:28: ( formalParameterDecls )?
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==BOOL||LA17_0==INT||LA17_0==REAL||LA17_0==STRING||LA17_0==VOID) ) {
				alt17=1;
			}
			switch (alt17) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:236:29: formalParameterDecls
					{
					pushFollow(FOLLOW_formalParameterDecls_in_formalParameters1823);
					formalParameterDecls38=formalParameterDecls();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, formalParameterDecls38.getTree());

					}
					break;

			}

			RPAREN39=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters1827); if (state.failed) return retval;
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "formalParameters"


	public static class formalParameterDecls_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "formalParameterDecls"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:1: formalParameterDecls :p+= formalParameterDecl ( COMMA p+= formalParameterDecl )* -> ^( PARAMETERS ( $p)* ) ;
	public final BigDataScriptParser.formalParameterDecls_return formalParameterDecls() throws RecognitionException {
		BigDataScriptParser.formalParameterDecls_return retval = new BigDataScriptParser.formalParameterDecls_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA40=null;
		List<Object> list_p=null;
		RuleReturnScope p = null;
		Object COMMA40_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleSubtreeStream stream_formalParameterDecl=new RewriteRuleSubtreeStream(adaptor,"rule formalParameterDecl");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:22: (p+= formalParameterDecl ( COMMA p+= formalParameterDecl )* -> ^( PARAMETERS ( $p)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:24: p+= formalParameterDecl ( COMMA p+= formalParameterDecl )*
			{
			pushFollow(FOLLOW_formalParameterDecl_in_formalParameterDecls1839);
			p=formalParameterDecl();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_formalParameterDecl.add(p.getTree());
			if (list_p==null) list_p=new ArrayList<Object>();
			list_p.add(p.getTree());
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:47: ( COMMA p+= formalParameterDecl )*
			loop18:
			do {
				int alt18=2;
				int LA18_0 = input.LA(1);
				if ( (LA18_0==COMMA) ) {
					alt18=1;
				}

				switch (alt18) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:48: COMMA p+= formalParameterDecl
					{
					COMMA40=(Token)match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls1842); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COMMA.add(COMMA40);

					pushFollow(FOLLOW_formalParameterDecl_in_formalParameterDecls1846);
					p=formalParameterDecl();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_formalParameterDecl.add(p.getTree());
					if (list_p==null) list_p=new ArrayList<Object>();
					list_p.add(p.getTree());
					}
					break;

				default :
					break loop18;
				}
			} while (true);

			// AST REWRITE
			// elements: p
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: p
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"token p",list_p);
			root_0 = (Object)adaptor.nil();
			// 238:79: -> ^( PARAMETERS ( $p)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:82: ^( PARAMETERS ( $p)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(PARAMETERS, "PARAMETERS")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:238:96: ( $p)*
				while ( stream_p.hasNext() ) {
					adaptor.addChild(root_1, stream_p.nextTree());

				}
				stream_p.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "formalParameterDecls"


	public static class formalParameterDecl_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "formalParameterDecl"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:240:1: formalParameterDecl : t= type v= variableInit -> ^( VAR_DECLARATION $t $v) ;
	public final BigDataScriptParser.formalParameterDecl_return formalParameterDecl() throws RecognitionException {
		BigDataScriptParser.formalParameterDecl_return retval = new BigDataScriptParser.formalParameterDecl_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope t =null;
		ParserRuleReturnScope v =null;

		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
		RewriteRuleSubtreeStream stream_variableInit=new RewriteRuleSubtreeStream(adaptor,"rule variableInit");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:240:21: (t= type v= variableInit -> ^( VAR_DECLARATION $t $v) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:240:23: t= type v= variableInit
			{
			pushFollow(FOLLOW_type_in_formalParameterDecl1868);
			t=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(t.getTree());
			pushFollow(FOLLOW_variableInit_in_formalParameterDecl1872);
			v=variableInit();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_variableInit.add(v.getTree());
			// AST REWRITE
			// elements: v, t
			// token labels: 
			// rule labels: v, retval, t
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"rule v",v!=null?v.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 240:48: -> ^( VAR_DECLARATION $t $v)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:240:51: ^( VAR_DECLARATION $t $v)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_DECLARATION, "VAR_DECLARATION")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				adaptor.addChild(root_1, stream_v.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "formalParameterDecl"


	public static class ellipsisParameterDecl_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ellipsisParameterDecl"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:242:1: ellipsisParameterDecl : type ELLIPSIS IDENTIFIER ;
	public final BigDataScriptParser.ellipsisParameterDecl_return ellipsisParameterDecl() throws RecognitionException {
		BigDataScriptParser.ellipsisParameterDecl_return retval = new BigDataScriptParser.ellipsisParameterDecl_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ELLIPSIS42=null;
		Token IDENTIFIER43=null;
		ParserRuleReturnScope type41 =null;

		Object ELLIPSIS42_tree=null;
		Object IDENTIFIER43_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:242:23: ( type ELLIPSIS IDENTIFIER )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:242:25: type ELLIPSIS IDENTIFIER
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_type_in_ellipsisParameterDecl1895);
			type41=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, type41.getTree());

			ELLIPSIS42=(Token)match(input,ELLIPSIS,FOLLOW_ELLIPSIS_in_ellipsisParameterDecl1897); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ELLIPSIS42_tree = 
			(Object)adaptor.create(ELLIPSIS42)
			;
			adaptor.addChild(root_0, ELLIPSIS42_tree);
			}

			IDENTIFIER43=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_ellipsisParameterDecl1899); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IDENTIFIER43_tree = 
			(Object)adaptor.create(IDENTIFIER43)
			;
			adaptor.addChild(root_0, IDENTIFIER43_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ellipsisParameterDecl"


	public static class parExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:248:1: parExpression : LPAREN ! assignmentExpression RPAREN !;
	public final BigDataScriptParser.parExpression_return parExpression() throws RecognitionException {
		BigDataScriptParser.parExpression_return retval = new BigDataScriptParser.parExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN44=null;
		Token RPAREN46=null;
		ParserRuleReturnScope assignmentExpression45 =null;

		Object LPAREN44_tree=null;
		Object RPAREN46_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:248:15: ( LPAREN ! assignmentExpression RPAREN !)
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:248:17: LPAREN ! assignmentExpression RPAREN !
			{
			root_0 = (Object)adaptor.nil();


			LPAREN44=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_parExpression1912); if (state.failed) return retval;
			pushFollow(FOLLOW_assignmentExpression_in_parExpression1915);
			assignmentExpression45=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression45.getTree());

			RPAREN46=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_parExpression1917); if (state.failed) return retval;
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parExpression"


	public static class expressionList_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "expressionList"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:250:1: expressionList : assignmentExpression ( COMMA ! assignmentExpression )* ;
	public final BigDataScriptParser.expressionList_return expressionList() throws RecognitionException {
		BigDataScriptParser.expressionList_return retval = new BigDataScriptParser.expressionList_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA48=null;
		ParserRuleReturnScope assignmentExpression47 =null;
		ParserRuleReturnScope assignmentExpression49 =null;

		Object COMMA48_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:250:16: ( assignmentExpression ( COMMA ! assignmentExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:250:18: assignmentExpression ( COMMA ! assignmentExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_assignmentExpression_in_expressionList1927);
			assignmentExpression47=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression47.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:250:39: ( COMMA ! assignmentExpression )*
			loop19:
			do {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( (LA19_0==COMMA) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:250:40: COMMA ! assignmentExpression
					{
					COMMA48=(Token)match(input,COMMA,FOLLOW_COMMA_in_expressionList1930); if (state.failed) return retval;
					pushFollow(FOLLOW_assignmentExpression_in_expressionList1933);
					assignmentExpression49=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression49.getTree());

					}
					break;

				default :
					break loop19;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expressionList"


	public static class assignmentExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "assignmentExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:252:1: assignmentExpression : sysExpression ( EQ ^ assignmentExpression )? ;
	public final BigDataScriptParser.assignmentExpression_return assignmentExpression() throws RecognitionException {
		BigDataScriptParser.assignmentExpression_return retval = new BigDataScriptParser.assignmentExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EQ51=null;
		ParserRuleReturnScope sysExpression50 =null;
		ParserRuleReturnScope assignmentExpression52 =null;

		Object EQ51_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:252:22: ( sysExpression ( EQ ^ assignmentExpression )? )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:252:24: sysExpression ( EQ ^ assignmentExpression )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_sysExpression_in_assignmentExpression1945);
			sysExpression50=sysExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, sysExpression50.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:252:38: ( EQ ^ assignmentExpression )?
			int alt20=2;
			int LA20_0 = input.LA(1);
			if ( (LA20_0==EQ) ) {
				alt20=1;
			}
			switch (alt20) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:252:39: EQ ^ assignmentExpression
					{
					EQ51=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentExpression1948); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EQ51_tree = 
					(Object)adaptor.create(EQ51)
					;
					root_0 = (Object)adaptor.becomeRoot(EQ51_tree, root_0);
					}

					pushFollow(FOLLOW_assignmentExpression_in_assignmentExpression1951);
					assignmentExpression52=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression52.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "assignmentExpression"


	public static class sysExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sysExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:254:1: sysExpression : ( conditionalExpression | sys | task );
	public final BigDataScriptParser.sysExpression_return sysExpression() throws RecognitionException {
		BigDataScriptParser.sysExpression_return retval = new BigDataScriptParser.sysExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope conditionalExpression53 =null;
		ParserRuleReturnScope sys54 =null;
		ParserRuleReturnScope task55 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:254:15: ( conditionalExpression | sys | task )
			int alt21=3;
			switch ( input.LA(1) ) {
			case BANG:
			case FALSE:
			case IDENTIFIER:
			case INT_LITERAL:
			case LBRACE:
			case LBRACKET:
			case LPAREN:
			case PLUS:
			case PLUSPLUS:
			case REAL_LITERAL:
			case STRING_LITERAL:
			case SUB:
			case SUBSUB:
			case TILDE:
			case TRUE:
				{
				alt21=1;
				}
				break;
			case SYS_LITERAL:
				{
				alt21=2;
				}
				break;
			case TASK:
				{
				alt21=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}
			switch (alt21) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:254:17: conditionalExpression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_conditionalExpression_in_sysExpression1962);
					conditionalExpression53=conditionalExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalExpression53.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:255:5: sys
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_sys_in_sysExpression1968);
					sys54=sys();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sys54.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:256:5: task
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_task_in_sysExpression1974);
					task55=task();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, task55.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sysExpression"


	public static class conditionalExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditionalExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:259:1: conditionalExpression : conditionalOrExpression ( QUES ^ assignmentExpression COLON conditionalExpression )? ;
	public final BigDataScriptParser.conditionalExpression_return conditionalExpression() throws RecognitionException {
		BigDataScriptParser.conditionalExpression_return retval = new BigDataScriptParser.conditionalExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token QUES57=null;
		Token COLON59=null;
		ParserRuleReturnScope conditionalOrExpression56 =null;
		ParserRuleReturnScope assignmentExpression58 =null;
		ParserRuleReturnScope conditionalExpression60 =null;

		Object QUES57_tree=null;
		Object COLON59_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:259:23: ( conditionalOrExpression ( QUES ^ assignmentExpression COLON conditionalExpression )? )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:259:25: conditionalOrExpression ( QUES ^ assignmentExpression COLON conditionalExpression )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression1985);
			conditionalOrExpression56=conditionalOrExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalOrExpression56.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:259:49: ( QUES ^ assignmentExpression COLON conditionalExpression )?
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==QUES) ) {
				alt22=1;
			}
			switch (alt22) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:259:50: QUES ^ assignmentExpression COLON conditionalExpression
					{
					QUES57=(Token)match(input,QUES,FOLLOW_QUES_in_conditionalExpression1988); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					QUES57_tree = 
					(Object)adaptor.create(QUES57)
					;
					root_0 = (Object)adaptor.becomeRoot(QUES57_tree, root_0);
					}

					pushFollow(FOLLOW_assignmentExpression_in_conditionalExpression1991);
					assignmentExpression58=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression58.getTree());

					COLON59=(Token)match(input,COLON,FOLLOW_COLON_in_conditionalExpression1993); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COLON59_tree = 
					(Object)adaptor.create(COLON59)
					;
					adaptor.addChild(root_0, COLON59_tree);
					}

					pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression1995);
					conditionalExpression60=conditionalExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalExpression60.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditionalExpression"


	public static class conditionalOrExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditionalOrExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:261:1: conditionalOrExpression : conditionalAndExpression ( BARBAR ^ conditionalAndExpression )* ;
	public final BigDataScriptParser.conditionalOrExpression_return conditionalOrExpression() throws RecognitionException {
		BigDataScriptParser.conditionalOrExpression_return retval = new BigDataScriptParser.conditionalOrExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token BARBAR62=null;
		ParserRuleReturnScope conditionalAndExpression61 =null;
		ParserRuleReturnScope conditionalAndExpression63 =null;

		Object BARBAR62_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:261:25: ( conditionalAndExpression ( BARBAR ^ conditionalAndExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:261:27: conditionalAndExpression ( BARBAR ^ conditionalAndExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression2006);
			conditionalAndExpression61=conditionalAndExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalAndExpression61.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:261:52: ( BARBAR ^ conditionalAndExpression )*
			loop23:
			do {
				int alt23=2;
				int LA23_0 = input.LA(1);
				if ( (LA23_0==BARBAR) ) {
					alt23=1;
				}

				switch (alt23) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:261:53: BARBAR ^ conditionalAndExpression
					{
					BARBAR62=(Token)match(input,BARBAR,FOLLOW_BARBAR_in_conditionalOrExpression2009); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					BARBAR62_tree = 
					(Object)adaptor.create(BARBAR62)
					;
					root_0 = (Object)adaptor.becomeRoot(BARBAR62_tree, root_0);
					}

					pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression2012);
					conditionalAndExpression63=conditionalAndExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalAndExpression63.getTree());

					}
					break;

				default :
					break loop23;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditionalOrExpression"


	public static class conditionalAndExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditionalAndExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:263:1: conditionalAndExpression : inclusiveOrExpression ( AMPAMP ^ inclusiveOrExpression )* ;
	public final BigDataScriptParser.conditionalAndExpression_return conditionalAndExpression() throws RecognitionException {
		BigDataScriptParser.conditionalAndExpression_return retval = new BigDataScriptParser.conditionalAndExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AMPAMP65=null;
		ParserRuleReturnScope inclusiveOrExpression64 =null;
		ParserRuleReturnScope inclusiveOrExpression66 =null;

		Object AMPAMP65_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:263:26: ( inclusiveOrExpression ( AMPAMP ^ inclusiveOrExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:263:28: inclusiveOrExpression ( AMPAMP ^ inclusiveOrExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression2023);
			inclusiveOrExpression64=inclusiveOrExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, inclusiveOrExpression64.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:263:50: ( AMPAMP ^ inclusiveOrExpression )*
			loop24:
			do {
				int alt24=2;
				int LA24_0 = input.LA(1);
				if ( (LA24_0==AMPAMP) ) {
					alt24=1;
				}

				switch (alt24) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:263:51: AMPAMP ^ inclusiveOrExpression
					{
					AMPAMP65=(Token)match(input,AMPAMP,FOLLOW_AMPAMP_in_conditionalAndExpression2026); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AMPAMP65_tree = 
					(Object)adaptor.create(AMPAMP65)
					;
					root_0 = (Object)adaptor.becomeRoot(AMPAMP65_tree, root_0);
					}

					pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression2029);
					inclusiveOrExpression66=inclusiveOrExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, inclusiveOrExpression66.getTree());

					}
					break;

				default :
					break loop24;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditionalAndExpression"


	public static class inclusiveOrExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "inclusiveOrExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:265:1: inclusiveOrExpression : exclusiveOrExpression ( BAR ^ exclusiveOrExpression )* ;
	public final BigDataScriptParser.inclusiveOrExpression_return inclusiveOrExpression() throws RecognitionException {
		BigDataScriptParser.inclusiveOrExpression_return retval = new BigDataScriptParser.inclusiveOrExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token BAR68=null;
		ParserRuleReturnScope exclusiveOrExpression67 =null;
		ParserRuleReturnScope exclusiveOrExpression69 =null;

		Object BAR68_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:265:23: ( exclusiveOrExpression ( BAR ^ exclusiveOrExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:265:25: exclusiveOrExpression ( BAR ^ exclusiveOrExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression2040);
			exclusiveOrExpression67=exclusiveOrExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, exclusiveOrExpression67.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:265:47: ( BAR ^ exclusiveOrExpression )*
			loop25:
			do {
				int alt25=2;
				int LA25_0 = input.LA(1);
				if ( (LA25_0==BAR) ) {
					alt25=1;
				}

				switch (alt25) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:265:48: BAR ^ exclusiveOrExpression
					{
					BAR68=(Token)match(input,BAR,FOLLOW_BAR_in_inclusiveOrExpression2043); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					BAR68_tree = 
					(Object)adaptor.create(BAR68)
					;
					root_0 = (Object)adaptor.becomeRoot(BAR68_tree, root_0);
					}

					pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression2046);
					exclusiveOrExpression69=exclusiveOrExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exclusiveOrExpression69.getTree());

					}
					break;

				default :
					break loop25;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "inclusiveOrExpression"


	public static class exclusiveOrExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "exclusiveOrExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:267:1: exclusiveOrExpression : andExpression ( CARET ^ andExpression )* ;
	public final BigDataScriptParser.exclusiveOrExpression_return exclusiveOrExpression() throws RecognitionException {
		BigDataScriptParser.exclusiveOrExpression_return retval = new BigDataScriptParser.exclusiveOrExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CARET71=null;
		ParserRuleReturnScope andExpression70 =null;
		ParserRuleReturnScope andExpression72 =null;

		Object CARET71_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:267:23: ( andExpression ( CARET ^ andExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:267:25: andExpression ( CARET ^ andExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression2057);
			andExpression70=andExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, andExpression70.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:267:39: ( CARET ^ andExpression )*
			loop26:
			do {
				int alt26=2;
				int LA26_0 = input.LA(1);
				if ( (LA26_0==CARET) ) {
					alt26=1;
				}

				switch (alt26) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:267:40: CARET ^ andExpression
					{
					CARET71=(Token)match(input,CARET,FOLLOW_CARET_in_exclusiveOrExpression2060); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					CARET71_tree = 
					(Object)adaptor.create(CARET71)
					;
					root_0 = (Object)adaptor.becomeRoot(CARET71_tree, root_0);
					}

					pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression2063);
					andExpression72=andExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, andExpression72.getTree());

					}
					break;

				default :
					break loop26;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exclusiveOrExpression"


	public static class andExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "andExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:269:1: andExpression : equalityExpression ( AMP ^ equalityExpression )* ;
	public final BigDataScriptParser.andExpression_return andExpression() throws RecognitionException {
		BigDataScriptParser.andExpression_return retval = new BigDataScriptParser.andExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AMP74=null;
		ParserRuleReturnScope equalityExpression73 =null;
		ParserRuleReturnScope equalityExpression75 =null;

		Object AMP74_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:269:15: ( equalityExpression ( AMP ^ equalityExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:269:17: equalityExpression ( AMP ^ equalityExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_equalityExpression_in_andExpression2074);
			equalityExpression73=equalityExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, equalityExpression73.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:269:36: ( AMP ^ equalityExpression )*
			loop27:
			do {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==AMP) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:269:37: AMP ^ equalityExpression
					{
					AMP74=(Token)match(input,AMP,FOLLOW_AMP_in_andExpression2077); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AMP74_tree = 
					(Object)adaptor.create(AMP74)
					;
					root_0 = (Object)adaptor.becomeRoot(AMP74_tree, root_0);
					}

					pushFollow(FOLLOW_equalityExpression_in_andExpression2080);
					equalityExpression75=equalityExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, equalityExpression75.getTree());

					}
					break;

				default :
					break loop27;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "andExpression"


	public static class equalityExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "equalityExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:271:1: equalityExpression : relationalExpression ( ( EQEQ | BANGEQ ) ^ relationalExpression )* ;
	public final BigDataScriptParser.equalityExpression_return equalityExpression() throws RecognitionException {
		BigDataScriptParser.equalityExpression_return retval = new BigDataScriptParser.equalityExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set77=null;
		ParserRuleReturnScope relationalExpression76 =null;
		ParserRuleReturnScope relationalExpression78 =null;

		Object set77_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:271:20: ( relationalExpression ( ( EQEQ | BANGEQ ) ^ relationalExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:271:22: relationalExpression ( ( EQEQ | BANGEQ ) ^ relationalExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_relationalExpression_in_equalityExpression2091);
			relationalExpression76=relationalExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, relationalExpression76.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:271:43: ( ( EQEQ | BANGEQ ) ^ relationalExpression )*
			loop28:
			do {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==BANGEQ||LA28_0==EQEQ) ) {
					alt28=1;
				}

				switch (alt28) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:271:45: ( EQEQ | BANGEQ ) ^ relationalExpression
					{
					set77=(Token)input.LT(1);
					set77=(Token)input.LT(1);
					if ( input.LA(1)==BANGEQ||input.LA(1)==EQEQ ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(set77)
						, root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_relationalExpression_in_equalityExpression2104);
					relationalExpression78=relationalExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, relationalExpression78.getTree());

					}
					break;

				default :
					break loop28;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "equalityExpression"


	public static class relationalExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "relationalExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:273:1: relationalExpression : additiveExpression ( ( LE | GE | LT | GT ) ^ additiveExpression )* ;
	public final BigDataScriptParser.relationalExpression_return relationalExpression() throws RecognitionException {
		BigDataScriptParser.relationalExpression_return retval = new BigDataScriptParser.relationalExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set80=null;
		ParserRuleReturnScope additiveExpression79 =null;
		ParserRuleReturnScope additiveExpression81 =null;

		Object set80_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:273:22: ( additiveExpression ( ( LE | GE | LT | GT ) ^ additiveExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:273:24: additiveExpression ( ( LE | GE | LT | GT ) ^ additiveExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_additiveExpression_in_relationalExpression2114);
			additiveExpression79=additiveExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, additiveExpression79.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:273:43: ( ( LE | GE | LT | GT ) ^ additiveExpression )*
			loop29:
			do {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( ((LA29_0 >= GE && LA29_0 <= GT)||LA29_0==LE||LA29_0==LT) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:273:44: ( LE | GE | LT | GT ) ^ additiveExpression
					{
					set80=(Token)input.LT(1);
					set80=(Token)input.LT(1);
					if ( (input.LA(1) >= GE && input.LA(1) <= GT)||input.LA(1)==LE||input.LA(1)==LT ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(set80)
						, root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_additiveExpression_in_relationalExpression2134);
					additiveExpression81=additiveExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, additiveExpression81.getTree());

					}
					break;

				default :
					break loop29;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "relationalExpression"


	public static class additiveExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "additiveExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:275:1: additiveExpression : multiplicativeExpression ( ( PLUS | SUB ) ^ multiplicativeExpression )* ;
	public final BigDataScriptParser.additiveExpression_return additiveExpression() throws RecognitionException {
		BigDataScriptParser.additiveExpression_return retval = new BigDataScriptParser.additiveExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set83=null;
		ParserRuleReturnScope multiplicativeExpression82 =null;
		ParserRuleReturnScope multiplicativeExpression84 =null;

		Object set83_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:275:20: ( multiplicativeExpression ( ( PLUS | SUB ) ^ multiplicativeExpression )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:275:22: multiplicativeExpression ( ( PLUS | SUB ) ^ multiplicativeExpression )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression2144);
			multiplicativeExpression82=multiplicativeExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, multiplicativeExpression82.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:275:47: ( ( PLUS | SUB ) ^ multiplicativeExpression )*
			loop30:
			do {
				int alt30=2;
				int LA30_0 = input.LA(1);
				if ( (LA30_0==PLUS||LA30_0==SUB) ) {
					alt30=1;
				}

				switch (alt30) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:275:49: ( PLUS | SUB ) ^ multiplicativeExpression
					{
					set83=(Token)input.LT(1);
					set83=(Token)input.LT(1);
					if ( input.LA(1)==PLUS||input.LA(1)==SUB ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(set83)
						, root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression2157);
					multiplicativeExpression84=multiplicativeExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, multiplicativeExpression84.getTree());

					}
					break;

				default :
					break loop30;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "additiveExpression"


	public static class multiplicativeExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiplicativeExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:277:1: multiplicativeExpression : methodExpresion ( ( STAR | SLASH | PERCENT ) ^ methodExpresion )* ;
	public final BigDataScriptParser.multiplicativeExpression_return multiplicativeExpression() throws RecognitionException {
		BigDataScriptParser.multiplicativeExpression_return retval = new BigDataScriptParser.multiplicativeExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set86=null;
		ParserRuleReturnScope methodExpresion85 =null;
		ParserRuleReturnScope methodExpresion87 =null;

		Object set86_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:277:26: ( methodExpresion ( ( STAR | SLASH | PERCENT ) ^ methodExpresion )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:277:28: methodExpresion ( ( STAR | SLASH | PERCENT ) ^ methodExpresion )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_methodExpresion_in_multiplicativeExpression2167);
			methodExpresion85=methodExpresion();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, methodExpresion85.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:277:44: ( ( STAR | SLASH | PERCENT ) ^ methodExpresion )*
			loop31:
			do {
				int alt31=2;
				int LA31_0 = input.LA(1);
				if ( (LA31_0==PERCENT||LA31_0==SLASH||LA31_0==STAR) ) {
					alt31=1;
				}

				switch (alt31) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:277:46: ( STAR | SLASH | PERCENT ) ^ methodExpresion
					{
					set86=(Token)input.LT(1);
					set86=(Token)input.LT(1);
					if ( input.LA(1)==PERCENT||input.LA(1)==SLASH||input.LA(1)==STAR ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(set86)
						, root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_methodExpresion_in_multiplicativeExpression2184);
					methodExpresion87=methodExpresion();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, methodExpresion87.getTree());

					}
					break;

				default :
					break loop31;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "multiplicativeExpression"


	public static class methodExpresion_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "methodExpresion"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:279:1: methodExpresion : e= listExpresion ( DOT ^ IDENTIFIER LPAREN ! RPAREN !| DOT ^ IDENTIFIER arguments )* ;
	public final BigDataScriptParser.methodExpresion_return methodExpresion() throws RecognitionException {
		BigDataScriptParser.methodExpresion_return retval = new BigDataScriptParser.methodExpresion_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token DOT88=null;
		Token IDENTIFIER89=null;
		Token LPAREN90=null;
		Token RPAREN91=null;
		Token DOT92=null;
		Token IDENTIFIER93=null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope arguments94 =null;

		Object DOT88_tree=null;
		Object IDENTIFIER89_tree=null;
		Object LPAREN90_tree=null;
		Object RPAREN91_tree=null;
		Object DOT92_tree=null;
		Object IDENTIFIER93_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:279:17: (e= listExpresion ( DOT ^ IDENTIFIER LPAREN ! RPAREN !| DOT ^ IDENTIFIER arguments )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:279:19: e= listExpresion ( DOT ^ IDENTIFIER LPAREN ! RPAREN !| DOT ^ IDENTIFIER arguments )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_listExpresion_in_methodExpresion2196);
			e=listExpresion();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:280:3: ( DOT ^ IDENTIFIER LPAREN ! RPAREN !| DOT ^ IDENTIFIER arguments )*
			loop32:
			do {
				int alt32=3;
				int LA32_0 = input.LA(1);
				if ( (LA32_0==DOT) ) {
					int LA32_2 = input.LA(2);
					if ( (LA32_2==IDENTIFIER) ) {
						int LA32_3 = input.LA(3);
						if ( (LA32_3==LPAREN) ) {
							int LA32_4 = input.LA(4);
							if ( (LA32_4==RPAREN) ) {
								int LA32_5 = input.LA(5);
								if ( (synpred45_BigDataScript()) ) {
									alt32=1;
								}
								else if ( (synpred46_BigDataScript()) ) {
									alt32=2;
								}

							}
							else if ( (LA32_4==BANG||LA32_4==FALSE||LA32_4==IDENTIFIER||LA32_4==INT_LITERAL||(LA32_4 >= LBRACE && LA32_4 <= LBRACKET)||LA32_4==LPAREN||LA32_4==PLUS||LA32_4==PLUSPLUS||LA32_4==REAL_LITERAL||(LA32_4 >= STRING_LITERAL && LA32_4 <= SUB)||LA32_4==SUBSUB||LA32_4==SYS_LITERAL||LA32_4==TASK||(LA32_4 >= TILDE && LA32_4 <= TRUE)) ) {
								alt32=2;
							}

						}

					}

				}

				switch (alt32) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:281:3: DOT ^ IDENTIFIER LPAREN ! RPAREN !
					{
					DOT88=(Token)match(input,DOT,FOLLOW_DOT_in_methodExpresion2204); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT88_tree = 
					(Object)adaptor.create(DOT88)
					;
					root_0 = (Object)adaptor.becomeRoot(DOT88_tree, root_0);
					}

					IDENTIFIER89=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodExpresion2207); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IDENTIFIER89_tree = 
					(Object)adaptor.create(IDENTIFIER89)
					;
					adaptor.addChild(root_0, IDENTIFIER89_tree);
					}

					LPAREN90=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_methodExpresion2209); if (state.failed) return retval;
					RPAREN91=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_methodExpresion2212); if (state.failed) return retval;
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:282:5: DOT ^ IDENTIFIER arguments
					{
					DOT92=(Token)match(input,DOT,FOLLOW_DOT_in_methodExpresion2219); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT92_tree = 
					(Object)adaptor.create(DOT92)
					;
					root_0 = (Object)adaptor.becomeRoot(DOT92_tree, root_0);
					}

					IDENTIFIER93=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodExpresion2222); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IDENTIFIER93_tree = 
					(Object)adaptor.create(IDENTIFIER93)
					;
					adaptor.addChild(root_0, IDENTIFIER93_tree);
					}

					pushFollow(FOLLOW_arguments_in_methodExpresion2224);
					arguments94=arguments();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments94.getTree());

					}
					break;

				default :
					break loop32;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "methodExpresion"


	public static class listExpresion_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "listExpresion"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:286:1: listExpresion : e= mapExpresion ( LBRACKET ^ assignmentExpression RBRACKET !)* ;
	public final BigDataScriptParser.listExpresion_return listExpresion() throws RecognitionException {
		BigDataScriptParser.listExpresion_return retval = new BigDataScriptParser.listExpresion_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACKET95=null;
		Token RBRACKET97=null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope assignmentExpression96 =null;

		Object LBRACKET95_tree=null;
		Object RBRACKET97_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:286:15: (e= mapExpresion ( LBRACKET ^ assignmentExpression RBRACKET !)* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:286:17: e= mapExpresion ( LBRACKET ^ assignmentExpression RBRACKET !)*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_mapExpresion_in_listExpresion2242);
			e=mapExpresion();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:286:32: ( LBRACKET ^ assignmentExpression RBRACKET !)*
			loop33:
			do {
				int alt33=2;
				int LA33_0 = input.LA(1);
				if ( (LA33_0==LBRACKET) ) {
					alt33=1;
				}

				switch (alt33) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:286:33: LBRACKET ^ assignmentExpression RBRACKET !
					{
					LBRACKET95=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_listExpresion2245); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LBRACKET95_tree = 
					(Object)adaptor.create(LBRACKET95)
					;
					root_0 = (Object)adaptor.becomeRoot(LBRACKET95_tree, root_0);
					}

					pushFollow(FOLLOW_assignmentExpression_in_listExpresion2248);
					assignmentExpression96=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression96.getTree());

					RBRACKET97=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_listExpresion2250); if (state.failed) return retval;
					}
					break;

				default :
					break loop33;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "listExpresion"


	public static class mapExpresion_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mapExpresion"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:288:1: mapExpresion : e= unaryExpression ( LBRACE ^ assignmentExpression RBRACE !)* ;
	public final BigDataScriptParser.mapExpresion_return mapExpresion() throws RecognitionException {
		BigDataScriptParser.mapExpresion_return retval = new BigDataScriptParser.mapExpresion_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACE98=null;
		Token RBRACE100=null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope assignmentExpression99 =null;

		Object LBRACE98_tree=null;
		Object RBRACE100_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:288:14: (e= unaryExpression ( LBRACE ^ assignmentExpression RBRACE !)* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:288:16: e= unaryExpression ( LBRACE ^ assignmentExpression RBRACE !)*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_unaryExpression_in_mapExpresion2263);
			e=unaryExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:288:34: ( LBRACE ^ assignmentExpression RBRACE !)*
			loop34:
			do {
				int alt34=2;
				int LA34_0 = input.LA(1);
				if ( (LA34_0==LBRACE) ) {
					alt34=1;
				}

				switch (alt34) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:288:35: LBRACE ^ assignmentExpression RBRACE !
					{
					LBRACE98=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_mapExpresion2266); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LBRACE98_tree = 
					(Object)adaptor.create(LBRACE98)
					;
					root_0 = (Object)adaptor.becomeRoot(LBRACE98_tree, root_0);
					}

					pushFollow(FOLLOW_assignmentExpression_in_mapExpresion2269);
					assignmentExpression99=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression99.getTree());

					RBRACE100=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_mapExpresion2271); if (state.failed) return retval;
					}
					break;

				default :
					break loop34;
				}
			} while (true);

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "mapExpresion"


	public static class unaryExpression_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "unaryExpression"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:290:1: unaryExpression : ( PLUS ^ unaryExpression | SUB ^ unaryExpression |op= PLUSPLUS e= unaryExpression -> ^( PRE $op $e) |op= SUBSUB e= unaryExpression -> ^( PRE $op $e) | unaryExpressionNotPlusMinus );
	public final BigDataScriptParser.unaryExpression_return unaryExpression() throws RecognitionException {
		BigDataScriptParser.unaryExpression_return retval = new BigDataScriptParser.unaryExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token op=null;
		Token PLUS101=null;
		Token SUB103=null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope unaryExpression102 =null;
		ParserRuleReturnScope unaryExpression104 =null;
		ParserRuleReturnScope unaryExpressionNotPlusMinus105 =null;

		Object op_tree=null;
		Object PLUS101_tree=null;
		Object SUB103_tree=null;
		RewriteRuleTokenStream stream_SUBSUB=new RewriteRuleTokenStream(adaptor,"token SUBSUB");
		RewriteRuleTokenStream stream_PLUSPLUS=new RewriteRuleTokenStream(adaptor,"token PLUSPLUS");
		RewriteRuleSubtreeStream stream_unaryExpression=new RewriteRuleSubtreeStream(adaptor,"rule unaryExpression");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:291:3: ( PLUS ^ unaryExpression | SUB ^ unaryExpression |op= PLUSPLUS e= unaryExpression -> ^( PRE $op $e) |op= SUBSUB e= unaryExpression -> ^( PRE $op $e) | unaryExpressionNotPlusMinus )
			int alt35=5;
			switch ( input.LA(1) ) {
			case PLUS:
				{
				alt35=1;
				}
				break;
			case SUB:
				{
				alt35=2;
				}
				break;
			case PLUSPLUS:
				{
				alt35=3;
				}
				break;
			case SUBSUB:
				{
				alt35=4;
				}
				break;
			case BANG:
			case FALSE:
			case IDENTIFIER:
			case INT_LITERAL:
			case LBRACE:
			case LBRACKET:
			case LPAREN:
			case REAL_LITERAL:
			case STRING_LITERAL:
			case TILDE:
			case TRUE:
				{
				alt35=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 35, 0, input);
				throw nvae;
			}
			switch (alt35) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:291:5: PLUS ^ unaryExpression
					{
					root_0 = (Object)adaptor.nil();


					PLUS101=(Token)match(input,PLUS,FOLLOW_PLUS_in_unaryExpression2284); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PLUS101_tree = 
					(Object)adaptor.create(PLUS101)
					;
					root_0 = (Object)adaptor.becomeRoot(PLUS101_tree, root_0);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression2287);
					unaryExpression102=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression102.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:292:5: SUB ^ unaryExpression
					{
					root_0 = (Object)adaptor.nil();


					SUB103=(Token)match(input,SUB,FOLLOW_SUB_in_unaryExpression2293); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SUB103_tree = 
					(Object)adaptor.create(SUB103)
					;
					root_0 = (Object)adaptor.becomeRoot(SUB103_tree, root_0);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression2296);
					unaryExpression104=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression104.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:293:5: op= PLUSPLUS e= unaryExpression
					{
					op=(Token)match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpression2308); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PLUSPLUS.add(op);

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression2312);
					e=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_unaryExpression.add(e.getTree());
					// AST REWRITE
					// elements: e, op
					// token labels: op
					// rule labels: retval, e
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 293:37: -> ^( PRE $op $e)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:293:40: ^( PRE $op $e)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(PRE, "PRE")
						, root_1);

						adaptor.addChild(root_1, stream_op.nextNode());

						adaptor.addChild(root_1, stream_e.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:294:5: op= SUBSUB e= unaryExpression
					{
					op=(Token)match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpression2334); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_SUBSUB.add(op);

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression2338);
					e=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_unaryExpression.add(e.getTree());
					// AST REWRITE
					// elements: op, e
					// token labels: op
					// rule labels: retval, e
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 294:35: -> ^( PRE $op $e)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:294:38: ^( PRE $op $e)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(PRE, "PRE")
						, root_1);

						adaptor.addChild(root_1, stream_op.nextNode());

						adaptor.addChild(root_1, stream_e.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:295:5: unaryExpressionNotPlusMinus
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression2358);
					unaryExpressionNotPlusMinus105=unaryExpressionNotPlusMinus();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpressionNotPlusMinus105.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "unaryExpression"


	public static class unaryExpressionNotPlusMinus_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "unaryExpressionNotPlusMinus"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:298:1: unaryExpressionNotPlusMinus : ( TILDE ^ unaryExpression | BANG ^ unaryExpression |e= primary op= SUBSUB -> ^( POST $op $e) |e= primary op= PLUSPLUS -> ^( POST $op $e) | primary );
	public final BigDataScriptParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus() throws RecognitionException {
		BigDataScriptParser.unaryExpressionNotPlusMinus_return retval = new BigDataScriptParser.unaryExpressionNotPlusMinus_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token op=null;
		Token TILDE106=null;
		Token BANG108=null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope unaryExpression107 =null;
		ParserRuleReturnScope unaryExpression109 =null;
		ParserRuleReturnScope primary110 =null;

		Object op_tree=null;
		Object TILDE106_tree=null;
		Object BANG108_tree=null;
		RewriteRuleTokenStream stream_SUBSUB=new RewriteRuleTokenStream(adaptor,"token SUBSUB");
		RewriteRuleTokenStream stream_PLUSPLUS=new RewriteRuleTokenStream(adaptor,"token PLUSPLUS");
		RewriteRuleSubtreeStream stream_primary=new RewriteRuleSubtreeStream(adaptor,"rule primary");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:299:3: ( TILDE ^ unaryExpression | BANG ^ unaryExpression |e= primary op= SUBSUB -> ^( POST $op $e) |e= primary op= PLUSPLUS -> ^( POST $op $e) | primary )
			int alt36=5;
			switch ( input.LA(1) ) {
			case TILDE:
				{
				alt36=1;
				}
				break;
			case BANG:
				{
				alt36=2;
				}
				break;
			case LPAREN:
				{
				int LA36_3 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case IDENTIFIER:
				{
				int LA36_4 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case INT_LITERAL:
				{
				int LA36_5 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case REAL_LITERAL:
				{
				int LA36_6 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case STRING_LITERAL:
				{
				int LA36_7 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case LBRACKET:
				{
				int LA36_8 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case LBRACE:
				{
				int LA36_9 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TRUE:
				{
				int LA36_10 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case FALSE:
				{
				int LA36_11 = input.LA(2);
				if ( (synpred55_BigDataScript()) ) {
					alt36=3;
				}
				else if ( (synpred56_BigDataScript()) ) {
					alt36=4;
				}
				else if ( (true) ) {
					alt36=5;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 11, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 36, 0, input);
				throw nvae;
			}
			switch (alt36) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:299:5: TILDE ^ unaryExpression
					{
					root_0 = (Object)adaptor.nil();


					TILDE106=(Token)match(input,TILDE,FOLLOW_TILDE_in_unaryExpressionNotPlusMinus2371); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					TILDE106_tree = 
					(Object)adaptor.create(TILDE106)
					;
					root_0 = (Object)adaptor.becomeRoot(TILDE106_tree, root_0);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus2374);
					unaryExpression107=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression107.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:300:5: BANG ^ unaryExpression
					{
					root_0 = (Object)adaptor.nil();


					BANG108=(Token)match(input,BANG,FOLLOW_BANG_in_unaryExpressionNotPlusMinus2380); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					BANG108_tree = 
					(Object)adaptor.create(BANG108)
					;
					root_0 = (Object)adaptor.becomeRoot(BANG108_tree, root_0);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus2383);
					unaryExpression109=unaryExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression109.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:301:5: e= primary op= SUBSUB
					{
					pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus2391);
					e=primary();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_primary.add(e.getTree());
					op=(Token)match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpressionNotPlusMinus2395); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_SUBSUB.add(op);

					// AST REWRITE
					// elements: op, e
					// token labels: op
					// rule labels: retval, e
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 301:28: -> ^( POST $op $e)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:301:31: ^( POST $op $e)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(POST, "POST")
						, root_1);

						adaptor.addChild(root_1, stream_op.nextNode());

						adaptor.addChild(root_1, stream_e.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:302:5: e= primary op= PLUSPLUS
					{
					pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus2418);
					e=primary();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_primary.add(e.getTree());
					op=(Token)match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpressionNotPlusMinus2422); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PLUSPLUS.add(op);

					// AST REWRITE
					// elements: e, op
					// token labels: op
					// rule labels: retval, e
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 302:29: -> ^( POST $op $e)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:302:32: ^( POST $op $e)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(POST, "POST")
						, root_1);

						adaptor.addChild(root_1, stream_op.nextNode());

						adaptor.addChild(root_1, stream_e.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:303:5: primary
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus2442);
					primary110=primary();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, primary110.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "unaryExpressionNotPlusMinus"


	public static class primary_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "primary"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:306:1: primary : ( parExpression | functionCall |id= IDENTIFIER -> ^( NAME $id) |li= INT_LITERAL -> ^( LITERAL_INT $li) |lr= REAL_LITERAL -> ^( LITERAL_REAL $lr) |ls= STRING_LITERAL -> ^( LITERAL_STRING $ls) | list | map |lbt= TRUE -> ^( LITERAL_BOOL $lbt) |lbf= FALSE -> ^( LITERAL_BOOL $lbf) );
	public final BigDataScriptParser.primary_return primary() throws RecognitionException {
		BigDataScriptParser.primary_return retval = new BigDataScriptParser.primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token id=null;
		Token li=null;
		Token lr=null;
		Token ls=null;
		Token lbt=null;
		Token lbf=null;
		ParserRuleReturnScope parExpression111 =null;
		ParserRuleReturnScope functionCall112 =null;
		ParserRuleReturnScope list113 =null;
		ParserRuleReturnScope map114 =null;

		Object id_tree=null;
		Object li_tree=null;
		Object lr_tree=null;
		Object ls_tree=null;
		Object lbt_tree=null;
		Object lbf_tree=null;
		RewriteRuleTokenStream stream_INT_LITERAL=new RewriteRuleTokenStream(adaptor,"token INT_LITERAL");
		RewriteRuleTokenStream stream_STRING_LITERAL=new RewriteRuleTokenStream(adaptor,"token STRING_LITERAL");
		RewriteRuleTokenStream stream_REAL_LITERAL=new RewriteRuleTokenStream(adaptor,"token REAL_LITERAL");
		RewriteRuleTokenStream stream_FALSE=new RewriteRuleTokenStream(adaptor,"token FALSE");
		RewriteRuleTokenStream stream_TRUE=new RewriteRuleTokenStream(adaptor,"token TRUE");
		RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:307:3: ( parExpression | functionCall |id= IDENTIFIER -> ^( NAME $id) |li= INT_LITERAL -> ^( LITERAL_INT $li) |lr= REAL_LITERAL -> ^( LITERAL_REAL $lr) |ls= STRING_LITERAL -> ^( LITERAL_STRING $ls) | list | map |lbt= TRUE -> ^( LITERAL_BOOL $lbt) |lbf= FALSE -> ^( LITERAL_BOOL $lbf) )
			int alt37=10;
			switch ( input.LA(1) ) {
			case LPAREN:
				{
				alt37=1;
				}
				break;
			case IDENTIFIER:
				{
				int LA37_2 = input.LA(2);
				if ( (LA37_2==LPAREN) ) {
					alt37=2;
				}
				else if ( (LA37_2==EOF||(LA37_2 >= AMP && LA37_2 <= AMPAMP)||LA37_2==ARROW||(LA37_2 >= BANGEQ && LA37_2 <= BARBAR)||LA37_2==CARET||(LA37_2 >= COLON && LA37_2 <= COMMA)||LA37_2==DOT||(LA37_2 >= EQ && LA37_2 <= EQEQ)||(LA37_2 >= GE && LA37_2 <= GT)||(LA37_2 >= LBRACE && LA37_2 <= LE)||LA37_2==LT||LA37_2==NEWLINE||LA37_2==PERCENT||LA37_2==PLUS||LA37_2==PLUSPLUS||(LA37_2 >= QUES && LA37_2 <= RBRACKET)||(LA37_2 >= RPAREN && LA37_2 <= SLASH)||LA37_2==STAR||LA37_2==SUB||LA37_2==SUBSUB) ) {
					alt37=3;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 37, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case INT_LITERAL:
				{
				alt37=4;
				}
				break;
			case REAL_LITERAL:
				{
				alt37=5;
				}
				break;
			case STRING_LITERAL:
				{
				alt37=6;
				}
				break;
			case LBRACKET:
				{
				alt37=7;
				}
				break;
			case LBRACE:
				{
				alt37=8;
				}
				break;
			case TRUE:
				{
				alt37=9;
				}
				break;
			case FALSE:
				{
				alt37=10;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 37, 0, input);
				throw nvae;
			}
			switch (alt37) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:307:5: parExpression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parExpression_in_primary2455);
					parExpression111=parExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, parExpression111.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:308:5: functionCall
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functionCall_in_primary2461);
					functionCall112=functionCall();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functionCall112.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:309:5: id= IDENTIFIER
					{
					id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary2469); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_IDENTIFIER.add(id);

					// AST REWRITE
					// elements: id
					// token labels: id
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_id=new RewriteRuleTokenStream(adaptor,"token id",id);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 309:22: -> ^( NAME $id)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:309:25: ^( NAME $id)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(NAME, "NAME")
						, root_1);

						adaptor.addChild(root_1, stream_id.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:310:5: li= INT_LITERAL
					{
					li=(Token)match(input,INT_LITERAL,FOLLOW_INT_LITERAL_in_primary2489); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_INT_LITERAL.add(li);

					// AST REWRITE
					// elements: li
					// token labels: li
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_li=new RewriteRuleTokenStream(adaptor,"token li",li);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 310:23: -> ^( LITERAL_INT $li)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:310:26: ^( LITERAL_INT $li)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(LITERAL_INT, "LITERAL_INT")
						, root_1);

						adaptor.addChild(root_1, stream_li.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:311:5: lr= REAL_LITERAL
					{
					lr=(Token)match(input,REAL_LITERAL,FOLLOW_REAL_LITERAL_in_primary2509); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_REAL_LITERAL.add(lr);

					// AST REWRITE
					// elements: lr
					// token labels: lr
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_lr=new RewriteRuleTokenStream(adaptor,"token lr",lr);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 311:24: -> ^( LITERAL_REAL $lr)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:311:27: ^( LITERAL_REAL $lr)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(LITERAL_REAL, "LITERAL_REAL")
						, root_1);

						adaptor.addChild(root_1, stream_lr.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:312:5: ls= STRING_LITERAL
					{
					ls=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_primary2529); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_STRING_LITERAL.add(ls);

					// AST REWRITE
					// elements: ls
					// token labels: ls
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_ls=new RewriteRuleTokenStream(adaptor,"token ls",ls);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 312:26: -> ^( LITERAL_STRING $ls)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:312:29: ^( LITERAL_STRING $ls)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(LITERAL_STRING, "LITERAL_STRING")
						, root_1);

						adaptor.addChild(root_1, stream_ls.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 7 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:313:5: list
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_list_in_primary2547);
					list113=list();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list113.getTree());

					}
					break;
				case 8 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:314:5: map
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_in_primary2553);
					map114=map();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map114.getTree());

					}
					break;
				case 9 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:315:5: lbt= TRUE
					{
					lbt=(Token)match(input,TRUE,FOLLOW_TRUE_in_primary2561); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TRUE.add(lbt);

					// AST REWRITE
					// elements: lbt
					// token labels: lbt
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_lbt=new RewriteRuleTokenStream(adaptor,"token lbt",lbt);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 315:19: -> ^( LITERAL_BOOL $lbt)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:315:22: ^( LITERAL_BOOL $lbt)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(LITERAL_BOOL, "LITERAL_BOOL")
						, root_1);

						adaptor.addChild(root_1, stream_lbt.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 10 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:316:5: lbf= FALSE
					{
					lbf=(Token)match(input,FALSE,FOLLOW_FALSE_in_primary2583); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FALSE.add(lbf);

					// AST REWRITE
					// elements: lbf
					// token labels: lbf
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_lbf=new RewriteRuleTokenStream(adaptor,"token lbf",lbf);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 316:19: -> ^( LITERAL_BOOL $lbf)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:316:22: ^( LITERAL_BOOL $lbf)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(LITERAL_BOOL, "LITERAL_BOOL")
						, root_1);

						adaptor.addChild(root_1, stream_lbf.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "primary"


	public static class list_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "list"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:1: list : LBRACKET (e= expressionList )? RBRACKET -> ^( LITERAL_LIST $e) ;
	public final BigDataScriptParser.list_return list() throws RecognitionException {
		BigDataScriptParser.list_return retval = new BigDataScriptParser.list_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACKET115=null;
		Token RBRACKET116=null;
		ParserRuleReturnScope e =null;

		Object LBRACKET115_tree=null;
		Object RBRACKET116_tree=null;
		RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
		RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
		RewriteRuleSubtreeStream stream_expressionList=new RewriteRuleSubtreeStream(adaptor,"rule expressionList");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:6: ( LBRACKET (e= expressionList )? RBRACKET -> ^( LITERAL_LIST $e) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:9: LBRACKET (e= expressionList )? RBRACKET
			{
			LBRACKET115=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_list2609); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET115);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:18: (e= expressionList )?
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==BANG||LA38_0==FALSE||LA38_0==IDENTIFIER||LA38_0==INT_LITERAL||(LA38_0 >= LBRACE && LA38_0 <= LBRACKET)||LA38_0==LPAREN||LA38_0==PLUS||LA38_0==PLUSPLUS||LA38_0==REAL_LITERAL||(LA38_0 >= STRING_LITERAL && LA38_0 <= SUB)||LA38_0==SUBSUB||LA38_0==SYS_LITERAL||LA38_0==TASK||(LA38_0 >= TILDE && LA38_0 <= TRUE)) ) {
				alt38=1;
			}
			switch (alt38) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:19: e= expressionList
					{
					pushFollow(FOLLOW_expressionList_in_list2614);
					e=expressionList();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expressionList.add(e.getTree());
					}
					break;

			}

			RBRACKET116=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_list2618); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET116);

			// AST REWRITE
			// elements: e
			// token labels: 
			// rule labels: retval, e
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 320:51: -> ^( LITERAL_LIST $e)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:320:54: ^( LITERAL_LIST $e)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(LITERAL_LIST, "LITERAL_LIST")
				, root_1);

				adaptor.addChild(root_1, stream_e.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list"


	public static class map_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "map"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:1: map : LBRACE (mt+= mapTuple ( COMMA mt+= mapTuple )* )? RBRACE -> ^( LITERAL_MAP ( $mt)* ) ;
	public final BigDataScriptParser.map_return map() throws RecognitionException {
		BigDataScriptParser.map_return retval = new BigDataScriptParser.map_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACE117=null;
		Token COMMA118=null;
		Token RBRACE119=null;
		List<Object> list_mt=null;
		RuleReturnScope mt = null;
		Object LBRACE117_tree=null;
		Object COMMA118_tree=null;
		Object RBRACE119_tree=null;
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleSubtreeStream stream_mapTuple=new RewriteRuleSubtreeStream(adaptor,"rule mapTuple");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:5: ( LBRACE (mt+= mapTuple ( COMMA mt+= mapTuple )* )? RBRACE -> ^( LITERAL_MAP ( $mt)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:8: LBRACE (mt+= mapTuple ( COMMA mt+= mapTuple )* )? RBRACE
			{
			LBRACE117=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_map2641); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACE.add(LBRACE117);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:16: (mt+= mapTuple ( COMMA mt+= mapTuple )* )?
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==BANG||LA40_0==FALSE||LA40_0==IDENTIFIER||LA40_0==INT_LITERAL||(LA40_0 >= LBRACE && LA40_0 <= LBRACKET)||LA40_0==LPAREN||LA40_0==PLUS||LA40_0==PLUSPLUS||LA40_0==REAL_LITERAL||(LA40_0 >= STRING_LITERAL && LA40_0 <= SUB)||LA40_0==SUBSUB||LA40_0==SYS_LITERAL||LA40_0==TASK||(LA40_0 >= TILDE && LA40_0 <= TRUE)) ) {
				alt40=1;
			}
			switch (alt40) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:17: mt+= mapTuple ( COMMA mt+= mapTuple )*
					{
					pushFollow(FOLLOW_mapTuple_in_map2647);
					mt=mapTuple();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_mapTuple.add(mt.getTree());
					if (list_mt==null) list_mt=new ArrayList<Object>();
					list_mt.add(mt.getTree());
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:30: ( COMMA mt+= mapTuple )*
					loop39:
					do {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0==COMMA) ) {
							alt39=1;
						}

						switch (alt39) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:31: COMMA mt+= mapTuple
							{
							COMMA118=(Token)match(input,COMMA,FOLLOW_COMMA_in_map2650); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_COMMA.add(COMMA118);

							pushFollow(FOLLOW_mapTuple_in_map2654);
							mt=mapTuple();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_mapTuple.add(mt.getTree());
							if (list_mt==null) list_mt=new ArrayList<Object>();
							list_mt.add(mt.getTree());
							}
							break;

						default :
							break loop39;
						}
					} while (true);

					}
					break;

			}

			RBRACE119=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_map2662); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE119);

			// AST REWRITE
			// elements: mt
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: mt
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_mt=new RewriteRuleSubtreeStream(adaptor,"token mt",list_mt);
			root_0 = (Object)adaptor.nil();
			// 322:65: -> ^( LITERAL_MAP ( $mt)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:68: ^( LITERAL_MAP ( $mt)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(LITERAL_MAP, "LITERAL_MAP")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:322:83: ( $mt)*
				while ( stream_mt.hasNext() ) {
					adaptor.addChild(root_1, stream_mt.nextTree());

				}
				stream_mt.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "map"


	public static class mapTuple_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mapTuple"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:324:1: mapTuple : assignmentExpression ARROW ^ assignmentExpression ;
	public final BigDataScriptParser.mapTuple_return mapTuple() throws RecognitionException {
		BigDataScriptParser.mapTuple_return retval = new BigDataScriptParser.mapTuple_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ARROW121=null;
		ParserRuleReturnScope assignmentExpression120 =null;
		ParserRuleReturnScope assignmentExpression122 =null;

		Object ARROW121_tree=null;

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:324:10: ( assignmentExpression ARROW ^ assignmentExpression )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:324:13: assignmentExpression ARROW ^ assignmentExpression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_assignmentExpression_in_mapTuple2684);
			assignmentExpression120=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression120.getTree());

			ARROW121=(Token)match(input,ARROW,FOLLOW_ARROW_in_mapTuple2686); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ARROW121_tree = 
			(Object)adaptor.create(ARROW121)
			;
			root_0 = (Object)adaptor.becomeRoot(ARROW121_tree, root_0);
			}

			pushFollow(FOLLOW_assignmentExpression_in_mapTuple2689);
			assignmentExpression122=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression122.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "mapTuple"


	public static class arguments_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arguments"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:1: arguments : LPAREN (e= expressionList )? RPAREN -> ^( ARGS $e) ;
	public final BigDataScriptParser.arguments_return arguments() throws RecognitionException {
		BigDataScriptParser.arguments_return retval = new BigDataScriptParser.arguments_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN123=null;
		Token RPAREN124=null;
		ParserRuleReturnScope e =null;

		Object LPAREN123_tree=null;
		Object RPAREN124_tree=null;
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_expressionList=new RewriteRuleSubtreeStream(adaptor,"rule expressionList");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:11: ( LPAREN (e= expressionList )? RPAREN -> ^( ARGS $e) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:14: LPAREN (e= expressionList )? RPAREN
			{
			LPAREN123=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arguments2698); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN123);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:21: (e= expressionList )?
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==BANG||LA41_0==FALSE||LA41_0==IDENTIFIER||LA41_0==INT_LITERAL||(LA41_0 >= LBRACE && LA41_0 <= LBRACKET)||LA41_0==LPAREN||LA41_0==PLUS||LA41_0==PLUSPLUS||LA41_0==REAL_LITERAL||(LA41_0 >= STRING_LITERAL && LA41_0 <= SUB)||LA41_0==SUBSUB||LA41_0==SYS_LITERAL||LA41_0==TASK||(LA41_0 >= TILDE && LA41_0 <= TRUE)) ) {
				alt41=1;
			}
			switch (alt41) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:22: e= expressionList
					{
					pushFollow(FOLLOW_expressionList_in_arguments2703);
					e=expressionList();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expressionList.add(e.getTree());
					}
					break;

			}

			RPAREN124=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arguments2707); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN124);

			// AST REWRITE
			// elements: e
			// token labels: 
			// rule labels: retval, e
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 326:52: -> ^( ARGS $e)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:326:55: ^( ARGS $e)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(ARGS, "ARGS")
				, root_1);

				adaptor.addChild(root_1, stream_e.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arguments"


	public static class functionCall_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functionCall"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:1: functionCall : (id= IDENTIFIER LPAREN RPAREN -> ^( FUNCTION_CALL $id) |id= IDENTIFIER arg= arguments -> ^( FUNCTION_CALL $id $arg) );
	public final BigDataScriptParser.functionCall_return functionCall() throws RecognitionException {
		BigDataScriptParser.functionCall_return retval = new BigDataScriptParser.functionCall_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token id=null;
		Token LPAREN125=null;
		Token RPAREN126=null;
		ParserRuleReturnScope arg =null;

		Object id_tree=null;
		Object LPAREN125_tree=null;
		Object RPAREN126_tree=null;
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_arguments=new RewriteRuleSubtreeStream(adaptor,"rule arguments");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:14: (id= IDENTIFIER LPAREN RPAREN -> ^( FUNCTION_CALL $id) |id= IDENTIFIER arg= arguments -> ^( FUNCTION_CALL $id $arg) )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==IDENTIFIER) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==LPAREN) ) {
					int LA42_2 = input.LA(3);
					if ( (LA42_2==RPAREN) ) {
						int LA42_3 = input.LA(4);
						if ( (synpred70_BigDataScript()) ) {
							alt42=1;
						}
						else if ( (true) ) {
							alt42=2;
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 42, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA42_2==BANG||LA42_2==FALSE||LA42_2==IDENTIFIER||LA42_2==INT_LITERAL||(LA42_2 >= LBRACE && LA42_2 <= LBRACKET)||LA42_2==LPAREN||LA42_2==PLUS||LA42_2==PLUSPLUS||LA42_2==REAL_LITERAL||(LA42_2 >= STRING_LITERAL && LA42_2 <= SUB)||LA42_2==SUBSUB||LA42_2==SYS_LITERAL||LA42_2==TASK||(LA42_2 >= TILDE && LA42_2 <= TRUE)) ) {
						alt42=2;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 42, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 42, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 42, 0, input);
				throw nvae;
			}
			switch (alt42) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:16: id= IDENTIFIER LPAREN RPAREN
					{
					id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_functionCall2731); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_IDENTIFIER.add(id);

					LPAREN125=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functionCall2733); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN125);

					RPAREN126=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functionCall2735); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN126);

					// AST REWRITE
					// elements: id
					// token labels: id
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_id=new RewriteRuleTokenStream(adaptor,"token id",id);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 328:46: -> ^( FUNCTION_CALL $id)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:49: ^( FUNCTION_CALL $id)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FUNCTION_CALL, "FUNCTION_CALL")
						, root_1);

						adaptor.addChild(root_1, stream_id.nextNode());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:329:5: id= IDENTIFIER arg= arguments
					{
					id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_functionCall2755); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_IDENTIFIER.add(id);

					pushFollow(FOLLOW_arguments_in_functionCall2759);
					arg=arguments();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_arguments.add(arg.getTree());
					// AST REWRITE
					// elements: id, arg
					// token labels: id
					// rule labels: arg, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleTokenStream stream_id=new RewriteRuleTokenStream(adaptor,"token id",id);
					RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg",arg!=null?arg.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 329:35: -> ^( FUNCTION_CALL $id $arg)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:329:38: ^( FUNCTION_CALL $id $arg)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FUNCTION_CALL, "FUNCTION_CALL")
						, root_1);

						adaptor.addChild(root_1, stream_id.nextNode());

						adaptor.addChild(root_1, stream_arg.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "functionCall"


	public static class block_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "block"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:1: block : LBRACE (st+= blockStatement )* RBRACE -> ^( BLOCK ( $st)* ) ;
	public final BigDataScriptParser.block_return block() throws RecognitionException {
		BigDataScriptParser.block_return retval = new BigDataScriptParser.block_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACE127=null;
		Token RBRACE128=null;
		List<Object> list_st=null;
		RuleReturnScope st = null;
		Object LBRACE127_tree=null;
		Object RBRACE128_tree=null;
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleSubtreeStream stream_blockStatement=new RewriteRuleSubtreeStream(adaptor,"rule blockStatement");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:7: ( LBRACE (st+= blockStatement )* RBRACE -> ^( BLOCK ( $st)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:9: LBRACE (st+= blockStatement )* RBRACE
			{
			LBRACE127=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_block2787); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACE.add(LBRACE127);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:16: (st+= blockStatement )*
			loop43:
			do {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==BANG||(LA43_0 >= BOOL && LA43_0 <= BREAK)||LA43_0==CHECKPOINT||LA43_0==CONTINUE||LA43_0==EXIT||(LA43_0 >= FALSE && LA43_0 <= FOR)||(LA43_0 >= IDENTIFIER && LA43_0 <= IF)||(LA43_0 >= INT && LA43_0 <= INT_LITERAL)||(LA43_0 >= LBRACE && LA43_0 <= LBRACKET)||LA43_0==LPAREN||LA43_0==NEWLINE||LA43_0==PLUS||LA43_0==PLUSPLUS||(LA43_0 >= REAL && LA43_0 <= RETURN)||LA43_0==SEMI||(LA43_0 >= STRING && LA43_0 <= SUB)||LA43_0==SUBSUB||LA43_0==SYS_LITERAL||LA43_0==TASK||(LA43_0 >= TILDE && LA43_0 <= TRUE)||(LA43_0 >= VOID && LA43_0 <= WHILE)) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:17: st+= blockStatement
					{
					pushFollow(FOLLOW_blockStatement_in_block2792);
					st=blockStatement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_blockStatement.add(st.getTree());
					if (list_st==null) list_st=new ArrayList<Object>();
					list_st.add(st.getTree());
					}
					break;

				default :
					break loop43;
				}
			} while (true);

			RBRACE128=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_block2796); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE128);

			// AST REWRITE
			// elements: st
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: st
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_st=new RewriteRuleSubtreeStream(adaptor,"token st",list_st);
			root_0 = (Object)adaptor.nil();
			// 335:48: -> ^( BLOCK ( $st)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:51: ^( BLOCK ( $st)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(BLOCK, "BLOCK")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:335:60: ( $st)*
				while ( stream_st.hasNext() ) {
					adaptor.addChild(root_1, stream_st.nextTree());

				}
				stream_st.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "block"


	public static class blockStatement_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "blockStatement"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:337:1: blockStatement : ( localVariableDeclarationStatement | statement );
	public final BigDataScriptParser.blockStatement_return blockStatement() throws RecognitionException {
		BigDataScriptParser.blockStatement_return retval = new BigDataScriptParser.blockStatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope localVariableDeclarationStatement129 =null;
		ParserRuleReturnScope statement130 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:337:16: ( localVariableDeclarationStatement | statement )
			int alt44=2;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==BOOL||LA44_0==INT||LA44_0==REAL||LA44_0==STRING||LA44_0==VOID) ) {
				switch ( input.LA(2) ) {
				case IDENTIFIER:
					{
					int LA44_3 = input.LA(3);
					if ( (LA44_3==COMMA||LA44_3==EQ||LA44_3==NEWLINE||LA44_3==SEMI) ) {
						alt44=1;
					}
					else if ( (LA44_3==LPAREN) ) {
						alt44=2;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 44, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACKET:
					{
					int LA44_4 = input.LA(3);
					if ( (LA44_4==RBRACKET) ) {
						int LA44_7 = input.LA(4);
						if ( (LA44_7==IDENTIFIER) ) {
							int LA44_3 = input.LA(5);
							if ( (LA44_3==COMMA||LA44_3==EQ||LA44_3==NEWLINE||LA44_3==SEMI) ) {
								alt44=1;
							}
							else if ( (LA44_3==LPAREN) ) {
								alt44=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 44, 3, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 44, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 44, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case LBRACE:
					{
					int LA44_5 = input.LA(3);
					if ( (LA44_5==RBRACE) ) {
						int LA44_8 = input.LA(4);
						if ( (LA44_8==IDENTIFIER) ) {
							int LA44_3 = input.LA(5);
							if ( (LA44_3==COMMA||LA44_3==EQ||LA44_3==NEWLINE||LA44_3==SEMI) ) {
								alt44=1;
							}
							else if ( (LA44_3==LPAREN) ) {
								alt44=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 44, 3, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 44, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA44_5==BOOL||LA44_5==INT||LA44_5==REAL||LA44_5==STRING||LA44_5==VOID) ) {
						int LA44_9 = input.LA(4);
						if ( (LA44_9==RBRACE) ) {
							int LA44_10 = input.LA(5);
							if ( (LA44_10==IDENTIFIER) ) {
								int LA44_3 = input.LA(6);
								if ( (LA44_3==COMMA||LA44_3==EQ||LA44_3==NEWLINE||LA44_3==SEMI) ) {
									alt44=1;
								}
								else if ( (LA44_3==LPAREN) ) {
									alt44=2;
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 44, 3, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 44, 10, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 44, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 44, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 44, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA44_0==BANG||LA44_0==BREAK||LA44_0==CHECKPOINT||LA44_0==CONTINUE||LA44_0==EXIT||(LA44_0 >= FALSE && LA44_0 <= FOR)||(LA44_0 >= IDENTIFIER && LA44_0 <= IF)||LA44_0==INT_LITERAL||(LA44_0 >= LBRACE && LA44_0 <= LBRACKET)||LA44_0==LPAREN||LA44_0==NEWLINE||LA44_0==PLUS||LA44_0==PLUSPLUS||(LA44_0 >= REAL_LITERAL && LA44_0 <= RETURN)||LA44_0==SEMI||(LA44_0 >= STRING_LITERAL && LA44_0 <= SUB)||LA44_0==SUBSUB||LA44_0==SYS_LITERAL||LA44_0==TASK||(LA44_0 >= TILDE && LA44_0 <= TRUE)||(LA44_0 >= WAIT && LA44_0 <= WHILE)) ) {
				alt44=2;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 44, 0, input);
				throw nvae;
			}
			switch (alt44) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:337:18: localVariableDeclarationStatement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement2817);
					localVariableDeclarationStatement129=localVariableDeclarationStatement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, localVariableDeclarationStatement129.getTree());

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:337:54: statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_statement_in_blockStatement2821);
					statement130=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, statement130.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "blockStatement"


	public static class localVariableDeclarationStatement_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "localVariableDeclarationStatement"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:339:1: localVariableDeclarationStatement : localVariableDeclaration semi !;
	public final BigDataScriptParser.localVariableDeclarationStatement_return localVariableDeclarationStatement() throws RecognitionException {
		BigDataScriptParser.localVariableDeclarationStatement_return retval = new BigDataScriptParser.localVariableDeclarationStatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope localVariableDeclaration131 =null;
		ParserRuleReturnScope semi132 =null;


		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:339:35: ( localVariableDeclaration semi !)
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:339:37: localVariableDeclaration semi !
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement2830);
			localVariableDeclaration131=localVariableDeclaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, localVariableDeclaration131.getTree());

			pushFollow(FOLLOW_semi_in_localVariableDeclarationStatement2832);
			semi132=semi();
			state._fsp--;
			if (state.failed) return retval;
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "localVariableDeclarationStatement"


	public static class localVariableDeclaration_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "localVariableDeclaration"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:1: localVariableDeclaration : t= type v+= variableInit ( COMMA v+= variableInit )* -> ^( VAR_DECLARATION $t ( $v)* ) ;
	public final BigDataScriptParser.localVariableDeclaration_return localVariableDeclaration() throws RecognitionException {
		BigDataScriptParser.localVariableDeclaration_return retval = new BigDataScriptParser.localVariableDeclaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA133=null;
		List<Object> list_v=null;
		ParserRuleReturnScope t =null;
		RuleReturnScope v = null;
		Object COMMA133_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
		RewriteRuleSubtreeStream stream_variableInit=new RewriteRuleSubtreeStream(adaptor,"rule variableInit");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:26: (t= type v+= variableInit ( COMMA v+= variableInit )* -> ^( VAR_DECLARATION $t ( $v)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:28: t= type v+= variableInit ( COMMA v+= variableInit )*
			{
			pushFollow(FOLLOW_type_in_localVariableDeclaration2844);
			t=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(t.getTree());
			pushFollow(FOLLOW_variableInit_in_localVariableDeclaration2848);
			v=variableInit();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_variableInit.add(v.getTree());
			if (list_v==null) list_v=new ArrayList<Object>();
			list_v.add(v.getTree());
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:51: ( COMMA v+= variableInit )*
			loop45:
			do {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==COMMA) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:52: COMMA v+= variableInit
					{
					COMMA133=(Token)match(input,COMMA,FOLLOW_COMMA_in_localVariableDeclaration2851); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COMMA.add(COMMA133);

					pushFollow(FOLLOW_variableInit_in_localVariableDeclaration2855);
					v=variableInit();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_variableInit.add(v.getTree());
					if (list_v==null) list_v=new ArrayList<Object>();
					list_v.add(v.getTree());
					}
					break;

				default :
					break loop45;
				}
			} while (true);

			// AST REWRITE
			// elements: t, v
			// token labels: 
			// rule labels: retval, t
			// token list labels: 
			// rule list labels: v
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);
			RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"token v",list_v);
			root_0 = (Object)adaptor.nil();
			// 341:77: -> ^( VAR_DECLARATION $t ( $v)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:80: ^( VAR_DECLARATION $t ( $v)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_DECLARATION, "VAR_DECLARATION")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:341:102: ( $v)*
				while ( stream_v.hasNext() ) {
					adaptor.addChild(root_1, stream_v.nextTree());

				}
				stream_v.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "localVariableDeclaration"


	public static class statement_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "statement"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:343:1: statement : ( assignmentExpression ^ semi !| block | BREAK ^ semi !| CONTINUE ^ semi !| CHECKPOINT ^ ( assignmentExpression )? semi !| EXIT ^ assignmentExpression semi !| forstatement | IF c= parExpression st= statement ( ELSE se= statement )? -> ^( IF $c $st ( $se)? ) | methodDeclaration | RETURN ^ ( assignmentExpression )? semi !| WAIT ^ semi !| WHILE c= parExpression s= statement -> ^( WHILE $c $s) | semi !);
	public final BigDataScriptParser.statement_return statement() throws RecognitionException {
		BigDataScriptParser.statement_return retval = new BigDataScriptParser.statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token BREAK137=null;
		Token CONTINUE139=null;
		Token CHECKPOINT141=null;
		Token EXIT144=null;
		Token IF148=null;
		Token ELSE149=null;
		Token RETURN151=null;
		Token WAIT154=null;
		Token WHILE156=null;
		ParserRuleReturnScope c =null;
		ParserRuleReturnScope st =null;
		ParserRuleReturnScope se =null;
		ParserRuleReturnScope s =null;
		ParserRuleReturnScope assignmentExpression134 =null;
		ParserRuleReturnScope semi135 =null;
		ParserRuleReturnScope block136 =null;
		ParserRuleReturnScope semi138 =null;
		ParserRuleReturnScope semi140 =null;
		ParserRuleReturnScope assignmentExpression142 =null;
		ParserRuleReturnScope semi143 =null;
		ParserRuleReturnScope assignmentExpression145 =null;
		ParserRuleReturnScope semi146 =null;
		ParserRuleReturnScope forstatement147 =null;
		ParserRuleReturnScope methodDeclaration150 =null;
		ParserRuleReturnScope assignmentExpression152 =null;
		ParserRuleReturnScope semi153 =null;
		ParserRuleReturnScope semi155 =null;
		ParserRuleReturnScope semi157 =null;

		Object BREAK137_tree=null;
		Object CONTINUE139_tree=null;
		Object CHECKPOINT141_tree=null;
		Object EXIT144_tree=null;
		Object IF148_tree=null;
		Object ELSE149_tree=null;
		Object RETURN151_tree=null;
		Object WAIT154_tree=null;
		Object WHILE156_tree=null;
		RewriteRuleTokenStream stream_WHILE=new RewriteRuleTokenStream(adaptor,"token WHILE");
		RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
		RewriteRuleTokenStream stream_ELSE=new RewriteRuleTokenStream(adaptor,"token ELSE");
		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
		RewriteRuleSubtreeStream stream_parExpression=new RewriteRuleSubtreeStream(adaptor,"rule parExpression");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:344:3: ( assignmentExpression ^ semi !| block | BREAK ^ semi !| CONTINUE ^ semi !| CHECKPOINT ^ ( assignmentExpression )? semi !| EXIT ^ assignmentExpression semi !| forstatement | IF c= parExpression st= statement ( ELSE se= statement )? -> ^( IF $c $st ( $se)? ) | methodDeclaration | RETURN ^ ( assignmentExpression )? semi !| WAIT ^ semi !| WHILE c= parExpression s= statement -> ^( WHILE $c $s) | semi !)
			int alt49=13;
			switch ( input.LA(1) ) {
			case BANG:
			case FALSE:
			case IDENTIFIER:
			case INT_LITERAL:
			case LBRACKET:
			case LPAREN:
			case PLUS:
			case PLUSPLUS:
			case REAL_LITERAL:
			case STRING_LITERAL:
			case SUB:
			case SUBSUB:
			case SYS_LITERAL:
			case TASK:
			case TILDE:
			case TRUE:
				{
				alt49=1;
				}
				break;
			case LBRACE:
				{
				int LA49_13 = input.LA(2);
				if ( (synpred74_BigDataScript()) ) {
					alt49=1;
				}
				else if ( (synpred75_BigDataScript()) ) {
					alt49=2;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 49, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case BREAK:
				{
				alt49=3;
				}
				break;
			case CONTINUE:
				{
				alt49=4;
				}
				break;
			case CHECKPOINT:
				{
				alt49=5;
				}
				break;
			case EXIT:
				{
				alt49=6;
				}
				break;
			case FOR:
				{
				alt49=7;
				}
				break;
			case IF:
				{
				alt49=8;
				}
				break;
			case BOOL:
			case INT:
			case REAL:
			case STRING:
			case VOID:
				{
				alt49=9;
				}
				break;
			case RETURN:
				{
				alt49=10;
				}
				break;
			case WAIT:
				{
				alt49=11;
				}
				break;
			case WHILE:
				{
				alt49=12;
				}
				break;
			case NEWLINE:
			case SEMI:
				{
				alt49=13;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 49, 0, input);
				throw nvae;
			}
			switch (alt49) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:344:5: assignmentExpression ^ semi !
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_assignmentExpression_in_statement2881);
					assignmentExpression134=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(assignmentExpression134.getTree(), root_0);
					pushFollow(FOLLOW_semi_in_statement2884);
					semi135=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:345:5: block
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_block_in_statement2891);
					block136=block();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, block136.getTree());

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:346:5: BREAK ^ semi !
					{
					root_0 = (Object)adaptor.nil();


					BREAK137=(Token)match(input,BREAK,FOLLOW_BREAK_in_statement2897); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					BREAK137_tree = 
					(Object)adaptor.create(BREAK137)
					;
					root_0 = (Object)adaptor.becomeRoot(BREAK137_tree, root_0);
					}

					pushFollow(FOLLOW_semi_in_statement2900);
					semi138=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:347:5: CONTINUE ^ semi !
					{
					root_0 = (Object)adaptor.nil();


					CONTINUE139=(Token)match(input,CONTINUE,FOLLOW_CONTINUE_in_statement2907); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					CONTINUE139_tree = 
					(Object)adaptor.create(CONTINUE139)
					;
					root_0 = (Object)adaptor.becomeRoot(CONTINUE139_tree, root_0);
					}

					pushFollow(FOLLOW_semi_in_statement2910);
					semi140=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 5 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:348:5: CHECKPOINT ^ ( assignmentExpression )? semi !
					{
					root_0 = (Object)adaptor.nil();


					CHECKPOINT141=(Token)match(input,CHECKPOINT,FOLLOW_CHECKPOINT_in_statement2917); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					CHECKPOINT141_tree = 
					(Object)adaptor.create(CHECKPOINT141)
					;
					root_0 = (Object)adaptor.becomeRoot(CHECKPOINT141_tree, root_0);
					}

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:348:17: ( assignmentExpression )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==BANG||LA46_0==FALSE||LA46_0==IDENTIFIER||LA46_0==INT_LITERAL||(LA46_0 >= LBRACE && LA46_0 <= LBRACKET)||LA46_0==LPAREN||LA46_0==PLUS||LA46_0==PLUSPLUS||LA46_0==REAL_LITERAL||(LA46_0 >= STRING_LITERAL && LA46_0 <= SUB)||LA46_0==SUBSUB||LA46_0==SYS_LITERAL||LA46_0==TASK||(LA46_0 >= TILDE && LA46_0 <= TRUE)) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:348:17: assignmentExpression
							{
							pushFollow(FOLLOW_assignmentExpression_in_statement2920);
							assignmentExpression142=assignmentExpression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression142.getTree());

							}
							break;

					}

					pushFollow(FOLLOW_semi_in_statement2923);
					semi143=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 6 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:349:5: EXIT ^ assignmentExpression semi !
					{
					root_0 = (Object)adaptor.nil();


					EXIT144=(Token)match(input,EXIT,FOLLOW_EXIT_in_statement2930); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EXIT144_tree = 
					(Object)adaptor.create(EXIT144)
					;
					root_0 = (Object)adaptor.becomeRoot(EXIT144_tree, root_0);
					}

					pushFollow(FOLLOW_assignmentExpression_in_statement2933);
					assignmentExpression145=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression145.getTree());

					pushFollow(FOLLOW_semi_in_statement2935);
					semi146=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 7 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:350:5: forstatement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_forstatement_in_statement2942);
					forstatement147=forstatement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, forstatement147.getTree());

					}
					break;
				case 8 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:5: IF c= parExpression st= statement ( ELSE se= statement )?
					{
					IF148=(Token)match(input,IF,FOLLOW_IF_in_statement2948); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_IF.add(IF148);

					pushFollow(FOLLOW_parExpression_in_statement2952);
					c=parExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parExpression.add(c.getTree());
					pushFollow(FOLLOW_statement_in_statement2956);
					st=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_statement.add(st.getTree());
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:37: ( ELSE se= statement )?
					int alt47=2;
					int LA47_0 = input.LA(1);
					if ( (LA47_0==ELSE) ) {
						int LA47_1 = input.LA(2);
						if ( (synpred82_BigDataScript()) ) {
							alt47=1;
						}
					}
					switch (alt47) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:38: ELSE se= statement
							{
							ELSE149=(Token)match(input,ELSE,FOLLOW_ELSE_in_statement2959); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ELSE.add(ELSE149);

							pushFollow(FOLLOW_statement_in_statement2963);
							se=statement();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_statement.add(se.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: IF, c, se, st
					// token labels: 
					// rule labels: retval, c, st, se
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_c=new RewriteRuleSubtreeStream(adaptor,"rule c",c!=null?c.getTree():null);
					RewriteRuleSubtreeStream stream_st=new RewriteRuleSubtreeStream(adaptor,"rule st",st!=null?st.getTree():null);
					RewriteRuleSubtreeStream stream_se=new RewriteRuleSubtreeStream(adaptor,"rule se",se!=null?se.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 351:58: -> ^( IF $c $st ( $se)? )
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:61: ^( IF $c $st ( $se)? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						stream_IF.nextNode()
						, root_1);

						adaptor.addChild(root_1, stream_c.nextTree());

						adaptor.addChild(root_1, stream_st.nextTree());

						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:74: ( $se)?
						if ( stream_se.hasNext() ) {
							adaptor.addChild(root_1, stream_se.nextTree());

						}
						stream_se.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 9 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:352:5: methodDeclaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_methodDeclaration_in_statement2987);
					methodDeclaration150=methodDeclaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, methodDeclaration150.getTree());

					}
					break;
				case 10 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:353:5: RETURN ^ ( assignmentExpression )? semi !
					{
					root_0 = (Object)adaptor.nil();


					RETURN151=(Token)match(input,RETURN,FOLLOW_RETURN_in_statement2993); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RETURN151_tree = 
					(Object)adaptor.create(RETURN151)
					;
					root_0 = (Object)adaptor.becomeRoot(RETURN151_tree, root_0);
					}

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:353:13: ( assignmentExpression )?
					int alt48=2;
					int LA48_0 = input.LA(1);
					if ( (LA48_0==BANG||LA48_0==FALSE||LA48_0==IDENTIFIER||LA48_0==INT_LITERAL||(LA48_0 >= LBRACE && LA48_0 <= LBRACKET)||LA48_0==LPAREN||LA48_0==PLUS||LA48_0==PLUSPLUS||LA48_0==REAL_LITERAL||(LA48_0 >= STRING_LITERAL && LA48_0 <= SUB)||LA48_0==SUBSUB||LA48_0==SYS_LITERAL||LA48_0==TASK||(LA48_0 >= TILDE && LA48_0 <= TRUE)) ) {
						alt48=1;
					}
					switch (alt48) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:353:14: assignmentExpression
							{
							pushFollow(FOLLOW_assignmentExpression_in_statement2997);
							assignmentExpression152=assignmentExpression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentExpression152.getTree());

							}
							break;

					}

					pushFollow(FOLLOW_semi_in_statement3002);
					semi153=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 11 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:354:5: WAIT ^ semi !
					{
					root_0 = (Object)adaptor.nil();


					WAIT154=(Token)match(input,WAIT,FOLLOW_WAIT_in_statement3009); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WAIT154_tree = 
					(Object)adaptor.create(WAIT154)
					;
					root_0 = (Object)adaptor.becomeRoot(WAIT154_tree, root_0);
					}

					pushFollow(FOLLOW_semi_in_statement3012);
					semi155=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;
				case 12 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:355:5: WHILE c= parExpression s= statement
					{
					WHILE156=(Token)match(input,WHILE,FOLLOW_WHILE_in_statement3019); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WHILE.add(WHILE156);

					pushFollow(FOLLOW_parExpression_in_statement3023);
					c=parExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_parExpression.add(c.getTree());
					pushFollow(FOLLOW_statement_in_statement3027);
					s=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_statement.add(s.getTree());
					// AST REWRITE
					// elements: c, WHILE, s
					// token labels: 
					// rule labels: retval, s, c
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.getTree():null);
					RewriteRuleSubtreeStream stream_c=new RewriteRuleSubtreeStream(adaptor,"rule c",c!=null?c.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 355:40: -> ^( WHILE $c $s)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:355:43: ^( WHILE $c $s)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						stream_WHILE.nextNode()
						, root_1);

						adaptor.addChild(root_1, stream_c.nextTree());

						adaptor.addChild(root_1, stream_s.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 13 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:356:5: semi !
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_semi_in_statement3046);
					semi157=semi();
					state._fsp--;
					if (state.failed) return retval;
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statement"


	public static class forstatement_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forstatement"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:360:1: forstatement : ( FOR LPAREN (init= forInit )? SEMI (cond= forCond )? SEMI (end= forEnd )? RPAREN st= statement -> ^( FOR_LOOP ( $init)? ( $cond)? ( $end)? $st) | FOR LPAREN init2= forInit2 COLON ex= assignmentExpression RPAREN st= statement -> ^( FOR_LOOP_LIST $init2 $ex $st) );
	public final BigDataScriptParser.forstatement_return forstatement() throws RecognitionException {
		BigDataScriptParser.forstatement_return retval = new BigDataScriptParser.forstatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token FOR158=null;
		Token LPAREN159=null;
		Token SEMI160=null;
		Token SEMI161=null;
		Token RPAREN162=null;
		Token FOR163=null;
		Token LPAREN164=null;
		Token COLON165=null;
		Token RPAREN166=null;
		ParserRuleReturnScope init =null;
		ParserRuleReturnScope cond =null;
		ParserRuleReturnScope end =null;
		ParserRuleReturnScope st =null;
		ParserRuleReturnScope init2 =null;
		ParserRuleReturnScope ex =null;

		Object FOR158_tree=null;
		Object LPAREN159_tree=null;
		Object SEMI160_tree=null;
		Object SEMI161_tree=null;
		Object RPAREN162_tree=null;
		Object FOR163_tree=null;
		Object LPAREN164_tree=null;
		Object COLON165_tree=null;
		Object RPAREN166_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_FOR=new RewriteRuleTokenStream(adaptor,"token FOR");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
		RewriteRuleSubtreeStream stream_forInit2=new RewriteRuleSubtreeStream(adaptor,"rule forInit2");
		RewriteRuleSubtreeStream stream_forEnd=new RewriteRuleSubtreeStream(adaptor,"rule forEnd");
		RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");
		RewriteRuleSubtreeStream stream_forInit=new RewriteRuleSubtreeStream(adaptor,"rule forInit");
		RewriteRuleSubtreeStream stream_forCond=new RewriteRuleSubtreeStream(adaptor,"rule forCond");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:3: ( FOR LPAREN (init= forInit )? SEMI (cond= forCond )? SEMI (end= forEnd )? RPAREN st= statement -> ^( FOR_LOOP ( $init)? ( $cond)? ( $end)? $st) | FOR LPAREN init2= forInit2 COLON ex= assignmentExpression RPAREN st= statement -> ^( FOR_LOOP_LIST $init2 $ex $st) )
			int alt53=2;
			int LA53_0 = input.LA(1);
			if ( (LA53_0==FOR) ) {
				int LA53_1 = input.LA(2);
				if ( (LA53_1==LPAREN) ) {
					int LA53_2 = input.LA(3);
					if ( (LA53_2==BOOL||LA53_2==INT||LA53_2==REAL||LA53_2==STRING||LA53_2==VOID) ) {
						switch ( input.LA(4) ) {
						case IDENTIFIER:
							{
							int LA53_5 = input.LA(5);
							if ( (LA53_5==COMMA||LA53_5==EQ||LA53_5==SEMI) ) {
								alt53=1;
							}
							else if ( (LA53_5==COLON) ) {
								alt53=2;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 53, 5, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
							}
							break;
						case LBRACKET:
							{
							int LA53_6 = input.LA(5);
							if ( (LA53_6==RBRACKET) ) {
								int LA53_9 = input.LA(6);
								if ( (LA53_9==IDENTIFIER) ) {
									int LA53_5 = input.LA(7);
									if ( (LA53_5==COMMA||LA53_5==EQ||LA53_5==SEMI) ) {
										alt53=1;
									}
									else if ( (LA53_5==COLON) ) {
										alt53=2;
									}
									else {
										if (state.backtracking>0) {state.failed=true; return retval;}
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++)
												input.consume();
											NoViableAltException nvae =
												new NoViableAltException("", 53, 5, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 53, 9, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 53, 6, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
							}
							break;
						case LBRACE:
							{
							int LA53_7 = input.LA(5);
							if ( (LA53_7==RBRACE) ) {
								int LA53_10 = input.LA(6);
								if ( (LA53_10==IDENTIFIER) ) {
									int LA53_5 = input.LA(7);
									if ( (LA53_5==COMMA||LA53_5==EQ||LA53_5==SEMI) ) {
										alt53=1;
									}
									else if ( (LA53_5==COLON) ) {
										alt53=2;
									}
									else {
										if (state.backtracking>0) {state.failed=true; return retval;}
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++)
												input.consume();
											NoViableAltException nvae =
												new NoViableAltException("", 53, 5, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 53, 10, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else if ( (LA53_7==BOOL||LA53_7==INT||LA53_7==REAL||LA53_7==STRING||LA53_7==VOID) ) {
								int LA53_11 = input.LA(6);
								if ( (LA53_11==RBRACE) ) {
									int LA53_12 = input.LA(7);
									if ( (LA53_12==IDENTIFIER) ) {
										int LA53_5 = input.LA(8);
										if ( (LA53_5==COMMA||LA53_5==EQ||LA53_5==SEMI) ) {
											alt53=1;
										}
										else if ( (LA53_5==COLON) ) {
											alt53=2;
										}
										else {
											if (state.backtracking>0) {state.failed=true; return retval;}
											int nvaeMark = input.mark();
											try {
												for (int nvaeConsume = 0; nvaeConsume < 8 - 1; nvaeConsume++)
													input.consume();
												NoViableAltException nvae =
													new NoViableAltException("", 53, 5, input);
												throw nvae;
											} finally {
												input.rewind(nvaeMark);
											}
										}
									}
									else {
										if (state.backtracking>0) {state.failed=true; return retval;}
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++)
												input.consume();
											NoViableAltException nvae =
												new NoViableAltException("", 53, 12, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}
								}
								else {
									if (state.backtracking>0) {state.failed=true; return retval;}
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++)
											input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 53, 11, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++)
										input.consume();
									NoViableAltException nvae =
										new NoViableAltException("", 53, 7, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
							}
							break;
						default:
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++)
									input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 53, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA53_2==BANG||LA53_2==FALSE||LA53_2==IDENTIFIER||LA53_2==INT_LITERAL||(LA53_2 >= LBRACE && LA53_2 <= LBRACKET)||LA53_2==LPAREN||LA53_2==PLUS||LA53_2==PLUSPLUS||LA53_2==REAL_LITERAL||LA53_2==SEMI||(LA53_2 >= STRING_LITERAL && LA53_2 <= SUB)||LA53_2==SUBSUB||LA53_2==SYS_LITERAL||LA53_2==TASK||(LA53_2 >= TILDE && LA53_2 <= TRUE)) ) {
						alt53=1;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++)
								input.consume();
							NoViableAltException nvae =
								new NoViableAltException("", 53, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
				}
				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 53, 0, input);
				throw nvae;
			}
			switch (alt53) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:5: FOR LPAREN (init= forInit )? SEMI (cond= forCond )? SEMI (end= forEnd )? RPAREN st= statement
					{
					FOR158=(Token)match(input,FOR,FOLLOW_FOR_in_forstatement3061); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FOR.add(FOR158);

					LPAREN159=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_forstatement3063); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN159);

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:16: (init= forInit )?
					int alt50=2;
					int LA50_0 = input.LA(1);
					if ( (LA50_0==BANG||LA50_0==BOOL||LA50_0==FALSE||LA50_0==IDENTIFIER||(LA50_0 >= INT && LA50_0 <= INT_LITERAL)||(LA50_0 >= LBRACE && LA50_0 <= LBRACKET)||LA50_0==LPAREN||LA50_0==PLUS||LA50_0==PLUSPLUS||(LA50_0 >= REAL && LA50_0 <= REAL_LITERAL)||(LA50_0 >= STRING && LA50_0 <= SUB)||LA50_0==SUBSUB||LA50_0==SYS_LITERAL||LA50_0==TASK||(LA50_0 >= TILDE && LA50_0 <= TRUE)||LA50_0==VOID) ) {
						alt50=1;
					}
					switch (alt50) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:18: init= forInit
							{
							pushFollow(FOLLOW_forInit_in_forstatement3069);
							init=forInit();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_forInit.add(init.getTree());
							}
							break;

					}

					SEMI160=(Token)match(input,SEMI,FOLLOW_SEMI_in_forstatement3074); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_SEMI.add(SEMI160);

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:39: (cond= forCond )?
					int alt51=2;
					int LA51_0 = input.LA(1);
					if ( (LA51_0==BANG||LA51_0==FALSE||LA51_0==IDENTIFIER||LA51_0==INT_LITERAL||(LA51_0 >= LBRACE && LA51_0 <= LBRACKET)||LA51_0==LPAREN||LA51_0==PLUS||LA51_0==PLUSPLUS||LA51_0==REAL_LITERAL||(LA51_0 >= STRING_LITERAL && LA51_0 <= SUB)||LA51_0==SUBSUB||LA51_0==SYS_LITERAL||LA51_0==TASK||(LA51_0 >= TILDE && LA51_0 <= TRUE)) ) {
						alt51=1;
					}
					switch (alt51) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:41: cond= forCond
							{
							pushFollow(FOLLOW_forCond_in_forstatement3080);
							cond=forCond();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_forCond.add(cond.getTree());
							}
							break;

					}

					SEMI161=(Token)match(input,SEMI,FOLLOW_SEMI_in_forstatement3085); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_SEMI.add(SEMI161);

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:62: (end= forEnd )?
					int alt52=2;
					int LA52_0 = input.LA(1);
					if ( (LA52_0==BANG||LA52_0==FALSE||LA52_0==IDENTIFIER||LA52_0==INT_LITERAL||(LA52_0 >= LBRACE && LA52_0 <= LBRACKET)||LA52_0==LPAREN||LA52_0==PLUS||LA52_0==PLUSPLUS||LA52_0==REAL_LITERAL||(LA52_0 >= STRING_LITERAL && LA52_0 <= SUB)||LA52_0==SUBSUB||LA52_0==SYS_LITERAL||LA52_0==TASK||(LA52_0 >= TILDE && LA52_0 <= TRUE)) ) {
						alt52=1;
					}
					switch (alt52) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:64: end= forEnd
							{
							pushFollow(FOLLOW_forEnd_in_forstatement3091);
							end=forEnd();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_forEnd.add(end.getTree());
							}
							break;

					}

					RPAREN162=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_forstatement3096); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN162);

					pushFollow(FOLLOW_statement_in_forstatement3100);
					st=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_statement.add(st.getTree());
					// AST REWRITE
					// elements: end, st, init, cond
					// token labels: 
					// rule labels: retval, init, st, cond, end
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_init=new RewriteRuleSubtreeStream(adaptor,"rule init",init!=null?init.getTree():null);
					RewriteRuleSubtreeStream stream_st=new RewriteRuleSubtreeStream(adaptor,"rule st",st!=null?st.getTree():null);
					RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.getTree():null);
					RewriteRuleSubtreeStream stream_end=new RewriteRuleSubtreeStream(adaptor,"rule end",end!=null?end.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 361:98: -> ^( FOR_LOOP ( $init)? ( $cond)? ( $end)? $st)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:101: ^( FOR_LOOP ( $init)? ( $cond)? ( $end)? $st)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FOR_LOOP, "FOR_LOOP")
						, root_1);

						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:113: ( $init)?
						if ( stream_init.hasNext() ) {
							adaptor.addChild(root_1, stream_init.nextTree());

						}
						stream_init.reset();

						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:120: ( $cond)?
						if ( stream_cond.hasNext() ) {
							adaptor.addChild(root_1, stream_cond.nextTree());

						}
						stream_cond.reset();

						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:361:127: ( $end)?
						if ( stream_end.hasNext() ) {
							adaptor.addChild(root_1, stream_end.nextTree());

						}
						stream_end.reset();

						adaptor.addChild(root_1, stream_st.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:362:5: FOR LPAREN init2= forInit2 COLON ex= assignmentExpression RPAREN st= statement
					{
					FOR163=(Token)match(input,FOR,FOLLOW_FOR_in_forstatement3128); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FOR.add(FOR163);

					LPAREN164=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_forstatement3130); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN164);

					pushFollow(FOLLOW_forInit2_in_forstatement3134);
					init2=forInit2();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_forInit2.add(init2.getTree());
					COLON165=(Token)match(input,COLON,FOLLOW_COLON_in_forstatement3136); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON165);

					pushFollow(FOLLOW_assignmentExpression_in_forstatement3140);
					ex=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_assignmentExpression.add(ex.getTree());
					RPAREN166=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_forstatement3142); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN166);

					pushFollow(FOLLOW_statement_in_forstatement3146);
					st=statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_statement.add(st.getTree());
					// AST REWRITE
					// elements: init2, ex, st
					// token labels: 
					// rule labels: ex, retval, init2, st
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_ex=new RewriteRuleSubtreeStream(adaptor,"rule ex",ex!=null?ex.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_init2=new RewriteRuleSubtreeStream(adaptor,"rule init2",init2!=null?init2.getTree():null);
					RewriteRuleSubtreeStream stream_st=new RewriteRuleSubtreeStream(adaptor,"rule st",st!=null?st.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 362:82: -> ^( FOR_LOOP_LIST $init2 $ex $st)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:362:85: ^( FOR_LOOP_LIST $init2 $ex $st)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FOR_LOOP_LIST, "FOR_LOOP_LIST")
						, root_1);

						adaptor.addChild(root_1, stream_init2.nextTree());

						adaptor.addChild(root_1, stream_ex.nextTree());

						adaptor.addChild(root_1, stream_st.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forstatement"


	public static class forInit_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forInit"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:365:1: forInit : (lv= localVariableDeclaration -> ^( FOR_INIT $lv) |el= expressionList -> ^( FOR_INIT $el) );
	public final BigDataScriptParser.forInit_return forInit() throws RecognitionException {
		BigDataScriptParser.forInit_return retval = new BigDataScriptParser.forInit_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope lv =null;
		ParserRuleReturnScope el =null;

		RewriteRuleSubtreeStream stream_expressionList=new RewriteRuleSubtreeStream(adaptor,"rule expressionList");
		RewriteRuleSubtreeStream stream_localVariableDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule localVariableDeclaration");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:365:9: (lv= localVariableDeclaration -> ^( FOR_INIT $lv) |el= expressionList -> ^( FOR_INIT $el) )
			int alt54=2;
			int LA54_0 = input.LA(1);
			if ( (LA54_0==BOOL||LA54_0==INT||LA54_0==REAL||LA54_0==STRING||LA54_0==VOID) ) {
				alt54=1;
			}
			else if ( (LA54_0==BANG||LA54_0==FALSE||LA54_0==IDENTIFIER||LA54_0==INT_LITERAL||(LA54_0 >= LBRACE && LA54_0 <= LBRACKET)||LA54_0==LPAREN||LA54_0==PLUS||LA54_0==PLUSPLUS||LA54_0==REAL_LITERAL||(LA54_0 >= STRING_LITERAL && LA54_0 <= SUB)||LA54_0==SUBSUB||LA54_0==SYS_LITERAL||LA54_0==TASK||(LA54_0 >= TILDE && LA54_0 <= TRUE)) ) {
				alt54=2;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 54, 0, input);
				throw nvae;
			}
			switch (alt54) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:366:2: lv= localVariableDeclaration
					{
					pushFollow(FOLLOW_localVariableDeclaration_in_forInit3178);
					lv=localVariableDeclaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_localVariableDeclaration.add(lv.getTree());
					// AST REWRITE
					// elements: lv
					// token labels: 
					// rule labels: retval, lv
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_lv=new RewriteRuleSubtreeStream(adaptor,"rule lv",lv!=null?lv.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 366:33: -> ^( FOR_INIT $lv)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:366:36: ^( FOR_INIT $lv)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FOR_INIT, "FOR_INIT")
						, root_1);

						adaptor.addChild(root_1, stream_lv.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:367:4: el= expressionList
					{
					pushFollow(FOLLOW_expressionList_in_forInit3197);
					el=expressionList();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expressionList.add(el.getTree());
					// AST REWRITE
					// elements: el
					// token labels: 
					// rule labels: retval, el
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {

					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_el=new RewriteRuleSubtreeStream(adaptor,"rule el",el!=null?el.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 367:27: -> ^( FOR_INIT $el)
					{
						// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:367:30: ^( FOR_INIT $el)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(
						(Object)adaptor.create(FOR_INIT, "FOR_INIT")
						, root_1);

						adaptor.addChild(root_1, stream_el.nextTree());

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forInit"


	public static class forCond_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forCond"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:370:1: forCond : e= assignmentExpression -> ^( FOR_CONDITION $e) ;
	public final BigDataScriptParser.forCond_return forCond() throws RecognitionException {
		BigDataScriptParser.forCond_return retval = new BigDataScriptParser.forCond_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope e =null;

		RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:370:9: (e= assignmentExpression -> ^( FOR_CONDITION $e) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:370:11: e= assignmentExpression
			{
			pushFollow(FOLLOW_assignmentExpression_in_forCond3223);
			e=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_assignmentExpression.add(e.getTree());
			// AST REWRITE
			// elements: e
			// token labels: 
			// rule labels: retval, e
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 370:38: -> ^( FOR_CONDITION $e)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:370:41: ^( FOR_CONDITION $e)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(FOR_CONDITION, "FOR_CONDITION")
				, root_1);

				adaptor.addChild(root_1, stream_e.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forCond"


	public static class forEnd_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forEnd"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:372:1: forEnd : e= expressionList -> ^( FOR_END $e) ;
	public final BigDataScriptParser.forEnd_return forEnd() throws RecognitionException {
		BigDataScriptParser.forEnd_return retval = new BigDataScriptParser.forEnd_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope e =null;

		RewriteRuleSubtreeStream stream_expressionList=new RewriteRuleSubtreeStream(adaptor,"rule expressionList");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:372:8: (e= expressionList -> ^( FOR_END $e) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:372:10: e= expressionList
			{
			pushFollow(FOLLOW_expressionList_in_forEnd3246);
			e=expressionList();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_expressionList.add(e.getTree());
			// AST REWRITE
			// elements: e
			// token labels: 
			// rule labels: retval, e
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 372:31: -> ^( FOR_END $e)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:372:34: ^( FOR_END $e)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(FOR_END, "FOR_END")
				, root_1);

				adaptor.addChild(root_1, stream_e.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forEnd"


	public static class forInit2_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forInit2"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:374:1: forInit2 : t= type id= IDENTIFIER -> ^( VAR_DECLARATION $t ^( VAR_INIT $id) ) ;
	public final BigDataScriptParser.forInit2_return forInit2() throws RecognitionException {
		BigDataScriptParser.forInit2_return retval = new BigDataScriptParser.forInit2_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token id=null;
		ParserRuleReturnScope t =null;

		Object id_tree=null;
		RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:374:10: (t= type id= IDENTIFIER -> ^( VAR_DECLARATION $t ^( VAR_INIT $id) ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:374:12: t= type id= IDENTIFIER
			{
			pushFollow(FOLLOW_type_in_forInit23269);
			t=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(t.getTree());
			id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_forInit23273); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IDENTIFIER.add(id);

			// AST REWRITE
			// elements: id, t
			// token labels: id
			// rule labels: retval, t
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleTokenStream stream_id=new RewriteRuleTokenStream(adaptor,"token id",id);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 374:37: -> ^( VAR_DECLARATION $t ^( VAR_INIT $id) )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:374:40: ^( VAR_DECLARATION $t ^( VAR_INIT $id) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_DECLARATION, "VAR_DECLARATION")
				, root_1);

				adaptor.addChild(root_1, stream_t.nextTree());

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:374:61: ^( VAR_INIT $id)
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(VAR_INIT, "VAR_INIT")
				, root_2);

				adaptor.addChild(root_2, stream_id.nextNode());

				adaptor.addChild(root_1, root_2);
				}

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forInit2"


	public static class sys_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sys"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:377:1: sys : s= SYS_LITERAL -> ^( SYS_EXPRESSION $s) ;
	public final BigDataScriptParser.sys_return sys() throws RecognitionException {
		BigDataScriptParser.sys_return retval = new BigDataScriptParser.sys_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token s=null;

		Object s_tree=null;
		RewriteRuleTokenStream stream_SYS_LITERAL=new RewriteRuleTokenStream(adaptor,"token SYS_LITERAL");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:377:5: (s= SYS_LITERAL -> ^( SYS_EXPRESSION $s) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:377:7: s= SYS_LITERAL
			{
			s=(Token)match(input,SYS_LITERAL,FOLLOW_SYS_LITERAL_in_sys3304); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_SYS_LITERAL.add(s);

			// AST REWRITE
			// elements: s
			// token labels: s
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 377:26: -> ^( SYS_EXPRESSION $s)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:377:29: ^( SYS_EXPRESSION $s)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(SYS_EXPRESSION, "SYS_EXPRESSION")
				, root_1);

				adaptor.addChild(root_1, stream_s.nextNode());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sys"


	public static class task_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "task"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:2: task : TASK (opt= taskOptions )? s= statement -> ^( TASK_EXPRESSION ( $opt)? $s) ;
	public final BigDataScriptParser.task_return task() throws RecognitionException {
		BigDataScriptParser.task_return retval = new BigDataScriptParser.task_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TASK167=null;
		ParserRuleReturnScope opt =null;
		ParserRuleReturnScope s =null;

		Object TASK167_tree=null;
		RewriteRuleTokenStream stream_TASK=new RewriteRuleTokenStream(adaptor,"token TASK");
		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
		RewriteRuleSubtreeStream stream_taskOptions=new RewriteRuleSubtreeStream(adaptor,"rule taskOptions");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:7: ( TASK (opt= taskOptions )? s= statement -> ^( TASK_EXPRESSION ( $opt)? $s) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:9: TASK (opt= taskOptions )? s= statement
			{
			TASK167=(Token)match(input,TASK,FOLLOW_TASK_in_task3329); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_TASK.add(TASK167);

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:14: (opt= taskOptions )?
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==LPAREN) ) {
				int LA55_1 = input.LA(2);
				if ( (synpred94_BigDataScript()) ) {
					alt55=1;
				}
			}
			switch (alt55) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:15: opt= taskOptions
					{
					pushFollow(FOLLOW_taskOptions_in_task3334);
					opt=taskOptions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_taskOptions.add(opt.getTree());
					}
					break;

			}

			pushFollow(FOLLOW_statement_in_task3340);
			s=statement();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_statement.add(s.getTree());
			// AST REWRITE
			// elements: s, opt
			// token labels: 
			// rule labels: retval, s, opt
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.getTree():null);
			RewriteRuleSubtreeStream stream_opt=new RewriteRuleSubtreeStream(adaptor,"rule opt",opt!=null?opt.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 381:48: -> ^( TASK_EXPRESSION ( $opt)? $s)
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:51: ^( TASK_EXPRESSION ( $opt)? $s)
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(TASK_EXPRESSION, "TASK_EXPRESSION")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:70: ( $opt)?
				if ( stream_opt.hasNext() ) {
					adaptor.addChild(root_1, stream_opt.nextTree());

				}
				stream_opt.reset();

				adaptor.addChild(root_1, stream_s.nextTree());

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "task"


	public static class taskOptions_return extends ParserRuleReturnScope {
		Object tree;
		public Object getTree() { return tree; }
	};


	// $ANTLR start "taskOptions"
	// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:383:2: taskOptions : LPAREN e+= assignmentExpression ( COMMA e+= assignmentExpression )* RPAREN -> ^( TASK_OPTIONS ( $e)* ) ;
	public final BigDataScriptParser.taskOptions_return taskOptions() throws RecognitionException {
		BigDataScriptParser.taskOptions_return retval = new BigDataScriptParser.taskOptions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN168=null;
		Token COMMA169=null;
		Token RPAREN170=null;
		List<Object> list_e=null;
		RuleReturnScope e = null;
		Object LPAREN168_tree=null;
		Object COMMA169_tree=null;
		Object RPAREN170_tree=null;
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");

		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:383:14: ( LPAREN e+= assignmentExpression ( COMMA e+= assignmentExpression )* RPAREN -> ^( TASK_OPTIONS ( $e)* ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:383:16: LPAREN e+= assignmentExpression ( COMMA e+= assignmentExpression )* RPAREN
			{
			LPAREN168=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_taskOptions3367); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN168);

			pushFollow(FOLLOW_assignmentExpression_in_taskOptions3371);
			e=assignmentExpression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_assignmentExpression.add(e.getTree());
			if (list_e==null) list_e=new ArrayList<Object>();
			list_e.add(e.getTree());
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:383:47: ( COMMA e+= assignmentExpression )*
			loop56:
			do {
				int alt56=2;
				int LA56_0 = input.LA(1);
				if ( (LA56_0==COMMA) ) {
					alt56=1;
				}

				switch (alt56) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:383:48: COMMA e+= assignmentExpression
					{
					COMMA169=(Token)match(input,COMMA,FOLLOW_COMMA_in_taskOptions3374); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COMMA.add(COMMA169);

					pushFollow(FOLLOW_assignmentExpression_in_taskOptions3378);
					e=assignmentExpression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_assignmentExpression.add(e.getTree());
					if (list_e==null) list_e=new ArrayList<Object>();
					list_e.add(e.getTree());
					}
					break;

				default :
					break loop56;
				}
			} while (true);

			RPAREN170=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_taskOptions3382); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN170);

			// AST REWRITE
			// elements: e
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: e
			// wildcard labels: 
			if ( state.backtracking==0 ) {

			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
			RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"token e",list_e);
			root_0 = (Object)adaptor.nil();
			// 384:9: -> ^( TASK_OPTIONS ( $e)* )
			{
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:384:12: ^( TASK_OPTIONS ( $e)* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(
				(Object)adaptor.create(TASK_OPTIONS, "TASK_OPTIONS")
				, root_1);

				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:384:28: ( $e)*
				while ( stream_e.hasNext() ) {
					adaptor.addChild(root_1, stream_e.nextTree());

				}
				stream_e.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "taskOptions"

	// $ANTLR start synpred3_BigDataScript
	public final void synpred3_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:8: ( SEMI )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:8: SEMI
		{
		match(input,SEMI,FOLLOW_SEMI_in_synpred3_BigDataScript1444); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred3_BigDataScript

	// $ANTLR start synpred5_BigDataScript
	public final void synpred5_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:17: ( NEWLINE )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:198:17: NEWLINE
		{
		match(input,NEWLINE,FOLLOW_NEWLINE_in_synpred5_BigDataScript1450); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred5_BigDataScript

	// $ANTLR start synpred10_BigDataScript
	public final void synpred10_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:23: ( arrayInitializer )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:212:23: arrayInitializer
		{
		pushFollow(FOLLOW_arrayInitializer_in_synpred10_BigDataScript1566);
		arrayInitializer();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred10_BigDataScript

	// $ANTLR start synpred45_BigDataScript
	public final void synpred45_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:281:3: ( DOT IDENTIFIER LPAREN RPAREN )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:281:3: DOT IDENTIFIER LPAREN RPAREN
		{
		match(input,DOT,FOLLOW_DOT_in_synpred45_BigDataScript2204); if (state.failed) return ;

		match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred45_BigDataScript2207); if (state.failed) return ;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred45_BigDataScript2209); if (state.failed) return ;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred45_BigDataScript2212); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred45_BigDataScript

	// $ANTLR start synpred46_BigDataScript
	public final void synpred46_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:282:5: ( DOT IDENTIFIER arguments )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:282:5: DOT IDENTIFIER arguments
		{
		match(input,DOT,FOLLOW_DOT_in_synpred46_BigDataScript2219); if (state.failed) return ;

		match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred46_BigDataScript2222); if (state.failed) return ;

		pushFollow(FOLLOW_arguments_in_synpred46_BigDataScript2224);
		arguments();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred46_BigDataScript

	// $ANTLR start synpred55_BigDataScript
	public final void synpred55_BigDataScript_fragment() throws RecognitionException {
		Token op=null;
		ParserRuleReturnScope e =null;


		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:301:5: (e= primary op= SUBSUB )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:301:5: e= primary op= SUBSUB
		{
		pushFollow(FOLLOW_primary_in_synpred55_BigDataScript2391);
		e=primary();
		state._fsp--;
		if (state.failed) return ;

		op=(Token)match(input,SUBSUB,FOLLOW_SUBSUB_in_synpred55_BigDataScript2395); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred55_BigDataScript

	// $ANTLR start synpred56_BigDataScript
	public final void synpred56_BigDataScript_fragment() throws RecognitionException {
		Token op=null;
		ParserRuleReturnScope e =null;


		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:302:5: (e= primary op= PLUSPLUS )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:302:5: e= primary op= PLUSPLUS
		{
		pushFollow(FOLLOW_primary_in_synpred56_BigDataScript2418);
		e=primary();
		state._fsp--;
		if (state.failed) return ;

		op=(Token)match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_synpred56_BigDataScript2422); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred56_BigDataScript

	// $ANTLR start synpred70_BigDataScript
	public final void synpred70_BigDataScript_fragment() throws RecognitionException {
		Token id=null;


		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:16: (id= IDENTIFIER LPAREN RPAREN )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:328:16: id= IDENTIFIER LPAREN RPAREN
		{
		id=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred70_BigDataScript2731); if (state.failed) return ;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred70_BigDataScript2733); if (state.failed) return ;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred70_BigDataScript2735); if (state.failed) return ;

		}

	}
	// $ANTLR end synpred70_BigDataScript

	// $ANTLR start synpred74_BigDataScript
	public final void synpred74_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:344:5: ( assignmentExpression semi )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:344:5: assignmentExpression semi
		{
		pushFollow(FOLLOW_assignmentExpression_in_synpred74_BigDataScript2881);
		assignmentExpression();
		state._fsp--;
		if (state.failed) return ;

		pushFollow(FOLLOW_semi_in_synpred74_BigDataScript2884);
		semi();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred74_BigDataScript

	// $ANTLR start synpred75_BigDataScript
	public final void synpred75_BigDataScript_fragment() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:345:5: ( block )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:345:5: block
		{
		pushFollow(FOLLOW_block_in_synpred75_BigDataScript2891);
		block();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred75_BigDataScript

	// $ANTLR start synpred82_BigDataScript
	public final void synpred82_BigDataScript_fragment() throws RecognitionException {
		ParserRuleReturnScope se =null;


		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:38: ( ELSE se= statement )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:351:38: ELSE se= statement
		{
		match(input,ELSE,FOLLOW_ELSE_in_synpred82_BigDataScript2959); if (state.failed) return ;

		pushFollow(FOLLOW_statement_in_synpred82_BigDataScript2963);
		se=statement();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred82_BigDataScript

	// $ANTLR start synpred94_BigDataScript
	public final void synpred94_BigDataScript_fragment() throws RecognitionException {
		ParserRuleReturnScope opt =null;


		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:15: (opt= taskOptions )
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:381:15: opt= taskOptions
		{
		pushFollow(FOLLOW_taskOptions_in_synpred94_BigDataScript3334);
		opt=taskOptions();
		state._fsp--;
		if (state.failed) return ;

		}

	}
	// $ANTLR end synpred94_BigDataScript

	// Delegated rules

	public final boolean synpred74_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred74_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred94_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred94_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred55_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred55_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred56_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred56_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred5_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred5_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred70_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred70_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred75_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred75_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred3_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred3_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred10_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred10_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred46_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred46_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred45_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred45_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred82_BigDataScript() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred82_BigDataScript_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}



	public static final BitSet FOLLOW_mainElement_in_main1403 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_EOF_in_main1407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_varDeclaration_in_mainElement1430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_mainElement1434 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SEMI_in_semi1444 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_NEWLINE_in_semi1450 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_type_in_typeList1460 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_COMMA_in_typeList1463 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_type_in_typeList1465 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_varDeclaration_in_memberDecl1479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_methodDeclaration_in_memberDecl1483 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_varDeclaration1494 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_variableInit_in_varDeclaration1498 = new BitSet(new long[]{0x0000000000800000L,0x0000000080001000L});
	public static final BitSet FOLLOW_COMMA_in_varDeclaration1501 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_variableInit_in_varDeclaration1505 = new BitSet(new long[]{0x0000000000800000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_varDeclaration1509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENTIFIER_in_variableInit1534 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_EQ_in_variableInit1537 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_variableInitializer_in_variableInit1541 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer1566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignmentExpression_in_variableInitializer1570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACE_in_arrayInitializer1579 = new BitSet(new long[]{0x1A20008000800200L,0x0018516012140020L});
	public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1582 = new BitSet(new long[]{0x0000000000800000L,0x0000000002000000L});
	public static final BitSet FOLLOW_COMMA_in_arrayInitializer1585 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1587 = new BitSet(new long[]{0x0000000000800000L,0x0000000002000000L});
	public static final BitSet FOLLOW_COMMA_in_arrayInitializer1594 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_RBRACE_in_arrayInitializer1598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_methodDeclaration1609 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration1613 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_formalParameters_in_methodDeclaration1617 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_methodDeclaration1621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_typePrimitive_in_type1647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_typeArrayList_in_type1652 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_typeMap_in_type1657 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primitiveType_in_typePrimitive1669 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_typePrimitive_in_typeArrayList1692 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_LBRACKET_in_typeArrayList1694 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_RBRACKET_in_typeArrayList1696 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primitiveType_in_typeMap1719 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_LBRACE_in_typeMap1721 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_RBRACE_in_typeMap1723 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primitiveType_in_typeMap1744 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_LBRACE_in_typeMap1746 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_primitiveType_in_typeMap1750 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_RBRACE_in_typeMap1752 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LT_in_typeArguments1799 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_type_in_typeArguments1801 = new BitSet(new long[]{0x0004000000800000L});
	public static final BitSet FOLLOW_COMMA_in_typeArguments1804 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_type_in_typeArguments1806 = new BitSet(new long[]{0x0004000000800000L});
	public static final BitSet FOLLOW_GT_in_typeArguments1810 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_formalParameters1819 = new BitSet(new long[]{0x0100000000008000L,0x0400001048000000L});
	public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters1823 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_formalParameters1827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_formalParameterDecl_in_formalParameterDecls1839 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_COMMA_in_formalParameterDecls1842 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_formalParameterDecl_in_formalParameterDecls1846 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_type_in_formalParameterDecl1868 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_variableInit_in_formalParameterDecl1872 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_ellipsisParameterDecl1895 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_ELLIPSIS_in_ellipsisParameterDecl1897 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_ellipsisParameterDecl1899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_parExpression1912 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_parExpression1915 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_parExpression1917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignmentExpression_in_expressionList1927 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_COMMA_in_expressionList1930 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_expressionList1933 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_sysExpression_in_assignmentExpression1945 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_EQ_in_assignmentExpression1948 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_assignmentExpression1951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditionalExpression_in_sysExpression1962 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sys_in_sysExpression1968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_task_in_sysExpression1974 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression1985 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
	public static final BitSet FOLLOW_QUES_in_conditionalExpression1988 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_conditionalExpression1991 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_COLON_in_conditionalExpression1993 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression1995 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression2006 = new BitSet(new long[]{0x0000000000001002L});
	public static final BitSet FOLLOW_BARBAR_in_conditionalOrExpression2009 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression2012 = new BitSet(new long[]{0x0000000000001002L});
	public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression2023 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_AMPAMP_in_conditionalAndExpression2026 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression2029 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression2040 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_BAR_in_inclusiveOrExpression2043 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression2046 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression2057 = new BitSet(new long[]{0x0000000000040002L});
	public static final BitSet FOLLOW_CARET_in_exclusiveOrExpression2060 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression2063 = new BitSet(new long[]{0x0000000000040002L});
	public static final BitSet FOLLOW_equalityExpression_in_andExpression2074 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AMP_in_andExpression2077 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_equalityExpression_in_andExpression2080 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_relationalExpression_in_equalityExpression2091 = new BitSet(new long[]{0x0000000200000402L});
	public static final BitSet FOLLOW_set_in_equalityExpression2095 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_relationalExpression_in_equalityExpression2104 = new BitSet(new long[]{0x0000000200000402L});
	public static final BitSet FOLLOW_additiveExpression_in_relationalExpression2114 = new BitSet(new long[]{0x2006000000000002L,0x0000000000000040L});
	public static final BitSet FOLLOW_set_in_relationalExpression2117 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_additiveExpression_in_relationalExpression2134 = new BitSet(new long[]{0x2006000000000002L,0x0000000000000040L});
	public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression2144 = new BitSet(new long[]{0x0000000000000002L,0x0000004000040000L});
	public static final BitSet FOLLOW_set_in_additiveExpression2148 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression2157 = new BitSet(new long[]{0x0000000000000002L,0x0000004000040000L});
	public static final BitSet FOLLOW_methodExpresion_in_multiplicativeExpression2167 = new BitSet(new long[]{0x0000000000000002L,0x0000000500010000L});
	public static final BitSet FOLLOW_set_in_multiplicativeExpression2171 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_methodExpresion_in_multiplicativeExpression2184 = new BitSet(new long[]{0x0000000000000002L,0x0000000500010000L});
	public static final BitSet FOLLOW_listExpresion_in_methodExpresion2196 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_DOT_in_methodExpresion2204 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_methodExpresion2207 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_methodExpresion2209 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_methodExpresion2212 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_DOT_in_methodExpresion2219 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_methodExpresion2222 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_arguments_in_methodExpresion2224 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_mapExpresion_in_listExpresion2242 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_LBRACKET_in_listExpresion2245 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_listExpresion2248 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_RBRACKET_in_listExpresion2250 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_unaryExpression_in_mapExpresion2263 = new BitSet(new long[]{0x0800000000000002L});
	public static final BitSet FOLLOW_LBRACE_in_mapExpresion2266 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_mapExpresion2269 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_RBRACE_in_mapExpresion2271 = new BitSet(new long[]{0x0800000000000002L});
	public static final BitSet FOLLOW_PLUS_in_unaryExpression2284 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression2287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUB_in_unaryExpression2293 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression2296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpression2308 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression2312 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUBSUB_in_unaryExpression2334 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression2338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression2358 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_unaryExpressionNotPlusMinus2371 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus2374 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BANG_in_unaryExpressionNotPlusMinus2380 = new BitSet(new long[]{0x1A20008000000200L,0x0018016010140020L});
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus2383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus2391 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_SUBSUB_in_unaryExpressionNotPlusMinus2395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus2418 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpressionNotPlusMinus2422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus2442 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parExpression_in_primary2455 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functionCall_in_primary2461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENTIFIER_in_primary2469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_LITERAL_in_primary2489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_REAL_LITERAL_in_primary2509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_primary2529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_in_primary2547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_in_primary2553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_primary2561 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FALSE_in_primary2583 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACKET_in_list2609 = new BitSet(new long[]{0x1A20008000000200L,0x0018516014140020L});
	public static final BitSet FOLLOW_expressionList_in_list2614 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_RBRACKET_in_list2618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACE_in_map2641 = new BitSet(new long[]{0x1A20008000000200L,0x0018516012140020L});
	public static final BitSet FOLLOW_mapTuple_in_map2647 = new BitSet(new long[]{0x0000000000800000L,0x0000000002000000L});
	public static final BitSet FOLLOW_COMMA_in_map2650 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_mapTuple_in_map2654 = new BitSet(new long[]{0x0000000000800000L,0x0000000002000000L});
	public static final BitSet FOLLOW_RBRACE_in_map2662 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignmentExpression_in_mapTuple2684 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_ARROW_in_mapTuple2686 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_mapTuple2689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arguments2698 = new BitSet(new long[]{0x1A20008000000200L,0x0018516050140020L});
	public static final BitSet FOLLOW_expressionList_in_arguments2703 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_arguments2707 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENTIFIER_in_functionCall2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_functionCall2733 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_functionCall2735 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENTIFIER_in_functionCall2755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_arguments_in_functionCall2759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACE_in_block2787 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170BA141020L});
	public static final BitSet FOLLOW_blockStatement_in_block2792 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170BA141020L});
	public static final BitSet FOLLOW_RBRACE_in_block2796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement2817 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_blockStatement2821 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement2830 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_localVariableDeclarationStatement2832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_localVariableDeclaration2844 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_variableInit_in_localVariableDeclaration2848 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_COMMA_in_localVariableDeclaration2851 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_variableInit_in_localVariableDeclaration2855 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_assignmentExpression_in_statement2881 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement2884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_block_in_statement2891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BREAK_in_statement2897 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement2900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CONTINUE_in_statement2907 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement2910 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CHECKPOINT_in_statement2917 = new BitSet(new long[]{0x1A20008000000200L,0x0018516090141020L});
	public static final BitSet FOLLOW_assignmentExpression_in_statement2920 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement2923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXIT_in_statement2930 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_statement2933 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement2935 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_forstatement_in_statement2942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_statement2948 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_parExpression_in_statement2952 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_statement2956 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_ELSE_in_statement2959 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_statement2963 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_methodDeclaration_in_statement2987 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RETURN_in_statement2993 = new BitSet(new long[]{0x1A20008000000200L,0x0018516090141020L});
	public static final BitSet FOLLOW_assignmentExpression_in_statement2997 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement3002 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WAIT_in_statement3009 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_statement3012 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHILE_in_statement3019 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_parExpression_in_statement3023 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_statement3027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_semi_in_statement3046 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_forstatement3061 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_forstatement3063 = new BitSet(new long[]{0x1B20008000008200L,0x0418517098140020L});
	public static final BitSet FOLLOW_forInit_in_forstatement3069 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_SEMI_in_forstatement3074 = new BitSet(new long[]{0x1A20008000000200L,0x0018516090140020L});
	public static final BitSet FOLLOW_forCond_in_forstatement3080 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_SEMI_in_forstatement3085 = new BitSet(new long[]{0x1A20008000000200L,0x0018516050140020L});
	public static final BitSet FOLLOW_forEnd_in_forstatement3091 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_forstatement3096 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_forstatement3100 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_forstatement3128 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_forstatement3130 = new BitSet(new long[]{0x0100000000008000L,0x0400001008000000L});
	public static final BitSet FOLLOW_forInit2_in_forstatement3134 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_COLON_in_forstatement3136 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_forstatement3140 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_forstatement3142 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_forstatement3146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_localVariableDeclaration_in_forInit3178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expressionList_in_forInit3197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignmentExpression_in_forCond3223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expressionList_in_forEnd3246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_forInit23269 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_forInit23273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SYS_LITERAL_in_sys3304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TASK_in_task3329 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_taskOptions_in_task3334 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_task3340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_taskOptions3367 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_taskOptions3371 = new BitSet(new long[]{0x0000000000800000L,0x0000000040000000L});
	public static final BitSet FOLLOW_COMMA_in_taskOptions3374 = new BitSet(new long[]{0x1A20008000000200L,0x0018516010140020L});
	public static final BitSet FOLLOW_assignmentExpression_in_taskOptions3378 = new BitSet(new long[]{0x0000000000800000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_taskOptions3382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SEMI_in_synpred3_BigDataScript1444 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NEWLINE_in_synpred5_BigDataScript1450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arrayInitializer_in_synpred10_BigDataScript1566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOT_in_synpred45_BigDataScript2204 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred45_BigDataScript2207 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_synpred45_BigDataScript2209 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred45_BigDataScript2212 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOT_in_synpred46_BigDataScript2219 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred46_BigDataScript2222 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_arguments_in_synpred46_BigDataScript2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primary_in_synpred55_BigDataScript2391 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_SUBSUB_in_synpred55_BigDataScript2395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_primary_in_synpred56_BigDataScript2418 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_PLUSPLUS_in_synpred56_BigDataScript2422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred70_BigDataScript2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_synpred70_BigDataScript2733 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred70_BigDataScript2735 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignmentExpression_in_synpred74_BigDataScript2881 = new BitSet(new long[]{0x0000000000000000L,0x0000000080001000L});
	public static final BitSet FOLLOW_semi_in_synpred74_BigDataScript2884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_block_in_synpred75_BigDataScript2891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ELSE_in_synpred82_BigDataScript2959 = new BitSet(new long[]{0x1B60018402218200L,0x1C185170B8141020L});
	public static final BitSet FOLLOW_statement_in_synpred82_BigDataScript2963 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_taskOptions_in_synpred94_BigDataScript3334 = new BitSet(new long[]{0x0000000000000002L});
}
