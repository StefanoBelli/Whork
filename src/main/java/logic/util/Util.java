package logic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Util {
	private Util() {}
	
	public static void exceptionLog(Exception e) {
		Logger logger = LoggerFactory.getLogger("WhorkExceptionLogger");

		logger.error("***************************");
		logger.error("* EXCEPTION LOGGING START *");
		logger.error("***************************");
		logger.error("");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		StringBuilder builder = new StringBuilder();
		builder.append("--> ")
			.append(trace[0].getMethodName())
			.append(" got called by: ")
			.append(trace[1].getClassName())
			.append("#")
			.append(trace[1].getMethodName())
			.append("()");

		logger.error(builder.toString());
		logger.error("");
		e.printStackTrace();
		logger.error("");
		logger.error("*************************");
		logger.error("* EXCEPTION LOGGING END *");
		logger.error("*************************");
	}
}
