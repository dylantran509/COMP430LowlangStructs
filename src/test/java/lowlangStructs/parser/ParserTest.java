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

        assertEquals(expected, received);
    }
    
    
    @Test
    public void parseFuncDefTest() throws ParseException {
        // (func add ((int x) (int y)) int
        //     (return (+ x y))
        // )
        
        Token[] tokens = new Token[] {
            new LeftParenToken(),
            new FuncToken(),
            new IdentifierToken("add"),
            new LeftParenToken(),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new RightParenToken(),
            new LeftParenToken(),
            new IntToken(),
            new IdentifierToken("y"),
            new RightParenToken(),
            new RightParenToken(),
            new IntToken(),
            new LeftParenToken(),
            new ReturnToken(),
            new LeftParenToken(),
            new PlusToken(),
            new IdentifierToken("x"),
            new IdentifierToken("y"),
            new RightParenToken(),
            new RightParenToken(),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(Arrays.asList(
                                           new FuncDef(
                                               new Variable("add"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("x")),
                                                   new Param(new IntType(), new Variable("y")))),
                                               new IntType(),
                                               new ArrayList<Stmt>(Arrays.asList(
                                                   new ReturnStmt(
                                                       new BinOpExpr(
                                                           new PlusOp(),
                                                           new LHSExpr(new VarLHS(new Variable("x"))),
                                                           new LHSExpr(new VarLHS(new Variable("y")))))))))),
                                       new ArrayList<Stmt>());
        
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    @Test
    public void parseFuncDefWithTokenizerTest() throws TokenizerException, ParseException {
        // (func add ((int x) (int y)) int
        //     (return (+ x y))
        // )
        
        Token[] tokens = new Tokenizer("(func add ((int x) (int y)) int (return (+ x y)))").tokenize();
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(Arrays.asList(
                                           new FuncDef(
                                               new Variable("add"),
                                               new ArrayList<Param>(Arrays.asList(
                                                   new Param(new IntType(), new Variable("x")),
                                                   new Param(new IntType(), new Variable("y")))),
                                               new IntType(),
                                               new ArrayList<Stmt>(Arrays.asList(
                                                   new ReturnStmt(
                                                       new BinOpExpr(
                                                           new PlusOp(),
                                                           new LHSExpr(new VarLHS(new Variable("x"))),
                                                           new LHSExpr(new VarLHS(new Variable("y")))))))))),
                                       new ArrayList<Stmt>());
        
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    
    @Test
    public void parseVardecTest() throws ParseException {
        // (vardec int x)
        Token[] tokens = new Token[] {
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new VardecStmt(
                                               new IntType(),
                                               new Variable("x")))));
        
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    @Test
    public void parseBlockOfVardecStmtsTest() throws ParseException {
        // (block
        //     (vardec int x)
        //     (vardec (* Node) leftChild)
        //     (vardec Node root)
        // )
        
        Token[] tokens = new Token[] {
            new LeftParenToken(),
            new BlockToken(),
            
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new RightParenToken(),
            
            new LeftParenToken(),
            new VardecToken(),
            new LeftParenToken(),
            new StarToken(),
            new IdentifierToken("Node"),
            new RightParenToken(),
            new IdentifierToken("leftChild"),
            new RightParenToken(),
            
            new LeftParenToken(),
            new VardecToken(),
            new IdentifierToken("Node"),
            new IdentifierToken("root"),
            new RightParenToken(),
            
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new BlockStmt(Arrays.asList(
                                               new VardecStmt(new IntType(), new Variable("x")),
                                               new VardecStmt(new PointerType(new StructType(new Variable("Node"))), new Variable("leftChild")),
                                               new VardecStmt(new StructType(new Variable("Node")), new Variable("root")))))));
        
        Program received = new Parser(tokens).parse();
        
        assertEquals(expected, received);
        
    }

    @Test
    public void parseExampleProgram() throws TokenizerException, ParseException {
        // Length of linked list example
        Token[] tokens = new Tokenizer(" (struct Node"
                                     + "     (int value)"
                                     + "     ((* Node) next)"
                                     + " )"
                                     + " (func length (((* Node) list)) int"
                                     + "     (vardec int retval)"
                                     + "     (assign retval 0)"
                                     + "     (while (!= list null)"
                                     + "         (block "
                                     + "             (assign retval (+ retval 1))"
                                     + "             (assign list (. (* list) next))"
                                     + "         )"
                                     + "     )"
                                     + "     (return retval)"
                                     + " )"
                                     + " (vardec Node first)"
                                     + " (vardec Node second)"
                                     + " (vardec Node third)"
                                     + " (assign (. first value) 1)"
                                     + " (assign (. first next) (& second))"
                                     + " (assign (. second value) 2)"
                                     + " (assign (. second next) (& third))"
                                     + " (assign (. third value) 3)"
                                     + " (assign (. third next) null)"
                                     + " (println (call length (& first)))").tokenize();
        Program myPgrm = new Parser(tokens).parse();
    }
    
}
