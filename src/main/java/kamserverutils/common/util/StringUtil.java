package kamserverutils.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

final public class StringUtil {

	private StringUtil() {
	}

	public static boolean isNumeric(final String intAsStr, int maxLength) {
		// TODO length of 40 check is a hack
		if (intAsStr == null || intAsStr.length() > maxLength || intAsStr.length() == 0) {
			return false;
		}
                
	 	int i = 0;
		for (final char c : intAsStr.toCharArray()) {
			if (!(c == '-' && i++ == 0 && intAsStr.length() > 1) && (c != '0'
							&& c != '1'
							&& c != '2'
							&& c != '3'
							&& c != '4'
							&& c != '5'
							&& c != '6'
							&& c != '7'
							&& c != '8'
							&& c != '9')) {
				return false;
			}
		}
		return true;
	}

	public static Integer parseOrNull(final String str) {

		if (StringUtil.isNumeric(str, 20)) {
			return Integer.parseInt(str);
		}
		return null;
	}

	public static String subStrFrom(final String custom, int from) {
		if (custom == null) {
			return null;
		}
		final int length = custom.length();
		if (from > length) {
			return "";
		}
		return custom.substring(from, length);
	}

	public static String subStrTo(final String freeText, final int to) {
		if (freeText == null) {
			return null;
		}
		final int length = freeText.length();
		if (to > length) {
			return freeText;
		}
		return freeText.substring(0, to);
	}

	interface StringTransformer {

		String transform(String str);
	}

	// This looks ike null or trim
	public static String nullableTrim(final String str) {
		if (str == null) {
			return null;
		}
		final String trimmedStr = str.trim();
		if (trimmedStr.length() == 0) {
			return null;
		}
		return trimmedStr;
	}

	public static String trimAndToLower(final String str) {
		if (str == null) {
			return null;
		}
		return str.trim().toLowerCase();
	}

	public static boolean isNullWhiteSpace(final String str) {
		return str == null || str.trim().isEmpty();
	}

	public static boolean isNullOrEmptyStr(final String str) {
		return str == null || "".equals(str);
	}

	public static String join(final String[] arr, final String deliminator) {
		final StringBuilder bldr = new StringBuilder();
		boolean seenFirst = false;
		for (final String str : arr) {
			if (seenFirst) {
				bldr.append(deliminator);
			}
			bldr.append(str);
			seenFirst = true;
		}
		return bldr.toString();
	}

	public static CharSequence interleaveAndHTMLEscape(final String str, final String delimiter) {
		final StringBuilder bldr = new StringBuilder();

		for (final char c : str.toCharArray()) {
			bldr.append(StringUtil.escapeHTML(c + "")).append(delimiter);
		}

		return bldr;
	}

	private static CharSequence interleave(final String str, final String delimiter) {
		final StringBuilder bldr = new StringBuilder();

		for (final char c : str.toCharArray()) {
			bldr.append(c).append(delimiter);
		}

		return bldr;
	}

	/**
	 * this looks like nullableTrim
	 *
	 * @param str
	 * @return Null if null or empty, else trimmed string
	 */
	public static String nullOrTrim(final String str) {
		if (StringUtil.isNullWhiteSpace(str)) {
			return null;
		}
		return str.trim();
	}

	public static String twilioifyPhoneNumber(final String str) {
//		return str.replaceAll("^+1 ", "");
		return str;
	}

	public static String truncate(final String str, final int maxLength) {
		if (maxLength < 0) {
			throw new IllegalArgumentException();
		}
		if (str == null) {
			return null;
		}
		final int length = str.length();
		if (length <= maxLength) {
			return str;
		}
		return str.substring(0, maxLength);
	}

	public static String escapeHTML(final String plainText) {
		if (plainText == null) {
			return null;
		}
		return plainText.replaceAll("&", "&amp;").
						replaceAll("\"", "&quot;").
						replaceAll("<", "&lt;").
						replaceAll(">", "&gt;");
	}

	public static String throwableToString(final Throwable t) {
		if (t == null) {
			return null;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	public static String longToStr(final long l) {
		final StringBuilder bldr = new StringBuilder();

		for (int i = 0; i < Long.numberOfLeadingZeros(l); i++) {
			bldr.append('0');
		}
		bldr.append(Long.toBinaryString(l));

		return bldr.toString();
	}

	/*
	 * Non-understood string values are not treated as false, NULL instead.
	 */
	public static Boolean toBoolean(final String str) {


		if (str == null) {
			return null;
		}
		String normalizedString = str.toLowerCase();


		if ("f".equals(normalizedString) || "false".equals(normalizedString)) {
			return Boolean.FALSE;
		}

		if ("t".equals(normalizedString) || "true".equals(normalizedString)) {
			return Boolean.TRUE;
		}

		return null;

	}

	public static boolean toBoolean(final String str, final boolean defaultValue) {


		final Boolean bool = StringUtil.toBoolean(str);

		return bool == null ? defaultValue : bool;
	}

	public static String nullableToString(final Object o) {
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	public static String nullableTrimPartParam(final HttpServletRequest req,
					final String partName) throws IOException, ServletException {
		final Part part = req.getPart(partName);
		if (part == null) {
			return null;
		}

		return StringUtil.nullableTrim(IOUtil.inputStreamToString(part.getInputStream()));

	}
}
