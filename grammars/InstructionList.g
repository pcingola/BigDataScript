//-----------------------------------------------------------------------------
//
// Structured text (IEC 61131-3) lexer & parser implementation
// using antlr (see http://www.antlr.org/)
//
//			Pablo Cingolani
//
// Notes: Adapted to ANTLR 3.1 (Sep 2008)
//   Heavily modified by hgonzalez (oct-nov 2009)
//-----------------------------------------------------------------------------
grammar InstructionList;

options {
  // We're going to output an AST.
  output = AST;
  backtrack=true;
  memoize=true;
  tokenVocab=StructuredText;//
}

// Tokens (abstract tokens and reserved words)
tokens { 
	BASE;
//	DIMEXPR;
	ENUMERATED;
	EXPRESSION;
	INFORMAL_CALL;
	FORMAL_CALL;
	FUNCTION_RETURN_TYPE;
	LITERAL;
	NULL;
	NUMBER_BASE16;
	NUMBER_BASE2;
	NUMBER_BASE8;
	COMPILATION_UNITS;
	STATEMENT;
	TYPE_DEF;
	VAR_ATTR;
	VAR_AT;
	VAR_DEF;
	VAR_ACC_DEF;
	VAR_REF;
	VAR_REF_ARRAY;	
	VAR_REF_SUB;
	IL_STATEMENT;
	IL_STATEMENTS; // bunch of inline IL_STATEMENTS ( for deferred operations or assignements; works as an expression)
	NOP; //pseudo operator for empty il statement
	ARRAY_INIT_ELEM;
	STRUCT_INIT_ELEM;
	ARRAY_INITIALIZATION;
	STRUCT_INITIALIZATION;
}

@lexer::header {
package com.gebautomation.iec61131.antlr;

import java.util.LinkedList;
import java.util.List;
import java.net.URL;
import com.gebautomation.iec61131.compiler.IecCompilationError;
}

@lexer::members { 
	List<IecCompilationError> errors = new LinkedList<IecCompilationError>();

	private URL file; // might be null
	public void setFile(URL file)	{ this.file = file; }	
	
	public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
		String hdr = getErrorHeader(e);
		String msg = getErrorMessage(e, tokenNames);
		String description = hdr + " " + msg;
		IecCompilationError iecErr = new IecCompilationError(description, file, e.line, e.charPositionInLine);
		errors.add(iecErr);
		super.displayRecognitionError(tokenNames, e);
	}

	
	public List<IecCompilationError> getErrors() { return errors; }

       
}

@header {
package com.gebautomation.iec61131.antlr;

import java.util.LinkedList;
import java.net.URL;
import org.antlr.runtime.debug.DebugTokenStream;
import com.gebautomation.iec61131.compiler.IecCompilationError;
}

@members {
	List<IecCompilationError> errors = new LinkedList<IecCompilationError>();
	
	private URL file; // might be null
	public void setFile(URL file)	{ this.file = file; }	
		
	public void logError(String description) {
		int lineNum = 0, charAt = 0;
		Token t = getTokenStream().LT(1);
		if( t != null ) {
			lineNum = t.getLine();
			charAt = t.getCharPositionInLine();
		}
		IecCompilationError iecErr = new IecCompilationError(description, file, lineNum, charAt);
		errors.add(iecErr);
	}
	
	public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
		String hdr = getErrorHeader(e);
		String msg = getErrorMessage(e, tokenNames);
		String description = hdr + " " + msg;
		IecCompilationError iecErr = new IecCompilationError(description, file, e.line, e.charPositionInLine);
		errors.add(iecErr);
		super.displayRecognitionError(tokenNames, e);
	}

	
	public List<IecCompilationError> getErrors() { return errors; }

}

//-----------------------------------------------------------------------------
// Lexer
//-----------------------------------------------------------------------------

