package Nodes.Stat;

import Nodes.Expr.ID;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import java.util.ArrayList;

public class ReadOp extends Stat implements NodeVisitor {
    ArrayList<ID> variabili;

    public ReadOp(ArrayList<ID> variabili){
        super("ReadOP");
        variabili.forEach(super::add);

        this.variabili = variabili;
    }

    public ArrayList<ID> getVariabili() {
        return variabili;
    }

    public void setVariabili(ArrayList<ID> variabili) {
        this.variabili = variabili;
    }

    public void addVariabili(ArrayList<ID> variabili){
        variabili.forEach(super::add);

        this.variabili.addAll(variabili);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
