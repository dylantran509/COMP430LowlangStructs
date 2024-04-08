package lowlangStructs.tokenizer;

public class AssignToken implements Token{

    @Override
    public boolean equals(final Object other) {
        return other instanceof AssignToken;
    }
    
    @Override
    public int hashCode() {
        return 9;
    }
    
    @Override
    public String toString() {
        return "AssignToken";
    }
    
}