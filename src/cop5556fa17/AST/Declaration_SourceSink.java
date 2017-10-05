package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

public class Declaration_SourceSink extends Declaration {
	
	public final Kind type;
	public final String name;
	public final Source source;
	
	public Declaration_SourceSink(Token firstToken, Token type, Token name, Source source) {
		super(firstToken);
		this.type = type.kind;
		this.name = name.getText();
		this.source = source;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDeclaration_SourceSink(this,arg);
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Declaration_SourceSink other = (Declaration_SourceSink) obj;
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
		if (type != other.type)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Declaration_SourceSink [type=" + type + ", name=" + name + ", source=" + source + "]";
	}




	
}
