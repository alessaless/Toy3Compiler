package SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, ArrayList<SymbolRow>> {
    SymbolTable father;
    String name;
    ArrayList<SymbolRow> symbolRows = new ArrayList<>();

    public SymbolTable(SymbolTable father, String name, ArrayList<SymbolRow> symbolRows) {
        super.put(name, symbolRows);

        this.father = father;
        this.name = name;
        this.symbolRows = symbolRows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<SymbolRow> getSymbolRows() {
        return symbolRows;
    }

    public void setSymbolRows(ArrayList<SymbolRow> symbolRows) {
        this.symbolRows = symbolRows;
    }

    //Controlla che la variabile non sia già stata dichiarata all'interno dello scope corrente
    public boolean probe(String name) {
        return this.symbolRows.stream().anyMatch(symbolRow -> symbolRow.getName().equals(name));
    }

    public void addID (SymbolRow symbolRow) throws Exception {
        if(!probe(symbolRow.getName())){
            this.symbolRows.add(symbolRow);
        } else {
            throw new Exception("Variable already declared");
        }
    }

    //Controlla che la variabile non sia stata già dichiarata all'interno di tutte le tabelle del programma
    public SymbolRow lookUp(String name) throws Exception {
        if(!probe(name)){
            if(this.father != null){
                return this.father.lookUp(name);
            } else {
                throw new Exception("Variable not declared");
            }
        } else {
            return this.symbolRows.stream().filter(symbolRow -> symbolRow.getName().equals(name)).findFirst().get();
        }
    }

}
