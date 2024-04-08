package lowlangStructs.tokenizer;

public class BlockToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof BlockToken;
    }
    
    @Override
    public int hashCode() {
        return 13;
    }
    
    @Override
    public String toString() {
        return "BlockToken";
    }
    
}