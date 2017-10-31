package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils;

public abstract class Declaration extends ASTNode {

	TypeUtils.Type typeName;

	public Declaration(Token firstToken) {
		super(firstToken);
	}

	public abstract TypeUtils.Type getType();

	public abstract void setType(TypeUtils.Type typeName);

}
