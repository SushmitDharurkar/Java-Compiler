package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public abstract class Declaration extends ASTNode {
	
	//NOTE Should I declare Type type in superclasses as well?

	public Declaration(Token firstToken) {
		super(firstToken);
	}



}
