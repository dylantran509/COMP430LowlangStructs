package lowlangStructs.parser;

public class PrintLnStmt implements Stmt {

    public final Expr expr;
    
    public PrintLnStmt(final Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof PrintLnStmt &&
                expr.equals(((PrintLnStmt)other).expr));
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("PrintLnStmt(" + expr.toString() + ")");
    }
    
}
