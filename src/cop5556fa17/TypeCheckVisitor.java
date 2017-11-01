package cop5556fa17;

import cop5556fa17.AST.*;
import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

import java.net.MalformedURLException;
import java.net.URL;
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

//	Declaration_Variable​ ​ ::=​ ​ ​ Type​ ​ name​ ​ (Expression​ ​ | ​ ​ ε ​ ​ )

	@Override
	public Object visitDeclaration_Variable( Declaration_Variable declaration_Variable, Object arg) throws Exception {
		if (!symbolTable.containsKey(declaration_Variable.name)){
			declaration_Variable.setType(TypeUtils.getType(declaration_Variable.type));
			if (declaration_Variable.e != null){
				declaration_Variable.e.visit(this, arg);
				if (declaration_Variable.e.getType() != declaration_Variable.getType()){
					throw new SemanticException(declaration_Variable.firstToken, "Semantic Exception found! Type mismatch.");
				}
			}
			symbolTable.put(declaration_Variable.name, declaration_Variable);
			return declaration_Variable;
		}
		else {
			throw new SemanticException(declaration_Variable.firstToken, "Semantic Exception found! This identifier has already been declared!");
		}
	}

//	Expression_Binary​ ​ ::=​ ​ Expression​_0​ ​ ​ op​ ​ Expression​_1

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception {
		if(expression_Binary.e0 != null && expression_Binary.e1 != null){
			expression_Binary.e0.visit(this, arg);
			expression_Binary.e1.visit(this, arg);

			TypeUtils.Type e0Type = expression_Binary.e0.getType();
			TypeUtils.Type e1Type = expression_Binary.e1.getType();
			if (e0Type != e1Type){
				throw new SemanticException(expression_Binary.firstToken, "Semantic Exception found! Type mismatch.");
			}
			else {
				if(expression_Binary.op == Kind.OP_EQ || expression_Binary.op == Kind.OP_NEQ){
					expression_Binary.setType(TypeUtils.Type.BOOLEAN);
				}
				else if ((expression_Binary.op == Kind.OP_GE || expression_Binary.op == Kind.OP_GT || expression_Binary.op == Kind.OP_LT || expression_Binary.op == Kind.OP_LE) && e0Type == TypeUtils.Type.INTEGER){
					expression_Binary.setType(TypeUtils.Type.BOOLEAN);
				}
				else if ((expression_Binary.op == Kind.OP_AND || expression_Binary.op == Kind.OP_OR) && (e0Type == TypeUtils.Type.INTEGER || e0Type == TypeUtils.Type.BOOLEAN)){
					expression_Binary.setType(e0Type);
				}
				else if ((expression_Binary.op == Kind.OP_DIV || expression_Binary.op == Kind.OP_MINUS || expression_Binary.op == Kind.OP_MOD || expression_Binary.op == Kind.OP_PLUS || expression_Binary.op == Kind.OP_POWER || expression_Binary.op == Kind.OP_TIMES) && e0Type == TypeUtils.Type.INTEGER){
					expression_Binary.setType(TypeUtils.Type.INTEGER);
				}
				else {
					throw new SemanticException(expression_Binary.firstToken, "Semantic Exception found! Unable to determine type.");
				}
			}
		}
		else {
			throw new SemanticException(expression_Binary.firstToken, "Semantic Exception found! Expressions cannot be null.");
		}
		return expression_Binary;
	}

//	Expression_Unary​ ​ ::=​ ​ op​ ​ Expression

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception {
		if (expression_Unary.e != null){
			expression_Unary.e.visit(this, arg);

			TypeUtils.Type eType = expression_Unary.e.getType();

			if ((expression_Unary.op == Kind.OP_EXCL) && (eType == TypeUtils.Type.BOOLEAN || eType == TypeUtils.Type.INTEGER)){
				expression_Unary.setType(eType);
			}
			else if ((expression_Unary.op == Kind.OP_PLUS || expression_Unary.op == Kind.OP_MINUS) && eType == TypeUtils.Type.INTEGER){
				expression_Unary.setType(TypeUtils.Type.INTEGER);
			}
			else {
				throw new SemanticException(expression_Unary.firstToken, "Semantic Exception found! Unable to determine type.");
			}
		}
		else {
			throw new SemanticException(expression_Unary.firstToken, "Semantic Exception found! Expression cannot be null.");
		}
		return expression_Unary;
	}

