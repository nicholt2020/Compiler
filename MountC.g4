grammar MountC;

/** The start rule; begin parsing here.
    This stripped down version of MountC only allows function calls
    of at most one parameter (to accommodate putint),
    and function declarations of no parameters.
    Also, the function can only have a single term show up in the
    return statement.
*/

program:   fun_def_list ;

fun_def_list:  fun_def fun_def_list
    |          /* epsilon */
    ;

fun_def:  ID '(' id_list ')'  '{' 'return' expr_list ';' '}' ;

id_list:  ID (',' ID)*
    |     /* epsilon */
    ;

id_list_tail:  ',' ID id_list_tail
    |          /* epsilon */
    ;

expr_list: expr expr_list_tail;

expr_list_tail: ',' expr expr_list_tail
    |           /* epsilon */
    ;

expr:    expr thenPart expr elsePart expr  # ifExpr
    |    term                    # termExpr
    |    term expr_tail          # termExprTail
    |    '(' expr_list ')'       # exprList
    ;

thenPart: '?' ;
elsePart: ':' ;

expr_tail:    op expr
    ;

term: NUM            # numTerm
    | ID term_tail   # idTerm
    ;

term_tail: '(' arg_list ')'     # funCall
    |      '=' expr        # equExp
    |      /* epsilon */    # epsilTT
    ;

arg_list: expr (',' expr)*    # argListExpr
    | /* epsilon */             # emptyArgList
    ;

arg_list_tail: ',' expr arg_list_tail # actualArg
    |          /* epsilon */  # emptyArglist
    ;

op:    '+'            # addOp
    |  '-'            # minusOp
    ;

ID  :   [a-zA-Z][a-zA-Z0-9]* ;      // match identifiers <label id="code.tour.expr.3"/>
NUM :   [0-9]+ ;         // match integers
WS  :   ([ \r\n\t]+) -> skip ; // toss out whitespace
COMMENT : '//' .*? '\n' -> skip ; // toss out end of line comments
