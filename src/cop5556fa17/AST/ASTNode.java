package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils;

public abstract class ASTNode {
	
	final public Token firstToken;

	TypeUtils.Type type;
	
	public ASTNode(Token firstToken) {
		super();	//Don't know why super() called in base class?
		this.firstToken = firstToken;
//		type = TypeUtils.Type.NONE;	//Note Not sure of this
	}

	public abstract TypeUtils.Type getType();

	public abstract void setType(TypeUtils.Type type);

	public abstract Object visit(ASTVisitor v, Object arg) throws Exception;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstToken == null) ? 0 : firstToken.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ASTNode other = (ASTNode) obj;
		if (firstToken == null) {
			if (other.firstToken != null)
				return false;
		} else if (!firstToken.equals(other.firstToken))
			return false;
		return true;
	}
	
	
}