//	Index​ ​ ::=​ ​ Expression​_0​ ​ ​ Expression​_1

	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		if (index.e0 != null && index.e1 != null){
			index.e0.visit(this, arg);
			index.e1.visit(this, arg);

			if (index.e0.getType() == TypeUtils.Type.INTEGER && index.e1.getType() == TypeUtils.Type.INTEGER){
				try{
					Expression_PredefinedName e0 = (Expression_PredefinedName) index.e0;
					Expression_PredefinedName e1 = (Expression_PredefinedName) index.e1;
					if (!(e0.kind == Kind.KW_r && e1.kind == Kind.KW_A)){
						index.setCartesian(true);
					}
					else {
						index.setCartesian(false);
					}
					return index;
				}
				catch (ClassCastException e){	//For this case Selector​ ​ ::=​ ​ ​ Expression​ ​ COMMA​ ​ Expression
					index.setCartesian(true);
					return index;
				}
			}
			else {
				throw new SemanticException(index.firstToken, "Semantic Exception found! INTEGER types expected.");
			}
		}
		else {
			throw new SemanticException(index.firstToken, "Semantic Exception found! Expressions cannot be null.");
		}
	}

//	Expression_PixelSelector​ ​ ::=​ ​ ​ ​ name​ ​ Index

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg) throws Exception {
		//Note Should Index and name type be checked for a match?
		Declaration d = symbolTable.get(expression_PixelSelector.name);

		if (expression_PixelSelector.index != null){
			expression_PixelSelector.index.visit(this, arg);
		}

		if (d != null){
			TypeUtils.Type type = d.getType();

			if (type == TypeUtils.Type.IMAGE){
				expression_PixelSelector.setType(TypeUtils.Type.INTEGER);
			}
			else if (expression_PixelSelector.index == null){
				expression_PixelSelector.setType(type);
			}
			else {
				throw new SemanticException(expression_PixelSelector.firstToken, "Semantic Exception found! Unable to determine type.");
			}
			return expression_PixelSelector;
		}
		else {
			throw new SemanticException(expression_PixelSelector.firstToken, "Semantic Exception found! Identifier not declared.");
		}
	}

//	Expression_Conditional​ ​ ::=​ ​ ​ Expression​_condition​ ​ ​ Expression​_true​ ​ ​ Expression​_false

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg) throws Exception {
		if (expression_Conditional.condition != null && expression_Conditional.trueExpression != null && expression_Conditional.falseExpression != null){
			expression_Conditional.condition.visit(this, arg);
			expression_Conditional.trueExpression.visit(this, arg);
			expression_Conditional.falseExpression.visit(this, arg);

			if ((expression_Conditional.condition.getType() == TypeUtils.Type.BOOLEAN) && (expression_Conditional.trueExpression.getType() == expression_Conditional.falseExpression.getType()) ){
				expression_Conditional.setType(expression_Conditional.trueExpression.getType());
				return expression_Conditional;
			}
			else {
				throw new SemanticException(expression_Conditional.firstToken, "Semantic Exception found! Type mismatch.");
			}
		}
		else {
			throw new SemanticException(expression_Conditional.firstToken, "Semantic Exception found! Expressions cannot be null.");
		}
	}

//	Declaration_Image​ ​ ​ ::=​ ​ name​ ​ ( ​ ​ ​ xSize​ ​ ySize​ ​ | ​ ​ ε )​ ​ Source

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		if (!symbolTable.containsKey(declaration_Image.name)){
			if (declaration_Image.xSize != null){
				if (declaration_Image.ySize != null){
					declaration_Image.xSize.visit(this, arg);
					declaration_Image.ySize.visit(this, arg);
					if (declaration_Image.xSize.getType() != TypeUtils.Type.INTEGER || declaration_Image.ySize.getType() != TypeUtils.Type.INTEGER){
						throw new SemanticException(declaration_Image.firstToken, "Semantic Exception found! INTEGER type expected.");
					}
				}
				else {
					throw new SemanticException(declaration_Image.firstToken, "Semantic Exception found! ySize cannot be null.");
				}
			}

			if (declaration_Image.source != null){
				declaration_Image.source.visit(this, arg);
			}

			declaration_Image.setType(TypeUtils.Type.IMAGE);
			symbolTable.put(declaration_Image.name, declaration_Image);
			return declaration_Image;
		}
		else {
			throw new SemanticException(declaration_Image.firstToken, "Semantic Exception found! This identifier has already been declared!");
		}
	}

