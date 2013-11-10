grammar BigDataScript;

options { 
		output = AST;
		backtrack=true;
		memoize=true;
}

/********************************************************************************************
		Lexer section
*********************************************************************************************/

INT_LITERAL : IntegerNumber ;

fragment
IntegerNumber
		: '0'
		| '1'..'9' ('0'..'9')*
		| '0' ('0'..'7')+
		| HexPrefix HexDigit+
		;

fragment HexPrefix : '0x' | '0X' ;

fragment HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;


fragment
NonIntegerNumber
		: ('0' .. '9')+ '.' ('0' .. '9')* Exponent?
		| '.' ( '0' .. '9' )+ Exponent?
		| ('0' .. '9')+ Exponent
		| ('0' .. '9')+
		|
		HexPrefix (HexDigit )*
		( ()
		| ('.' (HexDigit )* )
		)
		( 'p' | 'P' )
		( '+' | '-' )?
		( '0' .. '9' )+
		;

fragment Exponent : ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ ;

REAL_LITERAL : NonIntegerNumber ;

STRING_LITERAL : '"' ( EscapeSequence | ~( '\\' | '"' | '\r' | '\n' ))* '"' ;

// Multiline string literal
STRING_LITERAL_MULTILINE: '"""' ~( '"""' )* '"""' { setText(getText().substring( 3, getText().length()-3 ) ); } ;

fragment
EscapeSequence
		: '\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\'' | '\\'
		| ('0'..'3') ('0'..'7') ('0'..'7')
		| ('0'..'7') ('0'..'7')
		| ('0'..'7')
		)
	;

WS : ( ' ' | '\r' | '\t' | '\u000C' | '\n') { skip(); } ;

COMMENT
		@init{
		boolean isJavaDoc = false;
		}
		: '/*'
		{
		if((char)input.LA(1) == '*'){
		isJavaDoc = true;
		}
		}
		(options {greedy=false;} : . )*
		'*/'
		{
		if(isJavaDoc==true){
		$channel=HIDDEN;
		}else{
		skip();
		}
		}
		;

LINE_COMMENT
		: '//' ~('\n'|'\r')* ('\r\n' | '\r' | '\n') { skip(); }
		| '//' ~('\n'|'\r')* { skip(); } // a line comment could appear at the end of the file without CR/LF
		;

BOOL : 'bool' ; 
BREAK : 'break' ; 
BY : 'by' ; 
CASE : 'case' ; 
//CLASS : 'class' ; 
CONTINUE : 'continue' ; 
DEFAULT : 'default' ; 
DO : 'do' ; 
REAL : 'real' ; 
STRING : 'string' ; 
LIST : 'list' ; 
MAP : 'map' ; 
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
SEMI : ';' ; 
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
IDENTIFIER :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

/********************************************************************************************
		Parser section
*********************************************************************************************/

compilationUnit : (type )* ;

typeList : type (COMMA type)* ;

classBody : LBRACE (classBodyDeclaration)* RBRACE ;

classBodyDeclaration
		: SEMI
		| memberDecl
		;

memberDecl
		: fieldDeclaration
		| methodDeclaration
		;

methodDeclaration
		:
		IDENTIFIER formalParameters LBRACE (explicitConstructorInvocation)? (blockStatement)* RBRACE
		| (type | VOID) IDENTIFIER formalParameters (LBRACKET RBRACKET)* ( block | SEMI)
		;

fieldDeclaration : type variableDeclarator (COMMA variableDeclarator)* SEMI ;

variableDeclarator : IDENTIFIER (LBRACKET RBRACKET)* (EQ variableInitializer)? ;

type
		: classType (LBRACKET RBRACKET)*
		| primitiveType (LBRACKET RBRACKET)*
		;

classType : IDENTIFIER (typeArguments)? (DOT IDENTIFIER (typeArguments)?)* ;

primitiveType : BOOL | INT | REAL | STRING | LIST | MAP;

typeArguments : LT typeArgument (COMMA typeArgument)* GT ;

typeArgument
		: type
		| QUES ( (EXTENDS |SUPER) type)?
		;

qualifiedNameList : qualifiedName (COMMA qualifiedName)* ;

formalParameters : LPAREN (formalParameterDecls)? RPAREN ;

formalParameterDecls
		: ellipsisParameterDecl
		| normalParameterDecl (COMMA normalParameterDecl)*
		| (normalParameterDecl COMMA)+ ellipsisParameterDecl
		;

normalParameterDecl : type IDENTIFIER (LBRACKET RBRACKET)* ;

ellipsisParameterDecl : type ELLIPSIS IDENTIFIER ;

explicitConstructorInvocation
		: (nonWildcardTypeArguments)? (THIS |SUPER) arguments SEMI //NOTE: the position of Identifier 'super' is set to the type args position here
		| primary DOT (nonWildcardTypeArguments)? SUPER arguments SEMI
		;

qualifiedName : IDENTIFIER (DOT IDENTIFIER)* ;

elementValuePairs : elementValuePair (COMMA elementValuePair)* ;

elementValuePair : IDENTIFIER EQ elementValue ;

elementValue
		: conditionalExpression
		| elementValueArrayInitializer
		;

elementValueArrayInitializer : LBRACE (elementValue (COMMA elementValue)*)? (COMMA)? RBRACE ;

block : LBRACE (blockStatement)* RBRACE ;

blockStatement
		: localVariableDeclarationStatement
		| statement
		;

localVariableDeclarationStatement : localVariableDeclaration SEMI ;

localVariableDeclaration : type variableDeclarator (COMMA variableDeclarator)* ;