//---
// Symbols
//---
BIT_AND	: '&';
BIT_OR : '|';
ASSIGN : ':=';
ASSIGN_OUT :	 '=>';
COLON : ':';
COMMA : ',';
HAT  : '^' ;
//LEFT_CURLY : '{';
LEFT_SQUARE: '[';
LPAREN: '(';
MINUS : '-' ;
PERCENT  : '%' ;
PLUS  : '+' ;
POWER  : '**' ;
//RIGHT_CURLY : '}';
RIGHT_SQUARE : ']';
RPAREN: ')' ;
SEMICOLON  : ';' ;
SLASH  : '/' ;
STAR  : '*' ;
LESS_THAN  : '<' ;
LESS_EQUAL  : '<=' ;
GREATER_THAN  : '>' ;
GREATER_EQUAL  : '>=' ;
EQUALS : '=';
NOT_EQUAL	:	'<>';
HASH	:	 '#';


//  data types
fragment ELEMTYPE_REALS 	
	:	'REAL' | 'LREAL' | 'ANY_REAL';
fragment ELEMTYPE_INTS		
	:	'INT' | 'SINT' | 'DINT' | 'LINT' | 'UINT' | 'USINT' | 'UDINT' | 'ULINT' | 'ANY_INT';
fragment ELEMTYPES_NUMS 		
	:	ELEMTYPE_INTS | ELEMTYPE_REALS | 'ANY_NUM';
fragment ELEMTYPES_BIT		 
	:	'BYTE' | 'WORD' | 'DWORD' | 'LWORD' | 'ANY_BIT';
fragment ELEMTYPES_STRING
	:	'STRING' | 'WSTRING';
fragment ELEMTYPES_DATES		
	:	'DATE_AND_TIME' | 'TIME_OF_DAY' | 'DATE' | 'ANY_DATE';

// Elementary types (except bool)
ELEMTYPE_EB 		
	:	ELEMTYPES_NUMS | ELEMTYPES_BIT | ELEMTYPES_STRING | ELEMTYPES_DATES | 'TIME' | 'ANY';

BOOL : 'BOOL';
R_EDGE : 'R_EDGE';
F_EDGE : 'F_EDGE';

// Operators
AND :	 'AND';
OR 	:	'OR';
XOR :	 'XOR';
NOT :	'NOT';
MOD :	'MOD';

// BLock delimiters
RESOURCE :	'RESOURCE';
END_RESOURCE:'END_RESOURCE';
TASK :	 'TASK';
END_TASK	: 'END_TASK';
PROGRAM 	:	'PROGRAM';
END_PROGRAM 	:	'END_PROGRAM';
CONFIGURATION	:	'CONFIGURATION';
END_CONFIGURATION	:	'END_CONFIGURATION';
FUNCTION_BLOCK	:	'FUNCTION_BLOCK';
END_FUNCTION_BLOCK	:	'END_FUNCTION_BLOCK';
FUNCTION	:	'FUNCTION';
END_FUNCTION	:	'END_FUNCTION';
RANGE	:	'RANGE';
VAR :	'VAR';
VAR_ACCESS	:	'VAR_ACCESS';
VAR_EXTERNAL	:	'VAR_EXTERNAL';
VAR_GLOBAL	:	 'VAR_GLOBAL';
VAR_INPUT	:	 'VAR_INPUT';
VAR_IN_OUT	:	'VAR_IN_OUT';
VAR_OUTPUT	:	 'VAR_OUTPUT';
VAR_TEMP	:	 'VAR_TEMP';
END_VAR	:	'END_VAR';
TYPE	:	 'TYPE';
END_TYPE	:	 'END_TYPE';
STRUCT	:	 'STRUCT';
END_STRUCT	:	 'END_STRUCT';

