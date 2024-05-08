package lowlangStructs.parser;

public class NullExpr implements Expr {

    @Override
    public boolean equals(final Object other) {
        return other instanceof NullExpr;
    }
    
    @Override
    public int hashCode() {
        return 10;
    }
    
    @Override
    public String toString() {
        return "NullExpr";
    }
    
}
