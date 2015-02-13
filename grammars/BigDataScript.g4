grammar BigDataScript;

import BigDataScriptLexerRules;
    
//------------------------------------------------------------------------------
// Parser 
//------------------------------------------------------------------------------

// Main program
programUnit : eol* statement+;

// End of line (semicolons are optional)
eol : (';' | '\n' )+;

// Types
typeList : type (',' type)* ;

type : 'bool'                                                                              # typePrimitiveBool
     | 'int'                                                                               # typePrimitiveInt
     | 'real'                                                                              # typePrimitiveReal
     | 'string'                                                                            # typePrimitiveString 
     | 'void'                                                                              # typePrimitiveVoid
     | type '[' ']'                                                                        # typeArray
     | type '{' '}'                                                                        # typeMap
     | type '{' type '}'                                                                   # typeMap
     ;

// Variable declaration
varDeclaration       : type variableInit (',' variableInit)* | variableInitImplicit;
variableInit         : ID ('=' expression)? HELP_LITERAL?;
variableInitImplicit : ID ':=' expression HELP_LITERAL?;

// Include statement
includeFile : 'include' (STRING_LITERAL | STRING_LITERAL_SINGLE) eol;

// Statements
statement : '{' statement* '}'                                                             # block
            | 'break' eol*                                                                 # break
            | 'breakpoint' expression? eol*                                                # breakpoint
            | 'checkpoint' expression? eol*                                                # checkpoint
            | 'continue' eol*                                                              # continue
            | 'debug' expression? eol*                                                     # debug
            | 'exit' expression? eol*                                                      # exit
            | 'print' expression? eol*                                                     # print
            | 'println' expression? eol*                                                   # println
            | 'warning' expression? eol*                                                   # warning
            | 'error' expression? eol*                                                     # error
            | 'for' '(' ( forInit )? 
                          ';' ( forCondition )? 
                          ';' ( end=forEnd )? 
                    ')' statement eol*                                                     # forLoop
            | 'for' '(' varDeclaration ':' expression ')' statement eol*                   # forLoopList
            | 'if' '(' expression ')' statement eol* ( 'else' statement eol* )?            # if
            | 'kill' expression  eol*                                                      # kill
            | 'return' expression?  eol*                                                   # return
            | 'wait' (expression (',' expression)* )?  eol*                                # wait
            | 'while' '(' expression? ')' statement  eol*                                  # while
            | type ID '(' varDeclaration? (',' varDeclaration)* ')' statement  eol*        # functionDeclaration
            | varDeclaration  eol*                                                         # statementVarDeclaration
            | expression  eol*                                                             # statementExpr
            | includeFile eol*                                                             # statementInclude
            | eol                                                                          # statmentEol
          ;

forInit : varDeclaration | expressionList;

forCondition : expression;

forEnd : expressionList;

expression : BOOL_LITERAL                                                                  # literalBool
           | INT_LITERAL                                                                   # literalInt
           | REAL_LITERAL                                                                  # literalReal
           | STRING_LITERAL                                                                # literalString
           | STRING_LITERAL_SINGLE                                                         # literalString
           | ID '('(expression (',' expression )*)? ')'                                    # functionCall
           | expression '.' ID '('(expression (',' expression )*)? ')'                     # methodCall
           | ID                                                                            # varReference
           | expression '[' expression ']'                                                 # varReferenceList
           | expression '{' expression '}'                                                 # varReferenceMap
           | expression '&&' expression                                                    # expressionLogicAnd
           | expression '||' expression                                                    # expressionLogicOr
           | expression '&' expression                                                     # expressionBitAnd
           | expression '^' expression                                                     # expressionBitXor
           | expression '|' expression                                                     # expressionBitOr
           | ('++' | '--') expression                                                      # pre
           | expression ('++' | '--')                                                      # post
           | expression '!=' expression                                                    # expressionNe
           | expression '==' expression                                                    # expressionEq
           | expression '%' expression                                                     # expressionModulo
           | expression '/' expression                                                     # expressionDivide
           | expression '*' expression                                                     # expressionTimes
           | expression '-' expression                                                     # expressionMinus
           | expression '+' expression                                                     # expressionPlus
           | expression '<' expression                                                     # expressionLt
           | expression '>' expression                                                     # expressionGt
           | expression '<=' expression                                                    # expressionLe
           | expression '>=' expression                                                    # expressionGe
           | '~' expression                                                                # expressionBitNegation
           | '!' expression                                                                # expressionLogicNot
           | '-' expression                                                                # expressionUnaryMinus
           | '+' expression                                                                # expressionUnaryPlus
           | '(' expression ')'                                                            # expressionParen
           | expression '?' expression ':' expression                                      # expressionCond
           |  expression '<-' expression                                                   # expressionDepOperator
           | '[' ']'                                                                       # literalListEmpty
           | '[' expression (',' expression)* ']'                                          # literalList
           | '{' '}'                                                                       # literalMapEmpty
           | '{' expression '=>' expression (',' expression '=>' expression)* '}'          # literalMap
           | SYS_LITERAL                                                                   # expressionSys
           | TASK_LITERAL                                                                  # expressionTaskLiteral
           | 'task' ( '(' expression (',' expression)* ')' )? statement                    # expressionTask
           | 'dep'    '(' expression (',' expression)* ')'    statement                    # expressionDep
           | 'goal' expression                                                             # expressionGoal
           | ('par' | 'parallel') ( '(' expression (',' expression)* ')' )? statement      # expressionParallel
           | expression '|=' expression                                                    # expressionAssignmentBitOr
           | expression '&=' expression                                                    # expressionAssignmentBitAnd
           | expression '/=' expression                                                    # expressionAssignmentDiv
           | expression '*=' expression                                                    # expressionAssignmentMult
           | expression '-=' expression                                                    # expressionAssignmentMinus
           | expression '+=' expression                                                    # expressionAssignmentPlus
           | ID ':=' expression                                                            # expressionVariableInitImplicit
           | '(' expression (',' expression )+ ')' '=' expression                          # expressionAssignmentList
           | expression '=' expression                                                     # expressionAssignment
           ;

expressionList : expression ( ',' expression )* ;
