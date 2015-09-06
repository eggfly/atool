
package tk.eggfly.toolkit;

import tk.eggfly.toolkit.R;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    public static final String TAG = "eggfly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SyncUtils.disableAllSyncs(this);
        // ProcessUtils.getRunningProcesses(this);
    }

    static Account findAccountByType(Context context, String accountType) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(accountType);
        if (accounts != null && accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
