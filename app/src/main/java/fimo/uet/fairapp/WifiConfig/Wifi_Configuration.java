package fimo.uet.fairapp.WifiConfig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by HP on 5/1/2017.
 */

public class Wifi_Configuration {
    Context context;
    String SSID;
    String PassWord;

    public Wifi_Configuration(Context context, String SSID, String PassWord){
        this.context = context;
        this.SSID = SSID;
        this.PassWord = PassWord;
    }
    public void connectWifi(){
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = formatSSID(wifiInfo.getSSID());

        for(;;){
            if(!ssid.equals(SSID)){
                wifiManager.disconnect();
                try {
                    String s= SSID;
                    createAPConfiguration(s,PassWord,"WPA");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                while(!wifi.isConnected()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                    wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                }
                wifiInfo = wifiManager.getConnectionInfo();
                ssid = formatSSID(wifiInfo.getSSID());
                if(ssid.equals(SSID)){
                    break;
                }

            }else if(ssid.equals(SSID)){
                break;
            }
        }
    }

    private WifiConfiguration createAPConfiguration(String networkSSID, String networkPasskey, String securityMode) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
        WifiConfiguration  wifiConfiguration = new WifiConfiguration();

        wifiConfiguration.SSID = "\"" + networkSSID + "\"";

        if (securityMode.equalsIgnoreCase("OPEN")) {

            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        } else if (securityMode.equalsIgnoreCase("WEP")) {

            wifiConfiguration.wepKeys[0] = "\"" + networkPasskey + "\"";
            wifiConfiguration.wepTxKeyIndex = 0;
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        } else if (securityMode.equalsIgnoreCase("PSK")) {

            wifiConfiguration.preSharedKey = "\"" + networkPasskey + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        } else if(securityMode.equalsIgnoreCase("WPA2")||securityMode.equalsIgnoreCase("WPA"))
        {

            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.preSharedKey = "\"" + networkPasskey + "\"";
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            wifiManager.setWifiEnabled(true);
            wifiManager.saveConfiguration();
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();

                    break;
                }
            }
            wifiManager.addNetwork(wifiConfiguration);



        } else{


            return null;
        }

        return wifiConfiguration;

    }

    public String formatSSID(String ssid){
        String s="";
        char[]chars = ssid.toCharArray();
        for (int i=0;i<chars.length;i++){
            if(chars[i]!='"'){
                s+=chars[i];
            }
        }
        return s;
    }
}
