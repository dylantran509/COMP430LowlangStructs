package lowlangStructs.parser;

public class LHSExpr implements Expr{

    public final Expr expr;
    
    public LHSExpr(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof LHSExpr &&
                expr.equals(((LHSExpr)other).expr));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("LHSExpr(" +
                expr.toString() + ")");
    }
    
}
