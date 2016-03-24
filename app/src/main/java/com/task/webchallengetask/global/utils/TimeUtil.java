package com.task.webchallengetask.global.utils;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * Created by Sergbek on 17.11.2015.
 */
public final class TimeUtil {

    public static long getTimeBeginCurrentDay(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);

        return calendar.getTime().getTime() / 1000L;
    }

    public static long getTimeEndCurrentDay(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 59);

        return calendar.getTime().getTime() / 1000L;
    }

    public static long getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public static int getNextDay() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isFuture(long jobDay) {
        return jobDay > getTimeEndCurrentDay();
    }

    public static String getPickup_date(Integer _date) {
        if (_date != null) {
            Long intUnixTime = Long.valueOf(_date);
            return DateFormat.format("MM/dd", intUnixTime * 1000L).toString();
        }
        return "";
    }

    public static String getRequiredPickupDate(Integer _date) {
        if (_date != null) {
            Date date = new Date(_date*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
            return sdf.format(date);
        }
        return "";
    }

    public static String getFullDate(Integer _date) {
        if (_date != null) {
            return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date(Long.valueOf(_date) * 1000L));
        }
        return "";
    }

    public static String getFullDate(Date _date) {
        if (_date != null) {
            return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(_date);
        }
        return "";
    }

    public static String formatDateYMD(Date _date) {
        if (_date != null) {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(_date);
        }
        return "";
    }

    public static Date getTimeFromString(boolean withMinutes, String time) {
        SimpleDateFormat hasMinutesFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        SimpleDateFormat withoutMinutesFormat = new SimpleDateFormat("hh aa", Locale.US);
        time = time.trim();
        Date date;
        try {
            date = withMinutes ? hasMinutesFormat.parse(time) : withoutMinutesFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getFullDate(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 59);

        return calendar.getTime();
    }

    public static Date getGregorianTimeFromString(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        Date date;
        try {
            date = format.parse(time.trim());
            return date;
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar getCalendarFromString(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(time.trim()));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getStringFromTime(boolean withMinutes, Date date) {
        if (date != null) {
            SimpleDateFormat hasMinutesFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            SimpleDateFormat withoutMinutesFormat = new SimpleDateFormat("hh aa", Locale.US);
            return withMinutes ? hasMinutesFormat.format(date) : withoutMinutesFormat.format(date);
        } else {
            return "";
        }
    }

    public static String getFormattedTime(boolean withMinutes, String time) {
        if (!TextUtils.isEmpty(time)) {
            return getStringFromTime(withMinutes, getTimeFromString(withMinutes, time));
        } else {
            return "";
        }
    }

    public static String getFormattedGregorianTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            return getStringFromGregorianTime(getGregorianTimeFromString(time));
        } else {
            return "";
        }
    }


    public static String getStringFromGregorianTime(Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            return format.format(date);
        } else {
            return "";
        }
    }

    public static String getStringFromCalendar(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            return format.format(calendar.getTime());
        } else {
            return "";
        }

    }

    public static String addHourToTime(boolean withMinutes, String time, int addHour) {
        if (!TextUtils.isEmpty(time)) {
            Date date = getTimeFromString(withMinutes, time);
            assert date != null;
            Date newDate = new Date(date.getTime() + TimeUnit.HOURS.toMillis(addHour));
            return getStringFromTime(withMinutes, newDate);
        } else {
            return "";
        }
    }

    public static Date addSecondToTime(Date date) {
        return new Date(date.getTime() + TimeUnit.SECONDS.toMillis(1));
    }

    public static Calendar addSecondToCalendar(Calendar calendar) {
        calendar.add(Calendar.SECOND, 1);
        return calendar;
    }
}
