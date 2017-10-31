package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils;

public abstract class Sink extends ASTNode {

	TypeUtils.Type type;

	public Sink(Token firstToken) {
		super(firstToken);
	}


	public TypeUtils.Type getType() {
		return type;
	}


	public void setType(TypeUtils.Type type) {
		this.type = type;
	}

}
