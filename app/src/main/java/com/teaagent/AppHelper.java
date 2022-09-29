package com.teaagent;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import org.apache.commons.lang3.StringUtils;

import util.NetworkUtil;

public class AppHelper {
    private static final String PREFERENCE_TABLET_ID = "TABLET_ID";
    private static final String ACTIVE_EMPLOYEE_ID = "ACTIVE_EMPLOYEE_ID";
    private static final String EMPTY = "EMPTY";
    static AppHelper instance = null;
    Context context = TeaAgentApplication.Companion.getContext();
    String KEY_EMPTY = "";

    public static synchronized AppHelper getInstance() {
        if (null == instance) {
            instance = new AppHelper();
        }
        return instance;
    }

    /**
     * @return the tablet ID
     */


    public String getUniqueUserID() {

        String getTabletId = Settings.Secure.getString(
                TeaAgentApplication.Companion.getContext().getContentResolver(),
        Settings.Secure.ANDROID_ID

                );

        return getTabletId;
    } /*{
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tabletId = preferences.getString(PREFERENCE_TABLET_ID, EMPTY).toUpperCase(Locale.ROOT);
        Log.i("TAG","before tabletId "+tabletId);

        if (tabletId.equals(EMPTY) || tabletId.isEmpty()) {

            //Get device Id type from resources
            //Since we need device Id for initial DB set up we can't keep this in profile.
            String deviceIdTypeLabel = context.getResources().getString(R.string.device_id_type);
            DeviceIdType deviceIdType = DeviceIdType.valueOf(deviceIdTypeLabel);

            if (deviceIdType == DeviceIdType.DEFAULT) {
                tabletId = getMacAddress();
            } else if (deviceIdType == DeviceIdType.PHONE_NUMBER) {
                //Check for the permission needed
                //Change with new permission after Android 13
                if (ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    String phoneString = PhoneNumberUtil.getPhoneNumber(context);
                    if (null == phoneString) {
                        tabletId = KEY_EMPTY;
                    } else {
                        tabletId = phoneString;
                    }
                } else {
                    tabletId = KEY_EMPTY;
                }
            }

            setTabletIdInPreference(tabletId);
        }
        Log.i("TAG","tabletId "+tabletId);

        return tabletId;
    }*/

    public String getMacAddress() {
        String tabletId;
        tabletId = NetworkUtil.fetchMACAddress();
        if (tabletId.isEmpty()) {
            tabletId = getMacAddressFromWifiManager();
        }
        return tabletId;
    }

    private String getMacAddressFromWifiManager() {
        String macAddress = null;
        // We are checking if device is running Lollipop or lower version because from Marshmallow, the WifiInfo API
        // returns a constant MAC address 02:00:00:00:00:00 for security reasons. Some features like in-flight sync
        // are dependant on this to uniquely identify peers.
        // https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "MAC_ADDRESS_NOT_FOUND";
            }
            macAddress = StringUtils.remove(wInfo.getMacAddress(), ':');
        }
        return macAddress;
    }

    private void setTabletIdInPreference(String tabletId) {
        if (null != tabletId && !tabletId.isEmpty()) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putString(PREFERENCE_TABLET_ID, tabletId).apply();
        }
    }
}
