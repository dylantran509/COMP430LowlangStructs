package lowlangStructs.parser;

public class AddressExpr implements Expr {

    public final LHS lhs;
    
    public AddressExpr(final LHS lhs) {
        this.lhs = lhs;
    }
    
    @Override
    public boolean equals(final Object other ) {
        return (other instanceof AddressExpr &&
                lhs.equals(((AddressExpr)other).lhs));
    }
    
    @Override
    public int hashCode() {
        return lhs.hashCode();
    }
    
    @Override
    public String toString() {
        return ("AddressExpr(" +
                lhs.toString() + ")");
    }
    
}
