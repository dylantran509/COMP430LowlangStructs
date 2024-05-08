package lowlangStructs.parser;

public class DerefLHS implements LHS{

    public final LHS lhs;
    
    public DerefLHS(final LHS lhs) {
        this.lhs = lhs;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof DerefLHS &&
                lhs.equals(((DerefLHS)other).lhs));
    }
    
    @Override
    public int hashCode() {
        return lhs.hashCode();
    }
    
    @Override
    public String toString() {
        return "DerefLHS(" + lhs.toString() + ")";
    }
    
}
