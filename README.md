<h1>Toy3 Compiler</h1>

<h2>Project Description</h2>

Compiler written in Java that translates programs from the fictional Toy3 language into C.
Produced for the Compilers course of Computer Science at University of Salerno.

<h1>Technical Specifications</h1>

<table>
  <tr style="background-color: #f2f2f2;">
    <th>Symbol</th>
    <th>Token</th>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>program</td>
    <td>PROGRAM</td>
  </tr>
  <tr>
    <td>begin</td>
    <td>BEGIN</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>end</td>
    <td>END</td>
  </tr>
  <tr>
    <td>int</td>
    <td>INT</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>bool</td>
    <td>BOOL</td>
  </tr>
  <tr>
    <td>double</td>
    <td>DOUBLE</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>string</td>
    <td>STRING</td>
  </tr>
  <tr>
    <td>char</td>
    <td>CHAR</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>true</td>
    <td>TRUE</td>
  </tr>
  <tr>
    <td>false</td>
    <td>FALSE</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>return</td>
    <td>RETURN</td>
  </tr>
  <tr>
    <td>if</td>
    <td>IF</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>then</td>
    <td>THEN</td>
  </tr>
  <tr>
    <td>else</td>
    <td>ELSE</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>while</td>
    <td>WHILE</td>
  </tr>
  <tr>
    <td>do</td>
    <td>DO</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>&lt;&lt;</td>
    <td>IN</td>
  </tr>
  <tr>
    <td>&gt;&gt;</td>
    <td>OUT</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>!&gt;&gt;</td>
    <td>OUTNL</td>
  </tr>
  <tr>
    <td>and</td>
    <td>AND</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>or</td>
    <td>OR</td>
  </tr>
  <tr>
    <td>not</td>
    <td>NOT</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>ref</td>
    <td>REF</td>
  </tr>
  <tr>
    <td>def</td>
    <td>DEF</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>+</td>
    <td>PLUS</td>
  </tr>
  <tr>
    <td>-</td>
    <td>MINUS</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>*</td>
    <td>TIMES</td>
  </tr>
  <tr>
    <td>/</td>
    <td>DIV</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>&gt;=</td>
    <td>GE</td>
  </tr>
  <tr>
    <td>&gt;</td>
    <td>GT</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>&lt;=</td>
    <td>LE</td>
  </tr>
  <tr>
    <td>&lt;&gt;</td>
    <td>NE</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>&lt;</td>
    <td>LT</td>
  </tr>
  <tr>
    <td>==</td>
    <td>EQ</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>:=</td>
    <td>ASSIGN</td>
  </tr>
  <tr>
    <td>;</td>
    <td>SEMI</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>:</td>
    <td>COLON</td>
  </tr>
  <tr>
    <td>,</td>
    <td>COMMA</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>(</td>
    <td>LPAR</td>
  </tr>
  <tr>
    <td>)</td>
    <td>RPAR</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>{</td>
    <td>LBRAC</td>
  </tr>
  <tr>
    <td>}</td>
    <td>RBRAC</td>
  </tr>
  <tr style="background-color: #f2f2f2;">
    <td>!</td>
    <td>NOT</td>
  </tr>
  <tr>
    <td>=</td>
    <td>ASSIGNDECL</td>
  </tr>
  <tr>
    <td>|</td>
    <td>PIPE</td>
  </tr>
</table>

<h2>Toy3 Grammar</h2>

Program ::= PROGRAM Decls BEGIN VarDecls Statements END<br>

<br>
Decls ::= VarDecl Decls <br>
&emsp;&emsp; | DefDecl Decls <br>
&emsp;&emsp;          | / epsilon /<br>
<br>
VarDecls ::= VarDecls VarDecl <br>
&emsp;&emsp;           | / epsilon /<br>

<br>
VarDecl ::= VarsOptInit COLON TypeOrConstant SEMI<br>

