package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class Op extends Expr implements NodeVisitor {
    String name;

    public Op (String name){
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
