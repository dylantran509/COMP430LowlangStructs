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
            () -> assertEquals(new FuncToken(), new FuncToken()),
            () -> assertEquals(new DotToken(), new DotToken()),
            () -> assertEquals(new VardecToken(), new VardecToken()),
            () -> assertEquals(new AssignToken(), new AssignToken()),
            () -> assertEquals(new WhileToken(), new WhileToken()),
            () -> assertEquals(new IfToken(), new IfToken()),
            () -> assertEquals(new ReturnToken(), new ReturnToken()),
            () -> assertEquals(new BlockToken(), new BlockToken()),
            () -> assertEquals(new PrintLnToken(), new PrintLnToken()),
            () -> assertEquals(new StmtToken(), new StmtToken()),
            () -> assertEquals(new PlusToken(), new PlusToken()),
            () -> assertEquals(new MinusToken(), new MinusToken()),
            () -> assertEquals(new DivideToken(), new DivideToken()),
            () -> assertEquals(new LessThanToken(), new LessThanToken()),
            () -> assertEquals(new DoubleEqualToken(), new DoubleEqualToken()),
            () -> assertEquals(new NotEqualToken(), new NotEqualToken()),
            () -> assertEquals(new TrueToken(), new TrueToken()),
            () -> assertEquals(new FalseToken(), new FalseToken()),
            () -> assertEquals(new NullToken(), new NullToken()),
            () -> assertEquals(new AndToken(), new AndToken()),
            () -> assertEquals(new CallToken(), new CallToken())
        );
    }
    
}
