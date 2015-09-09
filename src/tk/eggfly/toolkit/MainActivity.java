
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
        // try {
        // PackageInfo info = getPackageManager().getPackageInfo("com.android.settings",
        // PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        // } catch (NameNotFoundException e) {
        // e.printStackTrace();
        // }
    }

    private void initViews() {
        findViewById(R.id.view_test_button).setOnClickListener(this);
        findViewById(R.id.view_statistics_button).setOnClickListener(this);
        findViewById(R.id.view_auto_start_button).setOnClickListener(this);
        findViewById(R.id.view_app_info_button).setOnClickListener(this);
        findViewById(R.id.view_app_permission_button).setOnClickListener(this);
        findViewById(R.id.view_settings_button).setOnClickListener(this);
        findViewById(R.id.view_manage_all_applications_button).setOnClickListener(this);
        findViewById(R.id.view_manage_applications_button).setOnClickListener(this);
        findViewById(R.id.view_dev_settings_button).setOnClickListener(this);
        findViewById(R.id.require_root_button).setOnClickListener(this);
        ViewGroup buttons = (ViewGroup) findViewById(R.id.buttons);
        for (int i = 0; i < buttons.getChildCount(); i++) {
            View view = buttons.getChildAt(i);
            if (UIUtils.getOnClickListener(view) == null) {
                view.setEnabled(false);
            }
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
            case R.id.view_test_button:
                startService(new Intent(this, MonitorService.class));
                // Utils.runRootCommands("pm disable com.xiaomi.channel");
                // Utils.runRootCommands("pm list packages -d");
                // Utils.runRootCommands("pm enable com.xiaomi.channel");
                break;
            case R.id.view_statistics_button:
                MonitorService.printStatistics(this);
                // Utils.runRootCommands("pm disable com.xiaomi.channel");
                // Utils.runRootCommands("pm list packages -d");
                // Utils.runRootCommands("pm enable com.xiaomi.channel");
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
            case R.id.view_auto_start_button:
                SecurityCenter.showAutoStartManagementPage(this);
                break;
            case R.id.require_root_button:
                SecurityCenter.showRootManagementActivity(this);
                break;
        }
    }
}
