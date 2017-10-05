package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Source_StringLiteral extends Source {
	
	public final String fileOrUrl;

	public Source_StringLiteral(Token firstToken, String fileOrUrl) {
		super(firstToken);
		this.fileOrUrl = fileOrUrl;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSource_StringLiteral(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fileOrUrl == null) ? 0 : fileOrUrl.hashCode());
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
		Source_StringLiteral other = (Source_StringLiteral) obj;
		if (fileOrUrl == null) {
			if (other.fileOrUrl != null)
				return false;
		} else if (!fileOrUrl.equals(other.fileOrUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Source_StringLiteral [fileOrUrl=");
		builder.append(fileOrUrl);
		builder.append("]");
		return builder.toString();
	}


	

}
