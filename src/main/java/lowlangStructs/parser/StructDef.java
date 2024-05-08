package lowlangStructs.parser;
import java.util.List;
import java.util.ArrayList;

public class StructDef {

    public final Variable structname;
    public final List<Param> params;
    
    public StructDef(final Variable structname, final List<Param> params) {
        this.structname = structname;
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof StructDef))
            return false;
        final StructDef otherAsStructDef = (StructDef)other;
        return (structname.equals(otherAsStructDef.structname) &&
                params.equals(otherAsStructDef.params));
    }
    
    @Override
    public int hashCode() {
        return structname.hashCode() + params.hashCode();
    }
    
    @Override
    public String toString() {
        return ("StructDef(" +
                structname.toString() + ", " +
                params.toString() + ")");
    }
    
}
