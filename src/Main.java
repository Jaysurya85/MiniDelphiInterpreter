import org.antlr.v4.runtime.*;
public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 1 && args[0].equals("--expr-test")) {
            runExprTest();
            return;
        }

        if (args.length != 1) {
            System.out.println("Usage: java Main <sourcefile>");
            System.out.println("   or: java Main --expr-test");
            return;
        }

        String filename = args[0];
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
        LLVMGenerator generator = new LLVMGenerator();
        System.out.print(generator.generateProgram(program));
    }

    private static void runExprTest() {
        ExprNode expr = new BinaryExprNode(
                "+",
                new IntLiteralNode(2),
                new IntLiteralNode(3));

        LLVMGenerator generator = new LLVMGenerator();
        generator.generateExpr(expr);
        System.out.print(generator.getCode());
    }
}
