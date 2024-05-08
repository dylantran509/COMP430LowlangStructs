package lowlangStructs.parser;

public class DoubleEqualOp implements Op {

    @Override
    public boolean equals(final Object other) {
        return other instanceof DoubleEqualOp;
    }
    
    @Override
    public int hashCode() {
        return 8;
    }
    
    @Override
    public String toString() {
        return "DoubleEqualOp";
    }
    
}
