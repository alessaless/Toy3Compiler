package Visitors.OpTable;

import Nodes.Type;
import SymbolTable.SymbolType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OpTableCombinations {

    public enum EnumOpTable {
        UMINUSOP,
        NOT,
        ARITHOP,
        CONCATOP,
        LOGICOP,
        RELATIONALOP,
        COMPAREOP,
        DIVOP
    }

    // operatori unari
    private static final OpTable UMINUSOP = new OpTable(
            "Uminus",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false))),
                                    new Type("int", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false))),
                                    new Type("double", false)
                            )
                    )
            )
    );

    private static final OpTable NOT = new OpTable(
            "Not",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("bool", false))),
                                    new Type("bool", false)
                            )
                    )
            )
    );


    // operatori binari
    private static final OpTable ARITHOP = new OpTable(
            "ArithOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("int", false))),
                                    new Type("int", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("double", false))),
                                    new Type("double", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("string", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("double", false))),
                                    new Type("double", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("int", false))),
                                    new Type("double", false)
                            )
                    )
            )
    );

    private static final OpTable CONCATOP = new OpTable(
            "ConcatOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("string", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("int", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("string", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("double", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("string", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("bool", false))),
                                    new Type("string", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("bool", false), new Type("string", false))),
                                    new Type("string", false)
                            )
                    )
            )
    );

    private static final OpTable LOGICOP = new OpTable(
            "LogicOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("bool", false), new Type("bool", false))),
                                    new Type("bool", false)
                            )
                    )
            )
    );

    private static final OpTable RELATIONALOP = new OpTable(
            "RelationalOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("int", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("double", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("double", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("int", false))),
                                    new Type("bool", false)
                            )
                    )
            )
    );

    private static final OpTable COMPAREOP = new OpTable(
            "CompareOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("int", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("double", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("double", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("double", false), new Type("int", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("bool", false), new Type("bool", false))),
                                    new Type("bool", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("string", false), new Type("string", false))),
                                    new Type("bool", false)
                            )
                    )
            )
    );

    private static final OpTable DIVOP = new OpTable(
            "DivOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("int", false), new Type("int", false))),
                                    new Type("double", false)
                            )
                    )
            )
    );

    //TODO: Gestire i tipi di ritorno di una funzione, i tipi di ritorno delle variabili singole, tutti in outTypeList
    public static SymbolType checkCombination(ArrayList<SymbolType> symbolTypeList, EnumOpTable enumOpTable) {
        try {
            //Prendo l'oggetto dato l'enum fornito in input
            OpTable opTable = (OpTable) OpTableCombinations.class.getDeclaredField(enumOpTable.name()).get(OpTableCombinations.class);
            //Controllo il match dei tipi forniti con quelli presenti in tabella
            for (OpRow opRow : opTable.getOpRowsList()) {
                boolean flag = true;
                //Controllo che i tipi forniti siano compatibili con quelli presenti in tabella
                //Inserisco all'interno di Iterator tutti i tipi di ritorno presenti in tabella
                Iterator<Type> itType = symbolTypeList.stream().flatMap(symbolType -> symbolType.getOutTypeList().stream()).iterator();
                Iterator<Type> itTypeTable = opRow.getOperandi().iterator();

                while (itType.hasNext() && itTypeTable.hasNext())
                    if (!itType.next().getName().equals(itTypeTable.next().getName())) {
                        flag = false;
                    }

                if (itType.hasNext() || itTypeTable.hasNext())
                    throw new RuntimeException("Utilizzati più operandi di quelli consentiti");

                //Restituisce il tipo fornito dal match
                if (flag) {
                    return new SymbolType(new ArrayList<>(List.of(opRow.getRisultato())));
                }
            }
            throw new RuntimeException("I tipi "+symbolTypeList.stream().flatMap(symbolType -> symbolType.getOutTypeList().stream()).map(Type::getName).toList() + " non sono supportati");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
