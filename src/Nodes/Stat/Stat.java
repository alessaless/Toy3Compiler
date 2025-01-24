package Nodes.Stat;

import Visitors.NodeVisitor;
import Visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class Stat extends DefaultMutableTreeNode implements NodeVisitor {
    String name;

    public Stat(String name) {
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
