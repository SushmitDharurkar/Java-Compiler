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
		orExpression();

		//TODO  implement this
		throw new UnsupportedOperationException();
	}

	void orExpression() {
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