// Other
ARRAY	:	'ARRAY';
AT 	:	 'AT';
BY 	:	 'BY';
OF 	:	 'OF';
DO 	:	 'DO';
TO 	:	 'TO';
READ_ONLY	:	'READ_ONLY';
READ_WRITE	:	'READ_WRITE';
RETAIN 	:	'RETAIN';
NON_RETAIN 	:	'NON_RETAIN';
CONSTANT 	:	'CONSTANT';


// IL operators (some have already been defined)
LD : 'LD';
LDN : 'LDN';
ST : 'ST';
STN : 'STN';
S : 'S';
R : 'R';
ANDN : 'ANDN';
ORN : 'ORN';
XORN : 'XORN';
ADD : 'ADD';
SUB : 'SUB';
MUL : 'MUL';
DIV : 'DIV';
GT : 'GT';
GE : 'GE';
EQ : 'EQ';
NE : 'NE';
LE : 'LE';
LT : 'LT';
JMP : 'JMP';
JMPC : 'JMPC';
JMPN : 'JMPN';
JMPNC : 'JMPNC';
JMPCN : 'JMPCN';
CAL : 'CAL';
CALC : 'CALC';
CALN : 'CALN';
CALNC : 'CALNC';
CALCN : 'CALCN';
RET : 'RET';
RETC : 'RETC';
RETN : 'RETN';
RETNC : 'RETNC';
RETCN : 'RETCN';

// IEC-61131-3 table 54 page 129
CD	:	'CD';
CLK :	 'CLK';
CU	: 	'CU';
IN	:	'IN';
PT	:	'PT';
PV	:	'PV';
R1 	:	 'R1';
S1 	:	 'S1';

// PRAGMAS : send to a special channel (22)
PRAGMA options { greedy = false; }
	: '{' .* '}'  { $channel=22; }; 
	
//---
// Comments
//---

// IEC style comments
COMMENT_IEC options { greedy = false; }
	: '(*' .* '*)'  { $channel=HIDDEN; };

// 'C' style comments
COMMENT_C options { greedy = false; }
	: '/*' .* '*/' { $channel=HIDDEN; };
	
// 'C' style single line comments (WARNING: DO NOT CONSUME THE ENDING NEWLINE!!!)
COMMENT_SL options { greedy = true; } 
 :  '//' ~('\r'|'\n')* { $channel=HIDDEN; };

//---
// Literals (expresions)
//---
LITERAL_BOOL :	('TRUE'|'FALSE');

protected	ESCAPE_SEQUENCE	:	'$\'' ;
    
LITERAL_STRING
    :	'\'' (ESCAPE_SEQUENCE | ~('\'') )* '\''
	;

protected	ESCAPE_SEQUENCE_W	:	'$\"' ;
    
LITERAL_WSTRING
    :	'\"' (ESCAPE_SEQUENCE_W | ~('\"') )* '\"'
	;


fragment LITERAL_NUMBER_CAST	: ;
LITERAL_ENUM 	
	:
		( 'REAL' | 'LREAL' | 'INT' | 'SINT' | 'DINT' | 'LINT' | 'UINT' | 'USINT' | 'UDINT' | 'ULINT' | 'BOOL' | 'BYTE' | 'WORD' | 'DWORD' | 'LWORD') 
			HASH (PLUS|MINUS)? { $type = LITERAL_NUMBER_CAST; }
		| ID HASH ID
	;

// Time literals can be in the form "T#ld4h34m43s22ms" or "T#35rn23.5s" (or extended form "T#ld_4h_34m_43s_22ms") which is more readable)

LITERAL_DATE
	:	('DATE' | 'D' | 'd') '#' NUMBER MINUS NUMBER MINUS NUMBER
	;
LITERAL_TIME 
	:	 ('TIME' | 'T' | 't') '#'
		( (PLUS|MINUS)? NUMBER ('.' NUMBER)? ('D'|'d') )? // Days
		( (PLUS|MINUS)? NUMBER ('.' NUMBER)? ('H'|'h') )? // Hours
		( (PLUS|MINUS)? NUMBER ('.' NUMBER)? ('M'|'m'))? // Minutes
		( (PLUS|MINUS)? NUMBER ('.' NUMBER)? ('S'|'s'))? // Seconds
		( (PLUS|MINUS)? NUMBER ('.' NUMBER)? ('MS'|'ms'))? // Miliseconds
	;
		

