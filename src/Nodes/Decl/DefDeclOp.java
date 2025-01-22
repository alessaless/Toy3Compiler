package Nodes.Decl;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

import Nodes.BodyOp;
import Nodes.Expr.ID;
import Nodes.Type;

//Perchè abbiamo fatto cosi?
public class DefDeclOp extends DefaultMutableTreeNode {
    ID id;
    ArrayList<ParDeclOp> parDeclOps;
    Type type;
    BodyOp bodyOp;

    public DefDeclOp(ID id, ArrayList<ParDeclOp> parDeclOps, Type type, BodyOp bodyOp) {
        this(id, parDeclOps, type, bodyOp, type.isVoid() ? "DefDeclOpWithoutType" : "DefDeclOpWithType");
    }

    private DefDeclOp(ID id, ArrayList<ParDeclOp> parDeclOps, Type type, BodyOp bodyOp, String nodeName) {
        super(nodeName);

        super.add(id);
        parDeclOps.forEach(super::add);
        super.add(type);
        super.add(bodyOp);

        this.id = id;
        this.parDeclOps = parDeclOps;
        this.type = type;
        this.bodyOp = bodyOp;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ArrayList<ParDeclOp> getParDeclOps() {
        return parDeclOps;
    }

    public void setParDeclOps(ArrayList<ParDeclOp> parDeclOps) {
        this.parDeclOps = parDeclOps;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }
}
