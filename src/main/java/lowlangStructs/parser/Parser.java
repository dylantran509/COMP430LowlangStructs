package lowlangStructs.parser;
import lowlangStructs.tokenizer.*;

import java.beans.Expression;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    public final Token[] tokens;
    
    public Parser(final Token[] tokens) {
        this.tokens = tokens;
    }
    
// ----------------------- HELPER CLASSES/FUNCTIONS ----------------------------------
    
    // This helper class achieves two things:
    // 1) It stores the current location in the Token[] array. (This avoids having to
    //    increment/decrement a global position counter)
    // 2) It stores the result of a successful parse
    
    public class ParseResult<A> {
        
        public final int nextPosition;
        public final A result;
        
        public ParseResult (final int nextPosition, final A result) {
            this.nextPosition = nextPosition;
            this.result = result;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof ParseResult))
                return false;
            final ParseResult<A> otherAsParseResult = (ParseResult<A>)other;
            return (nextPosition == (otherAsParseResult.nextPosition) &&
                    result.equals(otherAsParseResult.result));
        }
        
        @Override
        public int hashCode() {
            return nextPosition + result.hashCode();
        }
        
        @Override
        public String toString() {
            return ("ParseResult(" +
                    nextPosition + ", " +
                    result.toString() + ")");
        }
        
    }
    
    public Token getToken(final int position) throws ParseException {
        
        if (position >= 0 && position < tokens.length) {
            return tokens[position];
        } else {
            throw new ParseException("Out of tokens.");
        }
        
    }
    
    public void assertTokenIs(final int position, final Token expected) throws ParseException {
        final Token received = getToken(position);
        if (!expected.equals(received)) {
            throw new ParseException("ParseException. Expected: " + expected.toString() +
                                                      ", received: " + received.toString());
        }
    }
    
