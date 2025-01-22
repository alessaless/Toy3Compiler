package Nodes.Decl;

import Nodes.TypeOrConstOp;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class VarDeclOp extends DefaultMutableTreeNode {
    ArrayList<VarsOptInitOp> varsOptInitOpList;
    TypeOrConstOp typeOrConstOp;

    public VarDeclOp(ArrayList<VarsOptInitOp> varsOptInitOpList, TypeOrConstOp typeOrConstOp){
        super("VarDeclOp");
        varsOptInitOpList.forEach(super::add);
        super.add(typeOrConstOp);

        this.varsOptInitOpList = varsOptInitOpList;
        this.typeOrConstOp = typeOrConstOp;
    }

    public ArrayList<VarsOptInitOp> getVarsOptInitOpList() {
        return varsOptInitOpList;
    }

    public void setVarsOptInitOpList(ArrayList<VarsOptInitOp> varsOptInitOpList) {
        this.varsOptInitOpList = varsOptInitOpList;
    }

    public TypeOrConstOp getTypeOrConstOp() {
        return typeOrConstOp;
    }

    public void setTypeOrConstOp(TypeOrConstOp typeOrConstOp) {
        this.typeOrConstOp = typeOrConstOp;
    }
}
