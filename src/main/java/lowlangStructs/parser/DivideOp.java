package lowlangStructs.parser;

public class DivideOp implements Op {

    @Override
    public boolean equals(final Object other) {
        return other instanceof DivideOp;
    }
    
    @Override
    public int hashCode() {
        return 6;
    }
    
    @Override
    public String toString() {
        return "DivideOp";
    }
    
}
