package Nodes.Stat;

import java.util.ArrayList;
import Nodes.Expr.Expr;

public class ReturnOp extends Stat{
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

}
