package Nodes.Decl;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Nodes.BodyOp;
import Nodes.Expr.ID;
import Nodes.Type;
import SymbolTable.SymbolTable;
import Visitors.NodeVisitor;
import Visitors.Visitor;

//Perchè abbiamo fatto cosi?
public class DefDeclOp extends DefaultMutableTreeNode implements NodeVisitor {
    ID id;
    ArrayList<ParDeclOp> parDeclOps;
    Type type;
    BodyOp bodyOp;
    SymbolTable symbolTable;

    public DefDeclOp(ID id, ArrayList<ParDeclOp> parDeclOps, Type type, BodyOp bodyOp) {
        this(id, parDeclOps, type, bodyOp, type.isVoid() ? "DefDeclOpWithoutType" : "DefDeclOpWithType");
    }

    private DefDeclOp(ID id, ArrayList<ParDeclOp> parDeclOps, Type type, BodyOp bodyOp, String nodeName) {
        super(nodeName);

        super.add(id);
        parDeclOps.forEach(super::add);
        super.add(type);
        super.add(bodyOp);

        this.id = id;
        this.parDeclOps = parDeclOps;
        this.type = type;
        this.bodyOp = bodyOp;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ArrayList<ParDeclOp> getParDeclOps() {
        return parDeclOps;
    }

    public void setParDeclOps(ArrayList<ParDeclOp> parDeclOps) {
        this.parDeclOps = parDeclOps;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }

    public ArrayList<Type> getParametersTypes() {
        return this.getParDeclOps().stream()
                .map(ParDeclOp::getType)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
