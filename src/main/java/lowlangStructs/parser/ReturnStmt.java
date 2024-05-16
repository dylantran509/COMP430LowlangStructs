package lowlangStructs.parser;

public class ReturnStmt implements Stmt {

    public final Expr expr;
    
    public ReturnStmt() {
        this.expr = null;
    }
    
    public ReturnStmt(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ReturnStmt))
            return false;
        final ReturnStmt otherAsReturnStmt = (ReturnStmt)other;
        if (expr != null && otherAsReturnStmt.expr != null)
            return expr.equals(otherAsReturnStmt.expr);
        else
            return (expr == null && otherAsReturnStmt.expr == null);
    }
    
    @Override
    public int hashCode() {
        if (expr != null)
            return expr.hashCode();
        return 11;
    }
    
    @Override
    public String toString() {
        return ("ReturnStmt(" + expr.toString() + ")");
    }
    
}
