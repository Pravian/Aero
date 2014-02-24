package net.pravian.bukkitlib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents all Time-related utilities.
 */
public class TimeUtils {

    /**
     * The storage format for parsing dates.
     */
    public static final String DATE_STORAGE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
    private static final Map<String, Integer> times = new HashMap<String, Integer>();

    static {
        times.put("dawn", 22000);
        times.put("sunrise", 23000);
        times.put("morning", 24000);
        times.put("day", 24000);
        times.put("midday", 28000);
        times.put("noon", 28000);
        times.put("afternoon", 30000);
        times.put("evening", 32000);
        times.put("sunset", 37000);
        times.put("dusk", 37500);
        times.put("night", 38000);
        times.put("midnight", 16000);
    }

    private TimeUtils() {
    }

    /**
     * Returns the tick value based on a name.
     *
     * <p>Returns -1 if no time format was detected</p>
     *
     * <p>Author: bergerkiller</p>
     *
     * @param timeName The time name to parse.
     */
    public static long getTime(String timeName) {
        try {
            String[] bits = timeName.split(":");
            if (bits.length == 2) {
                long hours = 1000 * (Long.parseLong(bits[0]) - 8);
                long minutes = 1000 * Long.parseLong(bits[1]) / 60;
                return hours + minutes;
            } else {
                return (long) ((Double.parseDouble(timeName) - 8) * 1000);
            }
        } catch (Exception ex) {
            // Or some shortcuts
            Integer time = times.get(timeName == null ? null : timeName.toLowerCase());
            if (time != null) {
                return time.longValue();
            }
        }
        return -1;
    }

    /**
     * Parses a date offset from a string.
     *
     * <p>Examples:
     * <pre>
     * parseDateOffset("2y5mo1w3d5h4m3s"); // 2 Years, 5 Months, 1 Week, etc.
     * parseDateOffset("never"); // null
     * parseDateOffset("2d4h"); // 2 Days, 4 Hours.
     *
     * @param time
     * @return The Date at at the parsed offset / null.
     */
    public static Date parseDateOffset(String time) {

        if (time.trim().equalsIgnoreCase("never")) {
            return null;
        }

        Pattern timePattern = Pattern.compile(
                "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found) {
            return null;
        }

        Calendar c = new GregorianCalendar();

        if (years > 0) {
            c.add(Calendar.YEAR, years);
        }
        if (months > 0) {
            c.add(Calendar.MONTH, months);
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds);
        }

        return c.getTime();
    }

    /**
     * Parses a Date to a string using {@link #DATE_STORAGE_FORMAT}.
     *
     * <p>The default storage format is config-friendly, so it may be used in YamlConfig.</p>
     * <p>Parsing <i>null</i> returns "never".</p>
     *
     * @param date The Date to parse.
     * @return The parsed String.
     * @see #parseString(String)
     */
    public static String parseDate(Date date) {
        if (date == null) {
            return "never";
        }
        return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).format(date);
    }

    /**
     * Parses a Date to a string using {@link #DATE_STORAGE_FORMAT}.
     *
     * <p>The default storage format is config-friendly, so it may be used in YamlConfig.</p>
     * <p>Parsing <i>never</i> returns null.</p>
     *
     * @param date The String to parse to a date.
     * @return The parsed Date.
     * @see #parseDate(Date)
     */
    public static Date parseString(String date) {
        if (date == null || date.isEmpty() || date.trim().equalsIgnoreCase("never")) {
            return null;
        }
        try {
            return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            return new Date(0L);
        }
    }
}
