package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Index extends ASTNode {
	
	public static final Index defaultIndex = null; //TODO fix this

	public final Expression e0;
	public final Expression e1;
	
	boolean isCartesian;
	
	public Index(Token firstToken, Expression e0, Expression e1) {
		super(firstToken);
		this.e0 = e0;
		this.e1 = e1;
	}
	
	

	public boolean isCartesian() {
		return isCartesian;
	}



	public void setCartesian(boolean isCartesian) {
		this.isCartesian = isCartesian;
	}



	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIndex(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e0 == null) ? 0 : e0.hashCode());
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
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
		Index other = (Index) obj;
		if (e0 == null) {
			if (other.e0 != null)
				return false;
		} else if (!e0.equals(other.e0))
			return false;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Index [e0=");
		builder.append(e0);
		builder.append(", e1=");
		builder.append(e1);
		builder.append("]");
		return builder.toString();
	}
	
	

}
