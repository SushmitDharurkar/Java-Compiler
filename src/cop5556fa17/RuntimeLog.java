package cop5556fa17;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 
 * A simple globalLog that can be used to record a trace of 
 * an instrumented program.
 * 
 * The output can be used for grading and debugging.
 *
 */
public class RuntimeLog {

	private StringBuffer sb;

	public static RuntimeLog globalLog;
	public static ArrayList<BufferedImage> globalImageLog;
	
	public static void initLog() {
		globalLog = new RuntimeLog();
		globalLog.sb = new StringBuffer();
		globalImageLog = new ArrayList<BufferedImage>();
	}
	
	public static void globalLogAddImage(BufferedImage image) {
		if (globalImageLog != null) globalImageLog.add(image);
	}
	
	public static void addImage(BufferedImage image) {
		globalImageLog.add(image);
	}
	
	public static void globalLogAddEntry(String entry){
		if (globalLog != null) globalLog.addEntry(entry);
	}
	
	private void addEntry(String entry) {
		sb.append(entry);
	}

	public static String getGlobalString() {
		return (globalLog != null) ? globalLog.toString() : "";
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	public static void resetLogToNull() {
		globalLog = null;
		globalImageLog = null;
	}

}