//	Source_StringLiteral​ ​ ::=​ ​ ​ fileOrURL

	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception {
		try {
			new URL(source_StringLiteral.fileOrUrl);
			source_StringLiteral.setType(TypeUtils.Type.URL);
			return source_StringLiteral;
		}
		catch (MalformedURLException e){
			source_StringLiteral.setType(TypeUtils.Type.FILE);
			return source_StringLiteral;
		}
	}

//	Source_CommandLineParam​ ​ ​ ::=​ ​ Expression​_paramNum

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg) throws Exception {
		if (source_CommandLineParam.paramNum != null){
			source_CommandLineParam.paramNum.visit(this, arg);

			TypeUtils.Type type = source_CommandLineParam.paramNum.getType();
			if (type == TypeUtils.Type.INTEGER) {
				source_CommandLineParam.setType(type);
			}
			else {
				throw new SemanticException(source_CommandLineParam.firstToken, "Semantic Exception found! INTEGER type expected.");
			}
		}
		else {
			throw new SemanticException(source_CommandLineParam.firstToken, "Semantic Exception found! Expression_paranNum cannot be null.");
		}
		return source_CommandLineParam;
	}

//	Source_Ident​ ​ ::=​ ​ name

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		Declaration d = symbolTable.get(source_Ident.name);

		if (d != null){
			source_Ident.setType(d.getType());
			if (source_Ident.getType() == TypeUtils.Type.FILE || source_Ident.getType() == TypeUtils.Type.URL) {
				return source_Ident;
			}
			else {
				throw new SemanticException(source_Ident.firstToken, "Semantic Exception found! FILE or URL type expected.");
			}
		}
		else {
			throw new SemanticException(source_Ident.firstToken, "Semantic Exception found! Identifier not declared.");
		}


	}

//	Declaration_SourceSink​ ​ ​ ::=​ ​ Type​ ​ name​ ​ ​ Source

	@Override
	public Object visitDeclaration_SourceSink( Declaration_SourceSink declaration_SourceSink, Object arg) throws Exception {

		if (!symbolTable.containsKey(declaration_SourceSink.name)) {
			declaration_SourceSink.setType(TypeUtils.getType(declaration_SourceSink.type));
			if (declaration_SourceSink.source != null){
				declaration_SourceSink.source.visit(this, arg);
				if (declaration_SourceSink.source.getType() == declaration_SourceSink.getType()){
					symbolTable.put(declaration_SourceSink.name, declaration_SourceSink);
				}
				else {
					throw new SemanticException(declaration_SourceSink.firstToken, "Semantic Exception found! Type mismatch.");
				}
			}
			else {
				throw new SemanticException(declaration_SourceSink.firstToken, "Semantic Exception found! Source cannot be null.");
			}
			return declaration_SourceSink;
		}
		else {
			throw new SemanticException(declaration_SourceSink.firstToken, "Semantic Exception found! This identifier has already been declared!");
		}
	}

//	Expression_IntLit​ ​ ::=​ ​ ​ value

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		expression_IntLit.setType(TypeUtils.Type.INTEGER);
		return expression_IntLit;
	}

//	Expression_FunctionAppWithExprArg​ ​ ::=​ ​ ​ function​ ​ Expression

	@Override
	public Object visitExpression_FunctionAppWithExprArg(Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		if (expression_FunctionAppWithExprArg.arg != null){
			expression_FunctionAppWithExprArg.arg.visit(this, arg);

			if (expression_FunctionAppWithExprArg.arg.getType() == TypeUtils.Type.INTEGER){
				expression_FunctionAppWithExprArg.setType(TypeUtils.Type.INTEGER);
			}
			else {
				throw new SemanticException(expression_FunctionAppWithExprArg.firstToken, "Semantic Exception found! INTEGER type expected.");
			}
		}
		else {
			throw new SemanticException(expression_FunctionAppWithExprArg.firstToken, "Semantic Exception found! Expression cannot be null.");
		}
		return expression_FunctionAppWithExprArg;
	}

//	Expression_FunctionAppWithIndexArg​ ​ ::=​ ​ ​ ​ function​ ​ Index

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		if (expression_FunctionAppWithIndexArg.arg != null){
			expression_FunctionAppWithIndexArg.arg.visit(this, arg);
		}

		expression_FunctionAppWithIndexArg.setType(TypeUtils.Type.INTEGER);
		return expression_FunctionAppWithIndexArg;
	}

//	Expression_PredefinedName​ ​ ::=​ ​ ​ predefNameKind

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg) throws Exception {
		expression_PredefinedName.setType(TypeUtils.Type.INTEGER);
		return expression_PredefinedName;
	}

