package lowlangStructs.parser;
import lowlangStructs.tokenizer.*;
import lowlangStructs.parser.*;
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
    public void sameNodeEqualsTest() {
        assertAll("Identical node objects should be equal",
                () -> assertEquals(new IntType(), new IntType()),
                () -> assertEquals(new BoolType(), new BoolType()),
                () -> assertEquals(new VoidType(), new VoidType()),
                // Node Struct = Node Struct
                () -> assertEquals(new StructType(new Variable("Node")), new StructType(new Variable("Node"))),
                // pointer to int
                () -> assertEquals(new PointerType(new IntType()), new PointerType(new IntType())),
                () -> assertEquals(new VarLHS(new Variable("foo")), new VarLHS(new Variable("foo"))),
                () -> assertEquals(new FieldLHS(new VarLHS(new Variable("foo")), new Variable("bar")),
                                   new FieldLHS(new VarLHS(new Variable("foo")), new Variable("bar"))),
                () -> assertEquals(new DerefLHS(new VarLHS(new Variable("somePointer"))),
                                   new DerefLHS(new VarLHS(new Variable("somePointer")))),
                // (vardec int x)
                () -> assertEquals(new VardecStmt(new IntType(), new Variable("x")),
                                   new VardecStmt(new IntType(), new Variable("x"))),
                // (assign x 5)
                () -> assertEquals(new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(5)),
                                   new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(5))),
                // (while (< x y) (assign x (+ x 1)))
                () -> assertEquals(new WhileStmt(new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new LHSExpr(new VarLHS(new Variable("y")))), 
                                                 new AssignStmt(new VarLHS(new Variable("x")),
                                                                new BinOpExpr(new PlusOp(), new LHSExpr(new VarLHS(new Variable("x"))), new NumberLiteralExpr(1)))),
                                   new WhileStmt(new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new LHSExpr(new VarLHS(new Variable("y")))), 
                                                 new AssignStmt(new VarLHS(new Variable("x")),
                                                                new BinOpExpr(new PlusOp(), new LHSExpr(new VarLHS(new Variable("x"))), new NumberLiteralExpr(1))))),
                // (if (< x y) (return x) (return y))
                () -> assertEquals(new IfStmt(new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new LHSExpr(new VarLHS(new Variable("y")))),
                                                  new ReturnStmt(new LHSExpr(new VarLHS(new Variable("x")))),
                                                  new ReturnStmt(new LHSExpr(new VarLHS(new Variable("y"))))),
                                   new IfStmt(new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new LHSExpr(new VarLHS(new Variable("y")))),
                                                  new ReturnStmt(new LHSExpr(new VarLHS(new Variable("x")))),
                                                  new ReturnStmt(new LHSExpr(new VarLHS(new Variable("y")))))),
                () -> assertEquals(new ReturnStmt(), new ReturnStmt()),
                // (block (vardec int x) (vardec int y))
                () -> assertEquals(new BlockStmt(new ArrayList<Stmt>(Arrays.asList(
                                                     new VardecStmt(new IntType(), new Variable("x")),
                                                     new VardecStmt(new IntType(), new Variable("y"))))),
                                   new BlockStmt(new ArrayList<Stmt>(Arrays.asList(
                                                     new VardecStmt(new IntType(), new Variable("x")),
                                                     new VardecStmt(new IntType(), new Variable("y")))))),
                () -> assertEquals(new PrintLnStmt(new BooleanLiteralExpr(true)),
                                   new PrintLnStmt(new BooleanLiteralExpr(true))),
                // (stmt (call foo 3))
                () -> assertEquals(new ExprStmt(new CallExpr(new Variable("foo"),
                                                             new ArrayList<Expr>(Arrays.asList(
                                                                 new NumberLiteralExpr(3))))),
                                   new ExprStmt(new CallExpr(new Variable("foo"),
                                                             new ArrayList<Expr>(Arrays.asList(
                                                                 new NumberLiteralExpr(3)))))),
                () -> assertEquals(new PlusOp(), new PlusOp()),
                () -> assertEquals(new MinusOp(), new MinusOp()),
                () -> assertEquals(new StarOp(), new StarOp()),
                () -> assertEquals(new DivideOp(), new DivideOp()),
                () -> assertEquals(new LessThanOp(), new LessThanOp()),
                () -> assertEquals(new DoubleEqualOp(), new DoubleEqualOp()),
                () -> assertEquals(new NotEqualOp(), new NotEqualOp()),
                () -> assertEquals(new NumberLiteralExpr(1), new NumberLiteralExpr(1)),
                () -> assertEquals(new BooleanLiteralExpr(true), new BooleanLiteralExpr(true)),
                () -> assertEquals(new NullExpr(), new NullExpr()),
                () -> assertEquals(new LHSExpr(new VarLHS(new Variable("x"))), new LHSExpr(new VarLHS(new Variable("x")))),
                () -> assertEquals(new AddressExpr(new VarLHS(new Variable("root"))), new AddressExpr(new VarLHS(new Variable("root")))),
                () -> assertEquals(new DerefExpr(new LHSExpr(new VarLHS(new Variable("p")))), new DerefExpr(new LHSExpr(new VarLHS(new Variable("p"))))),
                () -> assertEquals(new BinOpExpr(new DoubleEqualOp(), new NumberLiteralExpr(3), new NumberLiteralExpr(3)),
                                   new BinOpExpr(new DoubleEqualOp(), new NumberLiteralExpr(3), new NumberLiteralExpr(3))),
                () -> assertEquals(new CallExpr(new Variable("foo"),
                                                new ArrayList<Expr>(Arrays.asList(
                                                    new NumberLiteralExpr(3)))),
                                   new CallExpr(new Variable("foo"),
                                                new ArrayList<Expr>(Arrays.asList(
                                                    new NumberLiteralExpr(3))))),
                () -> assertEquals(new Param(new StructType(new Variable("Node")), new Variable("leftChild")),
                                   new Param(new StructType(new Variable("Node")), new Variable("leftChild"))),
                // (struct Node (int value) (Node child))
                () -> assertEquals(new StructDef(
                                       new Variable("Node"),
                                       new ArrayList<Param>(Arrays.asList(
                                           new Param(new IntType(), new Variable("value")),
                                           new Param(new StructType(new Variable("Node")), new Variable("child"))))),
                                   new StructDef(
                                       new Variable("Node"),
                                       new ArrayList<Param>(Arrays.asList(
                                           new Param(new IntType(), new Variable("value")),
                                           new Param(new StructType(new Variable("Node")), new Variable("child")))))),
                () -> assertEquals(new Program(new ArrayList<StructDef>(),
                                               new ArrayList<FuncDef>(),
                                               new ArrayList<Stmt>()),
                                   new Program(new ArrayList<StructDef>(),
                                               new ArrayList<FuncDef>(),
                                               new ArrayList<Stmt>()))
            );
    }
    
    
    
    @Test
    public void emptyProgramTest() throws ParseException {
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>());
        
        Program received = new Parser(new Token[0]).parse();
        
        assertEquals(expected, received);
    }

    @Test
    public void parseStructDefTest() throws ParseException{
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
    public void parseStructDefWithTokenizerTest() throws TokenizerException, ParseException {
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
    public void parseMultipleStructDefTest() throws ParseException {
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
    public void parseMultipleStructDefWithTokenizerTest() throws TokenizerException, ParseException {
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
    public void parseVardecStmtTest() throws ParseException {
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
    public void parseIfStmtTest() throws ParseException {
        
        // (vardec int x)
        // (assign x 15)
        // (if (< x 20)
        //     (assign x (+ x 1))
        // )
        
        Token[] tokens = new Token[] {
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("x"),
            new NumberToken(15),
            new RightParenToken(),
            
            new LeftParenToken(),
            new IfToken(),
            new LeftParenToken(),
            new LessThanToken(),
            new IdentifierToken("x"),
            new NumberToken(20),
            new RightParenToken(),
            new LeftParenToken(),
            new AssignToken(),
            new IdentifierToken("x"),
            new LeftParenToken(),
            new PlusToken(),
            new IdentifierToken("x"),
            new NumberToken(1),
            new RightParenToken(),
            new RightParenToken(),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new VardecStmt(new IntType(), new Variable("x")),
                                           new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(15)),
                                           new IfStmt(
                                               new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new NumberLiteralExpr(20)),
                                               new AssignStmt(
                                                   new VarLHS(new Variable("x")),
                                                   new BinOpExpr(
                                                       new PlusOp(),
                                                       new LHSExpr(new VarLHS(new Variable("x"))),
                                                       new NumberLiteralExpr(1)))))));
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
    }
    
    @Test
    public void parseIfStmtWithTokenizerTest() throws TokenizerException, ParseException {
        
        // (vardec int x)
        // (assign x 15)
        // (if (< x 20)
        //     (assign x (+ x 1)))
        
        Token[] tokens = new Tokenizer("(vardec int x)"
                                     + "(assign x 15)"
                                     + "(if (< x 20)"
                                     + "    (assign x (+ x 1)))").tokenize();
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new VardecStmt(new IntType(), new Variable("x")),
                                           new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(15)),
                                           new IfStmt(
                                               new BinOpExpr(new LessThanOp(), new LHSExpr(new VarLHS(new Variable("x"))), new NumberLiteralExpr(20)),
                                               new AssignStmt(
                                                   new VarLHS(new Variable("x")),
                                                   new BinOpExpr(
                                                       new PlusOp(),
                                                       new LHSExpr(new VarLHS(new Variable("x"))),
                                                       new NumberLiteralExpr(1)))))));
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
    }
    
    @Test
    public void parseIfElseStmtTest() throws ParseException {
        
        // (if (< x 10)
        //     (stmt (call foo x))
        //     (stmt (call bar x)))
        
        Token[] tokens = new Token[] {
            new LeftParenToken(),
            new IfToken(),
            new LeftParenToken(),
            new LessThanToken(),
            new IdentifierToken("x"),
            new NumberToken(10),
            new RightParenToken(),
            new LeftParenToken(),
            new StmtToken(),
            new LeftParenToken(),
            new CallToken(),
            new IdentifierToken("foo"),
            new IdentifierToken("x"),
            new RightParenToken(),
            new RightParenToken(),
            new LeftParenToken(),
            new StmtToken(),
            new LeftParenToken(),
            new CallToken(),
            new IdentifierToken("bar"),
            new IdentifierToken("x"),
            new RightParenToken(),
            new RightParenToken(),
            new RightParenToken()
        };
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new IfStmt(
                                               new BinOpExpr(
                                                   new LessThanOp(),
                                                   new LHSExpr(new VarLHS(new Variable("x"))),
                                                   new NumberLiteralExpr(10)),
                                               new ExprStmt(new CallExpr(
                                                   new Variable("foo"),
                                                   new ArrayList<Expr>(Arrays.asList(
                                                       new LHSExpr(new VarLHS(new Variable("x"))))))),
                                               new ExprStmt(new CallExpr(
                                                       new Variable("bar"),
                                                       new ArrayList<Expr>(Arrays.asList(
                                                           new LHSExpr(new VarLHS(new Variable("x")))))))))));
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }
    
    @Test
    public void parseIfElseStmtWithTokenizerTest() throws TokenizerException, ParseException {
        
        // (if (< x 10)
        //     (stmt (call foo x))
        //     (stmt (call bar x)))
        
        Token[] tokens = new Tokenizer("(if (< x 10) (stmt (call foo x)) (stmt (call bar x)))").tokenize();
        
        Program expected = new Program(new ArrayList<StructDef>(),
                                       new ArrayList<FuncDef>(),
                                       new ArrayList<Stmt>(Arrays.asList(
                                           new IfStmt(
                                               new BinOpExpr(
                                                   new LessThanOp(),
                                                   new LHSExpr(new VarLHS(new Variable("x"))),
                                                   new NumberLiteralExpr(10)),
                                               new ExprStmt(new CallExpr(
                                                   new Variable("foo"),
                                                   new ArrayList<Expr>(Arrays.asList(
                                                       new LHSExpr(new VarLHS(new Variable("x"))))))),
                                               new ExprStmt(new CallExpr(
                                                       new Variable("bar"),
                                                       new ArrayList<Expr>(Arrays.asList(
                                                           new LHSExpr(new VarLHS(new Variable("x")))))))))));
        Program received = new Parser(tokens).parse();
        assertEquals(expected, received);
        
    }

    @Test
    public void parseExampleProgram() throws TokenizerException, ParseException {
        // Length of linked list example
        Token[] tokens = new Tokenizer(" (struct Node"
                                     + "     (int value)"
                                     + "     ((* Node) next))"
                                     + " (func length (((* Node) list)) int"
                                     + "     (vardec int retval)"
                                     + "     (assign retval 0)"
                                     + "     (while (!= list null)"
                                     + "         (block "
                                     + "             (assign retval (+ retval 1))"
                                     + "             (assign list (. (* list) next))))"
                                     + "     (return retval))"
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
