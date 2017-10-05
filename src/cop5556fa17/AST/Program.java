

package cop5556fa17.AST;

import java.util.ArrayList;

import cop5556fa17.Scanner.Token;

public class Program extends ASTNode {
	
	public final String name;
	public final ArrayList<ASTNode> decsAndStatements;
	


	
	public Program(Token firstToken, Token name, ArrayList<ASTNode> decsAndStatements) {
		super(firstToken);
		this.name=name.getText();
		this.decsAndStatements=decsAndStatements;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception{
		return v.visitProgram(this,arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((decsAndStatements == null) ? 0 : decsAndStatements.hashCode());
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
		Program other = (Program) obj;
		if (decsAndStatements == null) {
			if (other.decsAndStatements != null)
				return false;
		} else if (!decsAndStatements.equals(other.decsAndStatements))
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
		builder.append("Program [name=");
		builder.append(name);
		builder.append(", decsAndStatements=");
		builder.append(decsAndStatements);
		builder.append("]");
		return builder.toString();
	}





}

