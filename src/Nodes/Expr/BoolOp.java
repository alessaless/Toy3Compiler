package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class BoolOp extends Op implements NodeVisitor {
    Expr valueL;
    Expr valueR;

    public BoolOp (Op op, Expr valueL, Expr valueR){
        super("BoolOp");
        super.add(valueL);
        super.add(op);
        super.add(valueR);

        this.valueL = valueL;
        this.valueR = valueR;
    }

    public Expr getValueL() {
        return valueL;
    }

    public void setValueL(Expr valueL) {
        this.valueL = valueL;
    }

    public Expr getValueR() {
        return valueR;
    }

    public void setValueR(Expr valueR) {
        this.valueR = valueR;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
