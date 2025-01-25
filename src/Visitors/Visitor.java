package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.ParDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.Expr;
import Nodes.Expr.ID;
import Nodes.ProgramOp;
import Nodes.Stat.*;

public interface Visitor {
    Object visit(ProgramOp programOp);
    Object visit(BodyOp bodyOp);
    Object visit(DefDeclOp defDeclOp);
    Object visit(VarDeclOp varDeclOp);
    Object visit(Stat stat);
    Object visit(AssignOp assignOp);
    Object visit(ID id);
    Object visit(Expr expr);
    Object visit(WhileOp whileOp);
    Object visit(IfThenOp ifThenOp);
    Object visit(IfThenElseOp ifThenElseOp);
    Object visit(ParDeclOp parDeclOp);
}
