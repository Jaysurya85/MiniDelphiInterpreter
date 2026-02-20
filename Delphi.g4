grammar Delphi;

program
    : classDecl* varDeclSection? block EOF
    ;

classDecl
    : 'class' IDENTIFIER 'begin' classBody 'end' ';'
    ;

classBody
    : varDeclSection? constructorDecl?
    ;

constructorDecl
    : 'constructor' IDENTIFIER '(' ')' ';'
      block
      ';'
    ;

objectCreation
    : IDENTIFIER '.' IDENTIFIER '(' ')'
    ;

varDeclSection
    : 'var' varDecl+
    ;

varDecl
    : IDENTIFIER ':' typeName ';'
    ;

typeName
    : 'integer'
    | IDENTIFIER
    ;

block
    : 'begin' statement* 'end.'
    ;

statement
    : assignment
    | writelnStmt
    ;

assignment
    : (IDENTIFIER | fieldAccess) ':=' expression ';'
    ;

writelnStmt
    : 'writeln' '(' expression ')' ';'
    ;

expression
    : INTEGER
    | fieldAccess
    | objectCreation
    | IDENTIFIER
    ;

fieldAccess
    : IDENTIFIER '.' IDENTIFIER
    ;

IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

INTEGER
    : [0-9]+
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
