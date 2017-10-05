package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Expression_BooleanLit extends Expression {
	
	public final boolean value;
	
	public Expression_BooleanLit(Token firstToken, boolean value) {
		super(firstToken);
		this.value = value;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_BooleanLit(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (value ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expression_BooleanLit other = (Expression_BooleanLit) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expression_BooleanLit [value=" + value + "]";
	}

	

}
