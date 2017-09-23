package cop5556fa17;



import java.util.Arrays;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.SimpleParser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class SimpleParser {

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

	SimpleParser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public void parse() throws SyntaxException {
		program();
		matchEOF();
	}

	void consume(){
		t = scanner.nextToken();
	}
	

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	void program() throws SyntaxException {
		if(t.kind == IDENTIFIER){
			consume();
			while (t.kind == KW_int || t.kind == KW_boolean || t.kind == KW_image
					|| t.kind == KW_url || t.kind == KW_file || t.kind == IDENTIFIER){

				if (t.kind == KW_int || t.kind == KW_boolean || t.kind == KW_image
						|| t.kind == KW_url || t.kind == KW_file){
					declaration();
				}
				else if (t.kind == IDENTIFIER){
					statement();
				}
				if(t.kind == SEMI){
					consume();
				}
			}
		}
		else {
		    throw new SyntaxException(t, "No Input provided.");
        }
	}

	/*
	* Declaration​ ​ ::​ ​ = ​ ​ ​ VariableDeclaration​ ​ ​ ​ ​ ​ | ​ ​ ​ ​ ​ ImageDeclaration​ ​ ​ ​ | ​ ​ ​ ​ SourceSinkDeclaration
	* */

	void declaration() throws SyntaxException{
		if (t.kind == KW_int || t.kind == KW_boolean){
			variableDeclaration();
		}
		else if (t.kind == KW_image){
			imageDeclaration();
		}
		else if (t.kind == KW_url || t.kind == KW_file){
			sourceSinkDeclaration();
		}
	}

	/*
	* VariableDeclaration​ ​ ​ ::=​ ​ ​ VarType​ ​ IDENTIFIER​ ​ ​ ( ​ ​ ​ = ​ ​ ​ Expression​ ​ ​ | ​ ​ ε ​ ​ )
	* */

	void variableDeclaration() throws SyntaxException{
		varType();
		if (t.kind == IDENTIFIER){
			consume();
		}
		if (t.kind == OP_ASSIGN){
			expression();
		}
		else {
			return;
		}
	}

	/*
	* VarType​ ​ ::=​ ​ KW_int​ ​ | ​ ​ KW_boolean
	* */

	void varType() throws SyntaxException{
	    if (t.kind == KW_int){
	        consume();
        }
        else if (t.kind == KW_boolean){
	        consume();
        }
    }

    /*
    * SourceSinkDeclaration​ ​ ::=​ ​ SourceSinkType​ ​ IDENTIFIER​ ​ ​ OP_ASSIGN​ ​ ​ Source
    * */

	void sourceSinkDeclaration() throws SyntaxException{
		sourceSinkType();
		if (t.kind == IDENTIFIER){
			consume();
		}
		if (t.kind == OP_ASSIGN){
			consume();
		}
		source();
	}

    /*
    * Source​ ​ ::=​ ​ STRING_LITERAL​ ​ ​ | ​ ​ OP_AT​ ​ Expression​ ​ | ​ ​ IDENTIFIER
    * */

    void source() throws SyntaxException{
        if (t.kind == STRING_LITERAL){
            consume();
        }
        else if (t.kind == OP_AT){
            consume();
            expression();
        }
        else if (t.kind == IDENTIFIER){
            consume();
        }
    }

    /*
    * SourceSinkType​ ​ :=​ ​ KW_url​ ​ | ​ ​ KW_file
    * */

    void sourceSinkType() throws SyntaxException{
        if (t.kind == KW_url){
            consume();
        }
        else if (t.kind == KW_file){
            consume();
        }
    }

    /*
    * ImageDeclaration​ ​ ::=​ ​ ​ KW_image​ ​ ​ (LSQUARE​ ​ Expression​ ​ COMMA​ ​ Expression​ ​ RSQUARE​ ​ | ​ ​ ε )
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ IDENTIFIER​ ​ ( ​ ​ OP_LARROW​ ​ Source​ ​ | ​ ​ ε ​ ​ )
    * */

    void imageDeclaration() throws SyntaxException{
    	if (t.kind == KW_image){
    		consume();
    		if (t.kind == LSQUARE){
				consume();
				expression();
				if (t.kind == COMMA){
					consume();
				}
				expression();
				if (t.kind == RSQUARE){
					consume();
				}
			}
			if (t.kind == IDENTIFIER){
    			consume();
    			if (t.kind == OP_LARROW){
    				consume();
    				source();
				}
				else {
    				return;
				}
			}
		}
	}

    /*
	* Statement​ ​ ​ ::=​ ​ IDENTIFIER​ ​ ( ​ ​ ( ​ ​ LSQUARE​ ​ LhsSelector​ ​ RSQUARE​ ​ ​ ​ | ​ ​ ε ​ ​ ) ​ ​ ​ OP_ASSIGN​ ​ Expression​
	* ​ | ​ ​ OP_RARROW Sink​ ​ | ​ ​ OP_LARROW​ ​ Source​ ​ )
	* */

	void statement() throws SyntaxException{
		if (t.kind == IDENTIFIER){
			consume();
			if (t.kind == OP_RARROW){
				consume();
				sink();
			}
			else if (t.kind == OP_LARROW){
				consume();
				source();
			}
			else if (t.kind == LSQUARE){
				consume();
				lhsSelector();
				if (t.kind == RSQUARE){
					consume();
					if (t.kind == OP_ASSIGN){
						consume();
					}
					expression();
				}
			}
			else if (t.kind == OP_ASSIGN){
				consume();
			}
			expression();
		}
	}

	/*
	* Sink​ ​ ::=​ ​ IDENTIFIER​ ​ | ​ ​ KW_SCREEN​ ​ ​ //ident​ ​ must​ ​ be​ ​ file
	* Should I check for a file?
	* */

	void sink() throws SyntaxException{
		if (t.kind == IDENTIFIER){
			consume();
		}
		else if (t.kind == KW_SCREEN){
			consume();
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
	void expression() throws SyntaxException {
		orExpression();
		if(t.kind == OP_Q){
			consume();
			expression();
			if (t.kind == OP_COLON){
				consume();
			}
			expression();
		}
		else {
			return;
		}
	}

	/*
	* OrExpression​ ​ ::=​ ​ AndExpression​ ​ ​ ​ ( ​ ​ ​ OP_OR​ ​ ​ AndExpression)*
	* */

	void orExpression() throws SyntaxException{
		andExpression();
		while (t.kind == OP_OR){
			consume();
			andExpression();
		}
	}

	/*
	* AndExpression​ ​ ::=​ ​ EqExpression​ ​ ( ​ ​ OP_AND​ ​ ​ EqExpression​ ​ )*
	* */

	void andExpression() throws SyntaxException{
		eqExpression();
		while (t.kind == OP_AND){
			consume();
			eqExpression();
		}
	}

	/*
	* EqExpression​ ​ ::=​ ​ RelExpression​ ​ ​ ( ​ ​ ​ (OP_EQ​ ​ | ​ ​ OP_NEQ​ ​ ) ​ ​ ​ RelExpression​ ​ )*
	* */

	void eqExpression() throws SyntaxException{
		relExpression();
		while (t.kind == OP_EQ || t.kind == OP_NEQ){
			consume();
			relExpression();
		}
	}

	/*
	* RelExpression​ ​ ::=​ ​ AddExpression​ ​ ( ​ ​ ​ ( ​ ​ OP_LT​ ​ ​ | ​ ​ OP_GT​ ​ | ​ ​ ​ OP_LE​ ​ ​ | ​ ​ OP_GE​ ​ ) ​ ​ ​ ​ AddExpression)*
	* */

	void relExpression() throws SyntaxException{
		addExpression();
		while (t.kind == OP_LT || t.kind == OP_GT || t.kind == OP_LE || t.kind == OP_GE){
			consume();
			addExpression();
		}
	}

	/*
	* AddExpression​ ​ ::=​ ​ MultExpression​ ​ ​ ​ ( ​ ​ ​ (OP_PLUS​ ​ | ​ ​ OP_MINUS​ ​ ) ​ ​ MultExpression​ ​ )*
	* */

	void addExpression() throws SyntaxException{
		multExpression();
		while (t.kind == OP_PLUS || t.kind == OP_MINUS){
			consume();
			multExpression();
		}
	}

	/*
	* MultExpression​ ​ :=​ ​ UnaryExpression​ ​ ( ​ ​ ( ​ ​ OP_TIMES​ ​ | ​ ​ OP_DIV​ ​ ​ | ​ ​ OP_MOD​ ​ ) ​ ​ UnaryExpression​ ​ )*
	* */

	void multExpression() throws SyntaxException{
		unaryExpression();
		while (t.kind == OP_TIMES || t.kind == OP_DIV || t.kind == OP_MOD){
			consume();
			unaryExpression();
		}
	}

	/*
	* UnaryExpression​ ​ ::=​ ​ OP_PLUS​ ​ UnaryExpression
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ | ​ ​ OP_MINUS​ ​ UnaryExpression
​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ ​ | ​ ​ UnaryExpressionNotPlusMinus
	* */

	void unaryExpression() throws SyntaxException{
		if (t.kind == OP_PLUS){
			consume();
			unaryExpression();
		}
		else if (t.kind == OP_MINUS){
			consume();
			unaryExpression();
		}
		//Maybe I should check here
		else {
			unaryExpressionNotPlusMinus();
		}
	}

	/*
	* UnaryExpressionNotPlusMinus​ ​ ::=​ ​ ​ OP_EXCL​ ​ ​ UnaryExpression​ ​ ​ | ​ ​ Primary
		|​ ​ IdentOrPixelSelectorExpression​ ​ | ​ ​ KW_x​ ​ | ​ ​ KW_y​ ​ | ​ ​ KW_r​ ​ | ​ ​ KW_a​ ​ | ​ ​ KW_X​ ​ | ​ ​ KW_Y​ ​
		| ​ ​ KW_Z​ ​ | ​ ​ KW_A​ ​ | KW_R​ ​ | ​ ​ KW_DEF_X​ ​ | ​ ​ KW_DEF_Y
	* */

	void unaryExpressionNotPlusMinus() throws SyntaxException{
		switch (t.kind){
			case OP_EXCL:
				consume();
				unaryExpression();
				break;
			case INTEGER_LITERAL:
			case LPAREN:
			case KW_sin: case KW_cos: case KW_atan: case KW_abs: case KW_cart_x:
			case KW_cart_y: case KW_polar_a: case KW_polar_r:
				primary();
				break;
			case IDENTIFIER:
				identOrPixelSelectorExpression();
				break;
			case KW_x: case KW_y: case KW_r: case KW_a: case KW_X: case KW_Y:
			case KW_Z: case KW_A: case KW_R: case KW_DEF_X: case KW_DEF_Y:
				consume();
				break;
			default:
				// Nothing matched
				// Not sure about errors and when to throw exceptions
				throw new SyntaxException(t, "Error!");
		}
	}

	/*
	* Primary​ ​ ::=​ ​ INTEGER_LITERAL​ ​ | ​ ​ LPAREN​ ​ Expression​ ​ RPAREN​ ​ | ​ ​ FunctionApplication
	* */

	void primary() throws SyntaxException{
	    if (t.kind == INTEGER_LITERAL){
	        consume();
        }
        else if (t.kind == LPAREN){
	        consume();
	        expression();
	        if (t.kind == RPAREN){
	            consume();
            }
        }
        //Maybe I should check here
        else {
            functionApplication();
        }
    }

	/*
	* IdentOrPixelSelectorExpression::=​ ​ ​ IDENTIFIER​ ​ ( ​ ​ LSQUARE​ ​ Selector​ ​ RSQUARE​ ​ ​ ​ | ​ ​ ε ​ ​ )
	* */

	void identOrPixelSelectorExpression() throws SyntaxException{
        if (t.kind == IDENTIFIER){
            consume();
        }
        if (t.kind == LSQUARE){
            consume();
            selector();
            if (t.kind == RSQUARE){
                consume();
            }
        }
        else {
            return;
        }
    }

	/*
	* Lhs::=​ ​ ​ IDENTIFIER​ ​ ( ​ ​ LSQUARE​ ​ LhsSelector​ ​ RSQUARE​ ​ ​ ​ | ​ ​ ε ​ ​ )
	* */

	void lhs() throws SyntaxException{
	    if (t.kind == IDENTIFIER){
	        consume();
        }
        if (t.kind == LSQUARE){
	        consume();
	        if (t.kind == LSQUARE){     //Do I need to check this?
	            lhsSelector();
            }
            if (t.kind == RSQUARE){
	            consume();
            }
        }
        else{
            return;
        }
    }

	/*
	* FunctionApplication​ ​ ::=​ ​ FunctionName​ ​ ( ​ ​ LPAREN​ ​ Expression​ ​ RPAREN​ ​ ​ | ​ ​ LSQUARE​ ​ Selector​ ​ RSQUARE​ ​ )
	* */

	void functionApplication() throws SyntaxException{
		functionName();
		if (t.kind == LPAREN){
			consume();
			expression();
			if (t.kind == RPAREN){
				consume();
			}
		}
		else if(t.kind == LSQUARE){
			consume();
			selector();
			if (t.kind == RSQUARE){
				consume();
			}
		}
	}

	/*
	* FunctionName​ ​ ::=​ ​ KW_sin​ ​ | ​ ​ KW_cos​ ​ | ​ ​ KW_atan​ ​ | ​ ​ KW_abs
		|​ ​ KW_cart_x​ ​ | ​ ​ KW_cart_y​ ​ | ​ ​ KW_polar_a​ ​ | ​ ​ LW_polar_r
	* */

	void functionName() throws SyntaxException{
		switch (t.kind){
			case KW_sin: case KW_cos: case KW_atan: case KW_abs: case KW_cart_x:
			case KW_cart_y: case KW_polar_a: case KW_polar_r:
				consume();
				break;
			// Nothing matched
			// Not sure about errors and when to throw exceptions
			default:
				throw new SyntaxException(t, "Error!");
		}
	}

	/*
	* LhsSelector​ ​ ::=​ ​ LSQUARE​ ​ ​ ( ​ ​ XySelector​ ​ ​ | ​ ​ RaSelector​ ​ ​ ) ​ ​ ​ ​ RSQUARE
	* Maybe add boolean returns depending on success or failures for function calls
	* */

	void lhsSelector() throws SyntaxException{
		if (t.kind == LSQUARE){
			consume();
		}
		if (t.kind == KW_x){
			xySelector();
		}
		else if (t.kind == KW_r){
			raSelector();
		}
		if (t.kind == RSQUARE){
			consume();
		}
	}

	/*
	* XySelector​ ​ ::=​ ​ KW_x​ ​ COMMA​ ​ KW_y
	* Need to confirm flow of ifs
	* */

	void xySelector() throws SyntaxException{
		if (t.kind == KW_x){
			consume();
		}
		if (t.kind == COMMA) {
			consume();
		}
		if (t.kind == KW_y){
			consume();
		}
	}

	/*
	* RaSelector​ ​ ::=​ ​ KW_r​ ​ , ​ ​ KW_A
	* Need to confirm flow of ifs
	* */

	void raSelector() throws SyntaxException{
		if (t.kind == KW_r){
			consume();
			if (t.kind == COMMA){
				consume();
				if (t.kind == KW_A){
					consume();
				}
			}
		}
	}

	/*
	* Selector​ ​ ::=​ ​ ​ Expression​ ​ COMMA​ ​ Expression
	* */

	void selector() throws SyntaxException{
		expression();
		if(t.kind == COMMA){
			consume();
		}
		expression();
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
