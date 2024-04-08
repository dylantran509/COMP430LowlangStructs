package lowlangStructs.tokenizer;

public class StmtToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof StmtToken;
    }
    
    @Override
    public int hashCode() {
        return 15;
    }
    
    @Override
    public String toString() {
        return "StmtToken";
    }
    
}