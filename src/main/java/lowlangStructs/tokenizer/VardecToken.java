package lowlangStructs.tokenizer;

public class VardecToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof VardecToken;
    }
    
    @Override
    public int hashCode() {
        return 8;
    }
    
    @Override
    public String toString() {
        return "VardecToken";
    }
    
}
