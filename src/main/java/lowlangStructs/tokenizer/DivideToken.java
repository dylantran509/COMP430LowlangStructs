package lowlangStructs.tokenizer;

public class DivideToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof DivideToken;
    }
    
    @Override
    public int hashCode() {
        return 18;
    }
    
    @Override
    public String toString() {
        return "DivideToken";
    }
    
}