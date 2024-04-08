package lowlangStructs.tokenizer;

public class AndToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof AndToken;
    }
    
    @Override
    public int hashCode() {
        return 25;
    }
    
    @Override
    public String toString() {
        return "AndToken";
    }
    
}
