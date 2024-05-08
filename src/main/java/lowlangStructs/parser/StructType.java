package lowlangStructs.parser;

public class StructType implements Type {

    public final Variable structname;
    
    public StructType(final Variable structname) {
        this.structname = structname;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof StructType &&
                structname.equals(((StructType)other).structname));
    }
    
    @Override
    public int hashCode() {
        return structname.hashCode();
    }
    
    @Override
    public String toString() {
        return "StructType(" + structname + ")";
    }
    
}
