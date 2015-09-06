
package tk.eggfly.toolkit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.util.Log;

public class SyncUtils {

    static void disableAllSyncs(Context context) {
        // Account[] accounts = AccountManager.get(this).getAccounts();
        SyncAdapterType[] syncs = ContentResolver.getSyncAdapterTypes();

        for (SyncAdapterType sync : syncs) {
            Log.d(ToolkitApp.TAG, "" + sync);
            if (sync.isUserVisible()) {
                Account account = MainActivity.findAccountByType(context, sync.accountType);
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

}
