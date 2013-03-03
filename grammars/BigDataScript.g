grammar BigDataScript;

options { 
	output = AST;
	backtrack=true;
}

tokens {
	ARGS;
	BLOCK;
	FUNCTION_CALL;
	FUNCTION_DECLARATION;
	FOR_CONDITION;
	FOR_END;
	FOR_INIT;
	FOR_INIT_LIST;
	FOR_LOOP;
	FOR_LOOP_LIST;
	LITERAL_BOOL;
	LITERAL_INT;
	LITERAL_LIST;
	LITERAL_MAP;
	LITERAL_REAL;
	LITERAL_STRING;
	MAIN;
	METHOD_CALL;
	NAME;
	PARAMETERS;
	PRE;
	POST;
	PROGRAM_UNIT;
	SYS_EXPRESSION;
	TASK_LITERAL;
	TASK_OPTIONS;
	TASK_EXPRESSION;
	TYPE;
	TYPE_LIST;
	TYPE_MAP;
	VAR_DECLARATION;
	VAR_INIT;
}

@lexer::header {
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
}

@header {
package ca.mcgill.mcb.pcingola.bigDataScript.antlr;
}

/********************************************************************************************
		Lexer section
*********************************************************************************************/

//---
// Lexer Fragments
//---

fragment IntegerNumber
		: '0'
		| '1'..'9' ('0'..'9')*
		| '0' ('0'..'7')+
		| HexPrefix HexDigit+
		;

fragment EscapeSequence
		: '\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\'' | '\\'
		| ('0'..'3') ('0'..'7') ('0'..'7')
		| ('0'..'7') ('0'..'7')
		| ('0'..'7')
		)
		;

fragment EscapedNewLine : '\\' ( '\r' | '\n' | '\r\n');

fragment Exponent : ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ ;

fragment HexPrefix : '0x' | '0X' ;

fragment HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;


fragment NonIntegerNumber
		: ('0' .. '9')+ '.' ('0' .. '9')* Exponent?
		| '.' ( '0' .. '9' )+ Exponent?
		| ('0' .. '9')+ Exponent
		| ('0' .. '9')+
		;

fragment SysMultiLine : ( EscapedNewLine | ~( '\r' | '\n') )*;

//---
// Lexer tokens
//---

INT_LITERAL : IntegerNumber ;

NEWLINE : ( '\r' | '\n' | '\r\n' );

REAL_LITERAL : NonIntegerNumber ;

// String literal: Any character except quote or backslash OR a backslash followed by any char (this allows to escape the quote character)
STRING_LITERAL : '"' ( (~( '"' | '\\' ) | ('\\' .) )* ) '"' ;

SYS_LITERAL : 'sys'  ( ' ' | '\t' ) SysMultiLine;

WS : ( ' ' | '\t' | '\u000C' ) { skip(); } ;

COMMENT options {greedy=false;} : '/*' .* '*/' ;

LINE_COMMENT : '//' ~('\n'|'\r')* { skip(); };

BOOL : 'bool' ; 
BREAK : 'break' ; 
BY : 'by' ; 
CASE : 'case' ; 
CONTINUE : 'continue' ; 
DEFAULT : 'default' ; 
DO : 'do' ; 
REAL : 'real' ; 
STRING : 'string' ; 
ELSE : 'else' ; 
ENUM : 'enum' ; 
EXTENDS : 'extends' ; 
FOR : 'for' ; 
IF : 'if' ; 
IMPORT : 'import' ; 
INT : 'int' ; 
NEW : 'new' ; 
PACKAGE : 'package' ; 
RETURN : 'return' ; 
SUPER : 'super' ; 
SWITCH : 'switch' ; 
THIS : 'this' ; 
VOID : 'void' ; 
WHILE : 'while' ; 
TRUE : 'true' ; 
FALSE : 'false' ; 
LPAREN : '(' ; 
RPAREN : ')' ; 
LBRACE : '{' ; 
RBRACE : '}' ; 
LBRACKET : '[' ; 
RBRACKET : ']' ; 
SEMI : ';';
COMMA : ',' ; 
DOT : '.' ; 
ELLIPSIS : '...' ; 
EQ : '=' ; 
BANG : '!' ; 
TILDE : '~' ; 
QUES : '?' ; 
COLON : ':' ; 
EQEQ : '==' ; 
AMPAMP : '&&' ; 
BARBAR : '||' ; 
PLUSPLUS : '++' ; 
SUBSUB : '--' ; 
PLUS : '+' ; 
SUB : '-' ; 
STAR : '*' ; 
SLASH : '/' ; 
AMP : '&' ; 
BAR : '|' ; 
CARET : '^' ; 
PERCENT : '%' ; 
PLUSEQ : '+=' ; 
SUBEQ : '-=' ; 
STAREQ : '*=' ; 
SLASHEQ : '/=' ; 
AMPEQ : '&=' ; 
BAREQ : '|=' ; 
CARETEQ : '^=' ; 
PERCENTEQ : '%=' ; 
MONKEYS_AT : '@' ; 
BANGEQ : '!=' ; 
GT : '>' ; 
LT : '<' ; 
GE : '>=' ; 
LE : '<=' ; 
ARROW : '=>' ; 
DEP : '<-' ; 
TASK : 'task';
CHECKPOINT : 'checkpoint';
EXIT : 'exit';
WAIT : 'wait';
IDENTIFIER :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

