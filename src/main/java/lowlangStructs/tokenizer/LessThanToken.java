package lowlangStructs.tokenizer;

public class LessThanToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof LessThanToken;
    }
    
    @Override
    public int hashCode() {
        return 19;
    }
    
    @Override
    public String toString() {
        return "LessThanToken";
    }
    
}