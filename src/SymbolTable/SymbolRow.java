package SymbolTable;

public class SymbolRow {
    String name;
    String Kind;
    SymbolType type;
    String properties;

    public SymbolRow(String name, String Kind, SymbolType type, String properties) {
        this.name = name;
        this.Kind = Kind;
        this.type = type;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return Kind;
    }

    public SymbolType getType() {
        return type;
    }

    public String getProperties(){
        return properties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKind(String Kind) {
        this.Kind = Kind;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public void setProperties(String properties){
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "SymbolRow{" +
                "name='" + name + '\'' +
                ", Kind='" + Kind + '\'' +
                ", type=" + type +
                ", properties='" + properties + '\'' +
                '}';
    }
}
