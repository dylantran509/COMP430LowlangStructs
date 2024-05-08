package lowlangStructs.parser;

public class PointerType implements Type {

    public final Type type;
    
    public PointerType(final Type type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof PointerType &&
                type.equals(((PointerType)other).type));
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
    
    @Override
    public String toString() {
        return "PointerType(" + type.toString() + ")";
    }
    
}
