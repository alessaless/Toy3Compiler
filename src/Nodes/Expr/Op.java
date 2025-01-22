package Nodes.Expr;

public class Op extends Expr{
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

}
