package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Expression_Conditional extends Expression {
	
	public final Expression condition;
	public final Expression trueExpression;
	public final Expression falseExpression;

	public Expression_Conditional(Token firstToken, Expression condition, Expression trueExpression,
			Expression falseExpression) {
		super(firstToken);
		this.condition = condition;
		this.trueExpression = trueExpression;
		this.falseExpression = falseExpression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_Conditional(this,arg);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((falseExpression == null) ? 0 : falseExpression.hashCode());
		result = prime * result + ((trueExpression == null) ? 0 : trueExpression.hashCode());
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
		Expression_Conditional other = (Expression_Conditional) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (falseExpression == null) {
			if (other.falseExpression != null)
				return false;
		} else if (!falseExpression.equals(other.falseExpression))
			return false;
		if (trueExpression == null) {
			if (other.trueExpression != null)
				return false;
		} else if (!trueExpression.equals(other.trueExpression))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression_Conditional [condition=");
		builder.append(condition);
		builder.append(", trueExpression=");
		builder.append(trueExpression);
		builder.append(", falseExpression=");
		builder.append(falseExpression);
		builder.append("]");
		return builder.toString();
	}
	
	

}
