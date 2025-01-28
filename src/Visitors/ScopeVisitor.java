package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.ParDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.*;
import Nodes.ProgramOp;
import Nodes.Stat.*;
import Nodes.Type;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolRow;
import SymbolTable.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class ScopeVisitor implements Visitor{
    static SymbolTable symbolTableLocal;
    @Override
    public Object visit(ProgramOp programOp) {
        SymbolTable symbolTable = new SymbolTable(null, "ProgramOpSymbolTable", new ArrayList<SymbolRow>());
        if(programOp.getSymbolTable() == null){
            programOp.setSymbolTable(symbolTable);
        }
        symbolTableLocal = symbolTable;

        if (programOp.getDeclOp().getDefDeclOps() != null) {
            programOp.getDeclOp().getDefDeclOps().forEach(decl -> {
                ArrayList<Type> outTypeList = new ArrayList<>();
                outTypeList.add(decl.getType());

                SymbolRow row = new SymbolRow(
                        decl.getId().getName(),
                        "Funz",
                        new SymbolType(decl.getParametersTypes(), outTypeList),
                        "");
                try {
                    symbolTable.addID(row);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if(programOp.getDeclOp().getVarDeclOps() != null) {
            programOp.getDeclOp().getVarDeclOps().forEach(varDeclOp -> {
                Type t = varDeclOp.getTypeOrConstOp().getType();
                ArrayList<Type> varType = new ArrayList<>();
                varType.add(t);
                varDeclOp.getVarsOptInitOpList().forEach(decl -> {
                    SymbolRow row = new SymbolRow(
                            decl.getId().getName(),
                            "Var",
                            new SymbolType(null, varType),
                            "");
                    try {
                        symbolTable.addID(row);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

        programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> defDeclOp.accept(this));
        programOp.getDeclOp().getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        programOp.getBodyOp().accept(this);
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        SymbolTable symbolTableFather = symbolTableLocal;
        SymbolTable symbolTable = new SymbolTable(symbolTableLocal, "BodyOpSymbolTable", new ArrayList<SymbolRow>());

        if (bodyOp.getSymbolTable() == null) {
            bodyOp.setSymbolTable(symbolTable);
        }
        symbolTableLocal = bodyOp.getSymbolTable();

        if (bodyOp.getSymbolTable() != null) {
            bodyOp.getVarDeclOps().forEach(varDeclOp -> {
                Type t = varDeclOp.getTypeOrConstOp().getType();
                ArrayList<Type> varType = new ArrayList<>();
                varType.add(t);
                varDeclOp.getVarsOptInitOpList().forEach(decl -> {
                    try {
                        SymbolRow row = new SymbolRow(
                                decl.getId().getName(),
                                "Var",
                                new SymbolType(null, varType),
                                "");
                        try {
                            symbolTable.addID(row);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

        bodyOp.getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        bodyOp.getStatOps().forEach(stat -> stat.accept(this));
        symbolTableLocal = symbolTableFather;
        return null;
    }

    @Override
    public Object visit(DefDeclOp defDeclOp) {
        SymbolTable symbolTableFather = symbolTableLocal;
        SymbolTable symbolTable = new SymbolTable(symbolTableLocal, "DefDeclOpSymbolTable", new ArrayList<SymbolRow>());

        if (defDeclOp.getSymbolTable() == null) {
            defDeclOp.setSymbolTable(symbolTable);
        }

        symbolTableLocal = defDeclOp.getSymbolTable();

        defDeclOp.getParDeclOps().forEach(parDeclOp -> parDeclOp.accept(this));

        defDeclOp.getBodyOp().getVarDeclOps().forEach(varDeclOp -> {
            Type t = varDeclOp.getTypeOrConstOp().getType();
            ArrayList<Type> varType = new ArrayList<>();
            varType.add(t);
            varDeclOp.getVarsOptInitOpList().forEach(decl -> {
                if(decl.getExpr() != null){
                    decl.getExpr().accept(this);
                }
                try {
                    SymbolRow row = new SymbolRow(
                            decl.getId().getName(),
                            "Var",
                            new SymbolType(null, varType),
                            "");
                    try {
                        symbolTable.addID(row);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        defDeclOp.getBodyOp().getStatOps().forEach(stat -> stat.accept(this));

        symbolTableLocal = symbolTableFather;
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        ArrayList<Type> type = new ArrayList<>();
        type.add(parDeclOp.getType());
        parDeclOp.getPvarOps().forEach(pVarOp -> {
            try {
                SymbolRow row = new SymbolRow(
                        pVarOp.getId().getName(),
                        "Var",
                        new SymbolType(null, type),
                        "");
                try {
                    symbolTableLocal.addID(row);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }

    @Override
    public Object visit(Op op) {
        return null;
    }

    @Override
    public Object visit(ArithOp arithOp) {
        if(arithOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueL()).getName())){
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueL()).getName());
            }
        } else if(arithOp.getValueL() instanceof ArithOp){
            arithOp.getValueL().accept(this);
        }
        if(arithOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueR()).getName())) {
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueR()).getName());
            }
        } else if (arithOp.getValueR() instanceof ArithOp){
            arithOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(BoolOp boolOp) {
        if(boolOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueL()).getName())){
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueL()).getName());
            }
        } else if(boolOp.getValueL() instanceof BoolOp){
            boolOp.getValueL().accept(this);
        }
        if(boolOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueR()).getName())) {
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueR()).getName());
            }
        } else if (boolOp.getValueR() instanceof BoolOp){
            boolOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(RelOp relOp) {
        if(relOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueL()).getName())){
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueL()).getName());
            }
        } else if(relOp.getValueL() instanceof RelOp){
            relOp.getValueL().accept(this);
        }
        if(relOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueR()).getName())) {
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueR()).getName());
            }
        } else if (relOp.getValueR() instanceof RelOp){
            relOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        if(unaryOp.getValue() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) unaryOp.getValue()).getName())){
                throw new Error("Variabile non dichiarata" + ((ID) unaryOp.getValue()).getName());
            }
        } else if (unaryOp.getValue() instanceof UnaryOp){
            unaryOp.getValue().accept(this);
            }
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        returnOp.getExpr().accept(this);
        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        readOp.getVariabili().forEach(id -> {
            id.accept(this);
        });
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        writeOp.getExprList().forEach(expr -> {
            expr.accept(this);
        });
        return null;
    }
    @Override
    public Object visit(VarDeclOp varDeclOp) {
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(Const constOp) {
        return new Type(Const.getConstantType(constOp.getValue()), false);
    }

    @Override
    public Object visit(FunCallOp funCallOp) {
        if(!symbolTableLocal.lookUpBoolean(funCallOp.getId().getName())){
            throw new Error("Funzione non dichiarata" + funCallOp.getId().getName());
        }
        return symbolTableLocal.returnTypeOfId(funCallOp.getId().getName()).getOutTypeList().get(0);
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        if(!symbolTableLocal.lookUpBoolean(funCallOpExpr.getId().getName())){
            throw new Error("Funzione non dichiarata" + funCallOpExpr.getId().getName());
        }
        return symbolTableLocal.returnTypeOfId(funCallOpExpr.getId().getName()).getOutTypeList().get(0);
    }

    //TODO:Dobbiamo fare inferenza di tipo
    @Override
    public Object visit(AssignOp assignOp) {
        ArrayList<Type> t = new ArrayList<>();
        assignOp.getExprList().forEach(expr -> {
            t.add((Type) expr.accept(this));
            System.out.println("\nSTAMPO TYPE "+t);
        });
        int i = 0;
        assignOp.getIdList().forEach(id -> {
            if(!symbolTableLocal.lookUpBoolean(id.getName())){
                SymbolRow row = new SymbolRow(
                        id.getName(),
                        "Var",
                        new SymbolType(null, new ArrayList<>(List.of(new Type(t.get(i).getName(), t.get(i).isVoid())))),
                        "");
                try {
                    symbolTableLocal.addID(row);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // Process each ID
        });
        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(ID id) {
        if(!symbolTableLocal.lookUpBoolean(id.getName())){
            throw new Error("Variabile non dichiarata" + id.getName());
        }
        return symbolTableLocal.returnTypeOfId(id.getName()).getOutTypeList().get(0);
    }


    @Override
    public Object visit(WhileOp whileOp) {
        /*
        SymbolTable symbolTableFather = symbolTableLocal;
        SymbolTable symbolTable = new SymbolTable(symbolTableLocal, "WhileOpSymbolTable", new ArrayList<SymbolRow>());

        if (whileOp.getSymbolTable() == null) {
            whileOp.setSymbolTable(symbolTable);
        }

        symbolTableLocal = whileOp.getSymbolTable();

        //whileOp.getCondition().accept(this);

         */
        whileOp.getCondition().accept(this);
        whileOp.getBody().accept(this);

        //symbolTableLocal = symbolTableFather;
        return null;
    }

    @Override
    public Object visit(IfThenOp ifThenOp) {
        ifThenOp.getExpr().accept(this);
        ifThenOp.getBodyOp().accept(this);
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
        ifThenElseOp.getCondizione().accept(this);
        ifThenElseOp.getBodyIf().accept(this);
        ifThenElseOp.getBodyElse().accept(this);
        return null;
    }
}

