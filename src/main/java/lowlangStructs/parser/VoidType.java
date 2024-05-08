package lowlangStructs.parser;

public class VoidType implements Type{

    @Override
    public boolean equals(final Object other) {
        return other instanceof VoidType;
    }
    
    @Override
    public int hashCode() {
        return 2;
    }
    
    @Override
    public String toString() {
        return "VoidType";
    }
    
}
