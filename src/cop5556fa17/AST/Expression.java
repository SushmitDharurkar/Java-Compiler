package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public abstract class Expression extends ASTNode {
	
	public Expression(Token firstToken) {
		super(firstToken);
	}

}
