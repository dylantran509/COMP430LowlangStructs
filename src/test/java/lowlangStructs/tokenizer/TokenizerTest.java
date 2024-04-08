package lowlangStructs.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TokenizerTest {

    @Test
    void equalTokenAssertAllTest() {
        assertAll("Identical token objects should be equal",
            () -> assertEquals(new IdentifierToken("foo"), new IdentifierToken("foo")),
            () -> assertEquals(new NumberToken(21), new NumberToken(21)),
            () -> assertEquals(new IntToken(), new IntToken()),
            () -> assertEquals(new VoidToken(), new VoidToken()),
            () -> assertEquals(new LeftParenToken(), new LeftParenToken()),
            () -> assertEquals(new StarToken(), new StarToken()),
            () -> assertEquals(new RightParenToken(), new RightParenToken()),
            () -> assertEquals(new StructToken(), new StructToken())
            // Need other Token types here
        );
    }
    
    @Test
    void tokenizeNodeStructDefTest() throws TokenizerException {
        Token[] tokens = new Tokenizer("(struct Node (int value) ((* Node) next))").tokenize();
        Token[] expected = new Token[] {
            new LeftParenToken(),
            new StructToken(),
            new IdentifierToken("Node"),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("value"),
            new RightParenToken(),
            new LeftParenToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("Node"),
            new RightParenToken(),
            new IdentifierToken("next"),
            new RightParenToken(),
            new RightParenToken()
        };
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    void tokenizeFailureTest() throws TokenizerException {
        assertThrows(TokenizerException.class,
            ()->{
                Token[] tokens = new Tokenizer("890myVar").tokenize();
            });
        
    }
}
