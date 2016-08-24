package com.test.appa.register;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.test.appa.R;

public class NotificationUtil {
    private static final int WAIT_REGISTER = 100000;
    private static final int COMPLETE_REGISTER = 100001;

    public static void showWaitRegister(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.wait_reigster_des))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setAutoCancel(false)
                .setOngoing(true);

        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(WAIT_REGISTER, builder.build());
    }

    public static void closeWaitRegister(Context context) {
        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(WAIT_REGISTER);
    }

    public static void showCompleteRegister(Context context) {
        closeWaitRegister(context);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true);

        Notification.BigTextStyle style = new Notification.BigTextStyle(builder);
        style.setBigContentTitle(context.getString(R.string.app_name));
        style.bigText(context.getString(R.string.complete_reigster_des));
        builder.setStyle(style);

        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(COMPLETE_REGISTER, builder.build());
    }

    public static void closeCompleteRegister(Context context) {
        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(COMPLETE_REGISTER);
    }

}
