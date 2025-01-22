package Nodes.Expr;

public class Const extends Expr{
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
}
