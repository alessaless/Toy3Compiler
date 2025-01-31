package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.ParDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.*;
import Nodes.ProgramOp;
import Nodes.Stat.*;
import Nodes.Type;
import SymbolTable.*;
import Visitors.OpTable.OpTableCombinations;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TypeVisitor implements Visitor{
    static SymbolTable symbolTableLocal;
    private ArrayList<DefDeclOp> dichiarazioniFunzioni = new ArrayList<>();

    @Override
    public Object visit(ProgramOp programOp) {
        symbolTableLocal = programOp.getSymbolTable();
        programOp.getDeclOp().getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> {
            dichiarazioniFunzioni.add(defDeclOp);
            defDeclOp.accept(this);
        });

        programOp.getBodyOp().accept(this);
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        symbolTableLocal = bodyOp.getSymbolTable();
        bodyOp.getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        bodyOp.getStatOps().forEach(stat -> stat.accept(this));
        return null;
    }

    @Override
    public Object visit(DefDeclOp defDeclOp) {
        symbolTableLocal = defDeclOp.getSymbolTable();
        defDeclOp.getParDeclOps().forEach(parDeclOp -> parDeclOp.accept(this));
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        /*
        varDeclOp.getVarsOptInitOpList().forEach(varsOptInitOp -> {
            Type exprType;
            if(varsOptInitOp.getExpr() != null){
                exprType = varsOptInitOp.getExpr().accept(this);
            }
            if(!symbolTableLocal.returnTypeOfId(varsOptInitOp.getId().getName()).getOutTypeList().get(0).equals(exprType)){
                throw new Error("Type mismatch");
            }
        });

         */
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        if(assignOp.getIdList().size() != assignOp.getExprList().size()){
            throw new Error("Stai assegnando un numero diverso di espressioni ad un numero diverso di ID");
        }
        if (assignOp.getExprList().size() > 1) {
            for (var expr : assignOp.getExprList()) {
                if (expr instanceof FunCallOpExpr) {
                    throw new Error("Non puoi inserire una chiamata a una funzione in un'assegnazione multipla");
                }
            }
        }
        ArrayList<SymbolType> types = new ArrayList<>();
        assignOp.getExprList().forEach(expr -> types.add((SymbolType) expr.accept(this)));
        assignOp.getIdList().forEach(id -> {
            if (symbolTableLocal.returnTypeOfId(id.getValue()).getOutType() == null){
                //devo andare ad aggiungere il tipo alla variabile prendendolo dall'array di types
                symbolTableLocal.addTypeToId(id.getValue(), types.get(assignOp.getIdList().indexOf(id)));
            }else{
                if(!symbolTableLocal.returnTypeOfId(id.getValue()).getOutType().equals(types.get(assignOp.getIdList().indexOf(id)).getOutType())){
                    throw new Error("Type mismatch");
                }
            }
        });

        return null;
    }

    @Override
    public Object visit(ID id) {
        return symbolTableLocal.returnTypeOfId(id.getValue());
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        return null;
    }

    @Override
    public Object visit(IfThenOp ifThenOp) {
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        return null;
    }

    @Override
    public Object visit(Op op) {
        return null;
    }

    @Override
    public Object visit(ArithOp arithOp) {
        ArrayList<SymbolType> types = new ArrayList<>();
        if(arithOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueL()).getValue());
            }
            types.add((SymbolType) arithOp.getValueL().accept(this));;
        } else if(arithOp.getValueL() instanceof ArithOp || arithOp.getValueL() instanceof UnaryOp || arithOp.getValueL() instanceof Const || arithOp.getValueL() instanceof BoolOp || arithOp.getValueL() instanceof RelOp){
            types.add((SymbolType) arithOp.getValueL().accept(this));
        }

        if(arithOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) arithOp.getValueR()).getValue());
            }
            types.add((SymbolType) arithOp.getValueR().accept(this));
        } else if (arithOp.getValueR() instanceof ArithOp || arithOp.getValueR() instanceof UnaryOp || arithOp.getValueR() instanceof Const || arithOp.getValueR() instanceof BoolOp || arithOp.getValueR() instanceof RelOp){
            types.add((SymbolType) arithOp.getValueR().accept(this));
        }
        return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.ARITHOP);
    }

    @Override
    public Object visit(BoolOp boolOp) {
        ArrayList<SymbolType> types = new ArrayList<>();
        if(boolOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueL()).getValue());
            }
            types.add((SymbolType) boolOp.getValueL().accept(this));;
        } else if(boolOp.getValueL() instanceof BoolOp || boolOp.getValueL() instanceof UnaryOp || boolOp.getValueL() instanceof Const || boolOp.getValueL() instanceof ArithOp || boolOp.getValueL() instanceof RelOp){
            types.add((SymbolType) boolOp.getValueL().accept(this));
        }
        if(boolOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueR()).getValue());
            }
            types.add((SymbolType) boolOp.getValueR().accept(this));
        } else if (boolOp.getValueR() instanceof BoolOp || boolOp.getValueR() instanceof UnaryOp || boolOp.getValueR() instanceof Const || boolOp.getValueR() instanceof ArithOp || boolOp.getValueR() instanceof RelOp){
            types.add((SymbolType) boolOp.getValueR().accept(this));
        }
        return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.BOOLOP);
    }

    @Override
    public Object visit(RelOp relOp) {
        ArrayList<SymbolType> types = new ArrayList<>();
        if(relOp.getValueL() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueL()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueL()).getValue());
            }
            types.add((SymbolType) relOp.getValueL().accept(this));;
        } else if(relOp.getValueL() instanceof RelOp || relOp.getValueL() instanceof UnaryOp || relOp.getValueL() instanceof Const || relOp.getValueL() instanceof BoolOp || relOp.getValueL() instanceof ArithOp){
            types.add((SymbolType) relOp.getValueL().accept(this));
        }
        if(relOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueR()).getValue());
            }
            types.add((SymbolType) relOp.getValueR().accept(this));
        } else if (relOp.getValueR() instanceof RelOp || relOp.getValueL() instanceof UnaryOp || relOp.getValueL() instanceof Const || relOp.getValueL() instanceof BoolOp || relOp.getValueL() instanceof ArithOp){
            types.add((SymbolType) relOp.getValueR().accept(this));
        }
        return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.RELATIONALOP);
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        ArrayList<SymbolType> types = new ArrayList<>();
        if(unaryOp.getValue() instanceof ID){
            if(!symbolTableLocal.lookUpBoolean(((ID) unaryOp.getValue()).getValue())){
                throw new Error("Variabile non dichiarata" + ((ID) unaryOp.getValue()).getValue());
            }
            types.add((SymbolType) unaryOp.getValue().accept(this));;
        } else if(unaryOp.getValue() instanceof UnaryOp || unaryOp.getValue() instanceof ArithOp || unaryOp.getValue() instanceof Const || unaryOp.getValue() instanceof BoolOp || unaryOp.getValue() instanceof RelOp){
            types.add((SymbolType) unaryOp.getValue().accept(this));
        }

        return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.UNARYOP);
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        return null;
    }


    @Override
    public Object visit(Const constOp) {
        return new SymbolType(new Type(Const.getConstantType(constOp.getValue()), false));
    }

    @Override
    public Object visit(FunCallOp funCallOp) {
        return symbolTableLocal.returnTypeOfId(funCallOp.getId().getValue());
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {


        DefDeclOp quellachemiserve=dichiarazioniFunzioni.stream().filter(decl -> decl.getId().getValue().equals(funCallOpExpr.getId().getValue())).findFirst().get();
        AtomicInteger totParametri = new AtomicInteger();
        quellachemiserve.getParDeclOps().forEach(parDeclOp -> {
            totParametri.addAndGet(parDeclOp.getPvarOps().size());
        });

        if(totParametri.get() != funCallOpExpr.getParametri().size()){
            throw new Error("Stai passando un numero diverso di parametri alla funzione");
        }

        quellachemiserve.getParDeclOps().forEach(parDeclOp -> {
            AtomicInteger i = new AtomicInteger();
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if(pVarOp.isRef()){
                    System.out.println("Sono un ref");
                    if(funCallOpExpr.getParametri().get(i.get()) instanceof ID) {
                        System.out.println("sono un id");
                        ID idVar = (ID) funCallOpExpr.getParametri().get(i.get());
                        if(symbolTableLocal.lookUpWithKind(idVar.getValue(), "Var")!=null) {
                            System.out.println("mi ha trovato nella tab dei simboli ");
                            i.getAndIncrement();
                        } else {
                            throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                        }
                    } else {
                        throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                    }
                }
            });
        });
        return symbolTableLocal.returnTypeOfId(funCallOpExpr.getId().getValue());
    }


    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        return symbolTableLocal.returnTypeOfId(funCallOpStat.getId().getValue());
    }
}