LITERAL_TIME_OF_DAY
	:	('TIME_OF_DAY' | (('T'|'t') ('O'|'o') ('D'|'d')) ) '#'	NUMBER COLON NUMBER (COLON NUMBER ('.' NUMBER)?)?
	;

LITERAL_DATE_AND_TIME
	:	('DATE_AND_TIME' | ('D'|'d') ('T'|'t')) '#'	NUMBER MINUS NUMBER MINUS NUMBER MINUS NUMBER COLON NUMBER COLON (NUMBER ('.' NUMBER)?)?
	;

// Memory mapped variables
LITERAL_MEMORY
	:	'%' ('I' | 'i' | 'Q' | 'q' | 'M' | 'm') ('X' | 'B' | 'W' | 'D' | 'L' | 'x' | 'b' | 'w' | 'd' | 'l')? NUMBER ('.' NUMBER)*
	;


fragment    TIME_LITERAL	:   ;
fragment    LITERAL_INT		:   ;
fragment    DOTS		:   ;
fragment    DOT			:   ;

// can resolve to LITERAL_INT - does not include sign
LITERAL_REAL
   	: 
		('0'..'9') NUMBER?	// Numeric so far, resolve float
			(
			{ input.LA(2) != '.'}? => '.' NUMBER? EXPONENT?  { $type = LITERAL_REAL; }
			|   // Just a decimal literal
				(
				EXPONENT { $type = LITERAL_REAL; }
				|   { $type = LITERAL_INT; } // Just n.nnn
				)
			)
		| '.'
			(
			NUMBER EXPONENT?  { $type = LITERAL_REAL; } // Just  floating point
			|   '.' { $type = DOTS; } // Is it a range specifer?
			|   { $type = DOT; } // It was just a single .
			)
	;


fragment EXPONENT :   ('e'|'E') ('+'|'-')?  NUMBER ;

LITERAL_HEX
	:	'16#' ('0'..'9' | 'a'..'f' | 'A'..'F' | '_')+
	;

LITERAL_OCTAL
	:	'8#' ('0'..'7' | '_')+
	;

LITERAL_BINARY
	:	'2#' ('0' | '1' | '_')+
	;
	
//---
// Spaces, fragments, etc.
//---

// Send runs of space and tab characters to the hidden channel.        
WS: (' ' | '\t')+ { $channel = HIDDEN; };

// NEWLINE token. In IL this is NOT HIDDEN
NL: ( ('\r')?  '\n')+ ;

// A digit
fragment DIGIT : '0'..'9' ;

// A letter (ASCII)
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';

// Letter or digit
fragment ALPHANUM 	:	LETTER | DIGIT;

// An identifier, for variables,types, pous, labels, etc (cannot start with a digit, nor end with _)
//ID 	:	LETTER | (LETTER | '_') (ALPHANUM | '_' )* (ALPHANUM);
ID 	:	(LETTER | '_') (ALPHANUM | '_' )*;

// A number is a set of digits . unndescores are ignored separators
fragment NUMBER : (DIGIT | '_')+;


//-----------------------------------------------------------------------------
// Parser
//-----------------------------------------------------------------------------

//  ---- first section: general (ST/IL common) except for NL ---------------

boolEdgeR :	BOOL R_EDGE -> ^(BOOL R_EDGE) ;

boolEdgeF :	BOOL F_EDGE -> ^(BOOL F_EDGE) ;

// any type identifier, including bool, bool edges, and user definied	
idType :	ELEMTYPE_EB |  boolEdgeR | boolEdgeF | BOOL | ID;
// any type identifier, except booleans
idTypeEb :	ELEMTYPE_EB |  ID;

