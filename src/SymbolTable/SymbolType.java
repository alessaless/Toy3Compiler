package SymbolTable;

import Nodes.Type;

import java.util.ArrayList;

public class SymbolType {
    private ArrayList<Type> inTypeList = new ArrayList<>();
    private Type outType ;


    public SymbolType(ArrayList<Type> inTypeList, Type outType) {
        this.inTypeList = inTypeList;
        this.outType = outType;
    }
    public SymbolType(Type outType) {
        this.outType = outType;
    }

    public ArrayList<Type> getInTypeList() {
        return inTypeList;
    }

    public void setInTypeList(ArrayList<Type> inTypeList) {
        this.inTypeList = inTypeList;
    }

    public void addInTypeList(Type type){
        this.inTypeList.add(type);
    }

    public Type getOutType() {
        return outType;
    }

    public void setOutType(Type outType) {
        this.outType = outType;
    }

    public void addOutType(Type type){
        this.outType = type;
    }

}
