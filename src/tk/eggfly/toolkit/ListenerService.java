
package tk.eggfly.toolkit;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class ListenerService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        Bundle extras = notification.getNotification().extras;
        Bitmap bitmap = (Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
    }
}
