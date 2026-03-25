// Generated from Delphi.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DelphiParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DelphiVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(DelphiParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procedureDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureDecl(DelphiParser.ProcedureDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDecl(DelphiParser.InterfaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(DelphiParser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procedureSignature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureSignature(DelphiParser.ProcedureSignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constructorDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDecl(DelphiParser.ConstructorDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#destructorDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDestructorDecl(DelphiParser.DestructorDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#objectCreation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectCreation(DelphiParser.ObjectCreationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(DelphiParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varDeclSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclSection(DelphiParser.VarDeclSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(DelphiParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(DelphiParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(DelphiParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(DelphiParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#writelnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWritelnStmt(DelphiParser.WritelnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(DelphiParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(DelphiParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldAccess(DelphiParser.FieldAccessContext ctx);
}