package lowlangStructs.tokenizer;

public class NullToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof NullToken;
    }
    
    @Override
    public int hashCode() {
        return 24;
    }
    
    @Override
    public String toString() {
        return "NullToken";
    }
    
}
