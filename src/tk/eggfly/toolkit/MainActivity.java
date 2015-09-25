
package tk.eggfly.toolkit;

import tk.eggfly.toolkit.Navigator.AndroidSettings;
import tk.eggfly.toolkit.Navigator.AppDetail;
import tk.eggfly.toolkit.Navigator.SecurityCenter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    public static final String TAG = "eggfly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        ViewGroup buttons = (ViewGroup) findViewById(R.id.buttons);
        for (int i = 0; i < buttons.getChildCount(); i++) {
            View view = buttons.getChildAt(i);
            view.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.ROOT_MANAGEMENT_REQUEST_CODE) {
            Toast.makeText(this, "requiring root..", Toast.LENGTH_LONG).show();
            Utils.requireRoot(this);
            Toast.makeText(this, "requiring root end..", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_sync:
                startActivity(new Intent(this, SyncStatusActivity.class));
                break;
            case R.id.view_start_monitor_button:
                startService(new Intent(this, MonitorService.class));
                break;
            case R.id.view_statistics_button:
                MonitorService.printDetails(this);
                break;
            case R.id.view_app_info_button:
                AppDetail.showInstalledAppDetails(this, this.getPackageName());
                break;
            case R.id.view_app_permission_button:
                AppDetail.showAppPermissionEditActivity(this, this.getPackageName());
                break;
            case R.id.view_settings_button:
                AndroidSettings.showSettings(this);
                break;
            case R.id.view_manage_all_applications_button:
                AndroidSettings.showManageAllApps(this);
                break;
            case R.id.view_manage_applications_button:
                AndroidSettings.showManageApps(this);
                break;
            case R.id.view_dev_settings_button:
                AndroidSettings.showDevSettings(this);
                break;
            case R.id.view_notification_listener_settings_button:
                AndroidSettings.showNotificationListenerSettings(this);
                break;
            case R.id.view_auto_start_button:
                SecurityCenter.showAutoStartManagementPage(this);
                break;
            case R.id.require_root_button:
                SecurityCenter.showRootManagementActivity(this);
                break;
        }
    }
}
