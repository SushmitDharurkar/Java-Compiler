package cop5556fa17;

/**
 * 
 * A simple globalLog that can be used to record a trace of 
 * an instrumented program.
 * 
 * The output can be used for grading and debugging.
 * 
 * initLog should be called in every test case prior to executing the class
 *
 */
public class RuntimeLog {

	private StringBuffer sb;

	public static RuntimeLog globalLog;
	
	public static void initLog() {
		globalLog = new RuntimeLog();
		globalLog.sb = new StringBuffer();
	}
	
	public static void globalLogAddEntry(String entry){
		if (globalLog != null) globalLog.addEntry(entry);
	}
	
	private void addEntry(String entry) {
		sb.append(entry);
	}


	@Override
	public String toString() {
		return sb.toString();
	}

	public static void resetLogToNull() {
		globalLog = null;
	}

}
