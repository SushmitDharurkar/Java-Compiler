package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;


public class Declaration_Variable extends Declaration {
	
	public final Token type;
	public final String name;
	public final Expression e;
	

	public Declaration_Variable(Token firstToken,  Token type, Token name, Expression e) {
		super(firstToken);
		this.type = type;
		this.name = name.getText();
		this.e = e;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDeclaration_Variable(this,arg);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Declaration_Variable other = (Declaration_Variable) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Declaration_Variable [type=" + type + ", name=" + name + ", e="
				+ e + "]";
	}










	
	

}
