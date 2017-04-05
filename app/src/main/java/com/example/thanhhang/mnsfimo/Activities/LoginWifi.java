package com.example.thanhhang.mnsfimo.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by HP on 3/28/2017.
 */

public class LoginWifi extends AlertDialog{
    String ssid;
    TextView SSID;
    EditText PassWord;
    CheckBox ShowPassWord;
    Button KetNoi,Huy;
    public LoginWifi(final Context context, String ssid) {
        super(context);
        this.ssid=ssid;
//        WindowManager.LayoutParams params = getContext().getWindow().getAttributes();
//        params.gravity = Gravity.TOP;
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final AlertDialog alertDialog = alertadd.create();
        final View view = factory.inflate(R.layout.login_dialog, null);
        SSID = (TextView)view.findViewById(R.id.ssid);
        PassWord = (EditText)view.findViewById(R.id.pass_word);
        ShowPassWord = (CheckBox)view.findViewById(R.id.show_pass_word);
        KetNoi = (Button)view.findViewById(R.id.btx_ket_noi);
        Huy = (Button)view.findViewById(R.id.btx_huy);
        SSID.setText(ssid);
        PassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        KetNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                connectWifi();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//
                Intent intent1 = new Intent(context,ListWifiForMCU.class);
                context.startActivity(intent1);

            }
        });
        Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        PassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });
        ShowPassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    PassWord.setSelection(PassWord.getText().length());
                }else{
                    PassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PassWord.setSelection(PassWord.getText().length());
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }


    public void connectWifi(){
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = formatSSID(wifiInfo.getSSID());

        for(;;){
            if(!ssid.equals(this.ssid)){
                wifiManager.disconnect();
                try {
                    String s= this.ssid;
                    String pass_word = PassWord.getText().toString();
                    createAPConfiguration(s,pass_word,"WPA");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                while(!wifi.isConnected()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
                    wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                }
//                        try {
//                            Thread.sleep(8000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                wifiInfo = wifiManager.getConnectionInfo();
                ssid = formatSSID(wifiInfo.getSSID());
                if(ssid.equals(this.ssid)){
                    break;
                }

            }else if(ssid.equals(this.ssid)){
                break;
            }
        }
    }

    private WifiConfiguration createAPConfiguration(String networkSSID, String networkPasskey, String securityMode) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getContext().getSystemService(getContext().WIFI_SERVICE);
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
