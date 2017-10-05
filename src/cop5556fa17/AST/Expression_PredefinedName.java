package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

public class Expression_PredefinedName extends Expression {

	public final Kind kind;

	public Expression_PredefinedName(Token firstToken, Kind kind) {
		super(firstToken);
		this.kind = kind;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_PredefinedName(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
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
		Expression_PredefinedName other = (Expression_PredefinedName) obj;
		if (kind != other.kind)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_PredefinedName [name=");
		builder.append(kind);
		builder.append("]");
		return builder.toString();
	}

}
