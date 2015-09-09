
package tk.eggfly.toolkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.eggfly.toolkit.Utils.Parser;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class MonitorService extends IntentService {
    public static class ProcInfo implements Comparable<ProcInfo> {
        int noneActivityLaunchCount;
        String proc;
        List<LaunchInfo> launchInfos = new ArrayList<LaunchInfo>();

        @Override
        public int compareTo(ProcInfo another) {
            return another.noneActivityLaunchCount - this.noneActivityLaunchCount;
        }

        @Override
        public String toString() {
            return proc + ": " + noneActivityLaunchCount;
        }
    }

    public static class LaunchInfo {
        String proc, type, component, pid, uid;
    }

    private static Map<String, ProcInfo> sInfo = new HashMap<String, ProcInfo>();

    public MonitorService() {
        super("MonitorService");
    }

    private Parser mLogcatParser = new Parser() {
        @Override
        public void parse(String line) {
            // Start proc com.xiaomi.gallery for service com.xiaomi.gallery/.cloud.GallerySyncService: pid=15251
            // uid=10109 gids={50109, 3003, 1028, 1015}
            Pattern p = Pattern.compile("Start proc (.+) for (service|activity|broadcast|content provider) (.+): pid=(\\d+) uid=(\\d+) .+");
            Matcher m = p.matcher(line);
            if (m.find()) {
                final String proc = m.group(1);
                final String type = m.group(2);
                final String component = m.group(3);
                final String pid = m.group(4);
                final String uid = m.group(5);
                LaunchInfo launchInfo = new LaunchInfo();
                launchInfo.proc = proc;
                launchInfo.type = type;
                launchInfo.component = component;
                launchInfo.pid = pid;
                launchInfo.uid = uid;
                Log.d(ToolkitApp.TAG, TextUtils.join(",", new String[] { proc, type, component, pid, uid }));
                ProcInfo info = sInfo.get(proc);
                if (info == null) {
                    info = new ProcInfo();
                    info.noneActivityLaunchCount = 0;
                    info.proc = proc;
                    sInfo.put(proc, info);
                }
                if (!"activity".equals(type)) {
                    info.noneActivityLaunchCount++;
                    info.launchInfos.add(launchInfo);
                }
                Log.d(ToolkitApp.TAG, m.group());
            }
        }
    };

    protected void onHandleIntent(Intent intent) {
        try {
            Utils.runRootCommandsAndParse("logcat -s ActivityManager", mLogcatParser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postRun() {
        new Handler().postDelayed(mGetRunningProcessRunnable, 1000);
    }

    private Runnable mGetRunningProcessRunnable = new Runnable() {
        @Override
        public void run() {
            Utils.getRunningProcesses(MonitorService.this);
            postRun();
        }
    };

    public static void printStatistics(Context context) {
        List<ProcInfo> infoList = new ArrayList<ProcInfo>(sInfo.values());
        Collections.sort(infoList);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("statistics");
        CharSequence[] items = new CharSequence[infoList.size()];
        for (int i = 0; i < infoList.size(); i++) {
            items[i] = infoList.get(i).toString();
        }
        builder.setItems(items, null);
        builder.show();
    }
}
