package Nodes;

import Nodes.Decl.DeclOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.VarDeclOp;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProgramOp extends DefaultMutableTreeNode {
    DeclOp declOp;
    BodyOp bodyOp;

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
}
