
package tk.eggfly.toolkit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class Navigator {
    static class SecurityCenter {
        /**
         * 打开安全中心的自启动管理页
         */
        static void showAutoStartManagementPage(Context context) {
            Intent intent = new Intent();
            intent.setAction("miui.intent.action.OP_AUTO_START");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        static void showRootManagementActivity(Activity activity) {
            Intent intent = new Intent("miui.intent.action.ROOT_MANAGER");
            intent.setPackage("com.miui.securitycenter");
            activity.startActivityForResult(intent, Utils.ROOT_MANAGEMENT_REQUEST_CODE);
        }
    }

    static class AndroidSettings {
        public static void showDevSettings(Context context) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            context.startActivity(intent);
        }

        public static void showSettings(Context context) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }

        public static void showAppSettings(Context context) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
            context.startActivity(intent);
        }

        public static void showManageAllApps(Context context) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }

        public static void showManageApps(Context context) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }

        public static void showTest(Context context) {
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.applications.StorageUse");
            context.startActivity(intent);
        }
    }

    static class AppDetail {
        private static final String SCHEME = "package";
        /**
         * 調用系統InstalledAppDetails界面所需的Extra名稱(用於Android 2.1及之前版本)
         */
        private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
        /**
         * 調用系統InstalledAppDetails界面所需的Extra名稱(用於Android 2.2)
         */
        private static final String APP_PKG_NAME_22 = "pkg";
        /**
         * InstalledAppDetails所在包名
         */
        private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
        /**
         * InstalledAppDetails類名
         */
        private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

        /**
         * 調用系統InstalledAppDetails界面顯示已安裝應用程序的詳細信息。 對於Android 2.3（API Level
         * 9）以上，使用SDK提供的接口； 2.3以下，使用非公開的接口（查看InstalledAppDetails源碼）。
         * 
         * @param context
         * @param packageName
         *            應用程序的包名
         */
        public static void showInstalledAppDetails(Context context, String packageName) {
            Intent intent = new Intent();
            final int apiLevel = Build.VERSION.SDK_INT;
            if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts(SCHEME, packageName, null);
                intent.setData(uri);
            } else { // 2.3以下，使用非公開的接口（查看InstalledAppDetails源碼）
                // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
                final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                        : APP_PKG_NAME_21);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                        APP_DETAILS_CLASS_NAME);
                intent.putExtra(appPkgName, packageName);
            }
            context.startActivity(intent);
        }

        /**
         * MIUI: 应用权限设置页
         */
        public static void showAppPermissionEditActivity(Context context, String packageName) {
            boolean isMiui = Utils.isMIUI();
            if (isMiui) {
                // 之兼容miui v5/v6 的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
                try {
                    Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    localIntent.putExtra("extra_pkgname", packageName);
                    context.startActivity(localIntent);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", packageName, null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            }
        }
    }
}