// literal (unsigned)	   	
literalAny	
	:	// Numeric literals
		x=LITERAL_BOOL -> ^(LITERAL  $x)
		| si=LITERAL_INT  -> ^(LITERAL  $si)
		| sr=LITERAL_REAL -> ^(LITERAL  $sr)
		| x=LITERAL_HEX -> ^(LITERAL $x)
		| x=LITERAL_OCTAL -> ^(LITERAL $x)
		| x=LITERAL_BINARY -> ^(LITERAL $x)
		// Numeric literas using an explicit cast
		| y=LITERAL_NUMBER_CAST x=LITERAL_BOOL -> ^(LITERAL  $x $y)
		| y=LITERAL_NUMBER_CAST x=LITERAL_INT -> ^(LITERAL  $x $y)
		| y=LITERAL_NUMBER_CAST x=LITERAL_REAL -> ^(LITERAL  $x $y)
		| y=LITERAL_NUMBER_CAST x=LITERAL_HEX -> ^(LITERAL  $x $y)
		| y=LITERAL_NUMBER_CAST x=LITERAL_OCTAL -> ^(LITERAL  $x $y)
		| y=LITERAL_NUMBER_CAST x=LITERAL_BINARY -> ^(LITERAL  $x $y)
		// Other literals
		| x=LITERAL_STRING -> ^(LITERAL  $x)
		| x=LITERAL_WSTRING -> ^(LITERAL  $x)
		| x=LITERAL_TIME -> ^(LITERAL  $x)
		| x=LITERAL_DATE -> ^(LITERAL  $x)
		| x=LITERAL_TIME_OF_DAY -> ^(LITERAL  $x)
		| x=LITERAL_DATE_AND_TIME -> ^(LITERAL  $x)
		| x=LITERAL_ENUM -> ^(LITERAL $x)
	;

// literal (signed) (only for a few case - init expressions)	   	
literalAnySigned : (PLUS|MINUS)^ literalAny;	 
// literal, signed or not
literalAnyGral :  literalAny | literalAnySigned;	 
	
// Data type definitions

// A type specification (for a var/field/function declaration, or a user defined type) might be an ID (for a elementary or user defined type)
// or an inline type definition (DOES NEVER INCLUDE INIT EXPRESSION!)
typeSpec : structTypeSpec | typeSpecNoStruct;           

typeSpecNoStruct : enumTypeSpec | arrayTypeSpec | subSubrangeTypeSpec | idType ;           

arrayTypeSpec  :   ARRAY d=dimList OF t=idType  -> ^(ARRAY $t $d) ;

idList 	: ID (COMMA! ID)+ ;	           
          
enumTypeSpec :    LPAREN l=idList RPAREN -> ^(ENUMERATED $l) ;

subSubrangeTypeSpec  : t=idTypeEb LPAREN b=literalAnyGral DOTS e=literalAnyGral RPAREN -> ^(RANGE $t $b $e)  ;

structTypeSpec  :  STRUCT  l=varsDeclList END_STRUCT -> ^(STRUCT $l) ;

// A derived type (type def) may include init-expression, not semicolon
typeDef	:	i=ID COLON t=typeSpec  (ASSIGN NL* e=initExpression)? -> ^(TYPE_DEF $i $t $e*)	;	

// TYPE block
typeDefBlock 
	:	 
		TYPE^ 	nLs0! (typeDef ( SEMICOLON! nLs0! | { logError("Expecting ';' "); } ) )*
		( END_TYPE! | { logError("Expecting 'END_TYPE'"); } )
	;

       
// Array dimensions, for array type declaration
dimList :	LEFT_SQUARE! dimRange (COMMA! dimRange)* RIGHT_SQUARE!;
// int range
dimRange :	LITERAL_INT (WS*) DOTS! (WS*) LITERAL_INT;


