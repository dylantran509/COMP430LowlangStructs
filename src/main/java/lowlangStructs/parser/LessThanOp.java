package lowlangStructs.parser;

public class LessThanOp implements Op {

    @Override
    public boolean equals(final Object other) {
        return other instanceof LessThanOp;
    }
    
    @Override
    public int hashCode() {
        return 7;
    }
    
    @Override
    public String toString() {
        return "LessThanOp";
    }
    
}
