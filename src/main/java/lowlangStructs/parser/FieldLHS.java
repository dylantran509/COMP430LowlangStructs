package lowlangStructs.parser;

public class FieldLHS implements LHS {

    public final LHS lhs;
    public final Variable var;
    
    public FieldLHS(final LHS lhs, final Variable var) {
        this.lhs = lhs;
        this.var = var;;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof FieldLHS))
            return false;
        final FieldLHS otherAsFieldLHS = (FieldLHS)other;
        return (lhs.equals(otherAsFieldLHS.lhs) &&
                var.equals(otherAsFieldLHS.var));
    }
    
    @Override
    public int hashCode() {
        return lhs.hashCode() + var.hashCode();
    }
    
    @Override
    public String toString() {
        return ("FieldLHS(" +
                lhs.toString() + ", " +
                var.toString() + ")");
    }
}
