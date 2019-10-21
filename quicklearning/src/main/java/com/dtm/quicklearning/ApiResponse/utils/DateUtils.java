package com.dtm.quicklearning.ApiResponse.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtils {

    private final static DateTimeFormatter yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // dd-mm-yyyy
    private final static String REGEX_HORIZONTAL_SLASH_DDMMYYYY = "(0[1-9]|[12][0-9]|[3][01])-(0[1-9]|1[012])-\\d{4}";
    private final static SimpleDateFormat sdfHorizontalSlashDdMmYyyy = new SimpleDateFormat("dd-mm-yyyy", Locale.KOREA);
    // dd-mm-yy
    private final static String REGEX_HORIZONTAL_SLASH_DDMMYY = "(0[1-9]|[12][0-9]|[3][01])-(0[1-9]|1[012])-\\d{2}";
    private final static SimpleDateFormat sdfHorizontalSlashDdMmYy = new SimpleDateFormat("dd-mm-yy", Locale.KOREA);
    // mm-dd-yyyy
    private final static String REGEX_HORIZONTAL_SLASH_MMDDYYYY = "(0[1-9]|1[012])-(0[1-9]|[12][0-9]|[3][01])-\\d{4}";
    private final static SimpleDateFormat sdfHorizontalSlashMmDdYyyy = new SimpleDateFormat("mm-dd-yyyy", Locale.KOREA);
    // mm-dd-yy
    private final static String REGEX_HORIZONTAL_SLASH_MMDDYY = "(0[1-9]|1[012])-(0[1-9]|[12][0-9]|[3][01])-\\d{2}";
    private final static SimpleDateFormat sdfHorizontalSlashMmDdYy = new SimpleDateFormat("mm-dd-yy", Locale.KOREA);
    // yyyy-MM-dd
    private final static String REGEX_HORIZONTAL_SLASH_YYYYMMDD = "^((?:(?:1[6-9]|2[0-9])\\d{2})(-)(?:(?:(?:0?[13578]|1[02])(-)31)|((0?[1,3-9]|1[0-2])(-)(29|30))))$|^(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)0?2(-)29)$|^(?:(?:1[6-9]|2[0-9])\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))(-)(?:0?[1-9]|1\\d|2[0-8])$";
    private final static SimpleDateFormat sdfHorizontalSlashYyyyMmDd = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

    // dd/mm/yyyy
    private final static String REGEX_SLASH_DDMMYYYY = "(0[1-9]|[12][0-9]|[3][01])/(0[1-9]|1[012])/\\d{4}";
    private final static SimpleDateFormat sdfSlashDdMmYyyy = new SimpleDateFormat("dd/mm/yyyy", Locale.KOREA);
    // dd/mm/yy
    private final static String REGEX_SLASH_DDMMYY = "(0[1-9]|[12][0-9]|[3][01])/(0[1-9]|1[012])/\\d{2}";
    private final static SimpleDateFormat sdfSlashDdMmYy = new SimpleDateFormat("dd/mm/yy", Locale.KOREA);
    // mm/dd/yyyy
    private final static String REGEX_SLASH_MMDDYYYY = "(0[1-9]|1[012])/(0[1-9]|[12][0-9]|[3][01])/\\d{4}";
    private final static SimpleDateFormat sdfSlashMmDdYyyy = new SimpleDateFormat("mm/dd/yyyy", Locale.KOREA);
    // mm/dd/yy
    private final static String REGEX_SLASH_MMDDYY = "(0[1-9]|1[012])/(0[1-9]|[12][0-9]|[3][01])/\\d{2}";
    private final static SimpleDateFormat sdfSlashMmDdYy = new SimpleDateFormat("mm/dd/yy", Locale.KOREA);
    // yyyy/MM/dd
    private final static String REGEX_SLASH_YYYYMMDD = "\\d{4}/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|[3][01])";
    private final static SimpleDateFormat sdfSlashYyyyMmDd = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);

    public static Date now() {
        return new Date();
    }

    public static Date parseDate(String applyDt) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.parse(applyDt + "000000");
    }

    public static String formatToDateString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getHHmmss() {
        return new SimpleDateFormat("HHmmss").format(new Date());
    }

    public static String getDateYyyyMmddWithoutDash() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    public static String getDateYyyyMmWithoutDash() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        return simpleDateFormat.format(new Date());
    }

    public static String getTimeHHmmssWithoutDash() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static String yesterDay() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getIIACTranDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
        return simpleDateFormat.format(new Date());
    }

    public static String getPreviousDay() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(-1);

        return localDate.format(yyyyMMddFormatter);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }

    public static String getTodayLastTime() {
        return "235959";
    }

    private static Date parseToDateHorizontalSlashDdMmYyyy(String date) { // dd-mm-yyyy
        sdfHorizontalSlashDdMmYyyy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfHorizontalSlashDdMmYyyy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateHorizontalSlashDdMmYy(String date) { // dd-mm-yy
        sdfHorizontalSlashDdMmYy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfHorizontalSlashDdMmYy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateHorizontalSlashMmDdYyyy(String date) { // mm-dd-yyyy
        sdfHorizontalSlashMmDdYyyy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfHorizontalSlashMmDdYyyy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateHorizontalSlashMmDdYy(String date) { // mm-dd-yy
        sdfHorizontalSlashMmDdYy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfHorizontalSlashMmDdYy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateHorizontalSlashYyyyMMDd(String date) { // yyyy-MM-dd
        sdfHorizontalSlashYyyyMmDd.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfHorizontalSlashYyyyMmDd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Date parseToDateSlashDdMmYyyy(String date) { // dd/mm/yyyy
        sdfSlashDdMmYyyy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfSlashDdMmYyyy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateSlashDdMmYy(String date) { // dd/mm/yy
        sdfSlashDdMmYy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfSlashDdMmYy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateSlashMmDdYy(String date) { // mm/dd/yy
        sdfSlashMmDdYy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfSlashMmDdYy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateSlashMmDdYyyy(String date) { // mm/dd/yyyy
        sdfSlashMmDdYyyy.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfSlashMmDdYyyy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date parseToDateSlashYyyyMmDd(String date) { // yyyy/MM/dd
        sdfSlashYyyyMmDd.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfSlashYyyyMmDd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse String date to Date
     * Have return if parse fail.
     *
     * @param date String date time
     * @return Date
     * @author Nguyen Thanh Thai
     */
    public static Date parseToDate(String date) {
        if (date.matches(REGEX_HORIZONTAL_SLASH_DDMMYYYY)) { // dd-mm-yyyy
            return parseToDateHorizontalSlashDdMmYyyy(date);
        } else if (date.matches(REGEX_HORIZONTAL_SLASH_DDMMYY)) { // dd-mm-yy
            return parseToDateHorizontalSlashDdMmYy(date);
        } else if (date.matches(REGEX_HORIZONTAL_SLASH_MMDDYYYY)) { // mm-dd-yyyy
            return parseToDateHorizontalSlashMmDdYyyy(date);
        } else if (date.matches(REGEX_HORIZONTAL_SLASH_MMDDYY)) { // mm-dd-yy
            return parseToDateHorizontalSlashMmDdYy(date);
        } else if (date.matches(REGEX_HORIZONTAL_SLASH_YYYYMMDD)) { // yyyy-MM-dd
            return parseToDateHorizontalSlashYyyyMMDd(date);
        } else if (date.matches(REGEX_SLASH_DDMMYYYY)) { // dd/mm/yyyy
            return parseToDateSlashDdMmYyyy(date);
        } else if (date.matches(REGEX_SLASH_DDMMYY)) { // dd/mm/yy
            return parseToDateSlashDdMmYy(date);
        } else if (date.matches(REGEX_SLASH_MMDDYYYY)) { // mm/dd/yyyy
            return parseToDateSlashMmDdYyyy(date);
        } else if (date.matches(REGEX_SLASH_MMDDYY)) { // mm/dd/yy
            return parseToDateSlashMmDdYy(date);
        } else if (date.matches(REGEX_SLASH_YYYYMMDD)) { // yyyy/MM/dd
            return parseToDateSlashYyyyMmDd(date);
        }

        throw new UnknownFormatFlagsException("Format time current not define.");
    }


    public static Date releaseTimeZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date releaseTimeMaxOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * Get DateTime with value input.
     *
     * @param year
     * @param month      <code>{@link Calendar MONTH}</code>
     * @param dayOfMonth
     * @param hourOfDay
     * @param minute
     * @param second
     * @return dateTime
     * @author Nguyen Thanh Thai
     */
    public static Date getDateFrom(int year, int month, int dayOfMonth, int hourOfDay, int minute,
                                   int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth,
                hourOfDay, minute, second);
        return cal.getTime();
    }


    /**
     * Get date with zero time.
     *
     * @param year
     * @param month      <code>{@link Calendar MONTH}</code>
     * @param dayOfMonth
     * @return date with input value and time set to zero
     * @author Nguyen Thanh Thai
     */
    public static Date getDateZeroTimeFrom(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth,
                0, 0, 0);
        return cal.getTime();
    }


    /**
     * Get date with zero time.
     *
     * @param year
     * @param month <code>{@link Calendar MONTH}</code>
     * @return date with input value and time set to zero
     * @author Nguyen Thanh Thai
     */
    public static Date getDateEndOfMonthFrom(int year, int month) {
        Calendar cal = Calendar.getInstance();
        if (Calendar.DECEMBER == month) {
            month = Calendar.JANUARY;
            year++;
        } else {
            month += 1;
        }

        cal.set(year, month, 1,
                0, 0, 0);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }


}
