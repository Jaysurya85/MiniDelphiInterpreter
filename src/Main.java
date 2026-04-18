import org.antlr.v4.runtime.*;
public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 0 || args.length > 2) {
            System.out.println("Usage: java Main <sourcefile>");
            return;
        }

        String filename = args.length == 2 ? args[1] : args[0];
        CharStream input = CharStreams.fromFileName(filename);

        DelphiLexer lexer = new DelphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DelphiParser parser = new DelphiParser(tokens);

        DelphiParser.ProgramContext tree = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("Parsing failed with syntax errors");
        }

        AstBuilder builder = new AstBuilder();
        ProgramNode program = (ProgramNode) builder.visit(tree);
        AstPrinter printer = new AstPrinter();
        System.out.print(printer.print(program));
    }
}
