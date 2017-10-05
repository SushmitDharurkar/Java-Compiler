package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public abstract class Statement extends ASTNode {

	public Statement(Token firstToken) {
		super(firstToken);
	}

}
