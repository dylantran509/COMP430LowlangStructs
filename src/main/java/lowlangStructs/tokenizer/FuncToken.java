package lowlangStructs.tokenizer;

public class FuncToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof FuncToken;
    }
    
    @Override
    public int hashCode() {
        return 6;
    }
    
    @Override
    public String toString() {
        return "FuncToken";
    }
    
}