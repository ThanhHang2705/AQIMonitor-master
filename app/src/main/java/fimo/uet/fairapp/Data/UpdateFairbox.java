package fimo.uet.fairapp.Data;

import android.content.Context;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HP on 5/1/2017.
 */

public class UpdateFairbox {
    String Body;
    int ResponseCode = 0;
    Context context;
    static String pID="";
    public UpdateFairbox(Context context, String body){
        this.Body = body;
        this.context = context;
    }

    public int UpdateData(){
        RequestToServer();
//        while (ResponseCode==0){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        return ResponseCode;
    }

    public void ConfigFairBox(String url){
        final OkHttpClient client = new OkHttpClient();
        final String My_URL = url;

                Request request = new Request.Builder()
                        .header("Authorization","Basic !fimo@54321")
                        .url(My_URL)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ResponseCode = response.code();
                    try {
                        JSONObject PID = new JSONObject(response.body().string());
                        pID = PID.getString("pID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

    }

    public void RequestToServer() {
        ResponseCode = 0;
        final String url = "http://118.70.72.15:5902/API/v1/sensors";
        final OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


                RequestBody body = RequestBody.create(JSON, Body);
                Request request = new Request.Builder()
                        .addHeader("Authorization","Basic !fimo@54321")
                        .addHeader("Content-Type", "application/json")
                        .put(body)
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String s = response.body().string();
                    ResponseCode = response.code();


                } catch (IOException e) {
                    e.printStackTrace();
                }


    }

    public Integer Register(){
        ResponseCode = 0;
        final String url = "http://118.70.72.15:5902/api/v1/observations";
        final OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        String str_body = "{\n" +
                "  \"pID\": \""+pID+"\",\n" +
                "  \"aqi\": {\n" +
                "    \"t\": {\n" +
                "      \"v\": 24\n" +
                "    },\n" +
                "    \"h\": {\n" +
                "      \"v\": 70\n" +
                "    },\n" +
                "    \"pm25\": {\n" +
                "      \"v\": 80\n" +
                "    },\n" +
                "    \"pm10\": {\n" +
                "      \"v\": 70\n" +
                "    },\n" +
                "    \"pm1\": {\n" +
                "      \"v\": 20\n" +
                "    }\n" +
                "  }\n" +
                "}";
        RequestBody body = RequestBody.create(JSON, str_body);
        Request request = new Request.Builder()
                .addHeader("Authorization","Basic !fimo@54321")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            ResponseCode = response.code();


        } catch (IOException e) {
            e.printStackTrace();
        }

        while (ResponseCode==0){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ResponseCode;
    }
}
