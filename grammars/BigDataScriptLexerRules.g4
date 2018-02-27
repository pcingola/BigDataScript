lexer grammar BigDataScriptLexerRules;

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
        : '\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\'
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

// Number literals
NULL_LITERAL      : 'null';
BOOL_LITERAL      : 'true' | 'false' ;
INT_LITERAL       : IntegerNumber ;
REAL_LITERAL      : NonIntegerNumber ;

// String literal: Any character except quote or backslash OR a backslash 
// followed by any char (this allows to escape the quote character)
// Note: String literals can be multi-line
STRING_LITERAL    : '"' ( (~( '"' | '\\' ) | ('\\' .) )* ) '"' ;

// Single quote string literal (un-interpolated)
STRING_LITERAL_SINGLE 
                  : '\'' (~( '\'' ))*  '\'' ;

// 'sys' literal. Everything after a sys statement.
// Note: It can be multi-line by escaping before the end of line
HELP_LITERAL      : 'help' ( ' ' | '\t' )+ SysMultiLine;
SYS_LITERAL       : 'sys'  ( ' ' | '\t' )+ SysMultiLine;
TASK_LITERAL      : 'task' ( ' ' | '\t' )+ ~('(' | '{' | '\r' | '\n' ) SysMultiLine;

// Comments
COMMENT           : '/*' .*? '*/' { skip(); };
COMMENT_LINE      : '//' ~('\n'|'\r')* { skip(); };
COMMENT_LINE_HASH : '#' ~('\n'|'\r')* { skip(); };

// Identifier
ID                :    ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

// Newline and white space
WS                : ( ' ' | '\t' | '\r' | '\\\n' | '\\\r\n' | '\u000C' ) { skip(); } ;
