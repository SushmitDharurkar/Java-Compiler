//package cop5556fa17.AST;
//
//import cop5556fa17.Scanner.Token;
//
//public class Expression_Ident extends Expression {
//	
//	public final String name;
//
//	public Expression_Ident(Token firstToken, Token name) {
//		super(firstToken);
//		this.name = name.getText();
//	}
//
//	@Override
//	public Object visit(ASTVisitor v, Object arg) throws Exception {
//		return v.visitExpression_Ident(this,arg);
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (!(obj instanceof Expression_Ident))
//			return false;
//		Expression_Ident other = (Expression_Ident) obj;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "Expression_Ident [name=" + name + "]";
//	}
//
//
//
//}
package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Expression_Ident extends Expression {
	
	public final String name;
	
	

	public Expression_Ident(Token firstToken, Token ident) {
		super(firstToken);
		this.name = ident.getText();
	}



	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExpression_Ident(this, arg);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		Expression_Ident other = (Expression_Ident) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Expression_Ident [name=" + name + "]";
	}

	
}
