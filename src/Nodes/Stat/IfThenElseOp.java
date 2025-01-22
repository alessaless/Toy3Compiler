package Nodes.Stat;

import Nodes.BodyOp;
import Nodes.Expr.Expr;

public class IfThenElseOp extends Stat{
    BodyOp bodyIf;
    Expr condizione;
    BodyOp bodyElse;

    public IfThenElseOp(Expr condizione, BodyOp bodyIf, BodyOp bodyElse) {
        super("IfThenElseOp");
        super.add(condizione);
        super.add(bodyIf);
        super.add(bodyElse);

        this.condizione = condizione;
        this.bodyIf = bodyIf;
        this.bodyElse = bodyElse;
    }

    public BodyOp getBodyIf() {
        return bodyIf;
    }

    public void setBodyIf(BodyOp bodyIf) {
        this.bodyIf = bodyIf;
    }

    public Expr getCondizione() {
        return condizione;
    }

    public void setCondizione(Expr condizione) {
        this.condizione = condizione;
    }

    public BodyOp getBodyElse() {
        return bodyElse;
    }

    public void setBodyElse(BodyOp bodyElse) {
        this.bodyElse = bodyElse;
    }
}
