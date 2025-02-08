package Visitors.OpTable;

import Nodes.Type;
import SymbolTable.SymbolType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OpTableCombinations {

    public enum EnumOpTable {
        UNARYOP,
        ARITHOP,
        CONCATOP,
        LOGICOP,
        RELATIONALOP,
        BOOLOP,
        DIVOP
    }

    // operatori unari
    private static final OpTable UNARYOP = new OpTable(
            "UnaryOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false))),
                                    new Type("INT", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false))),
                                    new Type("DOUBLE", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false))),
                                    new Type("BOOL", false)
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
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("INT", false))),
                                    new Type("INT", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("DOUBLE", false))),
                                    new Type("DOUBLE", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("DOUBLE", false))),
                                    new Type("DOUBLE", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("INT", false))),
                                    new Type("DOUBLE", false)
                            )
                    )
            )
    );

    private static final OpTable CONCATOP = new OpTable(
            "ConcatOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("INT", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("DOUBLE", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("BOOL", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("CHAR", false), new Type("CHAR", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("CHAR", false), new Type("STRING", false))),
                                    new Type("STRING", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("CHAR", false))),
                                    new Type("STRING", false)
                            )
                    )
            )
    );

    private static final OpTable LOGICOP = new OpTable(
            "LogicOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("BOOL", false))),
                                    new Type("BOOL", false)
                            )
                    )
            )
    );

    private static final OpTable RELATIONALOP = new OpTable(
            "RelationalOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("BOOL", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("BOOL", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            )
                    )
            )
    );

    private static final OpTable BOOLOP = new OpTable(
            "BoolOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("BOOL", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("INT", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("DOUBLE", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("DOUBLE", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("STRING", false), new Type("BOOL", false))),
                                    new Type("BOOL", false)
                            ),
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("BOOL", false), new Type("STRING", false))),
                                    new Type("BOOL", false)
                            )
                    )
            )
    );

    private static final OpTable DIVOP = new OpTable(
            "DivOp",
            new ArrayList<>(
                    List.of(
                            new OpRow(
                                    new ArrayList<>(List.of(new Type("INT", false), new Type("INT", false))),
                                    new Type("DOUBLE", false)
                            )
                    )
            )
    );

    public static SymbolType checkCombination(ArrayList<SymbolType> symbolTypeList, EnumOpTable enumOpTable) {
        try {
            //Prendo l'oggetto dato l'enum fornito in input
            OpTable opTable = (OpTable) OpTableCombinations.class.getDeclaredField(enumOpTable.name()).get(OpTableCombinations.class);
            //Controllo il match dei tipi forniti con quelli presenti in tabella
            for (OpRow opRow : opTable.getOpRowsList()) {
                boolean flag = true;
                //Controllo che i tipi forniti siano compatibili con quelli presenti in tabella
                //Inserisco all'interno di Iterator tutti i tipi di ritorno presenti in tabella
                Iterator<Type> itType = symbolTypeList.stream().map(SymbolType::getOutType).iterator();
                Iterator<Type> itTypeTable = opRow.getOperandi().iterator();

                while (itType.hasNext() && itTypeTable.hasNext())
                    if (!itType.next().getName().equals(itTypeTable.next().getName())) {
                        flag = false;
                    }

                if (itType.hasNext() || itTypeTable.hasNext())
                    throw new RuntimeException("Utilizzati più operandi di quelli consentiti");

                //Restituisce il tipo fornito dal match
                if (flag) {
                    return new SymbolType(opRow.getRisultato());
                }
            }
            throw new RuntimeException("Il tipo non è supportato");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
