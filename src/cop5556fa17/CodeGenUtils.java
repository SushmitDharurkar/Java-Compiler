package cop5556fa17;

import java.io.OutputStream;
import java.io.PrintStream;
/**
 * This class contains several static methods useful when developing
 * the code generation part of our compiler.
 * 
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import cop5556fa17.TypeUtils.Type;

public class CodeGenUtils{
	


	/**
	 * Converts the provided classfile
	 * in a human readable format and returns as a String.
	 * 
	 * @param bytecode
	 */
	public static String bytecodeToString(byte[] bytecode) {
		int flags = ClassReader.SKIP_DEBUG;
		ClassReader cr;
		cr = new ClassReader(bytecode);
		StringWriter out = new StringWriter();
		cr.accept(new TraceClassVisitor(new PrintWriter(out)), flags);
		return out.toString();
	}
	
	/**
	 * Prints the provided classfile, generally created by asm,
	 * in a human readable format
	 * 
	 * @param bytecode
	 */
	public static void dumpBytecode(byte[] bytecode) {
		int flags = ClassReader.SKIP_DEBUG;
		ClassReader cr;
		cr = new ClassReader(bytecode);
		PrintStream out = System.out;
		cr.accept(new TraceClassVisitor(new PrintWriter(out)), flags);
	}

	/**
	 * Loader for dynamically generated classes.
	 * Instantiated by getInstance.
	 *
	 */
	public static class DynamicClassLoader extends ClassLoader {
		public DynamicClassLoader(ClassLoader parent) {
			super(parent);
		}

		public Class<?> define(String className, byte[] bytecode) {
			return super.defineClass(className, bytecode, 0, bytecode.length);
		}
	};

	
	/**
	 * Creates an instance of the indicated class from the provided byteCode.
	 * args is passed as a parameter to the constructor, and in order to
	 * be the correct type for generated code, should be String[]
	 * 	 
	 * 
	 * @param name
	 * @param byteCode
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Runnable getInstance(String name, byte[] byteCode, Object args)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		DynamicClassLoader loader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
		Class<?> testClass = loader.define(name, byteCode);
		Constructor<?> constructor = testClass.getConstructor(args.getClass());
		return (Runnable) constructor.newInstance(args);
	}
	
	/**
	 * Generates code to print the given String followed by ; to the standard output.
	 * IF !GEN, does not generate code.
	 * Used to allow observation of execution of generated program
	 * during development and grading.
	 * 
	 * @param mv
	 * @param message
	 */
	public static void genPrint(boolean GEN, MethodVisitor mv, String message) {
		if(GEN){
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(message + ";");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
		}
	}
	
	/**
	 * Generates code to log the given String with a ; appended
	 * IF !GEN, does not generate code.
	 * 
	 * @param mv
	 * @param message
	 */
	public static void genLog(boolean GEN, MethodVisitor mv, String message) {
		if(GEN){
		mv.visitLdcInsn(message + ";");
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cop5556fa17/RuntimeLog", "globalLogAddEntry", "(Ljava/lang/String;)V", false);
		}
	}	
	/**
	 * Generates code to print the value on top of the stack without consuming it.
	 * If !GEN, does not generate code.
	 * 
	 * GEN Requires stack is not empty, and type matches the given type.
	 * 
	 * Currently implemented only for integer and boolean.
	 * 
	 * @param GEN   
	 * @param mv
	 * @param type
	 */
	public static void genLogTOS(boolean GEN, MethodVisitor mv, Type type) {
		if (GEN) {
			//duplicate top of stack
			mv.visitInsn(Opcodes.DUP);		
			switch (type) {
			case INTEGER: {
				//convert to String
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer","toString","(I)Ljava/lang/String;", false);				
			}
				break;
			case BOOLEAN: {
				//convert to String
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean","toString","(Z)Ljava/lang/String;", false);
			}
				break;
			case IMAGE: {
				//write Image reference to image log, leaving stack in original state
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cop5556fa17/RuntimeLog", "globalLogAddImage", "("+ImageSupport.ImageDesc + ")V", false);
				return;
			}
			default: {
				throw new RuntimeException("genLogTOS called unimplemented type " + type);
			}
			}
			//if here, type was INTEGER or BOOLEAN
			//add ; to end
			mv.visitLdcInsn(";");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
			//write string to log, leaving stack in original state
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cop5556fa17/RuntimeLog", "globalLogAddEntry", "(Ljava/lang/String;)V", false);

		}
		
	}
	
	/**
	 * Generates code to print the value on top of the stack to the standard output without consuming it.
	 * If !GEN, does not generate code.
	 * 
	 * GEN Requires stack is not empty, and type matches the given type.
	 * 
	 * Currently implemented only for integer and boolean.
	 * 
	 * @param GEN   
	 * @param mv
	 * @param type
	 */
	public static void genPrintTOS(boolean GEN, MethodVisitor mv, Type type) {
		if (GEN) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitInsn(Opcodes.SWAP);
			switch (type) {
			case INTEGER: {
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V", false);

			}
				break;
			case BOOLEAN: {
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Z)V", false);
			}
				break;
			default: {
				throw new RuntimeException("genPrintTOS called unimplemented type " + type);
			}
			}
			//add ; to end
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn(";");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);		
		}
	}
}
