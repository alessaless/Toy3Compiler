package Nodes.Expr;

import Nodes.Decl.VarDeclOp;
import Nodes.Expr.Expr;
import Nodes.Stat.Stat;
import SymbolTable.SymbolTable;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class LetOp extends Expr implements NodeVisitor {
    ArrayList<VarDeclOp> varDeclOp;
    ArrayList<Stat> statements;
    Expr expr;

    SymbolTable symbolTable;

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public LetOp(ArrayList<VarDeclOp> varDeclOp, ArrayList<Stat> statements, Expr expr) {
        super("LetOp");

        varDeclOp.forEach(super::add);
        statements.forEach(super::add);
        super.add(expr);

        this.varDeclOp = varDeclOp;
        this.statements = statements;
        this.expr = expr;
    }

    public ArrayList<VarDeclOp> getVarDeclOp() {
        return varDeclOp;
    }

    public void setVarDeclOp(ArrayList<VarDeclOp> varDeclOp) {
        this.varDeclOp = varDeclOp;
    }

    public ArrayList<Stat> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<Stat> statements) {
        this.statements = statements;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
