//-----------------------------------------------------------------------------
//
// Structured text (IEC 61131-3) lexer & parser implementation
// using antlr (see http://www.antlr.org/)
//
//			Pablo Cingolani
//
// Notes: Adapted to ANTLR 3.1 (Sep 2008)
//        Heavily modified/simplified (Oct 2009) hgonzalez
//-----------------------------------------------------------------------------
grammar StructuredText;

options {
  // We're going to output an AST.
  output = AST;
  backtrack=true;
  memoize=true;
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
	ARRAY_INIT_ELEM;
	STRUCT_INIT_ELEM;
	ARRAY_INITIALIZATION;
	STRUCT_INITIALIZATION;
	LITERAL_ENUM_NO_CAST;
}


@lexer::header {
package com.gebautomation.iec61131.antlr;

import java.util.LinkedList;
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
		// super.displayRecognitionError(tokenNames, e);
	}

	
	public List<IecCompilationError> getErrors() { return errors; }
	
}

@header {
package com.gebautomation.iec61131.antlr;

import java.util.LinkedList;
import java.util.List;
import java.net.URL;
import com.gebautomation.iec61131.compiler.IecCompilationError;
}

@members {
	List<IecCompilationError> errors = new LinkedList<IecCompilationError>();
	private URL  file; // might be null
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
		// super.displayRecognitionError(tokenNames, e);
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
	:	'DATE_AND_TIME' | 'TIME_OF_DAY' | 'DATE' | 'ANY_DATE' ;

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


// ST statements/blocks
IF	:	'IF';
THEN :	'THEN';
ELSIF	:	'ELSIF';
ELSE	:	'ELSE';
END_IF : 'END_IF';
CASE :	'CASE';
END_CASE:	'END_CASE';
FOR	:	'FOR';
END_FOR	:	'END_FOR';
WHILE :	'WHILE';
END_WHILE:'END_WHILE';
REPEAT :	'REPEAT';
END_REPEAT :	'END_REPEAT';
UNTIL :	'UNTIL';
EXIT:	'EXIT';
RETURN:	'RETURN';

LADDER	:	'LADDER';  // this is a "ladder item" = pseudo statement (should be called ladderItem?)


// PRAGMAS

PRAGMA options { greedy = false; }
	: '{' .* '}'  { $channel=22; }; 
	
//---
// Comments
//---

// IEC style comments
COMMENT_IEC options { greedy = false; }
	: '(*' .* '*)' NEWLINE? { $channel=HIDDEN; };

// 'C' style comments
COMMENT_C options { greedy = false; }
	: '/*' .* '*/' NEWLINE? { $channel=HIDDEN; };
	
// 'C' style single line comments
COMMENT_SL : '//' ~('\r' | '\n')* NEWLINE	{ $channel=HIDDEN; };

//---
// Literals (expresions)
//---
LITERAL_BOOL :	('TRUE'|'FALSE' | 'true' |'false');

protected	ESCAPE_SEQUENCE	:	'$\'' ;
    
LITERAL_STRING
    :	'\'' (ESCAPE_SEQUENCE | ~('\'') )* '\''
	;

protected	ESCAPE_SEQUENCE_W	:	'$\"' ;
    
LITERAL_WSTRING
    :	'\"' (ESCAPE_SEQUENCE_W | ~('\"') )* '\"'
	;


//actually defined in LITERAL_ENUM (?)
fragment LITERAL_NUMBER_CAST	: ;

// only for literal enum with explicit cast
LITERAL_ENUM_CAST 	
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

LITERAL_MEMORY_STRICT
	:	'%' ('I' | 'i' | 'Q' | 'q' | 'M' | 'm') ('X' | 'B' | 'W' | 'D' | 'L' | 'x' | 'b' | 'w' | 'd' | 'l')? NUMBER  ('.' NUMBER)*
	;
// we dont enforce the restrions	
LITERAL_MEMORY
	:	'%' (LETTER)+ NUMBER  ('.' NUMBER)*
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

// Treat runs of newline characters as a single NEWLINE token.
NEWLINE: ('\r'? '\n')+ { $channel=HIDDEN; };

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

//  ---- first section: general (ST/IL common) ---------------

boolEdgeR :	BOOL R_EDGE -> ^(BOOL R_EDGE) ;

boolEdgeF :	BOOL F_EDGE -> ^(BOOL F_EDGE) ;

// any type identifier, including bool, bool edges, and user definied	
idType :	ELEMTYPE_EB |  boolEdgeR | boolEdgeF | BOOL | ID;
// any type identifier, except booleans
idTypeEb :	ELEMTYPE_EB |  ID;


// literals 
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
		| x=LITERAL_ENUM_CAST -> ^(LITERAL $x)
	;

// literal for enums without cast: only for initialization
literalEnumNoCast
	:	  x=ID -> ^(LITERAL_ENUM_NO_CAST  $x)
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

structTypeSpec  :  STRUCT l=varsDeclList END_STRUCT -> ^(STRUCT $l) ;


// A derived type (type def) may include init-expression, not semicolon
typeDef	:	i=ID COLON t=typeSpec (ASSIGN e=initExpression)? -> ^(TYPE_DEF $i $t $e*)	;	

// TYPE block
typeDefBlock 
	:	 
		TYPE^ (typeDef ( SEMICOLON! | { logError("Expecting ';' "); } ) )*
		( END_TYPE! | { logError("Expecting 'END_TYPE'"); } )
	;

       
// Array dimensions, for array type declaration
dimList :	LEFT_SQUARE! intRange (COMMA! intRange)* RIGHT_SQUARE!;
// int range
intRange :	LITERAL_INT DOTS! LITERAL_INT;


// declaration of variables (or fields in a struct) list
varsDeclList : ( ( varDecl | varDeclMultiple)  ( SEMICOLON! | { logError("Expecting ';'"); } ) )*	;

initExpression  
	:	 literalAny | literalAnySigned | literalEnumNoCast |
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
	  

// declaration of one variable (statement) 
varDecl : varName varAt?  COLON typeSpec varRw? (ASSIGN initExpression)? 
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


expressionsList:  expression ( COMMA! e=expression )*;

// a reference to a array element (inside brackets)
varRefArrayIndex : LEFT_SQUARE x=expressionsList RIGHT_SQUARE -> ^(VAR_REF_ARRAY $x) ;	 

// for structs or FB fields
varRefSubIndex : DOT x=ID -> ^(VAR_REF_SUB $x);

varRefIndex :varRefArrayIndex | varRefSubIndex; 

varRef :  varName varRefIndex* -> ^(VAR_REF varName varRefIndex*) ;


// Variable definition blocks
varBlock 		
	:	VAR^ varAttr? 
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR block"); } )
	;	 

varInputBlock 	
	:	VAR_INPUT^ varAttr? 
		varsDeclList		
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_INPUT block"); } )
	;

varOutputBlock 	
	: 	VAR_OUTPUT^ varAttr? 
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_OUTPUT block"); } )
	;

varInOutBlock 	
	: 	VAR_IN_OUT^ varAttr? 
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_IN_OUT block"); } )
	;

varGlobalBlock 	
	: 	VAR_GLOBAL^ varAttr? 
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_GLOBAL block"); } )
	;

varExternalBLock 	
	: 	VAR_EXTERNAL^ varAttr? 
		varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_EXTERNAL block"); } )
	;

varTempBlock	
	:	 VAR_TEMP^ varAttr? 
		 varsDeclList
		( END_VAR! | { logError("Expecting 'END_VAR' to close VAR_TEMP block"); } )
	;

varAccessBlock 
	: 	VAR_ACCESS^ varAttr? 
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
varDeclBlock :	varBlock | varInputBlock | varOutputBlock | varInOutBlock | varGlobalBlock | varExternalBLock| varTempBlock /*| varAccess*/;



// ------------ what follows is (mostly) ST specific  -------------------------------------------

// ID that can be used in ST laguage for function calls (Warning IL reserved words can aldo be used as IDs in ST language)
// Note: Also some keywords from ST can be used as function names (this is highly unusuall for a programming language)
idFunc  : ID | AND | OR | XOR | NOT | MOD ;


// Expressions
call
	: 
		informalCall
		| formalCall
	;

primitiveElement
	: 
		call
		| varRef 
		| literalAny
		| LPAREN expression RPAREN -> expression
	;

// Informal calls are used for funtion calls
informalCall :	f=idFunc LPAREN l=exprList RPAREN 
		-> ^(INFORMAL_CALL $f $l)
	;

exprList 
	:	expression (COMMA! expression)*;

// Formal calls can be used either for funtion calls or function blocks calls
formalCall 
	:	n=idFunc LPAREN (a=formalCallArgs)? RPAREN 
		-> ^(FORMAL_CALL $n $a*)
	;

formalCallArgs 
	:	(formalCallArg (COMMA! formalCallArg)* )?;

formalCallArg 
	:	( vi=varRef ASSIGN i=expression )		-> ^(ASSIGN $vi $i)
		| ( vo=varRef ASSIGN_OUT o=varRef )		-> ^(ASSIGN_OUT $vo $o)
	;

// WARNING: 'NOT' keyword is very ambiguous. You can write
// 		1-) NOT a			// Operator 'NOT'
//		2-) NOT( a )		// Informal call to function 'NOT'
//		3-) NOT( IN1 := a )	// Formal call to function 'NOT'
booleanNegationExpression 
	:	(NOT LPAREN ID ASSIGN) => call
		| (NOT^)* primitiveElement
	;

signExpression 
	:	((PLUS^|MINUS^))* booleanNegationExpression;

multiplyingExpression 
	:	signExpression ((STAR^|SLASH^|MOD^|POWER^) signExpression)* ;

addingExpression 
	:	multiplyingExpression ((PLUS^|MINUS^) multiplyingExpression)*;

relationalExpression 
	:	addingExpression ((EQUALS^|NOT_EQUAL^|GREATER_THAN^|GREATER_EQUAL^|LESS_THAN^|LESS_EQUAL^) addingExpression)* ;

expression  
	:	relationalExpression ((AND^|OR^|XOR^|BIT_AND^|BIT_OR^) relationalExpression)*;

/*expression  
	:	x=expr -> ^(EXPRESSION $x);
*/

//---
// Statements: All possible statements
//---
statements 
	:	(statement+) 
		-> ^(STATEMENT statement)*
	; // Zero or more statements

// each type of statement  - an statement includes the semicolon (and consummes it , except for the empty statement)
statement 
	: 	(
		emptyStatement
		| callStatement 
		| assignStatement
		| ifStatement
		| whileStatement
		| forStatement
		| exitStatement
		| returnStatement
		| repeatStatement
		| caseStatement
		| ladderStatement
		) 
	;

emptyStatement	
	:	SEMICOLON;

assignStatement	
	:	v=varRef ASSIGN e=expression	( SEMICOLON | { logError("Expecting ';'"); } ) 
		-> ^(ASSIGN $v* $e*)
	;

// sometimes a statement is just a function call
callStatement
	:	(
		x=informalCall SEMICOLON	-> ^(EXPRESSION $x)
		| y=formalCall SEMICOLON	-> ^(EXPRESSION $y)
		)
	;

ifStatement 
	:	IF^ expression THEN! 
				statements 
			( 
				( ELSIF | { logError("Expecting 'ELSIF'"); } ) 
					expression 
				( THEN! | { logError("Expecting 'THEN'"); } ) 
					statements 
			)* 
			( 
			 	( ELSE | { logError("Expecting 'ELSE'"); } )
					statements 
			)? 
		( END_IF! | { logError("Expecting 'END_IF'"); } ) 
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;
	

caseListElement : ( intRange | literalAny) 
;
// we dont remove the COMMA, to help distinguish ranges
caseList 
	:	 caseListElement (COMMA caseListElement)*
	
	;	           
	
caseElement 
	:	 caseList COLON! statements	;
	
caseStatement
	:	CASE^ expression 
		( OF! | { logError("Expecting 'OF'"); } ) 
			( 
			   caseElement
			)+
			( 	( ELSE | { logError("Expecting 'ELSE'"); } ) 
				statements
			)?
		( END_CASE! | { logError("Expecting 'END_CASE'"); } ) 
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;
	
forStatement 
	:	FOR^ varRef 
		( ASSIGN! | { logError("Expecting 'ASSIGN'"); } ) 
			expression 
		( TO! | { logError("Expecting 'TO'"); } ) 
			expression 
		( ( BY | { logError("Expecting 'BY'"); } ) 
			expression
		)?
		( DO! | { logError("Expecting 'DO'"); } ) 
			statements
		( END_FOR! | { logError("Expecting: statement or 'END_FOR'"); } ) 
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;
	
whileStatement
	:	WHILE^ expression 
		( DO! | { logError("Expecting 'DO'"); } ) 
			statements
		( END_WHILE! | { logError("Expecting statement or 'END_WHILE'"); } ) 
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;
	
repeatStatement
	:	
		REPEAT
			statements
		( UNTIL | { logError("Expecting 'UNTIL'"); } ) 
			expression
		( END_REPEAT | { logError("Expecting 'END_REPEAT'"); } ) 
		( SEMICOLON | { logError("Expecting ';'"); } ) 
		-> ^(REPEAT expression statements)
	;

exitStatement	
	:	EXIT 
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;

returnStatement	
	:	RETURN
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;

// This is not really an statement, it's just a way to 'store' ladder diagrams inside 'ST' code
ladderStatement 
	:	LADDER^ LPAREN! LITERAL_STRING (COMMA! LITERAL_STRING)* RPAREN!
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;



/* LPAREN! literalAnyGral (COMMA! literalAnyGral)* RPAREN!
		( SEMICOLON! | { logError("Expecting ';'"); } ) 
	;
*/

//---
// Function: A function definition
// Note: THe norm defines functions that have the same name as reserved words 
// (it is highly unusual for a programming languade to have reserved keywords as function names)
//---
function 
	:	
		FUNCTION^ idFunc // Function name 
			( functionReturnType | { logError("Expecting function return type declaration"); } ) 
			(varDeclBlock)* (statements)*
		( END_FUNCTION! | { logError("Expecting variable declaration, statement or 'END_FUNCTION'"); } ) 
	;

//  a function cannot retur a anonymous defined type
functionReturnType
	:	 COLON v=idType -> ^(FUNCTION_RETURN_TYPE $v);

// Function block
functionBlock 
	: 
		FUNCTION_BLOCK^ ID
			(varDeclBlock)* (statements)*
		( END_FUNCTION_BLOCK! | { logError("Expecting: type declaration, variable declaration, statement or 'END_FUNCTION_BLOCK'"); } ) 
	;

// Program
program	
	:	
		PROGRAM^ ID
			(typeDefBlock)* (varDeclBlock)* (statements)*
		( END_PROGRAM! | { logError("Expecting: type declaration, variable declaration, statements or 'END_PROGRAM'"); } ) 
	;
	
// Resources
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
	:	(configuration |  task | resource | typeDefBlock | program | functionBlock | function | SEMICOLON!)* EOF!;

main
	:	compilUnits -> ^(COMPILATION_UNITS compilUnits?);
