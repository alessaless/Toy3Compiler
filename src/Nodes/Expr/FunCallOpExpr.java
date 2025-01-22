package Nodes.Expr;

import java.util.ArrayList;

public class FunCallOpExpr extends Expr{
    ID id;
    ArrayList<Expr> parametri;
    public FunCallOpExpr(ID id, ArrayList<Expr> parametri) {
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
}