<br>
VarsOptInit ::= ID PIPE VarsOptInit<br>
&emsp;&emsp;              | ID ASSIGNDECL Expr PIPE VarsOptInit<br>
&emsp;&emsp;              | ID<br>
&emsp;&emsp;              | ID ASSIGNDECL Expr<br>
              
<br>
TypeOrConstant ::= Type<br>
&emsp;&emsp;                 | Constant<br>
<br>
Type ::= INT <br>
&emsp;&emsp;       | BOOL<br>
&emsp;&emsp;       | DOUBLE<br>
&emsp;&emsp;       | STRING <br>
&emsp;&emsp;       | CHAR <br>
<br>
Constant ::= TRUE<br>
&emsp;&emsp;           | FALSE<br>
&emsp;&emsp;           | INT_CONST<br>
&emsp;&emsp;           | DOUBLE_CONST<br>
&emsp;&emsp;           | CHAR_CONST<br>
&emsp;&emsp;           | STRING_CONST<br>
<br>
DefDecl ::= DEF ID LPAR ParDecls RPAR OptType Body<br>
&emsp;&emsp;          | DEF ID LPAR RPAR OptType Body<br>
<br>
ParDecls ::= ParDecl SEMI ParDecls<br>
&emsp;&emsp;           | ParDecl<br>
<br>
ParDecl ::= PVars COLON Type<br>

<br>
PVars ::= PVar COMMA PVars<br>
&emsp;&emsp;        | PVar<br>
        
<br>
PVar ::= ID<br>
&emsp;&emsp;       | REF ID<br>

<br>
OptType ::= COLON Type<br>
&emsp;&emsp;          | / epsilon /<br>
          
<br>
Body ::= LBRAC VarDecls Statements RBRAC<br>

<br>
Statements ::= Stat Statements<br>
&emsp;&emsp;             | / epsilon /<br>

<br>
Stat ::= Vars IN SEMI<br>
&emsp;&emsp;       | Exprs OUT SEMI<br>
&emsp;&emsp;       | Exprs OUTNL SEMI<br>
&emsp;&emsp;       | Vars ASSIGN Exprs SEMI<br>
&emsp;&emsp;       | FunCall SEMI <br>
 &emsp;&emsp;      | IF LPAR Expr RPAR THEN Body ELSE Body<br>
&emsp;&emsp;       | IF LPAR Expr RPAR THEN Body<br>
&emsp;&emsp;       | WHILE LPAR Expr RPAR DO Body<br>
&emsp;&emsp;       | RETURN Expr SEMI<br>

<br>
Vars ::= ID PIPE Vars<br>
&emsp;&emsp;       | ID<br>
       
<br>
Exprs ::= Expr COMMA Exprs<br>
&emsp;&emsp;        | Expr<br>
        
<br>
FunCall ::= ID LPAR Exprs RPAR <br>
&emsp;&emsp;          | ID LPAR RPAR <br>
          
<br>
Expr ::= Expr PLUS Expr<br>
&emsp;&emsp;       | Expr MINUS Expr<br>
&emsp;&emsp;       | Expr TIMES Expr<br>
&emsp;&emsp;       | Expr DIV Expr<br>
&emsp;&emsp;       | Expr AND Expr<br>
&emsp;&emsp;       | Expr OR Expr<br>
&emsp;&emsp;       | Expr GT Expr<br>
&emsp;&emsp;       | Expr GE Expr<br>
&emsp;&emsp;       | Expr LT Expr<br>
&emsp;&emsp;       | Expr LE Expr<br>
&emsp;&emsp;       | Expr EQ Expr<br>
&emsp;&emsp;       | Expr NE Expr<br>
&emsp;&emsp;       | LPAR Expr RPAR<br>
&emsp;&emsp;       | MINUS Expr<br>
&emsp;&emsp;       | NOT Expr<br>
&emsp;&emsp;       | ID<br>
&emsp;&emsp;       | FunCall<br>
&emsp;&emsp;       | Constant<br>
