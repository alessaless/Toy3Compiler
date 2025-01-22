package Nodes.Expr;

public class ArithOp extends Op{
    Expr valueL;
    Expr valueR;

    public ArithOp (Op op, Expr valueL, Expr valueR){
        super("ArithOp");
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
