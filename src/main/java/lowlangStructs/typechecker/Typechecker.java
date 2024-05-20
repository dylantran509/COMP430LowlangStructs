package lowlangStructs.typechecker;
import lowlangStructs.parser.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Typechecker {

    public final Program program;
    public Map<Variable, List<Param>> structSignatures;
    public Map<Variable, List<Param>> funcSignatures;
    public boolean inFuncDef;
    public Type currReturnType;
    
    public Typechecker(final Program program) {
        this.program = program;
        structSignatures = new HashMap<Variable, List<Param>>();
        funcSignatures = new HashMap<Variable, List<Param>>();
        this.inFuncDef = false;
        this.currReturnType = null;
    }
    
    public void typecheckProgram() throws TypecheckerException {
        Map<Variable, Type> typeEnv = new HashMap<>();
        
        // Typecheck StructDef list
        for (StructDef structDef: program.structDefs) {
            typecheckStructDef(structDef);
        }
        
        inFuncDef = true;
        // Typecheck FuncDef list
        for (FuncDef funcDef: program.funcDefs) {
            typeEnv = typecheckFuncDefSignatureAndReturnType(funcDef, typeEnv);
            currReturnType = typeEnv.get(funcDef.funcname);
            // Don't want variables in FuncDefs to go into outer scope, use temp map while
            // iterating though stmts
            Map<Variable, Type> tempTypeEnv = copyOfMap(typeEnv);
            List<Param> params = funcDef.params;
            for (Param param: params)
                tempTypeEnv.put(param.var, param.type);
            for (Stmt stmt: funcDef.stmts)
                tempTypeEnv = typecheckStmt(stmt, tempTypeEnv);
        }
        inFuncDef = false;
        
        // Finally, typecheck program stmt list using typeEnv map
        for (Stmt stmt: program.stmts)
            typeEnv = typecheckStmt(stmt, typeEnv);
        
        // Program is well-typed!
        System.out.println("Program is well-typed!");
        
    }
    
    public void typecheckStructDef(final StructDef structDef) throws TypecheckerException {
        // Check for existence
        if (structSignatures.containsKey(structDef.structname))
            // Duplicate Struct
            throw new TypecheckerException("TypecheckerException. Duplicate struct: "
                                          + structDef.toString());
        else {
            // Struct does not exist, add name to map
            // List of params temporarily null to allow for structs to
            // have instances of themselves as fields (e.g. Node struct
            // containing Node children/Node pointers)
            structSignatures.put(structDef.structname, null);
        }
        
        typecheckParams(structDef.params);
        
        // Valid struct
        structSignatures.put(structDef.structname, structDef.params);
        
    }
    
    public void typecheckParams(final List<Param> params) throws TypecheckerException {
        // Params must not have duplicate names, and types should be valid
        List<String> names = new ArrayList<>();
        for (Param param: params) {
            if (names.contains(param.var.name))
                // Duplicate param name
                throw new TypecheckerException("TypecheckerException. Duplicate param: " + param.toString());
            else {
                // Unique param name, ensure type is valid
                typecheckType(param.type);
                // Valid Param, add name to list
                names.add(param.var.name);
            }
        }
    }
    
    public void typecheckType(final Type type) throws TypecheckerException {
        if (type instanceof IntType || type instanceof VoidType)
            return;
        if (type instanceof PointerType) {
            // Recursively call on type pointed to
            typecheckType(((PointerType)type).type);
            return;
        }
        if (type instanceof StructType) {
            if (structSignatures.containsKey(((StructType)type).structname))
                // Valid struct
                return;
        }
        
        throw new TypecheckerException("TypecheckerException. Unrecognized type: " + type.toString());
        
    }
    
    public Map<Variable, Type> copyOfMap(final Map<Variable, Type> typeEnv) {
        Map<Variable, Type> newMap = new HashMap<>();
        newMap.putAll(typeEnv);
        return newMap;
    }
    
    public Map<Variable, Type> addToMap(final Variable var, final Type type, final Map<Variable, Type> typeEnv) {
        Map<Variable, Type> newMap = new HashMap<>();
        newMap.putAll(typeEnv);
        newMap.put(var, type);
        return newMap;
    }
    
    public Map<Variable, Type> typecheckFuncDefSignatureAndReturnType(final FuncDef funcDef, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        // Check for existence
        if (funcSignatures.containsKey(funcDef.funcname))
            // Duplicate FuncDef
            throw new TypecheckerException("TypecheckerException. Duplicate funcDef: " + funcDef.toString());
        // Unique FuncDef
        // Typecheck params
        typecheckParams(funcDef.params);
        // Typecheck return type
        typecheckType(funcDef.type);
        
        // Two maps must be updated
        //     - funcSignatures global map
        //     - typeEnv
        // By updating these maps at this point we allow our function to call themselves
        // (i.e. we allow for recursion)
        funcSignatures.put(funcDef.funcname, funcDef.params);
        return addToMap(funcDef.funcname, funcDef.type, typeEnv);
        
    }
    
    public Map<Variable, Type> typecheckStmt(final Stmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        if (stmt instanceof VardecStmt)
            return typecheckVardecStmt((VardecStmt)stmt, typeEnv);
        if (stmt instanceof AssignStmt)
            return typecheckAssignStmt((AssignStmt)stmt, typeEnv);
        if (stmt instanceof WhileStmt)
            return typecheckWhileStmt((WhileStmt)stmt, typeEnv);
        if (stmt instanceof IfStmt)
            return typecheckIfStmt((IfStmt)stmt, typeEnv);
        if (stmt instanceof ReturnStmt)
            return typecheckReturnStmt((ReturnStmt)stmt, typeEnv);
        if (stmt instanceof BlockStmt)
            return typecheckBlockStmt((BlockStmt)stmt, typeEnv);
        if (stmt instanceof PrintLnStmt) {
            typecheckExpr(((PrintLnStmt)stmt).expr, typeEnv);
            return typeEnv;
        }
        if (stmt instanceof ExprStmt) {
            typecheckExpr(((ExprStmt)stmt).expr, typeEnv);
            return typeEnv;
        }
        throw new TypecheckerException("TypecheckerException, Unknown stmt: " + stmt.toString());
    }
    
    public Map<Variable, Type> typecheckVardecStmt(final VardecStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        // Check for existence of variable
        if (typeEnv.containsKey(stmt.var))
            throw new TypecheckerException("TypecheckerException. Variable already exists within scope: " + stmt.var.toString());
        // Check type is valid
        typecheckType(stmt.type);
        // Well-typed VardecStmt, return updated typeEnv
        return addToMap(stmt.var, stmt.type, typeEnv);
    }
    
    public Map<Variable, Type> typecheckAssignStmt(final AssignStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        
        final Type LHSType = typecheckLHS(stmt.lhs, typeEnv);
        final Type ExprType = typecheckExpr(stmt.expr, typeEnv);
        
        if (LHSType == null)
            throw new TypecheckerException("TypecheckerException. Cannot assign value to null.");
        
        if (ExprType == null)
            if (!(LHSType instanceof PointerType))
                throw new TypecheckerException("TypecheckerException. Cannot assign null to non-Pointer type: " + stmt.toString());
            else
                return typeEnv;
            
        
        if (LHSType.equals(ExprType))
            // no change
            return typeEnv;
        else
            throw new TypecheckerException("TypecheckerException. Assignment type mismatch. Cannot assign " + ExprType.toString() + " to " + LHSType.toString()
                                           + "\n" + stmt.toString());
    }
    
    public Map<Variable, Type> typecheckWhileStmt(final WhileStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        // Ensure guard resolves to BoolType
        Type guardType = typecheckExpr(stmt.guard, typeEnv);
        if (!(guardType instanceof BoolType))
            throw new TypecheckerException("TypecheckerException. While guard must resolve to BoolType: " + stmt.toString());
        return typecheckStmt(stmt.stmt, typeEnv);
    }
    
    public Map<Variable, Type> typecheckIfStmt(final IfStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        // Ensure guard resolves to BoolType
        Type guardType = typecheckExpr(stmt.guard, typeEnv);
        if (!(guardType instanceof BoolType))
            throw new TypecheckerException("TypecheckerException. If guard must resolve to BoolType");
        typecheckStmt(stmt.ifBody, typeEnv);
        if (stmt.elseBody != null)
            typecheckStmt(stmt.elseBody, typeEnv);
        
        // No changes to typeEnv
        return typeEnv;
    }
    
    public Map<Variable, Type> typecheckReturnStmt(final ReturnStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        if (inFuncDef) {
            // ReturnStmt in FuncDef
            if (stmt.expr != null) {
                // Return not empty
                Type returnType = typecheckExpr(stmt.expr, typeEnv);
                if (returnType.equals(currReturnType))
                    return typeEnv;
                else
                    throw new TypecheckerException("TypecheckerException. Function return type mismatch. Expected: " + currReturnType.toString() + ", Received: " + returnType);
            } else {
                // Return empty
                if (!(currReturnType instanceof VoidType))
                    throw new TypecheckerException("TypecheckerException. Function return type mismatch. Expected: " + currReturnType.toString() + ", Received: VoidType()");
            }
        }
        // ReturnStmt outside of FuncDef
        throw new TypecheckerException("TypecheckerException. Cannot have ReturnStmt outside of FuncDef");
    }
    
    public Map<Variable, Type> typecheckBlockStmt(final BlockStmt stmt, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        Map<Variable, Type> tempTypeEnv = copyOfMap(typeEnv);
        for (Stmt statement: stmt.stmts)
            tempTypeEnv = typecheckStmt(statement, typeEnv);
        return tempTypeEnv;
    }
    
    public Type typecheckLHS(final LHS lhs, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        
        //Exception dealt with in typecheckAssignStmt
        if (lhs == null)
            return null;
        
        if (lhs instanceof VarLHS) {
            // simple lookup in typeEnv
            VarLHS varlhs = (VarLHS)lhs;
            if (typeEnv.containsKey(varlhs.var)) {
                return typeEnv.get(varlhs.var);
            }
            else
                throw new TypecheckerException("TypecheckerException. Unknown variable: " + varlhs.toString());
        }
        
        if (lhs instanceof FieldLHS) {
            FieldLHS fieldlhs = (FieldLHS)lhs;
            Type innerlhsType = typecheckLHS(fieldlhs.lhs, typeEnv);
            // Ensure inner lhs resolves to StructType, otherwise cannot access field
            if (innerlhsType instanceof StructType) {
                // Check that the Struct has the field specified by the var of the FieldLHS
                Variable structName = ((StructType)innerlhsType).structname;
                Variable fieldVar = fieldlhs.var;
                if (structSignatures.containsKey(structName)) {
                    // Iterate through the parameter list of the StructType
                    // and attempt to retrieve the type of the field specified
                    Type fieldType = null;
                    for (Param param: structSignatures.get(structName))
                        if (fieldVar.equals(param.var))
                            fieldType = param.type;
                    
                    if (fieldType != null)
                        return fieldType;
                    else
                        throw new TypecheckerException("TypecheckerException. " + structName.toString() +" does not contain field: " + fieldVar.toString());
                    
                } else
                    throw new TypecheckerException("TypecheckerException. No such struct defined: " + structName.toString());
                
            } else
                throw new TypecheckerException("TypecheckerException. Cannot access field of non-Struct type: " + lhs.toString());
        }
        
        // Likely incorrect
        if (lhs instanceof DerefLHS) {
            DerefLHS dereflhs = (DerefLHS)lhs;
            Type innerlhsType = typecheckLHS(dereflhs.lhs, typeEnv);
            if (innerlhsType instanceof PointerType) {
                return ((PointerType)innerlhsType).type;
            } else
                throw new TypecheckerException("TypecheckerException. Cannot dereference non-pointer type: " + lhs.toString());
        }
        
        throw new TypecheckerException("TypecheckerException. Unrecognized LHS: " + lhs.toString());
    }
    
    public Type typecheckExpr(final Expr expr, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        if (expr instanceof NumberLiteralExpr)
            return new IntType();
        if (expr instanceof NullExpr)
            // Will probably break something, needs testing
            return null;
        if (expr instanceof LHSExpr)
            return typecheckLHS(((LHSExpr)expr).lhs, typeEnv);
        if (expr instanceof AddressExpr)
            // Likely incorrect
            return new PointerType(typecheckLHS(((AddressExpr)expr).lhs, typeEnv));
        if (expr instanceof DerefExpr) {
            DerefExpr derefexpr = (DerefExpr)expr;
            Type exprType = typecheckExpr(derefexpr.expr, typeEnv);
            if (exprType instanceof PointerType)
                return ((PointerType)exprType).type;
            else
                throw new TypecheckerException("TypecheckerException. Cannot dereference non-PointerType: " + exprType.toString());
        }
        if (expr instanceof BinOpExpr)
            return typecheckBinOpExpr((BinOpExpr)expr, typeEnv);
        if (expr instanceof CallExpr)
            return typecheckCallExpr((CallExpr)expr, typeEnv);
        
        throw new TypecheckerException("TypecheckerException. Unknown expression: " + expr.toString());
            
        
    }
    
    public Type typecheckBinOpExpr(final BinOpExpr expr, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        final Op op = expr.op;
        final Type leftType = typecheckExpr(expr.leftExpr, typeEnv);
        final Type rightType = typecheckExpr(expr.rightExpr, typeEnv);
        
        if (op instanceof PlusOp || op instanceof MinusOp
                                 || op instanceof DivideOp
                                 || op instanceof StarOp) {
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
        }
        if (op instanceof LessThanOp)
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
        if (op instanceof DoubleEqualOp || op instanceof NotEqualOp) {
            if (leftType == null || rightType == null) {
                if (leftType == null && rightType == null)
                    return new BoolType();
                if (leftType instanceof PointerType || rightType instanceof PointerType)
                    return new BoolType();
                throw new TypecheckerException("TypecheckerException. Cannot compare non-pointer type against null: " + expr.toString());
            }
            return new BoolType();
        }
        
        throw new TypecheckerException("TypecheckerException. Invalid BinOpExpr: " + expr.toString());
        
    }
    
    public Type typecheckCallExpr(final CallExpr expr, final Map<Variable, Type> typeEnv) throws TypecheckerException {
        // Check for existence of function
        Variable functionName = expr.funcname;
        if (typeEnv.containsKey(functionName)) {
            List<Expr> exprs = expr.exprs;
            List<Param> params = funcSignatures.get(functionName);
            if (exprs.size() != params.size())
                throw new TypecheckerException("TypecheckerException. Function call argument number mismatch: " + expr.toString());
            for (int i = 0; i < exprs.size(); i++) {
                Type exprType = typecheckExpr(exprs.get(i), typeEnv);
                Type paramType = params.get(i).type;
                if (exprType.equals(paramType))
                    continue;
                else
                    throw new TypecheckerException("TypecheckerException. Function call argument type mismatch: " + expr.toString());
            }
            
            return typeEnv.get(functionName);
            
        } else
            throw new TypecheckerException("TypecheckerException. Unknown function: " + expr.toString());
    }
    
}
