package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class Const extends Expr implements NodeVisitor {
    String name, value;

    public Const(String name, String value){
        super(name+" : "+value);

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
