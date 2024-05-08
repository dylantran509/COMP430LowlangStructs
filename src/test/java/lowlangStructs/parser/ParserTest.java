package lowlangStructs.parser;
import lowlangStructs.tokenizer.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ParserTest {

    @Test
    public void structDefParseTest() throws ParseException{
        // (struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))
        Token[] tokens = new Token[] {
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
            new IdentifierToken("leftChild"),
            new RightParenToken(),
            new LeftParenToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("Node"),
            new RightParenToken(),
            new IdentifierToken("rightChild"),
            new RightParenToken(),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(Arrays.asList(
                                           new StructDef(
                                               new Variable("Node"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("leftChild")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("rightChild"))))))),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>());
        final Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    @Test
    public void structDefParseWithTokenizerTest() throws TokenizerException, ParseException {
        // (struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))
        Token[] tokens = new Tokenizer("(struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))").tokenize();
        
        Program expected = new Program(new ArrayList<StructDef>(Arrays.asList(
                                           new StructDef(
                                               new Variable("Node"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("leftChild")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("rightChild"))))))),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>());
        final Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    @Test
    public void multipleStructDefParseTest() throws ParseException {
        // (struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))
        // (struct Leaf (int value))
        // (struct Coodinate (int x) (int y))
        Token[] tokens = new Token[] {
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
            new IdentifierToken("leftChild"),
            new RightParenToken(),
            new LeftParenToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("Node"),
            new RightParenToken(),
            new IdentifierToken("rightChild"),
            new RightParenToken(),
            new RightParenToken(),
            
            new LeftParenToken(),
            new StructToken(),
            new IdentifierToken("Leaf"),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("value"),
            new RightParenToken(),
            new RightParenToken(),
            
            new LeftParenToken(),
            new StructToken(),
            new IdentifierToken("Coodinate"),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new RightParenToken(),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("y"),
            new RightParenToken(),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(Arrays.asList(
                                           new StructDef(
                                               new Variable("Node"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("leftChild")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("rightChild"))))),
                                           new StructDef(
                                               new Variable("Leaf"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value"))))),
                                           new StructDef(
                                               new Variable("Coodinate"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("x")),
                                                   new Param(new IntType(), new Variable("y"))))))),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>());
        
        
        final Program received = new Parser(tokens).parse();
        received.prettyPrint();
        assertEquals(expected, received);
    }
    
    @Test
    public void multipleStructDefParseWithTokenizerTest() throws TokenizerException, ParseException {
        // (struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))
        // (struct Leaf (int value))
        // (struct Coodinate (int x) (int y))
        Token[] tokens = new Tokenizer("(struct Node (int value) ((* Node) leftChild) ((* Node) rightChild))" +
                                       "(struct Leaf (int value))" + 
                                       "(struct Coordinate (int x) (int y))").tokenize();
        
        Program expected = new Program(new ArrayList<StructDef>(Arrays.asList(
                                           new StructDef(
                                               new Variable("Node"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("leftChild")),
                                                   new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("rightChild"))))),
                                           new StructDef(
                                               new Variable("Leaf"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("value"))))),
                                           new StructDef(
                                               new Variable("Coordinate"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("x")),
                                                   new Param(new IntType(), new Variable("y"))))))),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>());
        
        
        final Program received = new Parser(tokens).parse();
        received.prettyPrint();
        assertEquals(expected, received);
    }

    
    
}
