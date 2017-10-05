package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Statement_In extends Statement {
	
	public final String name;
	public final Source source;
	
	Declaration dec;
	

	public Declaration getDec() {
		return dec;
	}

	public void setDec(Declaration dec) {
		this.dec = dec;
	}

	public Statement_In(Token firstToken, Token name, Source source) {
		super(firstToken);
		this.name = name.getText();
		this.source = source;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatement_In(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Statement_In other = (Statement_In) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Statement_In [name=");
		builder.append(name);
		builder.append(", source=");
		builder.append(source);
		builder.append("]");
		return builder.toString();
	}
	
	

}