/********************************************************************************************
		Parser section
*********************************************************************************************/

// Main entry point for this grammar
main : (m+=mainElement)* EOF					-> ^(PROGRAM_UNIT $m*) ;

mainElement : varDeclaration | statement;
	
// End of line (semicolons are optional)
semi : SEMI+  | NEWLINE+;


typeList : type (COMMA type)* ;

//---
// Class, variable and methods / functions declarations
//---
memberDecl : varDeclaration | methodDeclaration ;

varDeclaration : t=type v+=variableInit (COMMA v+=variableInit)* semi 		-> ^(VAR_DECLARATION $t $v*);

variableInit : id=IDENTIFIER (EQ eq=variableInitializer)?			-> ^(VAR_INIT $id $eq?);

variableInitializer : arrayInitializer | expression ;

arrayInitializer : LBRACE (variableInitializer (COMMA variableInitializer)*)? (COMMA)? RBRACE ;

methodDeclaration : t=type n=IDENTIFIER p=formalParameters b=statement	-> ^(FUNCTION_DECLARATION $n $t $p $b);

type :	typePrimitive
	| typeArrayList
	| typeMap
	;

typePrimitive : t=primitiveType					-> ^(TYPE $t);

typeArrayList : t=typePrimitive LBRACKET RBRACKET			-> ^(TYPE_LIST $t);

typeMap : 
	val=primitiveType LBRACE RBRACE				-> ^(TYPE_MAP $val STRING)
	| val=primitiveType LBRACE key=primitiveType RBRACE		-> ^(TYPE_MAP $val $key)
	;

primitiveType : BOOL | INT | REAL | STRING | VOID;

typeArguments : LT type (COMMA type)* GT ;

formalParameters : LPAREN! (formalParameterDecls)? RPAREN! ;

formalParameterDecls : p+=formalParameterDecl (COMMA p+=formalParameterDecl)*	-> ^(PARAMETERS $p*);

formalParameterDecl : t=type v=variableInit				-> ^(VAR_DECLARATION $t $v);

ellipsisParameterDecl : type ELLIPSIS IDENTIFIER ;

//---
// Expressions
//---

expression 	: depExpression;

depExpression :  depl=expressionList (DEP  depr=expressionList)?		-> ^(DEP $depl $depr*);

//expressionList : a+=assignmentExpression (COMMA a+=assignmentExpression )*	-> ^(COMMA  $a*);
expressionList : assignmentExpression (COMMA^ assignmentExpression )*;

assignmentExpression : sysExpression (EQ^ assignmentExpression )?;

sysExpression : conditionalExpression
		| sys
		| task
		;

conditionalExpression : conditionalOrExpression (QUES^ expression COLON conditionalExpression)? ;

conditionalOrExpression : conditionalAndExpression (BARBAR^ conditionalAndExpression)* ;

conditionalAndExpression : inclusiveOrExpression (AMPAMP^ inclusiveOrExpression)* ;

inclusiveOrExpression : exclusiveOrExpression (BAR^ exclusiveOrExpression)* ;

exclusiveOrExpression : andExpression (CARET^ andExpression)* ;

andExpression : equalityExpression (AMP^ equalityExpression)* ;

equalityExpression : relationalExpression ( (EQEQ | BANGEQ)^ relationalExpression)*;

relationalExpression : additiveExpression ((LE | GE | LT | GT)^ additiveExpression)*;

additiveExpression : multiplicativeExpression ( (PLUS | SUB)^ multiplicativeExpression)*;

