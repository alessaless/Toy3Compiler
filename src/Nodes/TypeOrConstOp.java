package Nodes;

import Nodes.Expr.Const;

import javax.swing.tree.DefaultMutableTreeNode;

public class TypeOrConstOp extends DefaultMutableTreeNode {
    Type type;
    Const constant;

    public TypeOrConstOp(Type type){
        super("Type");
        super.add(type);
        this.type = type;
    }

    public TypeOrConstOp(Const constant){
        super("Const");
        super.add(constant);
        this.constant = constant;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Const getConstant() {
        return constant;
    }

    public void setConstant(Const constant) {
        this.constant = constant;
    }
}
