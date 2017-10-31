package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils;

public abstract class Source extends ASTNode{

	TypeUtils.Type type;

	public Source(Token firstToken) {
		super(firstToken);
	}

	public TypeUtils.Type getType() {
		return type;
	}

	public void setType(TypeUtils.Type type) {
		this.type = type;
	}
	
}
