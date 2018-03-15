grammar BigDataScript;

import BigDataScriptLexerRules;
    
//------------------------------------------------------------------------------
// Parser 
//------------------------------------------------------------------------------

// Main program
programUnit : eol* statement+ EOF;

// End of line (semicolons are optional)
eol : (';' | '\n' )+;

// Include statement
includeFile : 'include' (STRING_LITERAL | STRING_LITERAL_SINGLE) eol;

// Types
typeList : type (',' type)* ;

type : 'bool'                                                                              # typeBool
     | 'int'                                                                               # typeInt
     | 'real'                                                                              # typeReal
     | 'string'                                                                            # typeString 
     | 'void'                                                                              # typeVoid
     | type '[' ']'                                                                        # typeArray
     | type '{' '}'                                                                        # typeMap
     | type '{' type '}'                                                                   # typeMap
     | ID                                                                                  # typeClass
     ;

// Variable declaration
varDeclaration       : type variableInit (',' variableInit)* | variableInitImplicit;
variableInit         : ID ('=' expression)? HELP_LITERAL?;
variableInitImplicit : ID ':=' expression HELP_LITERAL?;

// Function declaration
functionDeclaration  : type ID '(' varDeclaration? (',' varDeclaration)* ')' statement;

// Class field definition
field :
      varDeclaration eol*                                                                  # fieldDeclaration
      | functionDeclaration eol*                                                           # methodDeclaration
      ;

// Class definition
classDef : 'class' ID eol* ('extends' ID)? eol* '{' eol* field* '}' ;

// Statements
statement : '{' statement* '}'                                                             # block
          | 'break' eol*                                                                   # break
          | 'breakpoint' expression? eol*                                                  # breakpoint
          | 'checkpoint' expression? eol*                                                  # checkpoint
          | 'continue' eol*                                                                # continue
          | 'debug' expression? eol*                                                       # debug
          | 'exit' expression? eol*                                                        # exit
          | 'print' expression? eol*                                                       # print
          | 'println' expression? eol*                                                     # println
          | 'warning' expression? eol*                                                     # warning
          | 'error' expression? eol*                                                       # error
          | 'for' '(' ( forInit )? 
                        ';' ( forCondition )? 
                        ';' ( end=forEnd )? 
                  ')' statement eol*                                                       # forLoop
          | 'for' '(' varDeclaration ':' expression ')' statement eol*                     # forLoopList
          | 'if' '(' expression ')' statement eol* ( 'else' statement eol* )?              # if
          | 'kill' expression  eol*                                                        # kill
          | 'return' expression?  eol*                                                     # return
          | 'wait' (expression (',' expression)* )?  eol*                                  # wait
          | 'switch' '(' expression? ')'
                        '{' eol* 
                           ( 'case' expression ':' statement* eol* )* 
                           ( 'default' ':' statement* )? 
                           ( 'case' expression ':' statement* eol* )*
                       '}' eol*                                                            # switch
          | 'while' '(' expression? ')' statement  eol*                                    # while
          | functionDeclaration  eol*                                                      # statementFunctionDeclaration
          | varDeclaration  eol*                                                           # statementVarDeclaration
          | classDef  eol*                                                                 # classDeclaration
          | expression  eol*                                                               # statementExpr
          | includeFile eol*                                                               # statementInclude
          | HELP_LITERAL                                                                   # help
          | eol                                                                            # statmentEol
          ;

forInit : varDeclaration | expressionList;

forCondition : expression;

forEnd : expressionList;

expression : NULL_LITERAL                                                                  # literalNull
           | BOOL_LITERAL                                                                  # literalBool
           | INT_LITERAL                                                                   # literalInt
           | REAL_LITERAL                                                                  # literalReal
           | STRING_LITERAL                                                                # literalString
		   | STRING_LITERAL_SINGLE                                                         # literalString
           | expression '.' ID '('(expression (',' expression )*)? ')'                     # methodCall
           | 'new' ID '('(expression (',' expression )*)? ')'                              # expressionNew
           | ID '('(expression (',' expression )*)? ')'                                    # functionCall
           | expression ('.'
                            (
                               ID
                               | ID '[' expression ']' 
                               | ID '{' expression '}'
                            )+
                        )                                                                  # referenceField
           | ID                                                                            # referenceVar
           | expression '[' expression ']'                                                 # referenceList
           | expression '{' expression '}'                                                 # referenceMap
           | ('++' | '--') expression                                                      # pre
           | expression ('++' | '--')                                                      # post
           | '~' expression                                                                # expressionBitNegation
           | '!' expression                                                                # expressionLogicNot
           | expression '%' expression                                                     # expressionModulo
           | expression '/' expression                                                     # expressionDivide
           | expression '*' expression                                                     # expressionTimes
           | expression '-' expression                                                     # expressionMinus
           | expression '+' expression                                                     # expressionPlus
           | expression '<' expression                                                     # expressionLt
           | expression '>' expression                                                     # expressionGt
           | expression '<=' expression                                                    # expressionLe
           | expression '>=' expression                                                    # expressionGe
           | expression '!=' expression                                                    # expressionNe
           | expression '==' expression                                                    # expressionEq
           | '-' expression                                                                # expressionUnaryMinus
           | '+' expression                                                                # expressionUnaryPlus
           | expression '&' expression                                                     # expressionBitAnd
           | expression '^' expression                                                     # expressionBitXor
           | expression '|' expression                                                     # expressionBitOr
           | expression '&&' expression                                                    # expressionLogicAnd
           | expression '||' expression                                                    # expressionLogicOr
           | '(' expression ')'                                                            # expressionParen
           | expression '?' expression ':' expression                                      # expressionCond
           | expression '<-' expression                                                    # expressionDepOperator
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
           | '(' expression (',' expression )+ ')' '=' expression                          # expressionAssignmentList
           | expression '=' expression                                                     # expressionAssignment
           | ID ':=' expression                                                            # expressionVariableInitImplicit
           ;

expressionList : expression ( ',' expression )* ;

