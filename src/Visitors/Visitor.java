package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.Expr;
import Nodes.Expr.ID;
import Nodes.ProgramOp;
import Nodes.Stat.AssignOp;
import Nodes.Stat.Stat;

public interface Visitor {
    Object visit(ProgramOp programOp);
    Object visit(BodyOp bodyOp);
    Object visit(DefDeclOp defDeclOp);
    Object visit(VarDeclOp varDeclOp);
    Object visit(Stat stat);
    Object visit(AssignOp assignOp);
    Object visit(ID id);
    Object visit(Expr expr);
}
