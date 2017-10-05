package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Expression_PixelSelector extends Expression {
	
	public final String name;
	public final Index index;
	
	boolean isCartesian;

	public boolean isCartesian() {
		return isCartesian;
	}

	public void setCartesian(boolean isCartesian) {
		this.isCartesian = isCartesian;
	}

	public Expression_PixelSelector(Token firstToken, Token name, Index index) {
		super(firstToken);
		this.name = name.getText();
		this.index = index;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_PixelSelector(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Expression_PixelSelector other = (Expression_PixelSelector) obj;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_PixelSelector [name=");
		builder.append(name);
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}

	
}
