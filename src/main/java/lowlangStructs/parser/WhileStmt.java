package lowlangStructs.parser;

public class WhileStmt implements Stmt {

    public final Expr expr;
    public final Stmt stmt;
    
    public WhileStmt(final Expr expr, final Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof WhileStmt))
            return false;
        final WhileStmt otherAsWhileStmt = (WhileStmt)other;
        return (expr.equals(otherAsWhileStmt.expr) &&
                stmt.equals(otherAsWhileStmt.stmt));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode() + stmt.hashCode();
    }
    
    @Override
    public String toString() {
        return ("WhileStmt(" +
                expr.toString() + ", " +
                stmt.toString() + ")");
    }
    
}
