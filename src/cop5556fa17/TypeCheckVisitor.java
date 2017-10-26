package cop5556fa17;

import cop5556fa17.AST.*;
import cop5556fa17.Scanner.Token;

import java.util.HashMap;

public class TypeCheckVisitor implements ASTVisitor {

	HashMap<String, Declaration> symbolTable;

	public TypeCheckVisitor(){
		symbolTable = new HashMap<>();
	}

	@SuppressWarnings("serial")
	public static class SemanticException extends Exception {
		Token t;

		public SemanticException(Token t, String message) {
			super("line " + t.line + " pos " + t.pos_in_line + ": "+  message);
			this.t = t;
		}

	}

	/**
	 * The program name is only used for naming the class.  It does not rule out
	 * variables with the same name.  It is returned for convenience.
	 * 
	 * @throws Exception 
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (ASTNode node: program.decsAndStatements) {
			node.visit(this, arg);
		}
		return program.name;
	}

	@Override
	public Object visitDeclaration_Variable( Declaration_Variable declaration_Variable, Object arg) throws Exception {
		if (!symbolTable.containsKey(declaration_Variable.name)){
			symbolTable.put(declaration_Variable.name, declaration_Variable);
			declaration_Variable.t = TypeUtils.getType(declaration_Variable.type);
			return declaration_Variable;
		}
		else {
			throw new SemanticException(declaration_Variable.firstToken, "Semantic Exception at line: " + declaration_Variable.firstToken.line + " position: " + declaration_Variable.firstToken.pos_in_line + ". This identifier has already been declared!");
		}
	}

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PixelSelector(
			Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Conditional(
			Expression_Conditional expression_Conditional, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		if (!symbolTable.containsKey(declaration_Image.name)){
			symbolTable.put(declaration_Image.name, declaration_Image);
			declaration_Image.type = TypeUtils.Type.IMAGE;
			return declaration_Image;
		}
		else {
			throw new SemanticException(declaration_Image.firstToken, "Semantic Exception at line: " + declaration_Image.firstToken.line + " position: " + declaration_Image.firstToken.pos_in_line + ". This identifier has already been declared!");
		}
	}

	@Override
	public Object visitSource_StringLiteral(
			Source_StringLiteral source_StringLiteral, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSource_CommandLineParam(
			Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitDeclaration_SourceSink( Declaration_SourceSink declaration_SourceSink, Object arg) throws Exception {
		if (!symbolTable.containsKey(declaration_SourceSink.name)){
			symbolTable.put(declaration_SourceSink.name, declaration_SourceSink);
			declaration_SourceSink.t = TypeUtils.getType(declaration_SourceSink.type);
			return declaration_SourceSink;
		}
		else {
			throw new SemanticException(declaration_SourceSink.firstToken, "Semantic Exception at line: " + declaration_SourceSink.firstToken.line + " position: " + declaration_SourceSink.firstToken.pos_in_line + ". This identifier has already been declared!");
		}
	}

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		expression_IntLit.type = TypeUtils.Type.INTEGER;
		return expression_IntLit;
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg) throws Exception {
		expression_PredefinedName.type = TypeUtils.Type.INTEGER;
		return expression_PredefinedName;
	}

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		sink_SCREEN.type = TypeUtils.Type.SCREEN;
		return sink_SCREEN;
	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {
		expression_BooleanLit.type = TypeUtils.Type.BOOLEAN;
		return expression_BooleanLit;
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
