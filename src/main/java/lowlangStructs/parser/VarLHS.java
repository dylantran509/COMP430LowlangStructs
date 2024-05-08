package lowlangStructs.parser;

public class VarLHS implements LHS{

    public final Variable var;
    
    public VarLHS(final Variable var) {
        this.var = var;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof VarLHS &&
                var.equals(((VarLHS)other).var));
    }
    
    @Override
    public int hashCode() {
        return var.hashCode();
    }
    
    @Override
    public String toString() {
        return "VarLHS(" + var.toString() + ")";
    }
    
}
