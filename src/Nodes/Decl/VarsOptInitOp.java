package Nodes.Decl;

import Nodes.Expr.Expr;
import Nodes.Expr.ID;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class VarsOptInitOp extends DefaultMutableTreeNode {
    String name;
    Expr expr;
    ID id;

    // VarsOptInit -> ID PIPE VarsOptInit
    // VarsOptInit -> ID
    public VarsOptInitOp(String name, ID id){
        super(name);
        super.add(id);

        this.name = name;
        this.id = id;
    }

    // VarsOptInit -> ID ASSIGNDECL Expr PIPE VarsOptInit
    // VarsOptInit -> ID ASSIGNDECL Expr
    public VarsOptInitOp(String name, ID id, Expr expr){
        super(name);
        super.add(id);
        super.add(expr);

        this.name = name;
        this.expr = expr;
        this.id = id;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
