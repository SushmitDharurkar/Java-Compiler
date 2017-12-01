package cop5556fa17;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556fa17.TypeUtils.Type;
import cop5556fa17.Scanner.Kind;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
import cop5556fa17.AST.Expression_FunctionAppWithExprArg;
import cop5556fa17.AST.Expression_FunctionAppWithIndexArg;
import cop5556fa17.AST.Expression_Ident;
import cop5556fa17.AST.Expression_IntLit;
import cop5556fa17.AST.Expression_PixelSelector;
import cop5556fa17.AST.Expression_PredefinedName;
import cop5556fa17.AST.Expression_Unary;
import cop5556fa17.AST.Index;
import cop5556fa17.AST.LHS;
import cop5556fa17.AST.Program;
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.AST.Statement_Assign;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * All methods and variable static.
	 */


	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;
	


	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.name;  
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		cw.visitSource(sourceFileName, null);
		// create main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		// initialize
		mv.visitCode();		
		//add label before first instruction
		Label mainStart = new Label();
		mv.visitLabel(mainStart);		
		// if GRADE, generates code to add string to log
//		CodeGenUtils.genLog(GRADE, mv, "entering main");

		// visit decs and statements to add field to class
		//  and instructions to main method, respectively
		ArrayList<ASTNode> decsAndStatements = program.decsAndStatements;
		for (ASTNode node : decsAndStatements) {
			node.visit(this, arg);
		}

		//generates code to add string to log
