package lowlangStructs.typechecker;
import lowlangStructs.parser.*;

import java.util.*;

// Create Typechecker class with Maps for function signatures, struct signatures, and type environment as well as a Type field for the current type returned
public class Typechecker {
    private final Map<Variable, List<Param>> funcSignatures;
    private final Map<Variable, List<Param>> structSignatures;
    private final Map<Variable, Type> typeEnv;
    private Type currReturnType;

    // Constructor for Typechecker class accounting for function signatures, struct signatures, and type environment
    public Typechecker(Map<Variable, List<Param>> funcSignatures, Map<Variable, List<Param>> structSignatures) {
        this.funcSignatures = funcSignatures;
        this.structSignatures = structSignatures;
        this.typeEnv = new HashMap<>();
    }

    // Create copy of any Map as a HashMap
    public Map<Variable, Type> copyOfMap(Map<Variable, Type> map) {
        return new HashMap<>(map);
    }

    // Function to typecheck for statements
    public Map<Variable, Type> typecheckStmt(final Stmt stmt, final Map<Variable, Type> typeEnv) {
        if (stmt instanceof VardecStmt)
            return typecheckVardecStmt((VardecStmt) stmt, typeEnv);
        if (stmt instanceof AssignStmt)
            return typecheckAssignStmt((AssignStmt) stmt, typeEnv);
        if (stmt instanceof WhileStmt)
            return typecheckWhileStmt((WhileStmt) stmt, typeEnv);
        if (stmt instanceof IfStmt)
            return typecheckIfStmt((IfStmt) stmt, typeEnv);
        if (stmt instanceof ReturnStmt)
            return typecheckReturnStmt((ReturnStmt) stmt, typeEnv);
        if (stmt instanceof BlockStmt)
            return typecheckBlockStmt((BlockStmt) stmt, typeEnv);
        throw new RuntimeException("TypecheckerException. Unknown stmt: " + stmt.toString());
    }


    // Function to determine the type read by the typechecker
    public void typecheckType(final Type type) {
        if (type instanceof IntType) return;
        if (type instanceof BoolType) return;
        if (type instanceof StructType) {
            if (!structSignatures.containsKey(((StructType) type).structname))
                throw new RuntimeException("TypecheckerException. Unknown struct type: " + type.toString());
            return;
        }
        if (type instanceof PointerType) {
            typecheckType(((PointerType) type).type);
            return;
        }
        throw new RuntimeException("TypecheckerException. Unknown type: " + type.toString());
    }

    // Function to typecheck any variable declarations within statements
    public Map<Variable, Type> typecheckVardecStmt(final VardecStmt stmt, final Map<Variable, Type> typeEnv) {
        typecheckType(stmt.type);
        Map<Variable, Type> newMap = copyOfMap(typeEnv);
        newMap.put(stmt.var, stmt.type);
        return newMap;
    }

    // Function to typecheck assignment statements
    public Map<Variable, Type> typecheckAssignStmt(final AssignStmt stmt, final Map<Variable, Type> typeEnv) {
        Type lhsType = typecheckLHS(stmt.lhs, typeEnv);
        Type rhsType = typecheckExpr(stmt.rhs, typeEnv);
        if (lhsType == null || rhsType == null)
            throw new RuntimeException("TypecheckerException. Null type in assignment: " + stmt.toString());
        if (!lhsType.equals(rhsType))
            throw new RuntimeException("TypecheckerException. Type mismatch in assignment: " + stmt.toString());
        return typeEnv;
    }

    // Function to typecheck while statements
    public Map<Variable, Type> typecheckWhileStmt(final WhileStmt stmt, final Map<Variable, Type> typeEnv) {
        Type guardType = typecheckExpr(stmt.guard, typeEnv);
        if (!(guardType instanceof BoolType))
            throw new RuntimeException("TypecheckerException. Non-boolean guard in while: " + stmt.toString());
        typecheckStmt(stmt.body, typeEnv);
        return typeEnv;
    }

