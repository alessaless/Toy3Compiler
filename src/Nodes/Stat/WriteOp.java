package Nodes.Stat;

import Nodes.Expr.Expr;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import java.util.ArrayList;

public class WriteOp extends Stat implements NodeVisitor {

    String name;
    ArrayList<Expr> exprList;


    // newline gestito con string name diverso.
    public WriteOp(String name, ArrayList<Expr> exprList){
        super(name);
        exprList.forEach(super::add);

        this.exprList = exprList;

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }

    public void addExprList(ArrayList<Expr> exprList) {
        exprList.forEach(super::add);
        this.exprList.addAll(exprList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