// declaration of variables (or fields in a struct) list
varsDeclList : ( ( varDecl | varDeclMultiple)  ( SEMICOLON! nLs0! | { logError("Expecting ';'"); } ) )*	;

	
initExpression  
	:	 literalAny | literalAnySigned | 
	a=arrayInitialization -> ^(ARRAY_INITIALIZATION $a)	| 
	s=structInitialization -> ^(STRUCT_INITIALIZATION $s);

arrayInitialization 
	: LEFT_SQUARE! arrayInitialElements (COMMA! arrayInitialElements)* RIGHT_SQUARE!; 

arrayInitialElements 
	:	 x=initExpression -> ^(ARRAY_INIT_ELEM  $x) 
	      | n=literalAny LPAREN x=initExpression RPAREN -> ^(ARRAY_INIT_ELEM $x $n) ;
	
structInitialization 
	:	LPAREN! structElementInitialization  (COMMA! structElementInitialization)*  RPAREN!;	
	
structElementInitialization 
	:	i=ID ASSIGN x=initExpression -> ^(STRUCT_INIT_ELEM $i $x) ;		

// declaration of one variable (statement) DOES NOT SUPPORT ANONYMOUS "AT" var
varDecl :  varName varAt? COLON typeSpec varRw? (ASSIGN initExpression)? 
			-> ^(VAR_DEF varName  typeSpec varAt? varRw? initExpression?)
	;	 
	
// multiple vars:  X,Y,Z : INT  (DOES NOT SUPPORT "AT" vars; may have init expression) 
varDeclMultiple :  varName (COMMA varName)+ COLON typeSpec (varRw)? (ASSIGN initExpression)? 
			-> ^(VAR_DEF varName typeSpec varRw? initExpression?)+

	;	 
		
varRw
	:	READ_ONLY | READ_WRITE
	;
		
varAttr	
	:	RETAIN | NON_RETAIN | CONSTANT;

varAt	
	:	x=AT y=LITERAL_MEMORY -> ^(VAR_AT $x $y);
	
varName : ID;

// a reference to a array element (inside brackets)
iLvarRefArrayIndex : LEFT_SQUARE x=il_operand_list RIGHT_SQUARE -> ^(VAR_REF_ARRAY $x) ;	 



// for structs or FB fields
varRefSubIndex : DOT x=idOrInputOperator -> ^(VAR_REF_SUB $x);

varRefIndex : iLvarRefArrayIndex | varRefSubIndex; 

varRef :  varName varRefIndex* -> ^(VAR_REF varName varRefIndex*) ;


// Variable definition blocks
varBlock 		
	:	VAR^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR block"); } )
	;	 

varInputBlock 	
	:	VAR_INPUT^ varAttr? 
		nLs0!
		varsDeclList		
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_INPUT block"); } )
	;

varOutputBlock 	
	: 	VAR_OUTPUT^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_OUTPUT block"); } )
	;

varInOutBlock 	
	: 	VAR_IN_OUT^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_IN_OUT block"); } )
	;

varGlobalBlock 	
	: 	VAR_GLOBAL^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_GLOBAL block"); } )
	;

varExternalBLock 	
	: 	VAR_EXTERNAL^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_EXTERNAL block"); } )
	;

varTempBlock	
	:	 VAR_TEMP^ varAttr? 
		nLs0!
		 varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_TEMP block"); } )
	;

varAccessBlock 
	: 	VAR_ACCESS^ varAttr? 
		nLs0!
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_ACCESS block"); } )
	;

// Variable definition 
/*varAccessDef 	 !!!!
	: 	
		v=varRef COLON w=varRef COLON t=varType a=varRw
		( SEMICOLON | { logError("Expecting ';'"); } ) 
		-> ^(VAR_ACC_DEF $v $w $t $a )
		| varDefMultiple 
	;
*/


