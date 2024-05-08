package lowlangStructs.parser;

public class AssignStmt implements Stmt {

    public final LHS lhs;
    public final Expr expr;
    
    public AssignStmt(final LHS lhs, final Expr expr) {
        this.lhs = lhs;
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof AssignStmt))
            return false;
        final AssignStmt otherAsAssignStmt = (AssignStmt)other;
        return (lhs.equals(otherAsAssignStmt.lhs) &&
                expr.equals(otherAsAssignStmt.expr));
    }
    
    @Override
    public int hashCode() {
        return lhs.hashCode() + expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("AssignStmt(" +
                lhs.toString() + ", " +
                expr.toString() + ")");
    }
    
}
