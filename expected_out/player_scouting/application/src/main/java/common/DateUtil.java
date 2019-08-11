package common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

    // Unknown:
    // ADT - Atlantic Daylight Time
    // ERST - European Russia Standard Time
    public static Map<String, TimeZone> TIME_ZONES = new HashMap<>();

    static {

        // EET - Eastern European Time
        TIME_ZONES.put("EET", TimeZone.getTimeZone("EET"));
        // AKST - Alaska Standard Time
        TIME_ZONES.put("AKST", TimeZone.getTimeZone("America/Anchorage"));
        // AKDT -Alaska Daylight Time
        TIME_ZONES.put("AKDT", TimeZone.getTimeZone("America/Anchorage"));
        // EST - Eastern Standard Time 
        TIME_ZONES.put("EDT", TimeZone.getTimeZone("America/New_York"));
        // EST - Eastern Standard Time 
        TIME_ZONES.put("EST", TimeZone.getTimeZone("America/New_York"));
        // CST - Central Standard Time
        TIME_ZONES.put("CST", TimeZone.getTimeZone("America/Chicago"));
        // CDT - Central Daylight Time
        TIME_ZONES.put("CDT", TimeZone.getTimeZone("America/Chicago"));
        // MST - Mountain Standard Time
        TIME_ZONES.put("MST", TimeZone.getTimeZone("America/Denver"));
        // MDT - Mountain Daylight Time
        TIME_ZONES.put("MDT", TimeZone.getTimeZone("America/Denver"));
        // PDT - Pacific Daylight Time
        TIME_ZONES.put("PDT", TimeZone.getTimeZone("America/Los_Angeles"));
        // PST  - Pacific Standard Time
        TIME_ZONES.put("PST", TimeZone.getTimeZone("America/Los_Angeles"));
        // NTS - Newfoundland Standard Time
        TIME_ZONES.put("NTS", TimeZone.getTimeZone("Canada/Newfoundland"));
        // HADT - Hawaii-Aleutian Daylight Time
        TIME_ZONES.put("HADT", TimeZone.getTimeZone("US/Aleutian"));
        // HAST - Hawaii-Aleutian Standard Time
        TIME_ZONES.put("HAST", TimeZone.getTimeZone("US/Aleutian"));
        // BST - British Summer Time
        TIME_ZONES.put("BST", TimeZone.getTimeZone("BST"));
        // GMT - Greenwich Mean Time
        TIME_ZONES.put("GMT", TimeZone.getTimeZone("GMT"));
        // CET - Central European Time
        TIME_ZONES.put("CET", TimeZone.getTimeZone("CET"));
        // AST - Atlantic Standard Time
        TIME_ZONES.put("AST", TimeZone.getTimeZone("AST"));

    }

    static public Date extract(final String pattern,
            final String asString,
            final String tzAsString) throws ParseException {
        final TimeZone tz = TIME_ZONES.get(tzAsString);
        if (tz == null) {
            throw new RuntimeException("Unknown timezone " + tzAsString);
        }
        // 21:43:59 2016-2-11
        // hh:mm:ss YYYY-M-d
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(tz);
        return sdf.parse(asString);
    }

    public static final String SIMPLE_DATE = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String STD_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String STD_W_SEC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.'SSSXXX";

    public static Date parseUTCOffSetDate(final String str) throws ParseException {

        if (str == null) {
            return null;
        }

        final DateFormat df
                = new SimpleDateFormat(str.matches(".*\\d\\.\\d.*")
                        ? STD_W_SEC_FORMAT : STD_FORMAT);
        return df.parse(str);
    }

    public static Date parseUTCOffSetDateWrapException(final String str) {
        try {
            return parseUTCOffSetDate(str);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
