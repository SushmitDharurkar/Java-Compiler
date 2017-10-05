package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Sink_Ident extends Sink {
	
	public final String name;
	
	public Sink_Ident(Token firstToken, Token name) {
		super(firstToken);
		this.name = name.getText();
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSink_Ident(this,arg);
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
		Sink_Ident other = (Sink_Ident) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sink_Ident [name=" + name + "]";
	}

	
}
