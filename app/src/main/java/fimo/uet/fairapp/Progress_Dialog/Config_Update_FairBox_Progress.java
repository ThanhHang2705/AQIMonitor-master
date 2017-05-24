package fimo.uet.fairapp.Progress_Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import fimo.uet.fairapp.R;

import fimo.uet.fairapp.Data.UpdateFairbox;

/**
 * Created by HP on 5/3/2017.
 */

public class Config_Update_FairBox_Progress extends AsyncTask<Integer,Integer,Integer> {
    private ProgressDialog dialog;
    /** application context. */
    private Activity activity;
    String body;
    String SSID;
    String PassWord;

    public Config_Update_FairBox_Progress(Activity activity, String SSID, String PassWord, String body){
        this.activity = activity;
        this.body = body;
        this.SSID = SSID;
        this.PassWord = PassWord;
        dialog = new ProgressDialog(activity);
    }


    @Override
    protected Integer doInBackground(Integer... params) {
        final String url = "http://192.168.4.1/config?ssid="+SSID+"&pwd="+PassWord;
        final UpdateFairbox updateFairbox = new UpdateFairbox(activity,body);
        updateFairbox.ConfigFairBox(url);
//        Wifi_Configuration wifi_configuration = new Wifi_Configuration(context,SSID, PassWord);
//        wifi_configuration.connectWifi();
        while (!isConnected()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return updateFairbox.UpdateData();
//        return updateFairbox.Register();
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(activity.getString(R.string.device_connecting));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        dialog.dismiss();
        if(integer>=200 && integer<300){
//            Toast.makeText(context,"Thiết bị đã kích hoạt thành công", Toast.LENGTH_LONG).show();
            CreateDialog(activity, "Thiết bị đã kích hoạt thành công");
        }else if(integer==500){
//            Toast.makeText(context,"Thiết bị chưa được kích hoạt", Toast.LENGTH_LONG).show();
            CreateDialog(activity,"Có lỗi xảy ra trên FAirServer");
        }else{
            CreateDialog(activity,"Thiết bị chưa được kích hoạt");
        }
//        activity.finish();
    }




    public void CreateDialog(final Activity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(context, "Không thoát được", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
