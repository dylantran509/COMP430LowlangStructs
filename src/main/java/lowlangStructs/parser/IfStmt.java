package lowlangStructs.parser;

public class IfStmt implements Stmt {

    public final Expr guard;
    public final Stmt ifBody;
    public final Stmt elseBody;
    
    public IfStmt(final Expr guard, final Stmt ifBody) {
        this.guard = guard;
        this.ifBody = ifBody;
        this.elseBody = null;
    }
    
    public IfStmt(final Expr guard,
                  final Stmt ifBody,
                  final Stmt elseBody) {
        this.guard = guard;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IfStmt))
            return false;
        final IfStmt otherAsIfStmt = (IfStmt)other;
        return (guard.equals(otherAsIfStmt.guard) &&
                ifBody.equals(otherAsIfStmt.ifBody) &&
                elseBody.equals(otherAsIfStmt.elseBody));
    }
    
    @Override
    public int hashCode() {
        return guard.hashCode() + ifBody.hashCode() + elseBody.hashCode();
    }
    
    @Override
    public String toString() {
        return ("IfStmt(" +
                guard.toString() + ", " +
                ifBody.toString() + ", " +
                elseBody.toString() + ")");
    }
    
}
