package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Statement_Assign extends Statement {
	
	public final LHS lhs;
	public final Expression e;
	
	boolean isCartesian;
	

	public Statement_Assign(Token firstToken, LHS lhs, Expression e) {
		super(firstToken);
		this.lhs = lhs;
		this.e = e;
	}
	
	

	public boolean isCartesian() {
		return isCartesian;
	}



	public void setCartesian(boolean isCartesian) {
		this.isCartesian = isCartesian;
	}



	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatement_Assign(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
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
		Statement_Assign other = (Statement_Assign) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Statement_Assign [lhs=");
		builder.append(lhs);
		builder.append(", e=");
		builder.append(e);
		builder.append("]");
		return builder.toString();
	}
	
	

}
