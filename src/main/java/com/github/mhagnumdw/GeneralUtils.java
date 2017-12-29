package com.github.mhagnumdw;

import java.io.PrintWriter;
import java.io.StringWriter;

// TODO: JavaDoc!
public class GeneralUtils {

	private GeneralUtils() {

	}

//	public static String getStackTrace(final Throwable throwable) {
//		final StringWriter sw = new StringWriter();
//		final PrintWriter pw = new PrintWriter(sw, true);
//		throwable.printStackTrace(pw);
//		return sw.getBuffer().toString();
//	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

}
