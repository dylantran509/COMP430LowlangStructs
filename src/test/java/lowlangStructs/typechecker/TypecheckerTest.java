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
    
    @Test
    public void typecheckFuncCallAsExprStmt() throws TypecheckerException {
        
        // (func increment ((int x)) void
        //     (assign x (+ x 1)))
        // (vardec int x)
        // (assign x 8)
        // (stmt (call increment x))
        
        Program myPrgm = new Program(new ArrayList<StructDef>(),
                                     new ArrayList<FuncDef>(Arrays.asList(
                                         new FuncDef(
                                             new Variable("increment"),
                                             new ArrayList<Param>(Arrays.asList(
                                                 new Param(new IntType(), new Variable("x")))),
                                             new VoidType(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new AssignStmt(
                                                     new VarLHS(new Variable("x")),
                                                     new BinOpExpr(
                                                         new PlusOp(),
                                                         new LHSExpr(new VarLHS(new Variable("x"))),
                                                         new NumberLiteralExpr(1)))))))),
                                     new ArrayList<Stmt>(Arrays.asList(
                                         new VardecStmt(new IntType(), new Variable("x")),
                                         new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(8)),
                                         new ExprStmt(new CallExpr(
                                                         new Variable("increment"),
                                                         new ArrayList<Expr>(Arrays.asList(
                                                             new LHSExpr(new VarLHS(new Variable("x"))))))))));
        
        Typechecker TC = new Typechecker(myPrgm);
        TC.typecheckProgram();
        
        
    }
    
    @Test
    public void typecheckFailDuplicateStructTest() throws TypecheckerException {
        // (struct Node
        //     (int value)
        //     (Node child))
        // (struct Node
        //     (int value)
        //     (Node child))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                                 new StructDef(
                                                     new Variable("Node"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("value")),
                                                         new Param(new StructType(new Variable("Node")), new Variable("leftChild"))))),
                                                 new StructDef(
                                                     new Variable("Node"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("value")),
                                                         new Param(new StructType(new Variable("Node")), new Variable("rightChild"))))))),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>());
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailDuplicateStructTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailDuplicateStructFieldTest() throws TypecheckerException {
        // (struct Node
        //     (int value)
        //     (int value))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                                 new StructDef(
                                                     new Variable("Node"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("value")),
                                                         new Param(new IntType(), new Variable("value"))))))),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>());
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailDuplicateStructFieldTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailUnknownTypeTest() throws TypecheckerException {
        // (struct Node
        //     (int value)
        //     (int value))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new StructType(new Variable("Node")), new Variable("root")))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailUnknownTypeTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailDuplicateFuncTest() throws TypecheckerException {
        // (func foo () void)
        // (func foo () void)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(Arrays.asList(
                                                 new FuncDef(
                                                     new Variable("foo"),
                                                     new ArrayList<Param>(),
                                                     new VoidType(),
                                                     new ArrayList<Stmt>()),
                                                 new FuncDef(
                                                     new Variable("foo"),
                                                     new ArrayList<Param>(),
                                                     new VoidType(),
                                                     new ArrayList<Stmt>()))),
                                             new ArrayList<Stmt>());
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailDuplicateFuncTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailDuplicateVariableTest() throws TypecheckerException {
        // (vardec int x)
        // (vardec int x)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new IntType(), new Variable("x")),
                                                 new VardecStmt(new IntType(), new Variable("x")))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailDuplicateVariableTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailDuplicateVariableWithinFuncDefTest() throws TypecheckerException {
        // (func foo ((int x)) void
        //     (vardec int x))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(Arrays.asList(
                                                 new FuncDef(
                                                     new Variable("foo"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("x")))),
                                                     new VoidType(),
                                                     new ArrayList<Stmt>(Arrays.asList(
                                                         new VardecStmt(new IntType(), new Variable("x"))))))),
                                             new ArrayList<Stmt>());
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailDuplicateVariableWithinFuncDefTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailAssignToNonExistantVariableTest() throws TypecheckerException {
        // (assign x 8)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(8)))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailAssignToNonExistantVariableTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailAssignValueToNullTest() throws TypecheckerException {
        // (assign null 8)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new AssignStmt(null, new NumberLiteralExpr(8)))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailAssignValueToNullTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailAssignNullToNonPointerTypeTest() throws TypecheckerException {
        // (vardec (* int) x)
        // (assign x null)
        // (vardec int y)
        // (assign y null)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new PointerType(new IntType()), new Variable("x")),
                                                 new AssignStmt(new VarLHS(new Variable("x")), new NullExpr()),
                                                 new VardecStmt(new IntType(), new Variable("y")),
                                                 new AssignStmt(new VarLHS(new Variable("y")), new NullExpr()))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailAssignNullToNonPointerTypeTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailAssignTypeMismatchTest() throws TypecheckerException {
        // (vardec (* int) x)
        // (assign x 8)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new PointerType(new IntType()), new Variable("x")),
                                                 new AssignStmt(new VarLHS(new Variable("x")), new NumberLiteralExpr(8)))));
                
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
                
            });
        
        System.out.println("typecheckFailAssignTypeMismatchTest:\n" +
                           myException.getMessage()
                           + "\n");
    }
    
    @Test
    public void typecheckFailWhileGuardNotBoolTypeTest() throws TypecheckerException {
        // (while (1)
        //     (println 1))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new WhileStmt(
                                                     new NumberLiteralExpr(1),
                                                     new PrintLnStmt(new NumberLiteralExpr(1))))));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailWhileGuardNotBoolTypeTest:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailIfGuardNotBoolTypeTest() throws TypecheckerException {
        // (if 1
        //     (println 1))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new IfStmt(
                                                     new NumberLiteralExpr(1),
                                                     new PrintLnStmt(new NumberLiteralExpr(1))))));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailIfGuardNotBoolTypeTest:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailReturnTypeMismatchTest() throws TypecheckerException {
        // (func foo () int
        //     (return 5))
        // (func bar () void
        //     (return 5))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(Arrays.asList(
                                                 new FuncDef(
                                                     new Variable("foo"),
                                                     new ArrayList<Param>(),
                                                     new IntType(),
                                                     new ArrayList<Stmt>(Arrays.asList(
                                                         new ReturnStmt(new NumberLiteralExpr(5))))),
                                                 new FuncDef(
                                                     new Variable("bar"),
                                                     new ArrayList<Param>(),
                                                     new VoidType(),
                                                     new ArrayList<Stmt>(Arrays.asList(
                                                         new ReturnStmt(new NumberLiteralExpr(5))))))),
                                             new ArrayList<Stmt>());
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailReturnTypeMismatchTest:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailReturnOutsideOfFuncTest() throws TypecheckerException {
        // (return)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new ReturnStmt())));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailReturnOutsideOfFuncTest:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailAccessNonExistantStructFieldTest() throws TypecheckerException {
        // (struct Node
        //     (int value)
        //     (Node child))
        // (vardec Node root)
        // (vardec Node left)
        // (assign (. root leftChild) left)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(Arrays.asList(
                                                 new StructDef(
                                                     new Variable("Node"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("value")),
                                                         new Param(new StructType(new Variable("Node")), new Variable("child"))))))),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new StructType(new Variable("Node")), new Variable("root")),
                                                 new VardecStmt(new StructType(new Variable("Node")), new Variable("left")),
                                                 new AssignStmt(new FieldLHS(new VarLHS(new Variable("root")), new Variable("leftChild")), new LHSExpr(new VarLHS(new Variable("left")))))));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailAccessNonExistantStructField:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailNonExistantStructTest() throws TypecheckerException {
        // (vardec Node root)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new StructType(new Variable("Node")), new Variable("root")))));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailNonExistantStruct:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailAccessFieldOfNonStructTest() throws TypecheckerException {
        // (vardec int x)
        // (assign (. x value) 5)
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>(Arrays.asList(
                                                 new VardecStmt(new IntType(), new Variable("x")),
                                                 new AssignStmt(new FieldLHS(new VarLHS(new Variable("x")), new Variable("value")), new NumberLiteralExpr(5)))));
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFailAccessFieldOfNonStructTest:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFailFuncCallArgumentCountMismatch() throws TypecheckerException {
        // (foo ((int x) (int y)) void)
        // (stmt (call foo 5))
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(Arrays.asList(
                                                 new FuncDef(
                                                     new Variable("foo"),
                                                     new ArrayList<Param>(Arrays.asList(
                                                         new Param(new IntType(), new Variable("x")),
                                                         new Param(new IntType(), new Variable("y")))),
                                                     new VoidType(),
                                                     new ArrayList<Stmt>(Arrays.asList(
                                                         new ExprStmt(new CallExpr(
                                                             new Variable("foo"),
                                                             new ArrayList<Expr>(Arrays.asList(
                                                                 new NumberLiteralExpr(5)))))))))),
                                             new ArrayList<Stmt>());
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFail:\n" +
                myException.getMessage()
                + "\n");
    }
    
    @Test
    public void typecheckFail() throws TypecheckerException {
        // 
        Exception myException = assertThrows(TypecheckerException.class,
            () -> {
                Program myPrgm = new Program(new ArrayList<StructDef>(),
                                             new ArrayList<FuncDef>(),
                                             new ArrayList<Stmt>());
                Typechecker TC = new Typechecker(myPrgm);
                TC.typecheckProgram();
            });
        
        System.out.println("typecheckFail:\n" +
                myException.getMessage()
                + "\n");
    }
    
}
