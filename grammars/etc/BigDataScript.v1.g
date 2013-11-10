//------------------------------------------------------------------------------
//
// BigDataScript parser
// using antlr (see http://www.antlr.org/)
//
//															2013 Pablo Cingolani
//------------------------------------------------------------------------------
grammar BigDataScript;

options {
  // We're going to output an AST.
  output = AST;
  //backtrack = true;
}

// Tokens (reserved words)
tokens {
	CLAUSE_BLOCK;
	FUNC_INVOKE;
	INVOKE;
	KEY_VALUE_PAIR;
	LITERAL_BOOL;
	LITERAL_INT;
	LITERAL_LIST;
	LITERAL_LIST_RANGE;
	LITERAL_MAP;
	LITERAL_REAL;
	LITERAL_STRING;	
	OP_BINARY;
	OP_UNARY;
	OP_STRING;
	PROGRAM;
	STRING_CONCAT;
	SYS_INVOKE;
	SYS_INVOKE_RES;
	TASK_INVOKE;
	VAR_DECL;
}

@lexer::header {
package com.dnaminer.bigDataScript;
}

@header {
package net.sourceforge.jFuzzyLogic.fcl;
}

//-----------------------------------------------------------------------------
// Lexer
//-----------------------------------------------------------------------------

// Send runs of space and tab characters to the hidden channel.        
WS: (' ' | '\t')+ { $channel = HIDDEN; };

// Treat runs of newline characters as a single NEWLINE token.
// On some platforms, newlines are represented by a \n character.
// On others they are represented by a \r and a \n character.
NEWLINE: ('\r'? '\n')+ { $channel=HIDDEN; };
 
// A number is a set of digits
//fragment NUMBER : (DIGIT)+;

// A DIGIT
fragment DIGIT : '0'..'9' ;

// A letter
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';

// Letter or digit
fragment ALPHANUM 	:	LETTER | DIGIT;

// Int number
BOOL     :   ('true'|'false');

// Int number
INT     :   (DIGIT)+;

// Real number (float/double) without any sign
REAL  :   (DIGIT)+ ( '.' (DIGIT)+ )? (('e'|'E') ('+'|'-')? (DIGIT)+)? ;

 // A string literal
STRING: '"' ~( '\n' | '\r' | '"' )* '"' { setText(getText().substring( 1, getText().length()-1 ) ); } ;

 // A string literal
STRING_SINGLE_QUOTE: '\'' ~( '\n' | '\r' | '\'' )* '\'' { setText(getText().substring( 1, getText().length()-1 ) ); } ;

// Multiline string literal
STRING_MULTI: '"""' ~( '"""' )* '"""' { setText(getText().substring( 3, getText().length()-3 ) ); } ;

  
// 'C' style single line comments
COMMENT_SL : '//' ~('\r' | '\n')* NEWLINE	{ $channel=HIDDEN; };

// 'C' style single line comments
COMMENT_SL2 : '#' ~('\r' | '\n')* NEWLINE	{ $channel=HIDDEN; };

// An identifier.
ID 	:	ALPHANUM (ALPHANUM | '_')*;

//-----------------------------------------------------------------------------
// Parser
//-----------------------------------------------------------------------------

//---
// Program and clauses
//---
main		: clauseBlock*;

clauseBlock		: clause | '{' (c+=clause)* '}';
clause		: ( varDecl 
		| clauseIf
		| clauseFor
		| clauseAssign
		| funcDecl
		| funcInvoke 
		| sys 
		| task
		| checkPoint
		) (';' | NEWLINE )
		;

// Package declaration
//package		: 'package' STRING;

// Import other modules
//importPackage	: 'import' STRING;

// IF clause
clauseIf		: 'if' expr clauseBlock 
		( 'else' clauseBlock )?
		;
		
// FOR clause
clauseFor		: 'for' ( ID ':' expr ) clauseBlock ;

// Assignmeng clause
clauseAssign	: ID '=' expr ;

//---
// Variable declaration
//---
varDecl		: 'var' i+=varDeclSingle (',' v+=varDeclSingle )* ;
varDeclSingle	: i=ID ('=' e=expr)?	-> ^(VAR_DECL $i $e);
varName		: ID;

classFqName	: ID ('.' ID)+;

