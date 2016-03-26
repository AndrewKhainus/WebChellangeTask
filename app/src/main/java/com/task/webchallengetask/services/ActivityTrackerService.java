package com.task.webchallengetask.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.IntentHelper;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.modules.activity.views.ActivityStartActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.task.webchallengetask.global.Constants.START_TIMER_ACTION;


/**
 * Created by andri on 22.03.2016.
 */
public class ActivityTrackerService extends Service {

    private Calendar mTimerTime;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private Timer mTimer;
    private String currentActivity;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        currentActivity = intent.getStringExtra(Constants.ACTIVITY_NAME_KEY);
        switch (intent.getAction()) {
            case START_TIMER_ACTION:
                if (mTimerTime == null) {
                    mTimerTime = TimeUtil.getCalendarFromString("00:00:00");
                }
                mTimer = new Timer();
                mTimer.schedule(new CounterRunnable(), 0, 1000);
                startForeground(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID,
                        getNotification(TimeUtil.getStringFromCalendar(mTimerTime)).build());
                break;
            case Constants.PAUSE_TIMER_ACTION:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                }
                break;
            case Constants.STOP_TIMER_ACTION:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                }
                mTimerTime = null;
                notificationManager.cancel(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID);
                stopForeground(true);
                stopSelf();
                break;
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class CounterRunnable extends TimerTask {

        @Override
        public void run() {
            mTimerTime = TimeUtil.addSecondToCalendar(mTimerTime);
            if (mBuilder != null) {
                mBuilder.setContentText(TimeUtil.getStringFromCalendar(mTimerTime));
                notificationManager.notify(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID, mBuilder.build());

            }
            sendBroadcast(IntentHelper.sendTimerUpdateIntent(mTimerTime));

        }

    }

    private NotificationCompat.Builder getNotification(String _message) {
        Intent notificationIntent = new Intent(this, ActivityStartActivity.class);
        notificationIntent.setAction(Constants.MAIN_ACTION);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(currentActivity)
                    .setContentText(_message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setColor(ContextCompat.getColor(this, R.color.red_color));
            }
        }
        return mBuilder;
    }

}