    // Function to typecheck if statements
    public Map<Variable, Type> typecheckIfStmt(final IfStmt stmt, final Map<Variable, Type> typeEnv) {
        Type guardType = typecheckExpr(stmt.guard, typeEnv);
        if (!(guardType instanceof BoolType))
            throw new RuntimeException("TypecheckerException. Non-boolean guard in if: " + stmt.toString());
        typecheckStmt(stmt.then, typeEnv);
        if (stmt.elze != null)
            typecheckStmt(stmt.elze, typeEnv);
        return typeEnv;
    }

    // Function to typecheck return statements
    public Map<Variable, Type> typecheckReturnStmt(final ReturnStmt stmt, final Map<Variable, Type> typeEnv) {
        Type returnType = typecheckExpr(stmt.expr, typeEnv);
        if (currReturnType == null)
            throw new RuntimeException("TypecheckerException. currReturnType is null: " + stmt.toString());
        else {
            if (returnType == null)
                if (currReturnType instanceof PointerType)
                    return typeEnv;
                else
                    throw new RuntimeException("TypecheckerException. Return type mismatch: " + stmt.toString());
            else if (currReturnType.equals(returnType))
                return typeEnv;
            else
                throw new RuntimeException("TypecheckerException. Return type mismatch: " + stmt.toString());
        }
    }

    // Function to typecheck block statements
    public Map<Variable, Type> typecheckBlockStmt(final BlockStmt stmt, final Map<Variable, Type> typeEnv) {
        Map<Variable, Type> newMap = copyOfMap(typeEnv);
        for (Stmt s : stmt.stmts)
            newMap = typecheckStmt(s, newMap);
        return typeEnv;
    }

    // Function to typecheck expressions
    public Type typecheckExpr(final Expr expr, final Map<Variable, Type> typeEnv) {
        if (expr instanceof VariableExpr)
            return typeEnv.get(((VariableExpr) expr).var);
        if (expr instanceof IntExpr)
            return new IntType();
        if (expr instanceof BoolExpr)
            return new BoolType();
        if (expr instanceof NullExpr)
            return null;
        if (expr instanceof BinOpExpr)
            return typecheckBinOpExpr((BinOpExpr) expr, typeEnv);
        if (expr instanceof UnOpExpr)
            return typecheckUnOpExpr((UnOpExpr) expr, typeEnv);
        if (expr instanceof FieldAccessExpr) {
            final FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expr;
            final Type LHS = typecheckExpr(fieldAccessExpr.expr, typeEnv);
            if (LHS instanceof StructType) {
                final Variable structname = ((StructType) LHS).structname;
                if (structSignatures.containsKey(structname)) {
                    final List<Param> structParams = structSignatures.get(structname);
                    for (Param param : structParams) {
                        if (param.var.equals(fieldAccessExpr.fieldname))
                            return param.type;
                    }
                }
            }
            throw new RuntimeException("TypecheckerException. Invalid FieldAccessExpr: " + expr.toString());
        }
        if (expr instanceof FuncCallExpr) {
            final FuncCallExpr funcCallExpr = (FuncCallExpr) expr;
            final Variable funcName = funcCallExpr.funcname;
            if (funcSignatures.containsKey(funcName)) {
                final List<Param> funcParams = funcSignatures.get(funcName);
                final List<Expr> callArgs = funcCallExpr.args;
                if (funcParams.size() == callArgs.size()) {
                    for (int i = 0; i < funcParams.size(); i++) {
                        final Type expectedType = funcParams.get(i).type;
                        final Type receivedType = typecheckExpr(callArgs.get(i), typeEnv);
                        if (expectedType == null)
                            if (!(receivedType instanceof PointerType))
                                throw new RuntimeException("TypecheckerException. Type mismatch in function call: " + expr.toString());
                            else
                                continue;
                        if (!(expectedType.equals(receivedType)))
                            throw new RuntimeException("TypecheckerException. Type mismatch in function call: " + expr.toString());
                    }
                    return typeEnv.get(funcName);
                }
                throw new RuntimeException("TypecheckerException. Incorrect number of arguments in function call: " + expr.toString());
            }
            throw new RuntimeException("TypecheckerException. Unknown function: " + expr.toString());
        }
        if (expr instanceof AllocExpr) {
            final AllocExpr allocExpr = (AllocExpr) expr;
            typecheckType(allocExpr.type);
            return new PointerType(allocExpr.type);
        }
        if (expr instanceof AllocArrayExpr) {
            final AllocArrayExpr allocArrayExpr = (AllocArrayExpr) expr;
            final Type type = typecheckExpr(allocArrayExpr.expr, typeEnv);
            if (type instanceof IntType) {
                typecheckType(allocArrayExpr.type);
                return new PointerType(allocArrayExpr.type);
            }
            throw new RuntimeException("TypecheckerException. Array allocation must have integer size: " + expr.toString());
        }
        if (expr instanceof ArrayAccessExpr) {
            final ArrayAccessExpr arrayAccessExpr = (ArrayAccessExpr) expr;
            final Type arrayType = typecheckExpr(arrayAccessExpr.base, typeEnv);
            final Type indexType = typecheckExpr(arrayAccessExpr.index, typeEnv);
            if (arrayType instanceof PointerType)
                if (indexType instanceof IntType)
                    return ((PointerType) arrayType).type;
                else
                    throw new RuntimeException("TypecheckerException. Array access with non-integer type: " + expr.toString());
            else
                throw new RuntimeException("TypecheckerException. Array access of non-array type: " + expr.toString());
        }
        throw new RuntimeException("TypecheckerException. Unknown expression: " + expr.toString());
    }

