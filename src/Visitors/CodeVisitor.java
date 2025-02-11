package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.ParDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.*;
import Nodes.ProgramOp;
import Nodes.Stat.*;
import Nodes.Type;
import SymbolTable.*;
import Visitors.OpTable.OpTableCombinations;
import java_cup.runtime.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeVisitor implements Visitor {
    private static SymbolTable symbolTableLocal;
    private final String fileName;

    private static FileWriter fileWriter;

    private ArrayList<DefDeclOp> dichiarazioniFunzioni = new ArrayList<>();

    public CodeVisitor(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public Object visit(ProgramOp programOp) {

        programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> {
            dichiarazioniFunzioni.add(defDeclOp);
        });

        try {

            String path = "test_files" + File.separator + "c_out" + File.separator;

            if (!Files.exists(Path.of(path)))
                new File(path).mkdirs();

            File file = new File(path + File.separator + this.fileName + ".c");

            file.createNewFile();
            fileWriter = new FileWriter(file);


            addUtils();

            addFirmeFunzioni();
            fileWriter.write("\n");


            symbolTableLocal = programOp.getSymbolTable();
            programOp.getDeclOp().getVarDeclOps().forEach(varDeclOp -> {
                varDeclOp.accept(this);
            });


            programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> {
                defDeclOp.accept(this);
            });

            fileWriter.write("\n\nint main() {\n");
            programOp.getBodyOp().accept(this);

            if (programOp.getBodyOp().getStatOps().size() == 0){
                fileWriter.write("return 0;");
            }else {
                if(!(programOp.getBodyOp().getStatOps().get(programOp.getBodyOp().getStatOps().size()-1) instanceof ReturnOp))
                    fileWriter.write("return 0;");
            }
            fileWriter.write("\n}");

            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        symbolTableLocal = bodyOp.getSymbolTable();
        bodyOp.getVarDeclOps().forEach(varDeclOp -> {
            varDeclOp.accept(this);
        });
        bodyOp.getStatOps().forEach(statOp -> {
            statOp.accept(this);
        });
        return null;
    }

    @Override
    public Object visit(DefDeclOp defDeclOp) {
        symbolTableLocal = defDeclOp.getSymbolTable();
        try {
            fileWriter.write("\n");
            scriviNelFileFirmaFunzione(defDeclOp, defDeclOp.getType());
            fileWriter.write("{\n");
            defDeclOp.getBodyOp().getVarDeclOps().forEach(varDeclOp -> {
                varDeclOp.accept(this);
            });
            defDeclOp.getBodyOp().getStatOps().forEach(statOp -> {
                    statOp.accept(this);
            });
            fileWriter.write("}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        varDeclOp.getVarsOptInitOpList().forEach(varsOptInitOp -> {
            SymbolRow symbolRow = symbolTableLocal.lookUpWithKind(varsOptInitOp.getId().getValue(), "Var");
            String type = symbolRow.getType().getOutType().getName().toLowerCase();

            try {
                if(varsOptInitOp.getExpr() == null) {
                    //operazione su stringhe di strcpy
                    if(varDeclOp.getTypeOrConstOp().isConstant()){
                        if(type.equals("string")){
                            type = "char";
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "[MAXCHAR] = " + varDeclOp.getTypeOrConstOp().getConstant().accept(this) + ";\n");
                        } else {
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + " = " + varDeclOp.getTypeOrConstOp().getConstant().accept(this) + ";\n");
                        }

                    } else {
                        if(type.equals("string")){
                            type = "char";
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "[MAXCHAR]; \n");
                        } else {
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "; \n");
                        }
                    }
                }
                else{
                    //qua invece concat
                    if(type.equals("string")) {
                        type = "char";
                        if(varsOptInitOp.getExpr() instanceof ArithOp){
                            type = "char*";
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + " = ");
                            concatOp(varsOptInitOp.getExpr());
                            fileWriter.write(";\n");
                        } else if(varsOptInitOp.getExpr() instanceof FunCallOpExpr){
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "[MAXCHAR];\n");
                            fileWriter.write("strcpy(" + varsOptInitOp.getId().getValue() + ", " + ConvertExprToString(varsOptInitOp.getExpr())+ ");\n");
                        } else {
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "[MAXCHAR]= " + ConvertExprToString(varsOptInitOp.getExpr()) + ";\n");
                        }
                    } else {
                        fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + " = " + ConvertExprToString(varsOptInitOp.getExpr()) + ";\n");
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        ArrayList<ID> ids = assignOp.getIdList();
        ArrayList<Expr> exprs = assignOp.getExprList();
        Type type = null;
        for(int i = 0; i < ids.size(); i++) {
            // !symbolTableLocal.lookUpWithKindForInitialize(ids.get(i).getValue(), "Var") &&
            if(symbolTableLocal.lookUpWithKind(ids.get(i).getValue(), "Var").getProperties().equals("toDeclare")){
                // devo anche dichiarare la variabile
                type = symbolTableLocal.returnTypeOfIdWithKind(ids.get(i).getValue(),"Var").getOutType();
                if(type.getName().toLowerCase().equals("string")){
                    type.setName("char");
                    if(exprs.get(i) instanceof ArithOp){
                        type.setName("char*");
                        try {
                            fileWriter.write(type.getName().toLowerCase() + " " + ids.get(i).getValue() + " = ");
                            concatOp(exprs.get(i));
                            fileWriter.write(";\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else{
                        try {
                            fileWriter.write(type.getName().toLowerCase() + " " + ids.get(i).getValue() + "[MAXCHAR] = " + ConvertExprToString(exprs.get(i))+ ";\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    try {
                        fileWriter.write(type.getName().toLowerCase() + " " + ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                // solo assegnamento
                try {
                    if(symbolTableLocal.lookUpWithKind(ids.get(i).getValue(), "Var").getType().getOutType().getName().equals("STRING")){
                        if(exprs.get(i) instanceof ArithOp){
                            fileWriter.write("strcpy(" + ids.get(i).getValue() + ", ");
                            concatOp(exprs.get(i));
                            fileWriter.write(");\n");
                        } else {
                            fileWriter.write("strcpy(" + ids.get(i).getValue() + ", " + ConvertExprToString(exprs.get(i))+ ");\n");
                        }
                    }else {
                        if(symbolTableLocal.lookUpWithKind(ids.get(i).getValue(),"Var").getProperties().equals("Ref")){
                            fileWriter.write("*" + ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
                        } else {
                            fileWriter.write(ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
                        }
                        //fileWriter.write( ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(ID id) {
        return id.getValue().toString();
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        try {
            fileWriter.write("while (");
            fileWriter.write(ConvertExprToString(whileOp.getCondition())+") {\n");
            whileOp.getBody().accept(this);
            fileWriter.write("\n}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(IfThenOp ifThenOp) {
        try {
            fileWriter.write("if (");
            fileWriter.write(ConvertExprToString(ifThenOp.getExpr()));
            fileWriter.write(") {\n");
            ifThenOp.getBodyOp().accept(this);
            fileWriter.write("\n}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
        try {
            fileWriter.write("if (");
            fileWriter.write(ConvertExprToString(ifThenElseOp.getCondizione()));
            fileWriter.write(") {\n");
            ifThenElseOp.getBodyIf().accept(this);
            fileWriter.write("\n} else {\n");
            ifThenElseOp.getBodyElse().accept(this);
            fileWriter.write("}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        return null;
    }

    @Override
    public Object visit(Op op) {
        return null;
    }

    @Override
    public Object visit(ArithOp arithOp) {
        String primoOperando = (String) arithOp.getValueL().accept(this);
        String secondoOperando = (String) arithOp.getValueR().accept(this);
        String tipoLatoSinistro = getTypeOfOp(arithOp.getValueL());
        String tipoLatoDestro = getTypeOfOp(arithOp.getValueR());

        switch (arithOp.getName()){
            case "AddOp": //sistemare qui
                if(tipoLatoSinistro.equals("STRING") || tipoLatoDestro.equals("STRING")){
                    if(tipoLatoSinistro.equals("STRING")){
                        if(tipoLatoDestro.equals("INT") || tipoLatoDestro.equals("BOOL")){
                            return "str_concat(" + primoOperando + ", " + "integer_to_str("+ secondoOperando + "))";  // CONVERTI AD INT IL SECONDO OPERANDO
                        } else if(tipoLatoDestro.equals("DOUBLE")) {
                            return "str_concat(" + primoOperando + ", " + "real_to_str(" +secondoOperando + "))";  // CONVERTI A DOUBLE IL SECONDO OPERANDO
                        } else {
                            // string + string
                            return "str_concat(" + primoOperando + ", " + secondoOperando + ")";
                        }
                    } else if(tipoLatoDestro.equals("STRING")){
                        if(tipoLatoSinistro.equals("INT") || tipoLatoSinistro.equals("BOOL")){
                            return "str_concat(" + "integer_to_str(" +primoOperando + "), " + secondoOperando + ")";  // CONVERTI AD INT IL PRIMO OPERANDO
                        } else if(tipoLatoSinistro.equals("DOUBLE")){
                            return "str_concat(" + "real_to_str(" +primoOperando + "), " + secondoOperando + ")";  // CONVERTI A DOUBLE IL PRIMO OPERANDO
                        } else {
                            return "str_concat(" + primoOperando + ", " + secondoOperando + ")";

                        }
                    }
                }
                if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                    primoOperando = "*" + primoOperando;
                }
                if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                    secondoOperando = "*" + secondoOperando;
                }
                return "("+primoOperando + " + " + secondoOperando +")";
            case "MinusOp":
                if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                    primoOperando = "*" + primoOperando;
                }
                if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                    secondoOperando = "*" + secondoOperando;
                }
                return "("+primoOperando + " - " + secondoOperando +")";
            case "TimesOp":
                if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                    primoOperando = "*" + primoOperando;
                }
                if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                    secondoOperando = "*" + secondoOperando;
                }
                return "("+primoOperando + " * " + secondoOperando +")";
            case "DivOp":
                if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                    primoOperando = "*" + primoOperando;
                }
                if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                    secondoOperando = "*" + secondoOperando;
                }
                return "("+primoOperando + " / " + secondoOperando +")";
        }
        return null;
    }

    @Override
    public Object visit(BoolOp boolOp) {
        String primoOperando = (String) boolOp.getValueL().accept(this);
        String secondoOperando = (String) boolOp.getValueR().accept(this);
        String tipoLatoSinistro = getTypeOfOp(boolOp.getValueL());
        String tipoLatoDestro = getTypeOfOp(boolOp.getValueR());
        if(!(tipoLatoSinistro.equals("STRING") && tipoLatoDestro.equals("STRING"))){
            if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                primoOperando = "*" + primoOperando;
            }
            if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                secondoOperando = "*" + secondoOperando;
            }
        }
        switch (boolOp.getName()){
            case "AndOp":
                return primoOperando + " && " + secondoOperando;
            case "OrOp":
                return primoOperando + " || " + secondoOperando;
        }
        return null;
    }

    @Override
    public Object visit(RelOp relOp) {
        String tipoLatoSinistro = getTypeOfOp(relOp.getValueL());
        String tipoLatoDestro = getTypeOfOp(relOp.getValueR());
        /*
        if(tipoLatoSinistro.equals("STRING") || tipoLatoDestro.equals("STRING")){
            // allora dobbiamo fare strcmp
            if((relOp.getValueL() instanceof Const && !tipoLatoSinistro.equals("STRING"))){
                return "strcmp(\"" + ConvertExprToString(relOp.getValueL()) + "\", " + ConvertExprToString(relOp.getValueR()) + ")";
            }
            if( relOp.getValueR() instanceof Const && !tipoLatoDestro.equals("STRING")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " +"\"" + ConvertExprToString(relOp.getValueR()) +"\"" + ")";
            }
            return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ")";

        } */
        if(tipoLatoSinistro.equals("STRING") && tipoLatoDestro.equals("STRING")){
            if(relOp.getName().equals("EqOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") == 0";
            } else if(relOp.getName().equals("NeOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") != 0";
            } else if (relOp.getName().equals("GtOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") > 0";
            } else if (relOp.getName().equals("GeOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") >= 0";
            } else if (relOp.getName().equals("LtOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") < 0";
            } else if (relOp.getName().equals("LeOp")){
                return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") <= 0";
            }
            //return "strcmp(" + ConvertExprToString(relOp.getValueL()) + ", " + ConvertExprToString(relOp.getValueR()) + ") == 0";
        }
        else {
            String primoOperando = (String) relOp.getValueL().accept(this);
            String secondoOperando = (String) relOp.getValueR().accept(this);
            if(symbolTableLocal.lookUpWithKind(primoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(primoOperando,"Var").getProperties().equals("Ref")){
                primoOperando = "*" + primoOperando;
            }
            if(symbolTableLocal.lookUpWithKind(secondoOperando,"Var")!=null && symbolTableLocal.lookUpWithKind(secondoOperando,"Var").getProperties().equals("Ref")){
                secondoOperando = "*" + secondoOperando;
            }
            switch (relOp.getName()){
                case "GtOp":
                    return primoOperando + " > " + secondoOperando;
                case "GeOp":
                    return primoOperando + " >= " + secondoOperando;
                case "LtOp":
                    return primoOperando + " < " + secondoOperando;
                case "LeOp":
                    return primoOperando + " <= " + secondoOperando;
                case "EqOp":
                    return primoOperando + " == " + secondoOperando;
                case "NeOp":
                    return primoOperando + " != " + secondoOperando;
            }
        }
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        String operando = (String) unaryOp.getValue().accept(this);
        switch (unaryOp.getName()){
            case "MinusOp":
                return "- (" + operando + ")";
            case "NotOp":
                return "! (" + operando + ")";
        }
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        try {
            fileWriter.write("return "+ ConvertExprToString(returnOp.getExpr()) + ";\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        try {
            fileWriter.write("scanf(\"");
            readOp.getVariabili().forEach(var -> {
                Type t = symbolTableLocal.lookUpWithKind(var.getValue(), "Var").getType().getOutType();
                switch (t.getName().toLowerCase()) {
                    case "int":
                    case "bool":
                        try {
                            fileWriter.write("%d" );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "double":
                        try {
                            fileWriter.write("%lf" );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "string":
                        try {
                            fileWriter.write("%s" );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            });
            fileWriter.write("\", ");
            readOp.getVariabili().forEach(var -> {
                Type t = symbolTableLocal.lookUpWithKind(var.getValue(), "Var").getType().getOutType();
                switch (t.getName().toLowerCase()) {
                    case "int":
                    case "bool":
                    case "double":
                        try {
                            if(readOp.getVariabili().get(readOp.getVariabili().size()-1).equals(var))
                                fileWriter.write("&" + var.getValue() + ");\n");
                            else
                                fileWriter.write("&" + var.getValue() + ", ");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "string":
                        try {
                            if(readOp.getVariabili().get(readOp.getVariabili().size()-1).equals(var))
                                fileWriter.write(" " + var.getValue() + ");\n");
                            else
                                fileWriter.write(" " + var.getValue() + ", ");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        try {
            //Inserisco i tipi di variabili che devo stampare
            fileWriter.write("printf(\"");
            writeOp.getExprList().forEach(expr -> {
               if(expr instanceof Const c){
                   stampaPercentPerPrintf(Const.getConstantType(c.getValue()));
               }
               else if(expr instanceof FunCallOpExpr funCallOpExpr) {
                   String tipo = symbolTableLocal.lookUpWithKind(funCallOpExpr.getId().getValue(), "Funz").getType().getOutType().toString().toLowerCase();
                   stampaPercentPerPrintf(tipo);
               } else if(expr instanceof ID id){
                   String tipo = symbolTableLocal.lookUpWithKind(id.getValue(), "Var").getType().getOutType().toString().toLowerCase();
                   stampaPercentPerPrintf(tipo);
               }
               else if(expr instanceof Op op){
                   stampaPercentPerPrintf(getTypeOfOp(op).toLowerCase());
               }
            });
            if(writeOp.getName().equals("OutNewLine")){
                fileWriter.write("\\n");
            }
            fileWriter.write("\", ");

            //inserisco le variabili da stampare
            writeOp.getExprList().forEach(expr -> {
                if(expr instanceof Const c){
                    try {
                        if(writeOp.getExprList().get(writeOp.getExprList().size()-1).equals(expr)) {
                                fileWriter.write(c.getValue().toString());
                        }else
                            fileWriter.write(c.getValue().toString()+", ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(expr instanceof FunCallOpExpr  || expr instanceof ID ) {
                    try {
                        if(writeOp.getExprList().get(writeOp.getExprList().size()-1).equals(expr)) {
                            fileWriter.write(ConvertExprToString(expr));
                        }else
                            fileWriter.write(ConvertExprToString(expr)  + ", ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(expr instanceof Op op){
                    try {
                        if(writeOp.getExprList().get(writeOp.getExprList().size()-1).equals(op)) {
                            fileWriter.write(ConvertExprToString(op));
                        }else
                            fileWriter.write(ConvertExprToString(op)  + ", ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            fileWriter.write(");\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visit(Const constOp) {
        return constOp.getValue().toString();
    }

    @Override
    public Object visit(FunCallOp funCallOp) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {

        Optional<DefDeclOp> quellachemiserve = dichiarazioniFunzioni.stream()
                .filter(decl -> decl.getId().getValue().equals(funCallOpExpr.getId().getValue()))
                .findFirst();

        StringBuilder toReturn = new StringBuilder(funCallOpExpr.getId().getValue() + "(");

        AtomicInteger i = new AtomicInteger(0);
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if (i.get() > 0) {
                    toReturn.append(", "); // Aggiunge una virgola tra gli argomenti
                }
                if (pVarOp.isRef() && !(symbolTableLocal.lookUpWithKind(funCallOpExpr.getParametri().get(i.get()).getName(), "Var").getType().getOutType().getName().equals("STRING"))) {
                    toReturn.append("&").append(funCallOpExpr.getParametri().get(i.get()).accept(this));
                } else {
                    toReturn.append(funCallOpExpr.getParametri().get(i.get()).accept(this));
                }
                i.incrementAndGet();
            });
        });

        toReturn.append(")");

        return toReturn.toString();
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        Optional<DefDeclOp> quellachemiserve = dichiarazioniFunzioni.stream()
                .filter(decl -> decl.getId().getValue().equals(funCallOpStat.getId().getValue()))
                .findFirst();

        StringBuilder toReturn = new StringBuilder(funCallOpStat.getId().getValue() + "(");

        AtomicInteger i = new AtomicInteger(0);
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if (i.get() > 0) {
                    toReturn.append(", "); // Aggiunge una virgola tra gli argomenti
                }
                if (pVarOp.isRef() && !(symbolTableLocal.lookUpWithKind(funCallOpStat.getParametri().get(i.get()).getName(), "Var").getType().getOutType().getName().equals("STRING"))) {
                    if(symbolTableLocal.lookUpWithKind(funCallOpStat.getParametri().get(i.get()).getName(), "Var").getProperties().equals("Ref")){
                        toReturn.append(funCallOpStat.getParametri().get(i.get()).accept(this));
                    } else {
                        toReturn.append("&").append(funCallOpStat.getParametri().get(i.get()).accept(this));
                    }
                } else {
                    toReturn.append(funCallOpStat.getParametri().get(i.get()).accept(this));
                }
                i.incrementAndGet();
            });
        });

        toReturn.append("); \n");

        try {
            fileWriter.write(toReturn.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void addUtils(){
        try {
            String path = "src" + File.separator +"Visitors"+ File.separator +"UtilsCFunc.c";
            String content = Files.readString(Path.of(path));
            fileWriter.write(content);
            fileWriter.write("\n\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String ConvertExprToString(Expr expr) {
        return (String) expr.accept(this);
    }

    private void scriviNelFileFirmaFunzione(DefDeclOp dichiarazione, Type type){
        try {
            String tipo = type.getName().toLowerCase();
            if(tipo.equals("string"))
                tipo = "char*";
            fileWriter.write(tipo + " " + dichiarazione.getId().getValue() + "(");
            AtomicInteger i = new AtomicInteger(0);
            dichiarazione.getParDeclOps().forEach(parDeclOp -> {
                parDeclOp.getPvarOps().forEach(pVarOp -> {
                    if (i.get() > 0) {
                        try {
                            fileWriter.write(", "); // Aggiunge una virgola tra gli argomenti
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        String tipoPar = parDeclOp.getType().getName().toLowerCase().equals("string") ? "char*" : parDeclOp.getType().getName().toLowerCase() ;
                        if(pVarOp.isRef() && !(tipoPar.equals("char*")))
                            fileWriter.write( tipoPar+ "* " + pVarOp.getId().getValue());
                        else
                            fileWriter.write(tipoPar+ " " + pVarOp.getId().getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    i.incrementAndGet();
                });
            });
            fileWriter.write(")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFirmeFunzioni(){
        dichiarazioniFunzioni.forEach(dichiarazione -> {
            try {
                scriviNelFileFirmaFunzione(dichiarazione, dichiarazione.getType());
                fileWriter.write(";\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void stampaPercentPerPrintf(String tipo){
        switch (tipo.toLowerCase()){
            case "string":
                try {
                    fileWriter.write("%s");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "int":
            case "bool":
                try {
                    fileWriter.write("%d");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "double":
                try {
                    fileWriter.write("%f");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private String getTypeOfOp(Expr expr) {
        if(expr instanceof Const c){
            return Const.getConstantType(c.getValue());
        } else if(expr instanceof ID id){
            return symbolTableLocal.lookUpWithKind(id.getValue(), "Var").getType().getOutType().getName();
        } else if(expr instanceof FunCallOpExpr funCallOpExpr){
            return symbolTableLocal.lookUpWithKind(funCallOpExpr.getId().getValue(), "Funz").getType().getOutType().getName();
        } else if(expr instanceof ArithOp arithOp) {
            String tipo1 = getTypeOfOp(arithOp.getValueL());
            String tipo2 = getTypeOfOp(arithOp.getValueR());
            ArrayList<SymbolType> types = new ArrayList<>(
                    List.of(
                            new SymbolType(new Type(tipo1, false)),
                            new SymbolType(new Type(tipo2, false))
                    )
            );
            if(tipo1.equals("STRING") || tipo2.equals("STRING")){
                if(arithOp.getName().equals("AddOp")){
                    return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.CONCATOP).getOutType().getName();
                }
            }
            return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.ARITHOP).getOutType().getName();
        } else if(expr instanceof BoolOp boolOp) {
            String tipo1 = getTypeOfOp(boolOp.getValueL());
            String tipo2 = getTypeOfOp(boolOp.getValueR());
            ArrayList<SymbolType> types = new ArrayList<>(
                    List.of(
                            new SymbolType(new Type(tipo1, false)),
                            new SymbolType(new Type(tipo2, false))
                    )
            );
            return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.BOOLOP).getOutType().getName();
        }else if(expr instanceof RelOp relOp) {

            String tipo1 = getTypeOfOp(relOp.getValueL());
            String tipo2 = getTypeOfOp(relOp.getValueR());
            ArrayList<SymbolType> types = new ArrayList<>(
                    List.of(
                            new SymbolType(new Type(tipo1, false)),
                            new SymbolType(new Type(tipo2, false))
                    )
            );
            return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.RELATIONALOP).getOutType().getName();
        } else if (expr instanceof UnaryOp unaryOp) {
            String tipo = getTypeOfOp(unaryOp.getValue());
            ArrayList<SymbolType> types = new ArrayList<>(
                    List.of(
                            new SymbolType(new Type(tipo,false))
                    )
            );
            return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.UNARYOP).getOutType().getName();
        } else {
            throw new Error("Errore, tipo non riconosciuto");
        }
    }

    private void concatOp(Expr expr) throws IOException {
        System.out.println("siamo in concatOp");
        // Se è un'operazione binaria
        if(expr instanceof ArithOp arithOp){
            if(arithOp.getName().equals("AddOp")){
                fileWriter.write("str_concat(" + ConvertExprToString(arithOp.getValueL()) + "," + ConvertExprToString(arithOp.getValueR()) + ")");
            }
        }
    }

}
