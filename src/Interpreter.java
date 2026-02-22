import java.util.*;

public class Interpreter extends DelphiBaseVisitor<Object> {

    private Map<String, Object> memory = new HashMap<>();
    private Map<String, ClassDef> classes = new HashMap<>();
    private ObjectInstance currentObject = null;

    // ================= ASSIGNMENT =================
    @Override
    public Object visitAssignment(DelphiParser.AssignmentContext ctx) {

        Object value = visit(ctx.expression());

        if (ctx.IDENTIFIER() != null) {

            String varName = ctx.IDENTIFIER().getText();

            // Global variable
            if (memory.containsKey(varName)) {
                memory.put(varName, value);
            }
            // Object field (inside constructor/destructor)
            else if (currentObject != null &&
                    currentObject.fields.containsKey(varName)) {

                currentObject.fields.put(varName, (Integer) value);
            } else {
                throw new RuntimeException("Unknown variable: " + varName);
            }

        } else {
            // Field access: p.age := ...
            DelphiParser.FieldAccessContext fieldCtx = ctx.fieldAccess();

            String objectName = fieldCtx.IDENTIFIER(0).getText();
            String fieldName = fieldCtx.IDENTIFIER(1).getText();

            ObjectInstance instance = (ObjectInstance) memory.get(objectName);

            if (instance == null) {
                throw new RuntimeException("Unknown object: " + objectName);
            }

            instance.fields.put(fieldName, (Integer) value);
        }

        return null;
    }

    // ================= FIELD ACCESS =================
    @Override
    public Object visitFieldAccess(DelphiParser.FieldAccessContext ctx) {

        String objectName = ctx.IDENTIFIER(0).getText();
        String fieldName = ctx.IDENTIFIER(1).getText();

        ObjectInstance instance = (ObjectInstance) memory.get(objectName);

        if (instance == null) {
            throw new RuntimeException("Unknown object: " + objectName);
        }

        return instance.fields.get(fieldName);
    }

    // ================= WRITELN =================
    @Override
    public Object visitWritelnStmt(DelphiParser.WritelnStmtContext ctx) {

        Object value = visit(ctx.expression());

        if (value == null) {
            System.out.println("null");
        } else if (value instanceof Integer) {
            System.out.println(value);
        } else if (value instanceof ObjectInstance) {
            System.out.println("Object of class: " +
                    ((ObjectInstance) value).classDef.name);
        } else {
            System.out.println(value);
        }

        return null;
    }

    // ================= EXPRESSION =================
    @Override
    public Object visitExpression(DelphiParser.ExpressionContext ctx) {

        if (ctx.INTEGER() != null) {
            return Integer.parseInt(ctx.INTEGER().getText());
        }

        if (ctx.IDENTIFIER() != null) {

            String varName = ctx.IDENTIFIER().getText();

            // 1️⃣ Global variable
            if (memory.containsKey(varName)) {
                return memory.get(varName);
            }

            // 2️⃣ Object field (constructor/destructor context)
            if (currentObject != null &&
                    currentObject.fields.containsKey(varName)) {

                return currentObject.fields.get(varName);
            }

            throw new RuntimeException("Unknown variable: " + varName);
        }

        return visitChildren(ctx);
    }

    // ================= CLASS DECLARATION =================
    @Override
    public Object visitClassDecl(DelphiParser.ClassDeclContext ctx) {

        String className = ctx.IDENTIFIER(0).getText();
        String parentName = null;
        if (ctx.IDENTIFIER().size() > 1) {
            parentName = ctx.IDENTIFIER(1).getText();
        }

        DelphiParser.ConstructorDeclContext constructor = ctx.classBody().constructorDecl();

        DelphiParser.DestructorDeclContext destructor = ctx.classBody().destructorDecl();

        List<String> fields = new ArrayList<>();

        if (ctx.classBody().varDeclSection() != null) {
            for (DelphiParser.VarDeclContext varDecl : ctx.classBody().varDeclSection().varDecl()) {

                fields.add(varDecl.IDENTIFIER().getText());
            }
        }

        ClassDef classDef = new ClassDef(className, fields,
                constructor, destructor, parentName);

        classes.put(className, classDef);

        if (parentName != null) {
            ClassDef parentClass = classes.get(parentName);
            if (parentClass == null) {
                throw new RuntimeException(
                        "Unknown parent class: " + parentName);
            }
            classDef.parent = parentClass;
        }
        for (DelphiParser.ProcedureDeclContext proc : ctx.classBody().procedureDecl()) {

            String methodName = proc.IDENTIFIER().getText();
            classDef.methods.put(methodName, proc);
        }
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

        if (classDef.parent != null && classDef.parent.constructor != null) {
            ObjectInstance previous = currentObject;
            currentObject = instance;
            visit(classDef.parent.constructor.block());
            currentObject = previous;
        }

        return instance;
    }

    // ================= VARIABLE DECLARATION =================
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

            memory.put(varName, null); // will be assigned later
        }

        return null;
    }

    // ================= METHOD CALL (DESTRUCTOR) =================
    @Override
    public Object visitMethodCall(DelphiParser.MethodCallContext ctx) {

        String objectName = ctx.IDENTIFIER(0).getText();
        String methodName = ctx.IDENTIFIER(1).getText();

        ObjectInstance instance = (ObjectInstance) memory.get(objectName);

        if (instance == null) {
            throw new RuntimeException("Unknown object: " + objectName);
        }

        ClassDef classDef = instance.classDef;

        // Destructor case
        if (methodName.equals("Destroy")) {

            if (classDef.destructor != null) {

                ObjectInstance previous = currentObject;
                currentObject = instance;

                visit(classDef.destructor.block());

                currentObject = previous;
            }

            return null;
        }

        // Normal method lookup
        DelphiParser.ProcedureDeclContext method = classDef.methods.get(methodName);

        // Check parent if not found
        ClassDef parent = classDef.parent;
        while (method == null && parent != null) {
            method = parent.methods.get(methodName);
            parent = parent.parent;
        }

        if (method == null) {
            throw new RuntimeException(
                    "Method not found: " + methodName);
        }

        ObjectInstance previous = currentObject;
        currentObject = instance;

        visit(method.block());

        currentObject = previous;

        return null;
    }
}
