
package tk.eggfly.toolkit;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class ProcessUtils {
    static void getRunningProcesses(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                Log.d(ToolkitApp.TAG, (list.get(i).processName + "\n"));
            }
        }
    }
}
