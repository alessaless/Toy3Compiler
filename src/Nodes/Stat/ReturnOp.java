package Nodes.Stat;

import java.util.ArrayList;
import Nodes.Expr.Expr;
import Visitors.NodeVisitor;
import Visitors.Visitor;

public class ReturnOp extends Stat implements NodeVisitor {
    Expr expr;

    public ReturnOp(Expr expr){
        super("ReturnOp");
        super.add(expr);

        this.expr = expr;
    }

    public Expr getExprList() {
        return expr;
    }

    public void setExprList(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
