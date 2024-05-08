package lowlangStructs.parser;

public class VardecStmt implements Stmt {

    public final Type type;
    public final Variable var;
    
    public VardecStmt(final Type type, final Variable var) {
        this.type = type;
        this.var = var;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof VardecStmt))
            return false;
        final VardecStmt otherAsVardecStmt = (VardecStmt)other;
        return (type.equals(otherAsVardecStmt.type) &&
                var.equals(otherAsVardecStmt.var));
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() + var.hashCode();
    }
    
    @Override
    public String toString() {
        return ("VardecStmt(" +
                type.toString() + ", " +
                var.toString() + ")");
    }
    
}
