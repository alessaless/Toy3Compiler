package Nodes.Stat;

import Nodes.Expr.Expr;
import Nodes.Expr.ID;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import java.util.ArrayList;

public class FunCallOpStat extends Stat implements NodeVisitor {
    ID id;
    ArrayList<Expr> parametri;
    public FunCallOpStat(ID id, ArrayList<Expr> parametri) {
        super("FunCallOp");
        super.add(id);

        parametri.forEach(super::add); // come fare super.add(per ogni parametro expr)
        this.id = id;
        this.parametri = parametri;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ArrayList<Expr> getParametri() {
        return parametri;
    }

    public void setParametri(ArrayList<Expr> parametri) {
        this.parametri = parametri;
    }

    public void addExprList(ArrayList<Expr> parametri) {
        parametri.forEach(super::add);
        this.parametri.addAll(parametri);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
