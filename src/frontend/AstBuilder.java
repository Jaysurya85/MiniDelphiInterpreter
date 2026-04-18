import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstBuilder extends DelphiBaseVisitor<Object> {

    @Override
    public Object visitProgram(DelphiParser.ProgramContext ctx) {
        if (!ctx.interfaceDecl().isEmpty() || !ctx.classDecl().isEmpty()) {
            throw unsupported("Compiler AST builder only supports the procedural subset");
        }

        List<ProcedureDefNode> procedures = new ArrayList<>();
        for (DelphiParser.ProcedureDefContext procedureCtx : ctx.procedureDef()) {
            procedures.add((ProcedureDefNode) visit(procedureCtx));
        }

        List<FunctionDefNode> functions = new ArrayList<>();
        for (DelphiParser.FunctionDefContext functionCtx : ctx.functionDef()) {
            functions.add((FunctionDefNode) visit(functionCtx));
        }

        List<VarDeclNode> globalVars = buildVarDeclList(ctx.varDeclSection());
        BlockNode mainBlock = (BlockNode) visit(ctx.block());

        return new ProgramNode(procedures, functions, globalVars, mainBlock);
    }

    @Override
    public Object visitBlock(DelphiParser.BlockContext ctx) {
        List<Object> statements = new ArrayList<>();
        for (DelphiParser.StatementContext statementCtx : ctx.statement()) {
            statements.add(visit(statementCtx));
        }
        return new BlockNode(statements);
    }

    @Override
    public Object visitStatement(DelphiParser.StatementContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Object visitVarDecl(DelphiParser.VarDeclContext ctx) {
        if (!ctx.typeName().getText().equals("integer")) {
            throw unsupported("Only integer variables are supported in the compiler subset");
        }
        return new VarDeclNode(ctx.IDENTIFIER().getText(), ctx.typeName().getText());
    }

    @Override
    public Object visitAssignment(DelphiParser.AssignmentContext ctx) {
        if (ctx.fieldAccess() != null) {
            throw unsupported("Field assignment is outside the compiler subset");
        }

        return new AssignNode(ctx.IDENTIFIER().getText(), visit(ctx.expression()));
    }

    @Override
    public Object visitWhileStmt(DelphiParser.WhileStmtContext ctx) {
        return new WhileNode(visit(ctx.expression()), (BlockNode) visit(ctx.block()));
    }

    @Override
    public Object visitForStmt(DelphiParser.ForStmtContext ctx) {
        return new ForNode(
                ctx.IDENTIFIER().getText(),
                visit(ctx.expression(0)),
                visit(ctx.expression(1)),
                (BlockNode) visit(ctx.block()));
    }

    @Override
    public Object visitBreakStmt(DelphiParser.BreakStmtContext ctx) {
        return new BreakNode();
    }

    @Override
    public Object visitContinueStmt(DelphiParser.ContinueStmtContext ctx) {
        return new ContinueNode();
    }

    @Override
    public Object visitProcedureDef(DelphiParser.ProcedureDefContext ctx) {
        return new ProcedureDefNode(
                ctx.IDENTIFIER().getText(),
                buildParameterNames(ctx.formalParams()),
                (BlockNode) visit(ctx.block()));
    }

    @Override
    public Object visitFunctionDef(DelphiParser.FunctionDefContext ctx) {
        return new FunctionDefNode(
                ctx.IDENTIFIER().getText(),
                buildParameterNames(ctx.formalParams()),
                (BlockNode) visit(ctx.block()));
    }

    @Override
    public Object visitProcedureCall(DelphiParser.ProcedureCallContext ctx) {
        return new ProcedureCallNode(ctx.IDENTIFIER().getText(), buildArgumentList(ctx.actualParams()));
    }

    @Override
    public Object visitFunctionCall(DelphiParser.FunctionCallContext ctx) {
        return new FunctionCallNode(ctx.IDENTIFIER().getText(), buildArgumentList(ctx.actualParams()));
    }

    @Override
    public Object visitReturnStmt(DelphiParser.ReturnStmtContext ctx) {
        return new ReturnNode(visit(ctx.expression()));
    }

    @Override
    public Object visitWritelnStmt(DelphiParser.WritelnStmtContext ctx) {
        return new WriteLnNode(visit(ctx.expression()));
    }

    @Override
    public Object visitExpression(DelphiParser.ExpressionContext ctx) {
        return visit(ctx.additiveExpr());
    }

    @Override
    public Object visitAdditiveExpr(DelphiParser.AdditiveExprContext ctx) {
        Object expr = visit(ctx.multiplicativeExpr(0));
        for (int i = 1; i < ctx.multiplicativeExpr().size(); i++) {
            expr = new BinaryExprNode(
                    ctx.getChild((2 * i) - 1).getText(),
                    expr,
                    visit(ctx.multiplicativeExpr(i)));
        }
        return expr;
    }

    @Override
    public Object visitMultiplicativeExpr(DelphiParser.MultiplicativeExprContext ctx) {
        Object expr = visit(ctx.primaryExpr(0));
        for (int i = 1; i < ctx.primaryExpr().size(); i++) {
            expr = new BinaryExprNode(
                    ctx.getChild((2 * i) - 1).getText(),
                    expr,
                    visit(ctx.primaryExpr(i)));
        }
        return expr;
    }

    @Override
    public Object visitPrimaryExpr(DelphiParser.PrimaryExprContext ctx) {
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        if (ctx.INTEGER() != null) {
            return new IntLiteralNode(Integer.parseInt(ctx.INTEGER().getText()));
        }
        if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        }
        if (ctx.IDENTIFIER() != null) {
            return new VarRefNode(ctx.IDENTIFIER().getText());
        }
        if (ctx.fieldAccess() != null) {
            throw unsupported("Field access is outside the compiler subset");
        }
        if (ctx.objectCreation() != null) {
            throw unsupported("Object creation is outside the compiler subset");
        }

        throw unsupported("Unsupported primary expression");
    }

    @Override
    public Object visitClassDecl(DelphiParser.ClassDeclContext ctx) {
        throw unsupported("Classes are outside the compiler subset");
    }

    @Override
    public Object visitInterfaceDecl(DelphiParser.InterfaceDeclContext ctx) {
        throw unsupported("Interfaces are outside the compiler subset");
    }

    @Override
    public Object visitMethodCall(DelphiParser.MethodCallContext ctx) {
        throw unsupported("Methods are outside the compiler subset");
    }

    @Override
    public Object visitObjectCreation(DelphiParser.ObjectCreationContext ctx) {
        throw unsupported("Object creation is outside the compiler subset");
    }

    @Override
    public Object visitFieldAccess(DelphiParser.FieldAccessContext ctx) {
        throw unsupported("Field access is outside the compiler subset");
    }

    private List<VarDeclNode> buildVarDeclList(DelphiParser.VarDeclSectionContext ctx) {
        if (ctx == null) {
            return Collections.emptyList();
        }

        List<VarDeclNode> declarations = new ArrayList<>();
        for (DelphiParser.VarDeclContext varDeclContext : ctx.varDecl()) {
            declarations.add((VarDeclNode) visit(varDeclContext));
        }
        return declarations;
    }

    private List<String> buildParameterNames(DelphiParser.FormalParamsContext ctx) {
        if (ctx == null) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<>();
        for (var identifier : ctx.IDENTIFIER()) {
            names.add(identifier.getText());
        }
        return names;
    }

    private List<Object> buildArgumentList(DelphiParser.ActualParamsContext ctx) {
        if (ctx == null) {
            return Collections.emptyList();
        }

        List<Object> arguments = new ArrayList<>();
        for (DelphiParser.ExpressionContext expressionCtx : ctx.expression()) {
            arguments.add(visit(expressionCtx));
        }
        return arguments;
    }

    private RuntimeException unsupported(String message) {
        return new UnsupportedOperationException(message);
    }
}
