package com.example.thanhhang.mnsfimo.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by HP on 3/17/2017.
 */

public class ConnectNode extends AppCompatActivity {
    TextView NameNode;
    EditText PassWord;
    String SecurityMode;
    static String SSID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_node);
        NameNode = (TextView)findViewById(R.id.wifi_name);
        PassWord = (EditText)findViewById(R.id.pass_word);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if (bundle != null) {
            SSID= bundle.getString("NameNode");
            NameNode.setText(SSID);
            SecurityMode = bundle.getString("SecurityMode");
        }
        PassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Toast.makeText(ConnectNode.this,PassWord.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = formatSSID(wifiInfo.getSSID());

                for(;;){
                    if(!ssid.equals(SSID)){
                        wifiManager.disconnect();
                        try {
                            createAPConfiguration(NameNode.getText().toString(),PassWord.getText().toString(),"WPA");
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        while(!wifi.isConnected()){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                            wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        }
//                        try {
//                            Thread.sleep(8000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        wifiInfo = wifiManager.getConnectionInfo();
                        ssid = formatSSID(wifiInfo.getSSID());
                        if(ssid.equals(SSID)){
                            break;
                        }

                    }else if(ssid.equals(SSID)){
                        break;
                    }
                }
//                setSsidAndPassword(ConnectNode.this,NameNode.getText().toString(),PassWord.getText().toString());
                Intent intent1 = new Intent(ConnectNode.this,ListWifiForMCU.class);
                startActivity(intent1);
                return false;
            }
        });

    }

    private WifiConfiguration createAPConfiguration(String networkSSID, String networkPasskey, String securityMode) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
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


    public static boolean setSsidAndPassword(Context context, String ssid, String ssidPassword) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

            wifiConfig.SSID = ssid;
            wifiConfig.preSharedKey = ssidPassword;

            Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
//    public int connectToAP(String networkSSID, String networkPasskey) {
//        for (ScanResult result : scanResultList) {
//
//            if (result.SSID.equals(networkSSID)) {
//
//                String securityMode = getScanResultSecurity(result);
//
//                WifiConfiguration wifiConfiguration = createAPConfiguration(networkSSID, networkPasskey, securityMode);
//
//                int res = wifiManager.addNetwork(wifiConfiguration);
//                Log.d(TAG, "# addNetwork returned " + res);
//
//                boolean b = wifiManager.enableNetwork(res, true);
//                Log.d(TAG, "# enableNetwork returned " + b);
//
//                wifiManager.setWifiEnabled(true);
//
//                boolean changeHappen = wifiManager.saveConfiguration();
//
//                if (res != -1 && changeHappen) {
//                    Log.d(TAG, "# Change happen");
//                    connectedSsidName = networkSSID;
//                } else {
//                    Log.d(TAG, "# Change NOT happen");
//                }
//
//                return res;
//            }
//        }
//
//        return -1;
//    }
}