// Any variable declaration
varDeclBlock :	(varBlock | varInputBlock | varOutputBlock | varInOutBlock | varGlobalBlock | varExternalBLock| varTempBlock) /*| varAccess*/ nLs0!;


// ---------- what follows is (mostly) IL specific ----------------

// consecutive newlines (at least 1)
nLs 	:	NL+;
// consecutive newlines (0 or more)
nLs0 	:	NL*;

// Id for variables, labels and pou names 
ilId 	:	ID;

// Notice that the special functions defined here in IL as TOKENS (ADD,OR...) are seen here as OPERANDS, not function calls
idFuncIl 	:	ilId ;

// as IL has not binary expressions, it's more conveniet to bind the sign to the literal (TODO: manipulate the tree, so that it is a LITERAL with the sign attached to the text)
literalWithSign :	 (PLUS|MINUS)^ literalAny;

//--- IL sintax follows aprox. the nomenclature from production rules in the IEC standard doc ----
	
instruction_list : il_instruction+;

il_instruction 
	:	 il_instructionX -> ^(IL_STATEMENT il_instructionX);

il_instructionX 
	:	 il_instruction_non_empty | il_instruction_empty; 
	
il_instruction_non_empty : 
(ID COLON!)? ( il_simple_operation
 | il_expression
 | il_formal_funct_call
 | il_fb_call
 | il_jump_operation
 | il_return_operator 
 ) nLs! 
;

// just a label
il_instruction_empty
	:  ID COLON nLs -> ID NOP;


// simple one line operation: operator-operand  or informal function call (not includes jump,ret,cal) 
il_simple_operation : ( il_simple_operator il_operand?  )  | il_informal_funct_call;

// we add a CAL operator
il_informal_funct_call :  idFuncIl il_operand_list? -> CAL ^(INFORMAL_CALL idFuncIl il_operand_list?);

// deferred operation (table 51b)
il_expression : il_expression1 | il_expression2;
// first operand not present 
il_expression1 : il_expr_operator LPAREN! nLs! simple_instr_list? RPAREN!;

// first operand present : LD implicit: we add it (DOES NOT WORK, FIX IT)
il_expression2 : x=il_expr_operator LPAREN o=il_operand nLs l=simple_instr_list RPAREN 
  -> $x  $l ^(IL_STATEMENT LD $o); 
  // NOT VERY NICE, THE NEW NODE SHOULD BE PLACED AS FIRST CHILD IN simple_instr_list , LETS DO IT IN JAVA

// FunctionBlock CAL,  with arguments (formal call)
il_fb_call_args_formal : il_call_operator idFuncIl LPAREN nLs il_param_list?  RPAREN -> il_call_operator ^(FORMAL_CALL idFuncIl il_param_list?); 
il_fb_call_args_inform : il_call_operator idFuncIl LPAREN il_operand_list RPAREN  -> il_call_operator ^(INFORMAL_CALL idFuncIl il_operand_list);  
// FunctionBlock CAL,  without arguments (we consider it a formal call)
il_fb_call_noargs : il_call_operator idFuncIl -> il_call_operator ^(FORMAL_CALL idFuncIl);

il_fb_call : il_fb_call_noargs | il_fb_call_args_formal | il_fb_call_args_inform ;

// formal fucntion call (parentheses) - we add the operator CAL
il_formal_funct_call : idFuncIl LPAREN nLs il_param_list? RPAREN
  -> CAL ^(FORMAL_CALL idFuncIl il_param_list?)
;

il_jump_operation : il_jump_operator ID;

il_operand : literalWithSign | literalAny | varRef ;

il_operand_list : il_operand (COMMA! il_operand)*;

simple_instr_list : il_simple_instruction+ ->^(IL_STATEMENTS il_simple_instruction+);

// like il_instruction, but more restricted: no labels, no jump/return, no deferred operands (expressions), no fb CAL
il_simple_instructionX : (il_simple_operation | il_expression | il_formal_funct_call) nLs!;
il_simple_instruction 
	:	 il_simple_instructionX -> ^(IL_STATEMENT il_simple_instructionX);