//	Statement_Out​ ​ ::=​ ​ name​ ​ Sink

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception {
		Declaration d = symbolTable.get(statement_Out.name);

		if (d != null){
			if (statement_Out.sink != null){
				statement_Out.sink.visit(this, arg);

				if (((d.getType() == TypeUtils.Type.INTEGER || d.getType() == TypeUtils.Type.BOOLEAN) && statement_Out.sink.getType() == TypeUtils.Type.SCREEN) || (d.getType() == TypeUtils.Type.IMAGE && (statement_Out.sink.getType() == TypeUtils.Type.FILE || statement_Out.sink.getType() == TypeUtils.Type.SCREEN)))  {
					statement_Out.setDec(d);
				}
				else {
					throw new SemanticException(statement_Out.firstToken, "Semantic Exception found! Type mismatch.");
				}
			}
			else {
				throw new SemanticException(statement_Out.firstToken, "Semantic Exception found! Sink cannot be null.");
			}
		}
		else {
			throw new SemanticException(statement_Out.firstToken, "Semantic Exception found! Identifier not declared.");
		}
		return statement_Out;
	}

//	Statement_In​ ​ ::=​ ​ name​ ​ Source

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception {
		Declaration d = symbolTable.get(statement_In.name);

		if (d != null){
			if (statement_In.source != null) {
				statement_In.source.visit(this, arg);

				if (d.getType() == statement_In.source.getType()){
					statement_In.setDec(d);
				}
				else {
					throw new SemanticException(statement_In.firstToken, "Semantic Exception found! Type mismatch.");
				}
			}
			else {
				throw new SemanticException(statement_In.firstToken, "Semantic Exception found! Source cannot be null.");
			}
		}
		else {
			throw new SemanticException(statement_In.firstToken, "Semantic Exception found! Identifier not declared.");
		}
		return statement_In;
	}

//	Statement_Assign​ ​ ::=​ ​ ​ LHS​ ​ ​ Expression

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception {
		if (statement_Assign.lhs != null && statement_Assign.e != null){
			statement_Assign.lhs.visit(this, arg);
			statement_Assign.e.visit(this, arg);

			if (statement_Assign.lhs.getType() == statement_Assign.e.getType()){
				statement_Assign.setCartesian(statement_Assign.lhs.isCartesian());
			}
			else {
				throw new SemanticException(statement_Assign.firstToken, "Semantic Exception found! Type mismatch.");
			}
		}
		else {
			throw new SemanticException(statement_Assign.firstToken, "Semantic Exception found! LHS or Expression cannot be null.");
		}
		return statement_Assign;
	}

//	LHS​ ​ ::=​ ​ name​ ​ Index

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		Declaration d = symbolTable.get(lhs.name);

		if (d != null){
			if (lhs.index != null){
				lhs.index.visit(this, arg);
				lhs.setCartesian(lhs.index.isCartesian());
			}
			lhs.setDeclaration(d);
			lhs.setType(d.getType());
			return lhs;
		}
		else {
			throw new SemanticException(lhs.firstToken, "Semantic Exception found! Identifier not declared.");
		}
	}

//	Sink_SCREEN​ ​ ::=​ ​ SCREEN

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		sink_SCREEN.setType(TypeUtils.Type.SCREEN);
		return sink_SCREEN;
	}

//	Sink_Ident​ ​ ::=​ ​ name

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		Declaration d = symbolTable.get(sink_Ident.name);

		if (d != null){
			sink_Ident.setType(d.getType());
			if (sink_Ident.getType() == TypeUtils.Type.FILE) {
				return sink_Ident;
			}
			else {
				throw new SemanticException(sink_Ident.firstToken, "Semantic Exception found! FILE type expected.");
			}
		}
		else {
			throw new SemanticException(sink_Ident.firstToken, "Semantic Exception found! Identifier not declared.");
		}
	}

//	Expression_BooleanLit​ ​ ::=​ ​ ​ value

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {
		expression_BooleanLit.setType(TypeUtils.Type.BOOLEAN);
		return expression_BooleanLit;
	}

//	Expression_Ident​ ​ ​ ::=​ ​ ​ ​ name

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident, Object arg) throws Exception {
		Declaration d = symbolTable.get(expression_Ident.name);

		if (d != null){
			expression_Ident.setType(d.getType());
			return expression_Ident;
		}
		else {
			throw new SemanticException(expression_Ident.firstToken, "Semantic Exception found! Identifier not declared.");
		}
	}

}
