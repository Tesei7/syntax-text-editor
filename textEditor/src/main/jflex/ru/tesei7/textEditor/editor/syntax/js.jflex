package ru.tesei7.textEditor.editor.syntax;

import ru.tesei7.textEditor.editor.syntax.*;

%%

%public
%class JavaScriptTokenizer
%implements TokenTypes
%implements Tokenizer
%unicode
%char
%type Token


%{
	public Token token(int type) {
		return new TokenImpl(type, yychar, yytext().length());
	}
	
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */

EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
MultilineCommentBegin = "/*" 
MultilineCommentEnd = "*/"

/* identifiers */
IdentifierStart	= ([:jletter:]|"_"|"$")
IdentifierPart = ({IdentifierStart}|[0-9])
Identifier = ({IdentifierStart}{IdentifierPart}*)

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctDigit          = [0-7]
    
/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"]
CommentCharacter = [^\r\n*]
SingleCharacter = [^\r\n\']

%state STRING, CHARLITERAL, COMMENT

%%

<YYINITIAL> {

  /* keywords */
  "abstract"  |
  "arguments" |
  "boolean"   |
  "break"     |
  "byte"      |
  "case"      |
  "catch"     |
  "char"      |
  "class"     |
  "const"     |
  "continue"  |
  "debugger"  |
  "default"   |
  "delete"    |
  "do"        |
  "double"    |
  "else"      |
  "enum"      |
  "eval"      |
  "export"    |
  "extends"   |
  "final"     |
  "finally"   |
  "float"     |
  "for"       |
  "function"  |
  "goto"      |
  "if"        |
  "implements"|
  "import" 	  |
  "in"        |
  "instanceof"|
  "int"       |
  "interface" |
  "let"       |
  "long"      |
  "native"    |
  "new"       |
  "null"      |
  "package"   |
  "private"   |
  "protected" |
  "public"    |
  "return"    |
  "short"     |
  "static"    |
  "super"     |
  "switch"    |
  "synchronized"|
  "this"      |
  "throw"     |
  "throws"    |
  "transient" |
  "try"       |
  "typeof"    |
  "var"       |
  "void"      |
  "volatile"  |
  "while"     |
  "with"      |
  "yield"                        { return token(KEYWORD); }

  /* boolean literals */
  "true"                         { return token(BOOLEAN_LITERAL); }
  "false"                        { return token(BOOLEAN_LITERAL); }
  
  /* separators */
  "("                            { return token(LPAREN); }
  ")"                            { return token(RPAREN); }
  "{"                            { return token(LBRACE); }
  "}"                            { return token(RBRACE); }
  "["                            { return token(LBRACK); }
  "]"                            { return token(RBRACK); }
  ";"                            { return token(SEMICOLON); }
  ","                            { return token(COMMA); }
  "."                            { return token(DOT); }
  
  /* operators */
  "="|                           
  ">"|                          
  "<" |                      
  "!" |                    
  "~" |                     
  "?" |                       
  ":" |                        
  "==" |                        
  "<=" |                       
  ">=" |                      
  "!=" |                      
  "&&" |                     
  "||" |                  
  "++" |                  
  "--" |                  
  "+"  |                  
  "-"  |                  
  "*"  |                  
  "/"  |                  
  "&"  |                  
  "|"  |                  
  "^"  |                  
  "%"  |                  
  "<<" |                  
  ">>" |                  
  ">>>"|     
  "->" |                  
  "+=" |                  
  "-=" |                  
  "*=" |                  
  "/=" |                  
  "&=" |                  
  "|=" |                  
  "^=" |                  
  "%=" |                  
  "<<=" |                 
  ">>=" |                 
  ">>>="                         { return token(OPERATOR); }
  
  /* string literal */
  \"                             { yybegin(STRING); return token(STRING_LITERAL); }

  /* character literal */
  \'                             { yybegin(CHARLITERAL); return token(CHARACTER_LITERAL); }

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  
  {DecIntegerLiteral}            { return token(INTEGER_LITERAL); }
  {HexIntegerLiteral}            { return token(INTEGER_LITERAL); }
  {OctIntegerLiteral}            { return token(INTEGER_LITERAL); }  
  
  {FloatLiteral}                 { return token(FLOATING_POINT_LITERAL); }

  
  /* comments */
  {EndOfLineComment}             { return token(COMMENT_EOL); }
  {MultilineCommentBegin}        { yybegin(COMMENT); return token(COMMENT_MULTI); }

  /* whitespace */
  {WhiteSpace}                   { return token(WHITESPACE); }

  /* identifiers */ 
  {Identifier}                   { return token(IDENTIFIER); }  
}

<COMMENT> {

  {MultilineCommentEnd}          { yybegin(YYINITIAL); return token(COMMENT_MULTI); }

  {LineTerminator}               { return token(COMMENT_MULTI); }               

  {CommentCharacter}+            { return token(COMMENT_MULTI); }
  
  \*                             { return token(COMMENT_MULTI); }
  
  /* error cases */
  \\.                            { return token(OTHER); }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return token(STRING_LITERAL); }
  
  {StringCharacter}+             { return token(STRING_LITERAL); }
  
  {LineTerminator}                { yybegin(YYINITIAL); } 
  
  /* error cases */
  \\.                            { return token(STRING_LITERAL); }
  
}

<CHARLITERAL> {
  \'                             { yybegin(YYINITIAL); return token(CHARACTER_LITERAL); }
  
  {SingleCharacter}+             { return token(CHARACTER_LITERAL); }
  
  {LineTerminator}               { yybegin(YYINITIAL); }  
  
  /* error cases */
  \\.                            { return token(CHARACTER_LITERAL); }
}

/* error fallback */
[^]                              { return token(OTHER); }
<<EOF>>                          { return token(EOF); }