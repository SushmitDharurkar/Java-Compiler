package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;

public class Statement_Out extends Statement {

	public final String name;
	public final Sink sink;
	
	Declaration dec;  //declaration for name.  Set during type checking
	public void setDec(Declaration dec) {this.dec = dec;}
	public Declaration getDec() {return dec;}
	
	public Statement_Out(Token firstToken, Token name, Sink sink) {
		super(firstToken);
		this.name = name.getText();
		this.sink = sink;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatement_Out(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sink == null) ? 0 : sink.hashCode());
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
		Statement_Out other = (Statement_Out) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sink == null) {
			if (other.sink != null)
				return false;
		} else if (!sink.equals(other.sink))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Statement_Out [name=" + name + ", sink=" + sink + "]";
	}


	
	

}
