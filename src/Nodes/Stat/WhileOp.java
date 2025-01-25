package Nodes.Stat;

import Nodes.BodyOp;
import Nodes.Expr.Expr;
import Visitors.NodeVisitor;
import Visitors.Visitor;

public class WhileOp extends Stat implements NodeVisitor {
    Expr condition;
    BodyOp body;

    public WhileOp(Expr condition, BodyOp body){
        super("WhileOp");
        super.add(condition);
        super.add(body);
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public void setCondition(Expr condition) {
        this.condition = condition;
    }

    public BodyOp getBody() {
        return body;
    }

    public void setBody(BodyOp body) {
        this.body = body;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
