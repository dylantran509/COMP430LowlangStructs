package lowlangStructs.parser;

public class StarOp implements Op {

    @Override
    public boolean equals(final Object other) {
        return other instanceof StarOp;
    }
    
    @Override
    public int hashCode() {
        return 5;
    }
    
    @Override
    public String toString() {
        return "StarOp";
    }
    
}
