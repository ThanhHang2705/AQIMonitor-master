package fimo.uet.fairapp.Data;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HP on 5/1/2017.
 */

public class GetIDFromFairBox {
    String JsonResult = null;

    public String GetID(){
        String ID = null;
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
            ID = Data.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ID;
    }

    public void RequestToServer(){
        final String url = "http://192.168.4.1/info";

        final OkHttpClient client = new OkHttpClient();

        new Thread(new Runnable() {
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
        }).start();

    }
}
