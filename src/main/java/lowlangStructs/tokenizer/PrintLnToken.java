package lowlangStructs.tokenizer;

public class PrintLnToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof PrintLnToken;
    }
    
    @Override
    public int hashCode() {
        return 14;
    }
    
    @Override
    public String toString() {
        return "PrintLnToken";
    }
    
}