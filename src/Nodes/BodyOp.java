package Nodes;

import Nodes.Decl.VarDeclOp;
import Nodes.Stat.Stat;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class BodyOp extends DefaultMutableTreeNode {
    ArrayList<VarDeclOp> varDeclOps;
    ArrayList<Stat> statOps;

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
}
