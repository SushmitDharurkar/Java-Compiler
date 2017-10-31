package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils;

public abstract class Expression_FunctionApp extends Expression {

	public Expression_FunctionApp(Token firstToken) {
		super(firstToken);
		
	}

	@Override
	public void setType(TypeUtils.Type type) {
		this.type = type;
		super.setType(type);
	}

}
