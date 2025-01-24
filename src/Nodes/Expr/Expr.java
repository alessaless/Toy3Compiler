package Nodes.Expr;

import Visitors.NodeVisitor;
import Visitors.Visitor;
import org.w3c.dom.Node;

import javax.swing.tree.DefaultMutableTreeNode;

public class Expr extends DefaultMutableTreeNode implements NodeVisitor {
    String name;
    public Expr(String name) {
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
