package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Declaration_Image extends Declaration {
	
	public final Expression xSize;
	public final Expression ySize;
	public final String name;
	public final Source source;

	public Declaration_Image(Token firstToken, Expression xSize, Expression ySize, Token name,
			Source source) {
		super(firstToken);
		this.xSize = xSize;
		this.ySize = ySize;
		this.name = name.getText();
		this.source = source;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDeclaration_Image(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((xSize == null) ? 0 : xSize.hashCode());
		result = prime * result + ((ySize == null) ? 0 : ySize.hashCode());
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
		Declaration_Image other = (Declaration_Image) obj;
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
		if (xSize == null) {
			if (other.xSize != null)
				return false;
		} else if (!xSize.equals(other.xSize))
			return false;
		if (ySize == null) {
			if (other.ySize != null)
				return false;
		} else if (!ySize.equals(other.ySize))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Declaration_Image [xSize=");
		builder.append(xSize);
		builder.append(", ySize=");
		builder.append(ySize);
		builder.append(", name=");
		builder.append(name);
		builder.append(", source=");
		builder.append(source);
		builder.append("]");
		return builder.toString();
	}



	
}
