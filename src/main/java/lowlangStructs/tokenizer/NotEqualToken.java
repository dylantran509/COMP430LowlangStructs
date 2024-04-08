package lowlangStructs.tokenizer;

public class NotEqualToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof NotEqualToken;
    }
    
    @Override
    public int hashCode() {
        return 21;
    }
    
    @Override
    public String toString() {
        return "NotEqualToken";
    }
    
}