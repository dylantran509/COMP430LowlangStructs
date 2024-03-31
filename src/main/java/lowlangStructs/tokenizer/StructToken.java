package lowlangStructs.tokenizer;

public class StructToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof StructToken;
    }
    
    @Override
    public int hashCode() {
        return 5;
    }
    
    @Override
    public String toString() {
        return "StructToken";
    }
    
}
