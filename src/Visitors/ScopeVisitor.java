package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.Expr;
import Nodes.Expr.ID;
import Nodes.ProgramOp;
import Nodes.Stat.*;
import Nodes.Type;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolRow;
import SymbolTable.SymbolType;

import java.util.ArrayList;

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
                            new SymbolType(varType, null),
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
                    SymbolRow row = new SymbolRow(
                            decl.getId().getName(),
                            "Var",
                            new SymbolType(varType, null),
                            "");
                    try {
                        symbolTable.addID(row);
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
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        if(stat instanceof AssignOp){
            System.out.println("\n\n\nSIAMO IN ASSIGNOP\n\n\n");
            ((AssignOp) stat).accept(this);
        } else if (stat instanceof IfThenElseOp) {
            ((IfThenElseOp) stat).getBodyIf().accept(this);
            ((IfThenElseOp) stat).getBodyElse().accept(this);
        } else if (stat instanceof IfThenOp) {
            ((IfThenOp) stat).getBodyOp().accept(this);
        } else if (stat instanceof WhileOp) {
            ((WhileOp) stat).getBody().accept(this);
        }
        return null;
    }

    //Inferenza di tipo
    @Override
    public Object visit(AssignOp assignOp) {
        /*
        assignOp.getIdList().forEach(id -> {
            // Process each ID
            SymbolRow row = new SymbolRow(
                    id.getName(),
                    "Var",
                    new SymbolType(new ArrayList<>(), null),
                    "");
            try {
                symbolTableLocal.addID(row);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assignOp.getExprList().forEach(expr -> {
            // Process each Expr
            expr.accept(this);
        });
*/
        return null;
    }

    @Override
    public Object visit(ID id) {
        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }


}

