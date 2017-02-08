package util;

public class LogInfo {
	
	public void info(String format, Object... arguments) {
		System.out.printf(format, arguments);
		System.out.println();
	}
	
	public void error(String format, Object... arguments) {
		System.err.printf(format, arguments);
		System.out.println();
	}
	
}
