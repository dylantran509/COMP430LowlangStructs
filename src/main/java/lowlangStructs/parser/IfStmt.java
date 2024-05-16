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
        boolean elseBodyMatch = true;
        if (elseBody != null && otherAsIfStmt.elseBody != null)
            elseBodyMatch = elseBody.equals(otherAsIfStmt.elseBody);
        else
            elseBodyMatch = (elseBody == null && otherAsIfStmt.elseBody == null);
        return (guard.equals(otherAsIfStmt.guard) &&
                ifBody.equals(otherAsIfStmt.ifBody) &&
                elseBodyMatch);
    }
    
    @Override
    public int hashCode() {
        if (elseBody != null)
            return guard.hashCode() + ifBody.hashCode() + elseBody.hashCode();
        return guard.hashCode() + ifBody.hashCode();
    }
    
    @Override
    public String toString() {
        return ("IfStmt(" +
                guard.toString() + ", " +
                ifBody.toString() + ", " +
                elseBody.toString() + ")");
    }
    
}
