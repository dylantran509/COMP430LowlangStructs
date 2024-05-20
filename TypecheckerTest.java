package lowlangStructs.typechecker;
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

import java.util.*;

public class TypecheckerTest {

    @Test
    // Test typechecking for VariableExpr
    public void testTypecheckExpr_VariableExpr() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();
        Variable var = new Variable();
        typeEnv.put(var, new IntType());

        VariableExpr expr = new VariableExpr();
        expr.var = var;

        Type result = typechecker.typecheckExpr(expr, typeEnv);

        assertEquals(new IntType(), result);
    }

    @Test
    // Test typechecking for IntExpr
    public void testTypecheckExpr_IntExpr() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();

        IntExpr expr = new IntExpr();

        Type result = typechecker.typecheckExpr(expr, typeEnv);

        assertEquals(new IntType(), result);
    }

    @Test
    // Test typechecking for BoolExpr
    public void testTypecheckExpr_BoolExpr() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();

        BoolExpr expr = new BoolExpr();

        Type result = typechecker.typecheckExpr(expr, typeEnv);

        assertEquals(new BoolType(), result);
    }

    // Class definitions for IntExpr, BoolExpr, and VariableExpr
    static class IntExpr implements Expr {}
    static class BoolExpr implements Expr {}
    static class VariableExpr implements Expr {
        Variable var;
    }

    // Unary operator definitions for UnOpExpr
    enum UnOp {
        NEG, NOT
    }

    static class UnOpExpr implements Expr {
        Expr expr;
        UnOp op;
    }

    // VariableLHS definition
    static class VariableLHS implements LHS {
        Variable var;
    }

    @Test
    // Test typechecking for addition operation
    public void testTypecheckBinOpExpr_Addition() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();

        IntExpr lhs = new IntExpr();
        IntExpr rhs = new IntExpr();
        BinOpExpr expr = new BinOpExpr();
        expr.lhs = lhs;
        expr.rhs = rhs;
        expr.op = BinOpExpr.Op.ADD;

        Type result = typechecker.typecheckBinOpExpr(expr, typeEnv);

        assertEquals(new IntType(), result);
    }

    @Test
    // Test typechecking for negation operation
    public void testTypecheckUnOpExpr_Negation() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();

        IntExpr operand = new IntExpr();
        UnOpExpr expr = new UnOpExpr();
        expr.expr = operand;
        expr.op = UnOpExpr.Op.NEG;

        Type result = typechecker.typecheckUnOpExpr(expr, typeEnv);

        assertEquals(new IntType(), result);
    }

    @Test
    // Test typechecking for VariableLHS
    public void testTypecheckLHS_VariableLHS() {
        Typechecker typechecker = new Typechecker(new HashMap<>(), new HashMap<>());
        Map<Variable, Type> typeEnv = new HashMap<>();
        Variable var = new Variable();
        typeEnv.put(var, new IntType());

        VariableLHS lhs = new VariableLHS();
        lhs.var = var;

        Type result = typechecker.typecheckLHS(lhs, typeEnv);

        assertEquals(new IntType(), result);
    }
}