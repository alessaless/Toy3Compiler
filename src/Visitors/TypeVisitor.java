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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TypeVisitor implements Visitor{
    static SymbolTable symbolTableLocal;
    private ArrayList<DefDeclOp> dichiarazioniFunzioni = new ArrayList<>();

    @Override
    public Object visit(ProgramOp programOp) {
        symbolTableLocal = programOp.getSymbolTable();
        programOp.getDeclOp().getDefDeclOps().forEach(defDeclOp -> {
            dichiarazioniFunzioni.add(defDeclOp);
            defDeclOp.accept(this);
        });
        programOp.getDeclOp().getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));


        programOp.getBodyOp().accept(this);
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        symbolTableLocal = bodyOp.getSymbolTable();
        bodyOp.getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        AtomicInteger numeroReturnPerBody = new AtomicInteger(0);
        bodyOp.getStatOps().forEach(stat ->
        {
            stat.accept(this);
        });
        return null;
    }

    @Override
    public Object visit(DefDeclOp defDeclOp) {
        symbolTableLocal = defDeclOp.getSymbolTable();
        defDeclOp.getParDeclOps().forEach(parDeclOp -> parDeclOp.accept(this));
        // controllo che il tipo di ritorno della funzione sia uguale al tipo di ritorno delle returnOp
        defDeclOp.getBodyOp().getStatOps().forEach(stat -> {
            if(stat instanceof ReturnOp){
                if(defDeclOp.getType().isVoid()){
                    throw new Error("Stai cercando di ritornare un tipo dalla funzione "+ defDeclOp.getId().getValue() + " che è di tipo void");
                }
                SymbolType symbolType =  (SymbolType) stat.accept(this);
                if(!symbolType.getOutType().getName().equals(defDeclOp.getType().getName())){
                    throw new Error("Stai cercando di ritornare un tipo diverso da quello dichiarato per la funzione " + defDeclOp.getId().getValue());
                }
            }
        });

        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        if(varDeclOp.getTypeOrConstOp().isConstant()) {
            Const constant = varDeclOp.getTypeOrConstOp().getConstant();
            Type t = new Type(constant.getConstantType(constant.getValue()), false);
            symbolTableLocal.lookUpWithKind(varDeclOp.getVarsOptInitOpList().get(0).getId().getValue(), "Var").getType().setOutType(t);
        }
        varDeclOp.getVarsOptInitOpList().forEach(varsOptInitOp -> {
            if(varsOptInitOp.getExpr() != null){
                SymbolType t = varsOptInitOp.getExpr().accept(this) != null ? (SymbolType) varsOptInitOp.getExpr().accept(this) : null;
                if(!t.getOutType().getName().equals(symbolTableLocal.returnTypeOfIdWithKind(varsOptInitOp.getId().getValue(), "Var").getOutType().getName()) ){
                    throw new Error("Stai assegnando alla variabile "+varsOptInitOp.getId().getValue()+" un tipo diverso da quello dichiarato");
                }
            }
        });
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
                if(!symbolTableLocal.returnTypeOfId(id.getValue()).getOutType().getName().equals(types.get(assignOp.getIdList().indexOf(id)).getOutType().getName())){
                    if(!(symbolTableLocal.returnTypeOfId(id.getValue()).getOutType().getName().equals("DOUBLE") && types.get(assignOp.getIdList().indexOf(id)).getOutType().getName().equals("INT"))){
                        throw new Error("Stai assegnando un tipo diverso da quello dichiarato");
                    }
                }
            }
        });

        return null;
    }

    @Override
    public Object visit(ID id) {
        SymbolType t = symbolTableLocal.returnTypeOfIdWithKind(id.getValue(), "Var");
        return t;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        SymbolType t = (SymbolType)  whileOp.getCondition().accept(this);
        if(!t.getOutType().getName().equals("BOOL")){
            throw new Error("La condizione del while non è di tipo booleano");
        }
        whileOp.getBody().accept(this);
        return null;
    }

    @Override
    public Object visit(IfThenOp ifThenOp) {
        SymbolType t = (SymbolType) ifThenOp.getExpr().accept(this);
        if(!t.getOutType().getName().equals("BOOL")){
            throw new Error("La condizione dell'if non è di tipo booleano");
        }
        ifThenOp.getBodyOp().accept(this);
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
        ifThenElseOp.getCondizione().accept(this);
        SymbolType t = (SymbolType)  ifThenElseOp.getCondizione().accept(this);
        if(!t.getOutType().getName().equals("BOOL")){
            throw new Error("La condizione dell'if non è di tipo booleano");
        }
        ifThenElseOp.getBodyIf().accept(this);
        ifThenElseOp.getBodyElse().accept(this);
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
                throw new Error("Variabile " + ((ID) arithOp.getValueR()).getValue() + " non dichiarata");
            }
            types.add((SymbolType) arithOp.getValueL().accept(this));;
        } else if(arithOp.getValueL() instanceof ArithOp || arithOp.getValueL() instanceof UnaryOp || arithOp.getValueL() instanceof Const || arithOp.getValueL() instanceof BoolOp || arithOp.getValueL() instanceof RelOp || arithOp.getValueL() instanceof FunCallOpExpr){
            types.add((SymbolType) arithOp.getValueL().accept(this));
        }

        if(arithOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) arithOp.getValueR()).getValue())) {
                throw new Error("Variabile " + ((ID) arithOp.getValueR()).getValue() + " non dichiarata");
            }
            types.add((SymbolType) arithOp.getValueR().accept(this));
        } else if (arithOp.getValueR() instanceof ArithOp || arithOp.getValueR() instanceof UnaryOp || arithOp.getValueR() instanceof Const || arithOp.getValueR() instanceof BoolOp || arithOp.getValueR() instanceof RelOp|| arithOp.getValueR() instanceof FunCallOpExpr){
            types.add((SymbolType) arithOp.getValueR().accept(this));
        }

        boolean flag = false;

        for (SymbolType type : types) {
            if ((type.getOutType().getName().equals("STRING") || type.getOutType().getName().equals("CHAR"))) {
                flag = true;
            }
        }

        if(flag){
            if(arithOp.getName().equals("AddOp")){
                return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.CONCATOP);
            } else {
                throw new Error("Operazione non consentita su stringhe");
            }
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
        } else if(boolOp.getValueL() instanceof BoolOp || boolOp.getValueL() instanceof UnaryOp || boolOp.getValueL() instanceof Const || boolOp.getValueL() instanceof ArithOp || boolOp.getValueL() instanceof RelOp || boolOp.getValueL() instanceof FunCallOpExpr){

            types.add((SymbolType) boolOp.getValueL().accept(this));
        }
        if(boolOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) boolOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) boolOp.getValueR()).getValue());
            }
            types.add((SymbolType) boolOp.getValueR().accept(this));
        } else if (boolOp.getValueR() instanceof BoolOp || boolOp.getValueR() instanceof UnaryOp || boolOp.getValueR() instanceof Const || boolOp.getValueR() instanceof ArithOp || boolOp.getValueR() instanceof RelOp || boolOp.getValueR() instanceof FunCallOpExpr){
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
        } else if(relOp.getValueL() instanceof RelOp || relOp.getValueL() instanceof UnaryOp || relOp.getValueL() instanceof Const || relOp.getValueL() instanceof BoolOp || relOp.getValueL() instanceof ArithOp || relOp.getValueL() instanceof FunCallOpExpr){
            types.add((SymbolType) relOp.getValueL().accept(this));
        }
        if(relOp.getValueR() instanceof ID) {
            if (!symbolTableLocal.lookUpBoolean(((ID) relOp.getValueR()).getValue())) {
                throw new Error("Variabile non dichiarata" + ((ID) relOp.getValueR()).getValue());
            }
            types.add((SymbolType) relOp.getValueR().accept(this));
        } else if (relOp.getValueR() instanceof RelOp || relOp.getValueR() instanceof UnaryOp || relOp.getValueR() instanceof Const || relOp.getValueR() instanceof BoolOp || relOp.getValueR() instanceof ArithOp || relOp.getValueR() instanceof FunCallOpExpr){
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
        } else if(unaryOp.getValue() instanceof UnaryOp || unaryOp.getValue() instanceof ArithOp || unaryOp.getValue() instanceof Const || unaryOp.getValue() instanceof BoolOp || unaryOp.getValue() instanceof RelOp || unaryOp.getValue() instanceof FunCallOpExpr){
            types.add((SymbolType) unaryOp.getValue().accept(this));
        }

        return OpTableCombinations.checkCombination(types, OpTableCombinations.EnumOpTable.UNARYOP);
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        SymbolType symbolType = (SymbolType) returnOp.getExpr().accept(this);
        return symbolType;
    }

    @Override
    public Object visit(ReadOp readOp) {
        readOp.getVariabili().forEach(var ->{
            if(symbolTableLocal.lookUpWithKind(var.getValue(),"Funz")!=null){
                throw new Error("Stai provando a leggere in input una funzione " + var.getValue());
            }
        });
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

        // prendo la funzione che sto chiamando
        Optional<DefDeclOp> quellachemiserve = dichiarazioniFunzioni.stream()
                .filter(decl -> decl.getId().getValue().equals(funCallOpExpr.getId().getValue()))
                .findFirst();

        DefDeclOp defDeclOp = quellachemiserve.orElseThrow(() -> new Error("Stai cercando di chiamare una funzione che non esiste"));
        AtomicInteger totParametri = new AtomicInteger();
        // prendo il numero di parametri
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            totParametri.addAndGet(parDeclOp.getPvarOps().size());
        });

        if(totParametri.get() != funCallOpExpr.getParametri().size()){
            throw new Error("Stai passando un numero diverso di parametri alla funzione");
        }


        ArrayList<SymbolType> tipiParametriChePasso = new ArrayList<>();
        funCallOpExpr.getParametri().forEach(expr -> {
            tipiParametriChePasso.add((SymbolType) expr.accept(this));
        });

        AtomicInteger i = new AtomicInteger(0);
        //Foreach che controlla il tipo dei parametri di pardeclop con funcallop
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if(parDeclOp.getType().getName().equals(tipiParametriChePasso.get(i.get()).getOutType().getName())){
                    i.getAndIncrement();
                } else {
                    throw new Error("Stai passando un tipo di parametro sbagliato");
                }
            });
        });

        // foreach che controlla passaggio dei parametri per riferimento
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            AtomicInteger j = new AtomicInteger();
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if(pVarOp.isRef()) {
                    if (funCallOpExpr.getParametri().get(j.get()) instanceof ID) {
                        ID idVar = (ID) funCallOpExpr.getParametri().get(j.get());
                        if (symbolTableLocal.lookUpWithKind(idVar.getValue(), "Var") != null) {
                            j.getAndIncrement();
                        } else {
                            throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                        }
                    } else {
                        throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                    }
                }
            });
        });

        return symbolTableLocal.returnTypeOfIdWithKind(funCallOpExpr.getId().getValue(), "Funz");
    }


    @Override
    public Object visit(FunCallOpStat funCallOpStat) {

        // prendo la funzione che sto chiamando
        Optional<DefDeclOp> quellachemiserve = dichiarazioniFunzioni.stream()
                .filter(decl -> decl.getId().getValue().equals(funCallOpStat.getId().getValue()))
                .findFirst();

        DefDeclOp defDeclOp = quellachemiserve.orElseThrow(() -> new Error("Stai cercando di chiamare una funzione che non esiste"));
        AtomicInteger totParametri = new AtomicInteger();
        // prendo il numero di parametri
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            totParametri.addAndGet(parDeclOp.getPvarOps().size());
        });

        if(totParametri.get() != funCallOpStat.getParametri().size()){
            throw new Error("Stai passando un numero diverso di parametri alla funzione");
        }


        ArrayList<SymbolType> tipiParametriChePasso = new ArrayList<>();
        funCallOpStat.getParametri().forEach(expr -> {
            SymbolType t = (SymbolType) expr.accept(this);
            tipiParametriChePasso.add(t);
        });

        AtomicInteger i = new AtomicInteger(0);
        //Foreach che controlla il tipo dei parametri di pardeclop con funcallop
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if(parDeclOp.getType().getName().equals(tipiParametriChePasso.get(i.get()).getOutType().getName())){
                    i.getAndIncrement();
                } else {
                    throw new Error("Stai passando un tipo di parametro sbagliato");
                }
            });
        });

        // foreach che controlla passaggio dei parametri per riferimento
        quellachemiserve.get().getParDeclOps().forEach(parDeclOp -> {
            AtomicInteger j = new AtomicInteger();
            parDeclOp.getPvarOps().forEach(pVarOp -> {
                if(pVarOp.isRef()) {
                    if (funCallOpStat.getParametri().get(j.get()) instanceof ID) {
                        ID idVar = (ID) funCallOpStat.getParametri().get(j.get());
                        if (symbolTableLocal.lookUpWithKind(idVar.getValue(), "Var") != null) {
                            j.getAndIncrement();
                        } else {
                            throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                        }
                    } else {
                        throw new Error("Stai passando per riferimento qualcosa che non è una variabile");
                    }
                }
            });
        });

        return symbolTableLocal.returnTypeOfIdWithKind(funCallOpStat.getId().getValue(), "Funz");
    }
}
