package tech.unwearable.magicsaltunwearable.services;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

/**
 * Created by budoc on 8/19/2017.
 */

public class NotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        sbn.getNotification();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Nothing to do
    }
}
