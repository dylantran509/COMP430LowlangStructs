package lowlangStructs.parser;

public class WhileStmt implements Stmt {

    public final Expr guard;
    public final Stmt stmt;
    
    public WhileStmt(final Expr guard, final Stmt stmt) {
        this.guard = guard;
        this.stmt = stmt;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof WhileStmt))
            return false;
        final WhileStmt otherAsWhileStmt = (WhileStmt)other;
        return (guard.equals(otherAsWhileStmt.guard) &&
                stmt.equals(otherAsWhileStmt.stmt));
    }
    
    @Override
    public int hashCode() {
        return guard.hashCode() + stmt.hashCode();
    }
    
    @Override
    public String toString() {
        return ("WhileStmt(" +
                guard.toString() + ", " +
                stmt.toString() + ")");
    }
    
}
