// Generated from Delphi.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DelphiParser}.
 */
public interface DelphiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(DelphiParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(DelphiParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constructorDecl}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDecl(DelphiParser.ConstructorDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constructorDecl}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDecl(DelphiParser.ConstructorDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#objectCreation}.
	 * @param ctx the parse tree
	 */
	void enterObjectCreation(DelphiParser.ObjectCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#objectCreation}.
	 * @param ctx the parse tree
	 */
	void exitObjectCreation(DelphiParser.ObjectCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varDeclSection}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclSection(DelphiParser.VarDeclSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varDeclSection}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclSection(DelphiParser.VarDeclSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(DelphiParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(DelphiParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(DelphiParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(DelphiParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(DelphiParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(DelphiParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(DelphiParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(DelphiParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#writelnStmt}.
	 * @param ctx the parse tree
	 */
	void enterWritelnStmt(DelphiParser.WritelnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#writelnStmt}.
	 * @param ctx the parse tree
	 */
	void exitWritelnStmt(DelphiParser.WritelnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(DelphiParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(DelphiParser.FieldAccessContext ctx);
}