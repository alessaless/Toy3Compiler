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

                SymbolRow row = new SymbolRow(
                        decl.getId().getValue(),
                        "Funz",
                        new SymbolType(decl.getParametersTypes(), decl.getType()),
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
                varDeclOp.accept(this);
            });
        }

        programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> defDeclOp.accept(this));
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
                varDeclOp.accept(this);
            });
        }

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
            varDeclOp.getVarsOptInitOpList().forEach(decl -> {
                if(decl.getExpr() != null){
                    decl.getExpr().accept(this);
                }
                try {
                    SymbolRow row = new SymbolRow(
                            decl.getId().getValue(),
                            "Var",
                            new SymbolType(null, t),
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
        parDeclOp.getPvarOps().forEach(pVarOp -> {
            System.out.println("STAMPA "+pVarOp.isRef());
            try {
                // in caso un parametro sia passato per riferimeto aggiungo la stringa "Ref" al campo properties
                String isRef = pVarOp.isRef() ? "Ref" : "";
                SymbolRow row = new SymbolRow(
                        pVarOp.getId().getValue(),
                        "Var",
                        new SymbolType(null, parDeclOp.getType()),
                        isRef);
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
            if(!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueL()).getValue());
            }
        } else if(arithOp.getValueL() instanceof ArithOp){
            arithOp.getValueL().accept(this);
        }
        if(arithOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueR()).getValue());
            }
        } else if (arithOp.getValueR() instanceof ArithOp){
            arithOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(BoolOp boolOp) {
        if(boolOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueL()).getValue());
            }
        } else if(boolOp.getValueL() instanceof BoolOp){
            boolOp.getValueL().accept(this);
        }
        if(boolOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueR()).getValue());
            }
        } else if (boolOp.getValueR() instanceof BoolOp){
            boolOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(RelOp relOp) {
        if(relOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueL()).getValue());
            }
        } else if(relOp.getValueL() instanceof RelOp){
            relOp.getValueL().accept(this);
        }
        if(relOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueR()).getValue());
            }
        } else if (relOp.getValueR() instanceof RelOp){
            relOp.getValueR().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        if(unaryOp.getValue() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) unaryOp.getValue()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) unaryOp.getValue()).getValue());
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
        if(varDeclOp.getVarsOptInitOpList().size()>1){
            if(varDeclOp.getTypeOrConstOp().isConstant()){
                throw new Error("Stai assegnando una costante a più variabili");
            }
        }

        Type t = varDeclOp.getTypeOrConstOp().getType();
        varDeclOp.getVarsOptInitOpList().forEach(decl -> {
            if(decl.getExpr() != null){
                if(varDeclOp.getTypeOrConstOp().isConstant()){
                    throw new Error("Non puoi assegnare una variabile dichiarata tramite il tipo di una costante");
                }
            }
            try {
                SymbolRow row = new SymbolRow(
                        decl.getId().getValue(),
                        "Var",
                        new SymbolType(null, t),
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
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(Const constOp) {
        return new Type(Const.getConstantType(constOp.getValue()), false);
    }

    @Override
    public Object visit(FunCallOp funCallOp) {
        if(!symbolTableLocal.lookUpBoolean(funCallOp.getId().getValue())){
            throw new Error("Funzione non dichiarata" + funCallOp.getId().getValue());
        }
        return symbolTableLocal.returnTypeOfId(funCallOp.getId().getValue()).getOutType();
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        if(!symbolTableLocal.lookUpBoolean(funCallOpExpr.getId().getValue())){
            throw new Error("Funzione non dichiarata" + funCallOpExpr.getId().getValue());
        }
        return symbolTableLocal.returnTypeOfId(funCallOpExpr.getId().getValue()).getOutType();
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        assignOp.getIdList().forEach(id -> {
            System.out.println("ID: "+id.getValue());
            if(!symbolTableLocal.lookUpBoolean(id.getValue())){
                SymbolRow row = new SymbolRow(
                        id.getValue(),
                        "Var",
                        new SymbolType(null, null),
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
        if(!symbolTableLocal.lookUpBoolean(id.getValue())){
            throw new Error("Variabile non dichiarata" + id.getValue());
        }
        return symbolTableLocal.returnTypeOfId(id.getValue()).getOutType();
    }


    @Override
    public Object visit(WhileOp whileOp) {
        whileOp.getCondition().accept(this);
        whileOp.getBody().accept(this);
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

