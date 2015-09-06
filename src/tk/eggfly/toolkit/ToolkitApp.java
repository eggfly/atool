
package tk.eggfly.toolkit;

import android.app.Application;
import android.content.Intent;

public class ToolkitApp extends Application {

    public static final String TAG = "eggfly";
    public static final String AUTHORITY = "toolkit_background";

    @Override
    public void onCreate() {
        super.onCreate();
        SyncUtils.registerSyncAdapter(this);
        startService(new Intent(this, MonitorService.class));
    }
}
