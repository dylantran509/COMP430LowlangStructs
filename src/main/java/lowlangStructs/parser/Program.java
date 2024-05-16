package lowlangStructs.parser;
import java.util.List;
import java.util.ArrayList;

public class Program {

    public final List<StructDef> structDefs;
    public final List<FuncDef> funcDefs;
    public final List<Stmt> stmts;
    
    public Program(final List<StructDef> structDefs,
                   final List<FuncDef> funcDefs,
                   final List<Stmt> stmts) {
        this.structDefs = structDefs;
        this.funcDefs = funcDefs;
        this.stmts = stmts;
    }
    
    @Override
    public boolean equals(final Object other) {
        if(!(other instanceof Program))
            return false;
        final Program otherAsProgram = (Program)other;
        return (structDefs.equals(otherAsProgram.structDefs) &&
                funcDefs.equals(otherAsProgram.funcDefs) &&
                stmts.equals(otherAsProgram.stmts));
    }
    
    @Override
    public int hashCode() {
        return (structDefs.hashCode() +
                funcDefs.hashCode() +
                stmts.hashCode());
    }
    
    @Override
    public String toString() {
        return ("Program(" +
                structDefs.toString() + ", " +
                funcDefs.toString() + ", " +
                stmts.toString() + ")");
    }
    
    public void prettyPrint() {
        String uglyString = this.toString();
        char[] arr = uglyString.toCharArray();
        
        StringBuilder prettyString = new StringBuilder();
        int IC = 0;
        for (char c: arr) {
            if (c == ' ')
                continue;
            if (c == '(') {
                prettyString.append(c + "\n");
                IC++;
                prettyString.append(indent(IC));
                continue;
            }
            if (c == ')') {
                IC--;
                prettyString.append("\n" + indent(IC) + c);
                continue;
            }
            if (c == ',') {
                prettyString.append(c + "\n" + indent(IC));
                continue;
            }
            prettyString.append(c);
        }
        
        System.out.println(prettyString);
        
    }
    
    public String indent(int IC) {
        String retval = "";
        while (IC > 0) {
            retval += "    ";
            IC--;
        }
        return retval;
    }
    
}
