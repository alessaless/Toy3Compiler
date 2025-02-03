package SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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

    public SymbolTable getFather() {
        return father;
    }

    public void setFather(SymbolTable father) {
        this.father = father;
    }

    //Controlla che la variabile non sia già stata dichiarata all'interno dello scope corrente
    public boolean probe(String name) {
        return this.symbolRows.stream().anyMatch(symbolRow -> symbolRow.getName().equals(name));
    }

    public boolean probeWithKind(String name, String kind) {
        return this.symbolRows.stream().anyMatch(symbolRow -> symbolRow.getName().equals(name) && symbolRow.getKind().equals(kind));
    }

    public void addID (SymbolRow symbolRow) throws Exception {
        if(!probe(symbolRow.getName())){
            this.symbolRows.add(symbolRow);
        } else {
            if(lookUp(symbolRow.getName()).getKind().equals(symbolRow.getKind())){
                System.out.println("sto confrontando: " + lookUp(symbolRow.getName()).getName() + " con " + symbolRow.getName());
                throw new Error("Variable already declared");
            } else {
                this.symbolRows.add(symbolRow);
            }
        }
    }

    //Controlla che la variabile non sia stata già dichiarata all'interno di tutte le tabelle del programma
    public SymbolRow lookUp(String name) throws Exception {
        if(!probe(name)){
            if(this.father != null){
                return this.father.lookUp(name);
            } else {
                throw new Error("Variable not declared");
            }
        } else {
            return this.symbolRows.stream().filter(symbolRow -> symbolRow.getName().equals(name)).findFirst().get();
        }
    }

    //restituisce true se la variabile è stata dichiarata, false altrimenti
    public boolean lookUpBoolean(String name){
        if(!probe(name)){
            if(this.father != null){
                return this.father.lookUpBoolean(name);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public SymbolRow lookUpWithKind(String name, String kind){
        if(!probeWithKind(name, kind)){
            if(this.father != null){
                return this.father.lookUpWithKind(name, kind);
            } else {
                return null;
            }
        } else {
            return this.symbolRows.stream().filter(symbolRow -> symbolRow.getName().equals(name) && symbolRow.getKind().equals(kind)).findFirst().get();
        }
    }

    //Restituisce il tipo di un dato id
    public SymbolType returnTypeOfId(String name) {
        Optional<SymbolRow> symbolRowOptional = this.getSymbolRows().stream().filter(symbolRow -> symbolRow.getName().equals(name)).findFirst();
        if (symbolRowOptional.isPresent())
            return symbolRowOptional.get().getType();
        else if (this.father != null)
            return this.father.returnTypeOfId(name);
        throw new RuntimeException("L'id " + name + " non è stato dichiarato");
    }

    public SymbolType returnTypeOfIdWithKind(String name, String kind) {
        Optional<SymbolRow> symbolRowOptional = this.getSymbolRows().stream().filter(symbolRow -> symbolRow.getName().equals(name) && symbolRow.getKind().equals(kind)).findFirst();
        if (symbolRowOptional.isPresent())
            return symbolRowOptional.get().getType();
        else if (this.father != null)
            return this.father.returnTypeOfIdWithKind(name, kind);
        throw new RuntimeException("L'id " + name + " non è stato dichiarato");
    }

    public void addTypeToId(String name, SymbolType symbolType) {
        Optional<SymbolRow> symbolRowOptional = this.getSymbolRows().stream().filter(symbolRow -> symbolRow.getName().equals(name)).findFirst();
        if (symbolRowOptional.isPresent()){
            if(symbolRowOptional.get().getKind().equals("Var")){
                symbolRowOptional.get().setType(new SymbolType(null, symbolType.getOutType()));
            } else {
                symbolRowOptional.get().setType(symbolType);
            }
        }

        else if (this.father != null)
            this.father.addTypeToId(name, symbolType);
        else
            throw new RuntimeException("L'id " + name + " non è stato dichiarato pt2");
    }

    //voglio una funzione che mi restituisce la tabella dei simboli dato in input una certa SymbolRow
    public SymbolTable getSymbolTable(SymbolRow symbolRow){
        if(this.symbolRows.contains(symbolRow)){
            return this;
        } else {
            if(this.father != null){
                return this.father.getSymbolTable(symbolRow);
            } else {
                return null;
            }
        }
    }
}
