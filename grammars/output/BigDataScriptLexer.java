// $ANTLR 3.x /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g 2013-01-22 18:54:36

package ca.mcgill.mcb.pcingola.bigDataScript.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class BigDataScriptLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public BigDataScriptLexer() {} 
	public BigDataScriptLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public BigDataScriptLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g"; }

	// $ANTLR start "IntegerNumber"
	public final void mIntegerNumber() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:68:3: ( '0' | '1' .. '9' ( '0' .. '9' )* | '0' ( '0' .. '7' )+ | HexPrefix ( HexDigit )+ )
			int alt4=4;
			int LA4_0 = input.LA(1);
			if ( (LA4_0=='0') ) {
				switch ( input.LA(2) ) {
				case 'X':
				case 'x':
					{
					alt4=4;
					}
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					{
					alt4=3;
					}
					break;
				default:
					alt4=1;
				}
			}
			else if ( ((LA4_0 >= '1' && LA4_0 <= '9')) ) {
				alt4=2;
			}
			else {
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:68:5: '0'
					{
					match('0'); 
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:69:5: '1' .. '9' ( '0' .. '9' )*
					{
					matchRange('1','9'); 
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:69:14: ( '0' .. '9' )*
					loop1:
					do {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop1;
						}
					} while (true);

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:70:5: '0' ( '0' .. '7' )+
					{
					match('0'); 
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:70:9: ( '0' .. '7' )+
					int cnt2=0;
					loop2:
					do {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( ((LA2_0 >= '0' && LA2_0 <= '7')) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt2 >= 1 ) break loop2;
								EarlyExitException eee =
									new EarlyExitException(2, input);
								throw eee;
						}
						cnt2++;
					} while (true);

					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:71:5: HexPrefix ( HexDigit )+
					{
					mHexPrefix(); 

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:71:15: ( HexDigit )+
					int cnt3=0;
					loop3:
					do {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( ((LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'F')||(LA3_0 >= 'a' && LA3_0 <= 'f')) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt3 >= 1 ) break loop3;
								EarlyExitException eee =
									new EarlyExitException(3, input);
								throw eee;
						}
						cnt3++;
					} while (true);

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IntegerNumber"

	// $ANTLR start "EscapeSequence"
	public final void mEscapeSequence() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:3: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' | ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:5: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' | ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) )
			{
			match('\\'); 
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:10: ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' | ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) ( '0' .. '7' ) | ( '0' .. '7' ) )
			int alt5=11;
			switch ( input.LA(1) ) {
			case 'b':
				{
				alt5=1;
				}
				break;
			case 't':
				{
				alt5=2;
				}
				break;
			case 'n':
				{
				alt5=3;
				}
				break;
			case 'f':
				{
				alt5=4;
				}
				break;
			case 'r':
				{
				alt5=5;
				}
				break;
			case '\"':
				{
				alt5=6;
				}
				break;
			case '\'':
				{
				alt5=7;
				}
				break;
			case '\\':
				{
				alt5=8;
				}
				break;
			case '0':
			case '1':
			case '2':
			case '3':
				{
				int LA5_9 = input.LA(2);
				if ( ((LA5_9 >= '0' && LA5_9 <= '7')) ) {
					int LA5_11 = input.LA(3);
					if ( ((LA5_11 >= '0' && LA5_11 <= '7')) ) {
						alt5=9;
					}
					else {
						alt5=10;
					}
				}
				else {
					alt5=11;
				}
				}
				break;
			case '4':
			case '5':
			case '6':
			case '7':
				{
				int LA5_10 = input.LA(2);
				if ( ((LA5_10 >= '0' && LA5_10 <= '7')) ) {
					alt5=10;
				}
				else {
					alt5=11;
				}
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}
			switch (alt5) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:12: 'b'
					{
					match('b'); 
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:18: 't'
					{
					match('t'); 
					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:24: 'n'
					{
					match('n'); 
					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:30: 'f'
					{
					match('f'); 
					}
					break;
				case 5 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:36: 'r'
					{
					match('r'); 
					}
					break;
				case 6 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:42: '\\\"'
					{
					match('\"'); 
					}
					break;
				case 7 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:49: '\\''
					{
					match('\''); 
					}
					break;
				case 8 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:67:56: '\\\\'
					{
					match('\\'); 
					}
					break;
				case 9 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:68:5: ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 10 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:69:5: ( '0' .. '7' ) ( '0' .. '7' )
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 11 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:70:5: ( '0' .. '7' )
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EscapeSequence"

	// $ANTLR start "EscapedNewLine"
	public final void mEscapedNewLine() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:25: ( '\\\\' ( '\\r' | '\\n' | '\\r\\n' ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:27: '\\\\' ( '\\r' | '\\n' | '\\r\\n' )
			{
			match('\\'); 
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:32: ( '\\r' | '\\n' | '\\r\\n' )
			int alt6=3;
			int LA6_0 = input.LA(1);
			if ( (LA6_0=='\r') ) {
				int LA6_1 = input.LA(2);
				if ( (LA6_1=='\n') ) {
					alt6=3;
				}
				else {
					alt6=1;
				}
			}
			else if ( (LA6_0=='\n') ) {
				alt6=2;
			}
			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}
			switch (alt6) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:34: '\\r'
					{
					match('\r'); 
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:41: '\\n'
					{
					match('\n'); 
					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:74:48: '\\r\\n'
					{
					match("\r\n"); 

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EscapedNewLine"

	// $ANTLR start "Exponent"
	public final void mExponent() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:76:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:76:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:76:35: ( '+' | '-' )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0=='+'||LA7_0=='-') ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
					{
					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:76:50: ( '0' .. '9' )+
			int cnt8=0;
			loop8:
			do {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt8 >= 1 ) break loop8;
						EarlyExitException eee =
							new EarlyExitException(8, input);
						throw eee;
				}
				cnt8++;
			} while (true);

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Exponent"

	// $ANTLR start "HexPrefix"
	public final void mHexPrefix() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:78:20: ( '0x' | '0X' )
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0=='0') ) {
				int LA9_1 = input.LA(2);
				if ( (LA9_1=='x') ) {
					alt9=1;
				}
				else if ( (LA9_1=='X') ) {
					alt9=2;
				}
				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else {
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:78:22: '0x'
					{
					match("0x"); 

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:78:29: '0X'
					{
					match("0X"); 

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HexPrefix"

	// $ANTLR start "HexDigit"
	public final void mHexDigit() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:80:19: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HexDigit"

	// $ANTLR start "NonIntegerNumber"
	public final void mNonIntegerNumber() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:3: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent | ( '0' .. '9' )+ )
			int alt17=4;
			alt17 = dfa17.predict(input);
			switch (alt17) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:5: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )?
					{
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:5: ( '0' .. '9' )+
					int cnt10=0;
					loop10:
					do {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt10 >= 1 ) break loop10;
								EarlyExitException eee =
									new EarlyExitException(10, input);
								throw eee;
						}
						cnt10++;
					} while (true);

					match('.'); 
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:23: ( '0' .. '9' )*
					loop11:
					do {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop11;
						}
					} while (true);

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:37: ( Exponent )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0=='E'||LA12_0=='e') ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:84:37: Exponent
							{
							mExponent(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:85:5: '.' ( '0' .. '9' )+ ( Exponent )?
					{
					match('.'); 
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:85:9: ( '0' .. '9' )+
					int cnt13=0;
					loop13:
					do {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
								EarlyExitException eee =
									new EarlyExitException(13, input);
								throw eee;
						}
						cnt13++;
					} while (true);

					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:85:25: ( Exponent )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0=='E'||LA14_0=='e') ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:85:25: Exponent
							{
							mExponent(); 

							}
							break;

					}

					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:86:5: ( '0' .. '9' )+ Exponent
					{
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:86:5: ( '0' .. '9' )+
					int cnt15=0;
					loop15:
					do {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt15 >= 1 ) break loop15;
								EarlyExitException eee =
									new EarlyExitException(15, input);
								throw eee;
						}
						cnt15++;
					} while (true);

					mExponent(); 

					}
					break;
				case 4 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:87:5: ( '0' .. '9' )+
					{
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:87:5: ( '0' .. '9' )+
					int cnt16=0;
					loop16:
					do {
						int alt16=2;
						int LA16_0 = input.LA(1);
						if ( ((LA16_0 >= '0' && LA16_0 <= '9')) ) {
							alt16=1;
						}

						switch (alt16) {
						case 1 :
							// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt16 >= 1 ) break loop16;
								EarlyExitException eee =
									new EarlyExitException(16, input);
								throw eee;
						}
						cnt16++;
					} while (true);

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NonIntegerNumber"

	// $ANTLR start "SysMultiLine"
	public final void mSysMultiLine() throws RecognitionException {
		try {
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:90:23: ( ( EscapedNewLine |~ ( '\\r' | '\\n' ) )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:90:25: ( EscapedNewLine |~ ( '\\r' | '\\n' ) )*
			{
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:90:25: ( EscapedNewLine |~ ( '\\r' | '\\n' ) )*
			loop18:
			do {
				int alt18=3;
				int LA18_0 = input.LA(1);
				if ( (LA18_0=='\\') ) {
					int LA18_2 = input.LA(2);
					if ( (LA18_2=='\n'||LA18_2=='\r') ) {
						alt18=1;
					}
					else {
						alt18=2;
					}

				}
				else if ( ((LA18_0 >= '\u0000' && LA18_0 <= '\t')||(LA18_0 >= '\u000B' && LA18_0 <= '\f')||(LA18_0 >= '\u000E' && LA18_0 <= '[')||(LA18_0 >= ']' && LA18_0 <= '\uFFFF')) ) {
					alt18=2;
				}

				switch (alt18) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:90:27: EscapedNewLine
					{
					mEscapedNewLine(); 

					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:90:44: ~ ( '\\r' | '\\n' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop18;
				}
			} while (true);

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SysMultiLine"

	// $ANTLR start "INT_LITERAL"
	public final void mINT_LITERAL() throws RecognitionException {
		try {
			int _type = INT_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:96:13: ( IntegerNumber )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:96:15: IntegerNumber
			{
			mIntegerNumber(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT_LITERAL"

	// $ANTLR start "NEWLINE"
	public final void mNEWLINE() throws RecognitionException {
		try {
			int _type = NEWLINE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:9: ( ( '\\r' | '\\n' | '\\r\\n' ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:11: ( '\\r' | '\\n' | '\\r\\n' )
			{
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:11: ( '\\r' | '\\n' | '\\r\\n' )
			int alt19=3;
			int LA19_0 = input.LA(1);
			if ( (LA19_0=='\r') ) {
				int LA19_1 = input.LA(2);
				if ( (LA19_1=='\n') ) {
					alt19=3;
				}
				else {
					alt19=1;
				}
			}
			else if ( (LA19_0=='\n') ) {
				alt19=2;
			}
			else {
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}
			switch (alt19) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:13: '\\r'
					{
					match('\r'); 
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:20: '\\n'
					{
					match('\n'); 
					}
					break;
				case 3 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:98:27: '\\r\\n'
					{
					match("\r\n"); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NEWLINE"

	// $ANTLR start "REAL_LITERAL"
	public final void mREAL_LITERAL() throws RecognitionException {
		try {
			int _type = REAL_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:100:14: ( NonIntegerNumber )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:100:16: NonIntegerNumber
			{
			mNonIntegerNumber(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "REAL_LITERAL"

	// $ANTLR start "STRING_LITERAL"
	public final void mSTRING_LITERAL() throws RecognitionException {
		try {
			int _type = STRING_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:16: ( '\"' ( (~ ( '\"' | '\\\\' ) | ( '\\\\' . ) )* ) '\"' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:18: '\"' ( (~ ( '\"' | '\\\\' ) | ( '\\\\' . ) )* ) '\"'
			{
			match('\"'); 
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:22: ( (~ ( '\"' | '\\\\' ) | ( '\\\\' . ) )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:24: (~ ( '\"' | '\\\\' ) | ( '\\\\' . ) )*
			{
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:24: (~ ( '\"' | '\\\\' ) | ( '\\\\' . ) )*
			loop20:
			do {
				int alt20=3;
				int LA20_0 = input.LA(1);
				if ( ((LA20_0 >= '\u0000' && LA20_0 <= '!')||(LA20_0 >= '#' && LA20_0 <= '[')||(LA20_0 >= ']' && LA20_0 <= '\uFFFF')) ) {
					alt20=1;
				}
				else if ( (LA20_0=='\\') ) {
					alt20=2;
				}

				switch (alt20) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:25: ~ ( '\"' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:43: ( '\\\\' . )
					{
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:43: ( '\\\\' . )
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:103:44: '\\\\' .
					{
					match('\\'); 
					matchAny(); 
					}

					}
					break;

				default :
					break loop20;
				}
			} while (true);

			}

			match('\"'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_LITERAL"

	// $ANTLR start "SYS_LITERAL"
	public final void mSYS_LITERAL() throws RecognitionException {
		try {
			int _type = SYS_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:105:13: ( 'sys' ( ' ' | '\\t' ) SysMultiLine )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:105:15: 'sys' ( ' ' | '\\t' ) SysMultiLine
			{
			match("sys"); 

			if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			mSysMultiLine(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SYS_LITERAL"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:107:4: ( ( ' ' | '\\t' | '\\u000C' ) )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:107:6: ( ' ' | '\\t' | '\\u000C' )
			{
			if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			 skip(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:109:33: ( '/*' ( . )* '*/' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:109:35: '/*' ( . )* '*/'
			{
			match("/*"); 

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:109:40: ( . )*
			loop21:
			do {
				int alt21=2;
				int LA21_0 = input.LA(1);
				if ( (LA21_0=='*') ) {
					int LA21_1 = input.LA(2);
					if ( (LA21_1=='/') ) {
						alt21=2;
					}
					else if ( ((LA21_1 >= '\u0000' && LA21_1 <= '.')||(LA21_1 >= '0' && LA21_1 <= '\uFFFF')) ) {
						alt21=1;
					}

				}
				else if ( ((LA21_0 >= '\u0000' && LA21_0 <= ')')||(LA21_0 >= '+' && LA21_0 <= '\uFFFF')) ) {
					alt21=1;
				}

				switch (alt21) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:109:40: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop21;
				}
			} while (true);

			match("*/"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "LINE_COMMENT"
	public final void mLINE_COMMENT() throws RecognitionException {
		try {
			int _type = LINE_COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:111:14: ( '//' (~ ( '\\n' | '\\r' ) )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:111:16: '//' (~ ( '\\n' | '\\r' ) )*
			{
			match("//"); 

			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:111:21: (~ ( '\\n' | '\\r' ) )*
			loop22:
			do {
				int alt22=2;
				int LA22_0 = input.LA(1);
				if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\t')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\uFFFF')) ) {
					alt22=1;
				}

				switch (alt22) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop22;
				}
			} while (true);

			 skip(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LINE_COMMENT"

	// $ANTLR start "BOOL"
	public final void mBOOL() throws RecognitionException {
		try {
			int _type = BOOL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:113:6: ( 'bool' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:113:8: 'bool'
			{
			match("bool"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BOOL"

	// $ANTLR start "BREAK"
	public final void mBREAK() throws RecognitionException {
		try {
			int _type = BREAK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:114:7: ( 'break' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:114:9: 'break'
			{
			match("break"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BREAK"

	// $ANTLR start "BY"
	public final void mBY() throws RecognitionException {
		try {
			int _type = BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:115:4: ( 'by' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:115:6: 'by'
			{
			match("by"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BY"

	// $ANTLR start "CASE"
	public final void mCASE() throws RecognitionException {
		try {
			int _type = CASE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:116:6: ( 'case' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:116:8: 'case'
			{
			match("case"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CASE"

	// $ANTLR start "CONTINUE"
	public final void mCONTINUE() throws RecognitionException {
		try {
			int _type = CONTINUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:117:10: ( 'continue' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:117:12: 'continue'
			{
			match("continue"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CONTINUE"

	// $ANTLR start "DEFAULT"
	public final void mDEFAULT() throws RecognitionException {
		try {
			int _type = DEFAULT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:118:9: ( 'default' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:118:11: 'default'
			{
			match("default"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DEFAULT"

	// $ANTLR start "DO"
	public final void mDO() throws RecognitionException {
		try {
			int _type = DO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:119:4: ( 'do' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:119:6: 'do'
			{
			match("do"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DO"

	// $ANTLR start "REAL"
	public final void mREAL() throws RecognitionException {
		try {
			int _type = REAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:120:6: ( 'real' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:120:8: 'real'
			{
			match("real"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "REAL"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:121:8: ( 'string' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:121:10: 'string'
			{
			match("string"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "ELSE"
	public final void mELSE() throws RecognitionException {
		try {
			int _type = ELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:122:6: ( 'else' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:122:8: 'else'
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELSE"

	// $ANTLR start "ENUM"
	public final void mENUM() throws RecognitionException {
		try {
			int _type = ENUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:123:6: ( 'enum' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:123:8: 'enum'
			{
			match("enum"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ENUM"

	// $ANTLR start "EXTENDS"
	public final void mEXTENDS() throws RecognitionException {
		try {
			int _type = EXTENDS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:124:9: ( 'extends' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:124:11: 'extends'
			{
			match("extends"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXTENDS"

	// $ANTLR start "FOR"
	public final void mFOR() throws RecognitionException {
		try {
			int _type = FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:125:5: ( 'for' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:125:7: 'for'
			{
			match("for"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FOR"

	// $ANTLR start "IF"
	public final void mIF() throws RecognitionException {
		try {
			int _type = IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:126:4: ( 'if' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:126:6: 'if'
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IF"

	// $ANTLR start "IMPORT"
	public final void mIMPORT() throws RecognitionException {
		try {
			int _type = IMPORT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:127:8: ( 'import' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:127:10: 'import'
			{
			match("import"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IMPORT"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:128:5: ( 'int' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:128:7: 'int'
			{
			match("int"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "NEW"
	public final void mNEW() throws RecognitionException {
		try {
			int _type = NEW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:129:5: ( 'new' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:129:7: 'new'
			{
			match("new"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NEW"

	// $ANTLR start "PACKAGE"
	public final void mPACKAGE() throws RecognitionException {
		try {
			int _type = PACKAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:130:9: ( 'package' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:130:11: 'package'
			{
			match("package"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PACKAGE"

	// $ANTLR start "RETURN"
	public final void mRETURN() throws RecognitionException {
		try {
			int _type = RETURN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:131:8: ( 'return' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:131:10: 'return'
			{
			match("return"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RETURN"

	// $ANTLR start "SUPER"
	public final void mSUPER() throws RecognitionException {
		try {
			int _type = SUPER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:132:7: ( 'super' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:132:9: 'super'
			{
			match("super"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SUPER"

	// $ANTLR start "SWITCH"
	public final void mSWITCH() throws RecognitionException {
		try {
			int _type = SWITCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:133:8: ( 'switch' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:133:10: 'switch'
			{
			match("switch"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SWITCH"

	// $ANTLR start "THIS"
	public final void mTHIS() throws RecognitionException {
		try {
			int _type = THIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:134:6: ( 'this' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:134:8: 'this'
			{
			match("this"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THIS"

	// $ANTLR start "VOID"
	public final void mVOID() throws RecognitionException {
		try {
			int _type = VOID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:135:6: ( 'void' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:135:8: 'void'
			{
			match("void"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VOID"

	// $ANTLR start "WHILE"
	public final void mWHILE() throws RecognitionException {
		try {
			int _type = WHILE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:136:7: ( 'while' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:136:9: 'while'
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHILE"

	// $ANTLR start "TRUE"
	public final void mTRUE() throws RecognitionException {
		try {
			int _type = TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:137:6: ( 'true' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:137:8: 'true'
			{
			match("true"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRUE"

	// $ANTLR start "FALSE"
	public final void mFALSE() throws RecognitionException {
		try {
			int _type = FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:138:7: ( 'false' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:138:9: 'false'
			{
			match("false"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FALSE"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:139:8: ( '(' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:139:10: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:140:8: ( ')' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:140:10: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "LBRACE"
	public final void mLBRACE() throws RecognitionException {
		try {
			int _type = LBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:141:8: ( '{' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:141:10: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACE"

	// $ANTLR start "RBRACE"
	public final void mRBRACE() throws RecognitionException {
		try {
			int _type = RBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:142:8: ( '}' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:142:10: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACE"

	// $ANTLR start "LBRACKET"
	public final void mLBRACKET() throws RecognitionException {
		try {
			int _type = LBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:143:10: ( '[' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:143:12: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACKET"

	// $ANTLR start "RBRACKET"
	public final void mRBRACKET() throws RecognitionException {
		try {
			int _type = RBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:144:10: ( ']' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:144:12: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACKET"

	// $ANTLR start "SEMI"
	public final void mSEMI() throws RecognitionException {
		try {
			int _type = SEMI;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:145:6: ( ';' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:145:8: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMI"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:146:7: ( ',' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:146:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			int _type = DOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:147:5: ( '.' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:147:7: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "ELLIPSIS"
	public final void mELLIPSIS() throws RecognitionException {
		try {
			int _type = ELLIPSIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:148:10: ( '...' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:148:12: '...'
			{
			match("..."); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELLIPSIS"

	// $ANTLR start "EQ"
	public final void mEQ() throws RecognitionException {
		try {
			int _type = EQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:149:4: ( '=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:149:6: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQ"

	// $ANTLR start "BANG"
	public final void mBANG() throws RecognitionException {
		try {
			int _type = BANG;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:150:6: ( '!' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:150:8: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BANG"

	// $ANTLR start "TILDE"
	public final void mTILDE() throws RecognitionException {
		try {
			int _type = TILDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:151:7: ( '~' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:151:9: '~'
			{
			match('~'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TILDE"

	// $ANTLR start "QUES"
	public final void mQUES() throws RecognitionException {
		try {
			int _type = QUES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:152:6: ( '?' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:152:8: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUES"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:153:7: ( ':' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:153:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "EQEQ"
	public final void mEQEQ() throws RecognitionException {
		try {
			int _type = EQEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:154:6: ( '==' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:154:8: '=='
			{
			match("=="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQEQ"

	// $ANTLR start "AMPAMP"
	public final void mAMPAMP() throws RecognitionException {
		try {
			int _type = AMPAMP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:155:8: ( '&&' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:155:10: '&&'
			{
			match("&&"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMPAMP"

	// $ANTLR start "BARBAR"
	public final void mBARBAR() throws RecognitionException {
		try {
			int _type = BARBAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:156:8: ( '||' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:156:10: '||'
			{
			match("||"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BARBAR"

	// $ANTLR start "PLUSPLUS"
	public final void mPLUSPLUS() throws RecognitionException {
		try {
			int _type = PLUSPLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:157:10: ( '++' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:157:12: '++'
			{
			match("++"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUSPLUS"

	// $ANTLR start "SUBSUB"
	public final void mSUBSUB() throws RecognitionException {
		try {
			int _type = SUBSUB;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:158:8: ( '--' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:158:10: '--'
			{
			match("--"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SUBSUB"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:159:6: ( '+' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:159:8: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "SUB"
	public final void mSUB() throws RecognitionException {
		try {
			int _type = SUB;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:160:5: ( '-' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:160:7: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SUB"

	// $ANTLR start "STAR"
	public final void mSTAR() throws RecognitionException {
		try {
			int _type = STAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:161:6: ( '*' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:161:8: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAR"

	// $ANTLR start "SLASH"
	public final void mSLASH() throws RecognitionException {
		try {
			int _type = SLASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:162:7: ( '/' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:162:9: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SLASH"

	// $ANTLR start "AMP"
	public final void mAMP() throws RecognitionException {
		try {
			int _type = AMP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:163:5: ( '&' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:163:7: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMP"

	// $ANTLR start "BAR"
	public final void mBAR() throws RecognitionException {
		try {
			int _type = BAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:164:5: ( '|' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:164:7: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BAR"

	// $ANTLR start "CARET"
	public final void mCARET() throws RecognitionException {
		try {
			int _type = CARET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:165:7: ( '^' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:165:9: '^'
			{
			match('^'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CARET"

	// $ANTLR start "PERCENT"
	public final void mPERCENT() throws RecognitionException {
		try {
			int _type = PERCENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:166:9: ( '%' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:166:11: '%'
			{
			match('%'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENT"

	// $ANTLR start "PLUSEQ"
	public final void mPLUSEQ() throws RecognitionException {
		try {
			int _type = PLUSEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:167:8: ( '+=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:167:10: '+='
			{
			match("+="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUSEQ"

	// $ANTLR start "SUBEQ"
	public final void mSUBEQ() throws RecognitionException {
		try {
			int _type = SUBEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:168:7: ( '-=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:168:9: '-='
			{
			match("-="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SUBEQ"

	// $ANTLR start "STAREQ"
	public final void mSTAREQ() throws RecognitionException {
		try {
			int _type = STAREQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:169:8: ( '*=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:169:10: '*='
			{
			match("*="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAREQ"

	// $ANTLR start "SLASHEQ"
	public final void mSLASHEQ() throws RecognitionException {
		try {
			int _type = SLASHEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:170:9: ( '/=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:170:11: '/='
			{
			match("/="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SLASHEQ"

	// $ANTLR start "AMPEQ"
	public final void mAMPEQ() throws RecognitionException {
		try {
			int _type = AMPEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:171:7: ( '&=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:171:9: '&='
			{
			match("&="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMPEQ"

	// $ANTLR start "BAREQ"
	public final void mBAREQ() throws RecognitionException {
		try {
			int _type = BAREQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:172:7: ( '|=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:172:9: '|='
			{
			match("|="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BAREQ"

	// $ANTLR start "CARETEQ"
	public final void mCARETEQ() throws RecognitionException {
		try {
			int _type = CARETEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:173:9: ( '^=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:173:11: '^='
			{
			match("^="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CARETEQ"

	// $ANTLR start "PERCENTEQ"
	public final void mPERCENTEQ() throws RecognitionException {
		try {
			int _type = PERCENTEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:174:11: ( '%=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:174:13: '%='
			{
			match("%="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENTEQ"

	// $ANTLR start "MONKEYS_AT"
	public final void mMONKEYS_AT() throws RecognitionException {
		try {
			int _type = MONKEYS_AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:175:12: ( '@' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:175:14: '@'
			{
			match('@'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MONKEYS_AT"

	// $ANTLR start "BANGEQ"
	public final void mBANGEQ() throws RecognitionException {
		try {
			int _type = BANGEQ;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:176:8: ( '!=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:176:10: '!='
			{
			match("!="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BANGEQ"

	// $ANTLR start "GT"
	public final void mGT() throws RecognitionException {
		try {
			int _type = GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:177:4: ( '>' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:177:6: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GT"

	// $ANTLR start "LT"
	public final void mLT() throws RecognitionException {
		try {
			int _type = LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:178:4: ( '<' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:178:6: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LT"

	// $ANTLR start "GE"
	public final void mGE() throws RecognitionException {
		try {
			int _type = GE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:179:4: ( '>=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:179:6: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GE"

	// $ANTLR start "LE"
	public final void mLE() throws RecognitionException {
		try {
			int _type = LE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:180:4: ( '<=' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:180:6: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LE"

	// $ANTLR start "ARROW"
	public final void mARROW() throws RecognitionException {
		try {
			int _type = ARROW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:181:7: ( '=>' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:181:9: '=>'
			{
			match("=>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ARROW"

	// $ANTLR start "TASK"
	public final void mTASK() throws RecognitionException {
		try {
			int _type = TASK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:182:6: ( 'task' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:182:8: 'task'
			{
			match("task"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TASK"

	// $ANTLR start "CHECKPOINT"
	public final void mCHECKPOINT() throws RecognitionException {
		try {
			int _type = CHECKPOINT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:183:12: ( 'checkpoint' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:183:14: 'checkpoint'
			{
			match("checkpoint"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CHECKPOINT"

	// $ANTLR start "EXIT"
	public final void mEXIT() throws RecognitionException {
		try {
			int _type = EXIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:184:6: ( 'exit' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:184:8: 'exit'
			{
			match("exit"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXIT"

	// $ANTLR start "WAIT"
	public final void mWAIT() throws RecognitionException {
		try {
			int _type = WAIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:185:6: ( 'wait' )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:185:8: 'wait'
			{
			match("wait"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WAIT"

	// $ANTLR start "IDENTIFIER"
	public final void mIDENTIFIER() throws RecognitionException {
		try {
			int _type = IDENTIFIER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:186:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:186:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:186:38: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop23:
			do {
				int alt23=2;
				int LA23_0 = input.LA(1);
				if ( ((LA23_0 >= '0' && LA23_0 <= '9')||(LA23_0 >= 'A' && LA23_0 <= 'Z')||LA23_0=='_'||(LA23_0 >= 'a' && LA23_0 <= 'z')) ) {
					alt23=1;
				}

				switch (alt23) {
				case 1 :
					// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop23;
				}
			} while (true);

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IDENTIFIER"

	@Override
	public void mTokens() throws RecognitionException {
		// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:8: ( INT_LITERAL | NEWLINE | REAL_LITERAL | STRING_LITERAL | SYS_LITERAL | WS | COMMENT | LINE_COMMENT | BOOL | BREAK | BY | CASE | CONTINUE | DEFAULT | DO | REAL | STRING | ELSE | ENUM | EXTENDS | FOR | IF | IMPORT | INT | NEW | PACKAGE | RETURN | SUPER | SWITCH | THIS | VOID | WHILE | TRUE | FALSE | LPAREN | RPAREN | LBRACE | RBRACE | LBRACKET | RBRACKET | SEMI | COMMA | DOT | ELLIPSIS | EQ | BANG | TILDE | QUES | COLON | EQEQ | AMPAMP | BARBAR | PLUSPLUS | SUBSUB | PLUS | SUB | STAR | SLASH | AMP | BAR | CARET | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | AMPEQ | BAREQ | CARETEQ | PERCENTEQ | MONKEYS_AT | BANGEQ | GT | LT | GE | LE | ARROW | TASK | CHECKPOINT | EXIT | WAIT | IDENTIFIER )
		int alt24=82;
		alt24 = dfa24.predict(input);
		switch (alt24) {
			case 1 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:10: INT_LITERAL
				{
				mINT_LITERAL(); 

				}
				break;
			case 2 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:22: NEWLINE
				{
				mNEWLINE(); 

				}
				break;
			case 3 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:30: REAL_LITERAL
				{
				mREAL_LITERAL(); 

				}
				break;
			case 4 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:43: STRING_LITERAL
				{
				mSTRING_LITERAL(); 

				}
				break;
			case 5 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:58: SYS_LITERAL
				{
				mSYS_LITERAL(); 

				}
				break;
			case 6 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:70: WS
				{
				mWS(); 

				}
				break;
			case 7 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:73: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 8 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:81: LINE_COMMENT
				{
				mLINE_COMMENT(); 

				}
				break;
			case 9 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:94: BOOL
				{
				mBOOL(); 

				}
				break;
			case 10 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:99: BREAK
				{
				mBREAK(); 

				}
				break;
			case 11 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:105: BY
				{
				mBY(); 

				}
				break;
			case 12 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:108: CASE
				{
				mCASE(); 

				}
				break;
			case 13 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:113: CONTINUE
				{
				mCONTINUE(); 

				}
				break;
			case 14 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:122: DEFAULT
				{
				mDEFAULT(); 

				}
				break;
			case 15 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:130: DO
				{
				mDO(); 

				}
				break;
			case 16 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:133: REAL
				{
				mREAL(); 

				}
				break;
			case 17 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:138: STRING
				{
				mSTRING(); 

				}
				break;
			case 18 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:145: ELSE
				{
				mELSE(); 

				}
				break;
			case 19 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:150: ENUM
				{
				mENUM(); 

				}
				break;
			case 20 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:155: EXTENDS
				{
				mEXTENDS(); 

				}
				break;
			case 21 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:163: FOR
				{
				mFOR(); 

				}
				break;
			case 22 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:167: IF
				{
				mIF(); 

				}
				break;
			case 23 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:170: IMPORT
				{
				mIMPORT(); 

				}
				break;
			case 24 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:177: INT
				{
				mINT(); 

				}
				break;
			case 25 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:181: NEW
				{
				mNEW(); 

				}
				break;
			case 26 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:185: PACKAGE
				{
				mPACKAGE(); 

				}
				break;
			case 27 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:193: RETURN
				{
				mRETURN(); 

				}
				break;
			case 28 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:200: SUPER
				{
				mSUPER(); 

				}
				break;
			case 29 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:206: SWITCH
				{
				mSWITCH(); 

				}
				break;
			case 30 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:213: THIS
				{
				mTHIS(); 

				}
				break;
			case 31 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:218: VOID
				{
				mVOID(); 

				}
				break;
			case 32 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:223: WHILE
				{
				mWHILE(); 

				}
				break;
			case 33 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:229: TRUE
				{
				mTRUE(); 

				}
				break;
			case 34 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:234: FALSE
				{
				mFALSE(); 

				}
				break;
			case 35 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:240: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 36 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:247: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 37 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:254: LBRACE
				{
				mLBRACE(); 

				}
				break;
			case 38 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:261: RBRACE
				{
				mRBRACE(); 

				}
				break;
			case 39 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:268: LBRACKET
				{
				mLBRACKET(); 

				}
				break;
			case 40 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:277: RBRACKET
				{
				mRBRACKET(); 

				}
				break;
			case 41 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:286: SEMI
				{
				mSEMI(); 

				}
				break;
			case 42 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:291: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 43 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:297: DOT
				{
				mDOT(); 

				}
				break;
			case 44 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:301: ELLIPSIS
				{
				mELLIPSIS(); 

				}
				break;
			case 45 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:310: EQ
				{
				mEQ(); 

				}
				break;
			case 46 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:313: BANG
				{
				mBANG(); 

				}
				break;
			case 47 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:318: TILDE
				{
				mTILDE(); 

				}
				break;
			case 48 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:324: QUES
				{
				mQUES(); 

				}
				break;
			case 49 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:329: COLON
				{
				mCOLON(); 

				}
				break;
			case 50 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:335: EQEQ
				{
				mEQEQ(); 

				}
				break;
			case 51 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:340: AMPAMP
				{
				mAMPAMP(); 

				}
				break;
			case 52 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:347: BARBAR
				{
				mBARBAR(); 

				}
				break;
			case 53 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:354: PLUSPLUS
				{
				mPLUSPLUS(); 

				}
				break;
			case 54 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:363: SUBSUB
				{
				mSUBSUB(); 

				}
				break;
			case 55 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:370: PLUS
				{
				mPLUS(); 

				}
				break;
			case 56 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:375: SUB
				{
				mSUB(); 

				}
				break;
			case 57 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:379: STAR
				{
				mSTAR(); 

				}
				break;
			case 58 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:384: SLASH
				{
				mSLASH(); 

				}
				break;
			case 59 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:390: AMP
				{
				mAMP(); 

				}
				break;
			case 60 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:394: BAR
				{
				mBAR(); 

				}
				break;
			case 61 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:398: CARET
				{
				mCARET(); 

				}
				break;
			case 62 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:404: PERCENT
				{
				mPERCENT(); 

				}
				break;
			case 63 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:412: PLUSEQ
				{
				mPLUSEQ(); 

				}
				break;
			case 64 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:419: SUBEQ
				{
				mSUBEQ(); 

				}
				break;
			case 65 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:425: STAREQ
				{
				mSTAREQ(); 

				}
				break;
			case 66 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:432: SLASHEQ
				{
				mSLASHEQ(); 

				}
				break;
			case 67 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:440: AMPEQ
				{
				mAMPEQ(); 

				}
				break;
			case 68 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:446: BAREQ
				{
				mBAREQ(); 

				}
				break;
			case 69 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:452: CARETEQ
				{
				mCARETEQ(); 

				}
				break;
			case 70 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:460: PERCENTEQ
				{
				mPERCENTEQ(); 

				}
				break;
			case 71 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:470: MONKEYS_AT
				{
				mMONKEYS_AT(); 

				}
				break;
			case 72 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:481: BANGEQ
				{
				mBANGEQ(); 

				}
				break;
			case 73 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:488: GT
				{
				mGT(); 

				}
				break;
			case 74 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:491: LT
				{
				mLT(); 

				}
				break;
			case 75 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:494: GE
				{
				mGE(); 

				}
				break;
			case 76 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:497: LE
				{
				mLE(); 

				}
				break;
			case 77 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:500: ARROW
				{
				mARROW(); 

				}
				break;
			case 78 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:506: TASK
				{
				mTASK(); 

				}
				break;
			case 79 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:511: CHECKPOINT
				{
				mCHECKPOINT(); 

				}
				break;
			case 80 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:522: EXIT
				{
				mEXIT(); 

				}
				break;
			case 81 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:527: WAIT
				{
				mWAIT(); 

				}
				break;
			case 82 :
				// /home/pcingola/workspace/BigDataScript/grammars/BigDataScript.g:1:532: IDENTIFIER
				{
				mIDENTIFIER(); 

				}
				break;

		}
	}


	protected DFA17 dfa17 = new DFA17(this);
	protected DFA24 dfa24 = new DFA24(this);
	static final String DFA17_eotS =
		"\1\uffff\1\5\4\uffff";
	static final String DFA17_eofS =
		"\6\uffff";
	static final String DFA17_minS =
		"\2\56\4\uffff";
	static final String DFA17_maxS =
		"\1\71\1\145\4\uffff";
	static final String DFA17_acceptS =
		"\2\uffff\1\2\1\1\1\3\1\4";
	static final String DFA17_specialS =
		"\6\uffff}>";
	static final String[] DFA17_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
			"",
			"",
			"",
			""
	};

	static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
	static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
	static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
	static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
	static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
	static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
	static final short[][] DFA17_transition;

	static {
		int numStates = DFA17_transitionS.length;
		DFA17_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
		}
	}

	class DFA17 extends DFA {

		public DFA17(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 17;
			this.eot = DFA17_eot;
			this.eof = DFA17_eof;
			this.min = DFA17_min;
			this.max = DFA17_max;
			this.accept = DFA17_accept;
			this.special = DFA17_special;
			this.transition = DFA17_transition;
		}
		@Override
		public String getDescription() {
			return "83:10: fragment NonIntegerNumber : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? | '.' ( '0' .. '9' )+ ( Exponent )? | ( '0' .. '9' )+ Exponent | ( '0' .. '9' )+ );";
		}
	}

	static final String DFA24_eotS =
		"\1\uffff\2\55\1\uffff\1\62\1\uffff\1\54\1\uffff\1\72\14\54\10\uffff\1"+
		"\126\1\130\3\uffff\1\133\1\136\1\141\1\144\1\146\1\150\1\152\1\uffff\1"+
		"\154\1\156\2\uffff\1\55\1\uffff\1\55\2\uffff\4\54\4\uffff\2\54\1\165\4"+
		"\54\1\172\6\54\1\u0083\12\54\33\uffff\6\54\1\uffff\4\54\1\uffff\6\54\1"+
		"\u009e\1\54\1\uffff\1\54\1\u00a1\1\u00a2\7\54\1\uffff\3\54\1\u00ad\1\54"+
		"\1\u00af\3\54\1\u00b3\1\54\1\u00b5\1\u00b6\1\54\1\u00b8\1\uffff\2\54\2"+
		"\uffff\1\54\1\u00bc\1\u00bd\1\u00be\1\u00bf\1\54\1\u00c1\1\54\1\u00c3"+
		"\1\54\1\uffff\1\u00c5\1\uffff\3\54\1\uffff\1\54\2\uffff\1\54\1\uffff\1"+
		"\u00cb\2\54\4\uffff\1\u00ce\1\uffff\1\u00cf\1\uffff\1\u00d0\1\uffff\3"+
		"\54\1\u00d4\1\54\1\uffff\1\u00d6\1\54\3\uffff\2\54\1\u00da\1\uffff\1\u00db"+
		"\1\uffff\1\u00dc\1\u00dd\1\54\4\uffff\1\54\1\u00e0\1\uffff";
	static final String DFA24_eofS =
		"\u00e1\uffff";
	static final String DFA24_minS =
		"\1\11\2\56\1\uffff\1\56\1\uffff\1\164\1\uffff\1\52\1\157\1\141\2\145\1"+
		"\154\1\141\1\146\1\145\2\141\1\157\1\141\10\uffff\2\75\3\uffff\1\46\1"+
		"\75\1\53\1\55\3\75\1\uffff\2\75\2\uffff\1\56\1\uffff\1\56\2\uffff\1\163"+
		"\1\162\1\160\1\151\4\uffff\1\157\1\145\1\60\1\163\1\156\1\145\1\146\1"+
		"\60\1\141\1\163\1\165\1\151\1\162\1\154\1\60\1\160\1\164\1\167\1\143\1"+
		"\151\1\165\1\163\3\151\33\uffff\1\11\1\151\1\145\1\164\1\154\1\141\1\uffff"+
		"\1\145\1\164\1\143\1\141\1\uffff\1\154\1\165\1\145\1\155\1\145\1\164\1"+
		"\60\1\163\1\uffff\1\157\2\60\1\153\1\163\1\145\1\153\1\144\1\154\1\164"+
		"\1\uffff\1\156\1\162\1\143\1\60\1\153\1\60\1\151\1\153\1\165\1\60\1\162"+
		"\2\60\1\156\1\60\1\uffff\1\145\1\162\2\uffff\1\141\4\60\1\145\1\60\1\147"+
		"\1\60\1\150\1\uffff\1\60\1\uffff\1\156\1\160\1\154\1\uffff\1\156\2\uffff"+
		"\1\144\1\uffff\1\60\1\164\1\147\4\uffff\1\60\1\uffff\1\60\1\uffff\1\60"+
		"\1\uffff\1\165\1\157\1\164\1\60\1\163\1\uffff\1\60\1\145\3\uffff\1\145"+
		"\1\151\1\60\1\uffff\1\60\1\uffff\2\60\1\156\4\uffff\1\164\1\60\1\uffff";
	static final String DFA24_maxS =
		"\1\176\2\145\1\uffff\1\71\1\uffff\1\171\1\uffff\1\75\1\171\2\157\1\145"+
		"\1\170\1\157\1\156\1\145\1\141\1\162\1\157\1\150\10\uffff\1\76\1\75\3"+
		"\uffff\1\75\1\174\5\75\1\uffff\2\75\2\uffff\1\145\1\uffff\1\145\2\uffff"+
		"\1\163\1\162\1\160\1\151\4\uffff\1\157\1\145\1\172\1\163\1\156\1\145\1"+
		"\146\1\172\1\164\1\163\1\165\1\164\1\162\1\154\1\172\1\160\1\164\1\167"+
		"\1\143\1\151\1\165\1\163\3\151\33\uffff\1\40\1\151\1\145\1\164\1\154\1"+
		"\141\1\uffff\1\145\1\164\1\143\1\141\1\uffff\1\154\1\165\1\145\1\155\1"+
		"\145\1\164\1\172\1\163\1\uffff\1\157\2\172\1\153\1\163\1\145\1\153\1\144"+
		"\1\154\1\164\1\uffff\1\156\1\162\1\143\1\172\1\153\1\172\1\151\1\153\1"+
		"\165\1\172\1\162\2\172\1\156\1\172\1\uffff\1\145\1\162\2\uffff\1\141\4"+
		"\172\1\145\1\172\1\147\1\172\1\150\1\uffff\1\172\1\uffff\1\156\1\160\1"+
		"\154\1\uffff\1\156\2\uffff\1\144\1\uffff\1\172\1\164\1\147\4\uffff\1\172"+
		"\1\uffff\1\172\1\uffff\1\172\1\uffff\1\165\1\157\1\164\1\172\1\163\1\uffff"+
		"\1\172\1\145\3\uffff\1\145\1\151\1\172\1\uffff\1\172\1\uffff\2\172\1\156"+
		"\4\uffff\1\164\1\172\1\uffff";
	static final String DFA24_acceptS =
		"\3\uffff\1\2\1\uffff\1\4\1\uffff\1\6\15\uffff\1\43\1\44\1\45\1\46\1\47"+
		"\1\50\1\51\1\52\2\uffff\1\57\1\60\1\61\7\uffff\1\107\2\uffff\1\122\1\1"+
		"\1\uffff\1\3\1\uffff\1\54\1\53\4\uffff\1\7\1\10\1\102\1\72\31\uffff\1"+
		"\62\1\115\1\55\1\110\1\56\1\63\1\103\1\73\1\64\1\104\1\74\1\65\1\77\1"+
		"\67\1\66\1\100\1\70\1\101\1\71\1\105\1\75\1\106\1\76\1\113\1\111\1\114"+
		"\1\112\6\uffff\1\13\4\uffff\1\17\10\uffff\1\26\12\uffff\1\5\17\uffff\1"+
		"\25\2\uffff\1\30\1\31\12\uffff\1\11\1\uffff\1\14\3\uffff\1\20\1\uffff"+
		"\1\22\1\23\1\uffff\1\120\3\uffff\1\36\1\41\1\116\1\37\1\uffff\1\121\1"+
		"\uffff\1\34\1\uffff\1\12\5\uffff\1\42\2\uffff\1\40\1\21\1\35\3\uffff\1"+
		"\33\1\uffff\1\27\3\uffff\1\16\1\24\1\32\1\15\2\uffff\1\117";
	static final String DFA24_specialS =
		"\u00e1\uffff}>";
	static final String[] DFA24_transitionS = {
			"\1\7\1\3\1\uffff\1\7\1\3\22\uffff\1\7\1\36\1\5\2\uffff\1\50\1\42\1\uffff"+
			"\1\25\1\26\1\46\1\44\1\34\1\45\1\4\1\10\1\1\11\2\1\41\1\33\1\53\1\35"+
			"\1\52\1\40\1\51\32\54\1\31\1\uffff\1\32\1\47\1\54\1\uffff\1\54\1\11\1"+
			"\12\1\13\1\15\1\16\2\54\1\17\4\54\1\20\1\54\1\21\1\54\1\14\1\6\1\22\1"+
			"\54\1\23\1\24\3\54\1\27\1\43\1\30\1\37",
			"\1\57\1\uffff\10\56\2\57\13\uffff\1\57\37\uffff\1\57",
			"\1\57\1\uffff\12\60\13\uffff\1\57\37\uffff\1\57",
			"",
			"\1\61\1\uffff\12\57",
			"",
			"\1\64\1\65\1\uffff\1\66\1\uffff\1\63",
			"",
			"\1\67\4\uffff\1\70\15\uffff\1\71",
			"\1\73\2\uffff\1\74\6\uffff\1\75",
			"\1\76\6\uffff\1\100\6\uffff\1\77",
			"\1\101\11\uffff\1\102",
			"\1\103",
			"\1\104\1\uffff\1\105\11\uffff\1\106",
			"\1\110\15\uffff\1\107",
			"\1\111\6\uffff\1\112\1\113",
			"\1\114",
			"\1\115",
			"\1\120\6\uffff\1\116\11\uffff\1\117",
			"\1\121",
			"\1\123\6\uffff\1\122",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\124\1\125",
			"\1\127",
			"",
			"",
			"",
			"\1\131\26\uffff\1\132",
			"\1\135\76\uffff\1\134",
			"\1\137\21\uffff\1\140",
			"\1\142\17\uffff\1\143",
			"\1\145",
			"\1\147",
			"\1\151",
			"",
			"\1\153",
			"\1\155",
			"",
			"",
			"\1\57\1\uffff\10\56\2\57\13\uffff\1\57\37\uffff\1\57",
			"",
			"\1\57\1\uffff\12\60\13\uffff\1\57\37\uffff\1\57",
			"",
			"",
			"\1\157",
			"\1\160",
			"\1\161",
			"\1\162",
			"",
			"",
			"",
			"",
			"\1\163",
			"\1\164",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\166",
			"\1\167",
			"\1\170",
			"\1\171",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\173\22\uffff\1\174",
			"\1\175",
			"\1\176",
			"\1\u0080\12\uffff\1\177",
			"\1\u0081",
			"\1\u0082",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u0084",
			"\1\u0085",
			"\1\u0086",
			"\1\u0087",
			"\1\u0088",
			"\1\u0089",
			"\1\u008a",
			"\1\u008b",
			"\1\u008c",
			"\1\u008d",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u008e\26\uffff\1\u008e",
			"\1\u008f",
			"\1\u0090",
			"\1\u0091",
			"\1\u0092",
			"\1\u0093",
			"",
			"\1\u0094",
			"\1\u0095",
			"\1\u0096",
			"\1\u0097",
			"",
			"\1\u0098",
			"\1\u0099",
			"\1\u009a",
			"\1\u009b",
			"\1\u009c",
			"\1\u009d",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u009f",
			"",
			"\1\u00a0",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00a3",
			"\1\u00a4",
			"\1\u00a5",
			"\1\u00a6",
			"\1\u00a7",
			"\1\u00a8",
			"\1\u00a9",
			"",
			"\1\u00aa",
			"\1\u00ab",
			"\1\u00ac",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00ae",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00b0",
			"\1\u00b1",
			"\1\u00b2",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00b4",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00b7",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\1\u00b9",
			"\1\u00ba",
			"",
			"",
			"\1\u00bb",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00c0",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00c2",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00c4",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\1\u00c6",
			"\1\u00c7",
			"\1\u00c8",
			"",
			"\1\u00c9",
			"",
			"",
			"\1\u00ca",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00cc",
			"\1\u00cd",
			"",
			"",
			"",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\1\u00d1",
			"\1\u00d2",
			"\1\u00d3",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00d5",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00d7",
			"",
			"",
			"",
			"\1\u00d8",
			"\1\u00d9",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			"\1\u00de",
			"",
			"",
			"",
			"",
			"\1\u00df",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
			""
	};

	static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
	static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
	static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
	static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
	static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
	static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
	static final short[][] DFA24_transition;

	static {
		int numStates = DFA24_transitionS.length;
		DFA24_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
		}
	}

	class DFA24 extends DFA {

		public DFA24(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 24;
			this.eot = DFA24_eot;
			this.eof = DFA24_eof;
			this.min = DFA24_min;
			this.max = DFA24_max;
			this.accept = DFA24_accept;
			this.special = DFA24_special;
			this.transition = DFA24_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( INT_LITERAL | NEWLINE | REAL_LITERAL | STRING_LITERAL | SYS_LITERAL | WS | COMMENT | LINE_COMMENT | BOOL | BREAK | BY | CASE | CONTINUE | DEFAULT | DO | REAL | STRING | ELSE | ENUM | EXTENDS | FOR | IF | IMPORT | INT | NEW | PACKAGE | RETURN | SUPER | SWITCH | THIS | VOID | WHILE | TRUE | FALSE | LPAREN | RPAREN | LBRACE | RBRACE | LBRACKET | RBRACKET | SEMI | COMMA | DOT | ELLIPSIS | EQ | BANG | TILDE | QUES | COLON | EQEQ | AMPAMP | BARBAR | PLUSPLUS | SUBSUB | PLUS | SUB | STAR | SLASH | AMP | BAR | CARET | PERCENT | PLUSEQ | SUBEQ | STAREQ | SLASHEQ | AMPEQ | BAREQ | CARETEQ | PERCENTEQ | MONKEYS_AT | BANGEQ | GT | LT | GE | LE | ARROW | TASK | CHECKPOINT | EXIT | WAIT | IDENTIFIER );";
		}
	}

}
