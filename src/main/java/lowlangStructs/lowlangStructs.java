package lowlangStructs;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import lowlangStructs.tokenizer.*;
import lowlangStructs.parser.*;
import lowlangStructs.typechecker.*;

public class lowlangStructs {

    public static void usage() {
        System.out.println("Takes:");
        System.out.println("-An input program in lowlangStructs language");
    }
    
    public static String readFileToString(final String fileName) throws IOException {
        return Files.readString(new File(fileName).toPath());
    }
    
    public static void main(String[] args) throws IOException,
                                                  TokenizerException,
                                                  ParseException,
                                                  TypecheckerException{
        if (args.length != 1) {
            usage();
        } else {
            final String input = readFileToString(args[0]);
            final Token[] tokens = new Tokenizer(input).tokenize();
            final Program myPrgm = new Parser(tokens).parse();
            final Typechecker TC = new Typechecker(myPrgm);
            TC.typecheckProgram();
        }
    }

}

