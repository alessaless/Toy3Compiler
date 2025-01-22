package Nodes.Expr;

public class BoolOp extends Op{
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
}
