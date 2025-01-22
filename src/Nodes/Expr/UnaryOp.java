package Nodes.Expr;

public class UnaryOp extends Expr{
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
}
