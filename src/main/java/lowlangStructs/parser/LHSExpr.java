package lowlangStructs.parser;

public class LHSExpr implements Expr{

    public final LHS lhs;
    
    public LHSExpr(final LHS lhs) {
        this.lhs = lhs;;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof LHSExpr &&
                lhs.equals(((LHSExpr)other).lhs));
    }
    
    @Override
    public int hashCode() {
        return lhs.hashCode();
    }
    
    @Override
    public String toString() {
        return ("LHSExpr(" +
                lhs.toString() + ")");
    }
    
}
