package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class RelOp extends Op implements NodeVisitor {
    Expr valueL;
    Expr valueR;

    public RelOp (Op op, Expr valueL, Expr valueR){
        super(op.getName());
        super.add(op);
        super.add(valueL);
        super.add(valueR);

        this.valueL = valueL;
        this.valueR = valueR;
    }

    public RelOp (String name, Expr valueL, Expr valueR){
        super(name);
        //super.add(op);
        super.add(valueL);
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
