
package tk.eggfly.toolkit;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import tk.eggfly.toolkit.R;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
        Context context,
        boolean autoInitialize,
        boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        /*
         * Put the data transfer code here.
         */
        Log.d(ToolkitApp.TAG, "onPerformSync");
        Context context = getContext();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setWhen(0);
        builder.setContentTitle("张照片");
        builder.setContentText("马上查看");
        builder.setAutoCancel(true);
        builder.setLights(Color.MAGENTA, 500, 500);
        // long[] pattern = { 500, 500, 500, 500, 500, 500, 500, 500, 500 };
        // builder.setVibrate(pattern);
        builder.setOngoing(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        // Intent incomingShareIntent = new Intent(context, FaceShareActivity.class);
        // incomingShareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // incomingShareIntent.putExtra(FaceShareActivity.EXTRA_SHOW_INCOMING_TAB, true);
        // PendingIntent pi = PendingIntent.getActivity(context,
        // Long.valueOf(record.getShareId()).hashCode(),
        // incomingShareIntent,
        // PendingIntent.FLAG_UPDATE_CURRENT);
        // builder.setContentIntent(pi);
        nm.notify(builder.hashCode(), builder.build());
    }
}
