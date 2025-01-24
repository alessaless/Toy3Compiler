package Nodes;

import Nodes.Decl.VarDeclOp;
import Nodes.Stat.Stat;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolType;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class BodyOp extends DefaultMutableTreeNode implements NodeVisitor {
    ArrayList<VarDeclOp> varDeclOps;
    ArrayList<Stat> statOps;
    SymbolTable symbolTable;

    public BodyOp(ArrayList<VarDeclOp> varDeclOps, ArrayList<Stat> statOps) {
        super("BodyOp");
        varDeclOps.forEach(super::add);
        statOps.forEach(super::add);

        this.varDeclOps = varDeclOps;
        this.statOps = statOps;
    }

    public ArrayList<VarDeclOp> getVarDeclOps() {
        return varDeclOps;
    }

    public void setVarDeclOps(ArrayList<VarDeclOp> varDeclOps) {
        this.varDeclOps = varDeclOps;
    }

    public ArrayList<Stat> getStatOps() {
        return statOps;
    }

    public void setStatOps(ArrayList<Stat> statOps) {
        this.statOps = statOps;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