//		CodeGenUtils.genLog(GRADE, mv, "leaving main");
		
		//adds the required (by the JVM) return statement to main
		mv.visitInsn(RETURN);
		
		//adds label at end of code
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		
		//handles parameters and local variables of main. Right now, only args
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);

		//Sets max stack size and number of local vars.
		//Because we use ClassWriter.COMPUTE_FRAMES as a parameter in the constructor,
		//asm will calculate this itself and the parameters are ignored.
		//If you have trouble with failures in this routine, it may be useful
		//to temporarily set the parameter in the ClassWriter constructor to 0.
		//The generated classfile will not be correct, but you will at least be
		//able to see what is in it.
		mv.visitMaxs(0, 0);
		
		//terminate construction of main method
		mv.visitEnd();
		
		//terminate class construction
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

	//	Declaration_Variable​ ​ ::=​ ​ ​ Type​ ​ name​ ​ (Expression​ ​ | ​ ​ ε ​ ​ )

	@Override
	public Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg) throws Exception {
		FieldVisitor fv;
		String name = declaration_Variable.name;
		if (declaration_Variable.getType() == Type.INTEGER){
			fv = cw.visitField(ACC_STATIC, name, "I", null, 0);
			if (declaration_Variable.e != null){
				declaration_Variable.e.visit(this, arg);
				mv.visitFieldInsn(PUTSTATIC, className, name, "I");
			}
		}
		else {	//Boolean
			fv = cw.visitField(ACC_STATIC, name, "Z", null, false);
			if (declaration_Variable.e != null){
				declaration_Variable.e.visit(this, arg);
				mv.visitFieldInsn(PUTSTATIC, className, name, "Z");
			}
		}
		fv.visitEnd();
		return declaration_Variable;
	}

	//	Expression_Binary​ ​ ::=​ ​ Expression​_0​ ​ ​ op​ ​ Expression​_1

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception {
		expression_Binary.e0.visit(this, arg);
		expression_Binary.e1.visit(this, arg);

		Kind op = expression_Binary.op;
		if (op == Kind.OP_OR){
			mv.visitInsn(IOR);
		}
		else if (op == Kind.OP_AND){
			mv.visitInsn(IAND);
		}
		else if (op == Kind.OP_PLUS){
			mv.visitInsn(IADD);
		}
		else if (op == Kind.OP_MINUS){
			mv.visitInsn(ISUB);
		}
		else if (op == Kind.OP_DIV){
			mv.visitInsn(IDIV);
		}
		else if (op == Kind.OP_TIMES){
			mv.visitInsn(IMUL);
		}
		else if (op == Kind.OP_MOD){
			mv.visitInsn(IREM);
		}
		else{
			Label l1 = new Label();
			if (op == Kind.OP_EQ) {
				mv.visitJumpInsn(IF_ICMPNE, l1);    //If False
			}
			else if (op == Kind.OP_NEQ){
				mv.visitJumpInsn(IF_ICMPEQ, l1);	//If False
			}
			else if (op == Kind.OP_LT){
				mv.visitJumpInsn(IF_ICMPGE, l1);	//If False
			}
			else if (op == Kind.OP_GT){
				mv.visitJumpInsn(IF_ICMPLE, l1);	//If False
			}
			else if (op == Kind.OP_LE){
				mv.visitJumpInsn(IF_ICMPGT, l1);	//If False
			}
			else if (op == Kind.OP_GE){
				mv.visitJumpInsn(IF_ICMPLT, l1);	//If False
			}
			//If true
			mv.visitInsn(ICONST_1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			//If False
			mv.visitLabel(l1);
			mv.visitInsn(ICONST_0);
			//Goto
			mv.visitLabel(l2);
		}

//		CodeGenUtils.genLogTOS(GRADE, mv, expression_Binary.getType());
		return expression_Binary;
	}

	//	Expression_Unary​ ​ ::=​ ​ op​ ​ Expression

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception {
		Type type = expression_Unary.getType();
		Kind op = expression_Unary.op;
		expression_Unary.e.visit(this, arg);
		if (op == Kind.OP_MINUS){
			mv.visitInsn(INEG);
		}
		else if (op == Kind.OP_EXCL){
			if (type == Type.INTEGER){
				mv.visitLdcInsn(2147483647);
				mv.visitInsn(IXOR);
			}
			else if (type == Type.BOOLEAN){
				Label l1 = new Label();
				mv.visitJumpInsn(IFEQ, l1);	//If value is false
				//If value is true make it false
				mv.visitInsn(ICONST_0);
				Label l2 = new Label();
				mv.visitJumpInsn(GOTO, l2);
				//If value is false make it true
				mv.visitLabel(l1);
				mv.visitInsn(ICONST_1);
				//Goto
				mv.visitLabel(l2);
			}
		}
//		CodeGenUtils.genLogTOS(GRADE, mv, type);
		return expression_Unary;
	}

	//	Index​ ​ ::=​ ​ Expression​_0​ ​ ​ Expression​_1
	// generate code to leave the two values on the stack
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		index.e0.visit(this, arg);
		index.e1.visit(this, arg);
		//Check this
		if (!index.isCartesian()){
			mv.visitInsn(DUP2);
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "cart_x", RuntimeFunctions.cart_xSig, false);
			mv.visitInsn(DUP_X2);
			mv.visitInsn(POP);
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "cart_y", RuntimeFunctions.cart_ySig, false);
		}
		return index;
	}

	//	Expression_PixelSelector​ ​ ::=​ ​ ​ ​ name​ ​ Index

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		mv.visitFieldInsn(GETSTATIC, className, expression_PixelSelector.name, ImageSupport.ImageDesc);
		expression_PixelSelector.index.visit(this, arg);
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "getPixel", ImageSupport.getPixelSig, false);
		return expression_PixelSelector;
	}

	//	Expression_Conditional​ ​ ::=​ ​ ​ Expression​_condition​ ​ ​ Expression​_true​ ​ ​ Expression​_false

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg) throws Exception {
		expression_Conditional.condition.visit(this, arg);

		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);	//False condition
		//True Condition
		expression_Conditional.trueExpression.visit(this, arg);
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		//False condition
		mv.visitLabel(l1);
		expression_Conditional.falseExpression.visit(this, arg);
		//Goto
		mv.visitLabel(l2);

