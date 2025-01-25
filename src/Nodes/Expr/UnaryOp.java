package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class UnaryOp extends Expr implements NodeVisitor {
    String name;
    Expr value;

    public UnaryOp(String name, Expr value){
        super(name);
        super.add(value);

        this.value = value;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
