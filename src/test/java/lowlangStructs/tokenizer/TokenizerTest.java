package lowlangStructs.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

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
    
    @Test
    void IdentifierAndNumberTokenNotEqualsTest() throws TokenizerException {
        assertAll("Identifier and number tokens with different stored values should not be equal",
            () -> assertNotEquals(new IdentifierToken("foo"), new IdentifierToken("bar")),
            () -> assertNotEquals(new NumberToken(1), new NumberToken(2))
        );
    }
    
    
    
    @Test
    void tokenizeEmptyProgramTest() throws TokenizerException {
        Token[] tokens = new Tokenizer("").tokenize();
        Token[] expected = new Token[] {};
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    void tokenizeProgramWithOnlySpacesTest() throws TokenizerException {
        Token[] tokens = new Tokenizer("        ").tokenize();
        Token[] expected = new Token[] {};
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    void tokenizeVardecAndAssignTest() throws TokenizerException {
        Token[] tokens = new Tokenizer("(vardec int myInt) (assign myInt 0)").tokenize();
        Token[] expected = new Token[] {
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("myInt"),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("myInt"),
            new NumberToken(0),
            new RightParenToken()
        };
        assertArrayEquals(tokens, expected);
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
    void tokenizeLinkedListLengthFuncTest() throws TokenizerException {
        String linkedListLengthFunc = "(func length (((* Node) list)) int"
                                    +     "(vardec int retval)"
                                    +     "(assign retval 0)"
                                    +     "(while (!= list null)"
                                    +         "(assign retval (+ retval 1))"
                                    +         "(assign list (. (* list) next))"
                                    +     ")"
                                    +     "(return retval)"
                                    + ")";
        
        Token[] tokens = new Tokenizer(linkedListLengthFunc).tokenize();
        Token[] expected = new Token[] {
            new LeftParenToken(),
            new FuncToken(),
            new IdentifierToken("length"),
            new LeftParenToken(),
            new LeftParenToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("Node"),
            new RightParenToken(),
            new IdentifierToken("list"),
            new RightParenToken(),
            new RightParenToken(),
            new IntToken(),
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("retval"),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("retval"),
            new NumberToken(0),
            new RightParenToken(),
            new LeftParenToken(),
            new WhileToken(),
            new LeftParenToken(),
            new NotEqualToken(),
            new IdentifierToken("list"),
            new NullToken(),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("retval"),
            new LeftParenToken(),
            new PlusToken(),
            new IdentifierToken("retval"),
            new NumberToken(1),
            new RightParenToken(),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("list"),
            new LeftParenToken(),
            new DotToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("list"),
            new RightParenToken(),
            new IdentifierToken("next"),
            new RightParenToken(),
            new RightParenToken(),
            new RightParenToken(),
            new LeftParenToken(),
            new ReturnToken(),
            new IdentifierToken("retval"),
            new RightParenToken(),
            new RightParenToken()
        };
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    void invalidSymbolTest() throws TokenizerException {
        Exception myException = assertThrows(TokenizerException.class,
            () -> {
                Token[] tokens = new Tokenizer("(vardec myInt^2 int)").tokenize();
            });
        System.out.println("invalidSymbolTest result:\n" + myException.toString() + "\n");
    }
    
    @Test
    void invalidIdentifierTest() throws TokenizerException {
        Exception myException = assertThrows(TokenizerException.class,
            ()->{
                Token[] tokens = new Tokenizer("(vardec 890myInt int)").tokenize();
            });
        System.out.println("invalidIdentifierTest result:\n" + myException.toString() + "\n");
    }
}
