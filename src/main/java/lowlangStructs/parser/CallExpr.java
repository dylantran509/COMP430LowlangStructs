package lowlangStructs.parser;
import java.util.List;
import java.util.ArrayList;

public class CallExpr implements Expr{

    public final Variable funcname;
    public final List<Expr> exprs;
    
    public CallExpr(final Variable funcname, final List<Expr> exprs) {
        this.funcname = funcname;
        this.exprs = exprs;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CallExpr))
            return false;
        final CallExpr otherAsCallExpr = (CallExpr)other;
        return(funcname.equals(otherAsCallExpr.funcname) &&
               exprs.equals(otherAsCallExpr.exprs));
    }
    
    @Override
    public int hashCode() {
        return funcname.hashCode() + exprs.hashCode();
    }
    
    @Override
    public String toString() {
        return ("CallExpr(" +
                funcname + ", " +
                exprs.toString() + ")");
    }
    
}
