package lowlangStructs.parser;

public class NotEqualOp implements Op {

    @Override
    public boolean equals(final Object other) {
        return other instanceof NotEqualOp;
    }
    
    @Override
    public int hashCode() {
        return 9;
    }
    
    @Override
    public String toString() {
        return "NotEqualOp";
    }
    
}
