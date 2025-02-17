package main.java.org.example;
import java.util.HashMap;
import java_cup.runtime.Symbol;
import java.util.Collections;


%%

%class Lexer
%cupsym sym
%cup
%line
%column
%unicode

Digit = [0-9]
Letter = [a-zA-Z]
ID = {Letter}({Letter}|{Digit})*
Digits = {Digit}+
Number = {Digits}(\.{Digits})?([Ee][+-]?{Digits})?
DoubleNumber = {Digits}(\.{Digits})
InputCharacter = [^\r\n]

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*

%{
    StringBuffer string = new StringBuffer();
        private Symbol symbol(int type) {
            Symbol s = new Symbol(type, yyline, yycolumn);
            //System.out.println("Token: " + sym.terminalNames[type] );
            return s;
        }
        private Symbol symbol(int type, Object value) {
            //System.out.println("Token: " + sym.terminalNames[type] + ", Value: " + value.toString());
            return new Symbol(type, yyline, yycolumn, value);
        }

        // Tabella degli identificatori (stringhe)
        public static HashMap<String, String> stringTable = new HashMap<>();

        private Symbol installID(String value) {
            if(!stringTable.containsKey(value)) {
                stringTable.put(value, value);
            }
            return symbol(sym.ID, value);
        }
%}



%state STRING

%%
<YYINITIAL>{
    // Parole chiave
    "program"        { return symbol(sym.PROGRAM); }
    "begin"          { return symbol(sym.BEGIN); }
    "end"            { return symbol(sym.END); }
    "int"            { return symbol(sym.INT); }
    "bool"           { return symbol(sym.BOOL); }
    "double"         { return symbol(sym.DOUBLE); }
    "string"         { return symbol(sym.STRING); }
    "char"           { return symbol(sym.CHAR); }
    "true"           { return symbol(sym.TRUE); }
    "false"          { return symbol(sym.FALSE); }
    "return"         { return symbol(sym.RETURN); }
    "if"             { return symbol(sym.IF); }
    "then"           { return symbol(sym.THEN); }
    "else"           { return symbol(sym.ELSE); }
    "while"          { return symbol(sym.WHILE); }
    "do"             { return symbol(sym.DO); }
    "<<"             { return symbol(sym.IN); }
    ">>"             { return symbol(sym.OUT); }
    "!>>"            { return symbol(sym.OUTNL); }
    "and"            { return symbol(sym.AND); }
    "or"             { return symbol(sym.OR); }
    "not"            { return symbol(sym.NOT); }
    "ref"            { return symbol(sym.REF); }
    "def"            { return symbol(sym.DEF); }
    // Operatori e simboli
    "+"              { return symbol(sym.PLUS); }
    "-"              { return symbol(sym.MINUS); }
    "*"              { return symbol(sym.TIMES); }
    "/"              { return symbol(sym.DIV); }
    ">="             { return symbol(sym.GE); }
    ">"              { return symbol(sym.GT); }
    "<="             { return symbol(sym.LE); }
    "<>"             { return symbol(sym.NE); }
    "<"              { return symbol(sym.LT); }
    "=="             { return symbol(sym.EQ); }
    ":="             { return symbol(sym.ASSIGN); }
    ";"              { return symbol(sym.SEMI); }
    ":"              { return symbol(sym.COLON); }
    ","              { return symbol(sym.COMMA); }
    "("              { return symbol(sym.LPAR); }
    ")"              { return symbol(sym.RPAR); }
    "{"              { return symbol(sym.LBRAC); }
    "}"              { return symbol(sym.RBRAC); }
    "!"              { return symbol(sym.NOT); }
    "|"              { return symbol(sym.PIPE); }
    "="             { return symbol(sym.ASSIGNDECL); }

}

<YYINITIAL> {
    // Identificatori e costanti
    {ID}             { return installID(yytext()); }
    {Digits}         { return symbol(sym.INT_CONST, yytext()); }
    {DoubleNumber}   { return symbol(sym.DOUBLE_CONST, yytext()); }
    // Spazi bianchi e commenti
    {WhiteSpace}     { /* Ignora spazi bianchi */ }
    {Comment}        { /* Ignora commenti */ }
}


\"([^\"\\]|\\.)*\"   { return symbol(sym.STRING_CONST, yytext()); }
\'([^\'\\]|\\.)\'    { return symbol(sym.CHAR_CONST, yytext()); }

. { return symbol(sym.error, "Error at char: " + yytext() + " in line: " + (yyline + 1) + " and column: " + (yycolumn + 1)); }