// list of assignemts for vars in formal call, one per line; includes NL, but not parentheses
il_param_list : il_param_instruction* il_param_last_instruction;
il_param_instruction : (il_param_assignment | il_param_out_assignment) COMMA! nLs! ;
il_param_last_instruction : ( il_param_assignment | il_param_out_assignment ) nLs0!;

il_param_assignment : ID ASSIGN^ ( il_operand | ( LPAREN! nLs! simple_instr_list RPAREN! ) ) ;
il_param_out_assignment : ID ASSIGN_OUT^ ( il_operand | ( LPAREN! nLs0! simple_instr_list RPAREN! ) ) ;


// Input operators as defines in IEC-61131-3, table 54, page 129
il_input_operator :  R | S | S1 | R1 | CLK | CU | PV | CD | IN	| PT;

idOrInputOperator 
	:	 ID | il_input_operator;
	
// operatos that can have parentheses (see table 52)
il_expr_operator :	 
  AND | ANDN | BIT_AND | OR | ORN | XOR | XORN |  ADD | SUB | MUL | DIV | MOD | GT | GE | EQ | NE | LE | LT;

// all operators, including il_expr_operators and input (BUT EXCLUDING JPM/CAL/RET...)
il_simple_operator :	 
  LD | LDN | ST | STN | il_expr_operator | il_input_operator;
	
il_return_operator	:	RET | RETC | RETN | RETNC | RETCN ;

il_jump_operator 	: JMP | JMPC | JMPN | JMPNC | JMPCN;
	
il_call_operator	:	CAL | CALC | CALCN;

 

// Function: A function definition
function :	FUNCTION^ nLs0! idFuncIl  // Function name 
			( functionReturnType | { logError("Expecting function return type declaration"); } ) 
			nLs! (varDeclBlock)* instruction_list
		( END_FUNCTION! | { logError("Expecting variable declaration, statement or 'END_FUNCTION'"); } ) 
	;

//  a function cannot retur a anonymous defined type
functionReturnType	:	 COLON v=idType -> ^(FUNCTION_RETURN_TYPE $v);

// Function block
functionBlock : FUNCTION_BLOCK^ idFuncIl nLs0!
			(varDeclBlock nLs0!)* instruction_list
		( END_FUNCTION_BLOCK! | { logError("Expecting: type declaration, variable declaration, statement or 'END_FUNCTION_BLOCK'"); } ) 
	;

// Program
program	:	PROGRAM^  idFuncIl 	
			nLs0! (typeDefBlock)* nLs0! (varDeclBlock)* instruction_list 
		( END_PROGRAM! | { logError("Expecting: type declaration, variable declaration, statements or 'END_PROGRAM'"); } ) 
	;
//programBody :	(typeDecl | varDecl | function | functionBlock | statements)+;

//---
// Resources
//---

resource
	:	RESOURCE^ 
		( END_RESOURCE! | { logError("Expecting 'END_RESOURCE'"); } ) 
	;

//---
// Tasks
//---

task 
	:	
		TASK^ 
		( END_TASK! | { logError("Expecting 'END_TASK'"); } ) 
	;
	
//---
// Configuration
//---
configName 
	:	 ID;
	
configuration 
	:	CONFIGURATION^ configName
			(varGlobalBlock | varAccessBlock | resource | program | task )* 
		( END_CONFIGURATION! | { logError("Expecting: variable declaration, resource declaration, program declaration, task declaration or 'END_CONFIGURATION'"); } ) 
	;
//---
// Main type
//---
compilUnits 
	:	(configuration |  task | resource | typeDefBlock | program | functionBlock | function | SEMICOLON! | nLs!)*  EOF!;

main
	:	p=compilUnits -> ^(COMPILATION_UNITS $p);

