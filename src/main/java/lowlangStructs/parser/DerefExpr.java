package lowlangStructs.parser;

public class DerefExpr implements Expr {

    public final Expr expr;
    
    public DerefExpr(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof DerefExpr &&
                expr.equals(((DerefExpr)other).expr));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("DerefExpr(" +
                expr.toString() + ")");
    }
    
}
