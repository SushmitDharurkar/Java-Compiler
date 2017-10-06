package cop5556fa17;



import cop5556fa17.AST.*;
import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

import java.util.ArrayList;

import static cop5556fa17.Scanner.Kind.*;

public class Parser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}


	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public Program parse() throws SyntaxException {
		Program p = program();
		matchEOF();
		return p;
	}

	void consume(){
		t = scanner.nextToken();
	}

	Token match(Kind k) throws SyntaxException{
		if (t.kind == k){
			Token temp = t;
			consume();
			return temp;
		}
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line +
			".\nExpected token: " + k);
		}
	}
	

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	Program program() throws SyntaxException {
		if(t.kind == IDENTIFIER){
			Token name = t, firstToken = t;
			consume();
			ArrayList<ASTNode> decsAndStatements = new ArrayList<>();

			while (t.kind == KW_int || t.kind == KW_boolean || t.kind == KW_image
					|| t.kind == KW_url || t.kind == KW_file || t.kind == IDENTIFIER){

				if (t.kind == KW_int || t.kind == KW_boolean || t.kind == KW_image
						|| t.kind == KW_url || t.kind == KW_file){
					decsAndStatements.add(declaration());
				}
				else if (t.kind == IDENTIFIER){
					decsAndStatements.add(statement());
				}
				match(SEMI);
			}

			return new Program(firstToken, name, decsAndStatements);
		}
		else {
		    throw new SyntaxException(t, "Input not valid. \nProgram should start with an IDENTIFIER.");
        }
	}

	/*
	* Declaration​ ​ ::​ ​ = ​ ​ ​ VariableDeclaration​ ​ ​ ​ ​ ​ | ​ ​ ​ ​ ​ ImageDeclaration​ ​ ​ ​ | ​ ​ ​ ​ SourceSinkDeclaration
	* */

	Declaration declaration() throws SyntaxException{
		Declaration d = null;
		if (t.kind == KW_int || t.kind == KW_boolean){
			d = variableDeclaration();
		}
		else if (t.kind == KW_image){
			d = imageDeclaration();
		}
		else if (t.kind == KW_url || t.kind == KW_file){
			d = sourceSinkDeclaration();
		}
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
		return d;
	}

	/*
	* VariableDeclaration​ ​ ​ ::=​ ​ ​ VarType​ ​ IDENTIFIER​ ​ ​ ( ​ ​ ​ = ​ ​ ​ Expression​ ​ ​ | ​ ​ ε ​ ​ )
	* */

	Declaration_Variable variableDeclaration() throws SyntaxException{
		Expression e = null;
		Token type = varType();
		Token firstToken = type;
		Token name = match(IDENTIFIER);
		if (t.kind == OP_ASSIGN){
			consume();
			e = expression();
		}
		return new Declaration_Variable(firstToken, type, name, e);
	}

	/*
	* VarType​ ​ ::=​ ​ KW_int​ ​ | ​ ​ KW_boolean
	* */

	Token varType() throws SyntaxException{
	    Token type = null;
		if (t.kind == KW_int){
	        type = t;
	    	consume();
        }
        else if (t.kind == KW_boolean){
			type = t;
	    	consume();
        }
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
		return type;
    }

    /*
    * SourceSinkDeclaration​ ​ ::=​ ​ SourceSinkType​ ​ IDENTIFIER​ ​ ​ OP_ASSIGN​ ​ ​ Source
    * */

	Declaration_SourceSink sourceSinkDeclaration() throws SyntaxException{
		Token type = sourceSinkType();
		Token firstToken = type;
		Token name = match(IDENTIFIER);
		match(OP_ASSIGN);
		Source s = source();
		return new Declaration_SourceSink(firstToken, type, name, s);
	}

    /*
    * Source​ ​ ::=​ ​ STRING_LITERAL​ ​ ​ | ​ ​ OP_AT​ ​ Expression​ ​ | ​ ​ IDENTIFIER
    * */

    Source source() throws SyntaxException{
        Token temp = null;
        Token firstToken = t;
    	if (t.kind == STRING_LITERAL){
            temp = t;
    		consume();
    		return new Source_StringLiteral(firstToken, temp.toString());
        }
        else if (t.kind == OP_AT){
            consume();
            return new Source_CommandLineParam(firstToken, expression());
        }
        else if (t.kind == IDENTIFIER){
            temp = t;
        	consume();
        	return new Source_Ident(firstToken, temp);
        }
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
    }

    /*
    * SourceSinkType​ ​ :=​ ​ KW_url​ ​ | ​ ​ KW_file
    * */

    Token sourceSinkType() throws SyntaxException{
        Token name = null;
    	if (t.kind == KW_url){
            name = t;
        	consume();
        }
        else if (t.kind == KW_file){
            name = t;
    		consume();
        }
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
		return name;
    }

    /*
    * ImageDeclaration​ ​ ::=​ ​ ​ KW_image​ ​ ​ (LSQUARE​ ​ Expression​ ​ COMMA​ ​ Expression​ ​ RSQUARE​ ​ | ​ ​ ε )
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ IDENTIFIER​ ​ ( ​ ​ OP_LARROW​ ​ Source​ ​ | ​ ​ ε ​ ​ )
    * */

    Declaration_Image imageDeclaration() throws SyntaxException{
		Token firstToken = match(KW_image);
    	Expression xSize = null;
		Expression ySize = null;
		Token name = null;
    	Source s = null;
		if (t.kind == LSQUARE){
			consume();
			xSize = expression();
			match(COMMA);
			ySize = expression();
			match(RSQUARE);
		}
		name = match(IDENTIFIER);
		if (t.kind == OP_LARROW){
			consume();
			s = source();
		}
		return new Declaration_Image(firstToken, xSize, ySize, name, s);
	}

    /*
	* Statement​ ​ ​ ::=​ ​ IDENTIFIER​ ​ ( ​ ​ ( ​ ​ LSQUARE​ ​ LhsSelector​ ​ RSQUARE​ ​ ​ ​ | ​ ​ ε ​ ​ ) ​ ​ ​ OP_ASSIGN​ ​ Expression​
	* ​ | ​ ​ OP_RARROW Sink​ ​ | ​ ​ OP_LARROW​ ​ Source​ ​ )
	*
	* I don't have LHS function, need to check if causes errors
	* */

	Statement statement() throws SyntaxException{
		Token name = match(IDENTIFIER);
		Token firstToken = name;
		Expression e = null;
		LHS lhs = null;
		if (t.kind == OP_RARROW){
			consume();
			return new Statement_Out(firstToken, name, sink());
		}
		else if (t.kind == OP_LARROW){
			consume();
			return new Statement_In(firstToken, name, source());
		}
		else if (t.kind == LSQUARE){
			consume();
			lhs = lhsSelector(name);
			match(RSQUARE);
			match(OP_ASSIGN);
			e = expression();
		}
		else if (t.kind == OP_ASSIGN){
			consume();
			lhs = new LHS(firstToken, name, null);
			e = expression();
		}
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
		return new Statement_Assign(firstToken, lhs, e);
	}

	/*
	* Sink​ ​ ::=​ ​ IDENTIFIER​ ​ | ​ ​ KW_SCREEN​ ​ ​ //ident​ ​ must​ ​ be​ ​ file
	* Should I check for a file?
	* */

	Sink sink() throws SyntaxException{
		Token firstToken = t;
		if (t.kind == IDENTIFIER){
			Token name = t;
			consume();
			return new Sink_Ident(firstToken, name);
		}
		else if (t.kind == KW_SCREEN){
			consume();
			return new Sink_SCREEN(firstToken);
		}
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
	}

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression  | OrExpression
	 *
	 * Expression​ ​ ::=​ ​ ​ OrExpression​ ​ ( ​ ​ OP_Q​ ​ ​ Expression​ ​ OP_COLON​ ​ Expression​ ​ | ​ ​ ε ​ ​ )
	 *
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
	Expression expression() throws SyntaxException {	//Check if this is working correctly
		Token firstToken = t;
		Expression condition = orExpression();
		Expression trueExpression = null;
		Expression falseExpression = null;
		if(t.kind == OP_Q){
			consume();
			trueExpression = expression();
			match(OP_COLON);
			falseExpression = expression();
		}
		return new Expression_Conditional(firstToken, condition, trueExpression, falseExpression);
	}

	/*
	* OrExpression​ ​ ::=​ ​ AndExpression​ ​ ​ ​ ( ​ ​ ​ OP_OR​ ​ ​ AndExpression)*
	* */
	// Need to check first token for while statements

	Expression orExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = andExpression();
		while (t.kind == OP_OR){
			Token op = t;
			consume();
			Expression e1 = andExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* AndExpression​ ​ ::=​ ​ EqExpression​ ​ ( ​ ​ OP_AND​ ​ ​ EqExpression​ ​ )*
	* */

	Expression andExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = eqExpression();
		while (t.kind == OP_AND){
			Token op = t;
			consume();
			Expression e1 = eqExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* EqExpression​ ​ ::=​ ​ RelExpression​ ​ ​ ( ​ ​ ​ (OP_EQ​ ​ | ​ ​ OP_NEQ​ ​ ) ​ ​ ​ RelExpression​ ​ )*
	* */

	Expression eqExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = relExpression();
		while (t.kind == OP_EQ || t.kind == OP_NEQ){
			Token op = t;
			consume();
			Expression e1 = relExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* RelExpression​ ​ ::=​ ​ AddExpression​ ​ ( ​ ​ ​ ( ​ ​ OP_LT​ ​ ​ | ​ ​ OP_GT​ ​ | ​ ​ ​ OP_LE​ ​ ​ | ​ ​ OP_GE​ ​ ) ​ ​ ​ ​ AddExpression)*
	* */

	Expression relExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = addExpression();
		while (t.kind == OP_LT || t.kind == OP_GT || t.kind == OP_LE || t.kind == OP_GE){
			Token op = t;
			consume();
			Expression e1 = addExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* AddExpression​ ​ ::=​ ​ MultExpression​ ​ ​ ​ ( ​ ​ ​ (OP_PLUS​ ​ | ​ ​ OP_MINUS​ ​ ) ​ ​ MultExpression​ ​ )*
	* */

	Expression addExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = multExpression();
		while (t.kind == OP_PLUS || t.kind == OP_MINUS){
			Token op = t;
			consume();
			Expression e1 = multExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* MultExpression​ ​ :=​ ​ UnaryExpression​ ​ ( ​ ​ ( ​ ​ OP_TIMES​ ​ | ​ ​ OP_DIV​ ​ ​ | ​ ​ OP_MOD​ ​ ) ​ ​ UnaryExpression​ ​ )*
	* */

	Expression multExpression() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = unaryExpression();
		while (t.kind == OP_TIMES || t.kind == OP_DIV || t.kind == OP_MOD){
			Token op = t;
			consume();
			Expression e1 = unaryExpression();
			e0 = new Expression_Binary(firstToken, e0, op, e1);
		}
		return e0;
	}

	/*
	* UnaryExpression​ ​ ::=​ ​ OP_PLUS​ ​ UnaryExpression
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ | ​ ​ OP_MINUS​ ​ UnaryExpression
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ | ​ ​ UnaryExpressionNotPlusMinus
	* */

	Expression unaryExpression() throws SyntaxException{	//Confusion here what to return
		Token firstToken = t;
		if (t.kind == OP_PLUS){
			Token op = t;
			consume();
			Expression e = unaryExpression();
			return new Expression_Unary(firstToken, op, e);
		}
		else if (t.kind == OP_MINUS){
			Token op = t;
			consume();
			Expression e = unaryExpression();
			return new Expression_Unary(firstToken, op, e);
		}
		else {
			return unaryExpressionNotPlusMinus();
		}
	}

	/*
	* UnaryExpressionNotPlusMinus​ ​ ::=​ ​ ​ OP_EXCL​ ​ ​ UnaryExpression​ ​ ​ | ​ ​ Primary
		|​ ​ IdentOrPixelSelectorExpression​ ​ | ​ ​ KW_x​ ​ | ​ ​ KW_y​ ​ | ​ ​ KW_r​ ​ | ​ ​ KW_a​ ​ | ​ ​ KW_X​ ​ | ​ ​ KW_Y​ ​
		| ​ ​ KW_Z​ ​ | ​ ​ KW_A​ ​ | KW_R​ ​ | ​ ​ KW_DEF_X​ ​ | ​ ​ KW_DEF_Y
	* */

	Expression unaryExpressionNotPlusMinus() throws SyntaxException{
		Token firstToken = t;
		switch (t.kind){
			case OP_EXCL:
				consume();
				return unaryExpression();
				//break;
			case INTEGER_LITERAL:
			case BOOLEAN_LITERAL:
			case LPAREN:
			case KW_sin: case KW_cos: case KW_atan: case KW_abs: case KW_cart_x:
			case KW_cart_y: case KW_polar_a: case KW_polar_r:
				return primary();
				//break;
			case IDENTIFIER:
				return identOrPixelSelectorExpression();
				//break;
			case KW_x: case KW_y: case KW_r: case KW_a: case KW_X: case KW_Y:
			case KW_Z: case KW_A: case KW_R: case KW_DEF_X: case KW_DEF_Y:
				Kind kind = t.kind;
				consume();
				return new Expression_PredefinedName(firstToken, kind);
				//break;
			default:
				// Nothing matched
				throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
	}

	/*
	* Primary​ ​ ::=​ ​ INTEGER_LITERAL​ ​ | ​ ​ LPAREN​ ​ Expression​ ​ RPAREN​ ​ | ​ ​ FunctionApplication | BOOLEAN_LITERAL
	* */

	Expression primary() throws SyntaxException{
		Token firstToken = t;
		if (t.kind == INTEGER_LITERAL){
	        Token int_lit = t;
	    	consume();
	    	return new Expression_IntLit(firstToken, int_lit.intVal());
        }
        else if (t.kind == LPAREN){
	        consume();
	        Expression e = expression();
	        match(RPAREN);
	        return e;
        }
		else if (t.kind == BOOLEAN_LITERAL){
	        boolean bool_lit;
			if (t.length == 4) {
				bool_lit = true;
			}
			else {
				bool_lit = false;
			}
        	consume();
        	return new Expression_BooleanLit(firstToken, bool_lit);//Need to find the value bool_lit
        }
        else {
            return functionApplication();
        }
    }

	/*
	* IdentOrPixelSelectorExpression::=​ ​ ​ IDENTIFIER​ ​ ( ​ ​ LSQUARE​ ​ Selector​ ​ RSQUARE​ ​ ​ ​ | ​ ​ ε ​ ​ )
	* */

	Expression identOrPixelSelectorExpression() throws SyntaxException{
        Token temp = match(IDENTIFIER);
		Token firstToken = temp;
        if (t.kind == LSQUARE){
            consume();
            Index index = selector();
            match(RSQUARE);
            return new Expression_PixelSelector(firstToken, temp, index);
        }
        return new Expression_Ident(firstToken, temp);
    }

	/*
	* FunctionApplication​ ​ ::=​ ​ FunctionName​ ​ ( ​ ​ LPAREN​ ​ Expression​ ​ RPAREN​ ​ ​ | ​ ​ LSQUARE​ ​ Selector​ ​ RSQUARE​ ​ )
	* */

	Expression_FunctionApp functionApplication() throws SyntaxException{
		Token firstToken = t;
		Kind function = functionName();
		if (t.kind == LPAREN){
			consume();
			Expression arg = expression();
			match(RPAREN);
			return new Expression_FunctionAppWithExprArg(firstToken, function, arg);
		}
		else if(t.kind == LSQUARE){
			consume();
			Index arg = selector();
			match(RSQUARE);
			return new Expression_FunctionAppWithIndexArg(firstToken, function, arg);
		}
		else {
			throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
	}

	/*
	* FunctionName​ ​ ::=​ ​ KW_sin​ ​ | ​ ​ KW_cos​ ​ | ​ ​ KW_atan​ ​ | ​ ​ KW_abs
		|​ ​ KW_cart_x​ ​ | ​ ​ KW_cart_y​ ​ | ​ ​ KW_polar_a​ ​ | ​ ​ LW_polar_r
	* */

	Kind functionName() throws SyntaxException{
		switch (t.kind){
			case KW_sin: case KW_cos: case KW_atan: case KW_abs: case KW_cart_x:
			case KW_cart_y: case KW_polar_a: case KW_polar_r:
				Kind function = t.kind;
				consume();
				return function;
				//break;
			default:
				// Nothing matched
				throw new SyntaxException(t, "Invalid token: " + t.kind + " at line: " + t.line + ", pos: " + t.pos_in_line);
		}
	}

	/*
	* LhsSelector​ ​ ::=​ ​ LSQUARE​ ​ ​ ( ​ ​ XySelector​ ​ ​ | ​ ​ RaSelector​ ​ ​ ) ​ ​ ​ ​ RSQUARE
	* Maybe add boolean returns depending on success or failures for function calls
	* */

	LHS lhsSelector(Token name) throws SyntaxException{
		Token firstToken = name;
		match(LSQUARE);
		Index index = null;
		if (t.kind == KW_x){
			index = xySelector();
		}
		else if (t.kind == KW_r){
			index = raSelector();
		}
		match(RSQUARE);
		return new LHS(firstToken, name, index);
	}

	/*
	* XySelector​ ​ ::=​ ​ KW_x​ ​ COMMA​ ​ KW_y
	* Not sure how to return expressions here
	* */

	Index xySelector() throws SyntaxException{
		Token t0 = match(KW_x);
		Token firstToken = t0;
		Expression e0 = new Expression_PredefinedName(firstToken, t0.kind);
		match(COMMA);
		Token t1 = match(KW_y);
		Expression e1 = new Expression_PredefinedName(firstToken, t1.kind);
		return new Index(firstToken, e0, e1);
	}

	/*
	* RaSelector​ ​ ::=​ ​ KW_r​ ​ , ​ ​ KW_A
	* Not sure how to return expressions here
	* */

	Index raSelector() throws SyntaxException{
		Token t0 = match(KW_r);
		Token firstToken = t0;
		Expression e0 = new Expression_PredefinedName(firstToken, t0.kind);
		match(COMMA);
		Token t1 = match(KW_A);
		Expression e1 = new Expression_PredefinedName(firstToken, t1.kind);
		return new Index(firstToken, e0, e1);
	}

	/*
	* Selector​ ​ ::=​ ​ ​ Expression​ ​ COMMA​ ​ Expression
	* */

	Index selector() throws SyntaxException{
		Token firstToken = t;
		Expression e0 = expression();
		match(COMMA);
		Expression e1 = expression();
		return new Index(firstToken, e0, e1);
	}


	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 *
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind == EOF) {
			return t;
		}
		String message =  "Expected EOL at " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}
}
