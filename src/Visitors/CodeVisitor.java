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
import java_cup.runtime.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

            String path  = "test_files" + File.separator + "c_out" + File.separator;

            if(!Files.exists(Path.of(path)))
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
            if(type.equals("string"))
                type = "char*";
                try {
                    if(varsOptInitOp.getExpr() == null) {
                        if(varDeclOp.getTypeOrConstOp().isConstant()){
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + " = " + varDeclOp.getTypeOrConstOp().getConstant().accept(this) + ";\n");
                        } else {
                            fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + "; \n");
                        }
                    }
                    else{
                        fileWriter.write(type + " " + varsOptInitOp.getId().getValue() + " = " + ConvertExprToString(varsOptInitOp.getExpr()) + ";\n");
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
            if(!symbolTableLocal.lookUpWithKindForInitialize(ids.get(i).getValue(), "Var")){
                // devo anche dichiarare la variabile
                type = symbolTableLocal.returnTypeOfIdWithKind(ids.get(i).getValue(),"Var").getOutType();
                try {
                    if(type.getName().toLowerCase().equals("string"))
                        type.setName("char*");
                    fileWriter.write(type.getName().toLowerCase() + " " + ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // solo assegnamento
                try {
                    fileWriter.write( ids.get(i).getValue() + " = " + ConvertExprToString(exprs.get(i))+ ";\n");
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
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
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
        switch (arithOp.getName()){
            case "AddOp":
                return primoOperando + " + " + secondoOperando;
            case "MinOp":
                return primoOperando + " - " + secondoOperando;
            case "TimesOp":
                return primoOperando + " * " + secondoOperando;
            case "DivOp":
                return primoOperando + " / " + secondoOperando;
        }
        return null;
    }

    @Override
    public Object visit(BoolOp boolOp) {
        String primoOperando = (String) boolOp.getValueL().accept(this);
        String secondoOperando = (String) boolOp.getValueR().accept(this);

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
        String primoOperando = (String) relOp.getValueL().accept(this);
        String secondoOperando = (String) relOp.getValueR().accept(this);

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
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        String operando = (String) unaryOp.getValue().accept(this);
        switch (unaryOp.getName()){
            case "MinusOp":
                return "- " + operando;
            case "NotOp":
                return "! " + operando;
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
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
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
                if (pVarOp.isRef()) {
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
                if (pVarOp.isRef()) {
                    toReturn.append("&").append(funCallOpStat.getParametri().get(i.get()).accept(this));
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
                        if(pVarOp.isRef())
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

}
