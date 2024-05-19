package lowlangStructs.typechecker;
import lowlangStructs.parser.*;
import java.util.List;

public class FunctionSignature {

    public final List<Param> params;
    public final Type returnType;
    
    public FunctionSignature(final List<Param> params, final Type returnType) {
        this.params = params;
        this.returnType = returnType;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof FunctionSignature))
            return false;
        return true;
    }
    
}
