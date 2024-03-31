package lowlangStructs.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;


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
    
}