// ------------------------------------------------------------------------------------
    
    // Entry point
    public Program parse() throws ParseException {
        
        // Begin reading tokens at index 0
        int position = 0;
        final ParseResult<Program> program = parseProgram(position);
        
        if (program.nextPosition == tokens.length)
            return program.result;
        else if (program.nextPosition < tokens.length) {
            program.result.prettyPrint();
            System.out.println(Arrays.toString(Arrays.copyOfRange(tokens, program.nextPosition, tokens.length)));
            throw new ParseException("ParseException. Remaining tokens starting with: " + getToken(program.nextPosition));
        }
        else
            throw new ParseException("ParseException. Token pointer > token array length");
        
    }
    
    // program ::= structdef* fdef* stmt*
    public ParseResult<Program> parseProgram(int position) {
        
        // Parse 0 or more StructDef objects
        List<StructDef> structdefs = new ArrayList<>();
        while (true) {
            try {
                ParseResult<StructDef> structdef = parseStructDef(position);
                structdefs.add(structdef.result);
                position = structdef.nextPosition;
            } catch (ParseException e) {
                // All StructDefs read
                break;
            }
        }
        
        // Parse 0 or more FuncDef objects
        List<FuncDef> funcdefs = new ArrayList<>();
        while (true) {
            try {
                ParseResult<FuncDef> funcdef = parseFuncDef(position);
                funcdefs.add(funcdef.result);
                position = funcdef.nextPosition;
            } catch (ParseException e) {
                // All FuncDefs read
                break;
            }
        }
        
        
        List<Stmt> stmts = new ArrayList<>();
        while (true) {
            try {
                ParseResult<Stmt> stmt = parseStmt(position);
                stmts.add(stmt.result);
                position = stmt.nextPosition;
            } catch (Exception e) {
                // All Stmts read
                break;
            }
        }
        
        return new ParseResult<Program> (position, new Program(structdefs, funcdefs, stmts));
        
    }
    
    // structdef ::= `(` `struct` structname param* `)`
    public ParseResult<StructDef> parseStructDef(int position) throws ParseException{
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new StructToken());
        final ParseResult<Variable> structname = parseVariable(position + 2);
        
        // Successfully parsed variable above, update position for loop below
        position = structname.nextPosition;
        
        // Parse 0 or more Param objects
        List<Param> params = new ArrayList<>();
        while (true) {
            try {
                
                ParseResult<Param> param = parseParam(position);
                params.add(param.result);
                position = param.nextPosition;
                
            } catch (ParseException e) {
                break;
            }
        }
        
        assertTokenIs(position, new RightParenToken());
        
        return new ParseResult<StructDef> (position + 1, new StructDef(structname.result, params));
        
    }
    
    // var is a variable
    public ParseResult<Variable> parseVariable(int position) throws ParseException {
        Token myToken = getToken(position);
        if (myToken instanceof IdentifierToken) {
            return new ParseResult<Variable> (position + 1, new Variable(((IdentifierToken)myToken).name));
        } else {
            throw new ParseException("ParseException. Expceted: IdentifierToken. Received: " + myToken.toString());
        }
    }
    
    // param :: = `(` type var `)`
    public ParseResult<Param> parseParam(int position) throws ParseException {
        
        assertTokenIs(position, new LeftParenToken());
        final ParseResult<Type> type = parseType(position + 1);
        final ParseResult<Variable> var = parseVariable(type.nextPosition);
        assertTokenIs(var.nextPosition, new RightParenToken());
        
        return new ParseResult<Param> (var.nextPosition + 1, new Param(type.result, var.result));
        
    }
    
    // type :: = 'int' | 'void' | structname | `(` `*` type `)`
    public ParseResult<Type> parseType(int position) throws ParseException {
        
        final Token myToken = getToken(position);
        if (myToken instanceof IntToken) {
            return new ParseResult<Type> (position + 1, new IntType());
        } else if (myToken instanceof VoidToken) {
            return new ParseResult<Type> (position + 1, new VoidType());
        //
        // It is not the parser's job to ensure StructTypes are properly created/referenced.
        // Thus we cheat a little by allowing any name/identifier at this point to be classified
        // as a StructType and leave type-checking to the type-checker
        //
        } else if (myToken instanceof IdentifierToken) {
            return new ParseResult<Type> (position + 1, new StructType(new Variable(((IdentifierToken)myToken).name)));
        } else {
            try {
                return parsePointerType(position);
            } catch (ParseException e) {
                throw new ParseException("ParseException. Expected: Type, Received: " + myToken.toString());
            }
        }
        
    }
    
    // `(` `*` type `)`
    public ParseResult<Type> parsePointerType(int position) throws ParseException {
        
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new StarToken());
        final ParseResult<Type> type = parseType(position + 2);
        assertTokenIs(type.nextPosition, new RightParenToken());
        
        return new ParseResult<Type> (type.nextPosition + 1, new PointerType(type.result));
        
    }
    
    // fdef ::= `(` `func` funcname `(` param* `)` type stmt* `)`
    public ParseResult<FuncDef> parseFuncDef(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new FuncToken());
        ParseResult<Variable> funcname = parseVariable(position + 2);
        assertTokenIs(funcname.nextPosition, new LeftParenToken());
        
        // Update position
        position = funcname.nextPosition + 1;
        List<Param> params = new ArrayList<>();
        while (true) {
            try {
                ParseResult<Param> param = parseParam(position);
                params.add(param.result);
                position = param.nextPosition;
            } catch (ParseException e) {
                // No more params
                break;
            }
        }
        
        assertTokenIs(position, new RightParenToken());
        ParseResult<Type> type = parseType(position + 1);
        
        position = type.nextPosition;
        List<Stmt> stmts = new ArrayList<>();
        while(true) {
            try {
                ParseResult<Stmt> stmt = parseStmt(position);
                stmts.add(stmt.result);
                position = stmt.nextPosition;
            } catch (ParseException e) {
                // All stmts read
                break;
            }
        }
        
        assertTokenIs(position, new RightParenToken());
        return new ParseResult<FuncDef> (position + 1, new FuncDef(funcname.result, params, type.result, stmts));
        
    }
    
    // stmt ::= '(' 'vardec' type var ')'     |
    //          '(' 'assign' lhs exp ')'      |
    //          '(' 'while' expr stmt ')'     |
    //          '(' 'if' expr stmt [stmt] ')' |
    //          '(' 'return' [expr] ')'       |
    //          '(' 'block' stmt* ')'         |
    //          '(' 'println' expr ')'        |
    //          '(' 'stmt' expr ')' 
    //
    public ParseResult<Stmt> parseStmt(int position) throws ParseException {
        try {
            return parseVardecStmt(position);
        }catch (Exception e){}
        try{
            return parseAssignStmt(position);
        }catch (Exception e){}
        try {
            return parseWhileStmt(position);
        }catch (Exception e){}
        try {
            return parseIfStmt(position);
        } catch (Exception e) {}
        try {
            return parseReturnStmt(position);
        } catch (Exception e) {}
        try {
            return parseBlockStmt(position);
        }catch (Exception e){}
        try {
            return parsePrintLn(position);
        }catch (Exception e){}
        
        throw new ParseException("ParseException. Unable to parse statement");
    }

    // stmt ::= '(' 'vardec' type var ')'    |
    public ParseResult<Stmt> parseVardecStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new VardecToken());
        ParseResult<Type> type = parseType(position + 2);
        ParseResult<Variable> var = parseVariable(type.nextPosition);
        assertTokenIs(var.nextPosition, new RightParenToken());

        return new ParseResult<Stmt>(var.nextPosition + 1, new VardecStmt(type.result, var.result));
    }
    //          '(' 'assign' lhs exp ')'     |
    public ParseResult<Stmt> parseAssignStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new AssignToken());
        ParseResult<LHS> lhs = parseLHS(position + 2);
        ParseResult<Expr> expr = parseExpr(lhs.nextPosition);
        assertTokenIs(expr.nextPosition, new RightParenToken());

        return new ParseResult<Stmt>(expr.nextPosition + 1, new AssignStmt(lhs.result, expr.result));
    }
    //          '(' 'while' exp stmt ')'     |
    public ParseResult<Stmt>parseWhileStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new WhileToken());
        ParseResult<Expr> expr = parseExpr(position + 2);
        ParseResult<Stmt> stmt = parseStmt(expr.nextPosition);
        assertTokenIs(stmt.nextPosition, new RightParenToken());

        return new ParseResult<Stmt>(stmt.nextPosition + 1, new WhileStmt(expr.result, stmt.result));

    }
    
    //        '(' 'if' expr stmt [stmt] ')' |
    public ParseResult<Stmt> parseIfStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new IfToken());
        ParseResult<Expr> guard = parseExpr(position + 2);
        ParseResult<Stmt> ifBody = parseStmt(guard.nextPosition);
        ParseResult<Stmt> elseBody = null;
        
        try {
            elseBody = parseStmt(ifBody.nextPosition);
        } catch (Exception e) {}
        
        if (elseBody != null) {
            // If statement has else body
            assertTokenIs(elseBody.nextPosition, new RightParenToken());
            return new ParseResult<Stmt> (elseBody.nextPosition + 1, new IfStmt(guard.result,
                                                                                ifBody.result,
                                                                                elseBody.result));
        } else {
            // No else body
            assertTokenIs(ifBody.nextPosition, new RightParenToken());
            return new ParseResult<Stmt> (ifBody.nextPosition + 1, new IfStmt(guard.result,
                                                                              ifBody.result));
        }
        
    }
    
    //          '(' 'return' [expr] ')'       |
    public ParseResult<Stmt> parseReturnStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new ReturnToken());
        ParseResult<Expr> expr = null;
        
        try {
            expr = parseExpr(position + 2);
        } catch (Exception e) {}
        
        if (expr != null) {
            // Returns something
            assertTokenIs(expr.nextPosition, new RightParenToken());
            return new ParseResult<Stmt> (expr.nextPosition + 1, new ReturnStmt(expr.result));
        } else {
            // Returns void
            assertTokenIs(position + 2, new RightParenToken());
            return new ParseResult<Stmt> (position + 3, new ReturnStmt());
        }
        
    }
    
    
    //          '(' 'println' exp ')'        |
    public ParseResult<Stmt> parsePrintLn(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new PrintLnToken());
        ParseResult<Expr> expr = parseExpr(position + 2);
        assertTokenIs(expr.nextPosition, new RightParenToken());

        return new ParseResult<Stmt>(expr.nextPosition + 1, new PrintLnStmt(expr.result));


    }
    //         '(' 'block' stmt* ')'
    public ParseResult<Stmt> parseBlockStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new BlockToken());
        
        position = position + 2;

        // Parse 0 or more Stmt objects
        List<Stmt> stmts = new ArrayList<>();
        while (true) {
            try {
                ParseResult<Stmt> stmt = parseStmt(position);
                stmts.add(stmt.result);
                position = stmt.nextPosition;

            } catch (ParseException e) {
                break;
            }
        }

        assertTokenIs(position, new RightParenToken());

        return new ParseResult<Stmt>(position + 1, new BlockStmt(stmts));
    }
    
    //          '(' 'stmt' expr ')' 
    public ParseResult<Stmt> parseExprStmt(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new StmtToken());
        ParseResult<Expr> expr = parseExpr(position + 2);
        assertTokenIs(expr.nextPosition, new RightParenToken());
        return new ParseResult<Stmt> (expr.nextPosition + 1, new ExprStmt(expr.result));
    }

    // lhs ::= var                |
    //        `(` `.` lhs var `)` |
    //        `(` `*` lhs `)`
    public ParseResult<LHS> parseLHS(int position) throws ParseException {
        
        try {
            return parseVarLHS(position);
        } catch (Exception e) {}
        try {
            return parseFieldLHS(position);
        } catch (Exception e) {}
        try {
            return parseDerefLHS(position);
        } catch (Exception e) {}
        
        throw new ParseException("ParseException. Unable to parse LHS");
    }
    
    // lhs ::= var                |
    public ParseResult<LHS> parseVarLHS(int position) throws ParseException {
        ParseResult<Variable> var = parseVariable(position);
        return new ParseResult<LHS>(var.nextPosition, new VarLHS(var.result));
    }
    
    //        `(` `.` lhs var `)` |
    public ParseResult<LHS> parseFieldLHS(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new DotToken());
        ParseResult<LHS> lhs = parseLHS(position + 2);
        ParseResult<Variable> var = parseVariable(lhs.nextPosition);
        assertTokenIs(var.nextPosition, new RightParenToken());
        return new ParseResult<LHS>(var.nextPosition + 1, new FieldLHS(lhs.result, var.result));
    }
    
    //        `(` `*` lhs `)`
    public ParseResult<LHS> parseDerefLHS(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new StarToken());
        ParseResult<LHS> lhs = parseLHS(position + 2);
        assertTokenIs(lhs.nextPosition, new RightParenToken());
        return new ParseResult<LHS>(lhs.nextPosition + 1, new DerefLHS(lhs.result));
    }
    
    // exp ::= i | `true` | `false`         |
    //        `null`                        | 
    //        lhs                           | 
    //        `(` `&` lhs `)`               |
    //        `(` `*` exp `)`               | 
    //        `(` op exp exp `)`            |
    //        `(` `call` funcname exp* `)` 
    public ParseResult<Expr> parseExpr(int position) throws ParseException {
        
        try {
            return parseNumberLiteralExpr(position);
        } catch (Exception e) {}
        try {
            return parseBooleanLiteralExpr(position);
        } catch (Exception e) {}
        try {
            return parseNullExpr(position);
        } catch (Exception e) {}
        try {
            return parseLHSExpr(position);
        } catch (Exception e) {}
        try {
            return parseAddressExpr(position);
        } catch (Exception e) {}
        try {
            return parseDerefExpr(position);
        } catch (Exception e) {}
        try {
            return parseBinOpExpr(position);
        } catch (Exception e) {}
        try {
            return parseCallExpr(position);
        } catch (Exception e) {}
        
        throw new ParseException("ParserException. Unable to parse expression");
    }
    
    // exp ::= i
    public ParseResult<Expr> parseNumberLiteralExpr(int position) throws ParseException {
        Token myToken = getToken(position);
        if (myToken instanceof NumberToken) {
            return new ParseResult<Expr>(position + 1, new NumberLiteralExpr(((NumberToken)myToken).value));
        } else {
            throw new ParseException("ParseException. Expected: NumberToken, Received: " + myToken.toString());
        }
    }
    
    //  'true' | 'false'
    public ParseResult<Expr> parseBooleanLiteralExpr(int position) throws ParseException {
        Token myToken = getToken(position);
        if (myToken instanceof TrueToken || myToken instanceof FalseToken) {
            boolean myBool = true;
            if (myToken instanceof TrueToken)
                myBool = true;
            if (myToken instanceof FalseToken)
                myBool = false;
            return new ParseResult<Expr> (position + 1, new BooleanLiteralExpr(myBool));
        } else {
            throw new ParseException("ParseException. Expected: True/FalseToken, Received: " + myToken.toString());
        }
    }
    
    //  'null'
    public ParseResult<Expr> parseNullExpr(int position) throws ParseException {
        assertTokenIs(position, new NullToken());
        return new ParseResult<Expr>(position + 1, new NullExpr());
    }
    
    //        lhs   | 
    public ParseResult<Expr> parseLHSExpr(int position) throws ParseException {
        ParseResult<LHS> lhs = parseLHS(position);
        return new ParseResult<Expr>(lhs.nextPosition, new LHSExpr(lhs.result));
    }
    
    //        `(` `&` lhs `)`               |
    public ParseResult<Expr> parseAddressExpr(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new AndToken());
        ParseResult<LHS> lhs = parseLHS(position + 2);
        assertTokenIs(lhs.nextPosition, new RightParenToken());
        return new ParseResult<Expr>(lhs.nextPosition + 1, new AddressExpr(lhs.result));
    }
    
    //        `(` `*` exp `)`               | 
    public ParseResult<Expr> parseDerefExpr(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new StarToken());
        ParseResult<Expr> expr = parseExpr(position + 2);
        assertTokenIs(expr.nextPosition, new RightParenToken());
        return new ParseResult<Expr>(expr.nextPosition + 1, new DerefExpr(expr.result));
    }
    
    //        `(` op exp exp `)`            |
    public ParseResult<Expr> parseBinOpExpr(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        ParseResult<Op> op = parseOp(position + 1);
        ParseResult<Expr> leftExpr = parseExpr(op.nextPosition);
        ParseResult<Expr> rightExpr = parseExpr(leftExpr.nextPosition);
        assertTokenIs(rightExpr.nextPosition, new RightParenToken());
        return new ParseResult<Expr>(rightExpr.nextPosition + 1, new BinOpExpr(op.result, leftExpr.result, rightExpr.result));
    }
    
    //        `(` `call` funcname exp* `)` 
    public ParseResult<Expr> parseCallExpr(int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new CallToken());
        ParseResult<Variable> var = parseVariable(position + 2);
        
        position = var.nextPosition;
        List<Expr> exprs = new ArrayList<>();
        while (true) {
            try {
                ParseResult<Expr> expr = parseExpr(position);
                exprs.add(expr.result);
                position = expr.nextPosition;
            } catch (Exception e) {
                // No more expr to read
                break;
            }
        }
        
        assertTokenIs(position, new RightParenToken());
        return new ParseResult<Expr>(position + 1, new CallExpr(var.result, exprs));
        
    }
    
    //  op ::= `+` | `-` | `*` | `/` | `<` | `==` | `!=`
    public ParseResult<Op> parseOp(int position) throws ParseException {
        Token myToken = getToken(position);
        Op op = null;
        if (myToken instanceof PlusToken)
            op = new PlusOp();
        else if (myToken instanceof MinusToken)
            op = new MinusOp();
        else if (myToken instanceof StarToken)
            op = new StarOp();
        else if (myToken instanceof DivideToken)
            op = new DivideOp();
        else if (myToken instanceof LessThanToken)
            op = new LessThanOp();
        else if (myToken instanceof DoubleEqualToken)
            op = new DoubleEqualOp();
        else if (myToken instanceof NotEqualToken)
            op = new NotEqualOp();
        else
            throw new ParseException("ParseException. Unable to parse as operator: " + myToken.toString());
        
        return new ParseResult<Op>(position + 1, op);
    }
    
    
    
}
