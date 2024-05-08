package lowlangStructs.parser;

public class BinOpExpr implements Expr {
    
    public final Op op;
    public final Expr leftExpr;
    public final Expr rightExpr;
    
    public BinOpExpr(final Op op,
                     final Expr leftExpr,
                     final Expr rightExpr) {
        this.op = op;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof BinOpExpr))
            return false;
        final BinOpExpr otherAsBinOpExpr = (BinOpExpr)other;
        return (op.equals(otherAsBinOpExpr.op) &&
                leftExpr.equals(otherAsBinOpExpr.leftExpr) &&
                rightExpr.equals(otherAsBinOpExpr.rightExpr));
    }
    
    @Override
    public int hashCode() {
        return op.hashCode() + leftExpr.hashCode() + rightExpr.hashCode();
    }
    
    @Override
    public String toString() {
        return ("BinOpExpr(" +
                op.toString() + ", " +
                leftExpr.toString() + ", " +
                rightExpr.toString() + ")");
    }

}
