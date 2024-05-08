package lowlangStructs.parser;

public class NumberLiteralExpr implements Expr{

    public final int value;
    
    public NumberLiteralExpr(final int value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof NumberLiteralExpr &&
                value == ((NumberLiteralExpr)other).value);
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public String toString() {
        return "NumberLiteralExpr(" + value + ")";
    }
    
}