//---
// Literals
//---
literal		: literalBool | literalInt | literalList | literalMap | literalReal | literalString;
literalBool		: f=BOOL		-> ^(LITERAL_BOOL $f);
literalInt		: f=INT		-> ^(LITERAL_INT $f);
literalReal		: f=REAL		-> ^(LITERAL_REAL $f);
literalString		: s=(STRING | STRING_SINGLE_QUOTE | STRING_MULTI ) -> ^(LITERAL_STRING $s);
literalMap		: '{'  '}' -> ^(LITERAL_MAP)
		| '{' kv+=keyValuePair ( ',' kv+=keyValuePair  )*  '}' -> ^(LITERAL_MAP $kv)
		;
literalList		: '['  ']' -> ^(LITERAL_LIST)
		| '[' e+=expr ( ',' e+=expr  )*  ']'		-> ^(LITERAL_LIST $e)
		| s+=expr  '...' e+=expr ('by' b=expr)? 		-> ^(LITERAL_LIST_RANGE $s $e $b)
		;

keyValuePair	: k=expr ':' v=expr	-> ^(KEY_VALUE_PAIR $k $v);

//---
// Expressions
//---

//expr		: exprBool | exprNum | exprStr;
exprWs		: e+=expr ( WS e+=expr  )* ;	// Expressions separated by white spaces

//---
// Boolean expressions
//---
expr		: literal
		| '+' => unaryExprPlus
		| '-' => unaryExprMinus
		;
		
unaryExprPlus	: expr;
unaryExprMinus	: expr;
	
//subExprBool		: unaryExprBool | binaryExprBool | '('! exprBool ')'!;
//unaryExprBool	: o=uniOperatorBool e=expressionBool		-> ^(OP_UNARY $o $e);
//binaryExprBool	: l=expressionBool o=binOperatorBool r=expressionBool	-> ^(OP_BINARY $o $l $r);
//binOperatorBool	: '&&' | '||' ;
//uniOperatorBool	: '!' ;
//expressionBool	: literalBool | funcInvoke ;

//---
// Numeric expressions
//---
//exprNum		: subExprNum (binOperatorNum^ subExprNum)*;
//subExprNum		: unaryExprNum | binaryExprNum | '('! exprNum ')'!;
//unaryExprNum 	: o=uniOperatorBool e=expressionBool		-> ^(OP_UNARY $o $e);
//binaryExprNum	: l=expressionNum o=binOperatorNum r=expressionNum	-> ^(OP_BINARY $o $l $r);
//binOperatorNum	: '=='  | '>='  | '>' | '<=' | '<'  | '!=' | '=~' | '!~' | '+' | '-' | '*' | '/' | '%' ;
//expressionNum	: literalReal | literalInt | funcInvoke;

//---
// String expressions
//---
exprStr		: s+=expressionString ('+' s+=expressionString)*		-> ^(OP_BINARY STRING_CONCAT $s);
expressionString	: literalString | funcInvoke | sysInvokeResult ;


//---
// Invoke a function
//---
funcInvoke		: f=ID '(' ')' 			-> ^(FUNC_INVOKE $f)
		| ID '(' e+=expr ( ',' e+=expr  )* ')'	-> ^(FUNC_INVOKE $f $e)
		;

//---
// Invoke a system command
//---
sys		: sysInvokeResult | sysInvoke | sysCmd ;
sysInvokeResult	:'`' e+=exprWs '`' 	-> ^(SYS_INVOKE_RES $e);	// Invoke a system command and return STDOUT as string results
sysInvoke		: 'sys' (sysOptions)* e=exprWs	-> ^(SYS_INVOKE $e);
sysOptions		: 'cpu' '=' expr
		| 'time' '=' expr
		;
sysCmd		: 'sys' cmd=('kill' | 'wait' | 'stderr' | 'stdout' | 'list' ) (e=expr)?;

//---
// Invoke a cluster task
//---
task		: taskInvoke | taskCmd;
taskInvoke		: 'task' (taskOptions)* e+=exprWs 	-> ^(TASK_INVOKE $e);
taskOptions		: 'cpu' '=' expr
		| 'mem' '=' expr
		| 'time' '=' expr
		| 'node' '=' expr
		| 'queue' '=' expr
		;
taskCmd		: 'task' cmd=('kill' | 'wait' | 'stderr' | 'stdout' | 'list' ) (e=expr)?;

//---
// Checkpoint commands
//---
checkPoint		: 'checkPoint';

//---
// Function declaration
//---
funcDecl		: 'func' ID '(' funcArg* ')' clauseBlock ;
funcArg		: ID ('=' expr)?;

//---
// Class declaration
//---
//clasDecl		: 'class' ID '{' 
//			( varDecl | funcDecl )+
//		 '}';
