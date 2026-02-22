grammar Delphi;

program
    : interfaceDecl* classDecl* varDeclSection? block '.' EOF
    ;

classDecl
    : 'class' className=IDENTIFIER
      ('extends' parentName=IDENTIFIER)?
      ('implements' interfaceName=IDENTIFIER)?
      'begin'
      classBody
      'end'
      ';'
    ;

classBody
    : varDeclSection? constructorDecl? destructorDecl? procedureDecl*
    ;


procedureDecl
    : 'procedure' IDENTIFIER '(' ')' ';'
      block
      ';'
    ;

interfaceDecl
    : 'interface' IDENTIFIER
      'begin'
      interfaceBody
      'end'
      ';'
    ;

interfaceBody
    : procedureSignature*
    ;

procedureSignature
    : 'procedure' IDENTIFIER '(' ')' ';'
    ;

constructorDecl
    : 'constructor' IDENTIFIER '(' ')' ';'
      block
      ';'
    ;

destructorDecl
    : 'destructor' IDENTIFIER '(' ')' ';'
      block
      ';'
    ;

objectCreation
    : IDENTIFIER '.' IDENTIFIER '(' ')'
    ;

methodCall
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
    : 'begin' statement* 'end'
    ;

statement
    : assignment
    | writelnStmt
    | methodCall ';'
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