multiplicativeExpression : methodExpresion ( (STAR | SLASH | PERCENT)^ methodExpresion)*;

methodExpresion : e=listExpresion
		(
		DOT^ IDENTIFIER LPAREN! RPAREN!
		| DOT^ IDENTIFIER arguments
		)*
		;

listExpresion : e=mapExpresion (LBRACKET^ expression RBRACKET!)*;

mapExpresion : e=unaryExpression (LBRACE^ expression RBRACE!)*;

unaryExpression
		: PLUS^ unaryExpression
		| SUB^ unaryExpression    
		| op=PLUSPLUS e=unaryExpression			-> ^(PRE $op $e)
		| op=SUBSUB e=unaryExpression			-> ^(PRE $op $e)
		| unaryExpressionNotPlusMinus
		;

unaryExpressionNotPlusMinus
		: TILDE^ unaryExpression
		| BANG^ unaryExpression
		| e=primary op=SUBSUB				-> ^(POST $op $e)
		| e=primary op=PLUSPLUS			-> ^(POST $op $e)
		| primary
		;

primary
		: parExpression
		| functionCall
		| id=IDENTIFIER				-> ^(NAME $id)
		| li=INT_LITERAL				-> ^(LITERAL_INT $li)
		| lr=REAL_LITERAL				-> ^(LITERAL_REAL $lr)
		| ls=STRING_LITERAL				-> ^(LITERAL_STRING $ls)
		| list
		| map
		| lbt=TRUE 					-> ^(LITERAL_BOOL $lbt)
		| lbf=FALSE					-> ^(LITERAL_BOOL $lbf)
		;

parExpression : LPAREN! expression RPAREN! ;

list :  LBRACKET (e=expressionList)? RBRACKET 				-> ^(LITERAL_LIST $e) ;

map :  LBRACE  (mt+=mapTuple (COMMA mt+=mapTuple)* )?  RBRACE 		-> ^(LITERAL_MAP $mt*) ;

mapTuple :	 expression ARROW^ expression;

arguments :  LPAREN (e=expressionList)? RPAREN 				-> ^(ARGS $e) ;

functionCall :	id=IDENTIFIER LPAREN RPAREN			-> ^(FUNCTION_CALL $id )
		| id=IDENTIFIER arg=arguments			-> ^(FUNCTION_CALL $id $arg)
		;

//---
// Statements
//---
block : LBRACE (st+=blockStatement)* RBRACE				-> ^(BLOCK $st*);

blockStatement : localVariableDeclarationStatement | statement ;

localVariableDeclarationStatement : localVariableDeclaration semi! ;

localVariableDeclaration : t=type v+=variableInit (COMMA v+=variableInit)* 	-> ^(VAR_DECLARATION $t $v*);

statement
		: expression^ semi!
		| block
		| BREAK^ semi!
		| CONTINUE^ semi!
		| CHECKPOINT^ expression? semi!
		| EXIT^ expression semi!
		| forstatement
		| IF c=parExpression st=statement (ELSE se=statement)?	-> ^(IF $c $st $se?)
		| methodDeclaration
		| RETURN^ (expression )? semi!
		| WAIT^ semi!
		| WHILE c=parExpression s=statement		-> ^(WHILE $c $s)
		| semi!
		;

// For loop
forstatement
		: FOR LPAREN ( init=forInit )? SEMI ( cond=forCond )? SEMI ( end=forEnd )? RPAREN st=statement	-> ^(FOR_LOOP $init? $cond? $end? $st) // normal for loop
		| FOR LPAREN init2=forInit2 COLON ex=expression RPAREN st=statement			-> ^(FOR_LOOP_LIST $init2 $ex $st) // enhanced for 
		;

forInit : 
	lv=localVariableDeclaration				-> ^(FOR_INIT $lv)
	| el=expressionList 					-> ^(FOR_INIT $el)
	;

forCond : e=expression						-> ^(FOR_CONDITION $e);

forEnd : e=expressionList					-> ^(FOR_END $e);

forInit2 : t=type id=IDENTIFIER					-> ^(VAR_DECLARATION $t ^(VAR_INIT $id));

// Command statement
sys : s=SYS_LITERAL						-> ^(SYS_EXPRESSION $s);

// Task statement

 task : TASK (opt=taskOptions)? s=statement				-> ^(TASK_EXPRESSION $opt? $s);
 	
 taskOptions : LPAREN e+=expression (COMMA e+=expression)*	RPAREN 
 							-> ^(TASK_OPTIONS $e*);
 							

