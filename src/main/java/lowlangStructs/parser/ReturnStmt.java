package lowlangStructs.parser;

public class ReturnStmt implements Stmt {

    public final Expr expr;
    
    public ReturnStmt(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof ReturnStmt &&
                expr.equals(((ReturnStmt)other).expr));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("ReturnStmt(" + expr.toString() + ")");
    }
    
}
