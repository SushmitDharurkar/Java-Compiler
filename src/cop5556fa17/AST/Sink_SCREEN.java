package cop5556fa17.AST;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import static cop5556fa17.Scanner.Kind.*;


public class Sink_SCREEN extends Sink {
	
	public final Kind kind;  //this is just here to allow generated hashcode, equals, and toString

	public Sink_SCREEN(Token firstToken) {
		super(firstToken);
		this.kind = KW_SCREEN;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSink_SCREEN(this, arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Sink_SCREEN))
			return false;
		Sink_SCREEN other = (Sink_SCREEN) obj;
		if (kind != other.kind)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sink_SCREEN [kind=" + kind + "]";
	}
	
	

}
