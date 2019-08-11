package common;

public class StringUtil {

    public static boolean isNullOrWhiteSpace(final String str) {
        if (str == null) {
            return true;
        }

        return str.trim().isEmpty();
    }

    public static String zeroPad(final String str, final int len) {
        if (str == null) {
            return str;
        }

        String retStr = str;
        while (retStr.length() < len) {
            retStr = "0" + retStr;
        }

        return retStr;
    }

    public static Double parseClock(final String str) {
        if (str == null || str.isEmpty() || "-".equals(str)) {
            return null;
        }

        final String[] parts = str.split(":");
        final boolean isNegative = str.charAt(0) == '-';

        if (parts.length == 1) {
            return 60 * Math.abs(Double.parseDouble(parts[0]));
        } else if (parts.length == 2) {
            // Minutes : seconds
            double minutes = Math.abs(Double.parseDouble(parts[0]));
            double seconds = Double.parseDouble(parts[1]);
            return (minutes * 60 + seconds) * (isNegative ? -1 : 1);
        } else if (parts.length == 3) {
            // Hours : minutes : seconds
            double hours = Math.abs(Double.parseDouble(parts[0]));
            double minutes = Math.abs(Double.parseDouble(parts[1]));
            double seconds = Double.parseDouble(parts[2]);
            return (hours * 3600 + minutes * 60 + seconds) * (isNegative ? -1 : 1);

        }

        throw new RuntimeException("Illegal format " + str);
    }
}
