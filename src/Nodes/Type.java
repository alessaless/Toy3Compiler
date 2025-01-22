package Nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class Type extends DefaultMutableTreeNode {
    String name;
    boolean isVoid;

    public Type(String name, boolean isVoid) {
        super(name);
        this.name = name;
        this.isVoid = isVoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }
}
