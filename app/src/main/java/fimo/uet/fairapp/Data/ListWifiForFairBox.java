package fimo.uet.fairapp.Data;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HP on 5/1/2017.
 */

public class ListWifiForFairBox {
    String JsonResult = null;
    Context context;


    public void GetListWifi(ArrayList<String> ListWifi){
        RequestToServer();
        while (JsonResult==null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JSONObject Data = null;
        try {
            Data = new JSONObject(JsonResult);
            JSONArray ListSSID = Data.getJSONArray("ssid");
            for(int i=0;i<ListSSID.length();i++){
                JSONObject Wifi = ListSSID.getJSONObject(i);
                String SSID = Wifi.getString("ssid");
                ListWifi.add(SSID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void RequestToServer(){
        final String url = "http://192.168.4.1/scan";

        final OkHttpClient client = new OkHttpClient();

       Thread t1 = new Thread(new Runnable() {
           @Override
           public void run() {
               Request request = new Request.Builder()
                       .header("Authorization","Basic !fimo@54321")
                       .url(url)
                       .build();
               try {
                   Response response = client.newCall(request).execute();
                   JsonResult = response.body().string();


               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
