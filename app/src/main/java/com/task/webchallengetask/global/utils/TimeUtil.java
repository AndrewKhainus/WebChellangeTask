package com.task.webchallengetask.global.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by Sergbek on 17.11.2015.
 */
public final class TimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private static final SimpleDateFormat dateFormatDDMM = new SimpleDateFormat("dd/MM", Locale.US);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);

    public static long getCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }



    public static String timeToString(long _time) {
        return timeFormat.format(new Date(_time));
    }

    public static String timeToStringDDMM(long _time) {
        return dateFormatDDMM.format(new Date(_time));
    }

    public static String dateToString(Date _date) {
        if (_date != null) {
            return dateFormat.format(_date);
        }
        return "";
    }

    public static Date stringToDate(String _date) {
        try {
            return dateFormat.parse(_date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseTime(String _date) {
        try {
            return timeFormat.parse(_date);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Calendar getCalendarFromString(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(timeFormat.parse(time.trim()));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromGregorianTime(Date date) {
        if (date != null) {
            return timeFormat.format(date);
        } else {
            return "";
        }
    }

    public static String getStringFromCalendar(Calendar calendar) {
        if (calendar != null) {
            return timeFormat.format(calendar.getTime());
        } else {
            return "";
        }

    }

    public static long sumDates(long _firstDate, long _secondDate) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(_firstDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(_secondDate);
        Calendar cTotal = (Calendar) c1.clone();
        cTotal.add(Calendar.YEAR, c2.get(Calendar.YEAR));
        cTotal.add(Calendar.MONTH, c2.get(Calendar.MONTH) + 1); // Months are zero-based!
        cTotal.add(Calendar.DATE, c2.get(Calendar.DATE));
        cTotal.add(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
        cTotal.add(Calendar.MINUTE, c2.get(Calendar.MINUTE));
        cTotal.add(Calendar.SECOND, c2.get(Calendar.SECOND));
        cTotal.add(Calendar.MILLISECOND, c2.get(Calendar.MILLISECOND));
        return cTotal.getTimeInMillis();
    }

    public static long getDifferenceByDay(Date _startDate, Date _endDate) {
        long startTime = _startDate.getTime();
        long endTime = _endDate.getTime();
        long diffTime = endTime - startTime;
        return diffTime / (1000 * 60 * 60 * 24);
    }

    public static Date addDayToDate(Date date, int _days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, _days);
        return new Date(c.getTime().getTime());
    }

    public static Date addEndOfDay(Date date, int _days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return new Date(c.getTime().getTime());
    }

    public static Date minusDayFromDate(Date date, int _days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -_days);
        return new Date(c.getTime().getTime());
    }

    public static Calendar addSecondToCalendar(Calendar calendar) {
        calendar.add(Calendar.SECOND, 1);
        return calendar;
    }

    public static int compareDay(long _firstTime, long _secondTime) {
        Calendar firstCal = Calendar.getInstance();
        Calendar secondCal = Calendar.getInstance();
        firstCal.setTimeInMillis(_firstTime);
        secondCal.setTimeInMillis(_secondTime);
        clearTime(firstCal);
        clearTime(secondCal);
        return firstCal.compareTo(secondCal);
    }

    public static boolean isSameDay(long _firstTime, long _secondTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(_firstTime));
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long timeFirst = calendar.getTimeInMillis() / 1000L;
        calendar.setTime(new Date(_secondTime));
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long timeSecond = calendar.getTimeInMillis() / 1000L;

        return timeFirst == timeSecond;
    }

    public static void clearTime(Calendar _calendar) {
        _calendar.set(Calendar.HOUR_OF_DAY, 0);
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void clearDate(Calendar _calendar) {
        _calendar.set(Calendar.YEAR, 0);
        _calendar.set(Calendar.MONTH, 0);
        _calendar.set(Calendar.DAY_OF_MONTH, 0);
    }

    public static void clearDate(long _time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(_time);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
    }

    public static int getTimeInSeconds(long _time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(_time);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        return hours * 60 * 60 + minutes * 60 + seconds;
    }

}
