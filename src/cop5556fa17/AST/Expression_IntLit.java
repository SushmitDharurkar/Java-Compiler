package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Expression_IntLit extends Expression {

	public final int value;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_IntLit(this, arg);
	}

	public Expression_IntLit(Token firstToken, int value) {
		super(firstToken);
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + value;
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
		Expression_IntLit other = (Expression_IntLit) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_IntLit [value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
	
	

}
