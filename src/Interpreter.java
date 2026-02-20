import java.util.*;

public class Interpreter extends DelphiBaseVisitor<Object> {

    private Map<String, Object> memory = new HashMap<>();
    private Map<String, ClassDef> classes = new HashMap<>();
    private ObjectInstance currentObject = null;

    @Override
    public Object visitAssignment(DelphiParser.AssignmentContext ctx) {

        Object value = visit(ctx.expression());

        if (ctx.IDENTIFIER() != null) {

            String varName = ctx.IDENTIFIER().getText();

            // If normal variable
            if (memory.containsKey(varName)) {
                memory.put(varName, value);
            }
            // If inside constructor → field assignment
            else if (currentObject != null) {
                currentObject.fields.put(varName, (Integer) value);
            } else {
                throw new RuntimeException("Unknown variable: " + varName);
            }
        } else {
            // Field access p.age := ...
            DelphiParser.FieldAccessContext fieldCtx = ctx.fieldAccess();

            String objectName = fieldCtx.IDENTIFIER(0).getText();
            String fieldName = fieldCtx.IDENTIFIER(1).getText();

            ObjectInstance instance = (ObjectInstance) memory.get(objectName);
            instance.fields.put(fieldName, (Integer) value);
        }

        return null;
    }

    @Override
    public Object visitFieldAccess(DelphiParser.FieldAccessContext ctx) {

        String objectName = ctx.IDENTIFIER(0).getText();
        String fieldName = ctx.IDENTIFIER(1).getText();

        ObjectInstance instance = (ObjectInstance) memory.get(objectName);
        return instance.fields.get(fieldName);
    }

    @Override
    public Object visitWritelnStmt(DelphiParser.WritelnStmtContext ctx) {
        Object value = visit(ctx.expression());
        if (value instanceof Integer) {
            System.out.println(value);
        } else {
            System.out.println("Object of class: " + ((ObjectInstance) value).classDef.name);
        }
        return null;
    }

    @Override
    public Object visitExpression(DelphiParser.ExpressionContext ctx) {

        if (ctx.INTEGER() != null) {
            return Integer.parseInt(ctx.INTEGER().getText());
        }

        if (ctx.IDENTIFIER() != null) {
            return memory.get(ctx.IDENTIFIER().getText());
        }
        return visitChildren(ctx);
    }

    @Override
    public Object visitClassDecl(DelphiParser.ClassDeclContext ctx) {
        String className = ctx.IDENTIFIER().getText();

        DelphiParser.ConstructorDeclContext constructor = ctx.classBody().constructorDecl();
        List<String> fields = new ArrayList<>();

        if (ctx.classBody().varDeclSection() != null) {
            for (DelphiParser.VarDeclContext varDecl : ctx.classBody().varDeclSection().varDecl()) {

                String fieldName = varDecl.IDENTIFIER().getText();
                fields.add(fieldName);
            }
        }

        ClassDef classDef = new ClassDef(className, fields, constructor);
        classes.put(className, classDef);
        System.out.println("Registered class: " + className);
        return null;
    }

    @Override
    public Object visitObjectCreation(DelphiParser.ObjectCreationContext ctx) {

        String className = ctx.IDENTIFIER(0).getText();

        ClassDef classDef = classes.get(className);

        if (classDef == null) {
            throw new RuntimeException("Unknown class: " + className);
        }

        ObjectInstance instance = new ObjectInstance(classDef);

        // Run constructor
        if (classDef.constructor != null) {

            ObjectInstance previous = currentObject;
            currentObject = instance;

            visit(classDef.constructor.block());

            currentObject = previous;
        }

        return instance;
    }

    @Override
    public Object visitVarDecl(DelphiParser.VarDeclContext ctx) {

        String varName = ctx.IDENTIFIER().getText();
        String typeName = ctx.typeName().getText();

        if (typeName.equals("integer")) {
            memory.put(varName, 0);
        } else {
            ClassDef classDef = classes.get(typeName);
            if (classDef == null) {
                throw new RuntimeException("Unknown class: " + typeName);
            }

            ObjectInstance instance = new ObjectInstance(classDef);
            memory.put(varName, instance);
        }

        return null;
    }
}
