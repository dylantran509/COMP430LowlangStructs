package lowlangStructs.tokenizer;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    
    // ---------------------------- Helper class and dictionary ------------------------------------
    
    class SymbolTokenPair {
        
        public String symbol;
        public Token token;
        
        public SymbolTokenPair(final String symbol, final Token token) {
            this.symbol = symbol;
            this.token = token;
        }
        
    }
    
    private final SymbolTokenPair[] pairs = new SymbolTokenPair[] {
            new SymbolTokenPair("(", new LeftParenToken()),
            new SymbolTokenPair("*", new StarToken()),
            new SymbolTokenPair(")", new RightParenToken()),
            new SymbolTokenPair(".", new DotToken()),
            new SymbolTokenPair("+", new PlusToken()),
            new SymbolTokenPair("-", new MinusToken()),
            new SymbolTokenPair("/", new DivideToken()),
            new SymbolTokenPair("<", new LessThanToken()),
            new SymbolTokenPair("==", new DoubleEqualToken()),
            new SymbolTokenPair("!=", new NotEqualToken()),
            new SymbolTokenPair("&", new AndToken())
    };
    
    // ---------------------------------------------------------------------------------------------
    
    private final String input;
    private int position;
    
    public Tokenizer(final String input) {
        this.input = input;
        this.position = 0;
    }
    
    public Token[] tokenize() throws TokenizerException {
        
        List<Token> tokenList = new ArrayList<>();
        skipWhitespace();
        while(position < input.length()) {
            Token toAdd = readToken();
            tokenList.add(toAdd);
            skipWhitespace();
        }
        
        return tokenList.toArray(new Token[tokenList.size()]);
    }
    
    public void skipWhitespace() {
        while(position < input.length() && input.charAt(position) == ' ')
            position++;
    }
    
    public Token readToken() throws TokenizerException {
        
        Token token;
        
        token = readSymbol();
        if (token != null) 
            return token;
        
        token = readNumber();
        if (token != null)
            return token;
        
        token = readIdentifierOrReservedWord();
        if (token != null)
            return token;
        
        throw new TokenizerException("TokenizerException: Could not tokenize " +
                                     readUntilWhitespaceOrEOF());
        
        
    }
    
    public Token readSymbol() throws TokenizerException {
        
        Token token = null;
        
        for(SymbolTokenPair pair: pairs) {
            if (input.startsWith(pair.symbol, position)) {
                token = pair.token;
                position += pair.symbol.length();
                return token;
            }
        }
        
        return token;
    }
    
    public Token readNumber() throws TokenizerException {
        
        int positionCopy = position;
        
        String digits = "";
        while (position < input.length()) {
            if (Character.isDigit(input.charAt(position))){
                digits += input.charAt(position);
                position++;
            } else if (Character.isAlphabetic(input.charAt(position))){
                // Encountered number followed by letter, abort reading number
                position = positionCopy;
                digits = "";
                break;
            } else {
                break;
            }
        }
        if (digits.length() > 0) {
            return new NumberToken(Integer.parseInt(digits));
        } else {
            return null;
        }
    }
    
    public Token readIdentifierOrReservedWord() throws TokenizerException {
        
        String name = "";
        if (Character.isLetter(input.charAt(position))) {
            
            name += input.charAt(position);
            position++;
            while (position < input.length() &&
                   Character.isLetterOrDigit(input.charAt(position))) {
                name += input.charAt(position);
                position++;
            }
            
            if (name.equals("int"))
                return new IntToken();
            else if (name.equals("void"))
                return new VoidToken();
            else if (name.equals("struct"))
                return new StructToken();
            else if (name.equals("func"))
                return new FuncToken();
            else if (name.equals("vardec"))
                return new VardecToken();
            else if (name.equals("assign"))
                return new AssignToken();
            else if (name.equals("while"))
                return new WhileToken();
            else if (name.equals("If"))
                return new IfToken();
            else if (name.equals("return"))
                return new ReturnToken();
            else if (name.equals("block"))
                return new BlockToken();
            else if (name.equals("println"))
                return new PrintLnToken();
            else if (name.equals("stmt"))
                return new StmtToken();
            else if (name.equals("true"))
                return new TrueToken();
            else if (name.equals("false"))
                return new FalseToken();
            else if (name.equals("null"))
                return new NullToken();
            else if (name.equals("call"))
                return new CallToken();
            else
                return new IdentifierToken(name);
            
        } else {
            return null;
        }
    }
    
    public String readUntilWhitespaceOrEOF() {
        String unrecognizedWord = "";
        
        while (position < input.length() &&
               !(input.charAt(position) == ' ')) {
            unrecognizedWord += input.charAt(position);
            position++;
        }
            
        
        return unrecognizedWord;
    }
    
}
