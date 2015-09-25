
package tk.eggfly.toolkit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class Utils {
    static final int ROOT_MANAGEMENT_REQUEST_CODE = 0xff;

    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            return true;
        }
        else {
            return false;
        }
    }

    static void disableAllSyncs(Context context) {
        // Account[] accounts = AccountManager.get(this).getAccounts();
        SyncAdapterType[] syncs = ContentResolver.getSyncAdapterTypes();

        for (SyncAdapterType sync : syncs) {
            Log.d(ToolkitApp.TAG, "" + sync);
            if (sync.isUserVisible()) {
                Account account = Utils.findAccountByType(context, sync.accountType);
                if (account != null) {
                    ContentResolver.setSyncAutomatically(account, sync.authority, false);
                    Log.d(ToolkitApp.TAG, "disabled sync: " + sync);
                }
            }
            // ContentResolver.setSyncAutomatically(account, authority, false);
        }
    }

    static void registerSyncAdapter(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.xiaomi");
        if (accounts != null && accounts.length > 0) {
            Account account = accounts[0];
            ContentResolver.setMasterSyncAutomatically(true);
            ContentResolver.setSyncAutomatically(account, ToolkitApp.AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, ToolkitApp.AUTHORITY, new Bundle(), 60);
        }
    }

    static void getRunningProcesses(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                RunningAppProcessInfo info = list.get(i);
                Log.d(ToolkitApp.TAG, info.processName + ", " + TextUtils.join(" ", info.pkgList));
            }
        }
    }

    public interface Parser {
        void parse(String line);
    }

    public static void runRootCommandsAndParse(String command, Parser parser) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));) {
            writer.write(command + "\n");
            writer.flush();
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (parser != null) {
                    parser.parse(line);
                }
            }
        }
    }

    static String runRootCommands(String command) {
        Process rootProcess;
        try {
            rootProcess = Runtime.getRuntime().exec("su");
            BufferedReader reader = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(rootProcess.getOutputStream()));
            writer.write(command + "\n");
            writer.write("exit\n");
            writer.flush();
            String line = null;
            StringBuilder data = new StringBuilder();
            StringBuilder errData = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                data.append(line + System.getProperty("line.separator"));
            }
            while ((line = errReader.readLine()) != null) {
                errData.append(line + System.getProperty("line.separator"));
            }
            Log.d(ToolkitApp.TAG, data.toString() + "\n" + errData.toString());
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Account findAccountByType(Context context, String accountType) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(accountType);
        if (accounts != null && accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    static void requireRoot(Activity activity) {
        try {
            Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
