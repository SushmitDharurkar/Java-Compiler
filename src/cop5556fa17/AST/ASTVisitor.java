package cop5556fa17.AST;

public interface ASTVisitor {

	Object visitProgram(Program program, Object arg) throws Exception;

	Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg) throws Exception;

	Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception;

	Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception;

	Object visitIndex(Index index, Object arg) throws Exception;

	Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg) throws Exception;

	Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg) throws Exception;

	Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception;

	Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception;

	Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg) throws Exception;

	Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception;

	Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg) throws Exception;

	Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception;

	Object visitExpression_FunctionAppWithExprArg(Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg,
                                                  Object arg) throws Exception;

	Object visitExpression_FunctionAppWithIndexArg(Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg,
                                                   Object arg) throws Exception;

	Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg) throws Exception;

	Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception;;

	Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception;

	Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception;

	Object visitLHS(LHS lhs, Object arg) throws Exception;

	Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception;

	Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception;

	Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception;

	Object visitExpression_Ident(Expression_Ident expression_Ident, Object arg) throws Exception;

	
}
