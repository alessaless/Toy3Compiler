package Nodes.Decl;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class DeclOp extends DefaultMutableTreeNode {
    ArrayList<VarDeclOp> varDeclOps;
    ArrayList<DefDeclOp> defDeclOps;

    public DeclOp(ArrayList<VarDeclOp> varDeclOps, ArrayList<DefDeclOp> defDeclOps){
        super("DeclOp");
        varDeclOps.forEach(super::add);
        defDeclOps.forEach(super::add);

        this.varDeclOps = varDeclOps;
        this.defDeclOps = defDeclOps;
    }

    public ArrayList<VarDeclOp> getVarDeclOps() {
        return varDeclOps;
    }

    public void setVarDeclOps(ArrayList<VarDeclOp> varDeclOps) {
        this.varDeclOps = varDeclOps;
    }

    public ArrayList<DefDeclOp> getDefDeclOps() {
        return defDeclOps;
    }

    public void setDefDeclOps(ArrayList<DefDeclOp> defDeclOps) {
        this.defDeclOps = defDeclOps;
    }
}
