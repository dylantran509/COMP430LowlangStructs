package lowlangStructs.typechecker;
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

public class TypecheckerTest {

    @Test
    public void typecheckNodeStructTest() throws TypecheckerException {
        
        // (struct Node
        //     (int value)
        //     ((* Node) next))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                         new StructDef(
                                             new Variable("Node"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("value")),
                                                 new Param(
                                                     new PointerType(new StructType(new Variable("Node"))),
                                                     new Variable("next"))))))),
                                     new ArrayList<FuncDef>(),
                                     new ArrayList<Stmt>());
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
    }
    
    @Test
    public void typecheckNodeStructWithTokenizerAndParserTest() throws TokenizerException,
                                                              ParseException,
                                                              TypecheckerException {
        Token[] tokens = new Tokenizer("(struct Node (int value) ((* Node) next))").tokenize();
        Program myPrgm = new Parser(tokens).parse();
        Typechecker wellTypedPrgm = new Typechecker(myPrgm);
        wellTypedPrgm.typecheckProgram();
    }
    
    @Test
    public void typecheckMultipleStructsTest() throws TypecheckerException {
        
        // (struct XCoord
        //     (int x))
        // (struct YCoord
        //     (int y))
        // (struct Coordinate
        //     (XCoord x)
        //     (YCoord y))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                         new StructDef(
                                             new Variable("XCoord"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("x"))))),
                                         new StructDef(
                                             new Variable("YCoord"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("y"))))),
                                         new StructDef(
                                             new Variable("Coordinate"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new StructType(new Variable("XCoord")), new Variable("x")),
                                                 new Param(new StructType(new Variable("YCoord")), new Variable("y"))))))),
                                     new ArrayList<FuncDef>(),
                                     new ArrayList<Stmt>());
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
    }
    
    @Test
    public void typecheckMultipleStructsWithTokenizerAndParserTest() throws TokenizerException,
                                                              ParseException,
                                                              TypecheckerException {
        Token[] tokens = new Tokenizer("(struct XCoord (int x)) (struct YCoord (int y)) (struct Coordinate (XCoord x) (YCoord y))").tokenize();
        Program myPrgm = new Parser(tokens).parse();
        Typechecker wellTypedPrgm = new Typechecker(myPrgm);
        wellTypedPrgm.typecheckProgram();
    }
    
    @Test
    public void typecheckSimpleFuncDefTest() throws TypecheckerException {
        
        // (func add ((int a) (int b)) int (return (+ a b)))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(),
                                     new ArrayList<FuncDef>(Arrays.asList(
                                         new FuncDef(
                                             new Variable("add"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("a")),
                                                 new Param(new IntType(), new Variable("b"))
                                             )),
                                             new IntType(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new ReturnStmt(
                                                     new BinOpExpr(
                                                         new PlusOp(),
                                                         new LHSExpr(new VarLHS(new Variable("a"))),
                                                         new LHSExpr(new VarLHS(new Variable("b")))
                                                     )
                                                 )
                                             ))
                                         )
                                     )),
                                     new ArrayList<Stmt>());
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
    }
    
    @Test
    public void typecheckSimpleFuncDefWithTokenizerAndParserTest() throws TokenizerException,
                                                                          ParseException,
                                                                          TypecheckerException {
        
        Token[] tokens = new Tokenizer("(func add ((int a) (int b)) int (return (+ a b)))").tokenize();
        Program myPrgm = new Parser(tokens).parse();
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
    }
    
    @Test
    public void typecheckFibonacciFuncTest() throws TypecheckerException {
        
        // (func fibonacci (int n) int
        //     (if (< n 2)
        //         (return n) 
        //         (return (+ (call fibonacci (- n 1)) (call fibonacci (- n 2)))))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(),
                                     new ArrayList<FuncDef>(Arrays.asList(
                                         new FuncDef(
                                             new Variable("fibonacci"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("n")))),
                                             new IntType(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new IfStmt(
                                                     new BinOpExpr(
                                                         new LessThanOp(),
                                                         new LHSExpr(new VarLHS(new Variable("n"))),
                                                         new NumberLiteralExpr(2)),
                                                     new ReturnStmt(
                                                         new LHSExpr(new VarLHS(new Variable("n")))),
                                                     new ReturnStmt(
                                                         new BinOpExpr(
                                                             new PlusOp(),
                                                             new CallExpr(
                                                                 new Variable("fibonacci"),
                                                                 new ArrayList<Expr>(Arrays.asList(
                                                                     new BinOpExpr(
                                                                         new MinusOp(),
                                                                         new LHSExpr(new VarLHS(new Variable("n"))),
                                                                         new NumberLiteralExpr(1))))),
                                                             new CallExpr(
                                                                     new Variable("fibonacci"),
                                                                     new ArrayList<Expr>(Arrays.asList(
                                                                         new BinOpExpr(
                                                                             new MinusOp(),
                                                                             new LHSExpr(new VarLHS(new Variable("n"))),
                                                                             new NumberLiteralExpr(2))))))))))))),
                                     new ArrayList<Stmt>());
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
    }
    
    @Test
    public void typecheckFibonacciFuncWithTokenizerAndParserTest() throws TokenizerException,
                                                                          ParseException,
                                                                          TypecheckerException {
        
        Token[] tokens = new Tokenizer("(func fibonacci ((int n)) int"
                                     + "    (if (< n 2)"
                                     + "        (return n)"
                                     + "        (return (+ (call fibonacci (- n 1)) (call fibonacci (- n 2))))))").tokenize();
        Program myPrgm = new Parser(tokens).parse();
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
    }
    
    @Test
    public void typecheckStructFieldAccessTest() throws TypecheckerException {
        
        // (struct Node
        //     (int value)
        //     (Node leftChild)
        //     (Node rightChild))
        // (vardec Node root)
        // (vardec Node left)
        // (vardec Node right)
        // (assign (. root value) 5)
        // (assign (. root leftChild) left)
        // (assign (. root rightChild) right)
        
        Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                         new StructDef(
                                             new Variable("Node"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("value")),
                                                 new Param(new StructType(new Variable("Node")), new Variable("leftChild")),
                                                 new Param(new StructType(new Variable("Node")), new Variable("rightChild"))))))),
                                     new ArrayList<FuncDef>(),
                                     new ArrayList<Stmt>(new ArrayList<Stmt>(Arrays.asList(
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("root")),
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("left")),
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("right")),
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("root")), new Variable("value")),
                                                        new NumberLiteralExpr(5)),
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("root")), new Variable("leftChild")),
                                                        new LHSExpr(new VarLHS(new Variable("left")))),
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("root")), new Variable("rightChild")),
                                                        new LHSExpr(new VarLHS(new Variable("right"))))))));
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
    }
    
    @Test
    public void typecheckStructFieldAccessWithTokenizerAndParserTest() throws TokenizerException,
                                                                              ParseException,
                                                                              TypecheckerException {
        
        // (struct Node
        //     (int value)
        //     (Node leftChild)
        //     (Node rightChild))
        // (vardec Node root)
        // (vardec Node left)
        // (vardec Node right)
        // (assign (. root value) 5)
        // (assign (. root leftChild) left)
        // (assign (. root rightChild) right)
        
        Token[] tokens = new Tokenizer("(struct Node"
                                     + "    (int value)"
                                     + "    (Node leftChild)"
                                     + "    (Node rightChild))"
                                     + "(vardec Node root)"
                                     + "(vardec Node left)"
                                     + "(vardec Node right)"
                                     + "(assign (. root value) 5)"
                                     + "(assign (. root leftChild) left)"
                                     + "(assign (. root rightChild) right)").tokenize();
        Program myPrgm = new Parser(tokens).parse();

        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
    }
    
    @Test
    public void typecheckExampleProgramTest() throws TypecheckerException {
        
        // (struct Node
        //     (int value)
        //     ((* Node) next))
        // (func length (((* Node) list)) int
        //     (vardec int retval)
        //     (assign retval 0)
        //     (while (!= list null)
        //         (block
        //             (assign retval (+ retval 1))
        //             (assign list (. (* list) next))))
        //     (return retval))
        // (vardec Node first)
        // (vardec Node second)
        // (vardec Node third)
        // (assign (. first value) 1)
        // (assign (. first next) (& second))
        // (assign (. second value) 2)
        // (assign (. second next) (& third))
        // (assign (. third value) 3)
        // (assign (. third next) null)
        // (println (call length (& first)))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                         new StructDef(
                                             new Variable("Node"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("value")),
                                                 new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("next"))
                                             ))
                                         )
                                     )),
                                     new ArrayList<FuncDef>(Arrays.asList(
                                         new FuncDef(
                                             new Variable("length"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new PointerType(new StructType(new Variable("Node"))), new Variable("list"))
                                             )),
                                             new IntType(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new IntType(), new Variable("retval")),
                                                 new AssignStmt(new VarLHS(new Variable("retval")), new NumberLiteralExpr(0)),
                                                 new WhileStmt(
                                                     new BinOpExpr(
                                                         new NotEqualOp(),
                                                         new LHSExpr(new VarLHS(new Variable("list"))),
                                                         new NullExpr()
                                                     ),
                                                     new BlockStmt(new ArrayList<Stmt>(Arrays.asList(
                                                         new AssignStmt(
                                                             new VarLHS(new Variable("retval")),
                                                             new BinOpExpr(
                                                                 new PlusOp(),
                                                                 new LHSExpr(new VarLHS(new Variable("retval"))),
                                                                 new NumberLiteralExpr(1)
                                                             )
                                                         ),
                                                         new AssignStmt(
                                                             new VarLHS(new Variable("list")),
                                                             new LHSExpr(new FieldLHS(
                                                                 new DerefLHS(new VarLHS(new Variable("list"))),
                                                                 new Variable("next")
                                                             ))
                                                         )
                                                     )))
                                                 ),
                                                 new ReturnStmt(new LHSExpr(new VarLHS(new Variable("retval"))))
                                             ))
                                         )
                                     )),
                                     new ArrayList<Stmt>(Arrays.asList(
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("first")),
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("second")),
                                         new VardecStmt(new StructType(new Variable("Node")), new Variable("third")),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("first")),
                                                        new Variable("value")), new NumberLiteralExpr(1)),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("first")),
                                                        new Variable("next")), new AddressExpr(new VarLHS(new Variable("second")))),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("second")),
                                                        new Variable("value")), new NumberLiteralExpr(2)),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("second")),
                                                        new Variable("next")), new AddressExpr(new VarLHS(new Variable("third")))),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("third")),
                                                        new Variable("value")), new NumberLiteralExpr(3)),
                                         
                                         new AssignStmt(new FieldLHS(new VarLHS(new Variable("third")),
                                                        new Variable("next")), new NullExpr()),
                                         
                                         new PrintLnStmt(new CallExpr(
                                                             new Variable("length"),
                                                             new ArrayList<Expr>(Arrays.asList(
                                                                 new AddressExpr(new VarLHS(new Variable("first"))))))))));
        
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
        
    }
    
    @Test
    public void typecheckExampleProgramWithTokenizerAndParserTest() throws TokenizerException,
                                                                           ParseException,
                                                                           TypecheckerException {
        
        // (struct Node
        //     (int value)
        //     ((* Node) next))
        // (func length (((* Node) list)) int
        //     (vardec int retval)
        //     (assign retval 0)
        //     (while (!= list null)
        //         (block
        //             (assign retval (+ retval 1))
        //             (assign list (. (* list) next))))
        //     (return retval))
        // (vardec Node first)
        // (vardec Node second)
        // (vardec Node third)
        // (assign (. first value) 1)
        // (assign (. first next) (& second))
        // (assign (. second value) 2)
        // (assign (. second next) (& third))
        // (assign (. third value) 3)
        // (assign (. third next) null)
        // (println (call length (& first)))
        
        Token[] tokens = new Tokenizer("(struct Node"
                                     + "    (int value)"
                                     + "    ((* Node) next))"
                                     + "(func length (((* Node) list)) int"
                                     + "    (vardec int retval)"
                                     + "    (assign retval 0)"
                                     + "    (while (!= list null)"
                                     + "        (block"
                                     + "            (assign retval (+ retval 1))"
                                     + "            (assign list (. (* list) next))))"
                                     + "    (return retval))"
                                     + "(vardec Node first)"
                                     + "(vardec Node second)"
                                     + "(vardec Node third)"
                                     + "(assign (. first value) 1)"
                                     + "(assign (. first next) (& second))"
                                     + "(assign (. second value) 2)"
                                     + "(assign (. second next) (& third))"
                                     + "(assign (. third value) 3)"
                                     + "(assign (. third next) null)"
                                     + "(println (call length (& first)))").tokenize();
        Program myPrgm = new Parser(tokens).parse();
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
        
    }
    
}
