package lowlangStructs.tokenizer;

public class DoubleEqualToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof DoubleEqualToken;
    }
    
    @Override
    public int hashCode() {
        return 20;
    }
    
    @Override
    public String toString() {
        return "DoubleEqualToken";
    }
    
}
