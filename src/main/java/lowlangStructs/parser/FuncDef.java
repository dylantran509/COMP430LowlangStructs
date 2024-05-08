package lowlangStructs.parser;
import java.util.List;
import java.util.ArrayList;

public class FuncDef {

    public final Variable funcname;
    public final List<Param> params;
    public final Type type;
    public final List<Stmt> stmts;
    
    public FuncDef(final Variable funcname,
                   final List<Param> params,
                   final Type type,
                   final List<Stmt> stmts) {
        this.funcname = funcname;
        this.params = params;
        this.type = type;
        this.stmts = stmts;
    }
    
    @Override
    public boolean equals(final Object other) {
        if(!(other instanceof FuncDef))
            return false;
        final FuncDef otherAsFuncDef = (FuncDef)other;
        return (funcname.equals(otherAsFuncDef.funcname) &&
                params.equals(otherAsFuncDef.params)&&
                type.equals(otherAsFuncDef.type) &&
                stmts.equals(otherAsFuncDef.stmts));
    }
    
    @Override
    public int hashCode() {
        return (funcname.hashCode() +
                params.hashCode() +
                type.hashCode() +
                stmts.hashCode());
    }
    
    @Override
    public String toString() {
        return ("FuncDef(" +
                funcname.toString() + ", " +
                params.toString() + ", " +
                type.toString() + ", " +
                stmts.toString() + ")");
    }
    
}
