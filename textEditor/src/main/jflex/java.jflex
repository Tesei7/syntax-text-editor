package ru.tesei7.textEditor.editor.syntax;

import ru.tesei7.textEditor.editor.syntax.*;

%%

%public
%class JavaTokenizer
%implements TokenTypes
%implements Tokenizer
%unicode
%type JavaToken

%{
	StringBuilder string = new StringBuilder();

	public JavaToken symbol(int type) {
		return new JavaToken(type, yytext());
	}
	
	public JavaToken symbol(int type, char c) {
		return new JavaToken(type, c);
	}
	
	public JavaToken symbol(int type, String s) {
		return new JavaToken(type, s);
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
Identifier = [:jletter:][:jletterdigit:]*
Annotation = "@"{Identifier}

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]
    
/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]
CommentCharacter = [^\r\n*]
SingleCharacter = [^\r\n\'\\]

%state STRING, CHARLITERAL, COMMENT

%%

<YYINITIAL> {

  /* keywords */
  "abstract" |  
  "boolean"  |  
  "break"    |  
  "byte"     |  
  "case"     |  
  "catch"    |  
  "char"     |  
  "class"    |  
  "const"    |  
  "continue" |  
  "do"       |  
  "double"   |  
  "else"     |  
  "extends"  |  
  "final"    |  
  "finally"  |  
  "float"    |  
  "for"      |  
  "default"  |  
  "implements"|     
  "import"    |     
  "instanceof"|     
  "int"       |     
  "interface" | 
  "@interface"|     
  "long"      |     
  "native"    |     
  "new"       | 
  "null"      |     
  "goto"      |     
  "if"        |     
  "public"    |     
  "short"     |     
  "super"     |     
  "switch"    |     
  "synchronized"|   
  "package"     |       
  "private"     |       
  "protected"   |       
  "transient"   |       
  "return"      |       
  "void"        |        
  "static"      |        
  "while"       |        
  "this"        |        
  "throw"       |        
  "throws"      |        
  "try"         |        
  "volatile"    |        
  "strictfp"                     { return symbol(KEYWORD); }

  /* boolean literals */
  "true"                         { return symbol(BOOLEAN_LITERAL); }
  "false"                        { return symbol(BOOLEAN_LITERAL); }
  
  /* separators */
  "("                            { return symbol(LPAREN); }
  ")"                            { return symbol(RPAREN); }
  "{"                            { return symbol(LBRACE); }
  "}"                            { return symbol(RBRACE); }
  "["                            { return symbol(LBRACK); }
  "]"                            { return symbol(RBRACK); }
  ";"                            { return symbol(SEMICOLON); }
  ","                            { return symbol(COMMA); }
  "."                            { return symbol(DOT); }
  
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
  ">>>="                         { return symbol(OPERATOR); }
  
  /* string literal */
  \"                             { yybegin(STRING); return symbol(STRING_LITERAL); }

  /* character literal */
  \'                             { yybegin(CHARLITERAL); return symbol(CHARACTER_LITERAL); }

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  "-2147483648"                  { return symbol(INTEGER_LITERAL); }
  
  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL); }
  {DecLongLiteral}               { return symbol(INTEGER_LITERAL); }
  
  {HexIntegerLiteral}            { return symbol(INTEGER_LITERAL); }
  {HexLongLiteral}               { return symbol(INTEGER_LITERAL); }
 
  {OctIntegerLiteral}            { return symbol(INTEGER_LITERAL); }  
  {OctLongLiteral}               { return symbol(INTEGER_LITERAL); }
  
  {FloatLiteral}                 { return symbol(FLOATING_POINT_LITERAL); }
  {DoubleLiteral}                { return symbol(FLOATING_POINT_LITERAL); }
  {DoubleLiteral}[dD]            { return symbol(FLOATING_POINT_LITERAL); }
  
  /* comments */
  {EndOfLineComment}             { return symbol(COMMENT_EOL); }
  {MultilineCommentBegin}        { yybegin(COMMENT); return symbol(COMMENT_MULTI); }

  /* whitespace */
  {WhiteSpace}                   { return symbol(WHITESPACE); }

  /* identifiers */ 
  {Identifier}                   { return symbol(IDENTIFIER); }  
  {Annotation}                   { return symbol(ANNOTATION); }
}

<COMMENT> {

  {MultilineCommentEnd}          { yybegin(YYINITIAL); return symbol(COMMENT_MULTI); }

  {LineTerminator}               { return symbol(COMMENT_MULTI); }               

  {CommentCharacter}+            { return symbol(COMMENT_MULTI); }
  
  \*                             { return symbol(COMMENT_MULTI); }
  
  /* error cases */
  \\.                            { return symbol(OTHER); }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return symbol(STRING_LITERAL); }
  
  {StringCharacter}+             { return symbol(STRING_LITERAL); }
  
  /* escape sequences */
  "\\b"   |                       
  "\\t"   |                       
  "\\n"   |                       
  "\\f"   |                       
  "\\r"   |                       
  "\\\""  |                       
  "\\'"   |                       
  "\\\\"                          { return symbol(STRING_LITERAL); }
  {LineTerminator}                { yybegin(YYINITIAL); } 
  
  /* error cases */
  \\.                            { return symbol(OTHER); }
  
}

<CHARLITERAL> {
  \'                             { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL); }
  
  {SingleCharacter}+             { return symbol(STRING_LITERAL); }
  
  /* escape sequences */
  "\\b"\'    |                    
  "\\t"\'    |                    
  "\\n"\'    |                    
  "\\f"\'    |                    
  "\\r"\'    |                    
  "\\\""\'   |                    
  "\\'"\'    |                    
  "\\\\"\'                       { return symbol(CHARACTER_LITERAL); }
  {LineTerminator}               { yybegin(YYINITIAL); }  
  
  /* error cases */
  \\.                            { return symbol(OTHER); }
}

/* error fallback */
[^]                              { return symbol(OTHER); }
<<EOF>>                          { return symbol(EOF); }