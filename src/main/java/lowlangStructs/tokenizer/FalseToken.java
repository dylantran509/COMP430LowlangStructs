package lowlangStructs.tokenizer;

public class FalseToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof FalseToken;
    }
    
    @Override
    public int hashCode() {
        return 23;
    }
    
    @Override
    public String toString() {
        return "FalseToken";
    }
    
}