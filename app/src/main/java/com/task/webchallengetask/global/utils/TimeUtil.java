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
        return dateFormat.format(new Date(_time));
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

    public static Date parseDate(String _date) {
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

    public static boolean isSameDay(long _firstTime, long _secondTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(_firstTime));
        int firstDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(new Date(_secondTime));
        int secondDay = calendar.get(Calendar.DAY_OF_MONTH);
        return firstDay == secondDay;
    }
}
