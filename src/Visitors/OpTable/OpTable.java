package Visitors.OpTable;

import java.util.ArrayList;

public class OpTable {
    private String operatore;
    private ArrayList<OpRow> opRowsList;

    public OpTable(String operatore, ArrayList<OpRow> opRowsList) {
        this.operatore = operatore;
        this.opRowsList = opRowsList;
    }

    public String getOperatore() {
        return operatore;
    }

    public void setOperatore(String operatore) {
        this.operatore = operatore;
    }

    public ArrayList<OpRow> getOpRowsList() {
        return opRowsList;
    }

    public void setOpRowsList(ArrayList<OpRow> opRowsList) {
        this.opRowsList = opRowsList;
    }
}
