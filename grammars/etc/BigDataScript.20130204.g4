grammar BigDataScript;

//------------------------------------------------------------------------------
// Lexer
//------------------------------------------------------------------------------


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

// Newline and white space
NEWLINE : ( '\r' | '\n' | '\r\n' );
WS : ( ' ' | '\t' | '\u000C' ) { skip(); } ;

// Number literals
BOOL_LITERAL : 'true' | 'false' ;
INT_LITERAL : IntegerNumber ;
REAL_LITERAL : NonIntegerNumber ;

// String literal: Any character except quote or backslash OR a backslash 
// followed by any char (this allows to escape the quote character)
// Note: String literals can be multi-line
STRING_LITERAL : '"' ( (~( '"' | '\\' ) | ('\\' .) )* ) '"' ;

// 'sys' literal. Everything after a sys statement.
// Note: It can be multi-line by escaping before the end of line
SYS_LITERAL : 'sys'  ( ' ' | '\t' )+ SysMultiLine;
TASK_MODE : 'task' -> pushMode(TASK); // Grammar mode for task literals

// Comments
COMMENT  : '/*' .*? '*/' { skip(); };
COMMENT_LINE : '//' ~('\n'|'\r')* { skip(); };
COMMENT_LINE_HASH : '#' ~('\n'|'\r')* { skip(); };

// Identifier
ID :    ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

//---
// Grammar mode for task literals
//---

//mode TASK;

//TASK_LITERAL:   ( ' ' | '\t' )+ '('     -> popMode
//                | ( ' ' | '\t' )+ '{'   -> popMode
//                | ~('\r' | '\n')*
//            ;
    
//------------------------------------------------------------------------------
// Parser 
//------------------------------------------------------------------------------

// Main program
programUnit : (statement)+;

// End of line (semicolons are optional)
eol : (';' | NEWLINE)+;

// Types
typeList : type (',' type)* ;

type : 'bool'                                                                       # typePrimitiveBool
     | 'int'                                                                        # typePrimitiveInt
     | 'real'                                                                       # typePrimitiveReal
     | 'string'                                                                     # typePrimitiveString 
     | 'void'                                                                       # typePrimitiveVoid
     | type '[' ']'                                                                 # typeArray
     | type '{' '}'                                                                 # typeMap
     | type '{' type '}'                                                            # typeMap
     ;

// Variable declaration
varDeclaration      : type variableInit (',' variableInit)*;
variableInit        : ID ('=' expression)?;

// Statements
statement : eol                                                                     # statmentEol
          | '{' statement* '}'                                                      # block
          | 'break' eol                                                             # break
          | 'checkpoint' expression? eol                                            # checkpoint
          | 'continue' eol                                                          # continue
          | 'exit' expression? eol                                                  # exit
          | 'for' '(' ( forInit )? 
                        ';' ( forCondition )? 
                        ';' ( end=forEnd )? 
                  ')' statement                                                     # forLoop
          | 'for' '(' varDeclaration ':' expression ')' statement                   # forLoopList
          | 'if' '(' expression ')' statement ( 'else' statement )?                 # if
          | 'return' expression? eol                                                # return
          | 'wait' (expression (',' expression)* )? eol                             # wait
          | 'while' '(' expression ')' statement                                    # while
          | type ID '(' varDeclaration* ')' statement                               # functionDeclaration
          | varDeclaration eol                                                      # statementVarDeclaration
          | expression eol                                                          # statmentExpr
          ;

forInit : varDeclaration | expressionList;

forCondition : expression;

forEnd : expressionList;

expression : BOOL_LITERAL                                                           # literalBool
           | INT_LITERAL                                                            # literalInt
           | REAL_LITERAL                                                           # literalReal
           | STRING_LITERAL                                                         # literalString
           | ID '('(expression (',' expression )*)? ')'                             # functionCall
           | expression '.' ID '('(expression (',' expression )*)? ')'              # methodCall
           | expression '&&' expression                                             # expressionLogicAnd
           | expression '||' expression                                             # expressionLogicOr
           | expression '&' expression                                              # expressionBitAnd
           | expression '^' expression                                              # expressionBitXor
           | expression '|' expression                                              # expressionBitOr
           | ('++' | '--') expression                                               # pre
           | expression ('++' | '--')                                               # post
           | expression '!=' expression                                             # expressionNe
           | expression '==' expression                                             # expressionEq
           | ID                                                                     # varReference
           | expression '[' expression ']'                                          # varReferenceList
           | expression '{' expression '}'                                          # varReferenceMap
           | expression '%' expression                                              # expressionModulo
           | expression '/' expression                                              # expressionDivide
           | expression '*' expression                                              # expressionTimes
           | expression '-' expression                                              # expressionMinus
           | expression '+' expression                                              # expressionPlus
           | expression '<' expression                                              # expressionLt
           | expression '>' expression                                              # expressionGt
           | expression '<=' expression                                             # expressionLe
           | expression '>=' expression                                             # expressionGe
           | '~' expression                                                         # expressionBitNegation
           | '!' expression                                                         # expressionLogicNot
           | '-' expression                                                         # expressionUnaryMinus
           | '+' expression                                                         # expressionUnaryPlus
           | '(' expression ')'                                                     # expressionParen
           | expression '?' expression ':' expression                               # expressionCond
           |  expression (',' expression)* '<-' expression (',' expression)*        # expressionDep
           | '[' expression (',' expression)* ']'                                   # literalList
           | '{' expression '=>' expression (',' expression '=>' expression)* '}'   # literalMap
           | SYS_LITERAL                                                            # expressionSys
           | 'task' ( '(' expression (',' expression)* ')' )? statement             # expressionTask
           | expression '=' expression                                              # expressionAssignment
           ;

expressionList : expression ( ',' expression )* ;
