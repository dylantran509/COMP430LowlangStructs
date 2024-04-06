package lowlangStructs.tokenizer;

public class DotToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof DotToken;
    }
    
    @Override
    public int hashCode() {
        return 7;
    }
    
    @Override
    public String toString() {
        return "DotToken";
    }
    
}
