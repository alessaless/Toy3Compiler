package Nodes;

import Nodes.Decl.DeclOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.VarDeclOp;
import SymbolTable.SymbolTable;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProgramOp extends DefaultMutableTreeNode implements NodeVisitor {
    DeclOp declOp;
    BodyOp bodyOp;
    SymbolTable symbolTable;

    public ProgramOp(DeclOp declOp, BodyOp bodyOp){
        super("ProgramOp");
        super.add(declOp);
        super.add(bodyOp);

        this.declOp = declOp;
        this.bodyOp = bodyOp;
    }

    public DeclOp getDeclOp() {
        return declOp;
    }

    public void setDeclOp(DeclOp declOp) {
        this.declOp = declOp;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
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
