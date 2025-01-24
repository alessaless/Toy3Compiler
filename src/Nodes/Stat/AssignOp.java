package Nodes.Stat;

import Nodes.Expr.Expr;
import Nodes.Expr.ID;
import Visitors.NodeVisitor;
import Visitors.Visitor;

import java.nio.file.FileVisitOption;
import java.util.ArrayList;

public class AssignOp extends Stat implements NodeVisitor {
    ArrayList<ID> idList;
    ArrayList<Expr> exprList;

    public AssignOp(ArrayList<ID> idList, ArrayList<Expr> exprList){
        super("AssignOp");
        idList.forEach(super::add);
        exprList.forEach(super::add);

        this.idList = idList;
        this.exprList = exprList;
    }

    public ArrayList<ID> getIdList() {
        return idList;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setIdList(ArrayList<ID> idList) {
        this.idList = idList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }

    public void addIdList(ArrayList<ID> idList) {
        idList.forEach(super::add);

        this.idList.addAll(idList);
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