//		CodeGenUtils.genLogTOS(GRADE, mv, expression_Conditional.trueExpression.getType());
		return expression_Conditional;
	}

	//	Declaration_Image​ ​ ​ ::=​ ​ name​ ​ ( ​ ​ ​ xSize​ ​ ySize​ ​ | ​ ​ ε )​ ​ Source

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		FieldVisitor fv;
		fv = cw.visitField(ACC_STATIC, declaration_Image.name, "Ljava/awt/image/BufferedImage;", null, null);
		fv.visitEnd();

		if (declaration_Image.source != null){
			declaration_Image.source.visit(this, arg);
			if (declaration_Image.xSize != null && declaration_Image.ySize != null){
				declaration_Image.xSize.visit(this, arg);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
				declaration_Image.ySize.visit(this, arg);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			}
			else {
				mv.visitInsn(ACONST_NULL);
				mv.visitInsn(ACONST_NULL);
			}
			//Check if these calls work without importing the class
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "readImage", ImageSupport.readImageSig, false);
		}
		else {
			if (declaration_Image.xSize != null && declaration_Image.ySize != null){
				declaration_Image.xSize.visit(this, arg);
				declaration_Image.ySize.visit(this, arg);
			}
			else {
				mv.visitLdcInsn(256);
				mv.visitLdcInsn(256);
			}
			//Check if these calls work without importing the class
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "makeImage", ImageSupport.makeImageSig, false);
		}
		mv.visitFieldInsn(PUTSTATIC, className, declaration_Image.name, "Ljava/awt/image/BufferedImage;");

		return declaration_Image;
	}

	//	Source_StringLiteral​ ​ ::=​ ​ ​ fileOrURL

	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception {
		mv.visitFieldInsn(GETSTATIC, className, source_StringLiteral.fileOrUrl, "Ljava/lang/String;");
		return source_StringLiteral;
	}

	//	Source_CommandLineParam​ ​ ​ ::=​ ​ Expression​_paramNum

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg) throws Exception {
		mv.visitVarInsn(ALOAD, 0);
		source_CommandLineParam.paramNum.visit(this, arg);
		mv.visitInsn(AALOAD);
		return source_CommandLineParam;
	}

	//	Source_Ident​ ​ ::=​ ​ name

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		mv.visitFieldInsn(GETSTATIC, className, source_Ident.name, "Ljava/lang/String;");
		return source_Ident;
	}

	//	Declaration_SourceSink​ ​ ​ ::=​ ​ Type​ ​ name​ ​ ​ Source

	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		declaration_SourceSink.source.visit(this, arg);

		FieldVisitor fv;
		fv = cw.visitField(ACC_STATIC, declaration_SourceSink.name, "Ljava/lang/String;", null, ""); //Note null or "" as initial value?
		fv.visitEnd();
		mv.visitFieldInsn(PUTSTATIC, className, declaration_SourceSink.name, "Ljava/lang/String;");
		return declaration_SourceSink;
	}

	//	Expression_IntLit​ ​ ::=​ ​ ​ value

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		mv.visitLdcInsn(expression_IntLit.value);
//		CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
		return expression_IntLit;
	}

	//Note We never matched KW_log in parser. Ask this on discussions
	//	Expression_FunctionAppWithExprArg​ ​ ::=​ ​ ​ function​ ​ Expression

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		expression_FunctionAppWithExprArg.arg.visit(this, arg);

		//Check if this works without importing RuntimeFunctions class
		mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "abs", RuntimeFunctions.absSig, false);

		return expression_FunctionAppWithExprArg;
	}

	//	Expression_FunctionAppWithIndexArg​ ​ ::=​ ​ ​ ​ function​ ​ Index

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		expression_FunctionAppWithIndexArg.arg.e0.visit(this, arg);
		expression_FunctionAppWithIndexArg.arg.e1.visit(this, arg);
		Kind k = expression_FunctionAppWithIndexArg.function;
		//Note I am already converting r,a to x,y in Index then what will happen here??
		if (k == Kind.KW_cart_x){
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "cart_x", RuntimeFunctions.cart_xSig, false);
		}
		else if (k == Kind.KW_cart_y){
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "cart_y", RuntimeFunctions.cart_ySig, false);
		}
		else if (k == Kind.KW_polar_r){
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_r", RuntimeFunctions.polar_rSig, false);
		}
		else if (k == Kind.KW_polar_a){
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_a", RuntimeFunctions.polar_aSig, false);
		}
		return expression_FunctionAppWithIndexArg;
	}

	//	Expression_PredefinedName​ ​ ::=​ ​ ​ predefNameKind

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		Kind k = expression_PredefinedName.kind;
		//Note Just loading current values on stack
		if (k == Kind.KW_x){
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
		}
		else if (k == Kind.KW_y){
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
		}
		else if (k == Kind.KW_X){
			mv.visitFieldInsn(GETSTATIC, className, "X", "I");
		}
		else if (k == Kind.KW_Y){
			mv.visitFieldInsn(GETSTATIC, className, "Y", "I");
		}
		else if (k == Kind.KW_r){
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_r", RuntimeFunctions.polar_rSig, false);
		}
		else if (k == Kind.KW_a){
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_a", RuntimeFunctions.polar_aSig, false);
		}
		else if (k == Kind.KW_R){
			mv.visitFieldInsn(GETSTATIC, className, "X", "I");
			mv.visitFieldInsn(GETSTATIC, className, "Y", "I");
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_r", RuntimeFunctions.polar_rSig, false);
		}
		else if (k == Kind.KW_A){
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(GETSTATIC, className, "Y", "I");
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className, "polar_a", RuntimeFunctions.polar_aSig, false);
		}
		//Just loading on stack
		else if (k == Kind.KW_DEF_X){
			mv.visitLdcInsn(256);
		}
		else if (k == Kind.KW_DEF_Y){
			mv.visitLdcInsn(256);
		}
		else if (k == Kind.KW_Z){
			mv.visitLdcInsn(16777215);
		}
		return expression_PredefinedName;
	}

	/** For Integers and booleans, the only "sink"is the screen, so generate code to print to console.
	 * For Images, load the Image onto the stack and visit the Sink which will generate the code to handle the image.
	 */

	//	Statement_Out​ ​ ::=​ ​ name​ ​ Sink

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception {
		Type type = statement_Out.getDec().getType();
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		if (type == Type.INTEGER){
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, "I");
			CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
		}
		else if (type == Type.BOOLEAN){
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, "Z");
			CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
		}
		else if (type == Type.IMAGE){
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, ImageSupport.ImageDesc);
			CodeGenUtils.genLogTOS(GRADE, mv, Type.IMAGE);
			statement_Out.sink.visit(this, arg);
		}
		return statement_Out;
	}

	/**
	 * Visit source to load rhs, which will be a String, onto the stack
	 */

	//	Statement_In​ ​ ::=​ ​ name​ ​ Source

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception {
		statement_In.source.visit(this, arg);
		Type type = statement_In.getDec().getType();
		if (type == Type.INTEGER){
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
			mv.visitFieldInsn(PUTSTATIC, className, statement_In.name, "I");
		}
		else if (type == Type.BOOLEAN){
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
			mv.visitFieldInsn(PUTSTATIC, className, statement_In.name, "Z");
		}
		else if (type == Type.IMAGE){
			mv.visitFieldInsn(PUTSTATIC, className, statement_In.name, "Ljava/lang/String;");
		}
		return statement_In;
	}

	//	Statement_Assign​ ​ ::=​ ​ ​ LHS​ ​ ​ Expression

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception {
		Type type = statement_Assign.lhs.getType();
		if (type == Type.INTEGER || type == Type.BOOLEAN ){
			statement_Assign.e.visit(this, arg);
			statement_Assign.lhs.visit(this, arg);
		}
		else if (type == Type.IMAGE){
			//Adding x, y, X and Y static fields to class
			FieldVisitor fv;
			{
				fv = cw.visitField(ACC_STATIC, "X", "I", null, 0);
				fv.visitEnd();
			}
			{
				fv = cw.visitField(ACC_STATIC, "Y", "I", null, 0);
				fv.visitEnd();
			}
			{
				fv = cw.visitField(ACC_STATIC, "x", "I", null, 0);
				fv.visitEnd();
			}
			{
				fv = cw.visitField(ACC_STATIC, "y", "I", null, 0);
				fv.visitEnd();
			}
			//Setting X
			mv.visitFieldInsn(GETSTATIC, className, statement_Assign.lhs.name, ImageSupport.ImageDesc);
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "getX", ImageSupport.getXSig, false);
			mv.visitFieldInsn(PUTSTATIC, className, "X", "I");
			//Setting Y
			mv.visitFieldInsn(GETSTATIC, className, statement_Assign.lhs.name, ImageSupport.ImageDesc);
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "getY", ImageSupport.getYSig, false);
			mv.visitFieldInsn(PUTSTATIC, className, "Y", "I");

			//For loop here
			//Initially using x and y as 0
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTSTATIC, className, "y", "I");
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
			mv.visitFieldInsn(GETSTATIC, className, "Y", "I");
			Label l1 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l1);
			//Initially using x as 0
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTSTATIC, className, "x", "I");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
			mv.visitFieldInsn(GETSTATIC, className, "X", "I");
			Label l3 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l3);
			//Innermost code in for loop
			statement_Assign.e.visit(this, arg);
			statement_Assign.lhs.visit(this, arg);
			//Incrementing inner x
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitFieldInsn(PUTSTATIC, className, "x", "I");
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l3);
			//Incrementing outer y
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitFieldInsn(PUTSTATIC, className, "y", "I");
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
		}
		return statement_Assign;
	}

	//	LHS​ ​ ::=​ ​ name​ ​ Index

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		Type type = lhs.getType();
		if (type == Type.INTEGER){
			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, "I");
		}
		else if (type == Type.BOOLEAN){
			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, "Z");
		}
		else if (type == Type.IMAGE){
			mv.visitFieldInsn(GETSTATIC, className, lhs.name, ImageSupport.ImageDesc);
			//Note Changing flow here as unnecessary conversions in index
//			lhs.index.visit(this, arg);
			mv.visitFieldInsn(GETSTATIC, className, "x", "I");
			mv.visitFieldInsn(GETSTATIC, className, "y", "I");
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "setPixel", ImageSupport.setPixelSig, false);
		}
		return lhs;
	}

	//	Sink_SCREEN​ ​ ::=​ ​ SCREEN

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		mv.visitMethodInsn(INVOKESTATIC, ImageFrame.className, "makeFrame", "(Ljava/awt/image/BufferedImage;)Ljavax/swing/JFrame;",false);
		//Check if this works
		mv.visitInsn(POP);
		return sink_SCREEN;
	}

	//	Sink_Ident​ ​ ::=​ ​ name

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		mv.visitFieldInsn(GETSTATIC, className, sink_Ident.name, "Ljava/lang/String;");
		//Check if these calls work without importing the class
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className, "write", ImageSupport.writeSig, false);
		return sink_Ident;
	}

	//	Expression_BooleanLit​ ​ ::=​ ​ ​ value

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {
		if (expression_BooleanLit.value){
			mv.visitInsn(ICONST_1);	//true opcode
		}
		else {
			mv.visitInsn(ICONST_0);	//false opcode
		}
//		CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
		return expression_BooleanLit;
	}

	//	Expression_Ident​ ​ ​ ::=​ ​ ​ ​ name

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident, Object arg) throws Exception {
		Type type = expression_Ident.getType();
		if (type == Type.INTEGER){
			mv.visitFieldInsn(GETSTATIC, className, expression_Ident.name, "I");
		}
		else if (type == Type.BOOLEAN){
			mv.visitFieldInsn(GETSTATIC, className, expression_Ident.name, "Z");
		}
//		CodeGenUtils.genLogTOS(GRADE, mv, type);
		return expression_Ident;
	}

}
