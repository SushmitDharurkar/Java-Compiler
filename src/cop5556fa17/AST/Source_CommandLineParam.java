package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Source_CommandLineParam extends Source {
	
	public final Expression paramNum;

	public Source_CommandLineParam(Token firstToken, Expression paramNum) {
		super(firstToken);
		this.paramNum = paramNum;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSource_CommandLineParam(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((paramNum == null) ? 0 : paramNum.hashCode());
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
		Source_CommandLineParam other = (Source_CommandLineParam) obj;
		if (paramNum == null) {
			if (other.paramNum != null)
				return false;
		} else if (!paramNum.equals(other.paramNum))
			return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Source_CommandLineParam [paramNum=");
		builder.append(paramNum);
		builder.append("]");
		return builder.toString();
	}


	
	

}
