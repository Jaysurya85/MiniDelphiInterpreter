import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Usage: java Main <sourcefile>");
            return;
        }

        String filename = args[0];
        CharStream input = CharStreams.fromFileName(filename);

        DelphiLexer lexer = new DelphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DelphiParser parser = new DelphiParser(tokens);

        ParseTree tree = parser.program();

        Interpreter interpreter = new Interpreter();
        interpreter.visit(tree);
    }
}
