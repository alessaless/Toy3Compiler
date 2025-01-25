package Nodes.Stat;

import Nodes.BodyOp;
import Nodes.Expr.Expr;
import Visitors.NodeVisitor;
import Visitors.Visitor;

public class IfThenOp extends Stat implements NodeVisitor {
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

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