statement
		: block 
		| IF parExpression statement (ELSE statement)?
		| forstatement
		| WHILE parExpression statement
		| DO statement WHILE parExpression SEMI
		| SWITCH parExpression LBRACE switchBlockStatementGroups RBRACE
		| RETURN (expression )? SEMI
		| BREAK (IDENTIFIER)? SEMI
		| CONTINUE (IDENTIFIER)? SEMI
		| expression SEMI
		| IDENTIFIER COLON statement
		| SEMI
		;

switchBlockStatementGroups : (switchBlockStatementGroup )* ;

switchBlockStatementGroup : switchLabel (blockStatement)* ;

switchLabel
		: CASE expression COLON
		| DEFAULT COLON
		;


formalParameter : type IDENTIFIER (LBRACKET RBRACKET)* ;

forstatement
		: FOR LPAREN type IDENTIFIER COLON expression RPAREN statement // enhanced for loop
		| FOR LPAREN (forInit )? SEMI (expression )? SEMI (expressionList )? RPAREN statement // normal for loop
		;

forInit : localVariableDeclaration | expressionList ;

parExpression : LPAREN expression RPAREN ;

expressionList : expression (COMMA expression )* ;

expression : expressionMain ;

expressionMain : conditionalExpression (assignmentOperator expressionMain )? ;

assignmentOperator
		: EQ
		| PLUSEQ
		| SUBEQ
		| STAREQ
		| SLASHEQ
		| AMPEQ
		| BAREQ
		| CARETEQ
		| PERCENTEQ
		| LT LT EQ
		| GT GT GT EQ
		| GT GT EQ
		;

conditionalExpression : conditionalOrExpression (QUES expression COLON conditionalExpression)? ;

conditionalOrExpression : conditionalAndExpression (BARBAR conditionalAndExpression)* ;

conditionalAndExpression : inclusiveOrExpression (AMPAMP inclusiveOrExpression)* ;

inclusiveOrExpression : exclusiveOrExpression (BAR exclusiveOrExpression)* ;

exclusiveOrExpression : andExpression (CARET andExpression)* ;

andExpression : equalityExpression (AMP equalityExpression)* ;

equalityExpression : instanceOfExpression ( ( EQEQ | BANGEQ) instanceOfExpression)* ;

instanceOfExpression : relationalExpression ;

relationalExpression : shiftExpression (relationalOp shiftExpression)* ;

relationalOp : LT EQ | GT EQ | LT | GT ;

shiftExpression : additiveExpression (shiftOp additiveExpression)* ;

shiftOp : LT LT | GT GT GT | GT GT ;

additiveExpression : multiplicativeExpression ( ( PLUS | SUB) multiplicativeExpression)* ;

multiplicativeExpression : unaryExpression ( ( STAR | SLASH | PERCENT) unaryExpression)* ;

/**
 * NOTE: for '+' and '-', if the next token is int or long interal, then it's not a unary expression.
 * it's a literal with signed value. INTLTERAL AND LONG LITERAL are added here for this.
 */
unaryExpression
		: PLUS unaryExpression
		| SUB unaryExpression
		| PLUSPLUS unaryExpression
		| SUBSUB unaryExpression
		| unaryExpressionNotPlusMinus
		;

unaryExpressionNotPlusMinus
		: TILDE unaryExpression
		| BANG unaryExpression
		| castExpression
		| primary (selector)* ( PLUSPLUS | SUBSUB)?
		;

castExpression
		: LPAREN primitiveType RPAREN unaryExpression
		| LPAREN type RPAREN unaryExpressionNotPlusMinus
		;

/**
 * Have to use scope here, parameter passing isn't well supported in antlr.
 */
primary
		: parExpression
		| THIS (DOT IDENTIFIER)* (identifierSuffix)?
		| IDENTIFIER (DOT IDENTIFIER)* (identifierSuffix)?
		| SUPER superSuffix
		| literal
		| creator
		;


superSuffix
		: arguments
		| DOT (typeArguments)? IDENTIFIER (arguments)?
		;


identifierSuffix
		:  (LBRACKET expression RBRACKET)+
		| arguments
		| DOT nonWildcardTypeArguments IDENTIFIER arguments
		| DOT THIS
		| DOT SUPER arguments
		| innerCreator
		;


selector
		: DOT IDENTIFIER (arguments)?
		| DOT THIS
		| DOT SUPER superSuffix
		| innerCreator
		| LBRACKET expression RBRACKET
		;

creator
		: NEW nonWildcardTypeArguments classType classCreatorRest
		| NEW classType classCreatorRest
		| arrayCreator
		;

arrayCreator
		: NEW createdName LBRACKET RBRACKET (LBRACKET RBRACKET)* arrayInitializer 
		| NEW createdName LBRACKET expression RBRACKET ( LBRACKET expression RBRACKET)* (LBRACKET RBRACKET)*
		;

variableInitializer
		: arrayInitializer
		| expression
		;

arrayInitializer : LBRACE (variableInitializer (COMMA variableInitializer)*)? (COMMA)? RBRACE ;


createdName
		: classType
		| primitiveType
		;

innerCreator : DOT NEW (nonWildcardTypeArguments)? IDENTIFIER (typeArguments)? classCreatorRest ;

classCreatorRest : arguments (classBody)? ;

nonWildcardTypeArguments : LT typeList GT ;

arguments : LPAREN (expressionList)? RPAREN ;

literal : INT_LITERAL | REAL_LITERAL | STRING_LITERAL | STRING_LITERAL_MULTILINE | TRUE | FALSE;

methodHeader : (type|VOID)? IDENTIFIER RPAREN ;

fieldHeader : type IDENTIFIER (LBRACKET RBRACKET)* (EQ|COMMA|SEMI) ;

localVariableHeader : type IDENTIFIER (LBRACKET RBRACKET)* (EQ|COMMA|SEMI) ;

