package kamserverutils.common.util;

import kamserverutils.common.exec.ErrorType;
import kamserverutils.common.exec.ExecutionResult;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final public class DateUtil {

	final static private String[] UTC_OFFSETS = {"-12", "-11", "-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3",
		"-2", "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	public final static long MS_PER_MINUTE = 60 * 1000;
	public final static long MS_PER_HOUR = 60 * MS_PER_MINUTE;
	public final static long MS_PER_DAY = 24 * MS_PER_HOUR;
	public final static long MS_PER_WEEK = 7 * MS_PER_DAY;
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz";
	private static final String DATE_TIME_FORMAT_WITH_OFFSET = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final String PRESENTATION_DATE_FORMAT = "EEE MMM d yyyy 'at' h:mma";
	private static final String PRESENTATION_TIME_PART_FORMAT = "h:mma";
	private static final String PRESENTATION_DATE_PART_FORMAT = "EEEEE MMM d";
	private static final String DAY_ONLY_PART_FORMAT = "d";

	private DateUtil() {
	}

	private static DateFormat getDateTimeFormat(final boolean setUTC) {
		final SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
		if (setUTC) {
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		return df;
	}

	public static Integer tzStringToInteger(final String offsetAsStr) {

		for (int i = 0; i < UTC_OFFSETS.length; i++) {
			if (UTC_OFFSETS[i].equals(offsetAsStr)) {
				return i - 12;
			}
		}
		return null;
	}

	public static String formatDate(final java.util.Date date) {
		if (date == null) {
			return null;
		}
		return DateUtil.getDateTimeFormat(true).format(date);
	}

	private static String getDayNumberSuffix(final int day) {
		if (day >= 11 && day <= 13) {
			return "th";
		}
		switch (day % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}

	public static String printableLocalizedTimePart(final long timestamp,
					final long utcOffset) {
		final DateFormat formatter = new SimpleDateFormat(PRESENTATION_TIME_PART_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		final java.util.Date shiftedDate = new java.util.Date(timestamp + 60 * 60 * 1000 * utcOffset);
		return formatter.format(shiftedDate);
	}

	public static String printableLocalizedDatePart(final long timestamp,
					final long utcOffset) {
		final DateFormat formatter = new SimpleDateFormat(PRESENTATION_DATE_PART_FORMAT);
		final DateFormat dayOnlyFormatter = new SimpleDateFormat(DAY_ONLY_PART_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		dayOnlyFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		final java.util.Date shiftedDate = new java.util.Date(timestamp + 60 * 60 * 1000 * utcOffset);
		return formatter.format(shiftedDate) + DateUtil.getDayNumberSuffix(Integer.parseInt(dayOnlyFormatter.format(shiftedDate)));
	}

	public static String printableLocalizedDate(final long timestamp,
					final long utcOffset) {
		final DateFormat formatter = new SimpleDateFormat(PRESENTATION_DATE_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		final java.util.Date shiftedDate = new java.util.Date(timestamp + 60 * 60 * 1000 * utcOffset);
		return formatter.format(shiftedDate);
	}

	public static String printableDateWithOffset(final Date date, final TimeZone tz) {
		final DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_WITH_OFFSET, Locale.US);
		formatter.setTimeZone(tz);
		return formatter.format(date);
	}

	public static java.util.Date parseDate(final String str) throws ParseException {

		if (str != null && str.length() > 3 && str.charAt(str.length() - 3) == ':') {
			final String woLast = StringUtil.subStrTo(str, str.length() - 3) + StringUtil.subStrFrom(str, str.length() - 2);
			return DateUtil.getDateTimeFormat(true).parse(woLast);
		}
		return DateUtil.getDateTimeFormat(true).parse(str);
	}
	public static ErrorType INVALID_DATE_PATTERN = new ErrorType("invalid_date_pattern", "Invalid date");

	public static ExecutionResult<java.util.Date> parseDateMMDDYYYY(final String str, final TimeZone tz, final String delimiter) {
		try {
			if (str == null) {
				return ExecutionResult.<java.util.Date>errorResult(INVALID_DATE_PATTERN);
			}
			final String patternStr = "MM" + delimiter + "dd" + delimiter + "yyyy";
			final DateFormat df = new SimpleDateFormat(patternStr);
			df.setTimeZone(tz);
			return ExecutionResult.<java.util.Date>successResult(df.parse(str.trim()));
		} catch (final ParseException e) {
			// Better to use regexp check.
			return ExecutionResult.<java.util.Date>errorResult(INVALID_DATE_PATTERN);
		}
	}

	/**
	 * Examples: January 1, 2013 January 21, 2013 February 18, 2013 March 29, 2013
	 * May 27, 2013 July 4, 2013 September 2, 2013 November 28, 2013 December 25,
	 * 2013
	 *
	 * @param str
	 * @param tz
	 * @return
	 */
	public static ExecutionResult<java.util.Date> parseLongDate(final String str, final TimeZone tz) {
		try {
			if (str == null) {
				return ExecutionResult.<java.util.Date>errorResult(INVALID_DATE_PATTERN);
			}
			final String patternStr = "MMMM d, yyyy";
			final DateFormat df = new SimpleDateFormat(patternStr);
			df.setTimeZone(tz);
			return ExecutionResult.<java.util.Date>successResult(df.parse(str.trim()));
		} catch (final ParseException e) {
			// Better to use regexp check.
			return ExecutionResult.<java.util.Date>errorResult(INVALID_DATE_PATTERN);
		}
	}

	public static boolean isInTheFuture(final long eventTime) {
		return System.currentTimeMillis() < eventTime;
	}

	public static boolean isInThePast(final Date eventDate) {
		return !DateUtil.isInTheFuture(eventDate);
	}

	public static boolean isInTheFuture(final Date eventDate) {
		return DateUtil.isInTheFuture(eventDate.getTime());
	}

	public static Date addDaysFromNow(final int daysToAdd) {
		return new Date(new Date().getTime() + daysToAdd * MS_PER_DAY);
	}

	public static Date addHoursFromNow(final int daysToAdd) {
		return new Date(new Date().getTime() + daysToAdd * MS_PER_HOUR);
	}

	public static Date addSecondsFromNow(final int secondsToAdd) {
		return new Date(new Date().getTime() + secondsToAdd * 1000); // TODO Does this work?  should mult by 1000?
	}

	public static double daysFromNow(final Date date) {
		return (date.getTime() - new Date().getTime()) * 1f / MS_PER_DAY;
	}

	private static long roundToUTCMidnightAsMS(final Date date) {
		final long nowAsMS = date.getTime();
		return nowAsMS - (nowAsMS % MS_PER_DAY);

	}

	public static Date lastMidnightPlusDays(final int dayOffSet) {
		final long midnightAsMS = DateUtil.roundToUTCMidnightAsMS(new Date());
		return new Date(midnightAsMS + dayOffSet * MS_PER_DAY);
	}

	public static Date lastMidnightPlus(final int daysOffSet, final int hourOffSet) {
		final Date addedToDate = DateUtil.addHoursFromNow(hourOffSet);
		return new Date(DateUtil.roundToUTCMidnightAsMS(addedToDate));
	}

	public static boolean isMidnightUTC(final Date date) {
		return date.getTime() % MS_PER_DAY == 0;
	}

	public static Date addMinutesToTS(final long eventTime, final int minuteOffset) {
		return new Date(eventTime + MS_PER_MINUTE * minuteOffset);
	}

	public static Date dateFromComponents(final int year,
					final int month,
					final int day,
					final int hour,
					final int minute,
					final TimeZone tz) {

		final Calendar cal = Calendar.getInstance();

		cal.setTimeZone(tz);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date addToNow(
					final int weeks,
					final int days,
					final int hours,
					final int minutes) {
		return DateUtil.addToDate(new Date(), weeks, days, hours, minutes);
	}

	public static Date addToDate(final Date date,
					final int weeks,
					final int days,
					final int hours,
					final int minutes) {
		return new Date(date.getTime()
						+ weeks * MS_PER_WEEK
						+ days * MS_PER_DAY
						+ hours * MS_PER_HOUR
						+ minutes * MS_PER_MINUTE);
	}

	public static Date addDaysWithTime(final Date date,
					final TimeZone tz,
					final int days,
					final int hour,
					final int minute) {
		final Date addedDays = DateUtil.addToDate(date, 0, days, 0, 0);

		final Calendar cal = Calendar.getInstance(tz);

		cal.setTime(addedDays);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);

		return cal.getTime();
	}

	public static Date nowSansTime(final TimeZone tz) {
		return DateUtil.removeTimePart(new Date(), tz);
	}

	public static Date removeTimePart(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTimeZone(tz);
		cal.setTime(new Date(date.getTime()));

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static int dayOfTheWeek(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int month(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.MONTH);
	}

	public static int dayOfMonth(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int year(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.YEAR);
	}

	public static int hour(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int minute(final Date date, final TimeZone tz) {
		final Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setTimeZone(tz);

		return cal.get(Calendar.MINUTE);
	}

	public static long daysFrom(final Date from, final Date to) {
		return (to.getTime() - from.getTime()) / MS_PER_DAY;
	}

	/**
	 * @deprecated Should be using TimeZone.inDaylightTime
	 * @param date
	 * @param tz
	 * @return
	 */
	public static boolean isAmericanDST(final Date date, final TimeZone tz) {


		// Starts on 2nd Sunday of March at 2AM
		// Ends on 1st Sunday in November at 2AM

		final Calendar cal = Calendar.getInstance();

		cal.setTimeZone(tz);
		cal.setTime(date);

		// TODO Get the proper regional time
//		return tz.inDaylightTime(date);
		int month = cal.get(Calendar.MONTH);
		int dotw = cal.get(Calendar.DAY_OF_WEEK);
		int hotd = cal.get(Calendar.HOUR_OF_DAY);
		int dayOfTheMonth = cal.get(Calendar.DAY_OF_MONTH);

		int dayOfNextSunday = 8 - dotw + dayOfTheMonth;

		if (month > Calendar.NOVEMBER || month < Calendar.MARCH) {
			return false;
		}

		if (month != Calendar.NOVEMBER && month != Calendar.MARCH) {
			return true;
		}

		// Else either nov or march
		if (month == Calendar.MARCH) {
			if (dayOfTheMonth < 8) {// Check indexing
				return false;
			}
			if (dayOfTheMonth > 14) {
				return true;
			}

			// In the 2nd week
			if (dotw != Calendar.SUNDAY && dayOfNextSunday > 14) {
				// Then 2nd Sunday has happend
				return true;
			} else if (dotw != Calendar.SUNDAY && dayOfNextSunday < 14) {
				// Hasn't happened yet
				return false;
			}

			// 2nd week on sunday
			return hotd >= 2;
		}

		if (month == Calendar.NOVEMBER) {

			if (dayOfTheMonth > 7) {
				return false;
			}
			// in 1st week
			// In the 2nd week
			if (dotw != Calendar.SUNDAY && dayOfNextSunday < 14 - 7) {
				return true;
			} else if (dotw != Calendar.SUNDAY && dayOfNextSunday > 14 - 7) {
				return false;
			}

		}
		return hotd < 2;

	}

	public static int hourDiffDSTFromNow(final Date toDate, final TimeZone tz) {
		boolean nowIsDST = DateUtil.isAmericanDST(new Date(), tz);
		boolean thenIsDST = DateUtil.isAmericanDST(toDate, tz);
		if (nowIsDST == thenIsDST) {
			return 0;
		}
		if (thenIsDST) { // And now isn't
			// remove an hour
			return -1;
		}
		return 1;
	}

	public static TimeZone offsetToTimeZone(final int utcOffset) {
		if (utcOffset > 0) {
			return TimeZone.getTimeZone("GMT+" + utcOffset);
		} else if (utcOffset < 0) {
			return TimeZone.getTimeZone("GMT" + utcOffset);
		} else {
			return TimeZone.getTimeZone("GMT");
		}
	}

	public static TimeZone nameToTimeZone(final String namedTimeZone) {
		return TimeZone.getTimeZone(namedTimeZone);
	}
}
