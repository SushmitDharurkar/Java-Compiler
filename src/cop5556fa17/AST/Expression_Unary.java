package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

public class Expression_Unary extends Expression {
	
	public final Kind op;
	public final Expression e;

	public Expression_Unary(Token firstToken, Token op, Expression e) {
		super(firstToken);
		this.e = e;
		this.op = op.kind;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_Unary(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
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
		Expression_Unary other = (Expression_Unary) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (op == null) {
			if (other.op != null)
				return false;
		} else if (!op.equals(other.op))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_Unary [op=");
		builder.append(op);
		builder.append(", e=");
		builder.append(e);
		builder.append("]");
		return builder.toString();
	}
	
	

}
