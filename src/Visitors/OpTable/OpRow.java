package Visitors.OpTable;

import Nodes.Type;

import java.util.ArrayList;

public class OpRow {
    private ArrayList<Type> operandi = new ArrayList<>();
    private Type risultato;

    public OpRow(ArrayList<Type> operandi, Type risultato) {
        this.operandi = operandi;
        this.risultato = risultato;
    }

    public ArrayList<Type> getOperandi() {
        return operandi;
    }

    public void setOperandi(ArrayList<Type> operandi) {
        this.operandi = operandi;
    }

    public Type getRisultato() {
        return risultato;
    }

    public void setRisultato(Type risultato) {
        this.risultato = risultato;
    }
}
