package Nodes.Stat;

import Nodes.BodyOp;
import Nodes.Expr.Expr;

public class IfThenOp extends Stat{
    Expr expr;
    BodyOp bodyOp;

    public IfThenOp(Expr expr, BodyOp bodyOp){
        super("IfThenOp");
        super.add(expr);
        super.add(bodyOp);

        this.bodyOp = bodyOp;
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }
}
