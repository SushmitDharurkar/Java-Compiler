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
			/*while (declaration() || statement()){
				if(t.kind == SEMI){
					consume();
				}
			}*/
		}
		//TODO  implement this
		throw new UnsupportedOperationException();
	}

	/*void declaration(){

	}

	void statement(){

	}*/

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression  | OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
	void expression() throws SyntaxException {
		orExpression();
		if(t.kind == OP_Q){
			consume();
		}
		expression();
		if (t.kind == OP_COLON){
			consume();
		}
		expression();

		//TODO  implement this
		throw new UnsupportedOperationException();
	}

	void orExpression() throws SyntaxException{
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
