package lowlangStructs.parser;

public class BooleanLiteralExpr implements Expr {

    public final boolean value;
    
    public BooleanLiteralExpr(final boolean value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof BooleanLiteralExpr &&
                value == ((BooleanLiteralExpr)other).value);
    }
    
    @Override
    public int hashCode() {
        // Return 1 if true, 0 if false
        return (value) ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return "BooleanLiteralExpr(" + value + ")";
    }
    
}
