package Nodes.Expr;

public class RelOp extends Op{
    Expr valueL;
    Expr valueR;

    public RelOp (Op op, Expr valueL, Expr valueR){
        super("RelOp");
        super.add(op);
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
}
