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

    public static String getConstantType(String str) {
        if (str.matches("-?\\d+")) {
            return "INT";
        } else if (str.matches("-?\\d*\\.\\d+")) {
            return "DOUBLE";
        } else if (str.length() == 3 && str.charAt(0) == '\'' && str.charAt(2) == '\'') {
            return "CHAR";
        } else if(str.equals("true") || str.equals("false")) {
            return "BOOL";
        } else {
            return "STRING";
        }
    }
}
