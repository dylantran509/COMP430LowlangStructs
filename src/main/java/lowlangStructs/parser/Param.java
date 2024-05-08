package lowlangStructs.parser;

public class Param {

    public final Type type;
    public final Variable var;
    
    public Param(final Type type, final Variable var) {
        this.type = type;
        this.var = var;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Param))
            return false;
        final Param otherAsParam = (Param)other;
        return (type.equals(otherAsParam.type) &&
                var.equals(otherAsParam.var));
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() + var.hashCode();
    }
    
    @Override
    public String toString() {
        return ("Param(" +
                type.toString() + ", " +
                var.toString() + ")");
    }
    
}
