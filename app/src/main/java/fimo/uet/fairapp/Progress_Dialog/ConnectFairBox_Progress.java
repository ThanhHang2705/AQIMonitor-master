package fimo.uet.fairapp.Progress_Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import fimo.uet.fairapp.Activities.ListWifiForMCU;
import fimo.uet.fairapp.R;
import fimo.uet.fairapp.WifiConfig.Wifi_Configuration;

/**
 * Created by HP on 5/2/2017.
 */

public class ConnectFairBox_Progress extends AsyncTask<Integer,Integer,Integer>{
    private ProgressDialog dialog;
    /** application context. */
    private Activity activity;
    String SSID;
    String PassWord;
    Bundle bundle;
    public ConnectFairBox_Progress(Activity activity, String SSID, String PassWord, Bundle bundle) {
        this.activity = activity;
        this.SSID = SSID;
        this.PassWord = PassWord;
        this.bundle = bundle;
        dialog = new ProgressDialog(activity);

    }
    @Override
    protected Integer doInBackground(Integer... params) {
        Wifi_Configuration wifi_configuration = new Wifi_Configuration(activity ,SSID, PassWord);
        wifi_configuration.connectWifi();
        return 1;
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(activity.getString(R.string.device_connecting));
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer==1){
            dialog.dismiss();
            Intent intent1 = new Intent(activity,ListWifiForMCU.class);
            intent1.putExtra("LatLng",bundle);
            activity.startActivity(intent1);
            activity.finish();
        }
    }
}
