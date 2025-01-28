package Visitors.OpTable;

import Nodes.Type;

import java.util.ArrayList;
import java.util.List;

public class OpTableCombinations {
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
}
