# lowlangStructs

## Grammar Definition

var is a variable
structname is the name of a structure
funcname is the name of a function
i is an integer

### Types
type ::= `int` | _Integers are a type_
         `void` |
          structname| _Structures are a type_
         `(` `*` type `)` _Pointer to something of type_

param :: = `(` type var `)`

### Structs
structdef ::= `(` `struct` structname param* `)`

### Functions
fdef ::= `(` `func` funcname `(` param* `)` type stmt* `)`


Left-hand side of an assignment.  Denotes a place where something can be assigned.
lhs ::= var | _Assignment to a variable_
        `(` `.` lhs var `)` | _Assignment to a field of a struct_
        `(` `*` lhs `)` _Assignment to something at an address_

stmt ::= `(` `vardec` type var `)` | _Variable declaration_
         `(` `assign` lhs exp `)` | _Assignment_
         `(` `while` exp stmt `)` | _While loops_
         `(` `if` exp stmt [stmt] `)` | _if_
         `(` `return` [exp] `)` | _Return_
         `(` `block` stmt* `)` |  _Blocks_
         `(` `println` exp `)` | _Printing something_
         `(` `stmt` exp `)` _Expression statements_

### Arithmetic and relational operators
op ::= `+` | `-` | `*` | `/` | `<` | `==` | `!=`

exp ::= i | `true` | `false` | _Integers and booleans_
        `null` | _Null; assignable to pointer types_
        lhs | _Accessing something in memory_
        `(` `&` lhs `)` | _Address-of something in memory_
        `(` `*` exp `)` | _Dereference something_
        `(` op exp exp `)` |
        `(` `call` funcname exp* `)` _Function calls_

program ::= structdef* fdef* stmt*


## Tokens

Format: TokenName (Hashcode)

_During the tokenizing stage, the tokenizer doesnt really care whether a name belongs to a variable, function, or struct._
_Thus, they all fall under the category "IdentifierToken"_
Format: TokenName (Hashcode)

- IdentifierToken (String)
- NumberToken (int)
- IntToken (0)
- VoidToken (1)
- LeftParenToken (2)
- StarToken (3)
- RightParenToken (4)
- StructToken (5)
- FuncToken (6)
- DotToken (7)
- VardecToken (8)
- AssignToken (9)
- WhileToken (10)
- IfToken (11)
- ReturnToken (12)
- BlockToken (13)
- PrintLnToken (14)
- StmtToken (15)
- PlusToken (16)
- MinusToken (17)
- DivideToken (18)
- LessThanToken (19)
- DoubleEqualToken (20)
- NotEqualToken (21)
- TrueToken (22)
- FalseToken (23)
- NullToken (24)
- AndToken (25)
- CallToken (26)

## AST Definition

_Numeric HashCode only for nodes that dont have children/fields_

### Type nodes (interface Type)

- class IntType (hashCode 0)
- class BoolType (hashcode 1)
- class VoidType (hashCode 2)
- class StructType
- class PointerType

### LHS nodes (interface LHS)

- class VarLHS 
- class FieldLHS
- class DerefLHS

### Statement Nodes (interface Stmt)

- class VardecStmt
- class AssignStmt
- class WhileStmt
- class IfStmt
- class ReturnStmt (hashCode 11 if expr null)
- class BlockStmt
- class PrintLnStmt
- class ExprStmt

### Operator Nodes (interface Op)

- class PlusOp (hashCode 3)
- class MinusOp (hashCode 4)
- class StarOp (hashCode 5)
- class DivideOp (hashCode 6)
- class LessThanOp (hashCode 7)
- class DoubleEqualOp (hashCode 8)
- class NotEqualOp (hashCode 9)

### Expression Nodes (interface Expr)

- class NumberLiteralExpr
- class BooleanLiteralExpr
- class NullExpr (hashCode 10)
- class LHSExpr
- class AddressExpr
- class DeferExpr
- class BinOpExpr
- class CallExpr

### Miscelleaneous Nodes

- class Param
- class StructDef
- class FuncDef

### Root Node

- class Program

