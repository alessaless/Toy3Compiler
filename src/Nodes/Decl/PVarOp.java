package Nodes.Decl;

import Nodes.Expr.ID;

import javax.swing.tree.DefaultMutableTreeNode;

public class PVarOp extends DefaultMutableTreeNode {
    ID id;
    boolean ref;

    public PVarOp(ID id, boolean ref) {
        super("PVarOp");
        super.add(id);
        this.id = id;
        this.ref = ref;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }
}
