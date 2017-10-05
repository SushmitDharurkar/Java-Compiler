package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

public class Expression_FunctionAppWithIndexArg extends Expression_FunctionApp {
	
	public final Kind function;
	public final Index arg;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_FunctionAppWithIndexArg(this,arg); 
	}

	public Expression_FunctionAppWithIndexArg(Token firstToken, Kind function, Index arg) {
		super(firstToken);
		this.function = function;
		this.arg = arg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((arg == null) ? 0 : arg.hashCode());
		result = prime * result + ((function == null) ? 0 : function.hashCode());
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
		Expression_FunctionAppWithIndexArg other = (Expression_FunctionAppWithIndexArg) obj;
		if (arg == null) {
			if (other.arg != null)
				return false;
		} else if (!arg.equals(other.arg))
			return false;
		if (function != other.function)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_FunctionAppWithIndexArg [function=");
		builder.append(function);
		builder.append(", arg=");
		builder.append(arg);
		builder.append("]");
		return builder.toString();
	}



}
