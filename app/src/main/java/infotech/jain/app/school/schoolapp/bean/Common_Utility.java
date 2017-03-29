package infotech.jain.app.school.schoolapp.bean;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by admin on 30/06/16.
 */
public class Common_Utility {


    public String getWifiMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return  macAddress;
    }
}