    // Function to typecheck binary operation expressions
    public Type typecheckBinOpExpr(final BinOpExpr expr, final Map<Variable, Type> typeEnv) {
        switch (expr.op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD: {
                final Type lhsType = typecheckExpr(expr.lhs, typeEnv);
                final Type rhsType = typecheckExpr(expr.rhs, typeEnv);
                if (!(lhsType instanceof IntType))
                    throw new RuntimeException("TypecheckerException. LHS of non-integer type in binary operation: " + expr.toString());
                if (!(rhsType instanceof IntType))
                    throw new RuntimeException("TypecheckerException. RHS of non-integer type in binary operation: " + expr.toString());
                return new IntType();
            }
            case LT:
            case LE:
            case GT:
            case GE: {
                final Type lhsType = typecheckExpr(expr.lhs, typeEnv);
                final Type rhsType = typecheckExpr(expr.rhs, typeEnv);
                if (!(lhsType instanceof IntType))
                    throw new RuntimeException("TypecheckerException. LHS of non-integer type in binary comparison: " + expr.toString());
                if (!(rhsType instanceof IntType))
                    throw new RuntimeException("TypecheckerException. RHS of non-integer type in binary comparison: " + expr.toString());
                return new BoolType();
            }
            case AND:
            case OR: {
                final Type lhsType = typecheckExpr(expr.lhs, typeEnv);
                final Type rhsType = typecheckExpr(expr.rhs, typeEnv);
                if (!(lhsType instanceof BoolType))
                    throw new RuntimeException("TypecheckerException. LHS of non-bool type in binary logical: " + expr.toString());
                if (!(rhsType instanceof BoolType))
                    throw new RuntimeException("TypecheckerException. RHS of non-bool type in binary logical: " + expr.toString());
                return new BoolType();
            }
            case EQ:
            case NE: {
                final Type lhsType = typecheckExpr(expr.lhs, typeEnv);
                final Type rhsType = typecheckExpr(expr.rhs, typeEnv);
                if (lhsType == null) {
                    if (rhsType == null)
                        return new BoolType();
                    if (rhsType instanceof PointerType)
                        return new BoolType();
                } else if (lhsType.equals(rhsType))
                    return new BoolType();
                throw new RuntimeException("TypecheckerException. Type mismatch in binary equality: " + expr.toString());
            }
            default:
                throw new RuntimeException("TypecheckerException. Invalid binary operator: " + expr.toString());
        }
    }

    // Function to typecheck unary operation expressions
    public Type typecheckUnOpExpr(final UnOpExpr expr, final Map<Variable, Type> typeEnv) {
        final Type operandType = typecheckExpr(expr.expr, typeEnv);
        switch (expr.op) {
            case NEG: {
                if (!(operandType instanceof IntType))
                    throw new RuntimeException("TypecheckerException. Negation of non-integer type: " + expr.toString());
                return new IntType();
            }
            case NOT: {
                if (!(operandType instanceof BoolType))
                    throw new RuntimeException("TypecheckerException. Logical negation of non-bool type: " + expr.toString());
                return new BoolType();
            }
            default:
                throw new RuntimeException("TypecheckerException. Invalid unary operator: " + expr.toString());
        }
    }

