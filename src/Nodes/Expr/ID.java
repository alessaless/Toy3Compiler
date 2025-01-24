package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;

public class ID extends Expr implements NodeVisitor {

    String value;
    public ID(String value) {
        super("ID: "+value);
        this.value = value;
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
