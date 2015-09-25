
package tk.eggfly.toolkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.os.Bundle;
import android.widget.TextView;

public class SyncStatusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_status);
        initCurrentSync();
        initSyncState();
        initDumpContentService();
    }

    private void initCurrentSync() {
        TextView current = (TextView) findViewById(R.id.current_sync_text);
        StringBuilder sb = new StringBuilder();
        List<SyncInfo> syncs = ContentResolver.getCurrentSyncs();
        for (SyncInfo sync : syncs) {
            sb.append(sync.toString() + "\n");
        }
        final String text = sb.toString();
        current.setText("".equals(text) ? "no current syncs" : text);
    }

    private void initSyncState() {
        TextView status = (TextView) findViewById(R.id.sync_status_text);
        SyncAdapterType[] allSyncTypes = ContentResolver.getSyncAdapterTypes();
        StringBuilder sb = new StringBuilder();
        for (SyncAdapterType adapter : allSyncTypes) {
            Account[] accounts = AccountManager.get(this).getAccountsByType(adapter.accountType);
            for (Account account : accounts) {
                boolean isActive = ContentResolver.isSyncActive(account, adapter.authority);
                boolean isPending = ContentResolver.isSyncActive(account, adapter.authority);
                sb.append(adapter + "with account=" + account + ": isActive=" + isActive + ", isPending=" + isPending + "\n");
            }
        }
        status.setText(sb.toString());
    }

    private void initDumpContentService() {
        TextView text = (TextView) findViewById(R.id.sync_text);
        try {
            final String content = dump();
            text.setText(content);
        } catch (Exception e) {
            text.setText("IOException: " + e);
        }
    }

    private static String dump() {
        return Utils.runRootCommands("dumpsys content");
    }

    public String dumpContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        Process process = Runtime.getRuntime().exec("dumpsys content");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }
}