    // Function to typecheck from left hand side as reading of variables, expressions, and structs is done from left to right
    public Type typecheckLHS(final LHS lhs, final Map<Variable, Type> typeEnv) {
        if (lhs instanceof VariableLHS)
            return typeEnv.get(((VariableLHS) lhs).var);
        if (lhs instanceof FieldAccessLHS) {
            final FieldAccessLHS fieldAccessLHS = (FieldAccessLHS) lhs;
            final Type baseType = typecheckExpr(fieldAccessLHS.base, typeEnv);
            if (baseType instanceof StructType) {
                final Variable structname = ((StructType) baseType).structname;
                if (structSignatures.containsKey(structname)) {
                    final List<Param> structParams = structSignatures.get(structname);
                    for (Param param : structParams) {
                        if (param.var.equals(fieldAccessLHS.fieldname))
                            return param.type;
                    }
                }
            }
            throw new RuntimeException("TypecheckerException. Invalid FieldAccessLHS: " + lhs.toString());
        }
        if (lhs instanceof ArrayAccessLHS) {
            final ArrayAccessLHS arrayAccessLHS = (ArrayAccessLHS) lhs;
            final Type arrayType = typecheckExpr(arrayAccessLHS.base, typeEnv);
            final Type indexType = typecheckExpr(arrayAccessLHS.index, typeEnv);
            if (arrayType instanceof PointerType)
                if (indexType instanceof IntType)
                    return ((PointerType) arrayType).type;
                else
                    throw new RuntimeException("TypecheckerException. Array access with non-integer type: " + lhs.toString());
            else
                throw new RuntimeException("TypecheckerException. Array access of non-array type: " + lhs.toString());
        }
        throw new RuntimeException("TypecheckerException. Unknown LHS: " + lhs.toString());
    }

    // Interfaces and static classes defined
    interface Stmt {}

    interface Expr {}

    interface LHS {}

    interface Type {}

    static class Variable {}

    static class Param {
        Variable var;
        Type type;
    }

    static class VardecStmt implements Stmt {
        Type type;
        Variable var;
    }    

    static class AssignStmt implements Stmt {
        LHS lhs;
        Expr rhs;
    }

    static class WhileStmt implements Stmt {
        Expr guard;
        Stmt body;
    }

    static class IfStmt implements Stmt {
        Expr guard;
        Stmt then;
        Stmt elze;
    }

    static class ReturnStmt implements Stmt {
        Expr expr;
    }

    static class BlockStmt implements Stmt {
        List<Stmt> stmts;
    }

    static class VariableExpr implements Expr {
        Variable var;
    }

    static class IntExpr implements Expr {}

    static class BoolExpr implements Expr {}

    static class NullExpr implements Expr {}

    static class BinOpExpr implements Expr {
        Expr lhs;
        Expr rhs;
        Op op;

        enum Op {
            ADD, SUB, MUL, DIV, MOD,
            LT, LE, GT, GE,
            AND, OR,
            EQ, NE
        }
    }

    static class UnOpExpr implements Expr {
        Expr expr;
        Op op;

        enum Op {
            NEG, NOT
        }
    }

    static class FieldAccessExpr implements Expr {
        Expr expr;
        Variable fieldname;
    }

    static class FuncCallExpr implements Expr {
        Variable funcname;
        List<Expr> args;
    }

    static class AllocExpr implements Expr {
        Type type;
    }

    static class AllocArrayExpr implements Expr {
        Type type;
        Expr expr;
    }

    static class ArrayAccessExpr implements Expr {
        Expr base;
        Expr index;
    }

    static class VariableLHS implements LHS {
        Variable var;
    }

    static class FieldAccessLHS implements LHS {
        Expr base;
        Variable fieldname;
    }

    static class ArrayAccessLHS implements LHS {
        Expr base;
        Expr index;
    }

    static class IntType implements Type {}

    static class BoolType implements Type {}

    static class StructType implements Type {
        Variable structname;
    }

    static class PointerType implements Type {
        Type type;

        PointerType(Type type) {
            this.type = type;
        }
    }
}