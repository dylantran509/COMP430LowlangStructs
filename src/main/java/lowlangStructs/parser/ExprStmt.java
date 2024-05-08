package lowlangStructs.parser;

public class ExprStmt implements Stmt {

    public final Expr expr;
    
    public ExprStmt(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof ExprStmt &&
                expr.equals(((ExprStmt)other).expr));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("ExprStmt(" + expr.toString() + ")");
    }
    
}
